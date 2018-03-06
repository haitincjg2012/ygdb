package com.apec.user.vo;

import com.apec.framework.common.enums.Realm;
import com.apec.framework.common.enums.Source;
import com.apec.framework.common.enumtype.*;
import com.apec.framework.dto.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 用户
 * @author yirde
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO extends BaseVO<Long>{

    private List<Long> ids;

    private List<Long> userOrgIds;

    /**
     * 登陆名
     */
    private String name;

    /**
     * 用户真实姓名
     */
    private String realName;

    /**
     * 身份证
     */
    private String idNumber;

    /**
     * 二维码图片地址
     */
    private String qrCodeUrl;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像地址
     */
    private String imgUrl;

    /**
     * 用户身份
     */
    private UserType userType;

    /**
     * 用户身份子类型
     */
    private UserDetailType userDetailType;

    /**
     * 用户状态
     */
    private UserStatus userStatus;

    /**
     * 用户实名认证状态
     */
    private UserRealAuth userRealAuth;

    /**
     * 来源
     */
    private Source source;

    /**
     * 实体所在的域
     */
    private Realm referralRealm;

    /**
     * 实体ID
     */
    private String  referralId;

    /**
     * 所在地区
     */
    private String  address;

    /**
     * 详细地址
     */
    private String addressDetail;

    /**
     * 主营品种
     */
    private String mainOperating;

    /**
     * 工作年限
     */
    private String workOfYear;

    /**
     * 用户登录记录
     */
    private List<UserLoginRecordVO> loginRecordVOS;

    /**
     * 用户积分记录
     */
    private List<UserPointRecordVO> userPointRecordVOS;


    /**
     * 用户审核记录记录
     */
    private List<UserAuthRecordVO> userAuthRecordVOS;

    /**
     * 密码
     */
    private String password;

    /**
     * 确认密码,修改密码时用
     */
    private String entruePassword;

    /**
     * 用户原密码
     */
    private String oldPassword;

    /**
     * 省
     */
    private String provinceId;

    /**
     * 市
     */
    private String cityId;

    /**
     * 区
     */
    private String areaId;

    /**
     * 镇子
     */
    private String townId;

    /**
     * 性別
     */
    private Sex sex;

    /**
     * 验证码
     */
    private String vaildCode;

    /**
     * 登录次数
     */
    private  Integer loginNumber;

    /**
     * 是否需要修改密码
     */
    private Boolean needChangePassword;

    /**
     * 组织主账号
     */
    private UserAccountType userAccountType;

    /**
     * OrgId 用户组织ID
     */
    private Long  userOrgId;

    /**
     * 用户组织
     */
    private UserOrgClientVO userOrgClientVO;

    /**
     * 开关，开为Y，关为N
     */
    private Boolean pushFlag;

    /**
     * 果满仓用户信息
     */
    private List<UserOfWmsVO> list;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;

}
