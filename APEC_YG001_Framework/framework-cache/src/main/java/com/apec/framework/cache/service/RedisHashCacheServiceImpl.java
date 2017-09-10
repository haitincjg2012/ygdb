package com.apec.framework.cache.service;

import com.apec.framework.cache.CacheException;
import com.apec.framework.cache.CacheHashService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Title: Hash 类型的缓存
 *
 * @author yirde  2017/7/17.
 */
@Service("cacheHashService")
public class RedisHashCacheServiceImpl implements CacheHashService {

    private final static  Logger logger = LoggerFactory.getLogger(RedisHashCacheServiceImpl.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public <T> void hmset(String key, Map<String, T> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
        }
    }

    @Override
    public void hset(String key, String field, String value) {
        try {
            redisTemplate.opsForHash().put(key, field, value);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
        }
    }

    @Override
    public Long hinc(String key, String field, long value) {
        try {
            return redisTemplate.opsForHash().increment(key,field,value);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
        }
    }

    @Override
    public void hdelField(String key, String... field){
        try {
            BoundHashOperations<String, String, ?> boundHashOperations = redisTemplate.boundHashOps(key);
            boundHashOperations.delete(field);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
        }
    }

    @Override
    public void expire(String key, long expireTime) {
        try {
            if(expireTime > 0){
                redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
        }
    }

    @Override
    public <T> T hget(String key, String field) {
        try {
            BoundHashOperations<String, String, T> boundHashOperations = redisTemplate.boundHashOps(key);
            return boundHashOperations.get(field);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
        }
    }

    @Override
    public <T> Map<String, T> hgetAll(String key) {
        try{
            BoundHashOperations<String, String, T> boundHashOperations = redisTemplate.boundHashOps(key);
            return boundHashOperations.entries();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
        }
    }

    @Override
    public void del(String... key) {
        try{
            if(key!=null && key.length > 0){
                if(key.length == 1){
                    redisTemplate.delete(key[0]);
                }else{
                    redisTemplate.delete(CollectionUtils.arrayToList(key));
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
        }
    }



}
