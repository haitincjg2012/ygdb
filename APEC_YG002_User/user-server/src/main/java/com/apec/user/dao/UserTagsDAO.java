package com.apec.user.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.user.model.UserTags;

import java.util.List;

/**
 * Title: 用户标签DAO
 *
 * @author yirde  2017/9/12.
 */
public interface UserTagsDAO  extends BaseDAO<UserTags, Long> {

    /**
     * 查找组织账户标签
     * @param userOrgId 组织id
     * @param enableFlag 状态码
     * @return 标签
     */
     List<UserTags> findByUserOrgIdAndEnableFlagOrderBySort(Long userOrgId,EnableFlag enableFlag);
}
