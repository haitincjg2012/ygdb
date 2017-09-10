package com.apec.framework.mq;

import com.apec.framework.common.enumtype.RoutingKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;

/**
 * 类 编 号：
 * 类 名 称：
 * 内容摘要：
 * 创建日期：2017/1/13 15:04
 * 编码作者：zhaolei
 */
public class AmqpConfig {
    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private String port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host}")
    private String vhost;

    private static Log log = LogFactory.getLog(AmqpConfig.class);

    public static RoutingKey key;

    public static RoutingKey[] keyArray;




    @Bean
    ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setAddresses(host + ":" + port);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        cachingConnectionFactory.setVirtualHost(vhost);
        cachingConnectionFactory.setPublisherConfirms(true);
        return cachingConnectionFactory;
    }

    @Bean
    RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(messageConverter());
        return template;
    }


    @Bean
    RabbitAdmin rabbitAdmin() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
        return rabbitAdmin;
    }

    @Bean
    public Queue defaultQueue() {
        if (null != key){
            log.info(" *** AmqpConfig开始创建队列："+key.name());
            Queue queue = new Queue(key.name(),true);
            rabbitAdmin().declareQueue(queue);
            return queue;
        }else {
            return new Queue(RoutingKey.TEST.getName());
        }
    }

    @Bean
    public QueueFactory queueFactory(){
        return new QueueFactory(rabbitAdmin(),getKeyArray());
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    public static void setKey(RoutingKey key) {
        AmqpConfig.key = key;
    }

    public static void setKeyArray(RoutingKey[] keyArray) {
        AmqpConfig.keyArray = keyArray;
    }

    public static RoutingKey[] getKeyArray() {
        return keyArray;
    }
}
