package com.apec.goods.dao;

import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.goods.model.AttributeValue;
import com.apec.framework.common.enumtype.EnableFlag;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by hmy on 2017/7/10.
 */
public interface AttributeValueDAO extends BaseDAO<AttributeValue,Long> {

    /**
     * 删除相关attributeId下的所有属性对象
     * @param attributeId
     */
    @Modifying(clearAutomatically = true)
    @Query(value = " update attribute_value set enable_flag = 'N',last_update_date = now(),last_update_by = :userId where attribute_id = :attributeId and  enable_flag = 'Y'",nativeQuery = true)
    void updateByAttributeId(@Param("attributeId") Long attributeId,@Param("userId") String userId);

    /**
     * 查询相关attributeId下的所有属性对象
     * @param attributeId
     */
    @Query(value = " select * from attribute_value where attribute_id = :attributeId and  enable_flag = 'Y'",nativeQuery = true)
    List<AttributeValue> findByAttributeId(@Param("attributeId") Long attributeId);

    /**
     * 批量删除所有goodsIds下的goodsAttr
     * @param attrIds
     * @param userId
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update attribute_value set enable_flag = 'N',last_update_date = now(),last_update_by = :userId where attribute_id in :attrIds and enable_flag = 'Y'",nativeQuery = true)
    int deleteAttributeValueByAttrNameList(@Param("attrIds") List<Long> attrIds,@Param("userId") String userId);

    /**
     * 批量删除goodsAttr
     * @param ids
     * @param userId
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update attribute_value set enable_flag = 'N',last_update_date = now(),last_update_by = :userId where id in :ids and enable_flag = 'Y'",nativeQuery = true)
    int deleteAttributeValueList(@Param("ids") List<Long> ids,@Param("userId") String userId);


}
