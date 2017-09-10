package com.apec.goods.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.goods.model.Goods;
import com.apec.framework.jpa.dao.BaseDAO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * Title: 客户DAO
 * @author yirde  2017/3/21
 */
public interface GoodsDAO extends BaseDAO<Goods, Long> {

    /**
     * 查找数据库中最近一个添加的主类信息
     * @param mainGoods
     * @return
     */
    Goods findFirstByMainGoodsAndEnableFlagOrderByCreateDateDesc(boolean mainGoods, EnableFlag enableFlag);

    /**
     * 批量删除goods
     * @param ids
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update goods set enable_flag = 'N',last_update_date = now(),last_update_by = :userId where id in :ids and enable_flag = 'Y'",nativeQuery = true)
    int deleteGoodsList(@Param("ids") List<Long> ids,@Param("userId") String userId);

}
