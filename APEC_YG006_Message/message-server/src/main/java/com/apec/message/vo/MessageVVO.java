package com.apec.message.vo;

import com.apec.framework.common.enumtype.MessageStatus;

public class MessageVVO {

    /**
     * 消息体
     */
    private MessageBodyVVO body;

    /**
     * 发送人ID
     */
    private String sender;

    /**
     * 状态
     */
    private MessageStatus messageStatus;

	public MessageBodyVVO getBody() {
		return body;
	}

	public void setBody(MessageBodyVVO body) {
		this.body = body;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public MessageStatus getMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(MessageStatus messageStatus) {
		this.messageStatus = messageStatus;
	}
}
