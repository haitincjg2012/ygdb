package com.apec.user.service;

import com.apec.framework.common.PageDTO;
import com.apec.framework.common.enumtype.UserStatus;
import com.apec.user.dto.UserAuthRecordDTO;
import com.apec.user.dto.UserDTO;
import com.apec.user.vo.*;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 用户相关服务
 */
public interface UserService {

    /**
     * 注册新用户
     * @param userVO 用户VO
     * @return ReturnCode 返回码
     */
    String addNewUser(UserVO userVO);

    /**
     * 选择符合用户条件的所有用户
     * @param vo 用户VO
     * @return List 数据集合
     */
    List<UserVO> selectAll(UserVO vo);

    /**
     * 修改用户信息
     * @param userVO 用户VO
     * @param userId 用户ID
     * @return ReturnCode 返回码
     */
    String updateUserInfo(UserVO userVO,String userId, Map<String,String> resultData);

    /**
     *  修改用户的手机号
     * @param userVO 用户VO
     * @param userId 用户ID
     * @return
     */
    String updateUserMobile(UserVO userVO,String userId,Map<String,String> resultMap);

    /**
     * 用户快速登陆
     * @param userLoginVO LoginVO
     * @param tokenOutTime Token超时时间
     * @param resultMap 结果集合
     *  @param isFirstLogin 是否为注册登录
     * @return ReturnCode 返回码
     */
    String quickLogin(UserLoginVO userLoginVO, int tokenOutTime, Map<String,String> resultMap,boolean isFirstLogin);

    /**
     * 冻结 / 解冻用户
     * @param userId 用户ID
     * @return ReturnCode 返回码
     */
    String freezeOrUnFreeze(String userId, UserStatus userStatus);

    /**
     * 用户实名认证
     * @param authRecordVO 用户实名认证记录
     * @param userId
     * @return
     */
    String userRealNameApply(UserAuthRecordVO authRecordVO, String userId,Map<String,String> resultMap);

    /**
     * 用户实名认证审批确认
     * @param authRecordVO 用户实名认证记录
     * @param userId
     * @return
     */
    String userRealNameEntrue(UserAuthRecordVO authRecordVO,String userId,Map<String,String> resultMap);

    /**
     * 分页查询用户实名认证记录信息,可带条件查询
     * @param dto
     * @param pageRequest
     * @return
     */
    PageDTO<UserAuthRecordViewVO> pageUserRealNameRecord(UserDTO dto, PageRequest pageRequest);

    /**
     * 手机号是否已经注册
     * @param userVO
     * @return
     */
    boolean isExist(UserVO userVO);

    /**
     * 找回密码
     * @param userVO
     * @param userId
     * @return
     */
    String getPassword(UserVO userVO,String userId);

    /**
     * 修改密码
     * @param userVO
     * @param userId
     * @return
     */
    String updatePassword(UserVO userVO,String userId);

    /**
     * 分页查询用户信息（管理端）
     * @param dto
     * @param pageRequest
     * @return
     */
    PageDTO<UserViewVO> pageUserInfo(UserDTO dto, PageRequest pageRequest);

    /**
     * 查询用户信息
     * @param vo
     * @return
     */
    List<UserViewVO> listUserInfo(UserVO vo);

    /**
     * 删除用户信息
     * @param userVO
     * @param userId
     * @return
     */
    String deleteUserInfo(UserVO userVO,String userId);

    /**
     * 查询用户信息
     * @param userVO
     * @return
     */
    UserViewVO findUserInfo(UserVO userVO);

    /**
     * 查询用户信息
     * @param userVO
     * @return
     */
    List<Long> listUserId(UserVO userVO);

    /**
     * 批量删除用户信息
     * @param ids
     * @return
     */
    String deleteUserList(List<Long> ids,String userId);


}
