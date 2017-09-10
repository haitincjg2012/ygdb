package com.apec.framework.common.config;

import com.apec.framework.common.filter.SessionTimeOutFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 * 类 编 号：
 * 类 名 称：WebAppConfig
 * 内容摘要：
 * 完成日期：
 * 编码作者：
 */
@Configuration
public class WebAppConfig
{
    @Value("${notAllowUrls}")
    private String[] notAllowUrls;

    @Bean
    public SessionTimeOutFilter sessionTimeOutFilter()
    {
        return new SessionTimeOutFilter();
    }

    @Bean
    public FilterRegistrationBean filterRegistration()
    {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(sessionTimeOutFilter());
        registration.addUrlPatterns(notAllowUrls);
        // registration.addInitParameter("paramName", "paramValue");
        // registration.setName("testFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //// 设置文件大小限制 ,超了，页面会抛出异常信息，这时候就需要进行异常信息的处理了;
        factory.setMaxFileSize("10MB"); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("50MB");
        //Sets the directory location where files will be stored.
        //factory.setLocation("路径地址");
        return factory.createMultipartConfig();
    }
}
