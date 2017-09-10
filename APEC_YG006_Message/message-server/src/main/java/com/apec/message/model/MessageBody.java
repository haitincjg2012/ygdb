package com.apec.message.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enums.Realm;
import com.apec.framework.common.enumtype.MessageType;
import com.apec.framework.jpa.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Title: MessageBody 消息体
 *
 * @author yirde  2017/6/30.
 */
@Entity
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class MessageBody extends BaseModel<Long> {

    private static final long serialVersionUID = 6277891566852240234L;

    /**
     * 标题
     */
    @Column(name = "title", nullable = true, length = 50)
    private String title;

    /**
     * 内容
     */
    @Column(name = "content", nullable = false)
    private String content;

    /**
     * 发送时间
     */
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "send_time")
    private Date sendTime;

    /**
     * 所在的实体域
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "realm", nullable = false, length = 20)
    private Realm realm;

    /**
     * 消息类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private MessageType type;

    /**
     * 是否全部通知 Y 全部通知 ，N  指定用户ID 通知
     */
    @Column(name = "all_receiver", nullable = false)
    private boolean allReceiver;

    /**
     * 消息列表
     */
    @OneToMany(mappedBy = "body",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Message> messages;

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

	public boolean isAllReceiver() {
		return allReceiver;
	}

	public void setAllReceiver(boolean allReceiver) {
		this.allReceiver = allReceiver;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

}
