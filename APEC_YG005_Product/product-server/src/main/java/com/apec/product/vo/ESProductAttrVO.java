package com.apec.product.vo;

import com.apec.framework.common.enumtype.AttributeShowLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Title:
 *
 * @author yirde  2017/7/20.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ESProductAttrVO {

    private AttributeShowLevel attributeShowLevel ;

    private String attrName;

    /**
     * 值， 多个值逗号隔开
     */
    private String attrValue;

    /**
     * 顺序
     */
    private Integer sort;
}
