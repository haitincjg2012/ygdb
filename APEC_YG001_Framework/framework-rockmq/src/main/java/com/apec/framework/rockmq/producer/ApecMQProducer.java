package com.apec.framework.rockmq.producer;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import com.alibaba.rocketmq.common.message.Message;
import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.MQSendStatus;
import com.apec.framework.rockmq.vo.IMQBody;
import com.apec.framework.vo.MQMessageLogVO;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;

/**
 * Title:消息生产者
 *
 * @author yirde  2017/6/30.
 */
public class ApecMQProducer extends DefaultMQProducer {

    private Logger logger = LoggerFactory.getLogger(ApecMQProducer.class);

    private static SelectMessageQueueByHash SELECT_MESSAGE_QUEUE_BY_HASH = new SelectMessageQueueByHash();

    @Override
    public String buildMQClientId() {
        String clientId = super.buildMQClientId();
        return clientId + "[Producer]";
    }

    @Override
    public void setInstanceName(String instanceName) {
        instanceName = instanceName + "[" + ManagementFactory.getRuntimeMXBean().getName() + "]";
        super.setInstanceName(instanceName);
    }

    /**
     * 使用orderKey排序发送消息
     * @param tag tag
     * @param key hash key
     * @param body 消息体
     * @param orderKey 排序key
     */
    public MQMessageLogVO sendConcurrentlyByKey(String tag, String key,  IMQBody body, String orderKey) {
        MQMessageLogVO sendLog = new MQMessageLogVO(Constants.DEFAULT_ROCKETMQ_TOPIC,tag,key,JSON.toJSONString(body),orderKey);
        if (orderKey == null) {
            throw new IllegalArgumentException(String.format("[tag:%s][key:%s][body:%s]{orderKey can't be null!}",
                      tag, key,  JSON.toJSONString(body)));
        }

        return this.send(sendLog, tag, key, JSON.toJSONBytes(body), orderKey);
    }

    /**
     * 使用orderKey排序发送消息
     * @param tag tag
     * @param key hash key
     * @param body 消息体
     * @param orderKey 排序key
     */
    public MQMessageLogVO sendConcurrentlyByKey(String tag, String key,  String body, String orderKey) {
        MQMessageLogVO sendLog = new MQMessageLogVO(Constants.DEFAULT_ROCKETMQ_TOPIC,tag,key,body,orderKey);
        if (orderKey == null) {
            throw new IllegalArgumentException(String.format("[tag:%s][key:%s][body:%s]{orderKey can't be null!}",
                    tag, key,  JSON.toJSONString(body)));
        }

        return this.send(sendLog, tag, key, JSON.toJSONBytes(body), orderKey);
    }

    /**
     * 发送消息
     *
     * @param tag tag
     * @param key hash key
     * @param body 消息体
     */
    public MQMessageLogVO sendConcurrently(String tag, String key,  IMQBody body) {
        MQMessageLogVO sendLog = new MQMessageLogVO(Constants.DEFAULT_ROCKETMQ_TOPIC,tag,key,JSON.toJSONString(body),null);
        return this.send(sendLog, tag, key,JSON.toJSONBytes(body), null);
    }
    /**
     * 发送消息
     *
     * @param tag tag
     * @param key hash key
     * @param body 消息体
     */
    public MQMessageLogVO sendConcurrently(String tag, String key,  String body) {
        MQMessageLogVO sendLog = new MQMessageLogVO(Constants.DEFAULT_ROCKETMQ_TOPIC,tag,key,JSON.toJSONString(body),null);
        return this.send(sendLog,tag, key,  JSON.toJSONBytes(body), null);
    }

    /**
     * 基础发送消息方法
     * @param tag tag
     * @param key hash key
     * @param body 消息体
     * @param orderlyKey 排序key。为空时，代表不需要排序
     */
    private MQMessageLogVO send(MQMessageLogVO sendLog,String tag, String key, byte[] body, String orderlyKey) {
        try {
            Message msg = new Message(Constants.DEFAULT_ROCKETMQ_TOPIC, tag, key, body);
            sendLog.setSendTime(System.currentTimeMillis());
            SendResult result;
            if (orderlyKey == null) { // 并发发送
                result = super.send(msg); // 发送
            } else {
                result = super.send(msg, SELECT_MESSAGE_QUEUE_BY_HASH, orderlyKey);
            }
            sendLog.setResult(String.valueOf(result));
            sendLog.setSendEndTime(System.currentTimeMillis());
            sendLog.setStatus(MQSendStatus.SUCCESS);
            if (result != null) {
                logger.info("[MQ Message Send][tag：{} ,key:{}，message: {} .send  status :{}!]",
                                                              tag,key,result.getMsgId(), result.getSendStatus().name());
            }
        } catch (Exception e) {
            sendLog.setSendEndTime(System.currentTimeMillis());
            sendLog.setStatus(MQSendStatus.FAILURE);
            sendLog.setSendException(ExceptionUtils.getStackTrace(e));
            logger.error("[MQ Message Send][tag：{} ,key:{},send failure {}]", tag,key, ExceptionUtils.getStackTrace(e));
        }
        return sendLog;
    }

//
//    private byte[] getBodyBytes(MQMessageSendLog sendLog) {
//        Map<String, Object> body = sendLog.getBody();
//        if (body != null) {
//            return JSON.toJSONBytes(body);
//        }
//        String bodyString = (String) sendLog.getMessage().get("body");
//        if (bodyString == null) {
//            throw new IllegalArgumentException(String.format("msg(%s) body is error", JSON.toJSONString(sendLog)));
//        }
//        return StringUtils.getBytesUtf8(bodyString);
//    }

    /**
     * 根据发送日志，对发送失败的消息进行重发
     *
     * @param sendLog 发送日志
     */
//    public void retrySend(MQMessageSendLog sendLog) {
//        Long retryTime = System.currentTimeMillis();
//        try {
//            @SuppressWarnings("unchecked") Map<String, String> msgProperties = (Map<String, String>) sendLog.getMessage().get("properties");
//            Message msg = new Message((String) sendLog.getMessage().get("topic"),
//                    msgProperties.get(MessageConst.PROPERTY_TAGS),
//                    msgProperties.get(MessageConst.PROPERTY_KEYS),
//                    (Integer) sendLog.getMessage().get("flag"),
//                    getBodyBytes(sendLog),
//                    Boolean.valueOf(msgProperties.get(MessageConst.PROPERTY_WAIT_STORE_MSG_OK)));
//            SendResult result;
//            if (sendLog.getOrderlyKey() != null) {
//                result = super.send(msg, SELECT_MESSAGE_QUEUE_BY_HASH, sendLog.getOrderlyKey());
//            } else {
//                result = super.send(msg);
//            }
//            mongoOperations.updateFirst(new Query(Criteria.where("_id").is(sendLog.getId())), Update.update("result", result)
//                    .set("retryTime", retryTime).set("retryEndTime", System.currentTimeMillis())
//                    .set("status", MQMessageSendLog.STATUS_SEND_SUCCESS), MQMessageSendLog.class);
//        } catch (Exception e) {
//            mongoOperations.updateFirst(new Query(Criteria.where("_id").is(sendLog.getId())), new Update()
//                    .set("retryTime", retryTime).set("retryEndTime", System.currentTimeMillis())
//                    .set("retryException", ExceptionUtils.getStackTrace(e)), MQMessageSendLog.class);
//        }
//
//    }
}