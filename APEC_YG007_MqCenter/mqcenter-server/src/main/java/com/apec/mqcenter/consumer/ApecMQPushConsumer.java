package com.apec.mqcenter.consumer;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.apec.framework.common.Constants;
import com.apec.framework.common.enums.Enums;
import com.apec.framework.common.enums.MqTag;
import com.apec.framework.common.enumtype.MQConsumerStatus;
import com.apec.framework.common.enumtype.MailType;
import com.apec.framework.mail.service.MailService;
import com.apec.framework.mail.vo.Mail;
import com.apec.mqcenter.client.RemotingClient;
import com.apec.mqcenter.service.MqCenterService;
import com.apec.mqcenter.vo.MQMessageConsumerLogVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Title: 消息消费端
 *
 * @author yirde  2017/7/4.
 */
public class ApecMQPushConsumer extends DefaultMQPushConsumer {

    private final Logger logger = LoggerFactory.getLogger(ApecMQPushConsumer.class);

    @Autowired
    private MqCenterService mqCenterService;

    @Autowired
    private RemotingClient remotingClient;

    @Autowired
    private MailService mailService;

    /*
     * 重试次数
     */
    private int retryNum;

    @Override
    public void start() throws MQClientException {

        super.subscribe(Constants.DEFAULT_ROCKETMQ_TOPIC,"*");

        super.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        super.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                Assert.isTrue(msgs.size() == 1, "默认每次只接受一个任务，如果超过1个，可能参数设置错误");

                MessageExt msg = msgs.get(0);
                MQMessageConsumerLogVO mqMessageConsumerLogVO = null;
                try{
                    long executeTime = System.currentTimeMillis();
                    logger.info("[ConsumeMessage][msgId({}) Tags({})keys({})]", msg.getMsgId(), msg.getTags(),msg.getKeys());
                    //TODO Redis Lock 排除并发重复消费问题 暂不解决，并发消息消费重复可能极低
                    //消息重复消费过滤,查询消息消费表的数据，如果已经存在，则不再操作
                    if(mqCenterService.findMessageConsumerIsExit(msg.getTags(),msg.getKeys())){
                        logger.warn("[ConsumeMessage][msgKey:{}],The message is repeated and not processed !",msg.getKeys());
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    String body = org.apache.commons.codec.binary.StringUtils.newStringUtf8(msg.getBody());
                    mqMessageConsumerLogVO = new MQMessageConsumerLogVO(msg.getTopic(),msg.getTags(),
                            msg.getKeys(),msg.getMsgId(),body,executeTime);
                    //实际调用
                    remotingClient.sendMqConsumerExecute(body, Enums.getEnumByKey(MqTag.class,msg.getTags()).getTagUrl());

                    try {
                         mqMessageConsumerLogVO.setConsumerEndTime(System.currentTimeMillis());
                         mqMessageConsumerLogVO.setStatus(MQConsumerStatus.SUCCESS);
                         mqCenterService.addMessageConsumer(mqMessageConsumerLogVO);
                    }catch (Exception exMq){
                        logger.error("[ConsumeMessage Error]Success Message save failed! ",exMq);
                        //TODO 落地失败，发送微信、邮件报告
                        sendMqMail(msg);
                    }
                }catch (Exception ex){
                    logger.error("[ConsumeMessage Error] ",ex);
                    if(msg.getReconsumeTimes() == getRetryNum()){
                        if(mqMessageConsumerLogVO != null) {
                            try {
                                mqMessageConsumerLogVO.setConsumerEndTime(System.currentTimeMillis());
                                mqMessageConsumerLogVO.setStatus(MQConsumerStatus.FAILURE);
                                mqCenterService.addMessageConsumer(mqMessageConsumerLogVO);
                            }catch (Exception exMq){
                                logger.error("[ConsumeMessage Error]ErrorMessage save failed! ",exMq);
                                //TODO 落地失败，发送微信、邮件报告
                                sendMqMail(msg);
                            }
                        }
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        super.start();
    }

    private void sendMqMail(MessageExt msg) {
        Mail mail = new Mail();
        String subject = "消息消费失败["+msg.getMsgId()+"]";
        String content = "message send failed: [msgId(%s) Tags(%s) keys(%s)]";
        mail.setSubject(subject);
        mail.setType(MailType.TEXT);
        mail.setContent(String.format(content, msg.getMsgId(), msg.getTags(),msg.getKeys()));
        try {
            mailService.sendMail(mail, true);
        } catch (Exception e) {
            logger.info("mail send failed :", e.getMessage());
        }
    }

    private int getRetryNum() {
        return retryNum;
    }

    public void setRetryNum(int retryNum) {
        this.retryNum = retryNum;
    }
}
