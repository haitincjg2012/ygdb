package com.apec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.apec.framework.base.BaseApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
public class DispatchApplication extends BaseApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(DispatchApplication.class, args);
    }
    

}
