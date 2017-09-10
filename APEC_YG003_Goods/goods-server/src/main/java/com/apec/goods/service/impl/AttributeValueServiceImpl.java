package com.apec.goods.service.impl;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.springcloud.SpringCloudClient;
import com.apec.goods.dao.AttributeValueDAO;
import com.apec.goods.model.AttributeName;
import com.apec.goods.model.AttributeValue;
import com.apec.goods.service.AttributeValueService;
import com.apec.goods.util.SnowFlakeKeyGen;
import com.apec.goods.vo.AttributeValueVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by hmy on 2017/7/10.
 */
@Service
public class AttributeValueServiceImpl implements AttributeValueService {

    @Autowired
    private AttributeValueDAO attributeValueDAO;

    @Autowired
    private SnowFlakeKeyGen idGen;

    @InjectLogger
    private Logger logger;

    @Autowired
    SpringCloudClient springCloudClient;

    @Override
    public String saveAttributeValue(AttributeValueVO attributeValueVO, String userId) {
        AttributeValue attributeValue = new AttributeValue();
        BeanUtil.copyPropertiesIgnoreNullFilds(attributeValueVO,attributeValue);
        AttributeName attributeName = new AttributeName();
        attributeName.setId(attributeValueVO.getAttributeNameId());
        attributeValue.setAttributeName(attributeName);
        attributeValue.setEnableFlag(EnableFlag.Y);
        attributeValue.setId(idGen.nextId());
        attributeValue.setCreateDate(new Date());
        attributeValue.setCreateBy(userId);
        attributeValueDAO.save(attributeValue);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String updateAttributeValue(AttributeValueVO attributeValueVO,String userId) {
        AttributeValue attributeValue = attributeValueDAO.findOne(attributeValueVO.getId());
        if(attributeValue == null){
            logger.info("can't find attributeValue :id{}" ,attributeValueVO.getId());
            return Constants.ENABLE_NOT_NULL;
        }
        if(!StringUtils.isEmpty(attributeValueVO.getAttrValue())){
            attributeValue.setAttrValue(attributeValueVO.getAttrValue());
        }
        attributeValue.setLastUpdateDate(new Date());
        attributeValue.setLastUpdateBy(userId);
        attributeValueDAO.save(attributeValue);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String removeAttributeValue(AttributeValueVO attributeValueVO,String userId) {
        AttributeValue attributeValue = attributeValueDAO.findOne(attributeValueVO.getId());
        if(attributeValue == null){
            logger.info("can't find attributeValue :id{}" ,attributeValueVO.getId());
            return Constants.ENABLE_NOT_NULL;
        }
        attributeValue.setEnableFlag(EnableFlag.N);
        attributeValue.setLastUpdateBy(userId);
        attributeValue.setLastUpdateDate(new Date());
        attributeValueDAO.save(attributeValue);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public AttributeValueVO findAttributeValue(AttributeValueVO attributeValueVO) {
        AttributeValueVO attributeValueVO1 = new AttributeValueVO();
        AttributeValue attributeValue = attributeValueDAO.findOne(attributeValueVO.getId());
        BeanUtil.copyPropertiesIgnoreNullFilds(attributeValue,attributeValueVO1);
        if(attributeValue.getAttributeName() != null){
            attributeValueVO1.setAttributeNameId(attributeValue.getAttributeName().getId());
        }
        return attributeValueVO1;
    }

    /**
     * 批量删除attributeValue
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public String deleteAttributeValueList(List<Long> ids, String userId){
        attributeValueDAO.deleteAttributeValueList(ids,userId);
        return Constants.RETURN_SUCESS;
    }


}
