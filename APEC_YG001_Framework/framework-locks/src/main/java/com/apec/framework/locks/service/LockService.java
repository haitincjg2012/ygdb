package com.apec.framework.locks.service;

import com.apec.framework.common.exception.LockExistsException;
import com.apec.framework.common.exception.LockNotHeldException;
import com.apec.framework.locks.Lock;

public interface LockService {

	/**
	 * 获取所有的锁
	 * @return
	 */
	Iterable<Lock> findAll();

	/**
	 * 创建锁
	 * @param name
	 * @return
	 * @throws LockExistsException
	 */
	Lock create(String name) throws LockExistsException;

	String getLockValue(String name);

	/**
	 * 释放锁
	 * @param name
	 * @param value
	 * @return
	 * @throws LockNotHeldException
	 */
	boolean release(String name, String value) throws LockNotHeldException;

	/**
	 * 更新锁
	 * @param name
	 * @param value
	 * @return
	 * @throws LockNotHeldException
	 */
	Lock refresh(String name, String value) throws LockNotHeldException;
}
