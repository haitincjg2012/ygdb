package com.apec.message.dto;

import java.util.Date;

import com.apec.framework.common.enums.Realm;
import com.apec.framework.common.enumtype.MessageType;
import com.apec.framework.dto.BaseDTO;

public class MessageBodyDTO extends BaseDTO {

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 发送时间
	 */
	private Date sentTime;

	/**
	 * 所在的实体域
	 */
	private Realm realm;

	/**
	 * 消息类型
	 */
	private MessageType type;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getSentTime() {
		return sentTime;
	}

	public void setSentTime(Date sentTime) {
		this.sentTime = sentTime;
	}

	public Realm getRealm() {
		return realm;
	}

	public void setRealm(Realm realm) {
		this.realm = realm;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}
}
