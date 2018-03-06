package com.apec.goods.service;

import com.apec.goods.vo.GoodsAttrVO;

import java.util.List;

/**
 * Created by hmy on 2017/7/10.
 * @author hmy
 */
public interface GoodsAttrService {

    /**
     * 添加商品属性关系
     * @param goodsAttrVO 商品属性关系对象
     * @param userId 操作人id
     * @return 处理结果
     */
    String saveGoodsAttr(GoodsAttrVO goodsAttrVO,String userId);

    /**
     * 修改商品属性关系
     * @param goodsAttrVO 商品属性关系对象
     * @param userId 操作人id
     * @return 处理结果
     */
    String updateGoodsAttr(GoodsAttrVO goodsAttrVO,String userId);

    /**
     * 删除商品属性关系
     * @param goodsAttrVO 商品属性关系对象
     * @param userId 操作人id
     * @return 处理结果
     */
    String removeGoodsAttr(GoodsAttrVO goodsAttrVO,String userId);

    /**
     * 查询商品属性关系
     * @param goodsAttrVO 商品属性关系对象
     * @return 商品属性关系
     */
    GoodsAttrVO findGoodsAttr(GoodsAttrVO goodsAttrVO);

    /**
     * 批量删除goodsAttrs
     * @param ids 要删除的对象集合ids
     * @param userId 操作人id
     * @return 处理结果
     */
    String deleteGoodsAttrList(List<Long> ids,String userId);

    /**
     * 序号上升
     * @param goodsAttrVO goodsAttrVO
     * @param userId 操作人id
     * @return 处理结果
     */
    String riseGoodsAttrSort(GoodsAttrVO goodsAttrVO,String userId);

    /**
     * 序号下降
     * @param goodsAttrVO goodsAttrVO
     * @param userId 操作人id
     * @return 处理结果
     */
    String downGoodsAttrSort(GoodsAttrVO goodsAttrVO,String userId);

}
