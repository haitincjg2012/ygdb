package com.apec.message.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
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
@Data
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
	@Column(name = "type",length = 20)
	private MessageType type;
	
	/**
	 * 备注
	 * */
	@Column(name = "remark")
	private String remark;


}
