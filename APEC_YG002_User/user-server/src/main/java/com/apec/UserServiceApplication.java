package com.apec;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import com.apec.framework.base.BaseApplication;
import com.apec.framework.springcloud.SpringCloudConfig;

import java.math.BigDecimal;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@Import(value = SpringCloudConfig.class)
public class UserServiceApplication extends BaseApplication
{

    public static void main(String[] args)
    {
        new SpringApplicationBuilder(UserServiceApplication.class).web(true).run(args);
    }

}