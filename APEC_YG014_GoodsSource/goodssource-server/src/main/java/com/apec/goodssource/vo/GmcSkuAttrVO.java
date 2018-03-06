package com.apec.goodssource.vo;

import com.apec.framework.dto.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class GmcSkuAttrVO extends BaseVO<Long> {

    /**
     * Sku ID
     */
    private Long skuId;

    /**
     * Attr Id
     */
    private Long id;

    /**
     * 商品属性名称 冗余
     */
    private String attrName;

    /**
     * 值类型id ，
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
