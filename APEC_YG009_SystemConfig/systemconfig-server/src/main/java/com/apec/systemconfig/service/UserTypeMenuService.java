package com.apec.systemconfig.service;

import com.apec.framework.common.enumtype.UserType;
import com.apec.systemconfig.vo.UserTypeMenuVO;

import java.util.List;

/**
 * Created by hmy on 2017/12/1.
 *
 * @author hmy
 */
public interface UserTypeMenuService {

    List<UserTypeMenuVO> findMenuByUserType(UserType userType);

}
