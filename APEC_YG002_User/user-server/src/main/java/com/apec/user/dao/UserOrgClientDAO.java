package com.apec.user.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.enumtype.UserAccountType;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.user.model.UserOrgClient;

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

}
