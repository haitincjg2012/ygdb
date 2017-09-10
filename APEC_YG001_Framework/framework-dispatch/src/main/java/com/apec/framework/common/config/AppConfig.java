package com.apec.framework.common.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.apec.framework.common.Constants;

@Configuration
public class AppConfig
{
    @Bean
    public Filter characterEncodingFilter()
    {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding(Constants.SYSTEM_ENCODING);
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate()
    {
        RestTemplate restTemplate = new RestTemplate();
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Constants.SYSTEM_CHARSET);
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }

}
