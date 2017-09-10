package com.apec.framework.springcloud;

import com.apec.framework.common.Constants;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaolei
 * @create 2016-09-13 9:15
 */
@Configuration
public class SpringCloudConfig
{
    @Bean
    @LoadBalanced
    RestTemplate restTemplate()
    {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(60000);
        requestFactory.setConnectTimeout(5000);
        
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Constants.SYSTEM_CHARSET);
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(converter);
        RestTemplate restTemplate = new RestTemplate(messageConverters);
        restTemplate.setRequestFactory(requestFactory);
        
        return restTemplate;
    }

    @Bean
    SpringCloudClient springCloudClient()
    {
        return new SpringCloudClient();
    }
}
