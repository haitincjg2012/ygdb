package com.apec.goods.service;

import com.apec.goods.vo.AttributeValueVO;

import java.util.List;

/**
 * Created by hmy on 2017/7/10.
 */
public interface AttributeValueService {

    /**
     * 增加
     */
    String saveAttributeValue(AttributeValueVO attributeValueVO, String userId);

    /**
     * 修改
     */
    String updateAttributeValue(AttributeValueVO attributeValueVO, String userId);

    /**
     * 删除
     */
    String removeAttributeValue(AttributeValueVO attributeValueVO,String userId);

    /**
     * 获取单条具体信息
     */
    AttributeValueVO findAttributeValue(AttributeValueVO attributeValueVO);

    /**
     * 批量删除attributeValue
     * @param ids
     * @return
     */
    String deleteAttributeValueList(List<Long> ids,String userId);
}
