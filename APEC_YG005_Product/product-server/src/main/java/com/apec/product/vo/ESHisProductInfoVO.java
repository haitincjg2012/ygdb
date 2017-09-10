package com.apec.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by wubi on 2017/8/7.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ESHisProductInfoVO {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 供求类型名称
     */
    private String productTypeName;

    /**
     * 显示 性别+先生/女士
     */
    private String showUserName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 加密手机号 137******0312
     */
    private String showMobile;

    /**
     * ImageURL First
     */
    private String firstImageUrl;

    /**
     * 用户类型
     * 积分等级存储Redis
     */
    private String userTypeName;

    /**
     * SkuName(包含营销前缀)
     */
    private String skuName;

    private Long orderWeight;

    private BigDecimal weight;

    private BigDecimal amount;

    /**
     * 起始价
     */
    private BigDecimal startAmount;

    /**
     *  结束价
     */
    private BigDecimal endAmount;

    /**
     * createDate 为产品需求发布的时间
     * 有效期，默认单位天
     * 7天 15天 30 天， 每晚凌晨三点 进行有效期变更
     * 权重 2
     */
    private int timeout;

    private String  address;
    /**
     * 详细地址
     */
    private String addressDetail;

    /**
     * 描述
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * ES ID
     */
    private String elasticId;

    /**
     * 需求 属性
     */
    private List<ESProductAttrVO> productAttrs;

    /**
     *  图片 属性
     */
    private List<ESProductImageVO> productImages;
}
