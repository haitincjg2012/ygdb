package com.apec;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

import com.apec.framework.base.BaseApplication;
import com.apec.framework.springcloud.SpringCloudConfig;

/**
 * 评论模块
 * @author yirder
 */
@EnableDiscoveryClient
@SpringBootApplication
@Import(value = SpringCloudConfig.class)
public class CommentServiceApplication extends BaseApplication
{
    public static void main(String[] args)
    {
        new SpringApplicationBuilder(CommentServiceApplication.class).web(true).run(args);
    }
}
