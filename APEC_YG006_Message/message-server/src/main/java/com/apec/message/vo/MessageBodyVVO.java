package com.apec.message.vo;

import java.util.Date;

import com.apec.framework.common.enumtype.MessageType;
import lombok.Data;

/**
 * @author xx
 */
@Data
public class MessageBodyVVO {
	
	private Long id;

	/**
     * 标题
     */
    private String title;

    /**
     * 自定义内容
     */
    private String content;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 消息类型
     */
    private MessageType type;


}
