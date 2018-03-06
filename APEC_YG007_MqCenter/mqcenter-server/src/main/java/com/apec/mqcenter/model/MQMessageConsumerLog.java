package com.apec.mqcenter.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.MQConsumerStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Title: MQ消息消费记录
 *
 * @author yirde  2017/7/4.
 */
@Data
@Entity
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
@EntityListeners(
        {AuditingEntityListener.class})
public class MQMessageConsumerLog implements Persistable<String> {

    private static final long serialVersionUID = 6233291566852233263L;

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
     * MsgId
     */
    private String msgId;


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
    private MQConsumerStatus status;

    // ========== 发送字段 ==========

    /**
     * 消费时间
     */
    private Long consumerTime;
    /**
     * 消费结束时间
     */
    private Long consumerEndTime;

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


}
