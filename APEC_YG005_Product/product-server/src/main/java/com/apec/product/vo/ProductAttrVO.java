package com.apec.product.vo;

import com.apec.framework.common.enumtype.AttributeShowLevel;
import com.apec.framework.common.enumtype.AttributeType;
import com.apec.framework.dto.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Title:
 *
 * @author yirde  2017/7/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttrVO extends BaseVO<Long> {

    private Long attrId;

    /**
     * 商品属性名称 冗余
     */
    private String attrName;

    private AttributeType attributeType;

    /**
     * 属性显示等级
     */
    private AttributeShowLevel attributeShowLevel ;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 值类型id ，多个ID逗号隔开
     */
    private  String attrValueId;

    /**
     * 值， 多个值逗号隔开
     */
    private String attrValue;

}
