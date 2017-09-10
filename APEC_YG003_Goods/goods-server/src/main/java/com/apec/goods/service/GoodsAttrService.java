package com.apec.goods.service;

import com.apec.goods.vo.GoodsAttrVO;

import java.util.List;

/**
 * Created by hmy on 2017/7/10.
 */
public interface GoodsAttrService {

    /**
     * 添加商品属性关系
     */
    String saveGoodsAttr(GoodsAttrVO goodsAttrVO,String userId);

    /**
     * 修改商品属性关系
     */
    String updateGoodsAttr(GoodsAttrVO goodsAttrVO,String userId);

    /**
     * 删除商品属性关系
     */
    String removeGoodsAttr(GoodsAttrVO goodsAttrVO,String userId);

    /**
     * 查询商品属性关系
     */
    GoodsAttrVO findGoodsAttr(GoodsAttrVO goodsAttrVO);

    /**
     * 批量删除goodsAttrs
     * @param ids
     * @return
     */
    String deleteGoodsAttrList(List<Long> ids,String userId);

}
