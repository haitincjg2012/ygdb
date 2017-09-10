package com.apec.message.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apec.framework.common.PageDTO;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.message.dao.ParamTemplateDAO;
import com.apec.message.model.ParamTemplate;
import com.apec.message.service.ParamTemplateService;
import com.apec.message.util.SnowFlakeKeyGen;
import com.apec.message.vo.ParamTemplateVO;

@Service
public class ParamTemplateServiceImpl implements ParamTemplateService{
	
	@InjectLogger
	private Logger log;
	
	@Autowired
	private ParamTemplateDAO paramsDAO;
	
	@Autowired
	private SnowFlakeKeyGen idGen;

	@Override
	@Transactional
	public boolean addParams(ParamTemplateVO paramsVO){
		ParamTemplate params = new ParamTemplate();
		params.setId(idGen.nextId());
		params.setParamKey(paramsVO.getParamKey());
		params.setParamValue(paramsVO.getParamValue());
		params.setType(paramsVO.getType());
		params.setRemark(paramsVO.getRemark());
		params.setCreateDate(new Date());
		params.setCreateBy(paramsVO.getCreateBy());
		paramsDAO.saveAndFlush(params);
		return true;
	}

	@Override
	public PageDTO<ParamTemplateVO> findAll(PageRequest pageRequest) {
		Page<ParamTemplate> paramsList = paramsDAO.findAll(pageRequest);
		PageDTO<ParamTemplateVO> response = new PageDTO<ParamTemplateVO>();
		List<ParamTemplateVO> paramsVOList = new LinkedList<ParamTemplateVO>();
		paramsList.forEach(baseInfo->{
			ParamTemplateVO paramsVO = new ParamTemplateVO();
			BeanUtil.copyProperties(baseInfo, paramsVO);
			paramsVOList.add(paramsVO);
		});
		response.setNumber(paramsList.getNumber()+1);
		response.setRows(paramsVOList);
		response.setTotalElements(paramsList.getTotalElements());
		response.setTotalPages(paramsList.getTotalPages());
		return response;
	}

	@Override
	public String findByParamKey(String paramKey) {
		String paramValue = paramsDAO.findByParamKey(paramKey);
		return paramValue;
	}

	@Override
	public void updateById(ParamTemplateVO paramsVO) {
		paramsDAO.updateById(paramsVO.getParamKey(), paramsVO.getParamValue(), paramsVO.getRemark(), paramsVO.getId());
	}

	@Override
	public void deleteById(Long id) {
		paramsDAO.delete(id);
	}

}
