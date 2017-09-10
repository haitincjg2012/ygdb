package com.apec.message.web;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.apec.framework.base.BaseController;
import com.apec.framework.common.Constants;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.common.util.JsonUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.message.service.ParamTemplateService;
import com.apec.message.vo.ParamTemplateVO;

@RestController
@RequestMapping(value = "/params")
public class ParamTemplateController extends BaseController{
	
	@InjectLogger
	private Logger log;

	@Autowired
	private ParamTemplateService paramsService;
	
	/**
	 * 根据key获取参数
	 * @param paramKey 参数key
	 * */
	@RequestMapping(value = "/getParamValue", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<String> getParamValue(@RequestBody String paramKey){
		try {
			ParamTemplateVO paramsVO = getFormJSON(paramKey, ParamTemplateVO.class);
			String paramValue = paramsService.findByParamKey(paramsVO.getParamKey());
			return getResultData(true, paramValue, null);
		} catch(BusinessException e) {
			log.error("调用服务异常:{}",e);
			return getResultData(false, null, Constants.SERVER_RESEST_EXCEPTION);
		} catch (Exception e) {
			log.error("系统异常:{}",e);
			return getResultData(false, null, Constants.SYS_ERROR);
		}
	}
	
	/**
	 * 增加参数配置
	 * @param paramsVO 参数对象体
	 * @return 
	 * */
	@RequestMapping(value = "/addParams", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<String> addParams(@RequestBody String json){
		ParamTemplateVO paramTemplateVO = JsonUtil.parseObject(json, ParamTemplateVO.class);
		if (paramTemplateVO == null){
			log.warn("can't add paramsVO,[paramsVO:{}]",paramTemplateVO);
			return getResultData(false, null, Constants.ERROR_100003);
		}
		if (StringUtils.isBlank(paramTemplateVO.getParamKey()) || StringUtils.isBlank(paramTemplateVO.getParamValue())){
			log.warn("can't add paramsVO,[paramKey:{},paramValue:{}]",paramTemplateVO.getParamKey(),paramTemplateVO.getParamValue());
			return getResultData(false, null, Constants.COMMON_MISSING_PARAMS);
		}
		try {
			paramsService.addParams(paramTemplateVO);
			return getResultData(true, null, "");
		} catch(BusinessException e){
			log.error("服务调用异常:{}",e);
			return getResultData(false, null, Constants.SERVER_RESEST_EXCEPTION);
		} catch (Exception e) {
			log.error("系统异常:{}",e);
			return getResultData(false, null, Constants.SYS_ERROR);
		}
	}
}
