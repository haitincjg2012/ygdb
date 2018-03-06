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
     * 查询用户身份下需要填写的信息
     */
    @Query(value = "select name_str from user_type_menu where user_type = ?1 and enable_flag = 'Y'  ",nativeQuery = true)
    List<String> findUserTypeMenuForUserType(String userType);

    /**
     * 统计该手机号用户对象在数据表中的个数
     * @param mobile 手机号
     * @param enableFlag 状态码
     * @return 个数
     */
    Long countByMobileAndEnableFlag(String mobile,EnableFlag enableFlag);

    /**
     * 根据用户组织ID查询记录
     * @param userOrgId 用户组织ID
     * @param enableFlag 用户状态
     * @return 用户集合
     */
    List<User> findByUserOrgIdAndEnableFlag(Long userOrgId,EnableFlag enableFlag);

    /**
     * 通过组织id和用户账号类型查询用户
     * @param userOrgId 组织好
     * @param enableFlag 状态码
     * @param userAccountType 用户账号类型
     * @return 用户集合
     */
    List<User> findByUserOrgIdAndEnableFlagAndUserAccountType(Long userOrgId, EnableFlag enableFlag, UserAccountType userAccountType);

    /**
     * 通过组织id和用户账号类型查询用户
     * @param userOrgId 组织好
     * @param enableFlag 状态码
     * @param userAccountType 用户账号类型
     * @return 用户集合
     */
    List<User> findByUserOrgIdAndEnableFlagAndUserAccountTypeNot(Long userOrgId, EnableFlag enableFlag, UserAccountType userAccountType);

    /**
     * 统计该身份证用户个数
     * @param idNumber 身份证号
     * @param enableFlag 状态码
     * @return 个数
     */
    Long countByIdNumberAndEnableFlag(String idNumber, EnableFlag enableFlag);

    /**
     * 批量冻结用户信息
     * @param ids 用户ids
     * @param userId 修改人
     * @return 改变的行数
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update user set user_status = 'FREEZE',last_update_date = now(),last_update_by = :userId where id in :ids and enable_flag = 'Y'",nativeQuery = true)
    int deleteUserList(@Param("ids") List<Long> ids,@Param("userId") String userId);

    /**
     * 通过用户身份和组织ids查询组织id
     * @param userType 用户身份
     * @param userOrgId 组织号
     * @return 组织id
     */
    @Query(value = "select distinct(user_org_id) from user where User_org_id in (:userOrgId) and enable_flag = 'Y' and user_type = :userType and user_account_type != 'ORG_CHILD_ACCOUNT'",nativeQuery = true)
    List<BigInteger> findUserOrgIdsByUserTypeAndUserOrgId(@Param("userType") String userType, @Param("userOrgId")List<Long> userOrgId);

    /**
     * 通过用户身份和组织ids查询组织id
     * @param name 用户姓名
     * @return 用户id
     */
    @Query(value = "select distinct(id) from user where name like %?1% and enable_flag = 'Y' ",nativeQuery = true)
    List<BigInteger> findIdsByNameLike(String name);

    /**
     * 通过组织ids查询用户信息，分页
     * @param userOrgId 组织ids
     * @param enableFlag 状态码
     * @param pageable 分页对象
     * @return 用户分页对象
     */
    Page<User> findByEnableFlagAndUserOrgIdIn(EnableFlag enableFlag , List<Long> userOrgId, Pageable pageable);

}
