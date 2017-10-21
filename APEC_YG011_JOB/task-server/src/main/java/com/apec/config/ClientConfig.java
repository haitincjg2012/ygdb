package com.apec.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hmy on 2017/10/12.
 */
@Configuration
public class ClientConfig {

    @Bean
    public Retryer feginRetryer(){
        Retryer retryer = new Retryer.Default(100, 10000, 1);
        return retryer;
    }

}
