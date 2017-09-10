package com.apec.user.model;

import javax.persistence.*;

import com.apec.framework.common.enums.Realm;
import com.apec.framework.common.enums.Source;
import com.apec.framework.common.enumtype.*;
import com.apec.framework.common.util.SecurityUtils;
import com.apec.framework.jpa.model.Authenticatable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import com.apec.framework.common.*;

/**
 * Title: 用户PO
 *
 * @author yirde  2017/3/21
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class User extends Authenticatable {

    private static final long serialVersionUID = 6277891566852240269L;

    /**
     * 登陆名
     */
    @Column(length = 15)
    private String name;

    /**
     * 用户真实姓名
     */
    @Column(length = 15)
    private String realName;

    /**
     * 身份证
     */
    @Column(unique = true)
    private String idNumber;

    /**
     * 手机号
     */
    @Column(unique = true)
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
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    /**
     * 用户身份子类型
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserDetailType userDetailType;

    /**
     * 用户状态
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    /**
     * 用户实名认证状态
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRealAuth userRealAuth;

    /**
     * 来源
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Source source;

    /**
     * 实体所在的域
     */
    @Enumerated(EnumType.STRING)
    @Column()
    private Realm referralRealm;

    /**
     * 实体ID
     */
    private String  referralId;//实体ID

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
     * 性别
     */
    @Enumerated(EnumType.STRING)
    private Sex sex;

    /**
     * OrgId 用户组织ID
     */
    private Long  userOrgId;

    /**
     * 账号类型：
     *  ORG_MAIN_ACCOUNT("组织账户主体账号"),
     *  ORG_CHILD_ACCOUNT("组织账户子级账号"),
     *  IND_MAIN_ACCOUNT("个体账号");
     */
    @Enumerated(EnumType.STRING)
    private UserAccountType userAccountType;

    @Transient
    public void password(final String mobile,final String password) {
        salt = SecurityUtils.getSalt(mobile);
        passphrase = SecurityUtils.getPassphrase(salt, password);
    }

}
