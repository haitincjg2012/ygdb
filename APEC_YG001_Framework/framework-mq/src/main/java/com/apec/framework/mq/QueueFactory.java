package com.apec.framework.mq;

import com.apec.framework.common.enumtype.RoutingKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

/**
 * 类 编 号：
 * 类 名 称：
 * 内容摘要：
 * 创建日期：2017-01-06 15:32
 * 编码作者：zhaolei
 */
public class QueueFactory {
    private static Log log = LogFactory.getLog(ProducerConfig.class);

    public QueueFactory(RabbitAdmin rabbitAdmin,RoutingKey[] routingKeys) {
        log.info(" *** QueueFactory准备创建多队列");
        if (null != routingKeys){
            log.info(" *** QueueFactory准备创建多队列，队列个数：" + routingKeys.length);
            for (RoutingKey routingKey : routingKeys) {
                log.info(" *** QueueFactory开始创建多队列中的：" + routingKey.name());
                rabbitAdmin.declareQueue(new Queue(routingKey.name(),true));
            }
        }
    }
}
