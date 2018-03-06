package com.apec.framework.cache.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

/**
 * redis
 * @author  xxx
 */
@Configuration
@PropertySource(value = "classpath:/redis.properties")
public class RedisConfig {
	@Value("${redis.host}")
	private String host;
	@Value("${redis.port}")
	private int port;
	@Value("${redis.timeout}")
	private int timeout;
	@Value("${redis.password}")
	private String password;
	@Value("${redis.pool.max-active}")
	private int maxTotal;
	@Value("${redis.pool.max-idle}")
	private int maxIdle;
	@Value("${redis.pool.max-wait}")
	private long maxWaitMillis;
	
	@Bean
	JedisPoolConfig jedisPoolConfig(){
		JedisPoolConfig config = new JedisPoolConfig();
		//最大连接数, 默认8个
		config.setMaxTotal(maxTotal);
		//最大空闲连接数, 默认8个
		config.setMaxIdle(maxIdle);
		//获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
		config.setMaxWaitMillis(maxWaitMillis);
		//在获取连接的时候检查有效性, 默认false
		config.setTestOnBorrow(false);
		//在空闲时检查有效性, 默认false
		config.setTestWhileIdle(false);
		return config;
	}
	
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
    	JedisConnectionFactory factory = new JedisConnectionFactory();
    	factory.setHostName(host);
    	factory.setPassword(password);
    	factory.setPort(port);
    	factory.setPoolConfig(jedisPoolConfig());
    	factory.setTimeout(timeout);
        return factory;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new StringRedisSerializer());
        return template;
    }


}
