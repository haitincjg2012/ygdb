package com.apec.goods.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.goods.model.GoodsAttr;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by hmy on 2017/7/10.
 */
public interface GoodsAttrDAO extends BaseDAO<GoodsAttr,Long> {

    /**
     * 根据goodId删除相关商品属性关系状态
     * @param goodsId
     */
    @Modifying
    @Query(value="update goods_attr set enable_flag = 'N',last_update_date = now(),last_update_by = :userId where goods_id = :goodsId and enable_flag = 'Y' ",nativeQuery = true)
    void updateByGoodId(@Param("goodsId") Long goodsId,@Param("userId") String userId);

    /**
     * 查询所有的商品Id下的商品的商品属性关系
     * @param goodsId
     * @param enableFlag
     * @return
     */
    List<GoodsAttr> findByGoodsIdAndEnableFlagOrderBySort(Long goodsId,EnableFlag enableFlag);

    /**
     * 批量删除所有goodsIds下的goodsAttr
     * @param goodsIds
     * @param userId
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update goods_attr set enable_flag = 'N',last_update_date = now(),last_update_by = :userId where goods_id in :goodsIds and enable_flag = 'Y'",nativeQuery = true)
    int deleteGoodsAttrByGoodsList(@Param("goodsIds") List<Long> goodsIds,@Param("userId") String userId);

    /**
     * 批量删除goodsAttr
     * @param ids
     * @param userId
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update goods_attr set enable_flag = 'N',last_update_date = now(),last_update_by = :userId where id in :ids and enable_flag = 'Y'",nativeQuery = true)
    int deleteGoodsAttrList(@Param("ids") List<Long> ids,@Param("userId") String userId);

}
