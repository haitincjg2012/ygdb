package com.apec.game.dao;

import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.game.model.Lottery;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * Created by hmy on 2017/12/21.
 *
 * @author hmy
 */
public interface LotteryDAO extends BaseDAO<Lottery,Long>{

    /**
     * 查询同时期内是否存在抽奖活动
     * @param enableFlag 状态码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计数量
     */
    @Query(value = "select count(id) from lottery where enable_flag = ?1 and  (?2 between start_time and end_time " +
            " or ?3 between start_time and end_time or start_time between ?2 and ?3 or end_time between ?2 and ?3)  ",nativeQuery = true)
    Long countBySameTimeLottery(String enableFlag, Date startTime, Date endTime);


}
