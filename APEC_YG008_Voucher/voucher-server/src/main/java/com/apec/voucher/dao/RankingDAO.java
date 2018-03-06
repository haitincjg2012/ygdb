package com.apec.voucher.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.enumtype.RankingType;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.voucher.model.Ranking;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * Created by hmy on 2017/12/19.
 *
 * @author hmy
 */
public interface RankingDAO extends BaseDAO<Ranking,Long>{

    /**
     * 查询在线的排行榜信息
     * @param rankingType 排行榜类型
     * @param enableFlag 状态码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 在线的排行榜信息
     */
    Ranking findFirstByRankingTypeAndEnableFlagAndStartTimeBeforeAndEndTimeAfterOrderByCreateDateDesc(RankingType rankingType ,EnableFlag enableFlag, Date startTime, Date endTime);

    /**
     * 查询是否有该时刻内的排行榜信息
     * @param rankingType 排行榜类型
     * @param enableFlag 状态码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 在线的排行榜信息
     */
    @Query(value = "select count(id) from ranking where ranking_type = ?1 and enable_flag = ?2 and  (?3 between start_time and end_time " +
            " or ?4 between start_time and end_time or start_time between ?3 and ?4 or end_time between ?3 and ?4)  ",nativeQuery = true)
    Long countByRankingType(String rankingType ,String enableFlag, Date startTime, Date endTime);

}
