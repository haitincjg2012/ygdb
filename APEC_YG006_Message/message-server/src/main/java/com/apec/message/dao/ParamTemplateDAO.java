package com.apec.message.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.message.model.ParamTemplate;

/**
 * 参数模板DAO
 * @author gunj
 * */
public interface ParamTemplateDAO extends BaseDAO<ParamTemplate, Long>{

	/**
	 * 获取参数表value值
	 * @param paramKey 配置key值
	 * @return String
	 * */
	@Query(value = "select param_value from param_template where param_key=:paramKey", nativeQuery = true)
	String findByParamKey(@Param("paramKey") String paramKey);
	
	/**
	 * 根据id更新参数
	 * @param paramKey paramKey
	 * @param paramValue paramValue
	 * @param remark 备注
	 * @param id id 
	 * */
	@Modifying
	@Query(value = "update param_template set param_key=:paramKey,paramValue=:paramValue,remark=:remark where id=:id", nativeQuery = true)
	void updateById(@Param("paramKey")String paramKey, @Param("paramValue")String paramValue,
			@Param("remark")String remark, @Param("id")Long id);
}
