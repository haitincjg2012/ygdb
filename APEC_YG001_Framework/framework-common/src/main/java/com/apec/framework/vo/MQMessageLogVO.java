package com.apec.framework.vo;

import com.apec.framework.common.enumtype.MQSendStatus;

/**
 * Title: MQ消息日志VO
 *
 * @author yirde  2017/7/3.
 */
public class MQMessageLogVO {

    /**
     * Topic
     */
    private String topic;

    /**
     * Tag
     */
    private String tag;

    /**
     * Keys
     */
    private String keys;

    /**
     * 消息body
     */
    private String body;

    /**
     * 顺序发送的KEY
     */
    private String orderKey;
    /**
     * 状态
     */
    private MQSendStatus status;

    // ========== 发送字段 ==========
    /**
     * 消息发送结果。
     * 另外消息执行时，使用{}进行重排执行
     */
    private String result;
    /**
     * 发送时间
     */
    private Long sendTime;
    /**
     * 发送结束时间
     */
    private Long sendEndTime;
    /**
     * 发送时异常
     */
    private String sendException;
    // ========== 重发字段 ==========
    /**
     * 重发时间
     */
    private Long retryTime;
    /**
     * 重发结束时间
     */
    private Long retryEndTime;

    /**
     * 重发时异常
     */
    private String retryException;

    public MQMessageLogVO() {}

    public MQMessageLogVO(String topic, String tag, String keys, String body, String orderKey) {
        this.topic = topic;
        this.tag = tag;
        this.keys = keys;
        this.body = body;
        this.orderKey = orderKey;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }


    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    public MQSendStatus getStatus() {
        return status;
    }

    public void setStatus(MQSendStatus status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public Long getSendEndTime() {
        return sendEndTime;
    }

    public void setSendEndTime(Long sendEndTime) {
        this.sendEndTime = sendEndTime;
    }

    public String getSendException() {
        return sendException;
    }

    public void setSendException(String sendException) {
        this.sendException = sendException;
    }

    public Long getRetryTime() {
        return retryTime;
    }

    public void setRetryTime(Long retryTime) {
        this.retryTime = retryTime;
    }

    public Long getRetryEndTime() {
        return retryEndTime;
    }

    public void setRetryEndTime(Long retryEndTime) {
        this.retryEndTime = retryEndTime;
    }

    public String getRetryException() {
        return retryException;
    }

    public void setRetryException(String retryException) {
        this.retryException = retryException;
    }

    public String getTag() {
        return tag;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }
}
