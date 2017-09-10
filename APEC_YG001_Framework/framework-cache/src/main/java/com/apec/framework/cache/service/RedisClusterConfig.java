//package com.apec.framework.cache.service;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import redis.clients.jedis.HostAndPort;
//import redis.clients.jedis.JedisCluster;
//import redis.clients.jedis.JedisPoolConfig;
//
//@Configuration
//@PropertySource(value = "classpath:/redis.properties")
//public class RedisClusterConfig
//{
//    @Value("${redis.host}")
//    private String host;
//    @Value("${redis.port}")
//    private int port;
//    @Value("${redis.password}")
//    private String password;
//    @Value("${redis.database}")
//    private String dbindex;
//
//    @Value("${spring.redis.host}")
//    private String host1;
//    @Value("${spring.redis.port}")
//    private int port1;
//    @Value("${spring.redis.host2}")
//    private String host2;
//    @Value("${spring.redis.port2}")
//    private int port2;
//    @Value("${spring.redis.timeout}")
//    private int timeout;
//    @Value("${spring.redis.maxRedirections}")
//    private int maxRedirections;
//    @Bean
//    public JedisPoolConfig jedisPoolConfig(){
//        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMaxIdle(8);
//        config.setMaxIdle(8);
//        config.setMaxWaitMillis(-1);
//        config.setTestOnBorrow(false);
//        config.setTestWhileIdle(false);
//        return config;
//    }
//    @Bean
//    public HostAndPort hostAndPort(String host,int port){
//        HostAndPort hostPort = new HostAndPort(host, port);
//        return hostPort;
//    }
//    @Bean
//    public JedisCluster jedisCluster(){
//        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
//        jedisClusterNodes.add(hostAndPort(host1, port1));
//        jedisClusterNodes.add(hostAndPort(host2, port2));
//        JedisCluster jedisCluster = new JedisCluster(jedisClusterNodes, timeout, maxRedirections, jedisPoolConfig());
//        return jedisCluster;
//    }
//
//    @Bean
//    public JedisConnectionFactory redisConnectionFactory() {
//        JedisConnectionFactory factory = new JedisConnectionFactory();
//    	factory.setHostName(host);
//    	factory.setPassword(password);
//    	factory.setPort(port);
//    	factory.setPoolConfig(jedisPoolConfig());
//    	factory.setTimeout(timeout);
//        factory.setDatabase(Integer.valueOf(dbindex));
//        return factory;
//    }
//    
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate() {
//        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
//        template.setConnectionFactory(redisConnectionFactory());
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new JdkSerializationRedisSerializer());
//        return template;
//    }
//}
