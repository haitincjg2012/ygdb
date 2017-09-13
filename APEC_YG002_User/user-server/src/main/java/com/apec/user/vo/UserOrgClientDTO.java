package com.apec.user.vo;

import com.apec.framework.common.enumtype.UserAccountType;
import com.apec.framework.dto.BaseDTO;
import lombok.Data;

/**
 * Created by hmy on 2017/9/12.
 */
@Data
public class UserOrgClientDTO extends BaseDTO {

    private Long id;

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

    /*
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


}
