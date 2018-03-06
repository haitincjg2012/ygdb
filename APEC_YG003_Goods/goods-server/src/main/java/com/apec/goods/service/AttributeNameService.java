package com.apec.goods.service;

import com.apec.framework.common.PageDTO;
import com.apec.goods.vo.AttributeNameVO;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * Created by hmy on 2017/7/10.
 * @author hmy
 */
public interface AttributeNameService {

    /**
     * 添加属性名称
     * @param attributeNameVO attributeNameVO
     * @param userID userID
     * @return  String
     */
    String saveAttributeName(AttributeNameVO attributeNameVO,String userID);

    /**
     * 修改属性名称
     * @param attributeNameVO attributeNameVO
     * @param userId userId
     * @return  String
     */
    String updateAttributeName(AttributeNameVO attributeNameVO,String userId);

    /**
     * 删除属性名称
     * @param attributeNameVO attributeNameVO
     * @param userId userId
     * @return  String
     */
    String removeAttributeName(AttributeNameVO attributeNameVO,String userId);

    /**
     * 查询属性名称具体信息
     * @param attributeNameVO 属性名称对象
     * @return 属性名称对象
     */
    AttributeNameVO findAttributeName(AttributeNameVO attributeNameVO);

    /**
     * 分页查询相关信息
     * @param attributeNameVO 属性名称对象
     * @param pageRequest 分页对象
     * @return 分页结果
     */
    PageDTO<AttributeNameVO> searchAttributeNamePage(AttributeNameVO attributeNameVO, PageRequest pageRequest);

    /**
     * 批量删除属性名称
     * @param ids 要删除的集合对象ids
     * @param userId 操作人id
     * @return 处理结果
     */
    String deleteAttributeNameList(List<Long> ids, String userId);


}
