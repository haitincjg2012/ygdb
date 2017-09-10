package com.apec.message.service;

import org.springframework.data.domain.PageRequest;

import com.apec.framework.common.PageDTO;
import com.apec.message.vo.ParamTemplateVO;

public interface ParamTemplateService {

	/**
	 * 增加参数
	 * @param paramsVO 对象体
	 * @return boolean
	 * */
	public boolean addParams(ParamTemplateVO paramsVO);
	
	/**
	 * 查询参数列表
	 * @return PageDTO<ParamsVO> 参数对象列表
	 * */
	public PageDTO<ParamTemplateVO> findAll(PageRequest pageRequest);
	
	/**
	 * 查询参数value
	 * @param paramKey 参数key
	 * @return String
	 * */
	public String findByParamKey(String paramKey);
	
	/**
	 * 根据id更新参数
	 * @param ParamTemplateVO
	 *
	 * */
	public void updateById(ParamTemplateVO paramsVO);
	
	/**
	 * 根据id删除参数
	 * @param id
	 * */
	public void deleteById(Long id);
}
