package com.apec.message.vo;

import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.apec.framework.common.enumtype.MessageTemplate;
import com.apec.framework.dto.BaseVO;

/**
 * 发送短信请求体
 * @author gunj
 * create by 3017-07-12
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsMessageVO extends BaseVO<Long>{

	/**
	 * 信息批次唯一序号
	 * */
	private UUID batchID;
	
	/**
	 * 批次名称
	 * */
	private String batchName;
	
	/**
	 * 短信号码和短信内容模板需要替换值
	 * */
	private Map<String, Map<String, String>> msgsMap;
	
	/**
	 * 短信模板key
	 * */
	private MessageTemplate templateKey;
	
	/**
	 * 信息业务类型
	 * */
	private Integer bizType;
	
	/**
	 * 是否过滤重复号码
	 * */
	private Boolean distinctFlag;
	
	/**
	 * 计划发送时间
	 * */
	private Integer scheduleTime;
	
	/**
	 * 备注
	 * */
	private String remark;
	
	/**
	 * 用户扩展码
	 * */
	private String customNum;
	
	/**
	 * 下发截至时间
	 * */
	private Long deadline;
	
	/**
	 * 消息类型是否为短信默认短信：true:彩信,false:短信
	 * */
	private Boolean msgTypeFlag;
	
	/**
	 * 发送类型是否群发，默认群发
	 * */
	private Boolean sendTypeFlag;
	
	/**
	 * key为手机号，value为自定义内容
	 * */
	private Map<String, String> contentMap;
	
	/**
	 * 是否使用动态模板
	 * */
	private Boolean temlateFlag;


}
