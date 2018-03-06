package com.apec.user.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.user.model.UserOrgImage;

import java.util.List;

/**
 * 用户组织的实例图片
 * Created by hmy on 2017/9/1.
 * @author hmy
 */
public interface UserOrgImageDAO extends BaseDAO<UserOrgImage,Long> {

    /**
     * 根据用户组织ID查询用户图片
      * @param userOrgId 用户组织ID
     * @param enableFlag 状态控制
     * @return List 数据集合
     */
   List<UserOrgImage> findByUserOrgIdAndEnableFlagOrderBySort(Long userOrgId, EnableFlag enableFlag);

}
