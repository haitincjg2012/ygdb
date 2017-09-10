package com.apec.message.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.GenericGenerator;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.MessageType;
import com.apec.framework.jpa.model.BaseModel;

/**
 * 参数模板model
 * @author gunj
 * 
 * */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
@Table(name = "param_template")
public class ParamTemplate extends BaseModel<Long>{

	private static final long serialVersionUID = 2656883137438360281L;

	/**
	 * key
	 * */
	@Column(name = "param_key", unique = true, nullable = false,length = 40)
	private String paramKey;
	
	/**
	 * 内容
	 * */
	@Column(name = "param_value", nullable = false)
	private String paramValue;
	
	/**
	 * 内容类型
	 * */
	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = true, length = 20)
	private MessageType type;
	
	/**
	 * 备注
	 * */
	@Column(name = "remark", nullable = true)
	private String remark;

	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
