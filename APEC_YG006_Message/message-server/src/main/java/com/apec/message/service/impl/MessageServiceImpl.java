package com.apec.message.service.impl;

import java.util.*;

import com.apec.framework.common.enumtype.EnableFlag;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.apec.framework.cache.CacheHashService;
import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.RedisHashConstants;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.enumtype.MessageStatus;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.springcloud.SpringCloudClient;
import com.apec.message.dao.BSMessageDAO;
import com.apec.message.dao.MessageBodyDAO;
import com.apec.message.dao.MessageDAO;
import com.apec.message.dao.ParamTemplateDAO;
import com.apec.message.dto.MessageDTO;
import com.apec.message.model.Message;
import com.apec.message.model.MessageBody;
import com.apec.message.model.QMessage;
import com.apec.message.service.MessageService;
import com.apec.message.util.SnowFlakeKeyGen;
import com.apec.message.vo.MessageBodyVVO;
import com.apec.message.vo.MessageVO;
import com.apec.message.vo.MessageVVO;

/**
 * @author xxx
 */
@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageDAO messageDao;
	
	@Autowired
	private MessageBodyDAO messageBodyDAO;
	
	@Autowired
	private ParamTemplateDAO paramTemplateDAO;
	
	@Autowired
	private SnowFlakeKeyGen idGen;
	
	@InjectLogger
	private Logger log;

	@Autowired
	private CacheHashService cacheHashService;
	
	@Autowired
	private SpringCloudClient springCloudClient;
	
	@Value("${list_user_id_url}")
	private String listUserIdUrl;
	
	@Autowired
	private BSMessageDAO bsMessageDAO;

	/**
	 * 修改用户的系统通知的条目
	 * @param userId 用户id
	 */
	private void updateMessageCount(String userId){
		cacheHashService.hinc(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.HASH_USER_MESSAGE_COUNT,1L);
	}

	/**
	 * 清除
	 * @param userId 用户ID
	 */
	private void clearMessageCount(String userId){
		cacheHashService.hset(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.HASH_USER_MESSAGE_COUNT,"0");
	}
	
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addMessageInfo(MessageVO messageVO) {
    	
    	/*
    	 * 新增站内信消息主体
    	 */
    	MessageBody messageBodyEntity = new MessageBody();
		//主题
    	messageBodyEntity.setTitle(messageVO.getBody().getTitle());
		//正文,默认自定义
    	messageBodyEntity.setContent(messageVO.getBody().getContent());
    	if (messageVO.getBody().getTemplateFlag()){
    		String template = paramTemplateDAO.findByParamKey(messageVO.getBody().getTemplateKey().getKey());
    		if (StringUtils.isEmpty(template)){
    			log.warn("can't find param_value by paramTemplateDAO.findByParamKey(param_key),[param_key:{}]",messageVO.getBody().getTemplateKey());
    			return ErrorCodeConst.ERROR_MESSSAGE_CONTENT_NULL;
    		}
    		for(Map.Entry<String, String> entry : messageVO.getBody().getContentMap().entrySet()){
    			StringBuilder sb = new StringBuilder();
    			sb.append("${").append(entry.getKey()).append("}");
    			template = template.replace(sb.toString(), entry.getValue());
    		}
    		messageBodyEntity.setContent(template);
    	}
		//创建时间
    	messageBodyEntity.setSendTime(new Date());
		//所在实体域
    	messageBodyEntity.setRealm(messageVO.getBody().getRealm());
		//消息类型：系统消息
    	messageBodyEntity.setType(messageVO.getBody().getType());
		//是否全部通知
    	messageBodyEntity.setAllReceiver(messageVO.getBody().getAllReceiver());
    	messageBodyEntity.setId(idGen.nextId());
    	messageBodyDAO.save(messageBodyEntity);
    	
    	//是否全部通知
    	List<Long> receivers;
    	if (messageVO.getBody().getAllReceiver()){
    		JSONObject param = new JSONObject();
    		String json = springCloudClient.post(listUserIdUrl, param.toString());
    		ResultData<List<Long>> resultData = BaseJsonUtil.parseObject(json, new TypeReference<ResultData<List<Long>>>(){});
    		receivers = resultData.getData();
    	} else{
    		receivers = messageVO.getReceivers();
    	}

    	/*
    	 * 系统站内信记录
    	 */
    	List<Message> mailMessageList = new LinkedList<>();
    	String sender = StringUtils.isEmpty(messageVO.getSender())?Constants.MESSAGE_SENDER_SYSTEM : messageVO.getSender();
    	for(Long receiver : receivers){
    		Message mailMessage = new Message();
    		mailMessage.setBody(messageBodyEntity);
			//接收者
    		mailMessage.setReceiver(receiver);
			//发送者
    		mailMessage.setSender(sender);
    		mailMessage.setMessageStatus(MessageStatus.NEW);
    		mailMessage.setId(idGen.nextId());
    		mailMessageList.add(mailMessage);

			//更新缓存
			updateMessageCount(String.valueOf(receiver));
    	}
    	messageDao.save(mailMessageList);

    	return Constants.RETURN_SUCESS;
    }
    
    
    

	@Override
	@Transactional(readOnly = true,rollbackFor = Exception.class)
	public PageDTO<MessageVVO> listMessageInfoBySender(MessageDTO messageDTO) {
		
		//获取站内信列表信息
		PageDTO<Object[]> pageDTO = bsMessageDAO.listBSMessage(messageDTO);
		List<Object[]> objs = pageDTO.getRows();
		
		List<MessageVVO> messageVVOList = new LinkedList<>();
		for (Object[] obj : objs){
			MessageVVO messageViewVO = new MessageVVO();
			messageViewVO.setSender(String.valueOf(obj[0]));
			
			MessageBodyVVO messageBodyViewVO = new MessageBodyVVO();
			messageBodyViewVO.setId(Long.parseLong(String.valueOf(obj[1])));
			messageBodyViewVO.setTitle(String.valueOf(obj[2]));
			messageBodyViewVO.setContent(String.valueOf(obj[3]));
			messageBodyViewVO.setSendTime((Date)obj[4]);
			
			messageViewVO.setBody(messageBodyViewVO);
			messageVVOList.add(messageViewVO);
		}
		PageDTO<MessageVVO> messageViewVOPage = new PageDTO<>();
		messageViewVOPage.setNumber(pageDTO.getNumber());
		messageViewVOPage.setRows(messageVVOList);
		messageViewVOPage.setTotalElements(pageDTO.getTotalElements());
		messageViewVOPage.setTotalPages(pageDTO.getTotalPages());
		return messageViewVOPage;
	}

	@Override
	public PageDTO<MessageVVO> listMessageInfoByReceiver(
			Long receiver, PageRequest pageRequest) {

			if (receiver == null){
				log.warn("receiver is null,[receiver:]");
			return new PageDTO<>();
		}
		
		Page<Message> messageList = messageDao.findAll(QMessage.message.receiver.eq(receiver), pageRequest);
		PageDTO<MessageVVO> response = new PageDTO<>();
		List<MessageVVO> messageVVOList = new LinkedList<>();
		for (Message message : messageList){
			MessageVVO messageVVO = new MessageVVO();
			messageVVO.setMessageStatus(message.getMessageStatus());
			messageVVO.setSender(message.getSender());
			MessageBodyVVO messageBodyVVO = new MessageBodyVVO();
			BeanUtil.copyPropertiesIgnoreNullFilds(message.getBody(), messageBodyVVO);
			messageVVO.setBody(messageBodyVVO);
			messageVVOList.add(messageVVO);
		}
		//清除缓存
		clearMessageCount(String.valueOf(receiver));
		response.setNumber(messageList.getNumber()+1);
		response.setRows(messageVVOList);
		response.setTotalElements(messageList.getTotalElements());
		response.setTotalPages(messageList.getTotalPages());
		return response;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String updateStatus(Long receiver, Long bodyId) {
		if (receiver == null || bodyId == null){
			log.warn("param is null,[receiver:{},bodyId:{}]", receiver, bodyId);
			return Constants.COMMON_MISSING_PARAMS;
		}
		int num = messageDao.updateMessageStatusByReceiverAndBodyId(MessageStatus.READ.name(), receiver, bodyId);
		if(num == 0){
			//这里不需要返回错误码,不然错误显示提示,太过硬性 . 设置已读更新失败,无需处理,交由后续再掉用.
			log.warn("[MessageServiceImpl][updateStatus]Update Message status, result: [num = 0],receiver:{}.bodyId:{}",receiver,bodyId);
		}

		return Constants.RETURN_SUCESS;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String deleteMessage(Long bodyId) {
		int number = messageDao.updateEnableFlagByBodyId(bodyId);
		if (number == 0){
			log.warn("[MessageServiceImpl][deleteMessage]Update Message and MessageBody enableFlag, result: [num = 0],bodyId:{}");
		}
		return Constants.RETURN_SUCESS;
	}

	/**
	 * 清空消息
	 * @param messageVO messageVO
	 * @return 结果码
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String clearMessage(MessageVO messageVO){
		Iterable<Message> messages = messageDao.findAll(getInputCondition(messageVO));
		Iterator<Message> iterator = messages.iterator();
		while(iterator.hasNext()){
			Message message = iterator.next();
			MessageBody messageBody = message.getBody();
			message.setEnableFlag(EnableFlag.N);
			message.setLastUpdateDate(new Date());
			messageBody.setEnableFlag(EnableFlag.N);
			messageBody.setLastUpdateDate(new Date());
			messageBodyDAO.save(messageBody);
			messageDao.save(message);
		}
		return Constants.RETURN_SUCESS;
	}

	/**
	 * 多条件查询帖子信息
	 * 根据多种情况查询
	 * 包括like:message,eq:sender,receiver,relativeId,articleMsgType
	 * @param messageVO 查询条件对象
	 * @return Predicate
	 */
	private Predicate getInputCondition(MessageVO messageVO)
	{
		List<BooleanExpression> predicates = new ArrayList<>();
		if(null != messageVO)
		{
			if(!CollectionUtils.isEmpty(messageVO.getReceivers()) && messageVO.getReceivers().size() > 0)
			{
				predicates.add(QMessage.message.receiver.in(messageVO.getReceivers()));
			}

		}
		predicates.add(QMessage.message.enableFlag.eq(EnableFlag.Y));
		return BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
	}

}
