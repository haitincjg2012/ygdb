package com.apec.message.dao;

import com.apec.framework.common.PageDTO;
import com.apec.message.dto.MessageDTO;

/**
 * 后台发送信息
 * @author gunj
 * create by 2017-09-01
 * */
public interface BSMessageDAO {

	/**
	 * 获取后台系统通知站内信列表
	 * @param MessageDTO
	 * @return PageDTO<Object[]>
	 * */
	PageDTO<Object[]> listBSMessage(MessageDTO messageDTO);
}
