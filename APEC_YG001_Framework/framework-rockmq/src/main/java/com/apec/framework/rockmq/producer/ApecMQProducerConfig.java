package com.apec.framework.rockmq.producer;

import com.apec.framework.common.Constants;
import com.apec.framework.log.InjectLogger;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Title:
 * @author yirde  2017/7/3.
 */
@Configuration
public class ApecMqProducerConfig {

    /**
     * MQ Producer Group
     */
    @Value("${rocketMQ.producer.group}")
    private String  producerGroup;

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

    @InjectLogger
    private Logger logger;

    @Bean
    public ApecMqProducer getApecMQProducer(){
        ApecMqProducer apecMQProducer = new ApecMqProducer();
        apecMQProducer.setNamesrvAddr(namesrvAddr);
        apecMQProducer.setProducerGroup(producerGroup);
        apecMQProducer.setCreateTopicKey(Constants.DEFAULT_ROCKETMQ_TOPIC);
        apecMQProducer.setInstanceName(instanceName);
        logger.info("=========================Initialize the RocketMQ configuration。=========================");
        try {
            apecMQProducer.start();
            logger.info("=========================Start RocketMQ Success。=========================");
        }catch (Exception ex){
            logger.error("Start MQ Producer Failed! ",ex);
            logger.info("=========================Start RocketMQ  Failed。=========================");
        }
        return apecMQProducer;
    }
}
