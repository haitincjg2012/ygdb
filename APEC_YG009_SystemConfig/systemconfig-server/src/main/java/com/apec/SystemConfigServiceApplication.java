package com.apec;

import com.apec.framework.base.BaseApplication;
import com.apec.framework.springcloud.SpringCloudConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@EnableDiscoveryClient
@SpringBootApplication
@Import(value = SpringCloudConfig.class)
public class SystemConfigServiceApplication extends BaseApplication
{
    public static void main(String[] args)
    {
        new SpringApplicationBuilder(SystemConfigServiceApplication.class).web(true).run(args);
    }
}
