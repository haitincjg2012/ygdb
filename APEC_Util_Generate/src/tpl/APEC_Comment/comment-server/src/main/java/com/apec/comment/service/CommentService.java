package com.apec.comment.service;

import com.apec.framework.dto.UserInfoVO;
/**
 * 评论相关服务
 */
public interface CommentService {

    /**
     * 添加新的供求
     * @param userInfoVO 用户信息VO
     * @return returnCode 返回码
     */
    String addProductAttr(UserInfoVO userInfoVO);

}
