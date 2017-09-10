package com.apec.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Title:Es Product InfoVO
 *
 * @author yirde  2017/7/20.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ESProductInfoVO {

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
     * 营销前缀
     */
    private String marketPreFix;

    /**
     * 单位
     */
    private String weightUnit;

    /**
     * 单价重量
     */
    private String priceUnit;

    /**
     * 用户类型
     * 积分等级存储Redis
     */
    private String userTypeName;

    /**
     * SkuName(包含营销前缀)
     */
    private String skuName;

    private float orderWeight;

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
     * 需求 属性
     */
    private List<ESProductAttrVO> productAttrs;

    /**
     *  图片 属性
     */
    private List<ESProductImageVO> productImages;



}
