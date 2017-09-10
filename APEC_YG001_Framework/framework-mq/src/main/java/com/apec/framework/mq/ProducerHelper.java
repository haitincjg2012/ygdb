package com.apec.framework.mq;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 类 编 号：
 * 类 名 称：
 * 内容摘要：
 * 创建日期：2017-01-05 19:54
 * 编码作者：zhaolei
 */
@Component
public class ProducerHelper implements RabbitTemplate.ConfirmCallback {
    private static Log log = LogFactory.getLog(ProducerHelper.class);

    @Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;

    @Autowired(required = false)
    private Queue defaultQueue;

    public void send(String message){
        send(defaultQueue.getName(), message);
    }

    public void send(String keyName,String message){
        send("",keyName,message);
    }

    public void send(String exchangeName,String keyName,String message){
        rabbitMessagingTemplate.convertAndSend(exchangeName, keyName, message);
    }

    public<T> void send(String exchangeName,String keyName,T messageObject){
        rabbitMessagingTemplate.convertAndSend(exchangeName, keyName, messageObject);
    }

    /**
     * rabbit 服务器给发送者的确认，说明rabbit 收到发送者的消息了
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if(ack){
            log.info(" *** RabbitMQ接收消息成功");
        }else{
            log.info(" *** RabbitMQ接收消息失败");
        }
    }
}
