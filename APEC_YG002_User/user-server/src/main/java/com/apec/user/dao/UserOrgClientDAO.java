package com.apec.user.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.enumtype.UserAccountType;
import com.apec.framework.common.enumtype.UserType;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.user.dto.UserDTO;
import com.apec.user.model.UserOrgClient;
import com.mysema.query.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Title: 用户组织
 *
 * @author yirde  2017/9/6.
 */
public interface UserOrgClientDAO extends BaseDAO<UserOrgClient,Long> {

    /**
     * 查询相应类型的组织信息
     * @return
     */
    List<UserOrgClient> findByUserAccountTypeAndEnableFlag(UserAccountType userAccountType, EnableFlag enableFlag);

    /**
     * 查询是否认证的组织信息
     * @return
     */
    List<UserOrgClient> findByPushFlagAndEnableFlag(boolean pushFlag, EnableFlag enableFlag);

    /**
     * 查询相应类型的组织信息
     * @return
     */
    UserOrgClient findByIdAndEnableFlag(Long id, EnableFlag enableFlag);


    /**
     * 查询相应类型的组织信息
     * @return
     */
    Page<UserOrgClient> findByIdInAndEnableFlag(List<Long> id, EnableFlag enableFlag,Pageable pageable);

    /**
     * 查询相应类型的组织信息
     * @return
     */
    Page<UserOrgClient> findByIdInAndEnableFlagAndAddressLike(List<Long> id, EnableFlag enableFlag,String address,Pageable pageable);

    /**
     * 查询所有已经推送的数据
     * @param pushFlag
     * @param enableFlag
     * @return
     */
    List<UserOrgClient> findByPushFlagAndEnableFlag(Integer pushFlag, EnableFlag enableFlag);

}
