package com.apec.product.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.product.model.ProductInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Title:ProductInfo DAO
 *
 * @author yirde  2017/7/17.
 */
public interface ProductInfoDAO extends BaseDAO<ProductInfo, Long> {
    /**
     * 统计timeout的总值
     * @return 过期时间总值
     */
    @Query(value = "SELECT sum(timeout) AS timeout FROM product_info " ,nativeQuery = true)
    List<Object> countTimeOut();

    /**
     * 根据ID修改数量和金额
     * @param id id
     * @param weight 重量
     * @param amount 数量
     */
    @Modifying
    @Query(value ="UPDATE product_info SET weight = :weight ,amount = :amount where id = :id",nativeQuery = true)
    void updateProduct(@Param("id")Long id, @Param("weight") int weight, @Param("amount") BigDecimal amount);


    /**
     * 根据ES ID查找供求
     * @param elasticId esId
     * @return List<ProductInfo>
     */
    List<ProductInfo> findFirstByElasticId(String elasticId);

    /**
     * 根据ES ID 和 用户Id 查找供求
     * @param elasticId esId
     * @param userId 用户id
     * @return 供求信息
     */
    ProductInfo findFirstByElasticIdAndUserId(String elasticId, Long userId);

    /**
     * 根据ES ID 和 enableFlag 查找供求
     * @param elasticId esId
     * @param enableFlag 状态码
     * @return 供求信息
     */
    ProductInfo findFirstByElasticIdAndEnableFlag(String elasticId, EnableFlag enableFlag);

    /**
     * 查询下架的供求 取每个用户的最新的10条
     * @return List<ProductInfo>
     */
    @Query(value = "SELECT a.*\n" +
            "         FROM product_info a \n" +
            "        WHERE a.timeout = 0 and (SELECT COUNT(1) as rest\n" +
            "         FROM product_info b\n" +
            "        WHERE ((ifnull(a.offsell_date,now()) <= ifnull(b.offsell_date,now()) and id > a.id ) or  (ifnull(a.offsell_date,now()) > ifnull(b.offsell_date,now()) and id > a.id ))\n" +
            "          AND b.timeout = 0 and b.user_id = a.user_id and b.enable_flag = 'Y' and a.enable_flag = 'Y') < 10\n" +
            "     ORDER BY a.user_id, ifnull(a.offsell_date,now()) DESC" ,nativeQuery = true)
    List<ProductInfo> findByOffsellProduct();

    /**
     * 查询未下架的供求信息
     * @param enableFlag 状态码
     * @param timeout 过期时间
     * @return 下架的供求信息
     */
    List<ProductInfo> findByEnableFlagAndTimeoutGreaterThan(EnableFlag enableFlag, Integer timeout);

    /**
     * 供求达人
     * @param num num
     * @return 达人id,上传总次数
     */
    @Query(value = "SELECT user_id ,count(1) as t FROM product_info where enable_flag = 'Y' group by user_id order by t desc limit :num " ,nativeQuery = true)
    List<BigInteger[]> maxProduct(@Param("num")int num);

    /**
     * 查询满足条件的供求达人
     * @param num 供求条数
     * @param startTime 查询开始时间
     * @param endTime 查询结束时间
     * @return 达人id,上传总次数
     */
    @Query(value = "SELECT user_id ,count(1) as t FROM product_info where enable_flag = 'Y' and create_date > ?2 and create_date < ?3 group by user_id having(t > ?1) order by t desc " ,nativeQuery = true)
    List<Object[]> queryUserForRanking(int num, Date startTime,Date endTime);

    /**
     * 查询用户上传的供求数量
     * @param userIds 用户id
     * @return 长传的供求数
     */
    @Query(value = "SELECT user_id ,count(1) as t FROM product_info where enable_flag = 'Y' and user_id in ?1 group by user_id order by t desc " ,nativeQuery = true)
    List<Object[]> queryUserInfoForRanking(List<Long> userIds);

    /**
     * 查询供求权重最大值
     * @return 最大权重值
     */
    @Query(value = "SELECT ifnull(max(order_weight),0) FROM product_info where enable_flag = 'Y' " ,nativeQuery = true)
    Long queryMaxOrderWeight();

}
