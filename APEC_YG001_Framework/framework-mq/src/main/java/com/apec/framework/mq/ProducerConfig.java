package com.apec.framework.mq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类 编 号：
 * 类 名 称：
 * 内容摘要：
 * 创建日期：2017-01-05 19:38
 * 编码作者：zhaolei
 */
@Configuration
public class ProducerConfig extends AmqpConfig {
    @Bean
    ProducerHelper producerHelper(){
        return new ProducerHelper();
    }
}
