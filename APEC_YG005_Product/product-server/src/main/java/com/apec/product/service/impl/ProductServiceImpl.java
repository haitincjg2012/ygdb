package com.apec.product.service.impl;

import com.apec.framework.cache.CacheHashService;
import com.apec.framework.cache.CacheService;
import com.apec.framework.common.*;
import com.apec.framework.common.constants.SysBusinessConstants;
import com.apec.framework.common.enums.Enums;
import com.apec.framework.common.enums.MqTag;
import com.apec.framework.common.enums.Realm;
import com.apec.framework.common.enumtype.*;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.common.util.JsonUtil;
import com.apec.framework.dto.UserInfoVO;
import com.apec.framework.elasticsearch.producer.ApecESProducer;
import com.apec.framework.elasticsearch.producer.ESProducerConstants;
import com.apec.framework.elasticsearch.vo.ESGetSingleResponseVO;
import com.apec.framework.elasticsearch.vo.ESPostResponseVO;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.rockmq.client.MQProducerClient;
import com.apec.framework.rockmq.vo.MessageBodyVO;
import com.apec.framework.rockmq.vo.MessageVO;
import com.apec.framework.rockmq.vo.UserPointRecordVO;
import com.apec.framework.springcloud.SpringCloudClient;
import com.apec.product.dao.ProductAttrDAO;
import com.apec.product.dao.ProductImageDAO;
import com.apec.product.dao.ProductInfoDAO;
import com.apec.product.dao.ProductSkuInfoDAO;
import com.apec.product.model.ProductAttr;
import com.apec.product.model.ProductImage;
import com.apec.product.model.ProductInfo;
import com.apec.product.model.ProductSkuInfo;
import com.apec.product.service.ProductService;
import com.apec.product.task.callback.OffSellJobFinishTask;
import com.apec.product.util.ProductAttrCompare;
import com.apec.product.util.SnowFlakeKeyGen;
import com.apec.product.vo.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private SnowFlakeKeyGen idGen;

    @InjectLogger
    private Logger logger;

    @Autowired
    private ApecESProducer apecESProducer;

    @Autowired
    private CacheHashService cacheHashService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private MQProducerClient mqProducerClient;

    @Autowired
    private ProductInfoDAO productInfoDAO;

    @Autowired
    private ProductSkuInfoDAO productSkuInfoDAO;

    @Autowired
    private ProductAttrDAO productAttrDAO;

    @Autowired
    private ProductImageDAO productImageDAO;

    @Autowired
    private SpringCloudClient springCloudClient;

    @Autowired
    private OffSellJobFinishTask offSellJobFinishTask;

    /**
     * 数据字典查询地址url
     */
    @Value("${dic_service_url}")
    private String dicServiceAddr;

    @Value("${dic_order_weight_code}")
    private String orderWeightCode;

    @Override
    public String testESInfo() {

        return Constants.RETURN_SUCESS;
    }

    /**
     * 获得随机的默认图片
     * @return
     */
    private List<ProductImageVO> getRandomProductImage(){
        List<Object[]> list = productImageDAO.findCountByDefTypeAndEnableFlag(ImageDefType.DEFAULT.name(),EnableFlag.Y.name());
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<Long> listIds = new ArrayList<>();
        Random random = new Random();
        String ids; String []idArr; int min=0,max,s;
        for (Object[] obj : list) {
            if (obj[0] != null && obj[1] != null ) {
                ids = String.valueOf(obj[1]);
                idArr = ids.split(",");
                max = idArr.length;
                s = random.nextInt(max)%(max-min+1) + min;
                listIds.add(Long.parseLong(idArr[s]));
            }
        }
        List<ProductImage> listImage = productImageDAO.findByIdInAndEnableFlagOrderBySort(listIds,EnableFlag.Y);
        List<ProductImageVO> listImageVO = new ArrayList<>();
        listImage.forEach(productImage -> {
            ProductImageVO productImageVO = new ProductImageVO();
            productImageVO.setImageUrl(productImage.getImageUrl());
            productImageVO.setSort(productImage.getSort());
            listImageVO.add(productImageVO);
        });
        return listImageVO;
    }
    @Override
    public String addProductAttrByInstead(Long userNo, ProductSkuInfoVO productSkuInfoVO,  List<Map<String,String>> resultMap){
        String userInfoJson = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userNo,RedisHashConstants.HASH_OBJCONTENT_CACHE);
        UserInfoVO userInfo ;
        if(StringUtils.isBlank(userInfoJson)){
            //获取不到数据,记录日志
            logger.warn("[ProductServiceImpl][addProductAttrByInstead]Can't find user hash cache. userNo:{}",userNo);
            return Constants.CUSTOMER_NOT_EXIST_BYUSERNO;
        }
        userInfo = JsonUtil.parseObject(userInfoJson,UserInfoVO.class);
        return addProductAttr(userInfo,productSkuInfoVO,resultMap);
    }
    @Override
    public String addProductAttr(UserInfoVO userInfoVO, ProductSkuInfoVO productSkuInfoVO, List<Map<String,String>> resultMap) {
        //未完善个人资料不能发布供求
        if(StringUtils.isBlank(userInfoVO.getName())) {
            logger.warn("[ProductServiceImpl][addProductAttr] Can't add New Product Attr , Imperfect personal information!");

            return ErrorCodeConst.ERROR_USER_INFO_ISNULL; //完善个人资料
        }
        //参数验证
        if(productSkuInfoVO.getProductAttrs() == null ){
            logger.warn("[ProductServiceImpl][addProductAttr] Can't add New Product Attr , Parameter verification failed!");
            return Constants.COMMON_MISSING_PARAMS;
        }
        Date nowDate = new Date();
        ProductSkuInfo productSkuInfo = new ProductSkuInfo();
        productSkuInfo.setId(idGen.nextId());
        productSkuInfo.setCreateDate(nowDate);
        productSkuInfo.setEnableFlag(EnableFlag.Y);
        productSkuInfo.setMobile(userInfoVO.getMobile());
        productSkuInfo.setUserId(userInfoVO.getUserId());
        productSkuInfo.setUserName(userInfoVO.getName());
        productSkuInfo.setUserType(userInfoVO.getUserType());
        productSkuInfo.setLastUpdateDate(nowDate);
        //排序
        Collections.sort(productSkuInfoVO.getProductAttrs(),new ProductAttrCompare());

        List<ProductAttr> list = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        productSkuInfoVO.getProductAttrs().forEach(productAttrVO -> {
                ProductAttr productAttr = new ProductAttr();
                if(productAttrVO.getAttributeShowLevel() == AttributeShowLevel.FIRST){
                    stringBuilder.append(productAttrVO.getAttrValue()).append(Constants.EMP_STR);
                }
                BeanUtil.copyPropertiesIgnoreNullFilds(productAttrVO,productAttr);
                productAttr.setEnableFlag(EnableFlag.Y);
                productAttr.setCreateDate(nowDate);
                productAttr.setId(idGen.nextId());
                productAttr.setProductSkuInfo(productSkuInfo);
                list.add(productAttr);
        });
        if(stringBuilder.length() <= 0){
            logger.warn("[ProductServiceImpl][addProductAttr] Can't add New Product Attr , SkuName is null!");
            return ErrorCodeConst.ERROR_PRODUCT_CREATEATTR_CONFIG;
        }
        productSkuInfo.setSkuName(stringBuilder.substring(0,stringBuilder.length()-1));
        productSkuInfo.setProductAttrs(list);

        //查询最近的两条条记录
        Map<Long,String> productMap = new LinkedHashMap<>();
        productMap.put(productSkuInfo.getId(),productSkuInfo.getSkuName());
        Map<String,String> resultMapOne = new HashMap<>();
        resultMapOne.put("skuId",String.valueOf(productSkuInfo.getId()));
        resultMapOne.put("skuName",productSkuInfo.getSkuName());
        resultMap.add(resultMapOne);
        try {
            List<Object[]> listObj = productSkuInfoDAO.querySkuNameIdByUserIdAndEnableFlag(userInfoVO.getUserId(),EnableFlag.Y.name());
            if (!CollectionUtils.isEmpty(listObj)) {
                Long objId;
                String objSkuName;
                for (Object[] obj : listObj) {
                    if (obj[0] != null && obj[1] != null ) {
                        objId = Long.valueOf(String.valueOf(obj[0]));
                        objSkuName = String.valueOf(obj[1]);
                        productMap.put(objId, objSkuName);
                    }
                }
            }
        }catch (Exception e){
            //此处只记录日志，不回滚
            logger.warn("[ProductServiceImpl][addProductAttr] Can not record history by userId:{}",userInfoVO.getUserId());
            logger.error("[ProductServiceImpl][addProductAttr] error!",e);
        }
        String productHis = JsonUtil.toJSONString(productMap);
        cacheHashService.hset(RedisHashConstants.HASH_USER_PREFIX + userInfoVO.getUserId(),RedisHashConstants.HASH_USER_CREATEPRODUCT_HIS, productHis);
        productSkuInfoDAO.save(productSkuInfo);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String addNewProductByInstead(Long userNo,ProductInfoVO productInfoVO){
        String userInfoJson = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userNo,RedisHashConstants.HASH_OBJCONTENT_CACHE);
        UserInfoVO userInfo ;
        if(StringUtils.isBlank(userInfoJson)){
            //获取不到数据,记录日志
            logger.warn("[ProductServiceImpl][addNewProductByInstead]Can't find user hash cache. userNo:{}",userNo);
            return Constants.CUSTOMER_NOT_EXIST_BYUSERNO;
        }
        userInfo = JsonUtil.parseObject(userInfoJson,UserInfoVO.class);
        return addNewProduct(userInfo,productInfoVO);
    }

    @Override
    public String addNewProduct(UserInfoVO userInfoVO, ProductInfoVO productInfoVO) {
        //未完善个人资料不能发布供求
        if(StringUtils.isBlank(userInfoVO.getName())) {
            logger.warn("[ProductServiceImpl][addNewProduct] Can't add New Product , Imperfect personal information!");
            return ErrorCodeConst.ERROR_USER_INFO_ISNULL; //完善个人资料
        }
        //未进行实名认证，只能发布5条供求信息,实名认证后，将该缓存释放
        String createProductNumKey = null ;
        String userInfoRedisKey = RedisHashConstants.HASH_USER_PREFIX + userInfoVO.getUserId();
        if(userInfoVO.getUserStatus() == UserStatus.UNREALAUTH){
            createProductNumKey = SysBusinessConstants.PREFIX_CREATEPRODUCT_NUM;
            String createProductNum =  cacheHashService.hget(userInfoRedisKey,createProductNumKey);
            if(StringUtils.isNotBlank(createProductNum) &&
                                        Integer.parseInt(createProductNum) > SysBusinessConstants.CREATE_PRODUCT_NUM ){
                logger.warn("[ProductServiceImpl][addNewProduct] Can't add New Product , Not rel-name authentication, can " +
                                    "only release five supply and demand information!");
                return ErrorCodeConst.ERROR_PRODUCT_CREATEPRO_OVER;
            }
        }
        //参数验证
        boolean flag = StringUtils.isBlank(productInfoVO.getSkuName()) || productInfoVO.getSkuId() == null ||
                                               productInfoVO.getProductType() == null || productInfoVO.getWeight() == null ;
        if(flag){
            logger.warn("[ProductServiceImpl][addNewProduct] Can't add New Product , Parameter verification failed!");
            return Constants.COMMON_MISSING_PARAMS;
        }
        productInfoVO.setId(idGen.nextId());
        productInfoVO.setUserId(userInfoVO.getUserId());
        productInfoVO.setCreateDate(new Date());
        productInfoVO.setEnableFlag(EnableFlag.Y);
        productInfoVO.setMobile(userInfoVO.getMobile());
        productInfoVO.setUserName(userInfoVO.getName());
        productInfoVO.setShowUserName(userInfoVO.getName()); //先生/
        productInfoVO.setUserType(userInfoVO.getUserType());
        //ES ProductInfoVO
        ESProductInfoVO esProductInfoVO = new ESProductInfoVO();
        BeanUtil.copyPropertiesIgnoreNullFilds(productInfoVO,esProductInfoVO);
        esProductInfoVO.setProductTypeName(productInfoVO.getProductType().getKey());
        esProductInfoVO.setSkuName(productInfoVO.getSkuName());
        esProductInfoVO.setUserTypeName(productInfoVO.getUserType().getKey());
        esProductInfoVO.setGoodsName(Constants.DEFAULT_GOODS_NAME);
        esProductInfoVO.setFirstImageUrl(productInfoVO.getFirstImageUrl());
        if(StringUtils.isNotBlank(productInfoVO.getMarketPreFix())) {
            esProductInfoVO.setMarketPreFix(productInfoVO.getMarketPreFix());
        }else{
            esProductInfoVO.setMarketPreFix("");
        }
        esProductInfoVO.setWeightUnit(productInfoVO.getWeightUnit());
        esProductInfoVO.setPriceUnit(productInfoVO.getPriceUnit());

         //查询ProductAttr
        ProductSkuInfo productSkuInfo = new ProductSkuInfo();
        productSkuInfo.setId(productInfoVO.getSkuId());
        List<ProductAttr> listProductAttr = productAttrDAO.findByProductSkuInfoAndEnableFlagOrderBySort(productSkuInfo,EnableFlag.Y);
        if(CollectionUtils.isEmpty(listProductAttr)){
            logger.warn("[ProductServiceImpl][addNewProduct] Can't add New Product , Parameter productSkuInfoId:{}, No attribute set!",productInfoVO.getSkuId());
            return Constants.COMMON_ERROR_PARAMS;
        }
        List<ESProductAttrVO> listAttrVO = new ArrayList<>();
        listProductAttr.forEach(productAttr -> {
            ESProductAttrVO productAttrVO = new ESProductAttrVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(productAttr,productAttrVO);
            listAttrVO.add(productAttrVO);
        });
        esProductInfoVO.setProductAttrs(listAttrVO);
        //验证ProductImage,如果用户没有上传图片，则选取系统默认的图片
        if(CollectionUtils.isEmpty(productInfoVO.getProductImages())){
            List<ProductImageVO> listImageVO = getRandomProductImage();
            productInfoVO.setProductImages(listImageVO);
            if(listImageVO != null ) {
                List<ESProductImageVO> listESImageVO = new ArrayList<>();
                int flags = 0 ;
                String [] ary;
                for(ProductImageVO productImageVO : listImageVO){
                    if(flags == 0 ) {
                        ary = productImageVO.getImageUrl().split(",");
                        esProductInfoVO.setFirstImageUrl(ary[0]);
                        productInfoVO.setFirstImageUrl(ary[0]);
                        listESImageVO.add(new ESProductImageVO(ary[1], productImageVO.getSort()));
                    }else {
                        listESImageVO.add(new ESProductImageVO(productImageVO.getImageUrl(), productImageVO.getSort()));
                    }
                    flags ++;
                }
                esProductInfoVO.setProductImages(listESImageVO);
            }
        }else {
            List<ESProductImageVO> listESImageVO = new ArrayList<>();
            for(ProductImageVO productImageVO : productInfoVO.getProductImages()){
                listESImageVO.add(new ESProductImageVO(productImageVO.getImageUrl(),productImageVO.getSort()));
            }
            esProductInfoVO.setProductImages(listESImageVO);
        }
        //ES 落地
        ESPostResponseVO esPostResponseVO = apecESProducer.postESInfo(ESProducerConstants.INDEX_URL_PRODUCT, JsonUtil.toJSONString(esProductInfoVO));
        if(StringUtils.isBlank(esPostResponseVO.getId())){
            return  Constants.SYS_ERROR;
        }
        productInfoVO.setElasticId(esPostResponseVO.getId());
        //发送Mq消息进行落地
        mqProducerClient.sendConcurrently(MqTag.PRODUCT_SAVE_NEW.getKey(),String.valueOf(productInfoVO.getId()),productInfoVO);
        //次数加1
        if(createProductNumKey != null){
            String createProductNum =  cacheHashService.hget(userInfoRedisKey,createProductNumKey);
             if(StringUtils.isBlank(createProductNum)){
                 createProductNum = "1";
             }else {
                 createProductNum = String.valueOf( Integer.valueOf(createProductNum) + 1 );
             }
            cacheHashService.hset(userInfoRedisKey,createProductNumKey,createProductNum);
        }
        // 发布供求获取积分
        UserPointRecordVO userPointRecordVO = new UserPointRecordVO();
        userPointRecordVO.setPointRuleType(PointRuleType.SINGLE_ONCE_SEND_REQUEST);//加积分的类型，实名认证成功
        userPointRecordVO.setId(idGen.nextId());
        List<Long> userIds = new ArrayList<>();
        userIds.add(userInfoVO.getUserId());
        userPointRecordVO.setUserIds(userIds);
        mqProducerClient.sendConcurrently(MqTag.USER_POINT_TAG_SUBSCRI_POINT.getKey(),String.valueOf(userPointRecordVO.getId()),userPointRecordVO);
        //Redis落地
        if(StringUtils.isNotBlank(esPostResponseVO.getId())) {
            String productRedis = cacheHashService.hget(userInfoRedisKey, RedisHashConstants.HASH_USER_CREATEPRODUCT_INFO);
            if (StringUtils.isBlank(productRedis)) {
                productRedis = esPostResponseVO.getId();
            } else {
                productRedis = productRedis + "," + esPostResponseVO.getId();
            }
            cacheHashService.hset(userInfoRedisKey, RedisHashConstants.HASH_USER_CREATEPRODUCT_INFO, productRedis);

            String productNewRedisKey = RedisHashConstants.HASH_PRODUCT_PREFIX + esPostResponseVO.getId();
            Map<String,String> map = new HashMap<>();
            map.put("userId",String.valueOf(userInfoVO.getUserId()));
            cacheHashService.hset(productNewRedisKey, RedisHashConstants.HASH_OBJCONTENT_CACHE, JsonUtil.toJSONString(map));
        }
        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional
    public String saveProduct(ProductInfoVO productInfoVO) {
        //参数验证
        boolean flag = StringUtils.isBlank(productInfoVO.getSkuName()) ||   productInfoVO.getProductType() == null ||
                productInfoVO.getWeight() == null || productInfoVO.getId() == null;

        if(flag){
            logger.warn("[ProductServiceImpl][saveProduct] Can't save New Product , Parameter verification failed!");
            return Constants.COMMON_MISSING_PARAMS;
        }
        ProductInfo productInfo = new ProductInfo();
        BeanUtil.copyPropertiesIgnoreNullFilds(productInfoVO,productInfo,"productAttrs","productImages");
        productInfoDAO.save(productInfo);

        ProductSkuInfo productSkuInfo = new ProductSkuInfo();
        productSkuInfo.setId(productInfoVO.getSkuId());
        List<ProductAttr> listProductAttr = productAttrDAO.findByProductSkuInfoAndEnableFlagOrderBySort(productSkuInfo,EnableFlag.Y);
        if(CollectionUtils.isEmpty(listProductAttr)){
            logger.warn("[ProductServiceImpl][saveProduct] Can't save New Product , Parameter productSkuInfoId:{}, No attribute set!",productInfoVO.getSkuId());
            return Constants.COMMON_ERROR_PARAMS;
        }
        for(ProductAttr productAttr :listProductAttr){
            productAttr.setProductInfo(productInfo);
        }
         productAttrDAO.save(listProductAttr);
        //ProductImage
        if(productInfoVO.getProductImages() != null) {
            List<ProductImage> listImage = new ArrayList<>();
            productInfoVO.getProductImages().forEach(productImageVO -> {
                ProductImage productImage = new ProductImage();
                BeanUtil.copyPropertiesIgnoreNullFilds(productImageVO, productImage);
                productImage.setId(idGen.nextId());
                productImage.setCreateDate(productInfoVO.getCreateDate());
                productImage.setEnableFlag(EnableFlag.Y);
                productImage.setProductId(productInfo.getId());
                listImage.add(productImage);
            });
            productImageDAO.save(listImage);
        }
        return Constants.RETURN_SUCESS;
    }

    /**
     * 立即下架 提供给前台
     * @param userInfoVO
     * @param productInfoVO
     * @return
     */
    @Override
    @Transactional
    public String offSellProduct(UserInfoVO userInfoVO, ProductInfoVO productInfoVO) {
        ProductInfo productInfo = productInfoDAO.findFirstByElasticIdAndUserId(productInfoVO.getElasticId(), userInfoVO.getUserId());
        if(productInfo == null) {
            logger.warn("ES ID not found [userId:{}]  [esId:{}]", userInfoVO.getUserId(), productInfoVO.getElasticId());
            return ErrorCodeConst.ERROR_PRODUCT_NOT_FOUND;
        }
        productInfo.setOffsellDate(new Date());
        ESHisProductInfoVO esProductInfoVO = genEsProductInfoVO(productInfo);
        //删除ES产品库 添加到下架库
        offSellProduct(esProductInfoVO);

        //更新数据库状态
        productInfo.setTimeout(0);
        //更新es id
        productInfo.setElasticId(esProductInfoVO.getElasticId());
        productInfoDAO.saveAndFlush(productInfo);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 立即下架
     * @param productInfoVO
     * @return
     */
    @Override
    @Transactional
    public String offSellProductByManager(ProductInfoVO productInfoVO){
        ProductInfo productInfo = productInfoDAO.findFirstByElasticIdAndEnableFlag(productInfoVO.getElasticId(), EnableFlag.Y);
        if(productInfo == null) {
            logger.warn("ES ID not   [esId:{}]",   productInfoVO.getElasticId());
            return ErrorCodeConst.ERROR_PRODUCT_NOT_FOUND;
        }
        productInfo.setOffsellDate(new Date());
        ESHisProductInfoVO esProductInfoVO = genEsProductInfoVO(productInfo);
        //删除ES产品库 添加到下架库
        offSellProduct(esProductInfoVO);

        //更新数据库状态
        productInfo.setTimeout(0);
        //更新es id
        productInfo.setElasticId(esProductInfoVO.getElasticId());
        productInfoDAO.saveAndFlush(productInfo);
        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional
    public String updateProductByES(UserInfoVO userInfoVO, ProductInfoVO productInfoVO) {

        ProductInfo productInfo = productInfoDAO.findFirstByElasticIdAndUserId(productInfoVO.getElasticId(), userInfoVO.getUserId());
        if(productInfo == null) {
            logger.warn("在数据库中没有找到对应ES ID：{}, ES和数据库中的数据不一致", productInfoVO.getElasticId());
            return ErrorCodeConst.ERROR_PRODUCT_NOT_FOUND;
        }
        //同步到ES库
        ESGetSingleResponseVO esGetSingleResponseVO = apecESProducer.getSingleESInfoById(ESProducerConstants.INDEX_URL_PRODUCT, productInfoVO.getElasticId(), ESProductInfoVO.class);
        if(null != esGetSingleResponseVO && esGetSingleResponseVO.getFound()) {
            Map<String,Object> productParam = new HashMap<>();
            if(productInfoVO.getAmount() != null) {
                productParam.put("amount", productInfoVO.getAmount());
            }
            if(productInfoVO.getWeight() != null) {
                productParam.put("weight", productInfoVO.getWeight());
            }
            if(productInfoVO.getStartAmount()!=null && productInfoVO.getEndAmount()!=null){
                productParam.put("startAmount", productInfoVO.getStartAmount());
                productParam.put("endAmount", productInfoVO.getEndAmount());
            }

            boolean result = apecESProducer.postESInfoForUpdateByDoc(ESProducerConstants.INDEX_URL_PRODUCT, productInfoVO.getElasticId(), productParam);
            if(result){
                logger.info("[ProductServiceImpl]#[updateProduct] update weight and amount,startAmount,endAmount {} es id: {} update success", ESProducerConstants.INDEX_URL_PRODUCT, productInfoVO.getElasticId());
            }else {
                logger.warn("[ProductServiceImpl]#[updateProduct] update weight and amount,startAmount,endAmount {} es id: {} update failed", ESProducerConstants.INDEX_URL_PRODUCT, productInfoVO.getElasticId());
            }

            //发送Mq消息进行落地
            productInfoVO.setId(productInfo.getId());
            logger.info("发送异步消息更新DB记录: [Tag:{}] [URL:{}]", MqTag.PRODUCT_UPDATE.getKey(), MqTag.PRODUCT_UPDATE.getTagUrl());
            mqProducerClient.sendConcurrently(MqTag.PRODUCT_UPDATE.getKey(),String.valueOf(productInfoVO.getId()),productInfoVO);

        }else {
            logger.warn("{} ES Not Found Id :{}", ESProducerConstants.INDEX_URL_PRODUCT, productInfoVO.getElasticId());
        }

        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional
    public String updateProduct(ProductInfoVO productInfoVO) {
        ProductInfo productInfo = productInfoDAO.findOne(productInfoVO.getId());
        if(productInfoVO.getWeight() != null) {
            productInfo.setWeight(productInfoVO.getWeight());
        }
        if(productInfoVO.getAmount() != null){
            productInfo.setAmount(productInfoVO.getAmount());
        }
        if(productInfoVO.getStartAmount() != null && productInfoVO.getEndAmount() != null){
            productInfo.setStartAmount(productInfoVO.getStartAmount());
            productInfo.setEndAmount(productInfoVO.getEndAmount());
        }
        productInfoDAO.saveAndFlush(productInfo);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String pushProductToEs(String indexUrl) {
        List<ProductInfo> productInfos = productInfoDAO.findByEnableFlagAndTimeoutGreaterThan(EnableFlag.Y, 0);
        return pushEsInfo(indexUrl, productInfos);
    }

    @Override
    public String pushOffSellProductToEs(String indexUrl) {
        List<ProductInfo> productInfoPages = productInfoDAO.findByOffsellProduct();
        return pushEsInfo(indexUrl, productInfoPages);
    }

    /**
     * TODO 定时任务要在下架之后才能跑 或者要有下架时间
     * @param indexUrl
     * @param productInfoPages
     * @return
     */
    private String pushEsInfo(String indexUrl, List<ProductInfo> productInfoPages) {
        //index 开头加斜杠
        if(!indexUrl.startsWith(Constants.SINGLE_SLASH)) {
            indexUrl = Constants.SINGLE_SLASH + indexUrl;
        }
        //index 末尾加斜杠
        if(!indexUrl.endsWith(Constants.SINGLE_SLASH)) {
            indexUrl += Constants.SINGLE_SLASH;
        }

        String newIndex;
        ESProductInfoVO esProductInfo;
        ESHisProductInfoVO esHisProductInfoVO;
        for (ProductInfo productInfoPage : productInfoPages) {
            esHisProductInfoVO = genEsProductInfoVO(productInfoPage);
            //落地ES
            esProductInfo = new ESProductInfoVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(esHisProductInfoVO, esProductInfo);
            //es id还是用原来的 所以index需要加上es id
            newIndex = indexUrl + productInfoPage.getElasticId();
            ESPostResponseVO esPostResponseVO = apecESProducer.postESInfo(newIndex, JsonUtil.toJSONString(esProductInfo));
        }
        return Constants.RETURN_SUCESS;
    }


    @Override
    @Async
    public void offSellProductJob() {
        Long startTime = System.currentTimeMillis();
        ResultData<String> jobResult = new ResultData<>();
        jobResult.setErrorCode(Constants.RETURN_SUCESS);
        try {
            //获取收藏总量
            String save_all_num_redis = cacheService.get(RedisConstants.PRO_ALL_SAVE_NUM);
            long saveNumTotal = 1L; //如果没有总量则默认为1
            if(StringUtils.isNotBlank(save_all_num_redis)){
                saveNumTotal = Long.valueOf(save_all_num_redis);
            }
            //获取浏览总量
            String view_all_num_redis = cacheService.get(RedisConstants.PRO_ALL_VIEW_NUM);
            long viewNumTotal = 1L; //如果没有总量则默认为1
            if(StringUtils.isNotBlank(view_all_num_redis)){
                viewNumTotal = Long.valueOf(view_all_num_redis);
            }
            //获取推荐列表
            String productRecommend = cacheService.get(RedisConstants.PRO_RECOMMEND);
            productRecommend = StringUtils.isBlank(productRecommend) ? "" : productRecommend;
            long productRecommendCount = productRecommend.split(",").length;

            //获取timeout 总量
            List<Object> timeoutList = productInfoDAO.countTimeOut();
            long timeOutTotal = 1; //如果没有总量则默认为1
            if(timeoutList.size() != 0){
                BigDecimal timeOutDecimal = (BigDecimal) timeoutList.get(0);
                if(timeOutDecimal != null) {
                    timeOutTotal = timeOutDecimal.longValue();
                }
            }

            //获取权重数据字典信息
            Map<String, String> requestMap = new HashMap<>();
            requestMap.put("code",orderWeightCode);
            String responseStr = springCloudClient.post(dicServiceAddr, JsonUtil.toJSONString(requestMap));
            ResultData resultData = JsonUtil.parseObject(responseStr, ResultData.class);
            Map<OrderWeightType,Integer> orderWeightMap = new HashMap<>();
            if(resultData.isSucceed()){
                List dicList = (List) resultData.getData();
                dicList.forEach(dic->{
                    Map dicMap = (Map)dic;
                    OrderWeightType orderWeightType = Enums.getEnumByNameOrNull(OrderWeightType.class, dicMap.get("keyword").toString());
                    if(orderWeightType != null) {
                        orderWeightMap.put(orderWeightType, Integer.valueOf(String.valueOf(dicMap.get("value"))));
                    }
                });
            }

            //批量处理下架和权重计算
            int page = 0;
            int size = 100;
            while(true) {
                PageRequest pageRequest = new PageRequest(page, size);
                Page<ProductInfo> productInfoPages = productInfoDAO.findAll(pageRequest);
                batchOffSellJob(saveNumTotal, viewNumTotal, productRecommend, productRecommendCount, timeOutTotal, orderWeightMap, productInfoPages);
                logger.info("======================总记录数:[{}]  已经处理记录数:[{}]===============================", productInfoPages.getTotalElements() , (page+1)*size);
                //循环每次取100条记录 如果页大小等于总页数时 退出循环
                if(page >= productInfoPages.getTotalPages()-1){
                    logger.info("=================================循环结束====================================");
                    break;
                }
                page ++;
            }

        }catch (Throwable e){
            jobResult.setErrorCode(Constants.SYS_ERROR);
            jobResult.setErrorMsg(e.getMessage());
            AsyncResult<ResultData> result = new AsyncResult<>(jobResult);
            //新增回调方法处理job执行结果
            result.addCallback(offSellJobFinishTask);
            throw e;//抛出异常保持事务一致性
        }
        logger.info("======================off sell job execute success============================");
        logger.info("off sell job execute total time :{}", System.currentTimeMillis() - startTime);
    }

    private void batchOffSellJob(long saveNumTotal, long viewNumTotal, String productRecommend, long productRecommendCount, long timeOutTotal, Map<OrderWeightType, Integer> orderWeightMap, Page<ProductInfo> productInfoPages) {
        boolean isOffSell;
        int oldTimeOut;
        int timeOut;
        ESHisProductInfoVO esHisProductInfoVO;
        for (ProductInfo productInfo: productInfoPages) {
            isOffSell = false;
            oldTimeOut = productInfo.getTimeout();
            //表示供求已经被下架
            if(oldTimeOut == 0){
                isOffSell = true;
            }
            //把供求信息中的time out减去1 如果数量为0时 则不减
            timeOut = oldTimeOut-1<=0 ? 0: oldTimeOut-1; //如果为0则表示已下架
            productInfo.setTimeout(timeOut);
            esHisProductInfoVO = genEsProductInfoVO(productInfo);
            //如果供求已经下架则不执行下架和权重计算
            if(isOffSell){
                continue;
            }
            //如果是当天下架的供求 推送到ES中
            if(timeOut == 0) {
                logger.info("=============off sell product [esId:{}] [userId:{}]  start==================", esHisProductInfoVO.getElasticId(), esHisProductInfoVO.getUserId());
                //设置下架时间
                productInfo.setOffsellDate(new Date());
                //设置下架时间
                esHisProductInfoVO.setOffsellDate(productInfo.getOffsellDate());
                offSellProduct(esHisProductInfoVO);
                //更新es id
                productInfo.setElasticId(esHisProductInfoVO.getElasticId());
                logger.info("=============off sell product [esId:{}] [userId:{}]  end==================", esHisProductInfoVO.getElasticId(), esHisProductInfoVO.getUserId());
            }else {//没有下架的供求需要修改ES的权重值
                logger.info("=============count product order weight [esId:{}] [userId:{}]  start==================", esHisProductInfoVO.getElasticId(), esHisProductInfoVO.getUserId());
                orderWeightCount(oldTimeOut, esHisProductInfoVO, saveNumTotal, viewNumTotal, timeOutTotal, productRecommendCount, productRecommend, orderWeightMap);
                logger.info("=============count product order weight [esId:{}] [userId:{}]  end==================", esHisProductInfoVO.getElasticId(), esHisProductInfoVO.getUserId());
            }

        }
        //保存所有的供求信息
        productInfoDAO.save(productInfoPages);
    }

    /**
     * 计算权重
     * @param timeOut
     * @param esHisProductInfoVO
     * @param saveNumTotal
     * @param viewNumTotal
     * @param timeOutTotal
     * @param recommendCount
     * @param productRecommendRedis
     * @param orderWeightMap
     */
    private void orderWeightCount(int timeOut, ESHisProductInfoVO esHisProductInfoVO, long saveNumTotal, long viewNumTotal,
                                  long timeOutTotal, long recommendCount, String productRecommendRedis,
                                  Map<OrderWeightType,Integer> orderWeightMap) {
        int scale = 2;
        String esId = esHisProductInfoVO.getElasticId();
        String productRedisKey = RedisHashConstants.HASH_PRODUCT_PREFIX + esId;
        //计算ES权重值 总权重100 单个权重计算方式:  份额/总量*权重值
        //下架时间权重 20
        Integer timeOutWeight = orderWeightMap.get(OrderWeightType.TIME_OUT);
        if(timeOutWeight == null) {
            timeOutWeight = 20;
        }
        float offSell = new BigDecimal(timeOut).divide(new BigDecimal(timeOutTotal), scale, BigDecimal.ROUND_HALF_UP).floatValue() * timeOutWeight;
        //浏览量权重 20
        Integer viewNumWeight = orderWeightMap.get(OrderWeightType.VIEW_NUM);
        if(viewNumWeight == null) {
            viewNumWeight = 20;
        }
        String productViewNumRedis = cacheHashService.hget(productRedisKey, RedisHashConstants.HASH_PRO_VIEW_NUM);
        productViewNumRedis = StringUtils.isBlank(productViewNumRedis) ? "0" : productViewNumRedis;//如果redis取不到值则为0
        float viewNum = new BigDecimal(productViewNumRedis).divide(new BigDecimal(viewNumTotal), scale, BigDecimal.ROUND_HALF_UP).floatValue() * viewNumWeight;
        //收藏量权重 20
        Integer saveNumWeight = orderWeightMap.get(OrderWeightType.SAVE_NUM);
        if(saveNumWeight == null) {
            saveNumWeight = 20;
        }
        String productSaveNumRedis = cacheHashService.hget(productRedisKey, RedisHashConstants.HASH_PRO_SAVE_NUM);
        productSaveNumRedis = StringUtils.isBlank(productSaveNumRedis) ? "0" : productSaveNumRedis; //如果redis取不到值则为0
        float saveNum = new BigDecimal(productSaveNumRedis).divide(new BigDecimal(saveNumTotal), scale, BigDecimal.ROUND_HALF_UP).floatValue() * saveNumWeight;
        //是否推荐权重 40 没有推荐则为0
        Integer recommendWeight = orderWeightMap.get(OrderWeightType.RRECOMMEND);
        if(recommendWeight == null) {
            recommendWeight = 40;
        }
        int productRecommend = 0;
        if(productRecommendRedis.contains(esId)){
            productRecommend = 1;
        }
        float recommend = (productRecommend/recommendCount) * recommendWeight;
        //计算权重
        float orderWeight = offSell + viewNum + saveNum + recommend;
        //更新权重
        ESGetSingleResponseVO esGetSingleResponseVO = apecESProducer.getSingleESInfoById(ESProducerConstants.INDEX_URL_PRODUCT, esId, ESProductInfoVO.class);
        if(null != esGetSingleResponseVO && esGetSingleResponseVO.getFound()) {
            Map<String,String> orderWeightParam = new HashMap<>();
            orderWeightParam.put("orderWeight", String.valueOf(orderWeight));
            orderWeightParam.put("timeout", String.valueOf(esHisProductInfoVO.getTimeout()));
            boolean result = apecESProducer.postESInfoForUpdateByDoc(ESProducerConstants.INDEX_URL_PRODUCT, esId, orderWeightParam);
            if(result){
                logger.info("{} es id: {} update success", ESProducerConstants.INDEX_URL_PRODUCT, esId);
            }else {
                logger.warn("{} es id: {} update failed", ESProducerConstants.INDEX_URL_PRODUCT, esId);
            }
        }



    }

    private ESHisProductInfoVO genEsProductInfoVO(ProductInfo productInfo) {
        //ES ProductInfoVO
        ESHisProductInfoVO esHisProductInfoVO = new ESHisProductInfoVO();
        BeanUtil.copyPropertiesIgnoreNullFilds(productInfo,esHisProductInfoVO);

        esHisProductInfoVO.setProductTypeName(productInfo.getProductType().getKey());
        String marketPreFix = StringUtils.isBlank(productInfo.getMarketPreFix()) ? "" : productInfo.getMarketPreFix();
        esHisProductInfoVO.setSkuName(marketPreFix + productInfo.getSkuName());
        esHisProductInfoVO.setUserTypeName(productInfo.getUserType().getKey());
        esHisProductInfoVO.setGoodsName(Constants.DEFAULT_GOODS_NAME);
        esHisProductInfoVO.setFirstImageUrl(productInfo.getFirstImageUrl());
        if(StringUtils.isNotBlank(productInfo.getMarketPreFix())) {
            esHisProductInfoVO.setMarketPreFix(productInfo.getMarketPreFix());
        }else{
            esHisProductInfoVO.setMarketPreFix("");
        }
        esHisProductInfoVO.setWeightUnit(productInfo.getWeightUnit());
        esHisProductInfoVO.setPriceUnit(productInfo.getPriceUnit());

        //查询ProductAttr
        ProductSkuInfo productSkuInfo = new ProductSkuInfo();
        productSkuInfo.setId(productInfo.getSkuId());
        List<ProductAttr> listProductAttr = productAttrDAO.findByProductSkuInfoAndEnableFlagOrderBySort(productSkuInfo, EnableFlag.Y);
        List<ESProductAttrVO> listAttrVO = new ArrayList<>();
        listProductAttr.forEach(productAttr -> {
            ESProductAttrVO productAttrVO = new ESProductAttrVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(productAttr,productAttrVO);
            listAttrVO.add(productAttrVO);
        });
        esHisProductInfoVO.setProductAttrs(listAttrVO);

        //获取图片信息
        List<ESProductImageVO> esProductImageVOList = new ArrayList<>();
        List<ProductImage> list =  productImageDAO.findByProductIdAndEnableFlagOrderBySort(productInfo.getId(),EnableFlag.Y);
        if(!CollectionUtils.isEmpty(list)) {
            list.forEach(productImage -> {
                ESProductImageVO productImageVO = new ESProductImageVO();
                BeanUtil.copyPropertiesIgnoreNullFilds(productImage, productImageVO);
                esProductImageVOList.add(productImageVO);
            });
        }
        esHisProductInfoVO.setProductImages(esProductImageVOList);

        return esHisProductInfoVO;
    }

    /**
     * 发送 供求下架的站内信
     * @param sukName
     * @param userId
     */
    private void sendMessageMqInfo(String sukName, Long userId){
        MessageBodyVO messageBodyVO = new MessageBodyVO();
        messageBodyVO.setType(MessageType.NOTIFICATION);//系统通知
        messageBodyVO.setTemplateFlag(true);//使用动态模板发送
        Map<String, String> contentMap = new HashMap<>();
        contentMap.put("sukName",sukName);
        messageBodyVO.setTemplateKey(MessageTemplate.PRODUCT_OFF_SELL_TEMPLATE);
        messageBodyVO.setRealm(Realm.MESSAGE);
        messageBodyVO.setContentMap(contentMap);
        //发送站内信，通知用户积分变动消息
        MessageVO messageVO = new MessageVO();
        messageVO.setBody(messageBodyVO);
        messageVO.setId(idGen.nextId());
        List<Long> receivers = new ArrayList<>();
        receivers.add(userId);
        messageVO.setReceivers(receivers);//接收者
        messageVO.setMessageStatus(MessageStatus.NEW);//用户状态
        mqProducerClient.sendConcurrently(MqTag.MESSAGE_TAG.getKey(),String.valueOf(messageVO.getId()),messageVO);
    }

    private void offSellProduct(ESHisProductInfoVO esProductInfoVO) {
        //落地ES
        ESProductInfoVO esProductInfo = new ESProductInfoVO();
        BeanUtil.copyPropertiesIgnoreNullFilds(esProductInfoVO, esProductInfo);
        ESPostResponseVO esPostResponseVO = apecESProducer.postESInfo(ESProducerConstants.INDEX_URL_OFF_SELL, JsonUtil.toJSONString(esProductInfo));
        boolean delResult = apecESProducer.deleteESInfo(ESProducerConstants.INDEX_URL_PRODUCT, esProductInfoVO.getElasticId());
        if(delResult) {
            logger.info("{} delete es id {} success", ESProducerConstants.INDEX_URL_PRODUCT, esProductInfoVO.getElasticId());
        }else {
            logger.warn("{} delete es id {} failed", ESProducerConstants.INDEX_URL_PRODUCT, esProductInfoVO.getElasticId());
        }
        //redis中新增下架的供求
        //Redis落地
        if(StringUtils.isNotBlank(esPostResponseVO.getId())) {
            logger.info("{} add es id {} success", ESProducerConstants.INDEX_URL_OFF_SELL, esPostResponseVO.getId());
            String productRedisKey = RedisHashConstants.HASH_USER_PREFIX + esProductInfoVO.getUserId();
            String offSellProductRedis = cacheHashService.hget(productRedisKey, RedisHashConstants.HASH_PRODUCT_OFF_SELL_PREFIX);
            if (StringUtils.isBlank(offSellProductRedis)) {
                offSellProductRedis = esPostResponseVO.getId();
            } else {
                offSellProductRedis = offSellProductRedis + "," + esPostResponseVO.getId();
            }
            logger.info("=============set redis [parent:{}] [key:{}] [value:{}]  start ===============", productRedisKey, RedisHashConstants.HASH_PRODUCT_OFF_SELL_PREFIX, esPostResponseVO.getId());
            cacheHashService.hset(productRedisKey, RedisHashConstants.HASH_PRODUCT_OFF_SELL_PREFIX, offSellProductRedis);
            logger.info("=============set redis [parent:{}] [key:{}] [value:{}]  end ===============", productRedisKey, RedisHashConstants.HASH_PRODUCT_OFF_SELL_PREFIX, esPostResponseVO.getId());
            //redis中删除已经下架的供求
            String productRedis = cacheHashService.hget(productRedisKey, RedisHashConstants.HASH_USER_CREATEPRODUCT_INFO);
            if(StringUtils.isNotBlank(productRedis)) {
                if(productRedis.contains(esProductInfoVO.getElasticId())){
                    String[] oldProducts = productRedis.split(",");
                    String[] newProducts = ArrayUtils.removeElement(oldProducts, esProductInfoVO.getElasticId());
                    String products = StringUtils.join(newProducts, ",");
                    //重新保存
                    logger.info("=============delete redis [parent:{}] [key:{}] [value:{}]  start ===============", productRedisKey, RedisHashConstants.HASH_USER_CREATEPRODUCT_INFO, esProductInfoVO.getElasticId());
                    cacheHashService.hset(productRedisKey, RedisHashConstants.HASH_USER_CREATEPRODUCT_INFO, products);
                    logger.info("=============delete redis [parent:{}] [key:{}] [value:{}]  end ===============", productRedisKey, RedisHashConstants.HASH_USER_CREATEPRODUCT_INFO, esProductInfoVO.getElasticId());

                }
            }

            //获取product节点的key 删除节点
            productRedisKey = RedisHashConstants.HASH_PRODUCT_PREFIX + esProductInfoVO.getElasticId();
            logger.info("=============delete redis [parent:{}] [key:{}] [value:{}]  start ===============", productRedisKey, RedisHashConstants.HASH_PRODUCT_PREFIX, esProductInfoVO.getElasticId());
            cacheHashService.del(productRedisKey);
            logger.info("=============delete redis [parent:{}] [key:{}] [value:{}]  start ===============", productRedisKey, RedisHashConstants.HASH_PRODUCT_PREFIX, esProductInfoVO.getElasticId());


            //发站内信 供求已经下架
            sendMessageMqInfo(esProductInfoVO.getSkuName(), esProductInfoVO.getUserId());
            //设置新的ES id
            esProductInfoVO.setElasticId(esPostResponseVO.getId());
        }else {
            logger.info("{} add es id {} failed", ESProducerConstants.INDEX_URL_OFF_SELL, esPostResponseVO.getId());
        }
    }
}
