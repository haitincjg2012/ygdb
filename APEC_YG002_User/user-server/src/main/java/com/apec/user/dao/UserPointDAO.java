package com.apec.user.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.user.model.UserPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 *
 * Created by hmy on 2017/7/6.
 */
public interface UserPointDAO extends BaseDAO<UserPoint,Long> {

    /**
     * 查询用户的积分
     * @param userId 用户ID
     * @param enableFlag 状态标识
     * @return UserPoint
     */
     UserPoint findByUserIdAndEnableFlag(Long userId, EnableFlag enableFlag);

//    /**
//     * 分页查询用户积分信息(通过用户姓名模糊查询)
//     * @param userName
//     * @param enableFlag
//     * @param pageable
//     * @return
//     */
//    Page<UserPoint> findByUser_nameLikeAndEnableFlagOrderByCreateDateDesc(String userName, EnableFlag enableFlag, Pageable pageable);

    /**
     * 修改用户积分
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update user_point set available_points = if(available_points + :points < 0,0,available_points + :points ) where user_id = :userId  ",nativeQuery = true)
    int updateUserPoints(@Param("points") int points, @Param("userId") Long userId);


}
