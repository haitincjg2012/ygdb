package com.apec.goods.vo;

import com.apec.framework.common.enumtype.AttributeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Created by hmy on 2017/7/10.
 * @author hmy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeNameVO{

    private Long id;

    private Date createDate;

    private Date lastUpdateDate;

    /**
     * 商品属性名称
     */
    private String attrName;

    /**
     * 商品属性前缀
     */
    private String showPreFix;

    /**
     * 商品属性的后缀
     */
    private String showSuFix;

    /**
     * 远程请求地址
     */
    private String remoteUrlParam;

    /**
     * 备注
     */
    private String  remark;

    /**
     * 属性值
     */
    private List<AttributeValueVO> attributeValueVOS;

    /**
     * 属性类型
     */
    private AttributeType attributeType;

}
