package com.apec.message.vo;

import java.util.Date;

import com.apec.framework.common.enumtype.MessageType;

public class MessageBodyVVO {
	
	private Long id;

	/**
     * 标题
     */
    private String title;

    /**
     * 自定义内容
     */
    private String content;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 消息类型
     */
    private MessageType type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	} 
}
