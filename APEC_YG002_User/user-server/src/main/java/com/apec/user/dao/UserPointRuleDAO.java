package com.apec.user.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.enumtype.PointRuleType;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.user.model.UserPointRule;

import java.util.List;

/**
 * 用户积分规则
 * Created by hmy on 2017/7/13.
 */
public interface UserPointRuleDAO extends BaseDAO<UserPointRule,Long> {

    //查询积分规则扣分规则
    List<UserPointRule> findByPointRuleTypeAndEnableFlagOrderByCreateDateDesc(PointRuleType pointRuleType, EnableFlag enableFlag);

    UserPointRule findByIdAndEnableFlag(Long id, EnableFlag enableFlag);

}
