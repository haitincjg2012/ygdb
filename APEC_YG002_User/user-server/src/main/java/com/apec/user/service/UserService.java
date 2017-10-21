package com.apec.user.service;

import com.apec.framework.common.PageDTO;
import com.apec.framework.common.enumtype.UserStatus;
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
    String addNewUser(UserVO userVO,String userId);

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
    PageDTO<UserAllInfo> pageUserInfo(UserDTO dto, PageRequest pageRequest);

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

    /**
     * 修改用户个人头像
     * @param userVO
     * @param userId
     * @param resultMap
     * @return
     */
    String updateImage(UserVO userVO,String userId, Map<String,String> resultMap);

    /**
     * 修改组织Banner图
     * @param userVO
     * @param userId
     * @param resultMap
     * @return
     */
    String updateBanner(UserVO userVO,String userId, Map<String,String> resultMap);


    /**
     * 查询所有的账户信息
     * @return
     */
    List<UserOrgClientVO> findOrgList();

    /**
     * 分页查询所有的账户信息
     * @return
     */
    PageDTO<UserOrgClientVO> findOrgPage(PageRequest pageRequest,UserOrgClientVO vo);

    /**
     * 设置用户账户信息
     * @param userVO
     * @param userId
     * @return
     */
    String pushUserAndOrg(UserVO userVO , String userId);

    /**
     * 设置组织标签信息
     * @param userOrgClientVO
     * @param userId
     * @return
     */
    String setOrgTags(UserOrgClientVO userOrgClientVO, String userId);

    /**
     * 删除组织
     * @param userOrgClientVO
     * @param userId
     * @return
     */
    String deleteOrg(UserOrgClientVO userOrgClientVO, String userId);

    /**
     * 修改组织信息
     * @param userOrgClientVO
     * @param userId
     * @return
     */
    String updateOrg(UserOrgClientVO userOrgClientVO, String userId);

    /**
     * 查询组织下的所有用户信息
     * @param userVO
     * @return
     */
    List<UserViewVO> findUserByOrgId(UserVO userVO);

    /**
     * 我的关注(组织账号信息)
     * @param userId
     * @return
     */
    PageDTO<UserAllInfo> findUserFocusOrg(Long userId,PageRequest pageRequest,UserDTO userDTO);

    /**
     * 用户解除绑定组织
     * @param userVO
     * @param userId
     * @return
     */
    String unBoundOrg(UserVO userVO,String userId);

    /**
     * 用户解除绑定组织
     * @return
     */
    UserOrgClientVO findUserOrgInfo(UserOrgClientVO userOrgClientVO);

    /**
     * 关闭认证
     * @return
     */
    String closeOrgPushFlag(UserVO userVO, String userId);

    /**
     * 批量认证
     * @param userVO
     * @param userId
     * @return
     */
    String batchAuthen(UserVO userVO, String userId);

    /**
     * 根据ES索引推送数据
     * @param indexUrl
     * @return
     */
    String pushDepotEsInfoByIndex(String indexUrl);

    /**
     * 根据ES索引推送数据
     * @param indexUrl
     * @return
     */
    String pushAgencyEsInfoByIndex(String indexUrl);

    /**
     * 根据ES索引推送数据
     * @param indexUrl
     * @return
     */
    String pushMerchantEsInfoByIndex(String indexUrl);
}
