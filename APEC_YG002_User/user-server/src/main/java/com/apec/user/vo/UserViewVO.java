package com.apec.user.vo;

import com.apec.framework.common.enumtype.Sex;
import com.apec.framework.common.enumtype.UserRealAuth;
import com.apec.framework.common.enumtype.UserStatus;
import com.apec.framework.common.enumtype.UserType;
import lombok.Data;

import java.util.List;

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
     * 实力描述信息
     */
    private String strengthDescription;

    /**
     * 上传banner图地址
     */
    private String bannerImgUrl;

//    /**
//     * 实力描述上传的照片
//     */
//    private List<UserStrengthImgUrlVO> userStrengthImgUrlVOS;


}
