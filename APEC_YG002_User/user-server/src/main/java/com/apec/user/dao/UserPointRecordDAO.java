package com.apec.user.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.enumtype.PointRuleType;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.user.model.UserPointRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 用户的积分记录
 * Created by hmy on 2017/7/6.
 */
public interface UserPointRecordDAO extends BaseDAO<UserPointRecord,Long> {

    Page<UserPointRecord> findByUserIdAndEnableFlag(Long userId, EnableFlag enableFlag, Pageable pageable);

    /**
     * 统计用户积分记录的总积分
     * @param userId
     * @param enableFlag
     * @return
     */
    @Query(value = "select ifnull(sum(points_changed),0) as pointsChanged from user_point_record where user_id = :userId and enable_flag = :enableFlag ",nativeQuery = true)
    Object[] findSumPointsByUserId(@Param("userId") Long userId, @Param("enableFlag") String enableFlag);

    /**
     * 查询某用户通过某种来源获得的积分记录
     * @param userId
     * @param enableFlag
     * @param pointRuleType
     * @return
     */
    List<UserPointRecord> findByUserIdAndEnableFlagAndPointRuleType(Long userId, EnableFlag enableFlag, PointRuleType pointRuleType);

}
