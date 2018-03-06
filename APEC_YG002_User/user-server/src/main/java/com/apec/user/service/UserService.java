package com.apec.user.service;

import com.apec.framework.common.PageDTO;
import com.apec.framework.common.enumtype.UserStatus;
import com.apec.framework.dto.UserInfoVO;
import com.apec.user.dto.UserAuthRecordDTO;
import com.apec.user.dto.UserDTO;
import com.apec.user.model.User;
import com.apec.user.model.UserOrgClient;
import com.apec.user.vo.*;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 用户相关服务
 * @author hmy
 */
public interface UserService {

    /**
     * 注册新用户
     * @param userVO 用户VO
     * @param userId 用户id
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
     * @param resultData 结果Map
     * @return ReturnCode 返回码
     */
    String updateUserInfo(UserVO userVO,String userId, Map<String,String> resultData);

    /**
     *  修改用户的手机号
     * @param userVO 用户VO
     * @param userId 用户ID
     * @param resultMap 结果Map
     * @return  String
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
     * @param userStatus 用户状态
     * @return ReturnCode 返回码
     */
    String freezeOrUnFreeze(String userId, UserStatus userStatus);

    /**
     * 用户实名认证
     * @param authRecordVO 用户实名认证记录
     * @param userId 用户id
     * @param resultMap 结果Map
     * @return String
     */
    String userRealNameApply(UserAuthRecordVO authRecordVO, String userId,Map<String,String> resultMap);

    /**
     * 用户实名认证审批确认
     * @param authRecordVO 用户实名认证记录
     * @param userId 用户id
     * @param resultMap 结果Map
     * @return String
     */
    String userRealNameEntrue(UserAuthRecordVO authRecordVO,String userId,Map<String,String> resultMap);

    /**
     * 分页查询用户实名认证记录信息,可带条件查询
     * @param dto dto
     * @param pageRequest pageRequest
     * @return PageDTO<UserAuthRecordViewVO>
     */
    PageDTO<UserAuthRecordViewVO> pageUserRealNameRecord(UserAuthRecordDTO dto, PageRequest pageRequest);

    /**
     * 手机号是否已经注册
     * @param userVO 用户VO
     * @return boolean
     */
    boolean isExist(UserVO userVO);

    /**
     * 找回密码
     * @param userVO 用户VO
     * @param userId 用户id
     * @return String
     */
    String getPassword(UserVO userVO,String userId);

    /**
     * 修改密码
     * @param userVO 用户VO
     * @param userId 用户id
     * @return String
     */
    String updatePassword(UserVO userVO,String userId);

    /**
     * 分页查询用户信息（管理端）
     * @param dto dto
     * @param pageRequest pageRequest
     * @return PageDTO<UserAllInfo>
     */
    PageDTO<UserAllInfo> pageUserInfo(UserDTO dto, PageRequest pageRequest);

    /**
     * 查询用户邀请的用户信息
     * @param userId 邀请用户信息
     * @param pageRequest pageRequest
     * @return PageDTO<UserViewVO>
     */
    PageDTO<UserViewVO> queryInviteUserInfo(PageRequest pageRequest,Long userId);

    /**
     * 查询要导出的用户信息
     * @param dto 查询条件
     * @return 用户信息
     */
    List<Object[]> selectUserInfoForExcel(UserDTO dto);

    /**
     * 查询要导出的企业信息
     * @param dto 查询条件
     * @return 企业信息
     */
    List<Object[]> selectUserOrgForExcel(UserOrgClientDTO dto);

    /**
     * 查询用户信息
     * @param  vo vo
     * @return List<UserViewVO>
     */
    List<UserViewVO> listUserInfo(UserVO vo);

    /**
     * 删除用户信息
     * @param userVO 用户VO
     * @param userId 用户id
     * @return String
     */
    String deleteUserInfo(UserVO userVO,String userId);

    /**
     * 查询用户信息
     * @param userVO 用户VO
     * @return String
     */
    UserViewVO findUserInfo(UserVO userVO);

    /**
     * 通过用户手机号查询用户信息
     * @param userVO 用户手机号
     * @return 用户信息
     */
    UserViewVO queryUserInfoByMobile(UserVO userVO);

    /**
     * 查询用户信息
     * @param userVO 用户Vo
     * @return String
     */
    List<Long> listUserId(UserVO userVO);

    /**
     * 批量删除用户信息
     * @param ids 用户ids
     * @param userId 用户id
     * @return String
     */
    String deleteUserList(List<Long> ids,String userId);

    /**
     * 修改用户个人头像
     * @param userVO 用户VO
     * @param userId 用户id
     * @param resultMap 结果Map
     * @return String
     */
    String updateImage(UserVO userVO,String userId, Map<String,String> resultMap);

    /**
     * 修改组织Banner图
     * @param userVO 用户VO
     * @param userId 用户id
     * @param resultMap 结果Map
     * @return String
     */
    String updateBanner(UserVO userVO,String userId, Map<String,String> resultMap);

    /**
     * 设置二维码图片地址
     * @param userVO 用户信息
     * @param userId 用户id
     * @param resultMap 返回参数
     * @return 操作返回码
     */
    String setQrCodeUrl(UserVO userVO,String userId, Map<String,String> resultMap);


    /**
     * 查询所有的账户信息
     * @return List<UserOrgClientVO>
     */
    List<UserOrgClientVO> findOrgList();

    /**
     * 分页查询所有的账户信息
     * @param pageRequest pageRequest
     * @param dto 查询条件
     * @return PageDTO<UserOrgClientVO>
     */
    PageDTO<UserOrgClientVO> findOrgPage(PageRequest pageRequest,UserOrgClientDTO dto);

    /**
     * 设置用户账户信息
     * @param userVO 用户VO
     * @param userId 用户id
     * @return String
     */
    String pushUserAndOrg(UserVO userVO , String userId);

    /**
     * 设置组织标签信息
     * @param userOrgClientVO 组织对象
     * @param userId 用户id
     * @return 处理结果
     */
    String setOrgTags(UserOrgClientVO userOrgClientVO, String userId);

    /**
     * 删除组织
     * @param userOrgClientVO 组织对象
     * @param userId 用户id
     * @return 处理结果
     */
    String deleteOrg(UserOrgClientVO userOrgClientVO, String userId);

    /**
     * 修改组织信息
     * @param userOrgClientVO 组织对象
     * @param userId 用户id
     * @return 处理结果
     */
    String updateOrg(UserOrgClientVO userOrgClientVO, String userId);

    /**
     * 查询组织下的所有用户信息
     * @param userVO 用户VO
     * @return String
     */
    List<UserViewVO> findUserByOrgId(UserVO userVO);

    /**
     * 我的关注(组织账号信息)
     * @param userId 用户id
     * @param pageRequest pageRequest
     * @param userDTO 查询条件对象
     * @return  用户分页信息
     */
    PageDTO<UserAllInfo> findUserFocusOrg(Long userId,PageRequest pageRequest,UserDTO userDTO);

    /**
     * 用户解除绑定组织
     * @param userVO 用户VO
     * @param userId 用户id
     * @return String
     */
    String unBoundOrg(UserVO userVO,String userId);

    /**
     * 用户解除绑定组织
     * @param userOrgClientVO 组织账号id
     * @return 组织信息
     */
    UserOrgClientVO findUserOrgInfo(UserOrgClientVO userOrgClientVO);

    /**
     * 查询用户组织名称
     * @param id 用户id
     * @return 组织名称
     */
    String  findUserOrgName(Long id);

    /**
     * 关闭认证
     * @param userVO 用户VO
     * @param userId 用户id
     * @return String
     */
    String closeOrgPushFlag(UserVO userVO, String userId);

    /**
     * 批量认证
     * @param userVO 用户VO
     * @param userId 用户id
     * @return String
     */
    String batchAuthen(UserVO userVO, String userId);

    /**
     * 根据ES索引推送数据
     * @param indexUrl 索引路径
     * @return String
     */
    String pushDepotEsInfoByIndex(String indexUrl);

    /**
     * 根据ES索引推送数据
     * @param indexUrl 索引路径
     * @return String
     */
    String pushAgencyEsInfoByIndex(String indexUrl);

    /**
     * 根据ES索引推送数据
     * @param indexUrl 索引路径
     * @return String
     */
    String pushMerchantEsInfoByIndex(String indexUrl);

    /**
     * 更新用户基本信息缓存
     * @param userNo 用户ID
     * @param user 用户对象
     */
    void updateUserInfoCache(String userNo,User user);

    /**
     * 更新用户组织缓存
     * @param userOrgClient 用户组织对象
     */
    void updateUserOrgInfoCache(UserOrgClient userOrgClient);

    /**
     * 查询参与行情竞猜用户的信息
     * @param quotationUser 查询条件
     * @param pageRequest 分页对象
     * @return 用户信息
     */
    PageDTO<QuotationUser> quotationUser(QuotationUser quotationUser,PageRequest pageRequest);

    /**
     * 果满仓用户信息补充
     * @param list 果满仓用户信息
     * @return 处理结果
     */
    String userOfWmsDeal(List<UserOfWmsVO> list);

    /**
     * 是否完善用户信息，每周提醒三次，超过次数不提醒
     * @param userId 用户id
     * @return true/false
     */
    Boolean checkFullUserInfo(Long userId);

}
