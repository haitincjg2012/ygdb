package com.apec.product.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.ProductType;
import com.apec.framework.common.enumtype.UserType;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Title: Product 数据
 *
 * @author yirde  2017/6/29.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class ProductInfo extends BaseModel<Long> {

    private static final long serialVersionUID = 6277831566123240323L;

    /**
     * 发布人的ID
     */
    private Long userId;

    /**
     * ES ID
     */
    private String elasticId;

    /**
     * 发布人的姓名
     */
    private String userName;

    /**
     * 显示 性别+先生/女士
     */
    private String showUserName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 第一张缩略图
     */
    private String firstImageUrl;

    /**
     * 加密手机号 137******0312
     */
    private String showMobile;

    /**
     * 用户类型 冗余
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    /**
     * Sku Id
     */
    private Long skuId;

    /**
     * 多个GoodsAttr所对应的值拼接
     * SKU名称  GoodsAttr.goodsAttrName(GoodsAttr.showSimpleName) >>
     * Eg: 【前缀】红富士【后缀】【前缀】80#【后缀】 12级 条红。。。
     * 考虑，存在后台给个别用户配置相关的销售前缀，单独列字段
     */
    private String skuName;

    /**
     * 营销前缀(最前)
     */
    private String marketPreFix;

    /**
     * 综合排序权重分数
     * 每晚凌晨三点进行重新排序 更新
     */
    private Long orderWeight;

    /**
     * 销售多少斤
     */
    private BigDecimal weight;

    /**
     * 单位重量
     */
    private String weightUnit;

    /**
     * 单价重量
     */
    private String priceUnit;

    /**
     * 单价  元/斤
     */
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

    /**
     * 所在地区
     */
    private String  address;

    /**
     * 详细地址
     */
    private String addressDetail;

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
     * 经度
     * @return
     */
    private BigDecimal getLongitude;

    /**
     * 维度
     */
    private BigDecimal getLatitude;

    /**
     * 描述
     */
    @Column(length = 500)
    private String remark;

    /**
     * 浏览人数
     * 权重 1
     */
    private int viewNum;

    /**
     * 联系人数
     * 权重 1
     */
    private int telNum;

    /**
     * 收藏数
     */
    private int favoriteCount;

    /**
     * 需求 属性
     */
    @OneToMany(mappedBy = "productInfo",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ProductAttr> productAttrs;

    /**
     * 下架时间
     */
    private Date offsellDate;
}
