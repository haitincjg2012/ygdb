package com.apec.product.dao;

import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.product.model.ProductSkuInfo;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Title:
 *
 * @author yirde  2017/7/20.
 */
public interface ProductSkuInfoDAO extends BaseDAO<ProductSkuInfo, Long> {

    /**
     * 根据用户ID和EnableFlag 查询最近的
     * @param userId 用户id
     * @param enableFlag 状态码
     * @return 商品属性和其id
     */
    @Query(value = "SELECT  id,sku_name FROM product_sku_info u WHERE  u.ENABLE_FLAG = ?2 AND u.user_id = ?1 " +
            "Order By u.CREATE_DATE DESC LIMIT 0,1 " ,nativeQuery = true  )
    List<Object[]> querySkuNameIdByUserIdAndEnableFlag(Long userId, String enableFlag);

}
