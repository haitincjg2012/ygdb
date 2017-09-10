package com.apec.goods.vo;

import com.apec.framework.common.enumtype.AttributeShowLevel;
import com.apec.framework.common.enumtype.AttributeType;
import com.apec.framework.dto.BaseVO;
import com.apec.goods.model.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by hmy on 2017/7/10.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsAttrVO {

    private Long id;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 属性ID
     */
    private Long attrId;
    /**
     * 商品属性名称 冗余
     */
    private String goodsAttrName;

    /**
     * 商品属性前缀 冗余
     */
//    private String showPreFix;

    /**
     * 商品属性的后缀 冗余
     */
//    private String showSuFix;

    /**
     * 远程请求地址 冗余
     */
//    private String remoteUrlParam;

    /**
     * 是否必填
     */
    private boolean mustRequired;

    /**
     * 商品属性类型 冗余
     */
    private AttributeType attributeType = AttributeType.RADIO;

    /**
     * 属性显示等级
     */
    private AttributeShowLevel attributeShowLevel ;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 该属性是否追加到SKU简称中
     */
    private boolean showSimpleName;

    /**
     * 该商品属性值
     */
    private List<AttributeValueVO> attributeValueVOS;

}
