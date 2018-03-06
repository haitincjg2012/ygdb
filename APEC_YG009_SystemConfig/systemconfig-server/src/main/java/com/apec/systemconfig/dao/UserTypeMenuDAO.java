package com.apec.systemconfig.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.enumtype.UserType;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.systemconfig.model.UserTypeMenu;

import java.util.List;

/**
 * Created by hmy on 2017/12/1.
 *
 * @author hmy
 */
public interface UserTypeMenuDAO extends BaseDAO<UserTypeMenu,Long> {

    /**
     * 查询用户身份对应的应该展示的字段信息
     * @param userType 用户身份
     * @param enableFlag 状态码
     * @return 菜单
     */
    List<UserTypeMenu> findByUserTypeAndEnableFlagOrderBySort(UserType userType, EnableFlag enableFlag);
}
