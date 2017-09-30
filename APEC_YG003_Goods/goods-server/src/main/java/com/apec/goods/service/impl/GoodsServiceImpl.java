package com.apec.goods.service.impl;

import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.util.BeanUtil;
import com.apec.goods.dao.AttributeNameDAO;
import com.apec.goods.dao.AttributeValueDAO;
import com.apec.goods.dao.GoodsAttrDAO;
import com.apec.goods.dao.GoodsDAO;
import com.apec.goods.model.*;
import com.apec.goods.service.GoodsService;
import com.apec.goods.util.SnowFlakeKeyGen;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.springcloud.SpringCloudClient;
import com.apec.goods.vo.AttributeValueVO;
import com.apec.goods.vo.GoodsAttrVO;
import com.apec.goods.vo.GoodsVO;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDAO goodsDAO;

    @Autowired
    private GoodsAttrDAO goodsAttrDAO;

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
    @Transactional(rollbackFor = {Exception.class})
    public String saveGoods(GoodsVO goodsVO,String userId) {
        //初始化并获取相关商品信息
        Goods goods = new Goods();
        BeanUtil.copyPropertiesIgnoreNullFilds(goodsVO,goods);
        goods.setCreateDate(new Date());
        goods.setEnableFlag(EnableFlag.Y);
        goods.setId(idGen.nextId());
        goods.setCreateBy(userId);
        //组装商品属性的对象值
        List<GoodsAttrVO> goodsAttrVOS = goodsVO.getGoodsAttrVOList();
        List<GoodsAttr> goodsAttrList = new ArrayList<>();
        //遍历验证数据的有效性及组装相关信息
        if(goodsAttrVOS != null && goodsAttrVOS.size() > 0){
            for(int i = 0 ; i < goodsAttrVOS.size(); i++){
                GoodsAttrVO goodsAttrVO = goodsAttrVOS.get(i);
                if(goodsAttrVO.getAttributeShowLevel() == null ){
                    logger.info("[GoodsServiceImpl][saveGoods] param of goodsAttr is empty");
                    return Constants.ERROR_100003;
                }
                GoodsAttr goodsAttr = new GoodsAttr();
                BeanUtil.copyPropertiesIgnoreNullFilds(goodsAttrVO,goodsAttr);
                //根据属性名称id查询属性名称具体信息
                AttributeName attributeName = attributeNameDAO.findOne(goodsAttrVO.getAttrId());
                if(attributeName == null || attributeName.getAttributeType() == null){
                    logger.info("[GoodsServiceImpl][saveGoods]the param of goodsAttr -- attributeName is empty ：attributeNameID{}",goodsAttrVO.getAttrId());
                    return Constants.ERROR_100003;
                }
                //将相关属性存入商品属性冗余字段信息中
                goodsAttr.setGoodsAttrName(attributeName.getAttrName());
                goodsAttr.setRemoteUrlParam(attributeName.getRemoteUrlParam());
                goodsAttr.setShowPreFix(attributeName.getShowPreFix());
                goodsAttr.setShowSuFix(attributeName.getShowSuFix());
                goodsAttr.setId(idGen.nextId());
                goodsAttr.setCreateDate(new Date());
                goodsAttr.setCreateBy(userId);
                goodsAttr.setEnableFlag(EnableFlag.Y);
                goodsAttr.setGoods(goods);
                goodsAttrList.add(goodsAttr);
            }
        }
        //保存商品信息
        goodsDAO.save(goods);
        //保存商品属性关系
        goodsAttrDAO.save(goodsAttrList);
        goodsVO.setId(goods.getId());
        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional
    public String updateGoods(GoodsVO goodsVO,String userId) {
        Goods goods  = goodsDAO.findOne(goodsVO.getId());
        if(goods == null){
            logger.info("[GoodsServiceImpl][updateGoods]can't find goods :id{}" ,goodsVO.getId());
            return Constants.ENABLE_NOT_NULL;
        }
        if(!StringUtils.isEmpty(goodsVO.getGoodsName())){
            goods.setGoodsName(goodsVO.getGoodsName());
        }if(!StringUtils.isEmpty(goodsVO.getRemark())){
            goods.setRemark(goodsVO.getRemark());
        }
        goods.setMainGoods(goodsVO.isMainGoods());//修改是否是主类信息
        goods.setLastUpdateBy(userId);
        goods.setLastUpdateDate(new Date());
        List<GoodsAttr> goodsAttrList = goodsAttrDAO.findByGoodsIdAndEnableFlagOrderBySort(goods.getId(),EnableFlag.Y);
        if(goodsVO.getGoodsAttrVOList() == null || goodsVO.getGoodsAttrVOList().size() <= 0){
            //修改后的商品无任何属性，删除其原有的所有属性
            goodsAttrDAO.updateByGoodId(goods.getId(),userId);
        }else{
            if(goodsAttrList != null && goodsAttrList.size() > 0){
                for(GoodsAttr goodsAttr:goodsAttrList){
                    boolean isNotExist = true;//该属性在新修改的商品信息中不存在了
                    GoodsAttrVO goodsAttrVO1 = new GoodsAttrVO();
                    for(GoodsAttrVO goodsAttrVO:goodsVO.getGoodsAttrVOList()){
                        if(goodsAttr.getId().equals(goodsAttrVO.getId())){
                            isNotExist = false;
                            BeanUtil.copyPropertiesIgnoreNullFilds(goodsAttrVO,goodsAttrVO1);
                            break;
                        }
                    }
                    if(isNotExist){
                        //不存在的商品属性，进行删除
                        goodsAttr.setEnableFlag(EnableFlag.N);
                        goodsAttr.setLastUpdateBy(userId);
                        goodsAttr.setLastUpdateDate(new Date());
                        goodsAttrDAO.save(goodsAttr);
                    }else{
                        //商品属性存在，若属性关系中属性名称id发生改变，则查询属性名称信息进行修改
                        if(goodsAttr.getAttrId() != goodsAttrVO1.getAttrId()){
                            //根据商品属性配置关系中的属性名称id查询属性名称具体信息
                            AttributeName attributeName = attributeNameDAO.findOne(goodsAttrVO1.getAttrId());
                            if(attributeName == null || attributeName.getAttributeType() == null){
                                logger.info("[GoodsServiceImpl][updateGoods] attributeName is empty :id {}",goodsAttrVO1.getAttrId());
                                return Constants.ERROR_100003;
                            }
                            //复制相关传入信息
                            //将查询到的属性名称信息放入属性关系冗余字段中
                            goodsAttr.setGoodsAttrName(attributeName.getAttrName());
                            goodsAttr.setRemoteUrlParam(attributeName.getRemoteUrlParam());
                            goodsAttr.setShowPreFix(attributeName.getShowPreFix());
                            goodsAttr.setShowSuFix(attributeName.getShowSuFix());
                        }
                        BeanUtil.copyPropertiesIgnoreNullFilds(goodsAttrVO1,goodsAttr);
                        goodsAttrDAO.save(goodsAttr);
                    }
                }
            }
            for(GoodsAttrVO goodsAttrVO:goodsVO.getGoodsAttrVOList()){
                if(goodsAttrVO.getId() == null || goodsAttrVO.getId() == 0L){
                    //没有商品属性关系id，意味着该属性关系为新增
                    if(goodsAttrVO.getAttributeShowLevel() == null ){
                        logger.info("[GoodsServiceImpl][saveGoods] param of goodsAttr is empty");
                        return Constants.ERROR_100003;
                    }
                    GoodsAttr goodsAttr = new GoodsAttr();
                    BeanUtil.copyPropertiesIgnoreNullFilds(goodsAttrVO,goodsAttr);
                    //根据属性名称id查询属性名称具体信息
                    AttributeName attributeName = attributeNameDAO.findOne(goodsAttrVO.getAttrId());
                    if(attributeName == null || attributeName.getAttributeType() == null){
                        logger.info("[GoodsServiceImpl][saveGoods]the param of goodsAttr -- attributeName is empty ：attributeNameID{}",goodsAttrVO.getAttrId());
                        return Constants.ERROR_100003;
                    }
                    //将相关属性存入商品属性冗余字段信息中
                    goodsAttr.setGoodsAttrName(attributeName.getAttrName());
                    goodsAttr.setRemoteUrlParam(attributeName.getRemoteUrlParam());
                    goodsAttr.setShowPreFix(attributeName.getShowPreFix());
                    goodsAttr.setShowSuFix(attributeName.getShowSuFix());
                    goodsAttr.setId(idGen.nextId());
                    goodsAttr.setCreateDate(new Date());
                    goodsAttr.setCreateBy(userId);
                    goodsAttr.setEnableFlag(EnableFlag.Y);
                    goodsAttr.setGoods(goods);
                    goodsAttrDAO.save(goodsAttr);
                }
            }
        }

        //修改商品信息
        goodsDAO.save(goods);
        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional
    public String removeGoods(GoodsVO goodsVO,String userId) {
        Goods goods = goodsDAO.findOne(goodsVO.getId());
        if(goods == null){
            logger.info("[GoodsServiceImpl][removeGoods]can't find goods :id{}" ,goodsVO.getId());
            return Constants.ENABLE_NOT_NULL;
        }
        goods.setEnableFlag(EnableFlag.N);
        goods.setLastUpdateBy(userId);
        goods.setLastUpdateDate(new Date());
        goodsDAO.save(goods);
        //删除相关属性关系对象
        goodsAttrDAO.updateByGoodId(goods.getId(),userId);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public GoodsVO getGoods(GoodsVO goodsVO) {
        //查询商品信息
        Goods goods =  null;
        if(goodsVO == null || goodsVO.getId() == null || goodsVO.getId() == 0L){
            //不存在则获取数据库中第一个主类信息
            goods = goodsDAO.findFirstByMainGoodsAndEnableFlagOrderByCreateDateDesc(true,EnableFlag.Y);
        }else{
            goods = goodsDAO.findOne(goodsVO.getId());
        }
        if(goods == null){
            return goodsVO;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(goods,goodsVO);
        List<GoodsAttrVO> goodsAttrVOList = new ArrayList<>();
        List<GoodsAttr> list = goodsAttrDAO.findByGoodsIdAndEnableFlagOrderBySort(goods.getId(),EnableFlag.Y);
        if(list == null || list.size() <= 0){
            goodsVO.setGoodsAttrVOList(goodsAttrVOList);
            return goodsVO;
        }
        //遍历商品属性关系
        for(GoodsAttr goodsAttr:list){
            GoodsAttrVO goodsAttrVO = new GoodsAttrVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(goodsAttr,goodsAttrVO);
            //将相关属性的values查询出来，并将其放在商品属性关系中
            List<AttributeValueVO> listVO = new LinkedList<>();
            Iterable<AttributeValue> values = attributeValueDAO.findByAttributeNameIdAndEnableFlagOrderBySort(goodsAttrVO.getAttrId(),EnableFlag.Y);
            Iterator<AttributeValue> it = values.iterator();
            while(it.hasNext()){
                AttributeValue attributeValue = it.next();
                AttributeValueVO attributeValueVO = new AttributeValueVO();
                BeanUtil.copyPropertiesIgnoreNullFilds(attributeValue,attributeValueVO);
                listVO.add(attributeValueVO);
            }
            goodsAttrVO.setAttributeValueVOS(listVO);
            goodsAttrVOList.add(goodsAttrVO);

        }
        goodsVO.setGoodsAttrVOList(goodsAttrVOList);
        return goodsVO;
    }

    /**
     * 分页查询商品信息，可以多条件查询
     * @param goodsVO
     * @param pageRequest
     * @return
     */
    @Override
    public PageDTO<GoodsVO> searchGoodsPage(GoodsVO goodsVO, PageRequest pageRequest) {
        Goods objects = goodsDAO.findGoodsInfo(201975988715584L);

        Page<Goods> goodsPage = goodsDAO.findAll(getInputCondition(goodsVO),pageRequest);
        PageDTO<GoodsVO> pageDTO = new PageDTO<>();
        List<GoodsVO> list = new ArrayList<>();
        for(Goods goods:goodsPage){
            GoodsVO goodsVO1 = new GoodsVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(goods,goodsVO1);
            List<GoodsAttrVO> goodsAttrVOList = new ArrayList<>();
            List<GoodsAttr> goodsAttrList = goodsAttrDAO.findByGoodsIdAndEnableFlagOrderBySort(goods.getId(),EnableFlag.Y);
            if(goodsAttrList != null && goodsAttrList.size() > 0){
                for(GoodsAttr goodsAttr:goodsAttrList){
                        GoodsAttrVO goodsAttrVO = new GoodsAttrVO();
                        BeanUtil.copyPropertiesIgnoreNullFilds(goodsAttr,goodsAttrVO);
                        goodsAttrVOList.add(goodsAttrVO);
                }
            }
            goodsVO1.setGoodsAttrVOList(goodsAttrVOList);
            list.add(goodsVO1);
        }
        pageDTO.setTotalElements(goodsPage.getTotalElements());
        pageDTO.setTotalPages(goodsPage.getTotalPages());
        pageDTO.setNumber(goodsPage.getNumber() + 1);
        pageDTO.setRows(list);
        return pageDTO;
    }


    /**
     * 多条件查询商品信息
     * 根据多种情况查询
     * 包括like:goodsName,remark
     * @param vo
     * @return
     */
    private Predicate getInputCondition(GoodsVO vo)
    {
        List<BooleanExpression> predicates = new ArrayList<>();
        if(null != vo)
        {
            if(!StringUtils.isEmpty(vo.getGoodsName()))
            {
                predicates.add(QGoods.goods.goodsName.like("%"+vo.getGoodsName()+"%"));
            }
            if(!StringUtils.isEmpty(vo.getRemark()))
            {
                predicates.add(QGoods.goods.remark.like("%" + vo.getRemark() + "%"));
            }

        }
        predicates.add(QGoods.goods.enableFlag.eq(EnableFlag.Y));
        return BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
    }

    /**
     * 批量删除goods
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public String deleteGoodsList(List<Long> ids,String userId){
        //批量删除将要删除的goods下的goodsAttr
        goodsAttrDAO.deleteGoodsAttrByGoodsList(ids,userId);
        //批量删除goods
        int num = goodsDAO.deleteGoodsList(ids,userId);
        return Constants.RETURN_SUCESS;
    }


}
