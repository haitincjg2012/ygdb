package com.apec.product.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.product.model.ProductTags;

/**
 * Created by hmy on 2018/1/18.
 *
 * @author hmy
 */
public interface ProductTagsDAO extends BaseDAO<ProductTags,Long>{

    /**
     * 查询供求标签
     * @param esId esId
     * @param enableFlag 状态码
     * @return 标签
     */
    Iterable<ProductTags> findByEsIdAndEnableFlag(String esId, EnableFlag enableFlag);

}
