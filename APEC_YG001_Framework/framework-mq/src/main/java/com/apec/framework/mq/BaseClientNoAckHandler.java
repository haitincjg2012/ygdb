package com.apec.framework.mq;

import com.apec.framework.common.enumtype.MqHandlerResult;
import com.rabbitmq.client.Channel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import java.io.IOException;

/**
 * 类 编 号：
 * 类 名 称：
 * 内容摘要：
 * 创建日期：2017-01-06 10:41
 * 编码作者：zhaolei
 */
public abstract class BaseClientNoAckHandler implements ChannelAwareMessageListener {
    private static Log log = LogFactory.getLog(BaseClientNoAckHandler.class);
    @Override
    public void onMessage(Message message, Channel channel) {
        log.info(" *** receive message:"+message);
        MqHandlerResult mqHandlerResult = MqHandlerResult.REDO;
        try {
            mqHandlerResult = handleMessage(message);
            log.info(" *** handle message result:"+mqHandlerResult);
        }catch (Exception e){
            log.error(" *** handle message exception!",e);
        }
        if(mqHandlerResult == MqHandlerResult.SUCCESS){
            try {
                log.info(" *** begin Ack message !");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                log.info(" *** Ack message finish");
            } catch (IOException e) {
                log.error(" *** Ack message exception!",e);
            }
        }else if (mqHandlerResult == MqHandlerResult.REDO){
            try {
                log.info(" *** begin REQUEUE message !");
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
                log.info(" *** REQUEUE message finish");
            } catch (IOException e) {
                log.error(" *** REQUEUE message exception!",e);
            }
        }else {
            try {
                log.info(" *** begin Reject message !");
                channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);
                log.info(" *** Reject message finish");
            } catch (IOException e) {
                log.error(" *** Reject message exception!",e);
            }
        }
    }

    public abstract MqHandlerResult handleMessage(Message message);

    public MqHandlerResult reHandler(Message message){
        boolean redelivered = message.getMessageProperties().getRedelivered();
        if (redelivered) {
            return MqHandlerResult.REJECT;
        }
        return MqHandlerResult.REDO;
    }
}
