package com.apec.framework.locks;

import java.util.Date;

/**
 * @author xxx
 */
public class Lock implements Comparable<Lock> {

	/**
	 * 锁的名称
	 */
	private String name;
	/**
	 * 锁的值，唯一
	 */
	private String value;
	/**
	 * 有效期
	 */
	private Date expires;

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public Date getExpires() {
		return expires;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}

	public boolean isExpired() {
		return expires.before(new Date());
	}

	@Override
	public int compareTo(Lock other) {
		return expires.compareTo(other.expires);
	}

	public Lock(String name, String value, Date expires) {
		this.name = name;
		this.value = value;
		this.expires = expires;
	}
}