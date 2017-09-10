package com.apec.systemuser.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.systemuser.model.SysUser;

import java.util.List;

public interface SysUserDao extends BaseDAO<SysUser, Long>
{

    /**
     * 根据ID集合查询数据
     * @param ids ID集
     * @param enableFlag　状态控制
     * @return List
     */
    List<SysUser> findByIdInAndEnableFlag(List<Long> ids, EnableFlag enableFlag);
}
