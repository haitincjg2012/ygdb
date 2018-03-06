package com.apec.goodssource.model;

import com.apec.framework.common.Constants;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * <p>
 * Title: 货源
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * </p>
 * <p>
 * Date:  -
 * </p>
 *
 * @author yirde <532186767@qq.com>
 * @version 1.0
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gmc_sku_info")
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class GmcSkuInfo extends BaseModel<Long> {

    private static final long serialVersionUID = 6287831566343240323L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * OrgId 用户组织ID
     */
    private Long  userOrgId;

    /**
     * 组织名
     */
    private String orgName;

    /**
     * SKU Name
     */
    private String skuName;

    /**
     * 营销前缀(最前)
     */
    private String marketPreFix;

    /**
     *  库存
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
     * 单价 单位
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

}