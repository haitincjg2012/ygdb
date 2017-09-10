package com.apec.message.vo;

import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import com.apec.framework.common.enumtype.MessageTemplate;
import com.apec.framework.dto.BaseVO;

/**
 * 发送短信请求体
 * @author gunj
 * create by 3017-07-12
 * */
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
	private boolean distinctFlag;
	
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
	private boolean msgTypeFlag;
	
	/**
	 * 发送类型是否群发，默认群发
	 * */
	private boolean sendTypeFlag;
	
	/**
	 * key为手机号，value为自定义内容
	 * */
	private Map<String, String> contentMap;
	
	/**
	 * 是否使用动态模板
	 * */
	private boolean temlateFlag;

	public UUID getBatchID() {
		return batchID;
	}

	public void setBatchID(UUID batchID) {
		this.batchID = batchID;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public Map<String, Map<String, String>> getMsgsMap() {
		return msgsMap;
	}

	public void setMsgsMap(Map<String, Map<String, String>> msgsMap) {
		this.msgsMap = msgsMap;
	}

	public MessageTemplate getTemplateKey() {
		return templateKey;
	}

	public void setTemplateKey(MessageTemplate templateKey) {
		this.templateKey = templateKey;
	}

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public boolean isDistinctFlag() {
		return distinctFlag;
	}

	public void setDistinctFlag(boolean distinctFlag) {
		this.distinctFlag = distinctFlag;
	}

	public Integer getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Integer scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCustomNum() {
		return customNum;
	}

	public void setCustomNum(String customNum) {
		this.customNum = customNum;
	}

	public Long getDeadline() {
		return deadline;
	}

	public void setDeadline(Long deadline) {
		this.deadline = deadline;
	}

	public boolean isMsgTypeFlag() {
		return msgTypeFlag;
	}

	public void setMsgTypeFlag(boolean msgTypeFlag) {
		this.msgTypeFlag = msgTypeFlag;
	}

	public boolean isSendTypeFlag() {
		return sendTypeFlag;
	}

	public void setSendTypeFlag(boolean sendTypeFlag) {
		this.sendTypeFlag = sendTypeFlag;
	}

	public boolean isTemlateFlag() {
		return temlateFlag;
	}

	public void setTemlateFlag(boolean temlateFlag) {
		this.temlateFlag = temlateFlag;
	}

	public Map<String, String> getContentMap() {
		return contentMap;
	}

	public void setContentMap(Map<String, String> contentMap) {
		this.contentMap = contentMap;
	}
}
