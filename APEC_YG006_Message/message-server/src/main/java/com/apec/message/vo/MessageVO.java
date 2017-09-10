package com.apec.message.vo;

import java.util.List;

import com.apec.framework.common.enumtype.MessageStatus;
import com.apec.framework.dto.BaseVO;

/**
 * Title:消息
 *
 * @author yirde  2017/7/3.
 */
public class MessageVO extends BaseVO<Long> {

	/**
	 * 消息体ID
	 */
	private Long bodyId;

    /**
     * 消息体
     */
    private MessageBodyVO body;

    /**
     * 发送人ID
     */
    private String sender;

    /**
     * 接收人ID
     */
    private List<Long> receivers;

    /**
     * 状态
     */
    private MessageStatus messageStatus;

	public MessageBodyVO getBody() {
		return body;
	}

	public void setBody(MessageBodyVO body) {
		this.body = body;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public List<Long> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<Long> receivers) {
		this.receivers = receivers;
	}

	public MessageStatus getMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(MessageStatus messageStatus) {
		this.messageStatus = messageStatus;
	}

	public Long getBodyId() {
		return bodyId;
	}

	public void setBodyId(Long bodyId) {
		this.bodyId = bodyId;
	}
}
