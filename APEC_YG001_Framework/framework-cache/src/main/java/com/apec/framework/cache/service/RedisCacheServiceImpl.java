package com.apec.framework.cache.service;


import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.apec.framework.cache.CacheException;
import com.apec.framework.cache.CacheService;

/**
 * @author xxx
 *
 */
@Service("cacheService")
public class RedisCacheServiceImpl implements CacheService
{
	private final static Logger logger = Logger.getLogger(RedisCacheServiceImpl.class);

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 批量删除对应的value
	 * 
	 * @param keys keys
	 */
	public void remove(final String... keys)
	{
		for (String key : keys)
		{
			removeObject(key);
		}
	}

	/**
	 * 批量删除key
	 * 
	 * @param pattern pattern
	 */
	public void removePattern(final String pattern)
	{
		Set<String> keys = redisTemplate.keys(pattern);
		if (keys.size() > 0){
			redisTemplate.delete(keys);
		}

	}

	/**
	 * 删除对应的value
	 * 
	 * @param key key
	 */
	private void removeObject(final String key)
	{
		if (exists(key))
		{
			redisTemplate.delete(key);
		}
	}

	/**
	 * 判断缓存中是否有对应的value
	 * 
	 * @param key key
	 */
	@Override
	public boolean exists(final String key)
	{
		return redisTemplate.hasKey(key);
	}

	/**
	 * 写入缓存
	 * 
	 * @param key key
	 * @param value value
	 * @return result result
	 */
	public boolean set(final String key, String value)
	{
		boolean result = false;
		try
		{
			ValueOperations<String, String> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			result = true;
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 写入缓存
	 * 
	 * @param key key
	 * @param value value
	 * @return result result
	 */
	public boolean set(final String key, String value, Long expireTime)
	{
		boolean result = false;
		try
		{
			ValueOperations<String, String> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	@Override
	public Long increment(final String key, Long value) throws CacheException
	{
		Long result;
		try
		{
			result = redisTemplate.opsForValue().increment(key, value);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
		return result;
	}

	@Override
	public Long getIncrValue(String key) throws CacheException
	{
		Long result;
		try
		{
			result = redisTemplate.opsForValue().increment(key, 0L);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
		return result;
	}
	

	@Override
	public long getCountLike(String keyPrefix) throws CacheException
	{
		try
		{
			if (StringUtils.isEmpty(keyPrefix))
			{
				return 0;
			} else
			{
				Set<String> matchedCacheKeys = redisTemplate.keys(keyPrefix + "*");
				return matchedCacheKeys.size();
				
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
			throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
		}
	}

	@Override
	public void remove(String key) throws CacheException
	{
		try
		{
			if (exists(key))
			{
				redisTemplate.delete(key);
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
			throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
		}

	}

	@Override
	public void removeLike(String keyPrefix) throws CacheException
	{
		try
		{
			if (!StringUtils.isEmpty(keyPrefix))
			{
				Set<String> matchedCacheKeys = redisTemplate.keys(keyPrefix + "*");
				for (String cacheKey : matchedCacheKeys)
				{
					this.remove(cacheKey);
				}
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
			throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
		}
	}

	@Override
	public void expire(String key, int minutes) throws CacheException
	{
		try
		{
			redisTemplate.expire(key, minutes, TimeUnit.MINUTES);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
	}

	@Override
	public void add(String key, String value, int minutes) throws CacheException {
		try {
			ValueOperations<String, String> operations = redisTemplate.opsForValue();
			operations.set(key, value, minutes, TimeUnit.MINUTES);
		} catch (Exception e) {
			logger.info("exception e{}",e);
//			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
		
	}

	@Override
	public void addAndSubsecondExpire(String key, String value, int seconds) throws CacheException {
		try {
			ValueOperations<String, String> operations = redisTemplate.opsForValue();
			operations.set(key, value, seconds*1L);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
	}

	@Override
	public void add(String key, String value) throws CacheException {
		try {
			ValueOperations<String, String> operations = redisTemplate.opsForValue();
			operations.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
	}

	@Override
	public String get(String key) throws CacheException {
		try {
			Object object = redisTemplate.opsForValue().get(key);
			if(null!=object){
				return object.toString();
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
	}

	@Override
	public void expireSecond(String key, int second) throws CacheException {
		try
		{
			redisTemplate.expire(key, second, TimeUnit.SECONDS);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
	}
}
