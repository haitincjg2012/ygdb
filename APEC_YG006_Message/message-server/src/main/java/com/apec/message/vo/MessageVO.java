package com.apec.message.vo;

import java.util.List;

import com.apec.framework.common.enumtype.MessageStatus;
import com.apec.framework.dto.BaseVO;
import lombok.Data;

/**
 * Title:消息
 *
 * @author yirde  2017/7/3.
 */
@Data
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



}
