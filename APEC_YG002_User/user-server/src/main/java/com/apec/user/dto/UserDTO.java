package com.apec.user.dto;

import com.apec.framework.common.enums.Realm;
import com.apec.framework.common.enums.Source;
import com.apec.framework.common.enumtype.*;
import com.apec.framework.dto.BaseDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Title:UserDTO
 *
 * @author yirde  2017/3/23
 */
@Data
public class UserDTO extends BaseDTO {

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 用户Id
     */
    private Long id;

    /**
     * 登陆名
     */
    private String name;

    /**
     * 用户真实姓名
     */
    private String realName;

    /**
     * 公司名称 ；冷库名称
     */
    private String companyName;

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
     * 是否已经注册奖励
     */
    private Boolean registryRewarded;

    /**
     * 是否已经推荐奖励
     */
    private Boolean referralRewarded;

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
    private Sex sex;

    /**
     * 用户账号类型
     */
    private UserAccountType userAccountType;

    /**
     * 开关，筛选条件，开为Y
     */
    private String pushFlag;

    /**
     * 用户id
     */
    private List<Long> userIds;


}
