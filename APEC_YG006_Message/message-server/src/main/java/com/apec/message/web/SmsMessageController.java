package com.apec.message.web;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.common.util.JsonUtil;
import com.apec.framework.common.util.ValidateUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.message.service.SmsMessageService;
import com.apec.message.vo.SmsCaptchaVO;
import com.apec.message.vo.SmsMessageVO;

/**
 * 短信接口
 * @author gunj
 * create by 2017-07-12
 * */
@RestController
@RequestMapping(value = "/smsMessage")
public class SmsMessageController extends MyBaseController{
	
	@InjectLogger
	private Logger log;

	@Autowired
	private SmsMessageService smsMessageService;
	
	/**
	 * 发送短信，彩信消息
	 * @param smsMessageVO 请求体
	 * @return ResultData<String>
	 * */
	@RequestMapping(value = "/sendSmsMessage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<String> sendSmsMessage(@RequestBody String json){
		SmsMessageVO smsMessageVO = JsonUtil.parseObject(json, SmsMessageVO.class);
		if (smsMessageVO.isTemlateFlag()){
			if (smsMessageVO.getMsgsMap() == null || smsMessageVO.getTemplateKey() == null){
				log.warn("msgsMap is null[msgsMap:{}]",smsMessageVO.getMsgsMap());
				return getResultData(false, null, Constants.ERROR_100003);
			}
		} else{
			if (smsMessageVO.getContentMap() == null){
				log.warn("contentMap is null,[contentMap:{}]",smsMessageVO.getContentMap());
				return getResultData(false, null, Constants.ERROR_100003);
			}
		}
		ResultData<String> resultData = new ResultData<>();
		try {
			String resultCode = smsMessageService.sendSmsMessageByPost(smsMessageVO);
			if (StringUtils.equals(resultCode, Constants.RETURN_SUCESS)) {
				resultData.setSucceed(true);
			} else {
				setErrorResultDate(resultData, resultCode);
			}
			return getResultData(true, null, "");
		} catch (BusinessException e){			
			log.error("Add Message  BusinessException{}", e);
			setErrorResultDate(resultData, e.getErrorCode());
		} catch (Exception e) {			
			log.error("Add  Message Exception{}", e);
			setErrorResultDate(resultData, Constants.SYS_ERROR);
		}
		return resultData;
	}

	/**
	 * 发送验证码
	 * @param jsonStr Mobile
	 * @return String
	 */
	@RequestMapping(value = "/captcha", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<String> sendCaptcha(@RequestBody String jsonStr) {
		SmsCaptchaVO smsCaptchaVO = getFormJSON(jsonStr,SmsCaptchaVO.class);
		if (!ValidateUtil.checkMobile(smsCaptchaVO.getMobile())){
			log.warn("[SmsMessage][captcha] mobile is not true,[mobile:{}]",smsCaptchaVO.getMobile());
			return getResultData(false, null, ErrorCodeConst.ERROR_SMSMESSAGE_MOBILE_NOTSURE);
		}
		if (smsCaptchaVO.getTemplateKey() == null){
			log.warn("[SmsMessage][captcha] captchaKey is null,[templateKey:{}]",smsCaptchaVO.getTemplateKey());
			return getResultData(false, null, Constants.COMMON_MISSING_PARAMS);
		}
		ResultData<String> resultData = new ResultData<>();
		try {			
			String returnCode = smsMessageService.sendSmsCaptcha(smsCaptchaVO);
			if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
				resultData.setSucceed(true);
			} else {
				setErrorResultDate(resultData, returnCode);
			}
		} catch (BusinessException e) {
			log.error("[SmsMessage][captcha]Send Captcha BusinessException", e);
			setErrorResultDate(resultData, e.getErrorCode());
		}catch (Exception e) {
			log.error("[SmsMessage][captcha]Send Captcha Exception", e);
			setErrorResultDate(resultData,Constants.SYS_ERROR);
		}
		return resultData;
	}



}
