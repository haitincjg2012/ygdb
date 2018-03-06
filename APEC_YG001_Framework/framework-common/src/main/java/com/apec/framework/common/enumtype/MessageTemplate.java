package com.apec.framework.common.enumtype;

/**
 * message配置key
 * @author gunj
 * */
public enum MessageTemplate {
	
	/**
	 * 验证码替换key值
	 * */
	CAPTCHA("captcha"),

	/**
	 * 果满仓用户注册通知
	 * */
	REGISTER_WMS("REGISTER_WMS"),

	/**
	 * 设置实名成功的消息模板以及相应的内容值
	 */
	USER_REALNAME_SUCCESS("USER_REALNAME_SUCCESS"),

	/**
	 * 设置实名失败的消息模板以及相应的内容值
	 */
	USER_REALNAME_FAIL("USER_REALNAME_FAIL"),

	/**
	 * 积分 增加的站内信
	 */
	USER_POINT_ADD("USER_POINT_ADD"),

	/**
	 * 积分 减少的站内信
	 */
	USER_POINT_REDUCE("USER_POINT_REDUCE"),

	/**
	 * 注册验证码模板key值
	 * */
	REGISTER_CAPTCHA_TEMPLATE("REGISTER_CAPTCHA_TEMPLATE"),

	/**
	 * 供求下架模板
	 * */
	PRODUCT_OFF_SELL_TEMPLATE("PRODUCT_OFF_SELL_TEMPLATE"),

	/**
	 * 通知行情审核结果的模板
	 * */
	RESULT_OF_ARTICLE("RESULT_OF_ARTICLE"),

	/**
	 * 通知交收单审核结果的模板
	 * */
	RESULT_OF_VOUCHER("RESULT_OF_VOUCHER"),

	/**
	 * 通知行情竞猜结果的模板
	 * */
	RESULT_OF_QUOTATION("RESULT_OF_QUOTATION"),

	/**
	 * 找回密码验证码模板key值
	 * */
	FIND_PASSWORD_CAPTCHA_TEMPLATE("FIND_PASSWORD_CAPTCHA_TEMPLATE");


	
	private String key;
	
	MessageTemplate(String key){
		this.key = key;
	}

	public String getKey(){
		return key;
	}
	
}
