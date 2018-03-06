package com.apec.mqcenter.service.impl;

import com.apec.framework.common.Constants;
import com.apec.mqcenter.dao.MQMessageConsumerDao;
import com.apec.mqcenter.model.MQMessageConsumerLog;
import com.apec.mqcenter.service.MqCenterService;
import com.apec.framework.log.InjectLogger;
import com.apec.mqcenter.vo.MQMessageConsumerLogVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author xxx
 */
@Service
public class MqCenterServiceImpl implements MqCenterService {


    @InjectLogger
    private Logger logger;

    @Autowired
    MQMessageConsumerDao mqMessageConsumerDao;

    @Override
    public boolean findMessageConsumerIsExit(String tag,String keys) {
        if(StringUtils.isBlank(tag) || StringUtils.isBlank(keys)){
            return false;
        }

        Long count = mqMessageConsumerDao.countByMsgKeyAndTag(keys,tag);
        return count != 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addMessageConsumer(MQMessageConsumerLogVO mqMessageConsumerLogVO) {
        if(StringUtils.isBlank(mqMessageConsumerLogVO.getTopic()) || StringUtils.isBlank(mqMessageConsumerLogVO.getTag()) ||
                StringUtils.isBlank(mqMessageConsumerLogVO.getKeys()) || StringUtils.isBlank(mqMessageConsumerLogVO.getMsgId()) ){
            logger.warn("Can't add MessageConsumer,Parameter verification failed!");
            return Constants.COMMON_MISSING_PARAMS;
        }
        MQMessageConsumerLog mqMessageConsumerLog = new MQMessageConsumerLog();
        mqMessageConsumerLog.setTopic(mqMessageConsumerLogVO.getTopic());
        mqMessageConsumerLog.setTag(mqMessageConsumerLogVO.getTag());
        mqMessageConsumerLog.setMsgId(mqMessageConsumerLogVO.getMsgId());
        mqMessageConsumerLog.setMsgKey(mqMessageConsumerLogVO.getKeys());
        mqMessageConsumerLog.setBody(mqMessageConsumerLogVO.getBody());
        mqMessageConsumerLog.setOrderlyKey(mqMessageConsumerLogVO.getOrderlyKey());
        mqMessageConsumerLog.setCreateTime(new Date());
        mqMessageConsumerLog.setConsumerTime(mqMessageConsumerLogVO.getConsumerTime());
        mqMessageConsumerLog.setConsumerEndTime(mqMessageConsumerLogVO.getConsumerEndTime());
        mqMessageConsumerLog.setStatus(mqMessageConsumerLogVO.getStatus());
        mqMessageConsumerDao.save(mqMessageConsumerLog);
        return Constants.RETURN_SUCESS;
    }


}
