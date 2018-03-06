package com.apec.framework.common.enums;

/**
 * 参数表key
 * @author  xx
 * */
public enum Params{
	
	/**
	 * 短信用户名
	 * */
	SMS_USERNAME("smsUserName"),
	
	/**
	 * 短信密码
	 * */
	SMS_PASSWORD("smsPassword"),
	
	/**
	 * 发送短信ip
	 * */
	SMS_IP("smsIp"),
	
	/**
	 * 发送短信端口
	 * */
	SMS_PORT("smsPort")
	;

	private String key;
	
	Params(String key){
		this.key = key;
	}
	

	public String getKey() {

		return key;
	}

}
