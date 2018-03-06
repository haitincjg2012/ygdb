package com.apec.framework.cache;

/**
 * 缓存组件-缓存接口
 * @author znw
 *
 */
public interface CacheService {

	/**
	 * 添加对象到缓存
	 * @param key 缓存key
	 * @param value 缓存对象
	 * @param minutes 过期时间
	 * @throws CacheException 缓存异常
	 */
	 void add(String key,String value,int minutes)throws CacheException;
	
	/**
	 * 添加对象到缓存
	 * @param key 缓存key
	 * @param value 缓存对象
	 * @throws CacheException 缓存异常
	 */
	 void add(String key,String value)throws CacheException;

	/**
	 * 添加过期时间 秒
	 * @param key 缓存Key
	 * @param value 缓存Value
	 * @param seconds 过期时间 秒
	 */
	 void addAndSubsecondExpire(String key, String value, int seconds);

	/**
	 * 获取缓存对象
	 * @param key 缓存key
	 * @return String 缓存的值
	 * @throws CacheException 缓存异常
	 */
	 String get(String key)throws CacheException;

	/**
	 * 获得缓存数量
	 * @param keyPrefix key前缀
	 * @return 缓存数量
	 * @throws CacheException 缓存异常
	 */
	 long getCountLike(String keyPrefix)throws CacheException;
	
	/**
	 * 删除缓存
	 * @param key 缓存key
	 * @throws CacheException 缓存异常
	 */
	void remove(String key)throws CacheException;
	
	/**
	 * 模糊删除缓存
	 * @param keyPrefix 缓存前缀
	 * @throws CacheException 缓存异常
	 */
	 void removeLike(String keyPrefix)throws CacheException;
	
	/**
	 * 获得缓存Key对应的Long值
	 * @param key 缓存key
	 * @return Long 缓存值
	 * @throws CacheException 缓存异常
	 */
	 Long getIncrValue(String key) throws CacheException;

	/**
	 * 缓存值叠加
	 * @param key 缓存的Key
	 * @param value  缓存Value上需要增加的值，
	 * @return  Long 增加之后的值
	 */
	Long increment(final String key, Long value);

	/**
	 * 过期时间设置，分钟
	 * @param key 缓存Key
	 * @param minutes 过期时间 分钟
	 * @throws CacheException 缓存异常
	 */
	void expire(String key , int minutes) throws CacheException;

	/**
	 * 判断缓存Key是否存在
 	 * @param key 缓存Key
	 * @return true 存在  false 不存在
	 */
	boolean exists(final String key);

	/**
	 * 过期时间设置， 秒
	 * @param key 缓存Key
	 * @param second 秒
	 * @throws CacheException 缓存异常
	 */
	void expireSecond(String key, int second) throws CacheException;
}