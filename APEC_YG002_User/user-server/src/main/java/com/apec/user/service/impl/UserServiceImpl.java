package com.apec.user.service.impl;

import com.apec.framework.cache.CacheHashService;
import com.apec.framework.cache.CacheService;
import com.apec.framework.common.*;
import com.apec.framework.common.constants.LoginConstants;
import com.apec.framework.common.constants.SysBusinessConstants;
import com.apec.framework.common.enums.MqTag;
import com.apec.framework.common.enums.Realm;
import com.apec.framework.common.enums.Source;
import com.apec.framework.common.enumtype.*;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.common.util.JsonUtil;
import com.apec.framework.common.util.SecurityUtils;
import com.apec.framework.dto.*;
import com.apec.framework.elasticsearch.producer.ApecESProducer;
import com.apec.framework.elasticsearch.producer.ESProducerConstants;
import com.apec.framework.elasticsearch.vo.ESGetSingleResponseVO;
import com.apec.framework.elasticsearch.vo.ESPostResponseVO;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.rockmq.client.MQProducerClient;
import com.apec.framework.rockmq.vo.MessageBodyVO;
import com.apec.framework.rockmq.vo.MessageVO;
import com.apec.framework.springcloud.SpringCloudClient;
import com.apec.user.dao.*;
import com.apec.user.dto.UserDTO;
import com.apec.user.model.*;
import com.apec.user.service.UserService;
import com.apec.user.util.SnowFlakeKeyGen;
import com.apec.user.util.UUIDGenerator;
import com.apec.user.vo.*;
import com.apec.user.vo.UserViewVO;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.util.*;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @InjectLogger
    private Logger logger;

    @Autowired
    private SnowFlakeKeyGen idGen;

    @Autowired
    private UserDAO userDao;

    @Autowired
    private UserTagsDAO userTagsDAO;

    @Autowired
    private UserLoginRecordDAO userLoginRecordDAO;

    @Autowired
    private UserAuthRecordDAO userAuthRecordDAO;

    @Autowired
    private UserOrgImageDAO userOrgImageDAO;

    @Autowired
    private UserOrgClientDAO userOrgClientDAO;

    @Autowired
    private UserPointDAO userPointDAO;

    @Autowired
    private MQProducerClient mqProducerClient;
    //缓存服务
    @Autowired
    private CacheService cacheService;

    //缓存服务
    @Autowired
    private CacheHashService cacheHashService;

    @Autowired
    private ApecESProducer apecESProducer;

    //查询地址url
    @Value("${region_requestAddress_url}")
    private String requestAddress;

    /**
     * 更新用户缓存
     * @param userId 用户ID
     * @param userPoint 用户积分
     * @param user 用户对象
     */
    private void updateUserCache(String userId,UserPoint userPoint,User user){
        //缓存更新异常,返回异常,数据回滚,Mq消息补偿机制重跑.
        String json = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.HASH_USER_CREATEUSERVIEW_INFO);
        com.apec.framework.dto.UserViewVO userViewVO ;
        if(StringUtils.isBlank(json)){
            //获取不到数据,记录日志
            logger.warn("[UserServiceImpl][updateUserCache]Can't find user hash cache. userId:{}",userId);
            userViewVO = new com.apec.framework.dto.UserViewVO();
        }else{
            userViewVO = JsonUtil.parseObject(json,com.apec.framework.dto.UserViewVO.class);
        }
        if(userPoint != null) {
            userViewVO.setPoint(userPoint.getAvailablePoints());
            userViewVO.setUserLevelKey(userPoint.getUserLevel().getKey());
            userViewVO.setUserLevelName(userPoint.getUserLevel().name());
        }
        if(user != null && user.getUserRealAuth() != null){
            userViewVO.setUserRealAuthKey(user.getUserRealAuth().getKey());
            userViewVO.setUserRealAuthName(user.getUserRealAuth().name());
        }
        cacheHashService.hset(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.HASH_USER_CREATEUSERVIEW_INFO, JsonUtil.toJSONString(userViewVO));
    }

    /**
     * 更新用户基本信息缓存
     * @param userNo 用户ID
     * @param user 用户对象
     */
    private void updateUserInfoCache(String userNo,User user){
        String userInfoJson = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userNo,RedisHashConstants.HASH_OBJCONTENT_CACHE);
        UserInfoVO userInfo ;
        if(StringUtils.isBlank(userInfoJson)){
            //获取不到数据,记录日志
            logger.warn("[UserServiceImpl][updateUserInfoCache]Can't find user hash cache. userNo:{}",userNo);
            userInfo = new UserInfoVO();
        }else{
            userInfo = JsonUtil.parseObject(userInfoJson,UserInfoVO.class);
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(user,userInfo);
        userInfo.setUserTypeKey(user.getUserType() == null?"":user.getUserType().getKey());
        if(StringUtils.isBlank(user.getIdNumber())){
            userInfo.setIdNumber(null);
        }
        if(StringUtils.isBlank(user.getRealName())){
            userInfo.setRealName(null);
        }
        cacheHashService.hset(RedisHashConstants.HASH_USER_PREFIX + userNo,RedisHashConstants.HASH_OBJCONTENT_CACHE, JsonUtil.toJSONString(userInfo));
    }

    /**
     * 更新用户组织缓存
     * @param userOrgClient 用户组织对象
     */
    private void updateUserOrgInfoCache(UserOrgClient userOrgClient){
        if(userOrgClient.getUserAccountType() == null){
            logger.info("[UserServiceImpl][updateUserOrgInfoCache] There is no need to update the cache. ");
            return;
        }
        List<User> listUser = userDao.findByUserOrgIdAndEnableFlag(userOrgClient.getId(),EnableFlag.Y);
        if(CollectionUtils.isEmpty(listUser)){
            logger.warn("[UserServiceImpl][updateUserOrgInfoCache] Can't find user data by userOrgId, userOrgId:{}",userOrgClient.getId());
            return;
        }
        StringBuilder stringBuilder = new StringBuilder("[");
        listUser.forEach(user -> {
            stringBuilder.append("{ 'userAccountType': '");
            stringBuilder.append(user.getUserAccountType());
            stringBuilder.append("','userId':'");
            stringBuilder.append(user.getId());
            stringBuilder.append("'},");
        });
        String jsonStr = stringBuilder.substring(0,stringBuilder.length()-1) + "]";
        cacheHashService.hset(RedisHashConstants.HASH_USER_ORG_PREFIX + userOrgClient.getId(),RedisHashConstants.HASH_ORG_USERLIST, jsonStr);
        cacheHashService.hset(RedisHashConstants.HASH_USER_ORG_PREFIX + userOrgClient.getId(),RedisHashConstants.HASH_ORG_ACCOUNTTYPE,
                userOrgClient.getUserAccountType().name());

    }

    /**
     *  推送ES 数据落地
     * @param user  用户 （个体账号。组织账户主账号
     * @param userOrgClient  组织账户
     * @return String 返回码
     */
    private String pushESInfo(User user,UserOrgClient userOrgClient, String index){
        //非空判断
        if(user == null) {return ErrorCodeConst.USER_NOTNULL;}
        if(userOrgClient == null)  {return  ErrorCodeConst.ERRPR_ORG_ISNULL;}
        if(!userOrgClient.isPushFlag() || user.getUserType() == null || userOrgClient.getUserAccountType() == null)
        {return  Constants.COMMON_MISSING_PARAMS;}
        ESOrgInfoVO esOrgInfoVO = new ESOrgInfoVO();
        BeanUtil.copyPropertiesIgnoreNullFilds(userOrgClient,esOrgInfoVO,"orgTags","orgId");
        esOrgInfoVO.setOrgId(userOrgClient.getId());
        esOrgInfoVO.setCreateDate(new Date());
        //根据UerOrgClient查找对应的组织标签
        List<UserTags> listUserTags = userTagsDAO.findByUserOrgIdAndEnableFlagOrderBySort(userOrgClient.getId(),EnableFlag.Y);
        List<ESTagsInfoVO> listEsTagsInfo = new ArrayList<>();
        if(!CollectionUtils.isEmpty(listUserTags)){
            listUserTags.forEach(userTags -> {
                ESTagsInfoVO esTagsInfoVO = new ESTagsInfoVO();
                BeanUtil.copyPropertiesIgnoreNullFilds(userTags,esTagsInfoVO );
                listEsTagsInfo.add(esTagsInfoVO);
            });
        }
        esOrgInfoVO.setOrgTags(listEsTagsInfo);
        //ES 落地
        ESPostResponseVO esPostResponseVO = new ESPostResponseVO();
        String postJson = null;
        switch (user.getUserType()){
            case LK:
                ESDepotOrgInfoVO esDepotOrgInfoVO = new ESDepotOrgInfoVO(esOrgInfoVO,userOrgClient.getOrgStockCap()
                        ,userOrgClient.getMainOperating());
                postJson = JsonUtil.toJSONString(esDepotOrgInfoVO);
                index = StringUtils.isBlank(index) ? ESProducerConstants.INDEX_URL_DEPORT_ORG : index + userOrgClient.getElasticId();
                break;
            case DB:
                ESAgencyInfoVO esAgencyInfoVO = new ESAgencyInfoVO(esOrgInfoVO,userOrgClient.getSaleAddress(),userOrgClient.getMainOperating(),user.getId());
                postJson = JsonUtil.toJSONString(esAgencyInfoVO);
                index = StringUtils.isBlank(index) ? ESProducerConstants.INDEX_URL_DAIBAN_ORG : index + userOrgClient.getElasticId();
                break;
            case KS:
                ESMerchantInfoVO esMerchantInfoVO = new ESMerchantInfoVO(esOrgInfoVO,userOrgClient.getMainOperating(),user.getId());
                postJson = JsonUtil.toJSONString(esMerchantInfoVO);
                index = StringUtils.isBlank(index) ? ESProducerConstants.INDEX_URL_KESHAN_ORG : index + userOrgClient.getElasticId();
                break;
            default:
                return Constants.RETURN_SUCESS;
        }
        if(StringUtils.isNotEmpty(postJson)) {
            esPostResponseVO = apecESProducer.postESInfo(index, postJson);//发送请求
        }
        if(StringUtils.isBlank(esPostResponseVO.getId())) {
            logger.error("[UserServiceImpl][PushESInfo] Can't get EsResponse ID !");
            throw new BusinessException(Constants.SYS_ERROR);
        }
        userOrgClient.setElasticId(esPostResponseVO.getId());
        return Constants.RETURN_SUCESS;
    }

    /**
     * 代办
     * @param indexUrl
     * @return
     */
    @Override
    public String pushAgencyEsInfoByIndex(String indexUrl){
        return this.pushEsInfo(indexUrl, UserType.DB);
    }

    /**
     * 客商
     * @param indexUrl
     * @return
     */
    @Override
    public String pushMerchantEsInfoByIndex(String indexUrl){
        return this.pushEsInfo(indexUrl, UserType.KS);
    }

    /**
     * 冷库
     * @param indexUrl
     * @return
     */
    @Override
    public String pushDepotEsInfoByIndex(String indexUrl) {
        return this.pushEsInfo(indexUrl, UserType.LK);
    }


    public String pushEsInfo(String indexUrl, UserType userType) {
        //index 开头加斜杠
        if (!indexUrl.startsWith(Constants.SINGLE_SLASH)) {
            indexUrl = Constants.SINGLE_SLASH + indexUrl;
        }
        //index 末尾加斜杠
        if (!indexUrl.endsWith(Constants.SINGLE_SLASH)) {
            indexUrl += Constants.SINGLE_SLASH;
        }
        //获取所有已推送的组织信息
        List<UserOrgClient> orgList = userOrgClientDAO.findByPushFlagAndEnableFlag(true, EnableFlag.Y);
        if(CollectionUtils.isEmpty(orgList)){
            return Constants.RETURN_SUCESS;
        }
        for (UserOrgClient userOrgClient : orgList) {
            List<User> userList = userDao.findByUserOrgIdAndEnableFlagAndUserAccountTypeNot(userOrgClient.getId(), EnableFlag.Y,UserAccountType.ORG_CHILD_ACCOUNT);
            //遍历用户信息 并且推送到ES
            if(!CollectionUtils.isEmpty(userList)){
                for (User user : userList) {
                    if(user.getUserType().compareTo(userType) == 0){ //判断客户类型是否一致
                        this.pushESInfo(user, userOrgClient, indexUrl);
                    }

                }
            }
        }
        return Constants.RETURN_SUCESS;
    }

    /**
     * 设置 ES 修改的参数
     * @param esOrgInfoVO ES组织账户VO
     * @param userOrgClient 用户组织账户
     * @param param 参数
     */
    private void setESUpdateParam(ESOrgInfoVO esOrgInfoVO, UserOrgClient userOrgClient ,Map<String,Object> param){
        if(StringUtils.isBlank(userOrgClient.getOrgName()) && !StringUtils.equals(userOrgClient.getOrgName(),
                esOrgInfoVO.getOrgName())) {
            param.put("orgName", userOrgClient.getMainOperating());
        }
        if(StringUtils.isBlank(userOrgClient.getOrgFirstBannerUrl()) && !StringUtils.equals(userOrgClient.getOrgFirstBannerUrl(),
                esOrgInfoVO.getOrgFirstBannerUrl())) {
            param.put("orgFirstBannerUrl", userOrgClient.getOrgFirstBannerUrl());
        }
        if(StringUtils.isBlank(userOrgClient.getAddress()) && !StringUtils.equals(userOrgClient.getAddress(),
                esOrgInfoVO.getAddress())) {
            param.put("address", userOrgClient.getAddress());
        }
        if(userOrgClient.getOrderWeight() != null &&  userOrgClient.getOrderWeight().compareTo(esOrgInfoVO.getOrderWeight())!= 0 ) {
            param.put("orderWeight", userOrgClient.getOrderWeight());
        }

        List<UserTags> listUserTags = userTagsDAO.findByUserOrgIdAndEnableFlagOrderBySort(userOrgClient.getId(),EnableFlag.Y);
        List<ESTagsInfoVO> listEsTagsInfo = new ArrayList<>();
        boolean flag =  CollectionUtils.isEmpty(esOrgInfoVO.getOrgTags());
        if(!CollectionUtils.isEmpty(listUserTags)){
            if(flag){
                listUserTags.forEach(userTags -> {
                    ESTagsInfoVO esTagsInfoVO = new ESTagsInfoVO();
                    BeanUtil.copyPropertiesIgnoreNullFilds(userTags, esTagsInfoVO);
                    listEsTagsInfo.add(esTagsInfoVO);
                });

            }
        }else{
            if(esOrgInfoVO.getOrgTags() != null && esOrgInfoVO.getOrgTags().size() > 0){
                param.put("orgTags",listEsTagsInfo);
            }
        }
    }
    /**
     * 修改ES信息
     * @param user 用户 （个体账号。组织账户主账号
     * @param userOrgClient 组织账户
     * @return String 返回码
     */
    private String updateESInfo(User user, UserOrgClient userOrgClient, Map<String,Object> updateParam){
        //非空判断
        if(user == null) {return ErrorCodeConst.USER_NOTNULL;}
        if(userOrgClient == null)  {return  ErrorCodeConst.ERRPR_ORG_ISNULL;}
        if(user.getUserType() == null || user.getUserAccountType() == UserAccountType.ORG_CHILD_ACCOUNT || (!userOrgClient.isPushFlag())
                || userOrgClient.getUserAccountType() == null || StringUtils.isBlank(userOrgClient.getElasticId()))
        {return  Constants.COMMON_MISSING_PARAMS;}
        boolean result = false;
        switch (user.getUserType()){
            case LK:
                ESGetSingleResponseVO<ESDepotOrgInfoVO> esGetSingleResponseVO = apecESProducer.getSingleESInfoById(ESProducerConstants.INDEX_URL_DEPORT_ORG,
                        userOrgClient.getElasticId(), ESDepotOrgInfoVO.class);
                if(null != esGetSingleResponseVO && esGetSingleResponseVO.getFound()) {
                    result = apecESProducer.postESInfoForUpdateByDoc(ESProducerConstants.INDEX_URL_DEPORT_ORG, userOrgClient.getElasticId(), updateParam);
                }
                break;
            case DB:
                ESGetSingleResponseVO<ESAgencyInfoVO> esGetDBResponseVO = apecESProducer.getSingleESInfoById(ESProducerConstants.INDEX_URL_DAIBAN_ORG,
                        userOrgClient.getElasticId(), ESAgencyInfoVO.class);
                if(null != esGetDBResponseVO && esGetDBResponseVO.getFound()) {
                    result = apecESProducer.postESInfoForUpdateByDoc(ESProducerConstants.INDEX_URL_DAIBAN_ORG, userOrgClient.getElasticId(), updateParam);
                }
                break;
            case KS:
                ESGetSingleResponseVO<ESAgencyInfoVO> esGetKSResponseVO = apecESProducer.getSingleESInfoById(ESProducerConstants.INDEX_URL_KESHAN_ORG,
                        userOrgClient.getElasticId(), ESAgencyInfoVO.class);
                if(null != esGetKSResponseVO && esGetKSResponseVO.getFound()) {
                    result = apecESProducer.postESInfoForUpdateByDoc(ESProducerConstants.INDEX_URL_KESHAN_ORG, userOrgClient.getElasticId(), updateParam);
                }
                break;
        }
        if(result){
            logger.info("[UserServiceImpl]#[updateESInfo] UserId:{}， Update ES Info Success : esId:{}", user.getId(), userOrgClient.getElasticId());
            return Constants.RETURN_SUCESS;
        }else {
            logger.warn("[UserServiceImpl]#[updateProduct] UserId:{}， Update ES Info failed  : esId:{}", user.getId(), userOrgClient.getElasticId());
            throw new BusinessException(Constants.SYS_ERROR);
        }
    }

    /**
     * 修改ES信息
     * @param user 用户 （个体账号。组织账户主账号
     * @param userOrgClient 组织账户
     * @return String 返回码
     */
    private String deleteESInfo(User user, UserOrgClient userOrgClient){
        //非空判断
        if(user == null) {return ErrorCodeConst.USER_NOTNULL;}
        if(userOrgClient == null)  {return  ErrorCodeConst.ERRPR_ORG_ISNULL;}
        if(user.getUserType() == null || (!userOrgClient.isPushFlag())
                || userOrgClient.getUserAccountType() == null || StringUtils.isBlank(userOrgClient.getElasticId()))
        {return  Constants.COMMON_MISSING_PARAMS;}
        boolean result = false;
        switch (user.getUserType()){
            case LK:
                ESGetSingleResponseVO<ESDepotOrgInfoVO> esGetSingleResponseVO = apecESProducer.getSingleESInfoById(ESProducerConstants.INDEX_URL_DEPORT_ORG,
                        userOrgClient.getElasticId(), ESDepotOrgInfoVO.class);
                if(null != esGetSingleResponseVO && esGetSingleResponseVO.getFound()) {
                    result = apecESProducer.deleteESInfo(ESProducerConstants.INDEX_URL_DEPORT_ORG, userOrgClient.getElasticId());
                }
                break;
            case DB:
                ESGetSingleResponseVO<ESAgencyInfoVO> esGetDBResponseVO = apecESProducer.getSingleESInfoById(ESProducerConstants.INDEX_URL_DAIBAN_ORG,
                        userOrgClient.getElasticId(), ESAgencyInfoVO.class);
                if(null != esGetDBResponseVO && esGetDBResponseVO.getFound()) {
                    result = apecESProducer.deleteESInfo(ESProducerConstants.INDEX_URL_DAIBAN_ORG, userOrgClient.getElasticId());
                }
                break;
            case KS:
                ESGetSingleResponseVO<ESAgencyInfoVO> esGetKSResponseVO = apecESProducer.getSingleESInfoById(ESProducerConstants.INDEX_URL_KESHAN_ORG,
                        userOrgClient.getElasticId(), ESAgencyInfoVO.class);
                if(null != esGetKSResponseVO && esGetKSResponseVO.getFound()) {
                    result = apecESProducer.deleteESInfo(ESProducerConstants.INDEX_URL_KESHAN_ORG, userOrgClient.getElasticId());
                }
                break;
        }
        if(result){
            logger.info("[UserServiceImpl]#[deleteESInfo] UserId:{}， Update ES Info Success : esId:{}", user.getId(), userOrgClient.getElasticId());
            return Constants.RETURN_SUCESS;
        }else {
            logger.warn("[UserServiceImpl]#[deleteESInfo] UserId:{}， Update ES Info failed  : esId:{}", user.getId(), userOrgClient.getElasticId());
            return Constants.SYS_ERROR;
        }
    }

    @Override
    @Transactional
    public String addNewUser(UserVO userVO,String userId) {
        UserVO vo = new UserVO();
        vo.setMobile(userVO.getMobile());
        //校验用户信息是否存在
        boolean isExist = this.isExist(vo);
        if(isExist){
            logger.info("[addNewUser] User is Exist. mobile{}:",userVO.getMobile());
            return ErrorCodeConst.USER_IS_EXIST;
        }
        //保存用户信息
        Date date = new Date();
        User user = new User();
        BeanUtil.copyPropertiesIgnoreNullFilds(userVO,user);
        user.setName(userVO.getMobile());
        user.setId(idGen.nextId());
        user.setEnableFlag(EnableFlag.Y);
        user.password(userVO.getMobile(),userVO.getPassword());
        user.setCreateDate(date);
        user.setUserStatus(UserStatus.UNREALAUTH);
        user.setUserRealAuth(UserRealAuth.UNREALAUTH);
        //是否有推荐人
        if(StringUtils.isNotBlank(userVO.getReferralId())){
            user.setReferralRealm(Realm.USER);
            user.setReferralId(userVO.getReferralId());
            //TODO 推荐人推荐,获得积分,验证推荐人的正确性
        }

        UserOrgClient userOrgClient = initUserOrgClient(user,userId);
        user.setUserOrgId(userOrgClient.getId());
        user.setUserAccountType(UserAccountType.IND_MAIN_ACCOUNT); //默认账号
        userDao.saveAndFlush(user);
        updateUserOrgInfoCache(userOrgClient);
        User user1 = userDao.findOne(user.getId());
        if(user1 == null){
            logger.error(" test findOne , the user id null , the userId id :{}" ,user.getId());
        }
        //初次登陆积分设置
        UserPointRecordVO userPointRecordVO = new UserPointRecordVO();
        userPointRecordVO.setId(idGen.nextId());
        userPointRecordVO.setPointRuleType(PointRuleType.REGISTER_LOGIN);//注册首次登陆
        List<Long> userIds = new ArrayList<>();
        userIds.add(user.getId());
        userPointRecordVO.setUserIds(userIds);
        //添加积分
        mqProducerClient.sendConcurrently(MqTag.USER_POINT_TAG_INIT_POINT.getKey(),String.valueOf(userPointRecordVO.getId()),userPointRecordVO);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 初始化组织信息
     */
    public UserOrgClient initUserOrgClient(User user, String userId){
        //用户组织的编号不存在则数据补足, ORG_CHILD_ACCOUNT 不能创建组织
        UserOrgClient userOrgClient = new UserOrgClient();
        userOrgClient.setId(idGen.nextId());
        userOrgClient.setEnableFlag(EnableFlag.Y);
        userOrgClient.setCreateDate(new Date());
        userOrgClient.setCreateBy(userId);
        userOrgClient.setAttentionNum(0);
        userOrgClient.setViewNum(0);
        userOrgClient.setProductNum(0);
        if(user.getUserAccountType() == UserAccountType.ORG_MAIN_ACCOUNT || user.getUserAccountType() == UserAccountType.ORG_CHILD_ACCOUNT){
            //当后台设置旧用户为主账号True，则主账号修改个人资料编辑组织信息，创建组织，设为组织账号
            userOrgClient.setUserAccountType(UserAccountType.ORG_ACCOUNT);  //组织账号
        }else{
            userOrgClient.setUserAccountType(UserAccountType.IND_ACCOUNT); //默认账户
        }
        //初始化组织名称和banner图缩略图地址
        userOrgClient.setOrgName(user.getName());
        userOrgClient.setOrgFirstBannerUrl(user.getImgUrl());
        return userOrgClient;
    }


    /**
     * 修改用户个人头像
     * @param userVO
     * @param userId
     * @param resultMap
     * @return
     */
    @Override
    @Transactional
    public String updateImage(UserVO userVO,String userId, Map<String,String> resultMap){
        //查詢用戶信息
        User user = userDao.findByIdAndEnableFlag(userVO.getId(),EnableFlag.Y);
        if(user == null){
            logger.info("[updateImage] user is not exist id{}:",userVO.getId());
            return ErrorCodeConst.USER_NOTNULL;
        }
        if(StringUtils.isNotBlank(userVO.getImgUrl())){
            user.setImgUrl(userVO.getImgUrl());
            resultMap.put("imgUrl",user.getImgUrl());
            //为个人账号，个人头像为banner图的缩略图地址
            if(user.getUserAccountType() == UserAccountType.IND_MAIN_ACCOUNT){
                UserOrgClient userOrgClient = null;
                //不为组织账号时，Banner的缩略图地址为个人头像地址
                if(user.getUserOrgId() != null){
                    userOrgClient = userOrgClientDAO.findByIdAndEnableFlag(user.getUserOrgId(),EnableFlag.Y);
                }
                //组织不存在则新建组织
                if(userOrgClient == null){
                    userOrgClient = initUserOrgClient(user,userId);
                    user.setUserOrgId(userOrgClient.getId());
                    if(user.getUserAccountType() != UserAccountType.ORG_MAIN_ACCOUNT){
                        user.setUserAccountType(UserAccountType.IND_MAIN_ACCOUNT);  //默认账号
                    }
                }
                if(userOrgClient != null){
                    userOrgClient.setOrgFirstBannerUrl(user.getImgUrl());
                    userOrgClient.setLastUpdateBy(userId);
                    userOrgClient.setLastUpdateDate(new Date());
                    userOrgClientDAO.save(userOrgClient);
                    //修改es中组织信息的banner图的缩略图地址
                    Map<String,Object> updateEsOrgInfo = new HashedMap();
                    updateEsOrgInfo.put("orgFirstBannerUrl",userOrgClient.getOrgFirstBannerUrl());
                    if(userOrgClient.isPushFlag()){
                        updateESInfo(user,userOrgClient,updateEsOrgInfo);
                    }

                }
            }
            user.setLastUpdateDate(new Date());
            user.setLastUpdateBy(userId);
            userDao.save(user);
            updateUserInfoCache(String.valueOf(user.getId()),user);
        }
        return Constants.RETURN_SUCESS;
    }

    /**
     * 修改组织Banner图
     * @param userVO
     * @param userId
     * @param resultMap
     * @return
     */
    @Override
    @Transactional
    public String updateBanner(UserVO userVO,String userId, Map<String,String> resultMap){
        //查詢用戶信息
        User user = userDao.findByIdAndEnableFlag(userVO.getId(),EnableFlag.Y);
        if(user == null){
            logger.info("[updateBanner] user is not exist id{}:",userVO.getId());
            return ErrorCodeConst.USER_NOTNULL;
        }
        if(user.getUserAccountType() == UserAccountType.ORG_CHILD_ACCOUNT){
            //非主账号不能编辑
            return ErrorCodeConst.ERROR_ORG_CHILDACCOUNT_EDITERR;
        }
        if(userVO.getUserOrgClientVO() != null && StringUtils.isNotBlank(userVO.getUserOrgClientVO().getOrgBannerUrl())){
            UserOrgClient userOrgClient = null;
            if(user.getUserOrgId() != null) {
                userOrgClient = userOrgClientDAO.findByIdAndEnableFlag(user.getUserOrgId(),EnableFlag.Y);
            }
            if(userOrgClient == null){
                userOrgClient = initUserOrgClient(user,userId);
                user.setUserOrgId(userOrgClient.getId());
                if(user.getUserAccountType() != UserAccountType.ORG_MAIN_ACCOUNT){
                    user.setUserAccountType(UserAccountType.IND_MAIN_ACCOUNT);  //默认账号
                }else{
                    user.setUserAccountType(UserAccountType.ORG_ACCOUNT);  //组织账号
                }
            }
            userOrgClient.setOrgBannerUrl(userVO.getUserOrgClientVO().getOrgBannerUrl());
            resultMap.put("orgBannerURL",userOrgClient.getOrgBannerUrl());
            if(user.getUserAccountType() == UserAccountType.ORG_MAIN_ACCOUNT){
                //如果为组织组账号。，则同时修改缩略图路径
                userOrgClient.setOrgFirstBannerUrl(userVO.getUserOrgClientVO().getOrgFirstBannerUrl());
                resultMap.put("orgFirstBannerURL",userOrgClient.getOrgFirstBannerUrl());
                userOrgClient.setLastUpdateBy(userId);
                userOrgClient.setLastUpdateDate(new Date());
                userOrgClientDAO.save(userOrgClient);
                //修改es中组织信息的banner图的缩略图地址
                Map<String,Object> updateEsOrgInfo = new HashedMap();
                updateEsOrgInfo.put("orgFirstBannerUrl",userOrgClient.getOrgFirstBannerUrl());
                if(userOrgClient.isPushFlag()){
                    updateESInfo(user,userOrgClient,updateEsOrgInfo);
                }

            }
        }
        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional
    public String updateUserInfo(UserVO userVO,String userId, Map<String,String> resultMap) {
        Map<String,Object> updateEsOrgInfo = new HashedMap();
        //查詢用戶信息
        User user = userDao.findByIdAndEnableFlag(userVO.getId(),EnableFlag.Y);
        if(user == null){
            logger.info("[updateUserInfo] user is not exist id{}:",userVO.getId());
            return ErrorCodeConst.USER_NOTNULL;
        }
        if(userVO.getSex() != null) {user.setSex(userVO.getSex());}

        if(StringUtils.isNotBlank(userVO.getName())){
            user.setName(userVO.getName());
            resultMap.put("name",user.getName());
        }
        //获取用户组织
        UserOrgClient userOrgClient = null;
        if(user.getUserOrgId() != null) {
            userOrgClient = userOrgClientDAO.findByIdAndEnableFlag(user.getUserOrgId(),EnableFlag.Y);
        }

        if(userOrgClient != null && !userOrgClient.isPushFlag()){//未推送时身份可以修改，已经推送后身份无法进行修改
            if(userVO.getUserType() != null){
                user.setUserType(userVO.getUserType());
                resultMap.put("userType",user.getUserType().name());
                resultMap.put("userTypeKey",user.getUserType().getKey());
            }
            if(userVO.getUserDetailType() != null && (user.getUserType() == UserType.LK || user.getUserType() == UserType.DB)){
                user.setUserDetailType(userVO.getUserDetailType());
                resultMap.put("userDetailType",user.getUserDetailType().name());
                resultMap.put("userDetailTypeKey",user.getUserDetailType().getKey());
            }else{
                user.setUserDetailType(null);
            }
        }
        if(StringUtils.isNotBlank(userVO.getAddressDetail())) {
            user.setAddressDetail(userVO.getAddressDetail());
        }
        if(StringUtils.isNotBlank(userVO.getMainOperating())) {
            user.setMainOperating(userVO.getMainOperating());
        }
        if(StringUtils.isNotBlank(userVO.getWorkOfYear())) {
            user.setWorkOfYear(userVO.getWorkOfYear());
        }
        //子账号不能编辑组织信息
        if(user.getUserAccountType() != UserAccountType.ORG_CHILD_ACCOUNT){
            if(userOrgClient == null){
                userOrgClient = initUserOrgClient(user,userId);
                user.setUserOrgId(userOrgClient.getId());
                if(user.getUserAccountType() != UserAccountType.ORG_MAIN_ACCOUNT){
                    user.setUserAccountType(UserAccountType.IND_MAIN_ACCOUNT);  //默认账号
                }
            }
            UserOrgClientVO userOrgClientVO = userVO.getUserOrgClientVO();
            //用户昵称不为空且与组织名称不相等
            if(StringUtils.isNotBlank(userVO.getName()) && (StringUtils.isBlank(userOrgClient.getOrgName()) || user.getUserType() != UserType.LK)){
                //组织账号为空时
                userOrgClient.setOrgName(userVO.getName());//组织名称为个人昵称
                updateEsOrgInfo.put("orgName",userOrgClient.getOrgName());
            }
            //用户组织VO
            if(userOrgClientVO != null){
                if(StringUtils.isNotBlank(userOrgClientVO.getOrgName()) && !StringUtils.equals(userOrgClientVO.getOrgName(),userOrgClient.getOrgName()) && user.getUserAccountType() != UserAccountType.ORG_CHILD_ACCOUNT && user.getUserType() == UserType.LK){
                    userOrgClient.setOrgName(userOrgClientVO.getOrgName());
                    updateEsOrgInfo.put("orgName",userOrgClient.getOrgName());
                }

                if(StringUtils.isNotBlank(userOrgClientVO.getAddress()) && !StringUtils.equals(userOrgClientVO.getAddress(),userOrgClient.getAddress())){
                    userOrgClient.setAddress(userOrgClientVO.getAddress());
                    updateEsOrgInfo.put("address",userOrgClient.getAddress());
                }
                if(StringUtils.isNotBlank(userOrgClientVO.getMainOperating()) && !StringUtils.equals(userOrgClientVO.getMainOperating(),userOrgClient.getMainOperating())){
                    userOrgClient.setMainOperating(userOrgClientVO.getMainOperating());
                    updateEsOrgInfo.put("mainOperating",userOrgClient.getMainOperating());
                }
                if(StringUtils.isNotBlank(userOrgClientVO.getSaleAddress()) && !StringUtils.equals(userOrgClientVO.getSaleAddress(),userOrgClient.getSaleAddress())){
                    userOrgClient.setSaleAddress(userOrgClientVO.getSaleAddress());
                    updateEsOrgInfo.put("saleAddress",userOrgClient.getSaleAddress());
                }
                if(StringUtils.isNotBlank(userOrgClientVO.getOrgStockCap()) && !StringUtils.equals(userOrgClientVO.getOrgStockCap(),userOrgClient.getOrgStockCap())){
                    userOrgClient.setOrgStockCap(userOrgClientVO.getOrgStockCap());
                    updateEsOrgInfo.put("orgStockCap",userOrgClient.getOrgStockCap());
                }
                userOrgClient.setRemark(userOrgClientVO.getRemark());
                userOrgClient.setAddressDetail(userOrgClientVO.getAddressDetail());

                //当用户的实力描述图片不为空，Image地址落地
                if(userOrgClientVO.getUserOrgImageVOS() != null){
                    List<UserOrgImage> userOrgImages = userOrgImageDAO.findByUserOrgIdAndEnableFlagOrderBySort(userOrgClient.getId(),EnableFlag.Y);
                    if(!CollectionUtils.isEmpty(userOrgImages)){
                        userOrgImages.forEach(userOrgImage -> {
                            Boolean isExist = false;
                            for(UserOrgImageVO vo : userOrgClientVO.getUserOrgImageVOS()){
                                if(vo != null && StringUtils.equals(userOrgImage.getImageUrl(),vo.getImageUrl())){
                                    isExist = true;
                                }
                            }
                            if(!isExist){
                                //如果原先数据库存储的图片不存在，则删除它
                                userOrgImage.setEnableFlag(EnableFlag.N);
                                userOrgImage.setLastUpdateDate(new Date());
                                userOrgImage.setLastUpdateBy(userId);
                                userOrgImageDAO.saveAndFlush(userOrgImage);
                            }
                        });
                    }
                    for(UserOrgImageVO vo:userOrgClientVO.getUserOrgImageVOS()){
                        if(StringUtils.isNotBlank(vo.getImageUrl())){
                            Boolean isNotExist = true;
                            if(!CollectionUtils.isEmpty(userOrgImages)) {
                                for (UserOrgImage userOrgImage : userOrgImages) {
                                    if ( StringUtils.equals(userOrgImage.getImageUrl(), vo.getImageUrl())) {
                                        isNotExist = false;
                                        userOrgImage.setSort(vo.getSort());  //更换排序
                                        userOrgImage.setLastUpdateDate(new Date());
                                        userOrgImage.setLastUpdateBy(userId);
                                        userOrgImage.setUserOrgId(userOrgClient.getId());
                                        userOrgImageDAO.saveAndFlush(userOrgImage);
                                    }
                                }
                            }
                            if(isNotExist){
                                //传入的图片地址在数据库中不存在，则添加图片地址
                                UserOrgImage userOrgImage = new UserOrgImage();
                                BeanUtil.copyPropertiesIgnoreNullFilds(vo,userOrgImage);
                                userOrgImage.setUserOrgId(userOrgClient.getId());
                                userOrgImage.setId(idGen.nextId());
                                userOrgImage.setEnableFlag(EnableFlag.Y);
                                userOrgImage.setCreateBy(userId);
                                userOrgImage.setCreateDate(new Date());
                                userOrgImageDAO.saveAndFlush(userOrgImage);
                            }
                        }
                    }
                }
            }
//            userOrgClient.setAttentionNum(0);
//            userOrgClient.setViewNum(0);
//            userOrgClient.setProductNum(0);
            //非组织账户的组织名称为用户昵称
            userOrgClient.setLastUpdateBy(userId);
            userOrgClient.setLastUpdateDate(new Date());
            userOrgClientDAO.saveAndFlush(userOrgClient);
            if(!updateEsOrgInfo.isEmpty() && userOrgClient.isPushFlag()){
                //更新组织缓存,es中的组织相关信息
                updateESInfo(user,userOrgClient,updateEsOrgInfo);
            }
        }
//        String addId = "";
//        if(StringUtils.isNotBlank(userVO.getProvinceId())){
//            user.setProvinceId(userVO.getProvinceId());
//            addId = user.getProvinceId();
//        }
//        if(StringUtils.isNotBlank(userVO.getCityId())){
//            user.setCityId(userVO.getCityId());
//            addId = user.getCityId();
//        }
//        if(StringUtils.isNotBlank(userVO.getAreaId())){
//            user.setAreaId(userVO.getAreaId());
//            addId = user.getAreaId();
//        }
//        if(StringUtils.isNotBlank(userVO.getTownId())){
//            user.setTownId(userVO.getTownId());
//            addId = user.getTownId();
//        }
//        String address = "";
//        if(StringUtils.isNotBlank(addId)){
//            //发送请求。获取地址信息
//            Map<String,String> requestInfo = new HashMap<>();
//            requestInfo.put("code",addId);
//            ResultData resultData = null;
//            String responseStr = springCloudClient.post(requestAddress,JsonUtil.toJSONString(requestInfo));
//            resultData = JsonUtil.parseObject(responseStr, ResultData.class);
//            if(resultData.isSucceed()){
//                address = (String)resultData.getData();
//                address = address.replaceAll("\\|","");
//            }
//        }
//        if(StringUtils.isNotBlank(address))  user.setAddress(address);
        user.setLastUpdateDate(new Date());
        user.setLastUpdateBy(userId);
        userDao.save(user);
        //更新缓存中的用户基本信息
        updateUserInfoCache(String.valueOf(user.getId()),user);
        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional
    public String updateUserMobile(UserVO userVO, String userId,Map<String,String> resultMap) {
        User user = userDao.findByIdAndEnableFlag(userVO.getId(),EnableFlag.Y);
        if(user == null){
            logger.info("[updateUserMobile] user is not exist id{}:",userVO.getId());
            return ErrorCodeConst.USER_NOTNULL;
        }
        if(StringUtils.isNotBlank(userVO.getMobile())){
            user.setMobile(userVO.getMobile());
        }
        user.setLastUpdateDate(new Date());
        user.setLastUpdateBy(userId);
        userDao.save(user);
        resultMap.put("mobile",user.getMobile());
        //更新缓存中的用户基本信息
        updateUserInfoCache(String.valueOf(user.getId()),user);
        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional
    public String quickLogin(UserLoginVO userLoginVO ,int tokenOutTime,Map<String,String> resultMap,boolean isFirstLogin) {
        User user = userDao.findByMobileAndEnableFlag(userLoginVO.getMobile(),EnableFlag.Y);
        if (user == null) {
            logger.warn("[UserLogin][login] User not exist.[userLoginVO.mobile={},ip={}]",userLoginVO.getMobile(),userLoginVO.getIp());
            return ErrorCodeConst.ERROR_USER_LOGIN_FAILED;
        }
        if(user.getUserStatus() == UserStatus.FREEZE){
            logger.warn("[UserLogin][login] The user account is locked.[userLoginVO.mobile={},ip={}]",userLoginVO.getMobile(),userLoginVO.getIp());
            return ErrorCodeConst.ERROR_USER_LOGIN_LOCK;
        }

        //登陆记录
        Date nowDate = new Date();
        UserLoginRecord userLoginRecord = new UserLoginRecord();
        userLoginRecord.setLoginTime(nowDate);
        userLoginRecord.setId(idGen.nextId());
        userLoginRecord.setCredential(userLoginVO.getMobile());
        userLoginRecord.setIpAddress(userLoginVO.getIp());
        userLoginRecord.setSource(userLoginVO.getSource() == null? Source.WEIXIN : userLoginVO.getSource());
        userLoginRecord.setCreateDate(nowDate);
        userLoginRecord.setUserId(user.getId());

        //获取User登录错误次数
        String loginFailCountKey = LoginConstants.PREFIX_LOGIN_FAILED + user.getId();
        String loginFailCount =  cacheService.get(loginFailCountKey);
        if(SecurityUtils.matchPassphrase(user.getPassphrase(), user.getSalt(), userLoginVO.getPassword())) {
            user.setLoginNumber(user.getLoginNumber() + 1); //用户并发登陆情况下的登陆次数 不考虑
            user.setLastUpdateDate(nowDate); //更新上次登陆时间
            userLoginRecord.setSuccess(true);
            userDao.save(user);
            userLoginRecordDAO.save(userLoginRecord);

            if(StringUtils.isNotBlank(loginFailCount)){
                cacheService.remove(loginFailCountKey); //移除计数器
            }

            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(user,userInfoVO);
            userInfoVO.setUserId(user.getId());
            userInfoVO.setUserTypeKey(userInfoVO.getUserType()== null? "":userInfoVO.getUserType().getKey());
            String token = getLoginToken(JsonUtil.toJSONString(userInfoVO),String.valueOf(user.getId()),tokenOutTime);
            //用户积分和等级
            UserPoint point = userPointDAO.findByUserIdAndEnableFlag(user.getId(),EnableFlag.Y);
            if(!isFirstLogin && point != null){
                //更新缓存（积分及实名认证状态）
                updateUserCache(String.valueOf(user.getId()), point, user);
            }
            resultMap.put("point",point == null?String.valueOf(0):String.valueOf(point.getAvailablePoints()));
            resultMap.put("userLevelKey",point == null?UserLevel.QT.getKey():point.getUserLevel().getKey());
            resultMap.put("userLevelName",point == null?UserLevel.QT.name():point.getUserLevel().name());
            resultMap.put("token",token);
            resultMap.put("name",user.getName());
            resultMap.put("mobile",user.getMobile());
            resultMap.put("imgUrl",user.getImgUrl());
            resultMap.put("userRealAuthName",user.getUserRealAuth().name());
            resultMap.put("userRealAuthKey",user.getUserRealAuth().getKey());
            resultMap.put("userTypeName",user.getUserType().name());
            resultMap.put("userTypeKey",user.getUserType().getKey());
            return Constants.RETURN_SUCESS;
        }
        userLoginRecord.setSuccess(false);
        userLoginRecordDAO.save(userLoginRecord);

        //失败次数记录
        if(StringUtils.isBlank(loginFailCount)){
            cacheService.add(loginFailCountKey,"0",LoginConstants.LOGIN_FAILED_COUNT_EXPREISE);
            return ErrorCodeConst.ERROR_USER_LOGIN_FAILED; //登陆失败
        }
        Integer countInt = Integer.parseInt(loginFailCount) + 1;
        if(countInt > LoginConstants.LOGIN_FAILED_MAX_COUNT){
            // 冻结用户
            user.setUserStatus(UserStatus.FREEZE);
            userDao.save(user);

            cacheService.remove(loginFailCountKey);
            return ErrorCodeConst.ERROR_USER_LOGIN_COUNT_OUT;
        }
        //更新登陆失败的次数，以及对应的过期时间
        cacheService.add(loginFailCountKey, String.valueOf(countInt), LoginConstants.LOGIN_FAILED_COUNT_EXPREISE);
        return ErrorCodeConst.ERROR_USER_LOGIN_FAILED; //登陆失败
    }

    @Override
    @Transactional
    public String freezeOrUnFreeze(String userId, UserStatus userStatus){
        User user = userDao.findByMobileAndEnableFlag(userId,EnableFlag.Y);
        if (user == null) {
            logger.warn("[UserService][freezeUser] User not exist.[user.id={}]",userId);
            return Constants.DATA_ISNULL;
        }
        user.setUserStatus(userStatus);
        userDao.save(user);

        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional
    public String userRealNameApply(UserAuthRecordVO authRecordVO, String userId,Map<String,String> resultMap) {
        //查詢用戶信息
        User user = userDao.findByIdAndEnableFlag(authRecordVO.getUserId(),EnableFlag.Y);
        if(user == null || user.getId() == null || user.getId() == 0L){
            logger.info("[userRealNameApply] user is not exist id{}:",authRecordVO.getUserId());
            return ErrorCodeConst.USER_NOTNULL;
        }
        if(StringUtils.equals(user.getUserRealAuth().name(),UserRealAuth.AUDITING.name()) || StringUtils.equals(user.getUserRealAuth().name(),UserRealAuth.NORMAL.name())){
            //用户已经在实名认证通过或正在实名认证审批中，不予再次提交实名认证信息
            logger.info("[userRealNameApply] user had realName query ,the userId : {}",authRecordVO.getUserId());
            return ErrorCodeConst.HAD_REALNAME;
        }
        //验证身份证的唯一性
        if(isExistIdNumber(authRecordVO.getIdNumber())){
            //如果该身份证已经实名认证通过
            logger.info("[userRealNameApply] the idNumber had use for realName ,the idNumber is : {}",user.getId());
            return ErrorCodeConst.IDNUMBER_ISEXIST;
        }
        //添加到用户实名认证申请记录中
        UserAuthRecord authRecord = new UserAuthRecord();
        authRecord.setImgOneURL(authRecordVO.getImgOneURL());
        authRecord.setImgTwoURL(authRecordVO.getImgTwoURL());
        authRecord.setIdNumber(authRecordVO.getIdNumber());
        authRecord.setRealName(authRecordVO.getRealName());
        authRecord.setUser(user);
        authRecord.setId(idGen.nextId());
        authRecord.setCreateBy(userId);
        authRecord.setCreateDate(new Date());
        authRecord.setEnableFlag(EnableFlag.Y);
        userAuthRecordDAO.save(authRecord);
        //修改用户信息
        user.setLastUpdateBy(userId);
        user.setLastUpdateDate(new Date());

        user.setUserRealAuth(UserRealAuth.AUDITING);//状态为实名认证中
        //用户缓存变更
//        UserPoint userPoint = userPointDAO.findByUserIdAndEnableFlag(user.getId(),EnableFlag.Y);
        updateUserCache(String.valueOf(user.getId()),null,user);
        //更新用户基本信息缓存
        updateUserInfoCache(String.valueOf(user.getId()),user);
        //Save
        userDao.save(user);
        resultMap.put("userRealAuthName",user.getUserRealAuth().name());
        resultMap.put("userRealAuthKey",user.getUserRealAuth().getKey());
        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional
    public String userRealNameEntrue(UserAuthRecordVO authRecordVO, String userId,Map<String,String> resultMap) {
        UserAuthRecord authRecord = userAuthRecordDAO.findByIdAndEnableFlag(authRecordVO.getId(),EnableFlag.Y);
        if(authRecord == null || authRecord.getId() == null || authRecord.getId() == 0L){
            logger.info("[userRealNameEntrue] userAuthRecord is not exist，id:{}",authRecordVO.getId());
            return Constants.COMMON_ERROR_PARAMS;
        }
        if(!StringUtils.isBlank(authRecord.getSuccess())){
            //该条记录已经审批过，不可以重复审批
            logger.info("[userRealNameEntrue] userAuthRecord had entrue ，id:{}",authRecordVO.getId());
            return ErrorCodeConst.CANOT_DUMBLE_ENTRUE;
        }
        if(authRecord.getUser() == null){
            logger.info("[userRealNameEntrue] user id  is not exist，id:{}",authRecordVO.getId());
            return Constants.COMMON_ERROR_PARAMS;
        }
        User user = userDao.findByIdAndEnableFlag(authRecord.getUser().getId(),EnableFlag.Y);
        if(user == null){
            logger.info("[userRealNameEntrue] user id  is not exist，id:{}",authRecord.getId());
            return Constants.COMMON_ERROR_PARAMS;
        }
        //审批用户实名认证信息
        authRecord.setRemark(authRecordVO.getRemark());
        authRecord.setSuccess(authRecordVO.getSuccess());
        authRecord.setReviewDate(new Date());
        authRecord.setReviewEmp(userId);
        //初始化站内信信息
        MessageBodyVO messageBodyVO = new MessageBodyVO();
        messageBodyVO.setType(MessageType.NOTIFICATION);//系统通知
        messageBodyVO.setTemplateFlag(true);//使用动态模板发送
        messageBodyVO.setRealm(Realm.USER);//???是否必须
        Map<String, String> contentMap = new HashMap<>();

        user.setUserRealAuth(UserRealAuth.REJECTED);
        if(StringUtils.equals(Constants.ISSUCCESS,authRecordVO.getSuccess())){
            user.setRealName(authRecord.getRealName());
            user.setIdNumber(authRecord.getIdNumber());
            //用户信息是否已经修改了性别
            if(user.getSex() == null){
                //没有修改过性别则按照输入的身份证信息最后一位来判定
                String idNumber = authRecord.getIdNumber();
                //15位身份证号最后一位为性别位
                String sex = idNumber.charAt(authRecord.getIdNumber().length() - 1)+"";
                if(idNumber.length() == 18){
                    //18位身份证性别为倒数第二位
                    sex = idNumber.charAt(authRecord.getIdNumber().length() - 2)+"";
                }
                int s = Integer.valueOf(sex);
                if(s%2 == 0){
                    //偶数，该用户为女性
                    user.setSex(Sex.FEMALE);
                }else{
                    user.setSex(Sex.MALE);
                }
                resultMap.put("sexName",user.getSex().name());
                resultMap.put("sexValue",user.getSex().getValue());
            }
            //认证通过
            user.setUserRealAuth(UserRealAuth.NORMAL);
            user.setUserStatus(UserStatus.NORMAL);
            //设置实名成功的消息模板以及相应的内容值
            messageBodyVO.setTemplateKey(MessageTemplate.USER_REALNAME_SUCCESS);
            contentMap.put("userName",user.getName());

            //认证通过，清除缓存
            String userInfoRedisKey = RedisHashConstants.HASH_USER_PREFIX + user.getId();
            cacheHashService.hdelField(userInfoRedisKey, SysBusinessConstants.PREFIX_CREATEPRODUCT_NUM);

        }else{
            //认证不通过
            //设置实名失败的消息模板以及相应的内容值
            messageBodyVO.setTemplateKey(MessageTemplate.USER_REALNAME_FAIL);
            contentMap.put("userName",StringUtils.isBlank(user.getName())?"":user.getName());
            contentMap.put("failCaused",StringUtils.isBlank(authRecord.getRemark())?"":authRecord.getRemark());

        }
        messageBodyVO.setContentMap(contentMap);
        user.setLastUpdateDate(new Date());
        user.setLastUpdateBy(userId);
        userAuthRecordDAO.save(authRecord);//修改实名认证记录中状态
        userDao.save(user);//修改用户信息中实名认证状态
        //用户缓存变更
        updateUserCache(String.valueOf(user.getId()),null,user);
        //更新缓存中的用户基本信息
        updateUserInfoCache(String.valueOf(user.getId()),user);
        resultMap.put("userRealAuthName",user.getUserRealAuth().name());
        resultMap.put("userRealAuthKey",user.getUserRealAuth().getKey());
        //发送站内信，通知用户审批结果
        MessageVO messageVO = new MessageVO();
        messageVO.setBody(messageBodyVO);
        messageVO.setId(idGen.nextId());
        List<Long> receivers = new ArrayList<>();
        receivers.add(user.getId());
        //接收者
        messageVO.setReceivers(receivers);
        //用户状态
        messageVO.setMessageStatus(MessageStatus.NEW);
        mqProducerClient.sendConcurrently(MqTag.MESSAGE_TAG.getKey(),String.valueOf(messageVO.getId()),messageVO);
        if(StringUtils.equals(Constants.ISSUCCESS,authRecordVO.getSuccess())){
            //给用户增加积分
            UserPointRecordVO userPointRecordVO = new UserPointRecordVO();
            //加积分的类型，实名认证成功
            userPointRecordVO.setPointRuleType(PointRuleType.VERIFIED_INFO);
            userPointRecordVO.setId(idGen.nextId());
            List<Long> userIds = new ArrayList<>();
            userIds.add(user.getId());
            userPointRecordVO.setUserIds(userIds);
            //实名认证加积分
            mqProducerClient.sendConcurrently(MqTag.USER_POINT_TAG_SUBSCRI_POINT.getKey(),String.valueOf(userPointRecordVO.getId()),userPointRecordVO);
        }
        return Constants.RETURN_SUCESS;
    }


    /**
     * 判断手机号是否已经注册
     * @param userVO
     * @return
     */
    @Override
    public boolean isExist(UserVO userVO){
        Long c = userDao.countByMobileAndEnableFlag(userVO.getMobile(),EnableFlag.Y);
        return c > 0;//如果该手机号下的有效用户个数大于0，则代码该手机号用户已经存在
    }

    /**
     * 找回密码
     * @param userVO
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String getPassword(UserVO userVO, String userId) {
        //通过手机号查找用户
        User user = userDao.findByMobileAndEnableFlag(userVO.getMobile(),EnableFlag.Y);
        if(user == null || user.getId() == null || user.getId() == 0L){
            logger.info("user is not exist id{}:",userVO.getId());
            return ErrorCodeConst.USER_NOTNULL;
        }
        //验证验证码有效性
        String key = Constants.CACHE_CAPTCHA_PREFIX + user.getMobile();
        String yzm = cacheService.get(key);
        if(StringUtils.isBlank(yzm)){
            return ErrorCodeConst.ERROR_VAILDCODE;
        }
        if(!StringUtils.equals(yzm,userVO.getVaildCode())){
            return ErrorCodeConst.ERROR_VAILDCODE;
        }
        cacheService.remove(key);//去除缓存中该用户的验证码信息，更新信息
        //更改密码
        user.password(user.getMobile(),userVO.getPassword());
        user.setLastUpdateBy(userId);
        user.setLastUpdateDate(new Date());
        userDao.save(user);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 修改密码
     * @param userVO
     * @param userId
     * @return
     */
    @Override
    public String updatePassword(UserVO userVO, String userId) {
        //查询用户信息
        User user = userDao.findByIdAndEnableFlag(userVO.getId(),EnableFlag.Y);
        if(user == null || user.getId() == null || user.getId() == 0L){
            logger.info("user is not exist id{}:",userVO.getId());
            return ErrorCodeConst.USER_NOTNULL;
        }
        //校验密码
        if(SecurityUtils.matchPassphrase(user.getPassphrase(), user.getSalt(), userVO.getOldPassword())) {
            //用户原密码与数据库密码匹配成功
            //更改密码
            user.password(user.getMobile(),userVO.getPassword());
            user.setLastUpdateBy(userId);
            user.setLastUpdateDate(new Date());
            userDao.save(user);
            return Constants.RETURN_SUCESS;
        }else{
            return ErrorCodeConst.ERROR_OLDPASSWORD;
        }
    }

    /**
     * 判断身份证是否已经被用于实名认证
     * @param idNumber
     * @return
     */
    private boolean isExistIdNumber(String idNumber){
        Long c = userDao.countByIdNumberAndEnableFlag(idNumber,EnableFlag.Y);
        //如果该身份证号下的有效用户大于0，则代表该身份证已经被用户使用认证
        return c > 0;
    }

    @Override
    public List<UserVO> selectAll(UserVO vo)
    {
        Iterable<User> iterable = userDao.findAll(getInputCondition(vo));
        Iterator<User> it = iterable.iterator();
        List<UserVO> list = new ArrayList<UserVO>();
        while (it.hasNext())
        {
            User entity = it.next();
            UserVO dto = new UserVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(entity, dto);
            list.add(dto);
        }
        return list;
    }


    /**
     * 根据多种情况查询用户信息
     * 包括like：name，realName, companyName, address, addressDetail eq：mobile，userType
     * @param vo
     * @return
     */
    private Predicate getInputCondition(UserVO vo)
    {
        List<BooleanExpression> predicates = new ArrayList<>();
        if(null != vo)
        {

            if (!StringUtils.isBlank(vo.getName())) {
                predicates.add(QUser.user.name.like("%" + vo.getName() + "%"));
            }
            if (!StringUtils.isBlank(vo.getMobile())) {
                predicates.add(QUser.user.mobile.like("%" + vo.getMobile() + "%"));
            }

            if(!StringUtils.isBlank(vo.getRealName()))
            {
                predicates.add(QUser.user.realName.like("%" + vo.getRealName() + "%"));
            }

            if(!StringUtils.isBlank(vo.getAddress()))
            {
                predicates.add(QUser.user.address.like("%" + vo.getAddress() + "%"));
            }
            if(!StringUtils.isBlank(vo.getAddressDetail()))
            {
                predicates.add(QUser.user.addressDetail.like("%" + vo.getAddressDetail() + "%"));
            }
            if(vo.getUserType() != null)
            {
                predicates.add(QUser.user.userType.eq(vo.getUserType()));
            }
            if(vo.getUserDetailType() != null)
            {
                predicates.add(QUser.user.userDetailType.eq(vo.getUserDetailType()));
            }
            if(vo.getUserAccountType() != null)
            {
                predicates.add(QUser.user.userAccountType.eq(vo.getUserAccountType()));
            }
            if(vo.getIdNumber() != null)
            {
                predicates.add(QUser.user.idNumber.like("%" + vo.getIdNumber() + "%"));
            }

        }
        predicates.add(QUser.user.enableFlag.eq(EnableFlag.Y));
        return BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
    }


    /**
     * 生成Login的Token，并缓存至Redis Cache
     * @param userNo 用户ID
     * @param tokenOutTime Token超时时间
     */
    private String getLoginToken(String userJson,String userNo,int tokenOutTime){
        String token = UUIDGenerator.getToken();
        // 清除之前用户id的 token
        Object oldToken = cacheService.get(LoginConstants.PREFIX_TOKEN_USERNO + userNo);
        if (null != oldToken){
            cacheService.remove(LoginConstants.PREFIX_TOKEN + oldToken);
        }
        //UserNo Token
        cacheService.add(LoginConstants.PREFIX_TOKEN + token, userNo, tokenOutTime * 24 * 60);
        // (token , userNo)
        // 保存token 到redis
        cacheService.add(LoginConstants.PREFIX_TOKEN_USERNO + userNo, token, tokenOutTime * 24 * 60);
        // (userNo , token)

        //Cache UserInfo
        cacheHashService.hset(RedisHashConstants.HASH_USER_PREFIX + userNo,RedisHashConstants.HASH_OBJCONTENT_CACHE, userJson);
        return token;
    }

    /**
     * 分页查询用户实名认证记录信息,可带条件查询(模糊查询用户名)
     * @param dto
     * @param pageRequest
     * @return
     */
    @Override
    public PageDTO<UserAuthRecordViewVO> pageUserRealNameRecord(UserDTO dto, PageRequest pageRequest){
        UserVO userVO = new UserVO();
        BeanUtil.copyPropertiesIgnoreNullFilds(dto,userVO);
        Page<UserAuthRecord> page = userAuthRecordDAO.findAll(getInputConditionUserRealName(userVO),pageRequest);
        PageDTO<UserAuthRecordViewVO> result = new PageDTO<>();
        List<UserAuthRecordViewVO> list = new ArrayList<>();
        User user ;
        for(UserAuthRecord authRecord:page){
            UserAuthRecordViewVO authRecordVO = new UserAuthRecordViewVO();
            user = authRecord.getUser();
            if(user != null){
                //将用户的手机号等信息放入申请记录中
                authRecordVO.setMobile(user.getMobile());
            }
            BeanUtil.copyPropertiesIgnoreNullFilds(authRecord,authRecordVO);
            list.add(authRecordVO);
        }
        result.setRows(list);
        result.setNumber(page.getNumber()+1);
        result.setTotalPages(page.getTotalPages());
        result.setTotalElements(page.getTotalElements());
        return result;
    }

    /**
     * 分页查询用户信息（管理端）
     * @param dto
     * @param pageRequest
     * @return
     */
    @Override
    public PageDTO<UserAllInfo> pageUserInfo(UserDTO dto, PageRequest pageRequest){
        PageDTO<UserAllInfo> result = new PageDTO<>();
        List<UserAllInfo> list = new ArrayList<>();
        Page<User> page = null;
        UserVO vo = new UserVO();
        BeanUtil.copyPropertiesIgnoreNullFilds(dto,vo,"pushFlag");
        if(StringUtils.isNotBlank(dto.getPushFlag())){
            List<UserOrgClient> userOrgClients = null;
            if(StringUtils.equals(Constants.ISSUCCESS,dto.getPushFlag())){
                userOrgClients = userOrgClientDAO.findByPushFlagAndEnableFlag(true,EnableFlag.Y);
            }else{
                userOrgClients = userOrgClientDAO.findByPushFlagAndEnableFlag(false,EnableFlag.Y);
            }
            if(userOrgClients != null){
                List<Long> userOrgIds = new ArrayList<>();
                userOrgClients.forEach(userOrgClient -> {
                    userOrgIds.add(userOrgClient.getId());
                });
                page = userDao.findByEnableFlagAndUserOrgIdIn(EnableFlag.Y,userOrgIds,pageRequest);
            }
        }else{
            page = userDao.findAll(getInputCondition(vo),pageRequest);
        }
        if(page != null){
            for(User user:page){
                UserAllInfo userAllInfo = new com.apec.user.vo.UserAllInfo();
                BeanUtil.copyPropertiesIgnoreNullFilds(user,userAllInfo);
                if(user.getUserRealAuth() != null){
                    userAllInfo.setUserRealAuthKey(user.getUserRealAuth().getKey());
                }
                if(user.getSex() != null){
                    userAllInfo.setSexValue(user.getSex().getValue());
                }
                if(user.getUserStatus() != null){
                    userAllInfo.setUserStatusKey(user.getUserStatus() == UserStatus.FREEZE?UserStatus.FREEZE.getKey():UserStatus.NORMAL.getKey());
                }
                if(user.getUserType() != null){
                    userAllInfo.setUserTypeKey(user.getUserType().getKey());
                }
                if(user.getUserAccountType() != null){
                    userAllInfo.setUserAccountTypeKey(user.getUserAccountType().getKey());
                }
                if(user.getUserDetailType() != null){
                    userAllInfo.setUserDetailTypeKey(user.getUserDetailType().getKey());
                }
                if(user.getUserOrgId() != null && user.getUserOrgId() != 0L){
                    UserOrgClient userOrgClient = userOrgClientDAO.findByIdAndEnableFlag(user.getUserOrgId(),EnableFlag.Y);
                    if(userOrgClient != null){
                        BeanUtil.copyPropertiesIgnoreNullFilds(userOrgClient,userAllInfo,"userAccountType","createDate","id");
                        userAllInfo.setOrgAccountType(userOrgClient.getUserAccountType());
                        userAllInfo.setOrgAccountTypeKey(userOrgClient.getUserAccountType().getKey());
                    }
                }
                UserPoint userPoint = userPointDAO.findByUserIdAndEnableFlag(user.getId(),EnableFlag.Y);
                if(userPoint != null){
                    BeanUtil.copyPropertiesIgnoreNullFilds(userPoint,userAllInfo,"createDate","id");
                }
                if(userAllInfo.getUserLevel() != null){
                    userAllInfo.setUserLevelKey(userAllInfo.getUserLevel().getKey());
                }
                list.add(userAllInfo);
            }
            result.setRows(list);
            result.setNumber(page.getNumber()+1);
            result.setTotalPages(page.getTotalPages());
            result.setTotalElements(page.getTotalElements());
        }

        return result;
    }

    /**
     * 查询用户信息
     */
    @Override
    public List<UserViewVO> listUserInfo(UserVO vo){
        Iterable<User> iterable = userDao.findAll(getInputCondition(vo));
        Iterator<User> it = iterable.iterator();
        List<UserViewVO> list = new ArrayList<UserViewVO>();
        while (it.hasNext())
        {
            User entity = it.next();
            UserViewVO viewVO = new UserViewVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(entity, viewVO);
            list.add(viewVO);
        }
        return list;
    }

    /**
     * 删除用户信息
     * @param userVO
     * @param userId
     * @return
     */
    @Override
    public String deleteUserInfo(UserVO userVO,String userId){
        User user = userDao.findByIdAndEnableFlag(userVO.getId(),EnableFlag.Y);
        if(user == null){
            return ErrorCodeConst.USER_NOTNULL;
        }
        if(userVO.isPushFlag()){//启用用户
            if(user.getUserRealAuth() == UserRealAuth.NORMAL){
                user.setUserStatus(UserStatus.NORMAL);
            }else{
                user.setUserStatus(UserStatus.UNREALAUTH);
            }
        }else{//禁用用户
            user.setUserStatus(UserStatus.FREEZE);
            //获取token
            String token = cacheService.get(LoginConstants.PREFIX_TOKEN_USERNO + String.valueOf(user.getId()));
            //删除token
            cacheService.remove(LoginConstants.PREFIX_TOKEN + token);
        }
        user.setLastUpdateDate(new Date());
        user.setLastUpdateBy(userId);
        userDao.save(user);//改变用户状态
        return Constants.RETURN_SUCESS;
    }

    /**
     * 查询用户信息
     * @param userVO
     * @return
     */
    @Override
    public UserViewVO findUserInfo(UserVO userVO){
        UserViewVO viewVO = new UserViewVO();
        UserOrgClient userOrgClient = null;
        User user = null;
        if(userVO.getUserOrgId() != null && userVO.getUserOrgId() != 0L ){
            userOrgClient = userOrgClientDAO.findByIdAndEnableFlag(userVO.getUserOrgId(),EnableFlag.Y);
            if(userOrgClient != null){
                List<User> users = userDao.findByUserOrgIdAndEnableFlagAndUserAccountTypeNot(userOrgClient.getId(),EnableFlag.Y,UserAccountType.ORG_CHILD_ACCOUNT);
                if(!CollectionUtils.isEmpty(users)){
                    user = users.get(0);
                }
            }
        }else{
            user = userDao.findByIdAndEnableFlag(userVO.getId(),EnableFlag.Y);
            if(user == null){
                return null;
            }
            if(user.getUserOrgId() != null && user.getUserOrgId() != 0L){
                userOrgClient = userOrgClientDAO.findByIdAndEnableFlag(user.getUserOrgId(),EnableFlag.Y);
            }
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(user,viewVO);
        UserPointVO userPointVO = new UserPointVO();
        if(user != null){
            if(user.getUserRealAuth() != null){
                viewVO.setUserRealAuthKey(user.getUserRealAuth().getKey());
            }
            if(user.getSex() != null){
                viewVO.setSexValue(user.getSex().getValue());
            }
            if(user.getUserStatus() != null){
                viewVO.setUserStatusKey(user.getUserStatus().getKey());
            }
            if(user.getUserType() != null){
                viewVO.setUserTypeKey(user.getUserType().getKey());
            }
            if(user.getUserAccountType() != null){
                viewVO.setUserAccountTypeKey(user.getUserAccountType().getKey());
            }
            if(user.getUserDetailType() != null){
                viewVO.setUserDetailTypeKey(user.getUserDetailType().getKey());
            }
            //查询用户积分相关信息
            UserPoint point = userPointDAO.findByUserIdAndEnableFlag(user.getId(),EnableFlag.Y);

            if(point != null){
                BeanUtil.copyPropertiesIgnoreNullFilds(point,userPointVO);
                if(point.getUserLevel() != null){
                    userPointVO.setUserLevelKey(point.getUserLevel().getKey());
                }
            }
        }
        viewVO.setUserPoint(userPointVO);
        List<UserTagsVO> userTagsVOS = new ArrayList<>();
        List<UserOrgImageVO> userOrgImageVOS = new ArrayList<>();
        if(userOrgClient != null){
            List<UserOrgImage> userOrgImages = userOrgImageDAO.findByUserOrgIdAndEnableFlagOrderBySort(userOrgClient.getId(),EnableFlag.Y);
            if(userOrgImages != null && userOrgImages.size() > 0){
                userOrgImages.forEach(userOrgImage -> {
                    UserOrgImageVO userOrgImageVO = new UserOrgImageVO();
                    BeanUtil.copyPropertiesIgnoreNullFilds(userOrgImage,userOrgImageVO);
                    userOrgImageVOS.add(userOrgImageVO);
                });
            }
            List<UserTags> userTagss = userTagsDAO.findByUserOrgIdAndEnableFlagOrderBySort(userOrgClient.getId(),EnableFlag.Y);
            if(!CollectionUtils.isEmpty(userTagss)){
                userTagss.forEach(userTags -> {
                    UserTagsVO userTagsVO = new UserTagsVO();
                    BeanUtil.copyPropertiesIgnoreNullFilds(userTags,userTagsVO);
                    userTagsVOS.add(userTagsVO);
                });
            }
        }
        UserOrgClientVO userOrgClientVO = new UserOrgClientVO();
        BeanUtil.copyPropertiesIgnoreNullFilds(userOrgClient,userOrgClientVO);
        if(userOrgClientVO.getUserAccountType() != null) {
            userOrgClientVO.setUserAccountTypeKey(userOrgClientVO.getUserAccountTypeKey());
        }
        userOrgClientVO.setUserOrgImageVOS(userOrgImageVOS);
        userOrgClientVO.setUserTagsVOS(userTagsVOS);
        viewVO.setUserOrgClientVO(userOrgClientVO);
        return viewVO;
    }

    /**
     * 根据多种情况查询用户实名对象信息
     * 包括like：name
     * @param vo 用户对象（以积分表中用户对象的信息去进行条件查询）
     * @return
     */
    private Predicate getInputConditionUserRealName(UserVO vo)
    {
        List<BooleanExpression> predicates = new ArrayList<>();
        if(null != vo)
        {
            if(StringUtils.isNotBlank(vo.getName()))
            {
                predicates.add(QUserAuthRecord.userAuthRecord.user.name.like("%" + vo.getName() + "%"));
            }if(StringUtils.isNotBlank(vo.getMobile()))
        {
            predicates.add(QUserAuthRecord.userAuthRecord.user.mobile.like("%" + vo.getMobile() + "%"));
        }if(StringUtils.isNotBlank(vo.getRealName()))
        {
            predicates.add(QUserAuthRecord.userAuthRecord.realName.like("%" + vo.getRealName() + "%"));
        }
        }
        predicates.add(QUserAuthRecord.userAuthRecord.enableFlag.eq(EnableFlag.Y));

        return BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
    }

    /**
     * 查询用户信息
     * @param userVO
     * @return
     */
    @Override
    public List<Long> listUserId(UserVO userVO){
        Iterable<User> iterable = userDao.findAll(getInputCondition(userVO));
        Iterator<User> it = iterable.iterator();
        List<Long> list = new ArrayList<>();
        while (it.hasNext())
        {
            User entity = it.next();
            list.add(entity.getId());
        }
        return list;
    }

    @Override
    @Transactional
    public String deleteUserList(List<Long> ids,String userId){
        int rows = userDao.deleteUserList(ids,userId);//修改的条数
        return Constants.RETURN_SUCESS;
    }

    /**
     * 查询所有的账户信息
     * @return
     */
    @Override
    public List<UserOrgClientVO> findOrgList(){
        List<UserOrgClientVO> userOrgClientVOS = new ArrayList<>();
        //查询相应的账户的信息
        Iterable<UserOrgClient> iterable = userOrgClientDAO.findByUserAccountTypeAndEnableFlag(UserAccountType.ORG_ACCOUNT,EnableFlag.Y);
        Iterator<UserOrgClient> it = iterable.iterator();
        while(it.hasNext()){
            UserOrgClient userOrgClient = it.next();
            UserOrgClientVO userOrgClientVO = new UserOrgClientVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(userOrgClient,userOrgClientVO);
            userOrgClientVOS.add(userOrgClientVO);
        }
        return userOrgClientVOS;
    }

    /**
     * 分页查询所有的账户信息
     * @return
     */
    @Override
    public PageDTO<UserOrgClientVO> findOrgPage(PageRequest pageRequest,UserOrgClientVO vo){
        PageDTO<UserOrgClientVO> result = new PageDTO<>();
        List<UserOrgClientVO> userOrgClientVOS = new ArrayList<>();
        //查询相应的账户的信息
        Page<UserOrgClient> page = userOrgClientDAO.findAll(getInputConditionUserOrg(vo),pageRequest);
        for(UserOrgClient userOrgClient:page){
            UserOrgClientVO userOrgClientVO = new UserOrgClientVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(userOrgClient,userOrgClientVO);
            if(userOrgClientVO.getUserAccountType() != null){
                userOrgClientVO.setUserAccountTypeKey(userOrgClientVO.getUserAccountType().getKey());
            }
            List<User> users = userDao.findByUserOrgIdAndEnableFlag(userOrgClientVO.getId(),EnableFlag.Y);
            if(!CollectionUtils.isEmpty(users)){
                StringBuffer stringBuffer = new StringBuffer();
                users.forEach(user -> {
                    if(StringUtils.isNotBlank(user.getName())){
                        stringBuffer.append(user.getName());
                    }else{
                        stringBuffer.append(user.getMobile());
                    }
                    if(user.getUserDetailType() != null){
                        stringBuffer.append("(" + user.getUserDetailType().getKey() + ")" );
                    }
                    stringBuffer.append(",");
                });
                userOrgClientVO.setOrgClientUsers(stringBuffer.substring(0,stringBuffer.length() - 1));
            }
            List<UserTags> userTagss = userTagsDAO.findByUserOrgIdAndEnableFlagOrderBySort(userOrgClient.getId(),EnableFlag.Y);
            List<UserTagsVO> userTagsVOS = new ArrayList<>();
            if(userTagss != null){
                userTagss.forEach(userTags -> {
                    UserTagsVO userTagsVO = new UserTagsVO();
                    BeanUtil.copyPropertiesIgnoreNullFilds(userTags,userTagsVO);
                    userTagsVOS.add(userTagsVO);
                });
            }
            userOrgClientVO.setUserTagsVOS(userTagsVOS);
            userOrgClientVOS.add(userOrgClientVO);
        }
        result.setTotalElements(page.getTotalElements());
        result.setNumber(page.getNumber() + 1);
        result.setTotalPages(page.getTotalPages());
        result.setRows(userOrgClientVOS);
        return result;
    }

    @Override
    public UserOrgClientVO findUserOrgInfo(UserOrgClientVO userOrgClientVO){
        UserOrgClient userOrgClient = userOrgClientDAO.findByIdAndEnableFlag(userOrgClientVO.getId(),EnableFlag.Y);
        if(userOrgClient == null){
            return userOrgClientVO;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(userOrgClient,userOrgClientVO);
        if(userOrgClientVO.getUserAccountType() != null){
            userOrgClientVO.setUserAccountTypeKey(userOrgClientVO.getUserAccountType().getKey());
        }
        List<User> users = userDao.findByUserOrgIdAndEnableFlag(userOrgClientVO.getId(),EnableFlag.Y);
        if(!CollectionUtils.isEmpty(users)){
            StringBuffer stringBuffer = new StringBuffer();
            users.forEach(user -> {
                if(StringUtils.isNotBlank(user.getName())){
                    stringBuffer.append(user.getName());
                }else{
                    stringBuffer.append(user.getMobile());
                }
                if(user.getUserDetailType() != null){
                    stringBuffer.append("(" + user.getUserDetailType().getKey() + ")" );
                }
                stringBuffer.append(",");
            });
            userOrgClientVO.setOrgClientUsers(stringBuffer.substring(0,stringBuffer.length()-1));
        }
        List<UserTags> userTagss = userTagsDAO.findByUserOrgIdAndEnableFlagOrderBySort(userOrgClient.getId(),EnableFlag.Y);
        List<UserTagsVO> userTagsVOS = new ArrayList<>();
        if(userTagss != null){
            userTagss.forEach(userTags -> {
                UserTagsVO userTagsVO = new UserTagsVO();
                BeanUtil.copyPropertiesIgnoreNullFilds(userTags,userTagsVO);
                userTagsVOS.add(userTagsVO);
            });
        }
        List<UserOrgImageVO> userOrgImageVOS = new ArrayList<>();
        List<UserOrgImage> userOrgImages = userOrgImageDAO.findByUserOrgIdAndEnableFlagOrderBySort(userOrgClient.getId(),EnableFlag.Y);
        if(userOrgImages != null && userOrgImages.size() > 0){
            userOrgImages.forEach(userOrgImage -> {
                UserOrgImageVO userOrgImageVO = new UserOrgImageVO();
                BeanUtil.copyPropertiesIgnoreNullFilds(userOrgImage,userOrgImageVO);
                userOrgImageVOS.add(userOrgImageVO);
            });
        }
        userOrgClientVO.setUserOrgImageVOS(userOrgImageVOS);
        userOrgClientVO.setUserTagsVOS(userTagsVOS);
        return userOrgClientVO;
    }

    /**
     * 根据多种情况查询用户实名对象信息
     * 包括like：name
     * @param vo 用户对象（以积分表中用户对象的信息去进行条件查询）
     * @return
     */
    private Predicate getInputConditionUserOrg(UserOrgClientVO vo)
    {
        List<BooleanExpression> predicates = new ArrayList<>();
        if(null != vo)
        {
            if(StringUtils.isNotBlank(vo.getOrgName()))
            {
                predicates.add(QUserOrgClient.userOrgClient.orgName.like("%" + vo.getOrgName() + "%"));
            }if(vo.getUserAccountType() != null)
        {
            predicates.add(QUserOrgClient.userOrgClient.userAccountType.eq(vo.getUserAccountType()));
        }if(StringUtils.isNotBlank(vo.getAddress()))
        {
            predicates.add(QUserOrgClient.userOrgClient.address.like("%" + vo.getAddress() + "%"));
        }if(StringUtils.isNotBlank(vo.getMainOperating()))
        {
            predicates.add(QUserOrgClient.userOrgClient.mainOperating.like("%" + vo.getMainOperating() + "%"));
        }if(StringUtils.isNotBlank(vo.getOrgStockCap()))
        {
            predicates.add(QUserOrgClient.userOrgClient.orgStockCap.like("%" + vo.getOrgStockCap() + "%"));
        }if(StringUtils.isNotBlank(vo.getSaleAddress()))
        {
            predicates.add(QUserOrgClient.userOrgClient.saleAddress.like("%" + vo.getSaleAddress() + "%"));
        }
        }
        predicates.add(QUserOrgClient.userOrgClient.enableFlag.eq(EnableFlag.Y));
        return BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
    }

    /**
     * 设置用户账户信息
     * @param userVO
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public String pushUserAndOrg(UserVO userVO , String userId){
        //查询用户信息
        User user = userDao.findByIdAndEnableFlag(userVO.getId(),EnableFlag.Y);
        if(user == null){
            return ErrorCodeConst.USER_NOTNULL;
        }
        if(userVO.isPushFlag()){
            //推送
            return authenOrg(user,userId);
        }

        UserOrgClient userOrgClient = null;
        //用户数据库中组织号为空且未传入相应的组织号时
        if((user.getUserOrgId() == null || user.getUserOrgId() == 0L) && (userVO.getUserOrgId() == null || userVO.getUserOrgId() == 0L) ){
            //原先没有配置组织,新建组织（老用户）
            userOrgClient = initUserOrgClient(user,userId);
            userOrgClientDAO.save(userOrgClient);
        }else{
            userOrgClient = userOrgClientDAO.findByIdAndEnableFlag(user.getUserOrgId(),EnableFlag.Y);
        }
        //用户账号设置为组织账号
        if(userVO.getUserAccountType() !=  null && userVO.getUserAccountType() != UserAccountType.IND_MAIN_ACCOUNT && userVO.getUserAccountType() !=  user.getUserAccountType() ){
            if(user.getUserAccountType() == UserAccountType.IND_MAIN_ACCOUNT){
                if(userOrgClient.isPushFlag()){
                    //企业已经认证，不允许从个人账号变为组织账号类型
                    return ErrorCodeConst.ORG_CANNOT_PUSHED;
                }
                if(userVO.getUserAccountType() != UserAccountType.ORG_MAIN_ACCOUNT){
                    //如果是个体账号，则只能设置为组织主账号
                    logger.info("now the user's userAccountType is {}, the next userAccountType is {}",user.getUserAccountType(),userVO.getUserAccountType());
                    return ErrorCodeConst.IDY_ONLY_MAIN;
                }
            }
            if(userVO.getUserAccountType() == UserAccountType.ORG_MAIN_ACCOUNT){
                //如果修改为主账号的话，需查询它所在的组织是否已经有主账号了
                List<User> users = userDao.findByUserOrgIdAndEnableFlagAndUserAccountType(userOrgClient.getId(),EnableFlag.Y,UserAccountType.ORG_MAIN_ACCOUNT);
                if(!CollectionUtils.isEmpty(users)){
                    logger.info("now the main user id is{}",users.get(0).getId());
                    //存在即不修改
                    return ErrorCodeConst.ORG_OWN_MAIN;
                }
            }
            //用户输入的用户账号身份不为空时且为组织账号时
            user.setUserAccountType(userVO.getUserAccountType());
            userOrgClient.setUserAccountType(UserAccountType.ORG_ACCOUNT);
        }else if(userVO.getUserAccountType() !=  null && userVO.getUserAccountType() !=  user.getUserAccountType() ){//设置为个人账号类型
            if(user.getUserAccountType() != UserAccountType.IND_MAIN_ACCOUNT){
                //组织账号类型不能变为个体账号类型
                return ErrorCodeConst.ORG_CANNOT_IDY;
            }
            user.setUserAccountType(UserAccountType.IND_MAIN_ACCOUNT);
            userOrgClient.setUserAccountType(UserAccountType.IND_ACCOUNT);
        }
        //用户绑定组织
        boolean flag = (userVO.getUserOrgId() != null && userVO.getUserOrgId() != 0L && (user.getUserOrgId() == null || user.getUserOrgId() == 0L || userVO.getUserOrgId().compareTo(user.getUserOrgId()) != 0));
        if(flag){
            if(user.getUserAccountType() == UserAccountType.ORG_MAIN_ACCOUNT){
                logger.info(" now the org id is {}, the bound orgid is {}",user.getUserOrgId(),userVO.getUserOrgId());
                return ErrorCodeConst.MAIN_CANNOT_BOUND;
            }
            //查询原有的账号信息
            UserOrgClient userOrgClient1 = userOrgClientDAO.findByIdAndEnableFlag(user.getUserOrgId(),EnableFlag.Y);
            if(user.getUserAccountType() == UserAccountType.IND_MAIN_ACCOUNT){
                //绑定的组织原本为个体账号
                //删除原有的组织以及推送的es中
                if(user.getUserOrgId() != null){

                    //删除es中已经推送的组织
                    if(userOrgClient1.isPushFlag()){
                        //已经推送的情况下删除es中的数据
                        deleteESInfo(user,userOrgClient1);
                        userOrgClient1.setPushFlag(false);
                        userOrgClient1.setElasticId(null);
                    }
                    userOrgClient1.setEnableFlag(EnableFlag.N);
                    userOrgClient1.setLastUpdateDate(new Date());
                    userOrgClient1.setLastUpdateBy(userId);
                    userOrgClientDAO.save(userOrgClient1);
                }
            }else{
                //如果为组织子账号，则查询该账号是否是该组织的唯一账号且该组织已经推送
                if(userOrgClient1.isPushFlag()){
                    List<User> users = userDao.findByUserOrgIdAndEnableFlag(userOrgClient.getId(),EnableFlag.Y);
                    if(users != null && users.size() <= 1 && userOrgClient.isPushFlag()){
                        return ErrorCodeConst.ONLY_ONE;
                    }
                }
            }
            //绑定组织
            userOrgClient = userOrgClientDAO.findByIdAndEnableFlag(userVO.getUserOrgId(),EnableFlag.Y);
            user.setUserAccountType(UserAccountType.ORG_CHILD_ACCOUNT);
        }

        if(userOrgClient == null){
            return ErrorCodeConst.ERRPR_ORG_ISNULL;//组织号为空
        }
        user.setUserOrgId(userOrgClient.getId());
        //不推送，只设置用户账号类型和选择组织
        //修改用户和组织在数据库中的相关状态
        user.setLastUpdateBy(userId);
        user.setLastUpdateDate(new Date());
        userOrgClient.setLastUpdateDate(new Date());
        userOrgClient.setLastUpdateBy(userId);
        userDao.saveAndFlush(user);
        userOrgClientDAO.saveAndFlush(userOrgClient);
        //修改用户缓存
        updateUserInfoCache(String.valueOf(user.getId()),user);
        //修改组织缓存
        updateUserOrgInfoCache(userOrgClient);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 设置组织标签信息
     * @param userOrgClientVO
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public String setOrgTags(UserOrgClientVO userOrgClientVO, String userId){
        //更新es中的数据
        UserOrgClient userOrgClient = userOrgClientDAO.findByIdAndEnableFlag(userOrgClientVO.getId(),EnableFlag.Y);
        if(userOrgClient == null){
            return ErrorCodeConst.ERRPR_ORG_ISNULL;
        }
        Map<String,Object> updateEsOrgInfo = new HashedMap();
        List<UserTagsVO> userTagsVOS = userOrgClientVO.getUserTagsVOS();
        List<UserTags> listUserTags = userTagsDAO.findByUserOrgIdAndEnableFlagOrderBySort(userOrgClientVO.getId(),EnableFlag.Y);
        if(listUserTags != null && listUserTags.size() > 0){
            listUserTags.forEach(userTags -> {
                UserTagsVO userTagsVO1 = new UserTagsVO();
                boolean isExist = false;
                if(userTagsVOS != null && userTagsVOS.size() > 0){
                    for(UserTagsVO userTagsVO:userTagsVOS){
                        if(userTagsVO.getId() == userTags.getId() || StringUtils.equals(userTagsVO.getTagName(),userTags.getTagName()) ){
                            if(userTagsVO.getId() == null || userTagsVO.getId() == 0L){
                                userTagsVOS.remove(userTagsVO);
                            }
                            isExist = true;
                            BeanUtil.copyPropertiesIgnoreNullFilds(userTagsVO,userTagsVO1);
                            break;
                        }
                    }
                }

                if(!isExist){
                    //不存在则删除
                    userTags.setLastUpdateBy(userId);
                    userTags.setLastUpdateDate(new Date());
                    userTags.setEnableFlag(EnableFlag.N);
                    userTagsDAO.saveAndFlush(userTags);
                    if(StringUtils.equals(userTags.getTagName(),UserTagType.GYLJRHZK.getKey())){
                        //如果删除的标签为供应链金融，权重变为0
                        userOrgClient.setOrderWeight(0L);
                        userOrgClient.setLastUpdateDate(new Date());
                        userOrgClient.setLastUpdateBy(userId);
                        updateEsOrgInfo.put("orderWeight",userOrgClient.getOrderWeight());
                    }
                }else{
                    //存在则判断是否修改了信息
                    if(!StringUtils.equals(userTags.getTagName(),userTagsVO1.getTagName())){
                        userTags.setTagName(userTagsVO1.getTagName());
                    }
                    if(userTags.getTagLayoutType() != userTagsVO1.getTagLayoutType()) {
                        userTags.setTagLayoutType(userTagsVO1.getTagLayoutType());
                    }
                    if(userTags.getSort() != userTagsVO1.getSort()) {
                        userTags.setSort(userTagsVO1.getSort());
                    }
                    userTags.setLastUpdateBy(userId);
                    userTags.setLastUpdateDate(new Date());
                    userTagsDAO.saveAndFlush(userTags);
                }
            });
        }
        if(userTagsVOS != null && userTagsVOS.size() > 0){
            for(UserTagsVO userTagsVO:userTagsVOS){
                boolean flag = userTagsVO.getId() == null || userTagsVO.getId() == 0L && StringUtils.isNotBlank(userTagsVO.getTagName());
                if(flag){
                    //增加标签
                    UserTags userTags = new UserTags();
                    BeanUtil.copyPropertiesIgnoreNullFilds(userTagsVO,userTags);
                    userTags.setId(idGen.nextId());
                    userTags.setCreateDate(new Date());
                    userTags.setEnableFlag(EnableFlag.Y);
                    userTags.setCreateBy(userId);
                    userTags.setUserOrgId(userOrgClientVO.getId());
                    userTagsDAO.saveAndFlush(userTags);
                    if(StringUtils.equals(userTags.getTagName(),UserTagType.GYLJRHZK.getKey())){
                        //如果增加的标签为供应链金融，权重变为2
                        userOrgClient.setOrderWeight(2L);
                        userOrgClient.setLastUpdateDate(new Date());
                        userOrgClient.setLastUpdateBy(userId);
                        updateEsOrgInfo.put("orderWeight",userOrgClient.getOrderWeight());
                    }
                }
            }
        }
        userOrgClientDAO.save(userOrgClient);
        List<UserTags> listUserTags1 = userTagsDAO.findByUserOrgIdAndEnableFlagOrderBySort(userOrgClientVO.getId(),EnableFlag.Y);
        List<ESTagsInfoVO> listEsTagsInfo = new ArrayList<>();
        listUserTags1.forEach(userTags -> {
            ESTagsInfoVO esTagsInfoVO = new ESTagsInfoVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(userTags, esTagsInfoVO);
            listEsTagsInfo.add(esTagsInfoVO);
        });
        updateEsOrgInfo.put("orgTags",listEsTagsInfo);
        List<User> users = userDao.findByUserOrgIdAndEnableFlagAndUserAccountTypeNot(userOrgClient.getId(),EnableFlag.Y,UserAccountType.ORG_CHILD_ACCOUNT);
        if(!CollectionUtils.isEmpty(users)){
            User user = users.get(0);
            if(!updateEsOrgInfo.isEmpty() && userOrgClient.isPushFlag()){
                updateESInfo(user,userOrgClient,updateEsOrgInfo);
            }
        }
        return Constants.RETURN_SUCESS;
    }

    /**
     * 删除组织信息
     * @param userOrgClientVO
     * @param userId
     * @return
     */
    @Override
    public String deleteOrg(UserOrgClientVO userOrgClientVO, String userId){
        UserOrgClient userOrgClient = userOrgClientDAO.findByIdAndEnableFlag(userOrgClientVO.getId(),EnableFlag.Y);
        if(userOrgClient == null){
            return ErrorCodeConst.ERRPR_TAGS;
        }
        //如果组织账号已经推送，则取消认证
        if(userOrgClient.isPushFlag() && StringUtils.isNotBlank(userOrgClient.getElasticId())){
            List<User> users = userDao.findByUserOrgIdAndEnableFlagAndUserAccountTypeNot(userOrgClient.getId(),EnableFlag.Y,UserAccountType.ORG_CHILD_ACCOUNT);
            if(!CollectionUtils.isEmpty(users) && users.size() > 0){
                User user = users.get(0);
                //删除es中信息
                deleteESInfo(user,userOrgClient);
                //修改组织表中推送状态信息
                userOrgClient.setPushFlag(false);//设置为不推送
                userOrgClient.setElasticId(null);
                //删除该组织下的标签
                List<UserTags> userTagss = userTagsDAO.findByUserOrgIdAndEnableFlagOrderBySort(userOrgClient.getId(),EnableFlag.Y);
                if(!CollectionUtils.isEmpty(userTagss)){
                    userTagss.forEach(userTags -> {
                        userTags.setLastUpdateBy(userId);
                        userTags.setLastUpdateDate(new Date());
                        userTags.setEnableFlag(EnableFlag.N);
                        userTagsDAO.save(userTags);
                    });
                }
            }
        }
        userOrgClient.setLastUpdateDate(new Date());
        userOrgClient.setEnableFlag(EnableFlag.N);
        userOrgClient.setLastUpdateBy(userId);
        userOrgClientDAO.save(userOrgClient);
        //清除组织与用户的绑定关系
        List<User> userList = userDao.findByUserOrgIdAndEnableFlag(userOrgClient.getId(),EnableFlag.Y);
        if(!CollectionUtils.isEmpty(userList) && userList.size() > 0){
            userList.forEach(user1 -> {
                user1.setUserOrgId(null);
                user1.setUserAccountType(UserAccountType.IND_MAIN_ACCOUNT);
                user1.setLastUpdateBy(userId);
                user1.setLastUpdateDate(new Date());
                userDao.save(user1);
            });
        }
        return Constants.RETURN_SUCESS;
    }

    /**
     * 修改组织信息
     * @param userOrgClientVO
     * @param userId
     * @return
     */
    @Override
    public String updateOrg(UserOrgClientVO userOrgClientVO, String userId){
        UserOrgClient userOrgClient = userOrgClientDAO.findByIdAndEnableFlag(userOrgClientVO.getId(),EnableFlag.Y);
        if(userOrgClient == null){
            return ErrorCodeConst.ERRPR_TAGS;
        }
        Map<String,Object> updateEsOrgInfo = new HashedMap();
        if(StringUtils.isNotBlank(userOrgClientVO.getOrgName()) && !StringUtils.equals(userOrgClientVO.getOrgName(),userOrgClient.getOrgName())){
            userOrgClient.setOrgName(userOrgClientVO.getOrgName());
            updateEsOrgInfo.put("orgName",userOrgClient.getOrgName());
        }
        if(StringUtils.isNotBlank(userOrgClientVO.getAddress()) && !StringUtils.equals(userOrgClientVO.getAddress(),userOrgClient.getAddress())){
            userOrgClient.setAddress(userOrgClientVO.getAddress());
            updateEsOrgInfo.put("address",userOrgClient.getAddress());
        }
        if(StringUtils.isNotBlank(userOrgClientVO.getAddressDetail()) && !StringUtils.equals(userOrgClientVO.getAddressDetail(),userOrgClient.getAddressDetail())){
            userOrgClient.setAddressDetail(userOrgClientVO.getAddressDetail());
            updateEsOrgInfo.put("addressDetail",userOrgClient.getAddressDetail());
        }
        if(StringUtils.isNotBlank(userOrgClientVO.getMainOperating()) && !StringUtils.equals(userOrgClientVO.getMainOperating(),userOrgClient.getMainOperating())){
            userOrgClient.setMainOperating(userOrgClientVO.getMainOperating());
            updateEsOrgInfo.put("mainOperating",userOrgClient.getMainOperating());
        }
        if(StringUtils.isNotBlank(userOrgClientVO.getSaleAddress()) && !StringUtils.equals(userOrgClientVO.getSaleAddress(),userOrgClient.getSaleAddress())){
            userOrgClient.setSaleAddress(userOrgClientVO.getSaleAddress());
            updateEsOrgInfo.put("saleAddress",userOrgClient.getSaleAddress());
        }
        if(StringUtils.isNotBlank(userOrgClientVO.getOrgStockCap()) && !StringUtils.equals(userOrgClientVO.getOrgStockCap(),userOrgClient.getOrgStockCap())){
            userOrgClient.setOrgStockCap(userOrgClientVO.getOrgStockCap());
            updateEsOrgInfo.put("orgStockCap",userOrgClient.getOrgStockCap());
        }
        if(userOrgClientVO.getOrderWeight() != null && userOrgClientVO.getOrderWeight() != userOrgClient.getOrderWeight()){
            userOrgClient.setOrderWeight(userOrgClientVO.getOrderWeight());
            updateEsOrgInfo.put("orderWeight",userOrgClient.getOrderWeight());
        }
        userOrgClient.setLastUpdateDate(new Date());
        userOrgClient.setLastUpdateBy(userId);
        userOrgClientDAO.save(userOrgClient);
        //更新es中的数据
        List<User> users = userDao.findByUserOrgIdAndEnableFlagAndUserAccountTypeNot(userOrgClient.getId(),EnableFlag.Y,UserAccountType.ORG_CHILD_ACCOUNT);
        if(!CollectionUtils.isEmpty(users)){
            User user = users.get(0);
            if(!updateEsOrgInfo.isEmpty() && userOrgClient.isPushFlag()){
                updateESInfo(user,userOrgClient,updateEsOrgInfo);
            }
        }
        return Constants.RETURN_SUCESS;
    }

    /**
     * 查询组织下的所有用户信息
     * @param userVO
     * @return
     */
    @Override
    public List<UserViewVO> findUserByOrgId(UserVO userVO){
        List<UserViewVO> userVOS = new ArrayList<>();
        if(userVO.getUserOrgId() == null || userVO.getUserOrgId() == 0L){
            User user = userDao.findByIdAndEnableFlag(userVO.getId(),EnableFlag.Y);
            userVO.setUserOrgId(user.getUserOrgId());
        }
        if(userVO.getUserOrgId() != null && userVO.getUserOrgId() != 0L){
            List<User> users = userDao.findByUserOrgIdAndEnableFlag(userVO.getUserOrgId(),EnableFlag.Y);
            if(!CollectionUtils.isEmpty(users)){
                users.forEach(user -> {
                    UserViewVO userViewVO = new UserViewVO();
                    BeanUtil.copyPropertiesIgnoreNullFilds(user,userViewVO);
                    userVOS.add(userViewVO);
                });
            }

        }
        return userVOS;
    }

//    /**
//     * 我的关注(组织账号信息)
//     * @param userId
//     * @return
//     */
//    @Override
//    public PageDTO<UserAllInfo> findUserFocusOrg(Long userId,PageRequest pageRequest,UserDTO userDTO){
//        PageDTO<UserAllInfo> result = new PageDTO<>();
//        List<UserAllInfo> userVOS = new ArrayList<>();
//        String userOrgIds = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.ORG_SAVE);
//        if(StringUtils.isNotBlank(userOrgIds)){
//            String[] orgIds = userOrgIds.split(",");
//            if(orgIds != null  && orgIds.length > 0){
//                PageDTO<Object[]> objs = focusUserOrgClientDAO.findMyFocusF(CollectionUtils.arrayToList(orgIds),pageRequest,userDTO);
//                if(objs != null && !CollectionUtils.isEmpty(objs.getRows())){
//                    objs.getRows().forEach(objects -> {
//                        UserAllInfo userAllInfo = new UserAllInfo();
//                        userAllInfo.setUserOrgId(((BigInteger)objects[0]).longValue());
//                        userAllInfo.setOrgName((String)objects[1]);
//                        userAllInfo.setAddress((String)objects[2]);
//                        userAllInfo.setOrgFirstBannerUrl((String)objects[3]);
//                        if(objects.length > 4 && objects[4] != null) userAllInfo.setId(((BigInteger)objects[4]).longValue());
//                        if(objects.length > 5 && objects[5] != null) userAllInfo.setUserType(UserType.valueOf(UserType.class,String.valueOf(objects[5])));
//                        if(objects.length > 6 && objects[6] != null) userAllInfo.setUserRealAuth(UserRealAuth.valueOf(UserRealAuth.class,String.valueOf(objects[6])));
//                        if(userAllInfo.getUserRealAuth() != null){
//                            userAllInfo.setUserRealAuthKey(userAllInfo.getUserRealAuth().getKey());
//                        }if(userAllInfo.getUserType() != null){
//                            userAllInfo.setUserTypeKey(userAllInfo.getUserType().getKey());
//                        }
////                        List<UserTagsVO> userTagsVOS = new ArrayList<>();
////                        List<UserTags> userTagss = userTagsDAO.findByUserOrgIdAndEnableFlagOrderBySort(userAllInfo.getUserOrgId(),EnableFlag.Y);
////                        if(!CollectionUtils.isEmpty(userTagss)){
////                            userTagss.forEach(userTags -> {
////                                UserTagsVO userTagsVO = new UserTagsVO();
////                                BeanUtil.copyPropertiesIgnoreNullFilds(userTags,userTagsVO);
////                                userTagsVOS.add(userTagsVO);
////                            });
////                        }
////                        userAllInfo.setUserTagsVOS(userTagsVOS);
//                        userVOS.add(userAllInfo);
//                    });
//                }
//                result.setRows(userVOS);
//                result.setNumber(objs.getNumber());
//                result.setTotalPages(objs.getTotalPages());
//                result.setTotalElements(objs.getTotalElements());
//            }
//        }
//        return result;
//    }

    /**
     * 我的关注(组织账号信息)
     * @param userId
     * @return
     */
    @Override
    public PageDTO<UserAllInfo> findUserFocusOrg(Long userId,PageRequest pageRequest,UserDTO userDTO){
        PageDTO<UserAllInfo> result = new PageDTO<>();
        List<UserAllInfo> userAllInfos = new ArrayList<>();
        Page<UserOrgClient> userOrgClients = null;
        String userOrgIds = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.ORG_SAVE);
        if(StringUtils.isNotBlank(userOrgIds)){
            String[] orgIds = userOrgIds.split(",");
            List<Long> ids = new ArrayList<>();
            for(int i = 0; i < orgIds.length; i++){
                ids.add(NumberUtils.createLong(orgIds[i]));
            }
            List<Long> id = new ArrayList<>();
            if(orgIds != null && orgIds.length > 0){
                if(userDTO.getUserType() != null){
                    Iterable<BigInteger> objects = userDao.findUserOrgIdsByUserTypeAndUserOrgId(userDTO.getUserType().name(),ids);
                    Iterator<BigInteger> it = objects.iterator();
                    while(it.hasNext()){
                        Long orgId = it.next().longValue();
                        id.add(orgId);
                    }
                }else{
                    for(int i = 0; i < orgIds.length; i++){
                        id.add(NumberUtils.createLong(orgIds[i]));
                    }
                }
            }
            if(!CollectionUtils.isEmpty(id)){
                if(StringUtils.isBlank(userDTO.getAddress())){
                    userOrgClients = userOrgClientDAO.findByIdInAndEnableFlag(id,EnableFlag.Y,pageRequest);
                }else{
                    userOrgClients = userOrgClientDAO.findByIdInAndEnableFlagAndAddressLike(id,EnableFlag.Y, "%" + userDTO.getAddress() + "%",pageRequest);
                }
                Iterator<UserOrgClient> it = userOrgClients.iterator();
                while(it.hasNext()){
                    UserOrgClient userOrgClient = it.next();
                    UserAllInfo userAllInfo = new UserAllInfo();
                    List<User> users = userDao.findByUserOrgIdAndEnableFlagAndUserAccountTypeNot(userOrgClient.getId(),EnableFlag.Y,UserAccountType.ORG_CHILD_ACCOUNT);
                    if(users != null && users.size() > 0 ){
                        BeanUtil.copyPropertiesIgnoreNullFilds(users.get(0),userAllInfo);
                    }
                    if(userAllInfo.getUserType() != null){
                        userAllInfo.setUserTypeKey(userAllInfo.getUserType().getKey());
                    }
                    if(userAllInfo.getUserRealAuth() != null){
                        userAllInfo.setUserRealAuthKey(userAllInfo.getUserRealAuth().getKey());
                    }
                    BeanUtil.copyPropertiesIgnoreNullFilds(userOrgClient,userAllInfo,"id");
                    userAllInfo.setUserOrgId(userOrgClient.getId());
                    userAllInfo.setOrgAccountType(userOrgClient.getUserAccountType());
                    userAllInfos.add(userAllInfo);
                }
            }
        }
        result.setRows(userAllInfos);
        if(userOrgClients != null){
            result.setNumber(userOrgClients.getNumber() + 1);
            result.setTotalElements(userOrgClients.getTotalElements());
            result.setTotalPages(userOrgClients.getTotalPages());
        }
        return result;
    }

    /**
     * 用户解除绑定组织
     * @param userVO
     * @param userId
     * @return
     */
    @Override
    public String unBoundOrg(UserVO userVO,String userId){
        User user = userDao.findByIdAndEnableFlag(userVO.getId(),EnableFlag.Y);
        if(user == null){
            return ErrorCodeConst.USER_NOTNULL;
        }
        if(user.getUserAccountType() != UserAccountType.ORG_CHILD_ACCOUNT){
            return ErrorCodeConst.ONLY_CHILD_UNBOUND;
        }
        UserOrgClient userOrgClient = null;
        if(user.getUserOrgId() != null && user.getUserOrgId() != 0L){
            userOrgClient = userOrgClientDAO.findByIdAndEnableFlag(user.getUserOrgId(),EnableFlag.Y);
        }
        if(userOrgClient != null){
            List<User> users = userDao.findByUserOrgIdAndEnableFlag(userOrgClient.getId(),EnableFlag.Y);
            if(users != null && users.size() <= 1 && userOrgClient.isPushFlag()){
                return ErrorCodeConst.ONLY_ONE;
            }
        }
        user.setUserAccountType(UserAccountType.IND_MAIN_ACCOUNT);
        user.setUserOrgId(null);
        user.setLastUpdateDate(new Date());
        user.setLastUpdateBy(userId);
        userDao.saveAndFlush(user);
        if(userOrgClient != null){
            //组织不为空时修改组织内部人员在redis中存储的关系
            updateUserOrgInfoCache(userOrgClient);
        }
        return Constants.RETURN_SUCESS;
    }

    /**
     * 关闭认证
     * @return
     */
    @Override
    public String closeOrgPushFlag(UserVO userVO, String userId){
        User user = userDao.findByIdAndEnableFlag(userVO.getId(),EnableFlag.Y);
        if(user == null){
            //用户信息不能为空
            return ErrorCodeConst.USER_NOTNULL;
        }
        if(user.getUserAccountType() == UserAccountType.ORG_CHILD_ACCOUNT){
            //组织子账号不能取消账户信息的推送
            return ErrorCodeConst.ERROR_ORG_CHILDACCOUNT_EDITERR;
        }
        if(user.getUserOrgId() == null || user.getUserOrgId() == 0L){
            //用户所在的组织不能为空
            return ErrorCodeConst.ERRPR_ORG_ISNULL;
        }
        UserOrgClient userOrgClient = userOrgClientDAO.findByIdAndEnableFlag(user.getUserOrgId(),EnableFlag.Y);
        if(userOrgClient == null){
            //用户所在的组织不能为空
            return ErrorCodeConst.ERRPR_ORG_ISNULL;
        }
        if(!userOrgClient.isPushFlag() || StringUtils.isBlank(userOrgClient.getElasticId())){
            //组织并未推送，不需要取消
            return ErrorCodeConst.ORG_NOT_PUSHED;
        }
        //删除es中信息
        deleteESInfo(user,userOrgClient);
        //修改组织表中推送状态信息
        //设置为不推送
        userOrgClient.setPushFlag(false);
        userOrgClient.setElasticId(null);
        userOrgClient.setLastUpdateBy(userId);
        userOrgClient.setLastUpdateDate(new Date());
        userOrgClientDAO.save(userOrgClient);
        //删除该组织下的标签
        List<UserTags> userTagss = userTagsDAO.findByUserOrgIdAndEnableFlagOrderBySort(userOrgClient.getId(),EnableFlag.Y);
        if(!CollectionUtils.isEmpty(userTagss)){
            userTagss.forEach(userTags -> {
                userTags.setLastUpdateBy(userId);
                userTags.setLastUpdateDate(new Date());
                userTags.setEnableFlag(EnableFlag.N);
                userTagsDAO.save(userTags);
            });
        }
        return Constants.RETURN_SUCESS;
    }

    /**
     * 批量认证
     * @param userVO
     * @param userId
     * @return
     */
    @Transactional
    @Override
    public String batchAuthen(UserVO userVO, String userId){
        userVO.getIds().forEach(id -> {
            User user = userDao.findByIdAndEnableFlag(id,EnableFlag.Y);
            String result = authenOrg(user,userId);
            if(!StringUtils.equals(result,Constants.RETURN_SUCESS)){
                throw new BusinessException("{}", result);
            }
        });
        return Constants.RETURN_SUCESS;
    }

    @Transactional
    public String authenOrg(User user,String userId){
        if(user.getUserType() != UserType.DB && user.getUserType() != UserType.KS && user.getUserType() != UserType.LK){
            return ErrorCodeConst.ORG_PUSH_USERTYPE;
        }
        if(user.getUserType() == UserType.DB || user.getUserType() == UserType.LK){
            if(user.getUserDetailType() == null){
                return ErrorCodeConst.USERINFO_IMPERFECT;
            }
        }
        UserOrgClient userOrgClient = null;
        //推送
        //不为组织子账号
        if(user.getUserAccountType() != UserAccountType.ORG_CHILD_ACCOUNT){
            //查询用户的组织信息，并推送相关组织信息
            if(user.getUserOrgId() != null && user.getUserOrgId() != 0L){
                userOrgClient = userOrgClientDAO.findByIdAndEnableFlag(user.getUserOrgId(),EnableFlag.Y);
            }
            if(userOrgClient == null){
                userOrgClient = initUserOrgClient(user,userId);
                user.setUserOrgId(userOrgClient.getId());
            }
            //开关打开
            if(!userOrgClient.isPushFlag()){
                //原来开关为关的情况，现在才开起来
                userOrgClient.setPushFlag(true);
            }else{
                return ErrorCodeConst.ERRPR_ORG_ISPUSH;
            }
            //冷库推送时默认加入企业认证标签
            if(user.getUserType() == UserType.LK){
                List<UserTags> userTagss = userTagsDAO.findByUserOrgIdAndEnableFlagOrderBySort(userOrgClient.getId(),EnableFlag.Y);
                boolean isRZ = false;
                //原本没有设置标签的情况下
                if(userTagss != null && userTagss.size() > 0){
                    for(UserTags userTags:userTagss){
                        if(StringUtils.equals(userTags.getTagName(),UserTagType.QYRZ.getKey())){
                            isRZ = true;
                        }
                    }
                }
                if(!isRZ){
                    //没有经过企业认证则加入企业认证标签
                    UserTags userTags = new UserTags();
                    userTags.setTagName(UserTagType.QYRZ.getKey());
                    userTags.setClassName(UserTagType.QYRZ.name());
                    userTags.setSort(0);//默认排序第一
                    userTags.setTagLayoutType(TagLayoutType.FIRST_LAY);
                    userTags.setUserOrgId(userOrgClient.getId());
                    userTags.setCreateBy(userId);
                    userTags.setCreateDate(new Date());
                    userTags.setEnableFlag(EnableFlag.Y);
                    userTags.setId(idGen.nextId());
                    userTagsDAO.saveAndFlush(userTags);
                }
            }

            String result = pushESInfo(user,userOrgClient, null);//索引
            if(!StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return result;//推送失败
            }
        }else{//组织子账号
            //是否选择了有效的组织，并绑定
            return ErrorCodeConst.ERROR_ORG_CHILDACCOUNT_EDITERR;
        }
        //修改用户和组织在数据库中的相关状态
        userDao.saveAndFlush(user);
        userOrgClientDAO.saveAndFlush(userOrgClient);
        //修改用户缓存
        updateUserInfoCache(String.valueOf(user.getId()),user);
        //修改组织缓存
        updateUserOrgInfoCache(userOrgClient);
        return Constants.RETURN_SUCESS;
    }

}
