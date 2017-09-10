package com.apec;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

import com.apec.framework.base.BaseApplication;
import com.apec.framework.springcloud.SpringCloudConfig;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableDiscoveryClient
@SpringBootApplication
@EnableAsync
@Import(value = SpringCloudConfig.class)
public class ProductServiceApplication extends BaseApplication
{
    public static void main(String[] args)
    {
        new SpringApplicationBuilder(ProductServiceApplication.class).web(true).run(args);
    }
}
