package com.apec.user.vo;

import com.apec.framework.common.enumtype.*;
import lombok.Data;

/**
 * Created by hmy on 2017/8/5.
 */
@Data
public class UserViewVO {

    private Long id;

    /**
     * 登陆名
     */
    private String name;

    /**
     * 头像地址
     */
    private String imgUrl;

    /**
     * 用户真实姓名
     */
    private String realName;

    /**
     * 公司名称 ；冷库名称
     */
    private String companyName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户身份
     */
    private UserType userType;

    private String userTypeKey;

    /**
     * 用户身份子类型
     */
    private UserDetailType userDetailType;

    private String userDetailTypeKey;

    /**
     * 用户状态
     */
    private UserStatus userStatus;

    private String userStatusKey;

    /**
     * 用户实名认证状态
     */
    private UserRealAuth userRealAuth;

    private String userRealAuthKey;

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
     * 性別
     */
    private Sex sex;

    private String sexValue;

    private UserPointVO userPoint;

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
     * 组织信息
     */
    private UserOrgClientVO userOrgClientVO;

    /**
     * 账号类型：
     *  ORG_MAIN_ACCOUNT("组织账户主体账号"),
     *  ORG_CHILD_ACCOUNT("组织账户子级账号"),
     *  IND_MAIN_ACCOUNT("个体账号");
     */
    private UserAccountType userAccountType;

    private String userAccountTypeKey;

    /**
     * OrgId 用户组织ID
     */
    private Long  userOrgId;

}
