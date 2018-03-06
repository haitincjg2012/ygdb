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
 * @author hmy
 */
public interface UserPointRecordDAO extends BaseDAO<UserPointRecord,Long> {

    /**
     * 通过用户id分页查询积分记录信息
     * @param userId 用户id
     * @param enableFlag 状态码
     * @param pageable 分页对象
     * @return 积分分页记录
     */
    Page<UserPointRecord> findByUserIdAndEnableFlag(Long userId, EnableFlag enableFlag, Pageable pageable);

    /**
     * 统计用户积分记录的总积分
     * @param userId 用户id
     * @param enableFlag 状态码
     * @return 总积分
     */
    @Query(value = "select ifnull(sum(points_changed),0) as pointsChanged from user_point_record where user_id = :userId and enable_flag = :enableFlag ",nativeQuery = true)
    Object[] findSumPointsByUserId(@Param("userId") Long userId, @Param("enableFlag") String enableFlag);

    /**
     * 查询某用户通过某种来源获得的积分记录
     * @param userId 用户id
     * @param enableFlag 状态码
     * @param pointRuleType 积分类型
     * @return 积分记录集合
     */
    List<UserPointRecord> findByUserIdAndEnableFlagAndPointRuleType(Long userId, EnableFlag enableFlag, PointRuleType pointRuleType);

}
