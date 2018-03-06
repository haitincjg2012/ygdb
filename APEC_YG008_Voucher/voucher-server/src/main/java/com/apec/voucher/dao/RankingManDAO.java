package com.apec.voucher.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.voucher.model.RankingMan;

/**
 * Created by hmy on 2017/12/19.
 *
 * @author hmy
 */
public interface RankingManDAO extends BaseDAO<RankingMan,Long> {

    /**
     * 查询某个排行榜的上榜用户
     * @param rankingId 排行榜id
     * @param enableFlag 状态码
     * @return 上榜条件
     */
    Iterable<RankingMan> findByRankingIdAndEnableFlag(Long rankingId, EnableFlag enableFlag);

    /**
     * 删除排行榜的上榜条件
     * @param rankingId 排行榜id
     * @param enableFlag 状态码
     * @return 改变行数
     */
    int deleteByRankingIdAndEnableFlag(Long rankingId, EnableFlag enableFlag);

}
