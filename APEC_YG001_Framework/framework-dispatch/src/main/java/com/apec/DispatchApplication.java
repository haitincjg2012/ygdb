package com.apec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.apec.framework.base.BaseApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.TimeUnit;

/**
 * @author xx
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
public class DispatchApplication extends BaseApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(DispatchApplication.class, args);
    }

    @Bean
    EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
        return (ConfigurableEmbeddedServletContainer container) -> {
            container.setSessionTimeout(100, TimeUnit.MINUTES);
        };
    }

}
