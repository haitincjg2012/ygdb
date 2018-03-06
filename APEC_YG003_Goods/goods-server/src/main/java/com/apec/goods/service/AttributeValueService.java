package com.apec.goods.service;

import com.apec.goods.vo.AttributeValueVO;

import java.util.List;

/**
 * Created by hmy on 2017/7/10.
 * @author hmy
 */
public interface AttributeValueService {

    /**
     * 增加属性值
     * @param attributeValueVO 属性值对象
     * @param userId 操作人id
     * @return 处理结果
     */
    String saveAttributeValue(AttributeValueVO attributeValueVO, String userId);

    /**
     * 修改属性值
     * @param attributeValueVO 属性值对象
     * @param userId 操作人id
     * @return 处理结果
     */
    String updateAttributeValue(AttributeValueVO attributeValueVO, String userId);

    /**
     * 删除属性值
     * @param attributeValueVO 属性值对象
     * @param userId 操作人id
     * @return 处理结果
     */
    String removeAttributeValue(AttributeValueVO attributeValueVO,String userId);

    /**
     * 获取单条具体信息
     * @param attributeValueVO 属性值对象
     * @return 属性值对象结果
     */
    AttributeValueVO findAttributeValue(AttributeValueVO attributeValueVO);

    /**
     * 批量删除attributeValue
     * @param ids 要删除的属性值对象集合ids
     * @param userId 操作人id
     * @return 处理结果
     */
    String deleteAttributeValueList(List<Long> ids,String userId);
}
