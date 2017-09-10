package com.apec.framework.cache;

import java.util.Map;

/**
 * Title: Hash缓存
 *
 * @author yirde  2017/7/17.
 */
public interface CacheHashService {

    /**
     * 将map写入缓存
     * @param key
     * @param map
     */
    <T> void hmset(String key, Map<String, T> map);

    /**
     * 向key对应的map中添加缓存对象
     * @param key	cache对象key
     * @param field map对应的key
     * @param value 	值
     */
     void hset(String key, String field, String value);

    /**
     * map缓存对象累加数据 HINCR BY
     * @param key cache对象key
     * @param field map对应的key
     * @param value 值
     * @return
     */
     Long hinc(String key, String field, long value);

    /**
     * 指定缓存的失效时间 秒
     * @param key 缓存KEY
     * @param expireTime 失效时间(秒)
     */
     void expire(String key, long  expireTime);

    /**
     * 获取map缓存中的某个对象
     * @param key cache对象key
     * @param field map对应的key
     * @return
     */
     <T> T hget(String key, String field);

    /**
     * 获取map缓存
     * @param key 缓存Key
     * @return
     */
     <T> Map<String, T> hgetAll(String key);

    /**
     * 删除缓存<br>
     * 根据key精确匹配删除
     * @param key 缓存Key
     */
    void del(String... key);

    /**
     * 删除map中的某个对象
     * @param key	map对应的key
     * @param field	map中该对象的key
     */
    void hdelField(String key, String... field);

}
