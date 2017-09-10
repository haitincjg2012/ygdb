package com.apec.user.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.user.model.UserLevelRule;

/**
 * Title: 用户等级规则表
 *
 * @author yirde  2017/7/29.
 */
public interface UserLevelRuleDAO extends BaseDAO<UserLevelRule, Long> {

    /**
     * 查询用户积分所对应的等级
     * @param point 用户积分
     * @param enableFlag 状态控制
     * @return
     */
    UserLevelRule findFirstByPointLessThanEqualAndEnableFlagOrderByPointDesc(Integer point, EnableFlag enableFlag);



}
