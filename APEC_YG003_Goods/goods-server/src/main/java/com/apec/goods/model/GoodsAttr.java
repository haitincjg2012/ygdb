package com.apec.goods.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.AttributeShowLevel;
import com.apec.framework.common.enumtype.AttributeType;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * Title: 商品大类属性
 *
 * @author yirde  2017/6/29.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class GoodsAttr extends BaseModel<Long> {

    private static final long serialVersionUID = 6237891166852240263L;

    /**
     * 商品
     */
    @ManyToOne
    @JoinColumn(name = "GOODS_ID", nullable = false)
    private Goods  goods;

    /**
     * 属性ID
     */
    private Long attrId;
    /**
     * 商品属性名称 冗余
     */
    @Column(nullable = false)
    private String goodsAttrName;

    /**
     * 商品属性前缀 冗余
     */
    private String showPreFix;

    /**
     * 商品属性的后缀 冗余
     */
    private String showSuFix;

    /**
     * 远程请求地址 冗余
     */
    private String remoteUrlParam;

    /**
     * 是否必填
     */
    private boolean mustRequired;

    /**
     * 商品属性类型 冗余
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AttributeType attributeType = AttributeType.RADIO;

    /**
     * 属性显示等级
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AttributeShowLevel attributeShowLevel ;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 该属性是否追加到SKU简称中
     */
    private boolean showSimpleName;

    @Transient
    private List<AttributeValue> attributeValues;


}
