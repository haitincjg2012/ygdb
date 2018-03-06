package com.apec.goods.service.impl;

import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.springcloud.SpringCloudClient;
import com.apec.goods.dao.AttributeNameDAO;
import com.apec.goods.dao.AttributeValueDAO;
import com.apec.goods.model.*;
import com.apec.goods.service.AttributeNameService;
import com.apec.goods.util.SnowFlakeKeyGen;
import com.apec.goods.vo.AttributeNameVO;
import com.apec.goods.vo.AttributeValueVO;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by hmy on 2017/7/10.
 * @author hmy
 */
@Service
public class AttributeNameServiceImpl implements AttributeNameService {

    @Autowired
    private AttributeNameDAO attributeNameDAO;

    @Autowired
    private AttributeValueDAO attributeValueDAO;

    @Autowired
    private SnowFlakeKeyGen idGen;

    @InjectLogger
    private Logger logger;

    @Autowired
    SpringCloudClient springCloudClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveAttributeName(AttributeNameVO attributeNameVO, String userID) {
        //新增属性名称
        AttributeName attributeName = new AttributeName();
        BeanUtil.copyPropertiesIgnoreNullFilds(attributeNameVO,attributeName);
        attributeName.setEnableFlag(EnableFlag.Y);
        attributeName.setCreateBy(userID);
        attributeName.setId(idGen.nextId());
        attributeName.setCreateDate(new Date());
        attributeNameDAO.save(attributeName);
        //保存相应的属性值对象
        List<AttributeValueVO> attributeValueVOS = attributeNameVO.getAttributeValueVOS();
        if(attributeValueVOS != null && attributeValueVOS.size() > 0){
            for(AttributeValueVO attributeValueVO:attributeValueVOS){
                if(attributeValueVO != null && StringUtils.isNotBlank(attributeValueVO.getAttrValue())){
                    AttributeValue attributeValue = new AttributeValue();
                    BeanUtil.copyPropertiesIgnoreNullFilds(attributeValueVO,attributeValue);
                    attributeValue.setAttributeNameId(attributeName.getId());
                    attributeValue.setId(idGen.nextId());
                    attributeValue.setCreateBy(userID);
                    attributeValue.setCreateDate(new Date());
                    attributeValue.setEnableFlag(EnableFlag.Y);
                    attributeValueDAO.save(attributeValue);
                }
            }
        }
        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateAttributeName(AttributeNameVO attributeNameVO,String userId) {
        //修改属性名称对象
        AttributeName attributeName = attributeNameDAO.findOne(attributeNameVO.getId());
        if(attributeName == null){
            logger.info("[AttributeNameServiceImpl][updateAttributeName]can't find attributeName :id{}" ,attributeNameVO.getId());
            return Constants.ENABLE_NOT_NULL;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(attributeNameVO,attributeName);
        attributeName.setLastUpdateBy(userId);
        attributeName.setLastUpdateDate(new Date());
        attributeNameDAO.save(attributeName);
        List<AttributeValueVO> attributeValueVOList = attributeNameVO.getAttributeValueVOS();
        //查询所有的属性对象
        List<AttributeValue> list = attributeValueDAO.findByAttributeNameIdAndEnableFlagOrderBySort(attributeNameVO.getId(),EnableFlag.Y);
        if(list != null && list.size() > 0){
            for(AttributeValue attributeValue:list){
                AttributeValue value = new AttributeValue();
                BeanUtil.copyPropertiesIgnoreNullFilds(attributeValue,value);
                AttributeValueVO vo = new AttributeValueVO();
                //不存在的对象
                boolean flag = true;
                if(attributeValueVOList != null && attributeValueVOList.size() > 0){
                    for(AttributeValueVO attributeValueVO:attributeValueVOList){
                        if(attributeValue != null && attributeValueVO != null && attributeValueVO.getId() != null && attributeValueVO.getId() != 0L && value.getId().equals(attributeValueVO.getId())){
                            //该对象存在
                            flag = false;
                            BeanUtil.copyPropertiesIgnoreNullFilds(attributeValueVO,vo);
                        }
                    }
                }
                if(flag){
                    //删除不存在的对象
                    value.setEnableFlag(EnableFlag.N);
                    value.setLastUpdateDate(new Date());
                    value.setLastUpdateBy(userId);
                    attributeValueDAO.save(value);
                }else{
                    //对存在的已改变的对象进行修改操作
                    if(!StringUtils.equals(vo.getAttrValue(),value.getAttrValue()) || !Objects.equals(value.getSort(),vo.getSort())){
                        value.setAttrValue(vo.getAttrValue());
                        value.setSort(vo.getSort());
                        value.setLastUpdateDate(new Date());
                        value.setLastUpdateBy(userId);
                        attributeValueDAO.save(value);
                    }
                }
            }
        }

        //新增的对象进行添加到数据库的操作
        if(attributeValueVOList != null && attributeValueVOList.size() > 0){
            for(AttributeValueVO attributeValueVO:attributeValueVOList){
                //如果属性值不为空其id为空且其属性值Value不为空时，该属性值为新增属性值
                boolean flagNew = attributeValueVO != null && StringUtils.isNoneBlank(attributeValueVO.getAttrValue()) && (attributeValueVO.getId() == null || attributeValueVO.getId() == 0L);
                if(flagNew){
                    //该对象为新增对象
                    AttributeValue attributeValue = new AttributeValue();
                    BeanUtil.copyPropertiesIgnoreNullFilds(attributeValueVO,attributeValue);
                    attributeValue.setAttributeNameId(attributeName.getId());
                    attributeValue.setId(idGen.nextId());
                    attributeValue.setCreateDate(new Date());
                    attributeValue.setCreateBy(userId);
                    attributeValue.setEnableFlag(EnableFlag.Y);
                    attributeValueDAO.save(attributeValue);
                }
            }
        }
        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String removeAttributeName(AttributeNameVO attributeNameVO,String userId) {
        AttributeName attributeName = attributeNameDAO.findOne(attributeNameVO.getId());
        if(attributeName == null){
            logger.info("[AttributeNameServiceImpl][removeAttributeName]can't find attributeName :id{}" ,attributeNameVO.getId());
            return Constants.ENABLE_NOT_NULL;
        }
        attributeNameDAO.removeAttributeName(attributeNameVO.getId(),userId);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public AttributeNameVO findAttributeName(AttributeNameVO attributeNameVO) {
        AttributeNameVO attributeNameVO1 = new AttributeNameVO();
        AttributeName attributeName = attributeNameDAO.findOne(attributeNameVO.getId());
        if(attributeName == null){
            return null;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(attributeName,attributeNameVO1);
        List<AttributeValueVO> list = new ArrayList<>();
        List<AttributeValue> attributeValues = attributeValueDAO.findByAttributeNameIdAndEnableFlagOrderBySort(attributeName.getId(),EnableFlag.Y);
        if(attributeValues != null && attributeValues.size() > 0){
            for(AttributeValue attributeValue:attributeValues){
                AttributeValueVO attributeValueVO = new AttributeValueVO();
                BeanUtil.copyPropertiesIgnoreNullFilds(attributeValue,attributeValueVO);
                list.add(attributeValueVO);
            }
        }
        attributeNameVO1.setAttributeValueVOS(list);
        return attributeNameVO1;
    }

    @Override
    public PageDTO<AttributeNameVO> searchAttributeNamePage(AttributeNameVO attributeNameVO, PageRequest pageRequest) {
        Page<AttributeName> attributeNamePage = attributeNameDAO.findAll(getInputCondition(attributeNameVO),pageRequest);
        PageDTO<AttributeNameVO> pageDTO = new PageDTO<>();
        List<AttributeNameVO> list = new ArrayList<>();
        for(AttributeName attributeName:attributeNamePage){
            AttributeNameVO attributeNameVO1 = new AttributeNameVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(attributeName,attributeNameVO1);
            List<AttributeValueVO> list1 = new ArrayList<>();
            if(attributeName != null){
                List<AttributeValue> attributeValues = attributeValueDAO.findByAttributeNameIdAndEnableFlagOrderBySort(attributeName.getId(),EnableFlag.Y);
                for(AttributeValue attributeValue:attributeValues){
                        AttributeValueVO attributeValueVO = new AttributeValueVO();
                        BeanUtil.copyPropertiesIgnoreNullFilds(attributeValue,attributeValueVO);
                        list1.add(attributeValueVO);
                }
            }
            attributeNameVO1.setAttributeValueVOS(list1);
            list.add(attributeNameVO1);
        }
        pageDTO.setTotalElements(attributeNamePage.getTotalElements());
        pageDTO.setTotalPages(attributeNamePage.getTotalPages());
        pageDTO.setNumber(attributeNamePage.getNumber() + 1);
        pageDTO.setRows(list);
        return pageDTO;
    }

    /**
     * 多条件查询商品信息
     * 根据多种情况查询
     * 包括like:attrName,eq:showPreFix,showSuFix,remoteUrlParam
     * @param vo 条件对象
     * @return Predicate
     */
    private Predicate getInputCondition(AttributeNameVO vo)
    {
        List<BooleanExpression> predicates = new ArrayList<>();
        if(null != vo)
        {
            if(!StringUtils.isEmpty(vo.getAttrName()))
            {
                predicates.add(QAttributeName.attributeName.attrName.like("%"+vo.getAttrName()+"%"));
            }
            if(!StringUtils.isEmpty(vo.getShowPreFix()))
            {
                predicates.add(QAttributeName.attributeName.showPreFix.eq(vo.getRemark()));
            }
            if(!StringUtils.isEmpty(vo.getShowSuFix()))
            {
                predicates.add(QAttributeName.attributeName.showSuFix.eq(vo.getShowSuFix()));
            }
            if(!StringUtils.isEmpty(vo.getRemoteUrlParam()))
            {
                predicates.add(QAttributeName.attributeName.remoteUrlParam.eq(vo.getRemoteUrlParam()));
            }

        }
        predicates.add(QAttributeName.attributeName.enableFlag.eq(EnableFlag.Y));
        return BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
    }

    /**
     * 批量删除属性名称
     * @param  ids ids
     * @param userId userId
     * @return String
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteAttributeNameList(List<Long> ids, String userId){
        //批量删除属性名称
        attributeNameDAO.deleteAttributeNameList(ids,userId);
        return Constants.RETURN_SUCESS;
    }



}
