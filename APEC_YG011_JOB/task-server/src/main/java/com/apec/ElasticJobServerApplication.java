package com.apec;

import com.apec.framework.springcloud.SpringCloudConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Import;

/**
 * Created by wubi on 2017/8/2.
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@Import(value = SpringCloudConfig.class)
public class ElasticJobServerApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ElasticJobServerApplication.class).web(true).run(args);
    }
}
