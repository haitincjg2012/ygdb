package com.apec.product.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.AttributeShowLevel;
import com.apec.framework.common.enumtype.AttributeType;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Title: SKU ATTR
 *
 * @author yirde  2017/7/6.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class ProductAttr  extends BaseModel<Long> {

    private static final long serialVersionUID = 6277831561234240323L;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private ProductInfo  productInfo;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_SKU_ID", nullable = false)
    private ProductSkuInfo  productSkuInfo;


    private Long attrId;

    /**
     * 商品属性名称 冗余
     */
    @Column(nullable = false)
    private String attrName;

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
     * 值类型id ，多个ID逗号隔开
     */
    private  String attrValueId;

    /**
     * 值， 多个值逗号隔开
     */
    private String attrValue;


}
