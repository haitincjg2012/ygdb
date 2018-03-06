package com.apec.product.vo;

import com.apec.framework.common.enumtype.ProductType;
import com.apec.framework.common.enumtype.UserType;
import com.apec.framework.dto.BaseVO;
import com.apec.framework.rockmq.vo.IMqBody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Title:
 *
 * @author yirde  2017/7/6.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfoVO extends BaseVO<Long> implements IMqBody {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * ES ID
     */
    private String elasticId;

    /**
     * 用户姓名
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
     * 加密手机号 137******0312
     */
    private String showMobile;

    /**
     * 用户类型 冗余
     */
    private UserType userType;

    /**
     * 用户类型 冗余
     */
    private String userTypeName;

    /**
     * 产品类型
     */
    private ProductType productType;

    /**
     * 产品类型
     */
    private String productTypeName;

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
     * 单位重量
     */
    private String weightUnit;

    /**
     * 单价重量
     */
    private String priceUnit;

    /**
     * 首图
     */
    private String firstImageUrl;

    /**
     * 综合排序权重分数
     * 每晚凌晨三点进行重新排序 更新
     */
    private Long orderWeight;

    /**
     * 销售多少斤/供应多少斤
     */
    private BigDecimal weight;

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
    private Integer timeout;

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
     */
    private BigDecimal getLongitude;

    /**
     * 维度
     */
    private BigDecimal getLatitude;

    /**
     * 描述
     */
    private String remark;

    /**
     * 浏览人数
     * 权重 1
     */
    private Integer viewNum;

    /**
     * 联系人数
     * 权重 1
     */
    private Integer telNum;

    /**
     * 收藏数
     */
    private Integer favoriteCount;

    /**
     * 需求 属性
     */
    private List<ProductAttrVO> productAttrs;

    /**
     * 需求 属性
     */
    private List<ProductImageVO> productImages;

    /**
     * 标签信息
     */
    private List<ProductTagsVO> productTags;

}
