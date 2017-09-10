package com.apec.goods.dao;

import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.goods.model.AttributeName;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by hmy on 2017/7/10.
 */
public interface AttributeNameDAO extends BaseDAO<AttributeName,Long> {

    /**
     * 删除对象
     * @param attributeId
     */
    @Modifying(clearAutomatically = true)
    @Query(value = " update attribute_name set enable_flag = 'N',last_update_date = now(),last_update_by = :userId where id = :attributeId and  enable_flag = 'Y'",nativeQuery = true)
    void removeAttributeName(@Param("attributeId") Long attributeId,@Param("userId") String userId);

    /**
     * 批量删除attributeName
     * @param ids
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update attribute_name set enable_flag = 'N',last_update_date = now(),last_update_by = :userId where id in :ids and enable_flag = 'Y'",nativeQuery = true)
    int deleteAttributeNameList(@Param("ids") List<Long> ids, @Param("userId") String userId);

}
