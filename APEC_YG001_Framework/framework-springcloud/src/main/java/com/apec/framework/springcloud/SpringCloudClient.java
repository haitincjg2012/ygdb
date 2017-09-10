package com.apec.framework.springcloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhaolei
 * @create 2016-09-12 10:16
 */
@Component
public class SpringCloudClient {
    @Autowired
    private RestTemplate restTemplate;

    public String get(String serviceUrl) {
        String result = restTemplate.getForObject(serviceUrl, String.class);
        return result;
    }

    public String post(String serviceUrl,String jsonStr){
        String result = restTemplate.postForObject(serviceUrl, jsonStr, String.class);
        return result;
    }
}

