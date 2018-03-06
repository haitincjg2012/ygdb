package com.apec.message.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.apec.framework.common.enumtype.MessageType;
import com.apec.framework.dto.BaseVO;

/**
 * 参数模板vo
 * @author gunj
 * 
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParamTemplateVO extends BaseVO<Long>{

	/**
	 * 参数key
	 * */
	private String paramKey;
	
	/**
	 * 参数value
	 * */
	private String paramValue;
	
	/**
	 * 内容类型
	 * */
	private MessageType type;
	
	/**
	 * 备注
	 * */
	private String remark;


}
