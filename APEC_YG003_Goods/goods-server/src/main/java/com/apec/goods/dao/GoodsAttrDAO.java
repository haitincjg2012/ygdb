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
 * @author hmy
 */
public interface GoodsAttrDAO extends BaseDAO<GoodsAttr,Long> {

    /**
     * 根据goodId删除相关商品属性关系状态
     * @param goodsId goodsId
     * @param userId 用户id
     */
    @Modifying
    @Query(value="update goods_attr set enable_flag = 'N',last_update_date = now(),last_update_by = :userId where goods_id = :goodsId and enable_flag = 'Y' ",nativeQuery = true)
    void updateByGoodId(@Param("goodsId") Long goodsId,@Param("userId") String userId);

    /**
     * 查询所有的商品Id下的商品的商品属性关系
     * @param goodsId goodsId
     * @param enableFlag 状态码
     * @return 所有的商品Id下的商品的商品属性关系
     */
    List<GoodsAttr> findByGoodsIdAndEnableFlagOrderBySort(Long goodsId,EnableFlag enableFlag);

    /**
     * 批量删除所有goodsIds下的goodsAttr
     * @param goodsIds  goodsIds
     * @param userId 用户id
     * @return 删除的行数
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update goods_attr set enable_flag = 'N',last_update_date = now(),last_update_by = :userId where goods_id in :goodsIds and enable_flag = 'Y'",nativeQuery = true)
    int deleteGoodsAttrByGoodsList(@Param("goodsIds") List<Long> goodsIds,@Param("userId") String userId);

    /**
     * 批量删除goodsAttr
     * @param ids ids
     * @param userId 用户id
     * @return 删除行数
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update goods_attr set enable_flag = 'N',last_update_date = now(),last_update_by = :userId where id in :ids and enable_flag = 'Y'",nativeQuery = true)
    int deleteGoodsAttrList(@Param("ids") List<Long> ids,@Param("userId") String userId);

    /**
     * 查询商品Id下的商品的商品属性关系排序字段比某个序号小的商品属性关系
     * @param goodsId goodsId
     * @param enableFlag 状态码
     * @return 所有的商品Id下的商品的商品属性关系
     */
    List<GoodsAttr> findByGoodsIdAndEnableFlagAndSortLessThanOrderBySortDesc(Long goodsId,EnableFlag enableFlag,Integer sort);

    /**
     * 查询商品Id下的商品的商品属性关系排序字段比某个序号小的商品属性关系
     * @param goodsId goodsId
     * @param enableFlag 状态码
     * @return 所有的商品Id下的商品的商品属性关系
     */
    List<GoodsAttr> findByGoodsIdAndEnableFlagAndSortGreaterThanOrderBySortAsc(Long goodsId,EnableFlag enableFlag,Integer sort);

}
