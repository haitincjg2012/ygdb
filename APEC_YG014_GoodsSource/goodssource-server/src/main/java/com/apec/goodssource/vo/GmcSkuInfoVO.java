package com.apec.goodssource.vo;

import com.apec.framework.dto.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: 信云科技有限公司
 * </p>
 * <p>
 * Date:  -
 * </p>
 *
 * @author yirde <532186767@qq.com>
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GmcSkuInfoVO extends BaseVO<Long> {

    /**
     * sku Id
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 组织名
     */
    private String orgName;

    /**
     * OrgId 用户组织ID
     */
    private Long  userOrgId;

    /**
     * SKU Name
     */
    private String skuName;

    /**
     * 营销前缀(最前)
     */
    private String marketPreFix;

    /**
     * 销售多少斤
     */
    private BigDecimal weight;

    /**
     * 单位重量
     */
    private String weightUnit;

    /**
     * 单价
     */
    private BigDecimal amount;

    /**
     * 单价重量
     */
    private String priceUnit;

    /**
     * 起始价
     */
    private BigDecimal startAmount;

    /**
     *  结束价
     */
    private BigDecimal endAmount;

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
     * 属性集合
     */
    private List<GmcSkuAttrVO> list;


}
