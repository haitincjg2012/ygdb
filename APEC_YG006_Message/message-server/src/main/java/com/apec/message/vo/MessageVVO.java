package com.apec.message.vo;

import com.apec.framework.common.enumtype.MessageStatus;
import lombok.Data;

/**
 * @author xxx
 */
@Data
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


}
