package com.apec.mqcenter.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.MQSendStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Title: MQ发送消息的失败记录收集
 *
 * @author yirde  2017/7/1.
 */
@Entity
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
@EntityListeners(
        {AuditingEntityListener.class})
public class MQMessageProducerLog implements Persistable<String> {

    private static final long serialVersionUID = 6233291566852240263L;

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
    @Id
    @GeneratedValue(generator = Constants.SYSTEM_GENERATOR)
    @Column(unique = true)
    private String msgKey;

    /**
     * 消息body
     */
    @Column(length = 1024)
    private String body;

    /**
     * 顺序发送的KEY
     */
    private String orderlyKey;
    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
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
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
    private Date createTime;

    @Override
    public String getId()
    {
        return msgKey;
    }
    @Override
    @JsonIgnore
    public boolean isNew()
    {
        return null == this.msgKey;
    }


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMsgKey() {
        return msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOrderlyKey() {
        return orderlyKey;
    }

    public void setOrderlyKey(String orderlyKey) {
        this.orderlyKey = orderlyKey;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
