package com.apec.goods.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 用户
 * @author yirde
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsVO{

    private Long id;

    private Date createDate;

    /**
     * 商品大类名称
     */
    private String goodsName;

    /**
     * 描述
     */
    private String remark;

    /**
     * 商品属性
     */
    private List<GoodsAttrVO> goodsAttrVOList;

    /**
     * 是否为主类
     */
    private Boolean mainGoods;



}
