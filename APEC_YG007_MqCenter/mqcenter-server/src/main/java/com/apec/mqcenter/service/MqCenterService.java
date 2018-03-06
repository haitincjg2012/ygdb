package com.apec.mqcenter.service;

import com.apec.mqcenter.vo.MQMessageConsumerLogVO;

/**
 * Mq消费 相关服务
 * @author xxx
 */
public interface MqCenterService {


    /**
     * 根据msgId查看消息是否已经入库
     * @param tag  tag
     * @param keys  唯一kEY
     * @return boolean
     */
     boolean findMessageConsumerIsExit(String tag,String keys);

    /**
     * 添加MessageConsumer
     * @param mqMessageConsumerLogVO consumerVOs
     * @return Code
     */
     String addMessageConsumer(MQMessageConsumerLogVO mqMessageConsumerLogVO);
}
