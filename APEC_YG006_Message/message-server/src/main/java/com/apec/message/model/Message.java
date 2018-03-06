package com.apec.message.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.GenericGenerator;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.MessageStatus;
import com.apec.framework.jpa.model.BaseModel;

/**
 * Title: 消息
 *
 * @author yirde  2017/6/30.
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
@Table(name = "message")
public class Message extends BaseModel<Long>{

    private static final long serialVersionUID = 6277321566852240263L;

    @ManyToOne
    @JoinColumn(name = "BODY_ID", nullable = false)
    private MessageBody body;

    /**
     * 发送人ID
     */
    @Column(length = 30)
    private String sender;

    /**
     * 接收人ID
     */
    @Column(nullable = false)
    private Long receiver;

    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MessageStatus messageStatus;

}
