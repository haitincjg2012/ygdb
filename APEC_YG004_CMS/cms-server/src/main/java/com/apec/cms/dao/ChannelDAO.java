package com.apec.cms.dao;


import com.apec.cms.model.Channel;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;

import java.util.List;

/**
 * Title: CMS-DAO
 * @author yirde  2017/3/21
 */
public interface ChannelDAO extends BaseDAO<Channel, Long> {

    /**
     * 根据 Enable查询， 按栏目类型排序
     * @param enableFlag EnableFlag
     * @return ListC
     */
    List<Channel> findByEnableFlagOrderByCategory(EnableFlag enableFlag);

    /**
     * 根据状态和EnableFlag 状态 统计
     * @param code 栏目编码
     * @param enableFlag 状态控制
     * @return 统计结果
     */
    Long countByCodeAndEnableFlag(String code,EnableFlag enableFlag);

    /**
     * 根据Code查询
     * @param code 编码
     * @param enableFlag 状态
     * @return Channel
     */
    Channel findByCodeAndEnableFlag(String code,EnableFlag enableFlag);

}
