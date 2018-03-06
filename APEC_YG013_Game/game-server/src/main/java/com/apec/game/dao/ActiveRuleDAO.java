package com.apec.game.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.game.model.ActiveRule;

/**
 * Created by hmy on 2017/12/21.
 *
 * @author hmy
 */
public interface ActiveRuleDAO extends BaseDAO<ActiveRule,Long> {

    /**
     * 查询抽奖活动的活动规则
     * @param lotteryId 活动id
     * @param enableFlag 状态码
     * @return 活动规则
     */
    ActiveRule findByLotteryIdAndEnableFlag(Long lotteryId, EnableFlag enableFlag);

}
