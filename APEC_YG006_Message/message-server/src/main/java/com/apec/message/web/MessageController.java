package com.apec.message.web;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.PageJSON;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.enums.Realm;
import com.apec.framework.common.enumtype.MessageType;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.dto.UserInfoVO;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.springcloud.SpringCloudClient;
import com.apec.message.dto.MessageDTO;
import com.apec.message.service.MessageService;
import com.apec.message.vo.MessageBodyVO;
import com.apec.message.vo.MessageVO;
import com.apec.message.vo.MessageVVO;

import java.util.ArrayList;
import java.util.List;


/**
 *  站内信模块接口
 *
 * @author yirder
 */
@RestController
@RequestMapping("/message")
public class MessageController extends MyBaseController {

    @InjectLogger
    private Logger log;

    @Autowired
    private MessageService messageService;
    
    @Value("${sys_user_detail_url}")
    private String sysUserDetailUrl;
    
    @Autowired
    private SpringCloudClient springCloudClient;

    /**
     * 添加消息
     */
    @RequestMapping(value = "/addMessage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<String> addMessage(@RequestBody String json){
    	MessageVO messageVO = BaseJsonUtil.parseObject(json, MessageVO.class);
    	if (messageVO.getBody() == null){
    		log.warn("MessageBodyVO is null");
    		return getResultData(false, null, Constants.ERROR_100003);
    	}
    	if (messageVO.getBody().getRealm() == null || messageVO.getBody().getType() == null || 
    			CollectionUtils.isEmpty(messageVO.getReceivers())){
    		log.warn("param is null,[realm:{},type:{},receivers:{}]",messageVO.getBody().getRealm()
    				,messageVO.getBody().getType(),messageVO.getReceivers());
    		return getResultData(false, null, Constants.COMMON_MISSING_PARAMS);
    	}
    	if (messageVO.getBody().getTemplateFlag()){
    		if (messageVO.getBody().getTemplateKey() == null){
    			log.warn("templateKey is null,[templateKey:{}]",messageVO.getBody().getTemplateKey());
    			return getResultData(false, null, Constants.COMMON_MISSING_PARAMS);
    		}
    	}else {
    		if (StringUtils.isEmpty(messageVO.getBody().getContent())){
    			log.warn("content is null, [content:{}]",messageVO.getBody().getContent());
    			return getResultData(false, null, Constants.COMMON_MISSING_PARAMS);
    		}
    	}
    	ResultData<String> resultData = new ResultData<>();
        try {       	
            String resultCode = messageService.addMessageInfo(messageVO);
            if (StringUtils.equals(Constants.RETURN_SUCESS, resultCode)){
            	resultData.setSucceed(true);
            } else{
            	setErrorResultDate(resultData, resultCode);
            }
        }catch (BusinessException e){
            log.error("Add Message  BusinessException", e);
            setErrorResultDate(resultData, Constants.SERVER_RESEST_EXCEPTION);
        } catch (Exception e) {
            log.error("Add  Message Exception", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     * 根据发送人查询站内信列表
     * */
    @RequestMapping(value = "/getMessageBySender", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<PageDTO<MessageVVO>> getMessageBySender(@RequestBody(required = false) String json){
    	
    	try {
    		PageJSON<String> pageJson = super.getPageJSON(json, String.class);
    		MessageDTO dto = BaseJsonUtil.parseObject(pageJson.getFormJSON(), MessageDTO.class);
    		PageDTO<MessageVVO> messageViewVOList = messageService.listMessageInfoBySender(dto);
			return getResultData(true, messageViewVOList, "");
		} catch (BusinessException e){
            log.error("Add Message  BusinessException", e);
            return getResultData(false, null, Constants.SERVER_RESEST_EXCEPTION);
        } catch (Exception e) {
        	log.error("Add Message Exception", e);
			return getResultData(false, null, Constants.SYS_ERROR);
		}
    }
    
    /**
     * 根据接收人查询站内信列表
     * @param   json json
     * */
    @RequestMapping(value = "/getMessageByReceiver", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public ResultData<PageDTO<MessageVVO>> getMessageByReceiver(@RequestBody String json){
    	
    	try {
			Long userId = getUserId(json);
			MessageDTO dto = getFormJSON(json,MessageDTO.class);
			dto.setReceiver(userId);
			PageRequest pageRequest = genPageRequest(dto);
    		PageDTO<MessageVVO> mailMessageBodyVOList = messageService.listMessageInfoByReceiver(dto.getReceiver(), pageRequest);
			return getResultData(true, mailMessageBodyVOList, "");
		} catch (BusinessException e){
            log.error("Add Message  BusinessException", e);
            return getResultData(false, null, Constants.SERVER_RESEST_EXCEPTION);
        } catch (Exception e) {
        	log.error("Add Message Exception", e);
			return getResultData(false, null, Constants.SYS_ERROR);
		}
    }

	/**
	 * 设置已读站内信
	 * @return ResultData
	 */
	@RequestMapping(value = "/setMessageRead", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<String> setMessageRead(@RequestBody String jsonStr) {
		ResultData<String> resultData = new ResultData<>();
		try {
			PageJSON<String> pageJSON = super.getPageJSON(jsonStr, String.class);
			MessageVO messageVO = BaseJsonUtil.parseObject(pageJSON.getFormJSON(), MessageVO.class);
			UserInfoVO userInfoVO = getUserInfo(pageJSON);
			String returnCode = messageService.updateStatus(userInfoVO.getUserId(),messageVO.getBodyId());
			if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
				resultData.setSucceed(true);
			} else {
				setErrorResultDate(resultData, returnCode);
			}

		} catch (BusinessException e) {
			log.error("SetMessageRead BusinessException", e);
			setErrorResultDate(resultData, e.getErrorCode());
		}catch (Exception e) {
			log.error("SetMessageRead Exception", e);
			setErrorResultDate(resultData,Constants.SYS_ERROR);
		}
		return resultData;
	}
	
	/**
	 * 后台系统通知发送站内信
	 * */
	@RequestMapping(value = "/addBSmessage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<String> addBSmessage(@RequestBody String json){
		
		PageJSON<String> pageJson = super.getPageJSON(json, String.class);
		MessageBodyVO messageBodyVO = BaseJsonUtil.parseObject(pageJson.getFormJSON(), MessageBodyVO.class);
		if (StringUtils.isBlank(messageBodyVO.getContent())){
			log.warn("content is null, [content:{}]",messageBodyVO.getContent());
			return getResultData(false, null, Constants.ERROR_100003);
		}
		//获取发送人信息
		Long userId = getUserId(json);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", userId);
		String jsonStr = springCloudClient.post(sysUserDetailUrl, jsonObject.toString());
		ResultData<String> returnData = BaseJsonUtil.parseObject(jsonStr, new TypeReference<ResultData<String>>(){});
		String str = returnData.getData();
		JSONObject jsonObj = BaseJsonUtil.parseObject(str);
		String sender = jsonObj.getString("name");
		
		messageBodyVO.setRealm(Realm.USER);
		messageBodyVO.setType(MessageType.NOTIFICATION);
		messageBodyVO.setAllReceiver(true);
		ResultData<String> resultData = new ResultData<>();
        try {       	
        	MessageVO messageVO = new MessageVO();
        	messageVO.setSender(sender);
        	messageVO.setBody(messageBodyVO);
            String resultCode = messageService.addMessageInfo(messageVO);
            if (StringUtils.equals(Constants.RETURN_SUCESS, resultCode)){
            	resultData.setSucceed(true);
            } else{
            	setErrorResultDate(resultData, resultCode);
            }
        }catch (BusinessException e){
            log.error("Add Message  BusinessException", e);
            setErrorResultDate(resultData, Constants.SERVER_RESEST_EXCEPTION);
        } catch (Exception e) {
            log.error("Add  Message Exception", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
	}
	
	/**
	 * 软删除站内信
	 * */
	@RequestMapping(value = "/deleteMessage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<String> deleteMessage(@RequestBody String json){
		
		PageJSON<String> pageJson = super.getPageJSON(json, String.class);
		MessageVO messageVO = BaseJsonUtil.parseObject(pageJson.getFormJSON(), MessageVO.class);
		if (messageVO.getBodyId() == null){
			return getResultData(false, "", Constants.ERROR_100003);
		}
		ResultData<String> resultData = new ResultData<>();
		try {
			String resultCode = messageService.deleteMessage(messageVO.getBodyId());
            if (StringUtils.equals(Constants.RETURN_SUCESS, resultCode)){
            	resultData.setSucceed(true);
            } else{
            	setErrorResultDate(resultData, resultCode);
            }
		}catch (BusinessException e){
            log.error("Add Message  BusinessException", e);
            setErrorResultDate(resultData, Constants.SERVER_RESEST_EXCEPTION);
        } catch (Exception e) {
            log.error("Add  Message Exception", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
		return resultData;
	}

	/**
	 * 清空站内信
	 * */
	@RequestMapping(value = "/clearMessage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<String> clearMessage(@RequestBody String json){

		PageJSON<String> pageJson = super.getPageJSON(json, String.class);
		MessageVO messageVO = BaseJsonUtil.parseObject(pageJson.getFormJSON(), MessageVO.class);
		if (messageVO == null || CollectionUtils.isEmpty(messageVO.getReceivers()) || messageVO.getReceivers().size() > 0){
			//前台清空消息数
			messageVO = new MessageVO();
			List<Long> receiver = new ArrayList<>();
			receiver.add(getUserId(json));
			messageVO.setReceivers(receiver);
		}
		ResultData<String> resultData = new ResultData<>();
		try {
			String resultCode = messageService.clearMessage(messageVO);
			if (StringUtils.equals(Constants.RETURN_SUCESS, resultCode)){
				resultData.setSucceed(true);
			} else{
				setErrorResultDate(resultData, resultCode);
			}
		}catch (BusinessException e){
			log.error("clearMessage  BusinessException", e);
			setErrorResultDate(resultData, Constants.SERVER_RESEST_EXCEPTION);
		} catch (Exception e) {
			log.error("clearMessage Exception", e);
			setErrorResultDate(resultData, Constants.SYS_ERROR);
		}
		return resultData;
	}
}
