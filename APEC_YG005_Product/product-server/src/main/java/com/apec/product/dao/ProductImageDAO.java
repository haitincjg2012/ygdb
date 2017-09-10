package com.apec.product.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.product.model.ProductImage;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Title:
 *
 * @author yirde  2017/7/17.
 */
public interface ProductImageDAO extends BaseDAO<ProductImage, Long>{

    /**
     * 根据imageDefType和EnableFlag 统计
     * @param imageDefType imageDefType
     * @param enableFlag EnableFlag
     * @return List
     */
    @Query(value = "SELECT sort,GROUP_CONCAT(id) FROM product_image u WHERE  " +
            " u.ENABLE_FLAG = ?2 AND u.image_def_type = ?1  GROUP BY sort Order By sort " ,nativeQuery = true  )
    List<Object[]>  findCountByDefTypeAndEnableFlag(String imageDefType, String enableFlag);

    /**
     * 根据ID 和Enable查询， 按Sort排序
     * @param listId Id集合
     * @param enableFlag EnableFlag
     * @return List
     */
    List<ProductImage> findByIdInAndEnableFlagOrderBySort(List<Long> listId, EnableFlag enableFlag);

    /**
     *  根据ProductInfoId 查询Image信息
     * @param productId productId
     * @param enableFlag 是否删除
     * @return List
     */
    List<ProductImage> findByProductIdAndEnableFlagOrderBySort(Long productId, EnableFlag enableFlag);
}
