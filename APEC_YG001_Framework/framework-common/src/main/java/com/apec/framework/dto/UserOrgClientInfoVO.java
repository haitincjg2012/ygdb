package com.apec.framework.dto;

import com.apec.framework.common.enumtype.UserAccountType;

/**
 * Title: 用户组织缓存VO
 *
 * @author yirde  2017/9/7.
 */
public class UserOrgClientInfoVO {

    /**
     * 用户组织ID
     */
    private Long userOrgId;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 组织的Banner图的URL
     */
    private String orgBannerUrl;

    /**
     * 组织的缩略图
     */
    private String orgFirstBannerUrl;

    /**
     * 仓库的库存容量
     */
    private String orgStockCap;

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
     * 账号类型
     */
    private UserAccountType userAccountType;


    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgBannerUrl() {
        return orgBannerUrl;
    }

    public void setOrgBannerUrl(String orgBannerUrl) {
        this.orgBannerUrl = orgBannerUrl;
    }

    public String getOrgFirstBannerUrl() {
        return orgFirstBannerUrl;
    }

    public void setOrgFirstBannerUrl(String orgFirstBannerUrl) {
        this.orgFirstBannerUrl = orgFirstBannerUrl;
    }

    public String getOrgStockCap() {
        return orgStockCap;
    }

    public void setOrgStockCap(String orgStockCap) {
        this.orgStockCap = orgStockCap;
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

    public Long getUserOrgId() {
        return userOrgId;
    }

    public void setUserOrgId(Long userOrgId) {
        this.userOrgId = userOrgId;
    }

    public String getMainOperating() {
        return mainOperating;
    }

    public void setMainOperating(String mainOperating) {
        this.mainOperating = mainOperating;
    }

    public String getElasticId() {
        return elasticId;
    }

    public void setElasticId(String elasticId) {
        this.elasticId = elasticId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public UserAccountType getUserAccountType() {
        return userAccountType;
    }

    public void setUserAccountType(UserAccountType userAccountType) {
        this.userAccountType = userAccountType;
    }
}
