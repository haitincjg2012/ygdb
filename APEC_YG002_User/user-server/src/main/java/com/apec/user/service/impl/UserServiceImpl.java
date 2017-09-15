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

    @Autowired
    private CacheService cacheService;  //缓存服务

    @Autowired
    private CacheHashService cacheHashService;  //缓存服务

    @Autowired
    private SpringCloudClient springCloudClient;

    @Autowired
    private ApecESProducer apecESProducer;

    @Value("${region_requestAddress_url}")
    private String requestAddress;  //查询地址url

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
        userViewVO.setPoint(userPoint.getAvailablePoints());
        userViewVO.setUserLevelKey(userPoint.getUserLevel().getKey());
        userViewVO.setUserLevelName(userPoint.getUserLevel().name());
        userViewVO.setUserRealAuthKey(user.getUserRealAuth().getKey());
        userViewVO.setUserRealAuthName(user.getUserRealAuth().name());
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
        if(userOrgClient.getUserAccountType() == null || (!userOrgClient.isPushFlag())){
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
    private String pushESInfo(User user,UserOrgClient userOrgClient){
        //非空判断
        if(user == null) return ErrorCodeConst.USER_NOTNULL;
        if(userOrgClient == null)  return  ErrorCodeConst.ERRPR_ORG_ISNULL;
        if(!userOrgClient.isPushFlag() || user.getUserType() == null || userOrgClient.getUserAccountType() == null)
            return  Constants.COMMON_MISSING_PARAMS;
        ESOrgInfoVO esOrgInfoVO = new ESOrgInfoVO();
        BeanUtil.copyPropertiesIgnoreNullFilds(userOrgClient,esOrgInfoVO,"orgTags","orgId");
        esOrgInfoVO.setOrgId(userOrgClient.getId());

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
        switch (user.getUserType()){
            case LK:
                ESDepotOrgInfoVO esDepotOrgInfoVO = new ESDepotOrgInfoVO(esOrgInfoVO,userOrgClient.getOrgStockCap()
                        ,userOrgClient.getMainOperating());
                esPostResponseVO = apecESProducer.postESInfo(ESProducerConstants.INDEX_URL_DEPORT_ORG, JsonUtil.toJSONString(esDepotOrgInfoVO));
                break;
            case DB:
                ESAgencyInfoVO esAgencyInfoVO = new ESAgencyInfoVO(esOrgInfoVO,userOrgClient.getSaleAddress(),userOrgClient.getMainOperating(),user.getId());
                esPostResponseVO = apecESProducer.postESInfo(ESProducerConstants.INDEX_URL_DAIBAN_ORG, JsonUtil.toJSONString(esAgencyInfoVO));
                break;
            case KS:
                ESMerchantInfoVO esMerchantInfoVO = new ESMerchantInfoVO(esOrgInfoVO,userOrgClient.getMainOperating(),user.getId());
                esPostResponseVO = apecESProducer.postESInfo(ESProducerConstants.INDEX_URL_KESHAN_ORG, JsonUtil.toJSONString(esMerchantInfoVO));
                break;
        }
        if(StringUtils.isBlank(esPostResponseVO.getId())) {
            logger.error("[UserServiceImpl][PushESInfo] Can't get EsResponse ID !");
            throw new BusinessException(Constants.SYS_ERROR);
        }
        userOrgClient.setElasticId(esPostResponseVO.getId());
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
        if(user == null) return ErrorCodeConst.USER_NOTNULL;
        if(userOrgClient == null)  return  ErrorCodeConst.ERRPR_ORG_ISNULL;
        if(user.getUserType() == null || user.getUserAccountType() == UserAccountType.ORG_CHILD_ACCOUNT || (!userOrgClient.isPushFlag())
                || userOrgClient.getUserAccountType() == null || StringUtils.isBlank(userOrgClient.getElasticId()))
            return  Constants.COMMON_MISSING_PARAMS;
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

    @Override
    @Transactional
    public String addNewUser(UserVO userVO) {
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

        UserOrgClient userOrgClient = new UserOrgClient();
        userOrgClient.setId(idGen.nextId());
        userOrgClient.setEnableFlag(EnableFlag.Y);
        userOrgClient.setCreateDate(date);
        userOrgClient.setUserAccountType(UserAccountType.IND_ACCOUNT);
        userOrgClientDAO.save(userOrgClient);
        user.setUserOrgId(userOrgClient.getId());
        user.setUserAccountType(UserAccountType.IND_MAIN_ACCOUNT); //默认账号
        userDao.save(user);

        //初次登陆积分设置
        UserPointRecordVO userPointRecordVO = new UserPointRecordVO();
        userPointRecordVO.setId(idGen.nextId());
        userPointRecordVO.setPointRuleType(PointRuleType.REGISTER_LOGIN);//注册首次登陆
        List<Long> userId = new ArrayList<>();
        userId.add(user.getId());
        userPointRecordVO.setUserIds(userId);
        //添加积分
        mqProducerClient.sendConcurrently(MqTag.USER_POINT_TAG_INIT_POINT.getKey(),String.valueOf(userPointRecordVO.getId()),userPointRecordVO);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 初始化组织信息
     */
    public UserOrgClient initUserOrgClient(User user){
        //用户组织的编号不存在则数据补足, ORG_CHILD_ACCOUNT 不能创建组织
        UserOrgClient userOrgClient = new UserOrgClient();
        userOrgClient.setId(idGen.nextId());
        userOrgClient.setEnableFlag(EnableFlag.Y);
        userOrgClient.setCreateDate(new Date());
        userOrgClient.setAttentionNum(0);
        userOrgClient.setViewNum(0);
        userOrgClient.setProductNum(0);
        if(user.getUserAccountType() == UserAccountType.ORG_MAIN_ACCOUNT){
            //当后台设置旧用户为主账号True，则主账号修改个人资料编辑组织信息，创建组织，设为组织账号
            userOrgClient.setUserAccountType(UserAccountType.ORG_ACCOUNT);  //组织账号
        }else{
            userOrgClient.setUserAccountType(UserAccountType.IND_ACCOUNT); //默认账户
        }
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
        User user = userDao.findOne(userVO.getId());
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
                    userOrgClient = userOrgClientDAO.findOne(user.getUserOrgId());
                }
                //组织不存在则新建组织
                if(userOrgClient == null){
                    userOrgClient = initUserOrgClient(user);
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
        User user = userDao.findOne(userVO.getId());
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
            if(user.getUserOrgId() != null) userOrgClient = userOrgClientDAO.findOne(user.getUserOrgId());
            if(userOrgClient == null){
                userOrgClient = initUserOrgClient(user);
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
        User user = userDao.findOne(userVO.getId());
        if(user == null){
            logger.info("[updateUserInfo] user is not exist id{}:",userVO.getId());
            return ErrorCodeConst.USER_NOTNULL;
        }
        if(userVO.getSex() != null) user.setSex(userVO.getSex());

        if(StringUtils.isNotBlank(userVO.getName())) user.setName(userVO.getName());

        if(StringUtils.isNotBlank(userVO.getAddressDetail())) user.setAddressDetail(userVO.getAddressDetail());
        if(StringUtils.isNotBlank(userVO.getMainOperating())) user.setMainOperating(userVO.getMainOperating());
        if(StringUtils.isNotBlank(userVO.getWorkOfYear())) user.setWorkOfYear(userVO.getWorkOfYear());
        //子账号不能编辑组织信息
        if(user.getUserAccountType() != UserAccountType.ORG_CHILD_ACCOUNT){

            //获取用户组织
            UserOrgClient userOrgClient = null;
            if(user.getUserOrgId() != null) userOrgClient = userOrgClientDAO.findOne(user.getUserOrgId());
            if(userOrgClient == null){
                userOrgClient = initUserOrgClient(user);
                user.setUserOrgId(userOrgClient.getId());
                if(user.getUserAccountType() != UserAccountType.ORG_MAIN_ACCOUNT){
                    user.setUserAccountType(UserAccountType.IND_MAIN_ACCOUNT);  //默认账号
                }
            }

            if(!userOrgClient.isPushFlag()){//未推送时身份可以修改，已经推送后身份无法进行修改
                if(userVO.getUserType() != null) user.setUserType(userVO.getUserType());
                if(userVO.getUserDetailType() != null) user.setUserDetailType(userVO.getUserDetailType());
            }
            UserOrgClientVO userOrgClientVO = userVO.getUserOrgClientVO();
            //用户昵称不为空且与组织名称不相等
            if(StringUtils.isNotBlank(userVO.getName()) && !StringUtils.equals(userVO.getName(),userOrgClient.getOrgName())){
                //如果为个体账号
                if(user.getUserAccountType() == UserAccountType.IND_MAIN_ACCOUNT){
                    userOrgClient.setOrgName(userVO.getName());//组织名称为个人昵称
                    updateEsOrgInfo.put("orgName",userOrgClient.getOrgName());
                }
            }
            //用户组织VO

            if(userOrgClientVO != null){
                if(StringUtils.isNotBlank(userOrgClientVO.getOrgName()) && !StringUtils.equals(userOrgClientVO.getOrgName(),userOrgClient.getOrgName()) && user.getUserAccountType() != UserAccountType.IND_MAIN_ACCOUNT){
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
            if(userOrgClient.getUserAccountType() != UserAccountType.ORG_ACCOUNT) userOrgClient.setOrgName(user.getName());
            userOrgClient.setLastUpdateBy(userId);
            userOrgClient.setLastUpdateDate(new Date());
            userOrgClientDAO.saveAndFlush(userOrgClient);
            if(!updateEsOrgInfo.isEmpty() && userOrgClient.isPushFlag()){
                //更新组织缓存,es中的组织相关信息
                updateESInfo(user,userOrgClient,updateEsOrgInfo);
            }
        }
        String addId = "";
        if(StringUtils.isNotBlank(userVO.getProvinceId())){
            user.setProvinceId(userVO.getProvinceId());
            addId = user.getProvinceId();
        }
        if(StringUtils.isNotBlank(userVO.getCityId())){
            user.setCityId(userVO.getCityId());
            addId = user.getCityId();
        }
        if(StringUtils.isNotBlank(userVO.getAreaId())){
            user.setAreaId(userVO.getAreaId());
            addId = user.getAreaId();
        }
        if(StringUtils.isNotBlank(userVO.getTownId())){
            user.setTownId(userVO.getTownId());
            addId = user.getTownId();
        }
        String address = "";
        if(StringUtils.isNotBlank(addId)){
            //发送请求。获取地址信息
            Map<String,String> requestInfo = new HashMap<>();
            requestInfo.put("code",addId);
            ResultData resultData = null;
            String responseStr = springCloudClient.post(requestAddress,JsonUtil.toJSONString(requestInfo));
            resultData = JsonUtil.parseObject(responseStr, ResultData.class);
            if(resultData.isSucceed()){
                address = (String)resultData.getData();
                address = address.replaceAll("\\|","");
            }
        }
        if(StringUtils.isNotBlank(address))  user.setAddress(address);
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
        User user = userDao.findOne(userVO.getId());
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

            if(StringUtils.isNotBlank(loginFailCount))
                cacheService.remove(loginFailCountKey); //移除计数器

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
        UserVO userVO = authRecordVO.getUserVO();
        //查詢用戶信息
        User user = userDao.findOne(userVO.getId());
        if(user == null || user.getId() == null || user.getId() == 0L){
            logger.info("[userRealNameApply] user is not exist id{}:",userVO.getId());
            return ErrorCodeConst.USER_NOTNULL;
        }
        if(StringUtils.equals(user.getUserRealAuth().name(),UserRealAuth.AUDITING.name()) || StringUtils.equals(user.getUserRealAuth().name(),UserRealAuth.NORMAL.name())){
            //用户已经在实名认证通过或正在实名认证审批中，不予再次提交实名认证信息
            logger.info("[userRealNameApply] user had realName query ,the userId : {}",userVO.getId());
            return ErrorCodeConst.HAD_REALNAME;
        }
        //验证身份证的唯一性
        if(isExistIdNumber(userVO)){
            //如果该身份证已经实名认证通过
            logger.info("[userRealNameApply] the idNumber had use for realName ,the idNumber is : {}",userVO.getId());
            return ErrorCodeConst.IDNUMBER_ISEXIST;
        }
        //添加到用户实名认证申请记录中
        UserAuthRecord authRecord = new UserAuthRecord();
        authRecord.setImgOneURL(authRecordVO.getImgOneURL());
        authRecord.setImgTwoURL(authRecordVO.getImgTwoURL());
        authRecord.setUser(user);
        authRecord.setId(idGen.nextId());
        authRecord.setCreateBy(userId);
        authRecord.setCreateDate(new Date());
        authRecord.setEnableFlag(EnableFlag.Y);
        userAuthRecordDAO.save(authRecord);
        //修改用户信息
        user.setRealName(userVO.getRealName());
        resultMap.put("realName",user.getRealName());
        user.setIdNumber(userVO.getIdNumber());
        resultMap.put("idNumber",user.getIdNumber());
        user.setLastUpdateBy(userId);
        user.setLastUpdateDate(new Date());
        //用户信息是否已经修改了性别
        if(user.getSex() == null){
            //没有修改过性别则按照输入的身份证信息最后一位来判定
            String idNumber = userVO.getIdNumber();
            String sex = idNumber.charAt(userVO.getIdNumber().length() - 1)+"";//15位身份证号最后一位为性别位
            if(idNumber.length() == 18){
                sex = idNumber.charAt(userVO.getIdNumber().length() - 2)+"";//18位身份证性别为倒数第二位
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
        user.setUserRealAuth(UserRealAuth.AUDITING);//状态为实名认证中
        //用户缓存变更
        UserPoint userPoint = userPointDAO.findByUserIdAndEnableFlag(user.getId(),EnableFlag.Y);
        updateUserCache(String.valueOf(user.getId()),userPoint,user);
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
        UserAuthRecord authRecord = userAuthRecordDAO.findOne(authRecordVO.getId());
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
        User user = userDao.findOne(authRecord.getUser().getId());
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
            //认证通过
            user.setUserRealAuth(UserRealAuth.NORMAL);
            user.setUserStatus(UserStatus.NORMAL);
            //给用户增加积分
            UserPointRecordVO userPointRecordVO = new UserPointRecordVO();
            userPointRecordVO.setPointRuleType(PointRuleType.VERIFIED_INFO);//加积分的类型，实名认证成功
            userPointRecordVO.setId(idGen.nextId());
            List<Long> userIds = new ArrayList<>();
            userIds.add(user.getId());
            userPointRecordVO.setUserIds(userIds);
            //实名认证加积分
            mqProducerClient.sendConcurrently(MqTag.USER_POINT_TAG_SUBSCRI_POINT.getKey(),String.valueOf(userPointRecordVO.getId()),userPointRecordVO);
            //设置实名成功的消息模板以及相应的内容值
            messageBodyVO.setTemplateKey(MessageTemplate.USER_REALNAME_SUCCESS);
            contentMap.put("userName",user.getName());

            //认证通过，清除缓存
            String userInfoRedisKey = RedisHashConstants.HASH_USER_PREFIX + user.getId();
            cacheHashService.hdelField(userInfoRedisKey, SysBusinessConstants.PREFIX_CREATEPRODUCT_NUM);

        }else{
            //认证不通过，清空上次申请的信息
            user.setIdNumber(null);
            user.setRealName("");

            //设置实名失败的消息模板以及相应的内容值
            messageBodyVO.setTemplateKey(MessageTemplate.USER_REALNAME_FAIL);
            contentMap.put("userName",user.getName());
            contentMap.put("failCaused",StringUtils.isBlank(authRecord.getRemark())?"":authRecord.getRemark());

        }
        messageBodyVO.setContentMap(contentMap);
        user.setLastUpdateDate(new Date());
        user.setLastUpdateBy(userId);
        userAuthRecordDAO.save(authRecord);//修改实名认证记录中状态
        userDao.save(user);//修改用户信息中实名认证状态
        //用户缓存变更
        UserPoint userPoint = userPointDAO.findByUserIdAndEnableFlag(user.getId(),EnableFlag.Y);
        updateUserCache(String.valueOf(user.getId()),userPoint,user);
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

        messageVO.setReceivers(receivers);//接收者
        messageVO.setMessageStatus(MessageStatus.NEW);//用户状态
        mqProducerClient.sendConcurrently(MqTag.MESSAGE_TAG.getKey(),String.valueOf(messageVO.getId()),messageVO);
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
    @Transactional
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
        User user = userDao.findOne(userVO.getId());
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
     * @param userVO
     * @return
     */
    private boolean isExistIdNumber(UserVO userVO){
        Long c = userDao.countByIdNumberAndEnableFlag(userVO.getIdNumber(),EnableFlag.Y);
        return c > 0;//如果该身份证号下的有效用户大于0，则代表该身份证已经被用户使用认证
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
            if(!StringUtils.isBlank(vo.getName()))
            {
                predicates.add(QUser.user.name.like("%" + vo.getName() + "%"));
            }
            if(!StringUtils.isBlank(vo.getRealName()))
            {
                predicates.add(QUser.user.realName.like("%" + vo.getRealName() + "%"));
            }
            if(!StringUtils.isBlank(vo.getMobile()))
            {
                predicates.add(QUser.user.mobile.eq(vo.getMobile()));
            }
//            if(!StringUtils.isBlank(vo.getCompanyName()))
//            {
//                predicates.add(QUser.user.companyName.like("%" + vo.getCompanyName() + "%"));
//            }
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
        if (null != oldToken)
            cacheService.remove(LoginConstants.PREFIX_TOKEN + oldToken);
        //UserNo Token
        cacheService.add(LoginConstants.PREFIX_TOKEN + token, userNo, tokenOutTime * 24 * 60); // (token , userNo)
        // 保存token 到redis
        cacheService.add(LoginConstants.PREFIX_TOKEN_USERNO + userNo, token, tokenOutTime * 24 * 60); // (userNo , token)

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
//        String userName = "";
//        if(dto != null) {
//            userName = dto.getName();
//        }
//        userName = "%" + userName + "%";//like
//        Page<UserAuthRecord> page = userAuthRecordDAO.findByUser_nameLikeAndEnableFlagOrderBySuccess(userName,EnableFlag.Y,pageRequest);
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
                //将用户的额身份证号和名称等信息放入申请记录中
                BeanUtil.copyPropertiesIgnoreNullFilds(user,authRecordVO);
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
    public PageDTO<UserViewVO> pageUserInfo(UserDTO dto, PageRequest pageRequest){
        PageDTO<UserViewVO> result = new PageDTO<>();
        List<UserViewVO> list = new ArrayList<>();
        UserVO vo = new UserVO();
        BeanUtil.copyPropertiesIgnoreNullFilds(dto,vo);
        Page<User> page = userDao.findAll(getInputCondition(vo),pageRequest);
        for(User user:page){
            UserViewVO viewVO = new com.apec.user.vo.UserViewVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(user,viewVO);
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
            if(user.getUserOrgId() != null && user.getUserOrgId() != 0L){
                UserOrgClient userOrgClient = userOrgClientDAO.findOne(user.getUserOrgId());
                UserOrgClientVO userOrgClientVO = new UserOrgClientVO();
                BeanUtil.copyPropertiesIgnoreNullFilds(userOrgClient,userOrgClientVO);
                viewVO.setUserOrgClientVO(userOrgClientVO);
            }
            list.add(viewVO);
        }
        result.setRows(list);
        result.setNumber(page.getNumber()+1);
        result.setTotalPages(page.getTotalPages());
        result.setTotalElements(page.getTotalElements());
        return result;
    }

    /**
     * 查询用户信息
     */
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
    public String deleteUserInfo(UserVO userVO,String userId){
        User user = userDao.findOne(userVO.getId());
        if(user == null){
            return ErrorCodeConst.USER_NOTNULL;
        }
        user.setEnableFlag(EnableFlag.N);
        user.setLastUpdateDate(new Date());
        user.setLastUpdateBy(userId);
        userDao.save(user);//删除用户信息
        //去除缓存中的用户信息
        cacheHashService.del(RedisHashConstants.HASH_USER_PREFIX + user.getId());
        return Constants.RETURN_SUCESS;
    }

    /**
     * 查询用户信息
     * @param userVO
     * @return
     */
    public UserViewVO findUserInfo(UserVO userVO){
        UserViewVO viewVO = new UserViewVO();
        UserOrgClient userOrgClient = null;
        User user;
        if(userVO.getUserOrgId() != null && userVO.getUserOrgId() != 0L ){
            userOrgClient = userOrgClientDAO.findOne(userVO.getUserOrgId());
            List<User> users = userDao.findByUserOrgIdAndEnableFlagAndUserAccountTypeNot(userOrgClient.getId(),EnableFlag.Y,UserAccountType.ORG_CHILD_ACCOUNT);
            user = users.get(0);
        }else{
            user = userDao.findOne(userVO.getId());
            if(user == null){
                return null;
            }
            if(user.getUserOrgId() != null && user.getUserOrgId() != 0L){
                userOrgClient = userOrgClientDAO.findOne(user.getUserOrgId());
            }
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(user,viewVO);
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
        UserPointVO userPointVO = new UserPointVO();
        BeanUtil.copyPropertiesIgnoreNullFilds(point,userPointVO);
        if(point.getUserLevel() != null){
            userPointVO.setUserLevelKey(point.getUserLevel().getKey());
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
            }if(StringUtils.isNotBlank(vo.getRealName()))
        {
            predicates.add(QUserAuthRecord.userAuthRecord.user.realName.like("%" + vo.getRealName() + "%"));
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
        for(Long id:ids){
            //去除缓存中的用户信息
            cacheHashService.del(RedisHashConstants.HASH_USER_PREFIX + id);
        }
        return Constants.RETURN_SUCESS;
    }

    /**
     * 查询所有的账户信息
     * @return
     */
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
    public PageDTO<UserOrgClientVO> findOrgPage(PageRequest pageRequest,UserOrgClientVO vo){
        PageDTO<UserOrgClientVO> result = new PageDTO<>();
        List<UserOrgClientVO> userOrgClientVOS = new ArrayList<>();
        //查询相应的账户的信息
        Page<UserOrgClient> page = userOrgClientDAO.findAll(getInputConditionUserOrg(vo),pageRequest);
        for(UserOrgClient userOrgClient:page){
            UserOrgClientVO userOrgClientVO = new UserOrgClientVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(userOrgClient,userOrgClientVO);
            List<UserTags> userTagss = userTagsDAO.findByUserOrgIdAndEnableFlagOrderBySort(userOrgClient.getId(),EnableFlag.Y);
            List<UserTagsVO> userTagsVOS = new ArrayList<>();
            userTagss.forEach(userTags -> {
                UserTagsVO userTagsVO = new UserTagsVO();
                BeanUtil.copyPropertiesIgnoreNullFilds(userTags,userTagsVO);
                userTagsVOS.add(userTagsVO);
            });
            userOrgClientVO.setUserTagsVOS(userTagsVOS);
            userOrgClientVOS.add(userOrgClientVO);
        }
        result.setTotalElements(page.getTotalElements());
        result.setNumber(page.getNumber() + 1);
        result.setTotalPages(page.getTotalPages());
        result.setRows(userOrgClientVOS);
        return result;
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
        User user = userDao.findOne(userVO.getId());
        if(user == null){
            return ErrorCodeConst.USER_NOTNULL;
        }
        UserOrgClient userOrgClient = null;
        //用户数据库中组织号为空且未传入相应的组织号时
        if((user.getUserOrgId() == null || user.getUserOrgId() == 0L) && (userVO.getUserOrgId() == null || userVO.getUserOrgId() == 0L) ){
            //原先没有配置组织,新建组织（老用户）
            userOrgClient = initUserOrgClient(user);
        }else if((userVO.getUserOrgId() != null && userVO.getUserOrgId() != 0L)){
            userOrgClient = userOrgClientDAO.findOne(userVO.getUserOrgId());
        }else{
            userOrgClient = userOrgClientDAO.findOne(user.getUserOrgId());
        }
        if(userOrgClient == null){
            return ErrorCodeConst.ERRPR_ORG_ISNULL;//组织号为空
        }
        user.setUserOrgId(userOrgClient.getId());
        ///如果身份有改变，修改用户身份
        if(userVO.getUserType() != null && !userOrgClient.isPushFlag()) user.setUserType(userVO.getUserType());
        if(userVO.getUserDetailType() != null && !userOrgClient.isPushFlag()) user.setUserDetailType(userVO.getUserDetailType());

        if(userVO.getUserAccountType() !=  null && userVO.getUserAccountType() != UserAccountType.IND_MAIN_ACCOUNT){
            if(userVO.getUserAccountType() == UserAccountType.ORG_MAIN_ACCOUNT){
                //如果修改为主账号的话，需查询它所在的组织是否已经有主账号了
                List<User> users = userDao.findByUserOrgIdAndEnableFlagAndUserAccountType(userOrgClient.getId(),EnableFlag.Y,UserAccountType.ORG_MAIN_ACCOUNT);
                if(users != null || users.size() > 0){
                    //存在即不修改
                    return ErrorCodeConst.ORG_OWN_MAIN;
                }
            }
            //用户输入的用户账号身份不为空时且为组织账号时
            user.setUserAccountType(userVO.getUserAccountType());
            userOrgClient.setUserAccountType(UserAccountType.ORG_ACCOUNT);
        }else if(userVO.getUserAccountType() !=  null){
            user.setUserAccountType(userVO.getUserAccountType());
            userOrgClient.setUserAccountType(UserAccountType.IND_ACCOUNT);
        }
        if(!userVO.isPushFlag()){//不推送，只设置用户账号类型和选择组织
            //修改用户和组织在数据库中的相关状态
            userDao.saveAndFlush(user);
            userOrgClientDAO.saveAndFlush(userOrgClient);
            //修改用户缓存
            updateUserInfoCache(String.valueOf(user.getId()),user);
            //修改组织缓存
            updateUserOrgInfoCache(userOrgClient);
            return Constants.RETURN_SUCESS;
        }
        //推送
        if(user.getUserAccountType() != UserAccountType.ORG_CHILD_ACCOUNT){//不为组织子账号
            //查询用户的组织信息，并推送相关组织信息
            if(user.getUserOrgId() == null || user.getUserOrgId() == 0L){
                return ErrorCodeConst.ERRPR_ORG_ISNULL;
            }
            userOrgClient = userOrgClientDAO.findOne(user.getUserOrgId());
            //开关打开
            if(!userOrgClient.isPushFlag()){//原来开关为关的情况，现在才开起来
                userOrgClient.setPushFlag(true);
            }else{
                return ErrorCodeConst.ERRPR_ORG_ISPUSH;
            }
            if(user.getUserType() == UserType.LK){//冷库推送时默认加入企业认证标签
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
            String result = pushESInfo(user,userOrgClient);
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
        UserOrgClient userOrgClient = userOrgClientDAO.findOne(userOrgClientVO.getId());
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
                        if(userTagsVO.getId() == userTags.getId()){
                            isExist = true;
                            BeanUtil.copyPropertiesIgnoreNullFilds(userTagsVO,userTagsVO1);
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
                        updateEsOrgInfo.put("orderWeight",userOrgClient.getOrderWeight());
                    }
                }else{
                    //存在则判断是否修改了信息
                    if(!StringUtils.equals(userTags.getTagName(),userTagsVO1.getTagName())) userTags.setTagName(userTagsVO1.getTagName());
                    if(userTags.getTagLayoutType() != userTagsVO1.getTagLayoutType()) userTags.setTagLayoutType(userTagsVO1.getTagLayoutType());
                    if(userTags.getSort() != userTagsVO1.getSort()) userTags.setSort(userTagsVO1.getSort());
                    userTags.setLastUpdateBy(userId);
                    userTags.setLastUpdateDate(new Date());
                    userTagsDAO.saveAndFlush(userTags);
                }
            });
        }
        if(userTagsVOS != null && userTagsVOS.size() > 0){
            for(UserTagsVO userTagsVO:userTagsVOS){
                if(userTagsVO.getId() == null || userTagsVO.getId() == 0L && StringUtils.isNotBlank(userTagsVO.getTagName())){
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
                        updateEsOrgInfo.put("orderWeight",userOrgClient.getOrderWeight());
                    }
                }
            }
        }

        List<UserTags> listUserTags1 = userTagsDAO.findByUserOrgIdAndEnableFlagOrderBySort(userOrgClientVO.getId(),EnableFlag.Y);
        List<ESTagsInfoVO> listEsTagsInfo = new ArrayList<>();
        listUserTags1.forEach(userTags -> {
            ESTagsInfoVO esTagsInfoVO = new ESTagsInfoVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(userTags, esTagsInfoVO);
            listEsTagsInfo.add(esTagsInfoVO);
        });
        updateEsOrgInfo.put("orgTags",listEsTagsInfo);
        List<User> users = userDao.findByUserOrgIdAndEnableFlagAndUserAccountTypeNot(userOrgClient.getId(),EnableFlag.Y,UserAccountType.ORG_CHILD_ACCOUNT);
        if(users != null && users.size() > 0){
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
    public String deleteOrg(UserOrgClientVO userOrgClientVO, String userId){
        UserOrgClient userOrgClient = userOrgClientDAO.findOne(userOrgClientVO.getId());
        if(userOrgClient == null){
            return ErrorCodeConst.ERRPR_TAGS;
        }
        userOrgClient.setLastUpdateDate(new Date());
        userOrgClient.setEnableFlag(EnableFlag.N);
        userOrgClient.setLastUpdateBy(userId);
        userOrgClientDAO.save(userOrgClient);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 修改组织信息
     * @param userOrgClientVO
     * @param userId
     * @return
     */
    public String updateOrg(UserOrgClientVO userOrgClientVO, String userId){
        UserOrgClient userOrgClient = userOrgClientDAO.findOne(userOrgClientVO.getId());
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
        if(users != null && users.size() > 0){
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
    public List<UserViewVO> findUserByOrgId(UserVO userVO){
        List<UserViewVO> userVOS = new ArrayList<>();
        if(userVO.getUserOrgId() == null || userVO.getUserOrgId() == 0L){
            User user = userDao.findOne(userVO.getId());
            userVO.setUserOrgId(user.getUserOrgId());
        }
        if(userVO.getUserOrgId() != null && userVO.getUserOrgId() != 0L){
            List<User> users = userDao.findByUserOrgIdAndEnableFlag(userVO.getUserOrgId(),EnableFlag.Y);
            users.forEach(user -> {
                UserViewVO userViewVO = new UserViewVO();
                BeanUtil.copyPropertiesIgnoreNullFilds(user,userViewVO);
                userVOS.add(userViewVO);
            });
        }
        return userVOS;
    }

}
