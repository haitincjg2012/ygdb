package com.apec.goods.service;

import com.apec.framework.common.PageDTO;
import com.apec.goods.vo.AttributeNameVO;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * Created by hmy on 2017/7/10.
 */
public interface AttributeNameService {

    /**
     * 添加属性名称
     */
    String saveAttributeName(AttributeNameVO attributeNameVO,String userID);

    /**
     * 修改属性名称
     */
    String updateAttributeName(AttributeNameVO attributeNameVO,String userId);

    /**
     * 删除属性名称
     */
    String removeAttributeName(AttributeNameVO attributeNameVO,String userId);

    /**
     * 查询属性名称具体信息
     */
    AttributeNameVO findAttributeName(AttributeNameVO attributeNameVO);

    /**
     * 分页查询相关信息
     */
    PageDTO<AttributeNameVO> searchAttributeNamePage(AttributeNameVO attributeNameVO, PageRequest pageRequest);

    /**
     * 批量删除属性名称
     * @param ids
     * @param userId
     * @return
     */
    String deleteAttributeNameList(List<Long> ids, String userId);


}
