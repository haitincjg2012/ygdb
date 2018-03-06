package com.apec.game.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.enumtype.LotteryFormType;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.game.model.LotteryForm;

/**
 * Created by hmy on 2017/12/21.
 *
 * @author hmy
 */
public interface LotteryFormDAO extends BaseDAO<LotteryForm,Long>{

    /**
     * 抽奖形式
     * @param lotteryFormType 抽奖形式
     * @param enableFlag 状态码
     * @return 抽奖形式对象
     */
    LotteryForm findByLotteryFormTypeAndEnableFlag(LotteryFormType lotteryFormType, EnableFlag enableFlag);


}
