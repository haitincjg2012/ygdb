package com.apec.goods.vo;

import lombok.Data;

/**
 * Created by hmy on 2017/7/10.
 * @author hmy
 */
@Data
public class AttributeValueVO {

    private Long id;

    /**
     * 属性名
     */
    private Long attributeNameId;

    /**
     * 商品属性名称
     */
    private String attrValue;

    /**
     * 排序
     */
    private Integer sort;

}
