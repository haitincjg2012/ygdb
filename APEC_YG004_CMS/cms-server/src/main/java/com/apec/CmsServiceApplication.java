package com.apec;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

import com.apec.framework.base.BaseApplication;
import com.apec.framework.springcloud.SpringCloudConfig;

/**
 * @author wubi
 */
@EnableDiscoveryClient
@SpringBootApplication
@Import(value = SpringCloudConfig.class)
public class CmsServiceApplication extends BaseApplication
{
    public static void main(String[] args)
    {
        new SpringApplicationBuilder(CmsServiceApplication.class).web(true).run(args);
    }
}
