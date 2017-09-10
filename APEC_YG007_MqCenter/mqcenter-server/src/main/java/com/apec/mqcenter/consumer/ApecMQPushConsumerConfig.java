package com.apec.mqcenter.consumer;

import com.apec.framework.log.InjectLogger;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Title:
 *
 * @author yirde  2017/7/4.
 */
@Configuration
public class ApecMQPushConsumerConfig {

    /**
     * MQ Producer Group
     */
    @Value("${rocketMQ.consumer.group}")
    private String  consumerGroup;

    /**
     * Instance Name
     */
    @Value("${rocketMQ.server.name}")
    private String instanceName;

    /**
     * Name Server Address
     */
    @Value("${rocketMQ.namesrvAddr}")
    private String namesrvAddr;

    @Value("${rocketMQ.consumer.consumeThreadMin}")
    private  int consumeThreadMin;


    @Value("${rocketMQ.consumer.consumeThreadMax}")
    private  int consumeThreadMax;

    @Value("${rocketMQ.consumer.retryNum}")
    private int retryNum;

    @InjectLogger
    private Logger logger;

    @Bean
    public ApecMQPushConsumer getApecMQPushConsumer(){
        ApecMQPushConsumer apecMQPushConsumer = new ApecMQPushConsumer();
        apecMQPushConsumer.setConsumerGroup(consumerGroup);
        apecMQPushConsumer.setInstanceName(instanceName);
        apecMQPushConsumer.setNamesrvAddr(namesrvAddr);
        apecMQPushConsumer.setConsumeThreadMin(consumeThreadMin);
        apecMQPushConsumer.setConsumeThreadMax(consumeThreadMax);
        apecMQPushConsumer.setRetryNum(retryNum);
        logger.info("=========================Initialize the RocketMQPushConsumer configuration。{}=========================",namesrvAddr);
        try {
            apecMQPushConsumer.start();
            logger.info("=========================Start RocketMQPushConsumer Success。=========================");
        }catch (Exception ex){
            logger.error("Start MQ Producer Failed! ",ex);
            logger.info("=========================Start RocketMQPushConsumer  Failed。=========================");
        }
        return apecMQPushConsumer;
    }
}
