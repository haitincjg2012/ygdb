package com.apec.framework.locks.service;

import com.apec.framework.cache.CacheException;
import com.apec.framework.cache.CacheService;
import com.apec.framework.common.Constants;
import com.apec.framework.common.exception.LockExistsException;
import com.apec.framework.common.exception.LockNotHeldException;
import com.apec.framework.locks.Lock;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * 类 编 号：
 * 类 名 称：
 * 内容摘要：
 * 创建日期：2017-01-07 19:09
 * 编码作者：zhaolei
 * @author xxx
 */
@Component
public class RedisLockServiceImpl implements LockService {
    private final Logger log = Logger.getLogger(getClass());
    private static final String DEFAULT_LOCK_PREFIX = Constants.DEFAULT_LOCK_PREFIX;

    private String prefix = DEFAULT_LOCK_PREFIX;

    /**
     * 15秒
     */
    private int expiry = 15;


    @Autowired
    CacheService cacheService;

    public void setPrefix(String prefix) {
        if (!prefix.endsWith(Constants.DOT)) {
            prefix = prefix + Constants.DOT;
        }
        this.prefix = prefix;
    }

    @Override
    public Iterable<Lock> findAll() {
        return null;
    }

    @Override
    public Lock create(String name) throws LockExistsException {
        String stored = getValue(name);
        if (stored != null) {
            log.error(" *** create lock by name :"+name + " , stored:"+stored);
            throw new LockExistsException(Constants.LOCK_EXISTS_EXCEPTION,"");
        }
        String value = UUID.randomUUID().toString();
        String key = keyForName(name);

        try {
            cacheService.addAndSubsecondExpire(key,value,expiry);
        } catch (CacheException e) {
            log.error(" *** cacheService.addAndSubsecondExpire exception ",e);
            throw new LockExistsException(Constants.LOCK_NOT_HELD_EXCEPTION,e);
        }
        Date expires = new Date(System.currentTimeMillis() + expiry*1000);
        return new Lock(name, value, expires);
    }

    @Override
    public String getLockValue(String name) {
        String lockValue = getValue(name);
        return lockValue;
    }

    @Override
    public boolean release(String name, String value) throws LockNotHeldException {
        String stored = getValue(name);
        if (stored != null && value.equals(stored)) {
            String key = keyForName(name);
            cacheService.remove(key);
            return true;
        }
        if (stored != null) {
            throw new LockNotHeldException(Constants.LOCK_NOT_HELD_EXCEPTION,"");
        }
        return  false;
    }

    @Override
    public Lock refresh(String name, String value) throws LockNotHeldException {
        String key = keyForName(name);
        String stored = getValue(name);
        if (stored != null && value.equals(stored)) {
            Date expires = new Date(System.currentTimeMillis() + expiry*1000);
            cacheService.expire(key,expiry);
            return new Lock(name, value, expires);
        }
        throw new LockNotHeldException(Constants.LOCK_NOT_HELD_EXCEPTION,"");
    }

    private String getValue(String name) {
        String key = keyForName(name);
        String stored = cacheService.get(key);
        return stored;
    }

    private String nameForKey(String key) {
        if (!key.startsWith(prefix)) {
            throw new IllegalStateException("Key (" + key + ") does not start with prefix (" + prefix + ")");
        }
        return key.substring(prefix.length());
    }

    private String keyForName(String name) {
        return prefix + name;
    }
}
