package com.apec.message.dto;

import java.util.Date;

import com.apec.framework.common.enums.Realm;
import com.apec.framework.common.enumtype.MessageType;
import com.apec.framework.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xxx
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageBodyDTO extends BaseDTO {

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 发送时间
	 */
	private Date sentTime;

	/**
	 * 所在的实体域
	 */
	private Realm realm;

	/**
	 * 消息类型
	 */
	private MessageType type;


}
