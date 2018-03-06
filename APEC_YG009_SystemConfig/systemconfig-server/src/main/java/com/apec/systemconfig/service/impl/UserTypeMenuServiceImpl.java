package com.apec.systemconfig.service.impl;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.enumtype.UserType;
import com.apec.framework.common.util.BeanUtil;
import com.apec.systemconfig.dao.UserTypeMenuDAO;
import com.apec.systemconfig.model.UserTypeMenu;
import com.apec.systemconfig.service.UserTypeMenuService;
import com.apec.systemconfig.vo.UserTypeMenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hmy on 2017/12/1.
 *
 * @author hmy
 */
@Service
public class UserTypeMenuServiceImpl implements UserTypeMenuService {

    @Autowired
    private UserTypeMenuDAO userTypeMenuDAO;

    @Override
    public List<UserTypeMenuVO> findMenuByUserType(UserType userType) {
        List<UserTypeMenuVO> result = new ArrayList<>();
        List<UserTypeMenu> list = userTypeMenuDAO.findByUserTypeAndEnableFlagOrderBySort(userType, EnableFlag.Y);
        list.forEach(userTypeMenu -> {
            UserTypeMenuVO userTypeMenuVO = new UserTypeMenuVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(userTypeMenu,userTypeMenuVO);
            result.add(userTypeMenuVO);
        });
        return result;
    }
}
