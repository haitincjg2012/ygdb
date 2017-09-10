package com.apec.framework.vo;

import com.apec.framework.common.enumtype.MessageType;
import com.apec.framework.dto.BaseVO;

import java.util.List;

/**
 * Title:消息VO
 *
 * @author yirde  2017/7/3.
 */
public class MessageVO extends BaseVO<Long> {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 所在域
     */
    private String realm;

    /**
     * 消息类型
     */
    private MessageType messageType;

    /**
     * 发送者
     */
    private String sender;

    /**
     * 接收者
     */
    private List<String> receivers;

    /**
     * 是否全部通知
     */
    private boolean allReceiver;

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

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<String> receivers) {
        this.receivers = receivers;
    }

    public boolean isAllReceiver() {
        return allReceiver;
    }

    public void setAllReceiver(boolean allReceiver) {
        this.allReceiver = allReceiver;
    }
}
