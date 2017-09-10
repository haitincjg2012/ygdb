package com.apec.goods.dto;

import com.apec.framework.dto.BaseDTO;
import com.apec.goods.vo.AttributeNameVO;

import java.util.List;

/**
 * Created by hmy on 2017/7/11.
 */
public class AttributeNameDTO extends BaseDTO {

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
    private List<AttributeNameVO> attributeNameVOS;

}
