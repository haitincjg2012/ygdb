package com.apec.message.dto;

import com.apec.framework.common.enumtype.MessageStatus;
import com.apec.framework.dto.BaseDTO;
import lombok.Data;

/**
 * Title:
 *
 * @author yirde 2017/3/23
 */
@Data
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

}
