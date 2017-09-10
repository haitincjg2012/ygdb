package com.apec;

import com.apec.framework.springcloud.SpringCloudConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

/**
 * Created by wubi on 2017/7/31.
 */
@EnableDiscoveryClient
@SpringBootApplication
@Import(value = SpringCloudConfig.class)
public class SystemUserServiceApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SystemUserServiceApplication.class).web(true).run(args);
    }
}
