package com.apec.message.service;

import com.apec.message.dto.SmsAccountInfoDTO;
import com.apec.message.vo.SmsCaptchaVO;
import com.apec.message.vo.SmsMessageVO;
import com.esms.MOMsg;
import com.esms.common.entity.MTReport;
import com.esms.common.entity.MTResponse;

/**
 * 短信接口
 * @author gunj
 * create by 2017-07-10
 * */
public interface SmsMessageService {

	/**
	 * 短信下发
	 * @param smsMessageVO 消息体
	 * @return 结果码
	 * @throws  Exception Exception
	 */
	String sendSmsMessageByPost(SmsMessageVO smsMessageVO)throws Exception;
	
	/**
	 * 获取账号信息
	 * @return  SmsAccountInfoDTO
	 * @throws Exception Exception
	 * */
	SmsAccountInfoDTO getAccountInfo() throws Exception;
	
	/**
	 * 获取上行信息
	 * @param number 读取的条数
	 * @return  MOMsg[]
	 * @throws Exception  Exception
	 * 
	 * */
	MOMsg[] getSmsMoMsgs(Integer number) throws Exception;
	
	/**
	 * 查询提交报告
	 * @param batchId 批次Id
	 * @param phone 手机号 批次id和手机号至少有一个必填
	 * @param pageSize 页数
	 * @param flag 是否精确查找
	 * @return  提交报告
	 * @throws Exception Exception
	 * */
	MTResponse[] getSmsReport(String batchId, String phone, Integer pageSize, boolean flag) throws Exception;
	
	/**
	 * 查询状态报告
	 * @param batchId 批次Id
	 * @param phone 手机号 批次id和手机号至少有一个必填
	 * @param pageSize 页数
	 * @param flag 是否精确查找
	 * @return  状态报告
	 * @throws Exception Exception
	 * */
	MTReport[] findSmsStatusReport(String batchId, String phone, Integer pageSize, boolean flag) throws Exception;
	
	/**
	 * 获取状态报告
	 * @param number 状态报告条数
	 * @return  MTReport[]
	 * @throws Exception Exception
	 * */
	MTReport[] getSmsStatusReport(Integer number) throws Exception;
	
	/**
	 * 修改密码
	 * @param password 要修改的密码
	 * @throws Exception Exception
	 * */
	void modifySmsPwd(String password) throws Exception;


	/**
	 * 发送手机验证码
	 * @param smsCaptchaVO smsCaptchaVO
	 * @return ReturnCode 返回码
	 * @throws Exception Exception
	 */
	String sendSmsCaptcha(SmsCaptchaVO smsCaptchaVO)throws Exception ;
}
