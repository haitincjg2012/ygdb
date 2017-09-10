package com.apec.user.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.enumtype.UserRealAuth;
import com.apec.user.model.User;
import com.apec.framework.jpa.dao.BaseDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * Title: 客户DAO
 * @author yirde  2017/3/21
 */
public interface UserDAO extends BaseDAO<User, Long> {

    /**
     * 通过手机号查找用户
     * @param mobile 手机号
     * @param enableFlag 状态码
     * @return User对象
     */
    User findByMobileAndEnableFlag(String mobile,EnableFlag enableFlag);

    /**
     * 统计该手机号用户对象在数据表中的个数
     * @param mobile
     * @param enableFlag
     * @return
     */
    Long countByMobileAndEnableFlag(String mobile,EnableFlag enableFlag);

    /**
     * 通过身份证查找用户
     * @param idNumber 身份证号
     * @param enableFlag 状态码
     * @return User对象
     */
    List<User> findByIdNumberAndEnableFlagOrderByCreateDateDesc(String idNumber, EnableFlag enableFlag);

    /**
     * 统计该身份证用户个数
     * @param idNumber
     * @param enableFlag
     * @return
     */
    Long countByIdNumberAndEnableFlag(String idNumber, EnableFlag enableFlag);

    /**
     * 批量删除用户信息
     * @param ids
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update user set enable_flag = 'N',last_update_date = now(),last_update_by = :userId where id in :ids and enable_flag = 'Y'",nativeQuery = true)
    int deleteUserList(@Param("ids") List<Long> ids,@Param("userId") String userId);

}
