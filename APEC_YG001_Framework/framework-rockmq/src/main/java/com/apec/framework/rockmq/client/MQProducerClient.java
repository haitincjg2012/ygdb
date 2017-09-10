package com.apec.framework.rockmq.client;

import com.apec.framework.common.enumtype.MQSendStatus;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.rockmq.producer.ApecMQProducer;
import com.apec.framework.rockmq.vo.IMQBody;
import com.apec.framework.vo.MQMessageLogVO;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * Title: 生产者客户端
 *
 * @author yirde  2017/7/4.
 */
@Component
public class MQProducerClient {

    @Autowired
    ApecMQProducer apecMQProducer;

    @Autowired
    RemotingClient remotingClient;

    @InjectLogger
    private Logger logger;

    /**
     * 发送普通消息
     * @param tag Tag
     * @param key key
     * @param body 消息体
     */
    public void sendConcurrently(String tag, String key,  IMQBody body) {
        MQMessageLogVO mqMessageLogVO = apecMQProducer.sendConcurrently(tag,key,body);
        //消息发送失败，消息落地，落地失败记录日志
        if(mqMessageLogVO.getStatus() == MQSendStatus.FAILURE){
            remotingClient.sendMqProducerFailLog(mqMessageLogVO);
        }
    }

    /**
     * 发送普通消息
     * @param tag Tag
     * @param key key
     * @param body 消息体
     */
    public void sendConcurrently(String tag, String key,  String body) {
        MQMessageLogVO mqMessageLogVO = apecMQProducer.sendConcurrently(tag,key,body);
        //消息发送失败，消息落地，落地失败记录日志
        if(mqMessageLogVO.getStatus() == MQSendStatus.FAILURE){
            remotingClient.sendMqProducerFailLog(mqMessageLogVO);
        }
    }

    /**
     * 发送顺序消息
     * @param tag Tag
     * @param key key
     * @param body 消息体
     * @param orderKey 排序字段
     */
    public  void sendConcurrentlyByKey(String tag, String key,  IMQBody body, String orderKey) {
        MQMessageLogVO mqMessageLogVO = apecMQProducer.sendConcurrentlyByKey(tag,key,body,orderKey);
        //消息发送失败，消息落地，落地失败记录日志
        if(mqMessageLogVO.getStatus() == MQSendStatus.FAILURE){
            remotingClient.sendMqProducerFailLog(mqMessageLogVO);
        }
    }


    /**
     *
     * Release Resources
     */
    @PreDestroy
    public void releaseRestClient(){
        logger.info("=========================Release the ApecRocketMQ Producer configuration =========================");
        try {
            apecMQProducer.shutdown();
            logger.info("=========================Release the ApecRocketMQ Producer Success =========================");
        }catch (Exception ex){
            logger.error("=========================Release the ApecRocketMQ Producer Failed =========================",ex);
        }
    }

}
