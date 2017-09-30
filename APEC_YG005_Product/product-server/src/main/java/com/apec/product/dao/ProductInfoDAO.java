package com.apec.product.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.product.model.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Title:ProductInfo DAO
 *
 * @author yirde  2017/7/17.
 */
public interface ProductInfoDAO extends BaseDAO<ProductInfo, Long> {
    /**
     * 统计timeout的总值
     * @return
     */
    @Query(value = "SELECT sum(timeout) AS timeout FROM product_info" ,nativeQuery = true)
    List<Object> countTimeOut();

    /**
     * 根据ID修改数量和金额
     * @param id
     * @param weight
     * @param amount
     */
    @Modifying
    @Query(value ="UPDATE product_info SET weight = :weight ,amount = :amount where id = :id",nativeQuery = true)
    void updateProduct(@Param("id")Long id, @Param("weight") int weight, @Param("amount") BigDecimal amount);


    /**
     * 根据ES ID查找供求
     * @param elasticId
     * @return
     */
    List<ProductInfo> findFirstByElasticId(String elasticId);

    /**
     * 根据ES ID 和 用户Id 查找供求
     * @param elasticId
     * @param userId
     * @return
     */
    ProductInfo findFirstByElasticIdAndUserId(String elasticId, Long userId);

    /**
     * 根据ES ID 和 enableFlag 查找供求
     * @param elasticId
     * @param enableFlag
     * @return
     */
    ProductInfo findFirstByElasticId(String elasticId, EnableFlag enableFlag);

    /**
     * 查询下架的供求 取最新的10条
     * @param enableFlag
     * @param timeout
     * @param pageable
     * @return
     */
    Page<ProductInfo> findByEnableFlagAndTimeoutLessThanEqualOrderByCreateDate(EnableFlag enableFlag, Integer timeout, Pageable pageable);

    /**
     * 查询未下架的供求信息
     * @param enableFlag
     * @param timeout
     * @return
     */
    List<ProductInfo> findByEnableFlagAndTimeoutGreaterThan(EnableFlag enableFlag, Integer timeout);
}
