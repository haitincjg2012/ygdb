package com.apec.message.dto;

import com.apec.framework.common.enumtype.MessageStatus;
import com.apec.framework.dto.BaseDTO;

/**
 * Title:
 *
 * @author yirde 2017/3/23
 */
public class MessageDTO extends BaseDTO {

    private MessageBodyDTO body;

    /**
     * 发送人ID
     */
    private String sender;

    /**
     * 接收人ID
     */
    private Long receiver;

    /**
     * 状态
     */
    private MessageStatus messageStatus;

	public MessageBodyDTO getBody() {
		return body;
	}

	public void setBody(MessageBodyDTO body) {
		this.body = body;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Long getReceiver() {
		return receiver;
	}

	public void setReceiver(Long receiver) {
		this.receiver = receiver;
	}

	public MessageStatus getMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(MessageStatus messageStatus) {
		this.messageStatus = messageStatus;
	}
}
