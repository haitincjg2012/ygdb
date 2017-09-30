package com.apec.user.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.enumtype.UserAccountType;
import com.apec.user.model.User;
import com.apec.framework.jpa.dao.BaseDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
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
     * 通过用户查找用户
     * @param id 手机号
     * @param enableFlag 状态码
     * @return User对象
     */
    User findByIdAndEnableFlag(Long id,EnableFlag enableFlag);

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
     * 根据用户组织ID查询记录
     * @param userOrgId 用户组织ID
     * @param enableFlag 用户状态
     * @return
     */
    List<User> findByUserOrgIdAndEnableFlag(Long userOrgId,EnableFlag enableFlag);

    /**
     * 通过组织id和用户账号类型查询用户
     * @param userOrgId
     * @param enableFlag
     * @param userAccountType
     * @return
     */
    List<User> findByUserOrgIdAndEnableFlagAndUserAccountType(Long userOrgId, EnableFlag enableFlag, UserAccountType userAccountType);

    /**
     * 通过组织id和用户账号类型查询用户
     * @param userOrgId
     * @param enableFlag
     * @param userAccountType
     * @return
     */
    List<User> findByUserOrgIdAndEnableFlagAndUserAccountTypeNot(Long userOrgId, EnableFlag enableFlag, UserAccountType userAccountType);

    /**
     * 统计该身份证用户个数
     * @param idNumber
     * @param enableFlag
     * @return
     */
    Long countByIdNumberAndEnableFlag(String idNumber, EnableFlag enableFlag);

    /**
     * 批量冻结用户信息
     * @param ids
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update user set user_status = 'FREEZE',last_update_date = now(),last_update_by = :userId where id in :ids and enable_flag = 'Y'",nativeQuery = true)
    int deleteUserList(@Param("ids") List<Long> ids,@Param("userId") String userId);

    /**
     * 通过用户身份和组织ids查询组织id
     * @param userType
     * @param userOrgId
     * @return
     */
    @Query(value = "select distinct(user_org_id) from user where User_org_id in (:userOrgId) and enable_flag = 'Y' and user_type = :userType and user_account_type != 'ORG_CHILD_ACCOUNT'",nativeQuery = true)
    List<BigInteger> findUserOrgIdsByUserTypeAndUserOrgId(@Param("userType") String userType, @Param("userOrgId")List<Long> userOrgId);

    /**
     * 通过组织ids查询用户信息，分页
     * @param userOrgId
     * @return
     */
    Page<User> findByEnableFlagAndUserOrgIdIn(EnableFlag enableFlag , List<Long> userOrgId, Pageable pageable);
//    @Query(value = "select * from user where User_org_id in (:userOrgId) and enable_flag = 'Y'  /**#pageable**/\n  ",
//    countQuery = "select count(*) from user where User_org_id in (:userOrgId) and enable_flag = 'Y' ",nativeQuery = true)

}
