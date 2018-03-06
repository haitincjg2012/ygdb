package com.apec.goodssource.model;

import com.apec.framework.common.Constants;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <p>
 * Title: GMC SKU  ATTR
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
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
@Table(name = "gmc_sku_attr")
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class GmcSkuAttr extends BaseModel<Long> {

    /**
     * Sku ID
      */
    private Long skuId;

    /**
     * 商品属性名称 冗余
     */
    @Column(nullable = false)
    private String attrName;

    /**
     * 值类型id ，多个ID逗号隔开
     */
    private  Long attrValueId;

    /**
     * 值， 多个值逗号隔开
     */
    private String attrValue;

    /**
     * 顺序
     */
    private Integer sort;

}

