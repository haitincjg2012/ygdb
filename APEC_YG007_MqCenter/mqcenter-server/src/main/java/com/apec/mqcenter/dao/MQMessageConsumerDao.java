package com.apec.mqcenter.dao;

import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.mqcenter.model.MQMessageConsumerLog;

/**
 * Title:
 *
 * @author yirde  2017/7/4.
 */
public interface MQMessageConsumerDao  extends BaseDAO<MQMessageConsumerLog,String> {

    /**
     * 统计
     * @param keys keys
     * @param tag tag
     * @return Long
     */
    Long countByMsgKeyAndTag(String keys, String tag);


}
