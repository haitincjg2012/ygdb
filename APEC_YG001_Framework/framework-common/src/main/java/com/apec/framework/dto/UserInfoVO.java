package com.apec.framework.dto;

import com.apec.framework.common.enums.Source;
import com.apec.framework.common.enumtype.*;

import java.util.List;

/**
 * 用户基本信息表model
 * @author zhangwenjia
 */
public class UserInfoVO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户昵称
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
     * 用户身份KEY
     */
    private String userTypeKey;

    /**
     * 用户状态
     */
    private UserStatus userStatus;

    /**
     * 用户身份子类型
     */
    private UserDetailType userDetailType;

    /**
     * 用户实名认证状态
     */
    private UserRealAuth userRealAuth;

    /**
     * 来源
     */
    private Source source;

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
     * 性别
     */
    private Sex sex;

    /**
     * 组织ID
     */
    private Long  userOrgId;

    /**
     * 用户账户类型
     */
    private UserAccountType userAccountType;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public UserRealAuth getUserRealAuth() {
        return userRealAuth;
    }

    public void setUserRealAuth(UserRealAuth userRealAuth) {
        this.userRealAuth = userRealAuth;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getMainOperating() {
        return mainOperating;
    }

    public void setMainOperating(String mainOperating) {
        this.mainOperating = mainOperating;
    }

    public String getWorkOfYear() {
        return workOfYear;
    }

    public void setWorkOfYear(String workOfYear) {
        this.workOfYear = workOfYear;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getUserTypeKey() {
        return userTypeKey;
    }

    public void setUserTypeKey(String userTypeKey) {
        this.userTypeKey = userTypeKey;
    }

    public UserDetailType getUserDetailType() {
        return userDetailType;
    }

    public void setUserDetailType(UserDetailType userDetailType) {
        this.userDetailType = userDetailType;
    }

    public Long getUserOrgId() {
        return userOrgId;
    }

    public void setUserOrgId(Long userOrgId) {
        this.userOrgId = userOrgId;
    }

    public UserAccountType getUserAccountType() {
        return userAccountType;
    }

    public void setUserAccountType(UserAccountType userAccountType) {
        this.userAccountType = userAccountType;
    }
}
