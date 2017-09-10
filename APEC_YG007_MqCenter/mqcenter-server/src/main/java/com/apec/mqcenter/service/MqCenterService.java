package com.apec.mqcenter.service;

import com.apec.mqcenter.vo.MQMessageConsumerLogVO;

/**
 * Mq消费 相关服务
 */
public interface MqCenterService {


    /**
     * 根据msgId查看消息是否已经入库
     * @param keys  唯一kEY
     * @return
     */
     boolean findMessageConsumerIsExit(String tag,String keys);

    /**
     * 添加MessageConsumer
     * @param mqMessageConsumerLogVO consumerVOs
     * @return Code
     */
     String addMessageConsumer(MQMessageConsumerLogVO mqMessageConsumerLogVO);
}
