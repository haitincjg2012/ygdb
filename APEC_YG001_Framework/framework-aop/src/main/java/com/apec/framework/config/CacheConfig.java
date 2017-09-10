package com.apec.framework.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by wjw on 2016/11/15.
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis.cluster")
public class CacheConfig {

   List<String> nodes;

    public List<String> getNodes() {
        return nodes;
    }
    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory());
        template.setEnableTransactionSupport(true);
        return template;
    }
    @Bean(name="redisTemplate")
    public RedisTemplate<String,Object> redisTemplate() {
        RedisTemplate<String,Object> template = new RedisTemplate<String,Object>();
        RedisConnectionFactory redisConnectionFactory = redisConnectionFactory();
        template.setConnectionFactory(redisConnectionFactory);
        template.setEnableTransactionSupport(false);
        StringRedisSerializer serializer = new StringRedisSerializer();
        template.setKeySerializer(serializer);
        return template;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new JedisConnectionFactory(
                new RedisClusterConfiguration(getNodes()));
    }
}
