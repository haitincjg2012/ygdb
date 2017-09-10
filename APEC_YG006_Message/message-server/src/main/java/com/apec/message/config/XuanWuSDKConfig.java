package com.apec.message.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.esms.PostMsg;
import com.esms.common.entity.Account;

@Configuration
public class XuanWuSDKConfig {

	@Value("${sms.username}")
	private String userName;
	
	@Value("${sms.password}")
	private String password;
	
	@Value("${sms.ip}")
	private String ip;
	
	@Value("${sms.port}")
	private String port;
	
	@Bean
	public Account getAccount(){
		Account account = new Account(userName, password);
		return account;
	}
	
	@Bean
	public PostMsg getPostMsg(){
		PostMsg postMsg = new PostMsg();
		postMsg.getCmHost().setHost(ip, Integer.parseInt(port));
		return postMsg;
	}
	
}
