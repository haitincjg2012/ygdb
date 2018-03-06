package com.apec.game.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.game.model.LotteryHint;

/**
 * Created by hmy on 2017/12/21.
 *
 * @author hmy
 */
public interface LotteryHintDAO extends BaseDAO<LotteryHint,Long>{

    /**
     * 查询抽奖活动的提示信息
     * @param lotteryId 抽奖活动id
     * @param enableFlag 状态码
     * @return 提示信息
     */
    Iterable<LotteryHint> findByLotteryIdAndEnableFlag(Long lotteryId, EnableFlag enableFlag);

}
