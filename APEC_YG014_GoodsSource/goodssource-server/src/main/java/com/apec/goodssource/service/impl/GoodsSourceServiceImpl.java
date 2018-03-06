package com.apec.goodssource.service.impl;

import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.dto.UserInfoVO;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.springcloud.SpringCloudClient;
import com.apec.goodssource.dao.GmcSkuAttrDAO;
import com.apec.goodssource.dao.GmcSkuInfoDAO;
import com.apec.goodssource.dto.GoodsSourceDTO;
import com.apec.goodssource.model.GmcSkuAttr;
import com.apec.goodssource.model.GmcSkuInfo;
import com.apec.goodssource.model.QGmcSkuInfo;
import com.apec.goodssource.service.GoodsSourceService;
import com.apec.goodssource.util.SnowFlakeKeyGen;
import com.apec.goodssource.vo.GmcSkuBatchVO;
import com.apec.goodssource.vo.GmcSkuInfoVO;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * <p>
 * Title: 货源
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Date:  -
 * </p>
 *
 * @author yirde <532186767@qq.com>
 * @version 1.0
 */
@Service
public class GoodsSourceServiceImpl implements GoodsSourceService {

    @InjectLogger
    private Logger logger;

    @Autowired
    private SnowFlakeKeyGen idGen;

    @Autowired
    private GmcSkuInfoDAO gmcSkuInfoDAO;

    @Autowired
    private GmcSkuAttrDAO gmcSkuAttrDAO;

    @Value("${user_org_url}")
    private String userOrgUrl;

    @Autowired
    private SpringCloudClient springCloudClient;


    @Override
    @Transactional( rollbackFor =  Exception.class )
    public String addNewGoodsSourceInfo(GmcSkuBatchVO gmcSkuBatchVO, String userId) {
        //根据用户ID查询组织名称
        Map<String, String> requestMap = new HashMap<>(16);
        requestMap.put("id",String.valueOf(gmcSkuBatchVO.getUserOrgId()));
        String responseStr = springCloudClient.post(userOrgUrl, BaseJsonUtil.toJSONString(requestMap));
        ResultData<String> resultData = BaseJsonUtil.parseObject(responseStr, ResultData.class);
        String orgName = resultData.getData();
        logger.info("【GoodsSourceServiceImpl】【addNewGoodsSourceInfo】 orgName:{}",orgName);
        gmcSkuBatchVO.getListSkuInfo().forEach(gmcSkuInfoVO -> {
            if(gmcSkuInfoVO.getId() == null || gmcSkuInfoVO.getId() == 0L){
                   return;
            }
            GmcSkuInfo gmcSkuInfo = gmcSkuInfoDAO.findByIdAndEnableFlag(gmcSkuInfoVO.getId(), EnableFlag.Y);
            if (gmcSkuInfo == null) {
                 gmcSkuInfo = new GmcSkuInfo();
            }
            Date date = new Date();
            //属性Copy
            BeanUtil.copyPropertiesIgnoreNullFilds(gmcSkuInfoVO,gmcSkuInfo);
            gmcSkuInfo.setOrgName(orgName);
            gmcSkuInfo.setCreateDate(date);
            gmcSkuInfo.setEnableFlag(EnableFlag.Y);
            gmcSkuInfoDAO.save(gmcSkuInfo);

            final Long skuId = gmcSkuInfo.getId();
            List<GmcSkuAttr> list = new ArrayList<>();
            gmcSkuInfoVO.getList().forEach(gmcSkuAttrVO -> {
                if(gmcSkuAttrVO.getId() == null || gmcSkuAttrVO.getId() == 0L){
                    return;
                }
                GmcSkuAttr gmcSkuAttr = new GmcSkuAttr();
                BeanUtil.copyPropertiesIgnoreNullFilds(gmcSkuAttrVO,gmcSkuAttr);
                gmcSkuAttr.setSkuId(skuId);
                gmcSkuAttr.setCreateDate(date);
                gmcSkuAttr.setEnableFlag(EnableFlag.Y);
                list.add(gmcSkuAttr);
            });
            gmcSkuAttrDAO.save(list);
        });
        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional( rollbackFor =  Exception.class )
    public String updateGoodsSourceInfo(GmcSkuBatchVO gmcSkuBatchVO) {
        //根据用户ID查询组织名称
        Map<String, String> requestMap = new HashMap<>(16);
        requestMap.put("id",String.valueOf(gmcSkuBatchVO.getUserOrgId()));
        String responseStr = springCloudClient.post(userOrgUrl, BaseJsonUtil.toJSONString(requestMap));
        ResultData<String> resultData = BaseJsonUtil.parseObject(responseStr, ResultData.class);
        String orgName = resultData.getData();
        logger.info("【GoodsSourceServiceImpl】【addNewGoodsSourceInfo】 orgName:{}",orgName);
        gmcSkuBatchVO.getListSkuInfo().forEach(gmcSkuInfoVO -> {
            if(gmcSkuInfoVO.getId() == null || gmcSkuInfoVO.getId() == 0L){
                return;
            }
            //查询
            GmcSkuInfo gmcSkuInfo = gmcSkuInfoDAO.findByIdAndEnableFlag(gmcSkuInfoVO.getId(), EnableFlag.Y);
            if(gmcSkuInfo == null){
                gmcSkuInfo = new GmcSkuInfo();
            }
            Date date = new Date();
            gmcSkuInfo.setOrgName(orgName);
            //更新
            if(gmcSkuInfoVO.getAmount() != null) {
                gmcSkuInfo.setAmount(gmcSkuInfoVO.getAmount());
            }
            if(StringUtils.isNotBlank(gmcSkuInfoVO.getSkuName())){
                gmcSkuInfo.setSkuName(gmcSkuInfoVO.getSkuName());
            }
            if(gmcSkuInfoVO.getWeight() != null){
                gmcSkuInfo.setWeight(gmcSkuInfoVO.getWeight());
            }
            if(gmcSkuInfoVO.getStartAmount()!= null){
                gmcSkuInfo.setStartAmount(gmcSkuInfoVO.getStartAmount());
            }
            if(gmcSkuInfoVO.getEndAmount() != null){
                gmcSkuInfo.setEndAmount(gmcSkuInfoVO.getEndAmount());
            }
            if(gmcSkuInfoVO.getEnableFlag() !=  null){
                gmcSkuInfo.setEnableFlag(gmcSkuInfoVO.getEnableFlag());
            }
            gmcSkuInfo.setLastUpdateDate(date);
            gmcSkuInfoDAO.save(gmcSkuInfo);

            final Long skuId = gmcSkuInfo.getId();
            if(gmcSkuInfoVO.getList() != null){
                 List<GmcSkuAttr> list = new ArrayList<>();
                gmcSkuInfoVO.getList().forEach(gmcSkuAttrVO -> {
                    if(gmcSkuAttrVO.getId() == null || gmcSkuAttrVO.getId() == 0L){
                        return;
                    }
                     GmcSkuAttr gmcSkuAttr = gmcSkuAttrDAO.findOne(gmcSkuAttrVO.getId());
                     if(gmcSkuAttr == null){
                         gmcSkuAttr = new GmcSkuAttr();
                     }
                     BeanUtil.copyPropertiesIgnoreNullFilds(gmcSkuAttrVO,gmcSkuAttr);
                     gmcSkuAttr.setSkuId(skuId);
                     if(gmcSkuAttr.getCreateDate() == null ){
                         gmcSkuAttr.setCreateDate(date);
                     }
                     gmcSkuAttr.setLastUpdateDate(date);
                     if(gmcSkuAttr.getEnableFlag() == null) {
                         gmcSkuAttr.setEnableFlag(EnableFlag.Y);
                     }
                     list.add(gmcSkuAttr);
                 });
                gmcSkuAttrDAO.save(list);
            }
        });
        return Constants.RETURN_SUCESS;
    }

    @Override
    public PageDTO<GmcSkuInfoVO> queryGmcSkuInfoPage(GoodsSourceDTO goodsSourceDTO, PageRequest pageRequest) {
        PageDTO<GmcSkuInfoVO> result = new PageDTO<>();
        List<GmcSkuInfoVO> list = new ArrayList<>();
        Page<GmcSkuInfo> page = gmcSkuInfoDAO.findAll(getInputCondition(goodsSourceDTO),pageRequest);
        page.forEach(productInfo -> {
            GmcSkuInfoVO gmcSkuInfoVO = new GmcSkuInfoVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(productInfo,gmcSkuInfoVO);
            list.add(gmcSkuInfoVO);
        });
        result.setRows(list);
        result.setTotalPages(page.getTotalPages());
        result.setNumber(page.getNumber() + 1);
        result.setTotalElements(page.getTotalElements());
        return result;
    }

    /**
     * 根据多种情况查询信息
     * 包括like：skuName,address  eq:productType,userId  in:userId
     * @param  goodsSourceDTO vo
     * @return  Predicate
     */
    private Predicate getInputCondition(GoodsSourceDTO goodsSourceDTO) {
        List<BooleanExpression> predicates = new ArrayList<>();
        if (null != goodsSourceDTO) {
            if (goodsSourceDTO.getUserId() != null) {
                predicates.add(QGmcSkuInfo.gmcSkuInfo.userId.eq(goodsSourceDTO.getUserId()));
            }
            if (goodsSourceDTO.getUserOrgId() != null) {
                predicates.add(QGmcSkuInfo.gmcSkuInfo.userOrgId.eq(goodsSourceDTO.getUserOrgId()));
            }
            if (StringUtils.isNotBlank(goodsSourceDTO.getAddress())) {
                predicates.add(QGmcSkuInfo.gmcSkuInfo.address.like("%" + goodsSourceDTO.getAddress() + "%" ).or(
                               QGmcSkuInfo.gmcSkuInfo.addressDetail.like("%" + goodsSourceDTO.getAddress() + "%" )
                ));
            }
            if (StringUtils.isNotBlank(goodsSourceDTO.getCategory())) {
                predicates.add(QGmcSkuInfo.gmcSkuInfo.skuName.like("%" + goodsSourceDTO.getCategory() + "%" ));
            }
            if (StringUtils.isNotBlank(goodsSourceDTO.getSpec())) {
                predicates.add(QGmcSkuInfo.gmcSkuInfo.skuName.like("%" + goodsSourceDTO.getSpec() + "%" ));
            }
        }
        predicates.add(QGmcSkuInfo.gmcSkuInfo.enableFlag.eq(EnableFlag.Y));
        return BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
    }
}
