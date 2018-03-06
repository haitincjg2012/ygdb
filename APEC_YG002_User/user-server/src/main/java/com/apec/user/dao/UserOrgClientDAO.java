package com.apec.user.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.enumtype.UserAccountType;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.user.model.UserOrgClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Title: 用户组织
 *
 * @author yirde  2017/9/6.
 */
public interface UserOrgClientDAO extends BaseDAO<UserOrgClient,Long> {

    /**
     * 查询相应类型的组织信息
     * @param userAccountType 用户账号类型
     * @param enableFlag 状态码
     * @return List<UserOrgClient>
     */
    List<UserOrgClient> findByUserAccountTypeAndEnableFlag(UserAccountType userAccountType, EnableFlag enableFlag);

    /**
     * 查询是否认证的组织信息
     * @param pushFlag 是否认证
     * @param enableFlag 状态码
     * @return 组织集合
     */
    List<UserOrgClient> findByPushFlagAndEnableFlag(boolean pushFlag, EnableFlag enableFlag);

    /**
     * 查询相应类型的组织信息
     * @param id id
     * @param enableFlag 状态码
     * @return 组织对象
     */
    UserOrgClient findByIdAndEnableFlag(Long id, EnableFlag enableFlag);


    /**
     * 查询相应类型的组织信息
     * @param id id
     * @param enableFlag 状态码
     * @param pageable 分页对象
     * @return 组织分页结果
     */
    Page<UserOrgClient> findByIdInAndEnableFlag(List<Long> id, EnableFlag enableFlag,Pageable pageable);

    /**
     * 查询相应类型的组织信息
     * @param id id
     * @param enableFlag 状态码
     * @param address 地址
     * @param pageable 分页对象
     * @return 组织分页结果
     */
    Page<UserOrgClient> findByIdInAndEnableFlagAndAddressLike(List<Long> id, EnableFlag enableFlag,String address,Pageable pageable);

}
