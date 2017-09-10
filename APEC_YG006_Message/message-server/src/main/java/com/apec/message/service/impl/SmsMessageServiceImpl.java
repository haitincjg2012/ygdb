package com.apec.message.service.impl;

import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.apec.framework.cache.CacheService;
import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.constants.SysBusinessConstants;
import com.apec.framework.common.enumtype.MessageTemplate;
import com.apec.message.dao.ParamTemplateDAO;
import com.apec.message.dto.SmsAccountInfoDTO;
import com.apec.message.service.SmsMessageService;
import com.apec.message.vo.SmsCaptchaVO;
import com.apec.message.vo.SmsMessageVO;
import com.esms.MOMsg;
import com.esms.MessageData;
import com.esms.PostMsg;
import com.esms.common.entity.Account;
import com.esms.common.entity.AccountInfo;
import com.esms.common.entity.BusinessType;
import com.esms.common.entity.MTPack;
import com.esms.common.entity.MTPack.MsgType;
import com.esms.common.entity.MTPack.SendType;
import com.esms.common.entity.MTReport;
import com.esms.common.entity.MTResponse;
import com.esms.common.util.MediaUtil;

@Service
@RefreshScope
public class SmsMessageServiceImpl implements SmsMessageService{

	@Autowired
	private ParamTemplateDAO paramsDAO;

	@Autowired
	private Account account;
	
	@Autowired
	private PostMsg postMsg;

	@Autowired
	private CacheService cacheService;
	
	@Override
	public String sendSmsMessageByPost(SmsMessageVO smsMessageVO) throws Exception{
		MTPack pack = new MTPack();
		pack.setBatchID(UUID.randomUUID());//批量唯一序列号
		pack.setBatchName(smsMessageVO.getBatchName());//批量名称
		pack.setMsgType(MsgType.SMS);//默认发送消息类型:短信
		if (smsMessageVO.isMsgTypeFlag()){
			String path = SmsMessageServiceImpl.class.getClassLoader().getResource("mms").getPath();
			path = URLDecoder.decode(path, "utf-8");			
			//设置公共彩信资源
			pack.setMedias(MediaUtil.getMediasFromFolder(path));
			pack.setMsgType(MsgType.MMS);//彩信
		}
		//信息业务类型默认为0
		Integer bizType = smsMessageVO.getBizType() == null?0:smsMessageVO.getBizType();
		pack.setBizType(bizType);//信息业务类型
		pack.setDistinctFlag(smsMessageVO.isDistinctFlag());//是否过滤重复号码
		//发送时间默让即时
		if (smsMessageVO.getScheduleTime() != null){
			pack.setScheduleTime(smsMessageVO.getScheduleTime());//计划发送时间
		}
		pack.setRemark(smsMessageVO.getRemark());//备注
		pack.setCustomNum(smsMessageVO.getCustomNum());//用户扩展码
		//默认下发截至时间为当前系统时间
		if (smsMessageVO.getDeadline() != null){
			pack.setDeadline(smsMessageVO.getDeadline());//下发截至时间
		}
		
		List<MessageData> msgs = new LinkedList<MessageData>();
		pack.setSendType(SendType.MASS);//默认发送类型:群发
		//是否群发
		if (smsMessageVO.isSendTypeFlag()){
			pack.setSendType(SendType.GROUP);//组发
		}
		//是否使用数据库配置模板
		if (smsMessageVO.isTemlateFlag()){
			msgs = getTeplate(smsMessageVO, msgs);
		}else{
			msgs = getCustomContent(smsMessageVO, msgs);
		}
		if (msgs == null){
			return ErrorCodeConst.ERROR_MESSSAGE_CONTENT_NULL;
		}
		pack.setMsgs(msgs);
		postMsg.post(account, pack);
		return Constants.RETURN_SUCESS;
	}
	
	/**
	 * 生成模板内容
	 * @param smsMessageVO 短信VO对象
	 * @param msgs 发送内容list
	 * */
	private List<MessageData> getTeplate(SmsMessageVO smsMessageVO, List<MessageData> msgs){
		//获取数据库配置模板
		String template = paramsDAO.findByParamKey(smsMessageVO.getTemplateKey().getKey());
		if (StringUtils.isBlank(template)){
			return null;
		}
		for(Map.Entry<String, Map<String, String>> msgEntry : smsMessageVO.getMsgsMap().entrySet()){
			//模板替换
			for(Map.Entry<String, String> templateEntry : msgEntry.getValue().entrySet()){
				StringBuilder sb = new StringBuilder();
				sb.append("${").append(templateEntry.getKey()).append("}");
				template = template.replace(sb.toString(), templateEntry.getValue());
			}
			msgs.add(new MessageData(msgEntry.getKey(), template));
		}
		return msgs;
	}
	
	/**
	 * 生成自定义内容
	 * 
	 * */
	private List<MessageData> getCustomContent(SmsMessageVO smsMessageVO, List<MessageData> msgs){
		for(Map.Entry<String, String> contentEntry : smsMessageVO.getContentMap().entrySet()){
			msgs.add(new MessageData(contentEntry.getKey(), contentEntry.getValue()));
		}
		return msgs;
	}
	
	@Override
	public SmsAccountInfoDTO getAccountInfo() throws Exception{
		SmsAccountInfoDTO smsAccountInfo = new SmsAccountInfoDTO();
		AccountInfo accountInfo = postMsg.getAccountInfo(account); //获取账号详细信息
		smsAccountInfo.setAccountInfo(accountInfo);
		
		BusinessType[] businessTypes = postMsg.getBizTypes(account);//获取账号绑定业务类型
		smsAccountInfo.setBusinessTypes(businessTypes);
		return smsAccountInfo;
	}

	@Override
	public MOMsg[] getSmsMoMsgs(Integer number) throws Exception {
		MOMsg[] response = postMsg.getMOMsgs(account, number);
		return response;
	}

	@Override
	public MTReport[] getSmsStatusReport(Integer number) throws Exception {
		MTReport[] response = postMsg.getReports(account, number);
		return response;
	}

	@Override
	public void modifySmsPwd(String password) throws Exception {
		postMsg.modifyPassword(account, password);
		
	}

	@Override
	public String sendSmsCaptcha(SmsCaptchaVO smsCaptchaVO) throws Exception {
		//获取6位数验证码并缓存
		String key = Constants.CACHE_CAPTCHA_PREFIX + smsCaptchaVO.getMobile();
		String captcha =  randomCaptcha(cacheService.get(key));
		cacheService.add(key,captcha,SysBusinessConstants.CAPTCHA_TIMEOUT);
		
		//发送短信验证码
		MTPack mtPack = new MTPack();
		mtPack.setMsgType(MsgType.SMS);
		mtPack.setSendType(SendType.MASS);
		mtPack.setDistinctFlag(false);
		List<MessageData> msgs = new LinkedList<MessageData>();
		
		//发送内容生成
		String template = paramsDAO.findByParamKey(smsCaptchaVO.getTemplateKey().getKey());
		if (StringUtils.isEmpty(template)){
			return ErrorCodeConst.ERROR_MESSSAGE_CONTENT_NULL;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("${").append(MessageTemplate.CAPTCHA.getKey()).append("}");
		template = template.replace(sb.toString(), captcha);
		
		msgs.add(new MessageData(smsCaptchaVO.getMobile(), template));
		mtPack.setMsgs(msgs);
		postMsg.post(account, mtPack);	
		return Constants.RETURN_SUCESS;
	}

	/**
	 * 获取随机验证码
	 * @param oldCaptcha 原验证码
	 * @return Captcha
	 */
	private String randomCaptcha(String oldCaptcha){
		String captcha = RandomStringUtils.randomNumeric(SysBusinessConstants.CAPTCHA_LENGTH);//生成6位的随机数
		if(StringUtils.equals(captcha,oldCaptcha)){
			return randomCaptcha(oldCaptcha);
		}
		return captcha;
	}

	@Override
	public MTReport[] findSmsStatusReport(String batchId, String phone,
			Integer pageSize, boolean flag) throws Exception {
		UUID id = UUID.fromString(batchId);
		MTReport[] response = null;
		if (flag){
			response = postMsg.findReports(account, pageSize, id, phone, 0);
		}else{
			response = postMsg.findReports(account, pageSize, id, phone, 1);
		}
		return response;
	}

	@Override
	public MTResponse[] getSmsReport(String batchId, String phone,
			Integer pageSize, boolean flag) throws Exception {
		UUID id = UUID.fromString(batchId);
		MTResponse[] response = null;
		if (flag){
			response = postMsg.findResps(account, pageSize, id, phone, 0);
		}else{
			response = postMsg.findResps(account, pageSize, id, phone, 1);
		}
		return response;
	}
}
