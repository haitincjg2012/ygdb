package com.apec.user.vo;

import com.apec.framework.common.enumtype.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by hmy on 2017/9/15.
 * @author hmy
 */
@Data
public class UserAllInfo {

    private Long id;

    private Date createDate;

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
     * 工作年限
     */
    private String workOfYear;

    /**
     * 性別
     */
    private Sex sex;

    private String sexValue;

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
     * 账号类型：
     *  ORG_MAIN_ACCOUNT("组织账户主体账号"),
     *  ORG_CHILD_ACCOUNT("组织账户子级账号"),
     *  IND_MAIN_ACCOUNT("个体账号");
     */
    private UserAccountType orgAccountType;

    /**
     * 账号类型：
     *  ORG_MAIN_ACCOUNT("组织账户主体账号"),
     *  ORG_CHILD_ACCOUNT("组织账户子级账号"),
     *  IND_MAIN_ACCOUNT("个体账号");
     */
    private String orgAccountTypeKey;

    private String userAccountTypeKey;

    /**
     * OrgId 用户组织ID
     */
    private Long  userOrgId;

    /**
     * 仓库组名称
     */
    private String orgName;

    /**
     * 组织的Banner图的URL
     */
    private String orgBannerUrl;

    /**
     * 组织缩略图
     */
    private String orgFirstBannerUrl;

    /**
     * 仓库的库存容量
     */
    private String orgStockCap;

    /**
     * 关注数
     */
    private int attentionNum;

    /**
     * 浏览数
     */
    private int viewNum;

    /**
     * 供求数
     */
    private int productNum;

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
     * 销售地址
     */
    private String saleAddress;

    /**
     * 综合排序权重分数
     * 每晚凌晨三点进行重新排序 更新
     */
    private Long orderWeight;

    /**
     * ES ID
     */
    private String elasticId;

    /**
     * 实力描述
     */
    private String remark;

    /**
     * 账户类型
     */
    private UserAccountType userAccountType;

    /**
     * 开关，
     */
    private boolean pushFlag;

    /**
     * 该用户总的可用积分
     */
    private Integer availablePoints;

    /**
     * 该用户的等级
     * 等级规则计算每日凌晨1:00计算
     */
    private UserLevel userLevel = UserLevel.QT;

    private String userLevelKey;

    /**
     * 最近一个消费积分的时间
     */
    private Date lastConsumeTime;

    /**
     * 组织标签
     */
    private List<UserTagsVO> userTagsVOS;


}
