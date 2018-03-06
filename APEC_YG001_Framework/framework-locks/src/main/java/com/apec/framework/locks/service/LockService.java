package com.apec.framework.locks.service;

import com.apec.framework.common.exception.LockExistsException;
import com.apec.framework.common.exception.LockNotHeldException;
import com.apec.framework.locks.Lock;

/**
 * @author xxxx
 */
public interface LockService {

	/**
	 * 获取所有的锁
	 * @return Iterable<Lock>
	 */
	Iterable<Lock> findAll();

	/**
	 * 创建锁
	 * @param name name
	 * @return Lock
	 * @throws LockExistsException LockExistsException
	 */
	Lock create(String name) throws LockExistsException;

	/**
	 * getLockValue
	 * @param name name
	 * @return String
	 */
	String getLockValue(String name);

	/**
	 * 释放锁
	 * @param name name
	 * @param value value
	 * @return boolean
	 * @throws LockNotHeldException LockNotHeldException
	 */
	boolean release(String name, String value) throws LockNotHeldException;

	/**
	 * 更新锁
	 * @param name name
	 * @param  value value
	 * @return Lock
	 * @throws LockNotHeldException LockNotHeldException
	 */
	Lock refresh(String name, String value) throws LockNotHeldException;
}
