package com.apec.user.service;

import com.apec.user.vo.UserActiveInfoVO;

/**
 * Created by hmy on 2018/2/7.
 *
 * @author hmy
 */
public interface UserActiveInfoService {

    /**
     * 增加使用活动信息
     * @param userActiveInfoVO 信息
     * @return 处理码
     */
    String addUserActiveInfo(UserActiveInfoVO userActiveInfoVO);

    /**
     * 修改使用活动信息
     * @param userActiveInfoVO 信息
     * @return 处理码
     */
    String updateUserActiveInfo(UserActiveInfoVO userActiveInfoVO);

    /**
     * 查询记录信息
     * @param userActiveInfoVO 记录唯一标识
     * @return 记录信息
     */
    UserActiveInfoVO findUserActiveInfo(UserActiveInfoVO userActiveInfoVO);

}
