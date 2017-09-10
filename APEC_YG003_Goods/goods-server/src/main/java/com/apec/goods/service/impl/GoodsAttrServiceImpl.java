package com.apec.goods.service.impl;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.springcloud.SpringCloudClient;
import com.apec.goods.dao.AttributeNameDAO;
import com.apec.goods.dao.GoodsAttrDAO;
import com.apec.goods.model.AttributeName;
import com.apec.goods.model.Goods;
import com.apec.goods.model.GoodsAttr;
import com.apec.goods.service.GoodsAttrService;
import com.apec.goods.util.SnowFlakeKeyGen;
import com.apec.goods.vo.GoodsAttrVO;
import com.apec.goods.vo.GoodsVO;
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
public class GoodsAttrServiceImpl implements GoodsAttrService {

    @Autowired
    private GoodsAttrDAO goodsAttrDAO;

    @Autowired
    private AttributeNameDAO attributeNameDAO;

    @Autowired
    private SnowFlakeKeyGen idGen;

    @InjectLogger
    private Logger logger;

    @Autowired
    SpringCloudClient springCloudClient;

    @Override
    public String saveGoodsAttr(GoodsAttrVO goodsAttrVO,String userId) {
        GoodsAttr goodsAttr = new GoodsAttr();
        //根据商品属性配置关系中的属性名称id查询属性名称具体信息
        AttributeName attributeName = attributeNameDAO.findOne(goodsAttrVO.getAttrId());
        if(attributeName == null || attributeName.getAttributeType() == null){
            logger.info("[GoodsAttrServiceImpl][saveGoodsAttr] attributeName is empty :id {}",goodsAttrVO.getAttrId());
            return Constants.ERROR_100003;
        }
        //复制相关传入信息
        BeanUtil.copyPropertiesIgnoreNullFilds(goodsAttrVO,goodsAttr);
        //将查询到的属性名称信息放入属性关系冗余字段中
        goodsAttr.setGoodsAttrName(attributeName.getAttrName());
        goodsAttr.setRemoteUrlParam(attributeName.getRemoteUrlParam());
        goodsAttr.setShowPreFix(attributeName.getShowPreFix());
        goodsAttr.setShowSuFix(attributeName.getShowSuFix());
        //配置商品属性中商品信息
        Goods goods = new Goods();
        goods.setId(goodsAttrVO.getGoodsId());
        goodsAttr.setGoods(goods);
        goodsAttr.setCreateBy(userId);
        goodsAttr.setCreateDate(new Date());
        goodsAttr.setId(idGen.nextId());
        goodsAttr.setEnableFlag(EnableFlag.Y);
        goodsAttrDAO.save(goodsAttr);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String updateGoodsAttr(GoodsAttrVO goodsAttrVO,String userId) {
        GoodsAttr goodsAttr = goodsAttrDAO.findOne(goodsAttrVO.getId());
        if(goodsAttr == null){
            logger.info("[GoodsAttrServiceImpl][updateGoodsAttr]can't find goodsAttr :id{}" ,goodsAttrVO.getId());
            return Constants.ENABLE_NOT_NULL;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(goodsAttrVO,goodsAttr);
        if(goodsAttr.getAttrId() != null && goodsAttr.getAttrId() != 0L){
            //修改商品属性关系中属性名称信息
            AttributeName attributeName = attributeNameDAO.findOne(goodsAttr.getAttrId());
            if(attributeName == null || attributeName.getAttributeType() == null){
                logger.info("[GoodsAttrServiceImpl][updateGoodsAttr]the param of goodsAttr -- attributeName is empty attrId{}" ,goodsAttrVO.getAttrId());
                return Constants.ERROR_100003;
            }
            //将相关属性存入商品属性冗余字段信息中
            goodsAttr.setGoodsAttrName(attributeName.getAttrName());
            goodsAttr.setRemoteUrlParam(attributeName.getRemoteUrlParam());
            goodsAttr.setShowPreFix(attributeName.getShowPreFix());
            goodsAttr.setShowSuFix(attributeName.getShowSuFix());
        }
        goodsAttr.setLastUpdateBy(userId);
        goodsAttr.setLastUpdateDate(new Date());
        goodsAttrDAO.save(goodsAttr);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String removeGoodsAttr(GoodsAttrVO goodsAttrVO,String userId) {
        GoodsAttr goodsAttr = goodsAttrDAO.findOne(goodsAttrVO.getId());
        if(goodsAttr == null){
            logger.info("[GoodsAttrServiceImpl][removeGoodsAttr]can't find goodsAttr :id{}" ,goodsAttrVO.getId());
            return Constants.ENABLE_NOT_NULL;
        }
        goodsAttr.setEnableFlag(EnableFlag.N);
        goodsAttr.setLastUpdateBy(userId);
        goodsAttr.setLastUpdateDate(new Date());
        goodsAttrDAO.save(goodsAttr);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public GoodsAttrVO findGoodsAttr(GoodsAttrVO goodsAttrVO) {
        GoodsAttrVO goodsAttrVO1 = new GoodsAttrVO();
        GoodsAttr goodsAttr = goodsAttrDAO.findOne(goodsAttrVO.getId());
        BeanUtil.copyPropertiesIgnoreNullFilds(goodsAttr,goodsAttrVO1);
        return goodsAttrVO1;
    }

    @Override
    @Transactional
    public String deleteGoodsAttrList(List<Long> ids,String userId){
        goodsAttrDAO.deleteGoodsAttrList(ids,userId);
        return Constants.RETURN_SUCESS;
    }



}
