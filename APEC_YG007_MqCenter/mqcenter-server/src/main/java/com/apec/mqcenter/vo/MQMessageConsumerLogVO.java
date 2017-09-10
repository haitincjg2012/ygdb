package com.apec.mqcenter.vo;

import com.apec.framework.common.enumtype.MQConsumerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Title:
 *
 * @author yirde  2017/7/4.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MQMessageConsumerLogVO  {

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
     * MsgId
     */
    private String msgId;


    /**
     * 消息body
     */
    private String body;

    /**
     * 顺序发送的KEY
     */
    private String orderlyKey;
    /**
     * 状态
     */
    private MQConsumerStatus status;

    // ========== 发送字段 ==========
    /**
     * 消息发送结果。
     * 另外消息执行时，使用{}进行重排执行
     */
    private String result;
    /**
     * 消费时间
     */
    private Long consumerTime;
    /**
     * 消费结束时间
     */
    private Long consumerEndTime;
    /**
     * 消费时异常
     */
    private String  consumerException;
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


    private Date createTime;

    public MQMessageConsumerLogVO(String topic, String tag, String keys, String msgId, String body ,Long consumerTime) {
        this.topic = topic;
        this.tag = tag;
        this.keys = keys;
        this.msgId = msgId;
        this.body = body;
        this.consumerTime = consumerTime;
    }

    public MQMessageConsumerLogVO(String topic, String tag, String keys, String msgId, MQConsumerStatus status, Long consumerEndTime, String consumerException) {
        this.topic = topic;
        this.tag = tag;
        this.keys = keys;
        this.msgId = msgId;
        this.status = status;
        this.consumerEndTime = consumerEndTime;
        this.consumerException = consumerException;
    }



}
