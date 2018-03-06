package com.apec.message.service;

import org.springframework.data.domain.PageRequest;

import com.apec.framework.common.PageDTO;
import com.apec.message.vo.ParamTemplateVO;

/**
 * @author xxx
 */
public interface ParamTemplateService {

	/**
	 * 增加参数
	 * @param paramsVO 对象体
	 * @return boolean
	 * */
	boolean addParams(ParamTemplateVO paramsVO);
	
	/**
	 * 查询参数列表
	 * @param pageRequest 分页对象
	 * @return PageDTO<ParamsVO> 参数对象列表
	 * */
	PageDTO<ParamTemplateVO> findAll(PageRequest pageRequest);
	
	/**
	 * 查询参数value
	 * @param paramKey 参数key
	 * @return String
	 * */
	String findByParamKey(String paramKey);
	
	/**
	 * 根据id更新参数
	 * @param paramsVO paramsVO
	 *
	 * */
	void updateById(ParamTemplateVO paramsVO);
	
	/**
	 * 根据id删除参数
	 * @param id id
	 * */
	void deleteById(Long id);
}
