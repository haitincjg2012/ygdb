package com.apec.product.dto;

import com.apec.framework.common.enumtype.ProductType;
import com.apec.framework.dto.BaseDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by hmy on 2017/12/26.
 *
 * @author hmy
 */
@Data
public class ProductInfoDTO extends BaseDTO {

    /**
     * 用户ID
     */
    private Long userId;

    private List<Long> userIds;

    private String userName;

    /**
     * ES ID
     */
    private String elasticId;

    /**
     * 产品类型
     */
    private ProductType productType;

    /**
     * 多个GoodsAttr所对应的值拼接
     * SKU名称  GoodsAttr.goodsAttrName(GoodsAttr.showSimpleName) >>
     * Eg: 【前缀】红富士【后缀】【前缀】80#【后缀】 12级 条红。。。
     * 考虑，存在后台给个别用户配置相关的销售前缀，单独列字段
     */
    private String skuName;

    /**
     * 所在地区
     */
    private String  address;

    /**
     * 是否已下架
     */
    private Boolean underCarriage;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;


}
