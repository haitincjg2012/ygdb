package com.apec.product.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.product.model.ProductAttr;
import com.apec.product.model.ProductSkuInfo;

import java.util.List;

/**
 * Title:
 *
 * @author yirde  2017/7/17.
 */
public interface ProductAttrDAO extends BaseDAO<ProductAttr, Long> {

    /**
     * 根据ProductSkuId 和EnableFlag 查询，按Sort升序排列
     * @param productSkuInfo productSkuInfo
     * @param enableFlag EnableFlag
     * @return List
     */
    List<ProductAttr> findByProductSkuInfoAndEnableFlagOrderBySort(ProductSkuInfo productSkuInfo, EnableFlag enableFlag);
}
