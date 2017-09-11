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
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.common.util.JsonUtil;
import com.apec.framework.common.util.SecurityUtils;
import com.apec.framework.dto.*;
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
            userInfo.setIdNumber("");
        }
        if(StringUtils.isBlank(user.getRealName())){
            userInfo.setRealName("");
        }
        cacheHashService.hset(RedisHashConstants.HASH_USER_PREFIX + userNo,RedisHashConstants.HASH_OBJCONTENT_CACHE, JsonUtil.toJSONString(userInfo));
    }

    /**
     * 更新用户组织缓存
     * @param userNo 用户组织ID
     * @param userOrgClient 用户组织对象
     */
    private void updateUserOrgInfoCache(String userNo,UserOrgClient userOrgClient){
//        String userInfoJson = cacheHashService.hget(RedisHashConstants.HASH_USER_ORG_PREFIX + userNo,RedisHashConstants.HASH_OBJCONTENT_CACHE);
//        UserOrgClientInfoVO userOrgClientInfoVO ;
//        if(StringUtils.isBlank(userInfoJson)){
//            //获取不到数据,记录日志
//            logger.warn("[UserServiceImpl][updateUserOrgInfoCache]Can't find user org hash cache. userNo:{}",userNo);
//            userInfo = new UserInfoVO();
//        }else{
//            userInfo = JsonUtil.parseObject(userInfoJson,UserInfoVO.class);
//        }
//        BeanUtil.copyPropertiesIgnoreNullFilds(user,userInfo);
//        userInfo.setUserTypeKey(user.getUserType() == null?"":user.getUserType().getKey());
//        if(StringUtils.isBlank(user.getIdNumber())){
//            userInfo.setIdNumber("");
//        }
//        if(StringUtils.isBlank(user.getRealName())){
//            userInfo.setRealName("");
//        }
//        cacheHashService.hset(RedisHashConstants.HASH_USER_PREFIX + userNo,RedisHashConstants.HASH_OBJCONTENT_CACHE, JsonUtil.toJSONString(userInfo));
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
        userOrgClient.setUserAccountType(UserAccountType.INIT_ACCOUNT);
        userOrgClientDAO.save(userOrgClient);
        user.setUserOrgId(userOrgClient.getId());
        user.setUserAccountType(UserAccountType.INIT_MAIN_ACCOUNT); //默认账号
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

    @Override
    @Transactional
    public String updateUserInfo(UserVO userVO,String userId, Map<String,String> resultMap) {
        //查詢用戶信息
        User user = userDao.findOne(userVO.getId());
        if(user == null){
            logger.info("[updateUserInfo] user is not exist id{}:",userVO.getId());
            return ErrorCodeConst.USER_NOTNULL;
        }
        if(StringUtils.isNotBlank(userVO.getImgUrl())){
            user.setImgUrl(userVO.getImgUrl());
            resultMap.put("imgUrl",user.getImgUrl());
        }
        if(userVO.getSex() != null) user.setSex(userVO.getSex());
        if(StringUtils.isNotBlank(userVO.getName())) user.setName(userVO.getName());
        if(userVO.getUserType() != null) user.setUserType(userVO.getUserType());
        if(userVO.getUserDetailType() != null) user.setUserDetailType(userVO.getUserDetailType());
        if(StringUtils.isNotBlank(userVO.getAddressDetail())) user.setAddressDetail(userVO.getAddressDetail());
        if(StringUtils.isNotBlank(userVO.getMainOperating())) user.setMainOperating(userVO.getMainOperating());
        if(StringUtils.isNotBlank(userVO.getWorkOfYear())) user.setWorkOfYear(userVO.getWorkOfYear());
        //子账号不能编辑组织信息
        if(user.getUserAccountType() != UserAccountType.ORG_CHILD_ACCOUNT){
            //获取用户组织
            UserOrgClient userOrgClient = null;
            if(user.getUserOrgId() != null) userOrgClient = userOrgClientDAO.findOne(user.getUserOrgId());
            if(userOrgClient == null){
                //用户组织的编号不存在则数据补足, ORG_CHILD_ACCOUNT 不能创建组织
                userOrgClient = new UserOrgClient();
                userOrgClient.setId(idGen.nextId());
                userOrgClient.setEnableFlag(EnableFlag.Y);
                userOrgClient.setCreateDate(new Date());
                user.setUserOrgId(userOrgClient.getId());
                if(user.getUserAccountType() == UserAccountType.ORG_MAIN_ACCOUNT){
                    //当后台设置旧用户为主账号True，则主账号修改个人资料编辑组织信息，创建组织，设为组织账号
                    userOrgClient.setUserAccountType(UserAccountType.ORG_ACCOUNT);  //组织账号
                }else if (user.getUserAccountType() == UserAccountType.IND_MAIN_ACCOUNT){
                    //旧用户设置为个人账号，个人主体账号
                    userOrgClient.setUserAccountType(UserAccountType.IND_ACCOUNT);  //个人账户
                }else{
                    userOrgClient.setUserAccountType(UserAccountType.INIT_ACCOUNT); //默认账户
                    user.setUserAccountType(UserAccountType.INIT_MAIN_ACCOUNT);  //默认账号
                }
            }
            //用户组织VO
            UserOrgClientVO userOrgClientVO = userVO.getUserOrgClientVO();
            List<UserOrgImage> listCacheOrgImage = new ArrayList<>();
            if(userOrgClientVO != null){
                BeanUtil.copyPropertiesIgnoreNullFilds(userOrgClientVO,userOrgClient,"elasticId","userOrgImageVOS","userAccountType");
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
                                        userOrgImageDAO.saveAndFlush(userOrgImage);
                                        listCacheOrgImage.add(userOrgImage);
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
                                listCacheOrgImage.add(userOrgImage);
                            }
                        }
                    }
                }
            }
            userOrgClient.setAttentionNum(0);
            userOrgClient.setViewNum(0);
            userOrgClient.setProductNum(0);
            userOrgClientDAO.saveAndFlush(userOrgClient);
            //更新组织缓存
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
        userLoginRecord.setUserId(user.getUserOrgId());

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
            resultMap.put("userId",String.valueOf(user.getId()));
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
        authRecord.setUserId(user.getId());
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
        if(authRecord.getUserId() == null){
            logger.info("[userRealNameEntrue] user id  is not exist，id:{}",authRecordVO.getId());
            return Constants.COMMON_ERROR_PARAMS;
        }
        User user = userDao.findOne(authRecord.getUserId());
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
            user = userDao.findOne(authRecord.getUserId());
            if(user != null){
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
    public List<com.apec.user.vo.UserViewVO> listUserInfo(UserVO vo){
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
        User user = userDao.findOne(userVO.getId());
        if(user == null){
            return null;
        }
        UserViewVO viewVO = new UserViewVO();
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
        //查询用户积分相关信息
        UserPoint point = userPointDAO.findByUserIdAndEnableFlag(user.getId(),EnableFlag.Y);
        UserPointVO userPointVO = new UserPointVO();
        BeanUtil.copyPropertiesIgnoreNullFilds(point,userPointVO);
        if(point.getUserLevel() != null){
            userPointVO.setUserLevelKey(point.getUserLevel().getKey());
        }
        viewVO.setUserPoint(userPointVO);
//        List<UserStrengthImgUrlVO> userStrengthImgUrlVOS = new ArrayList<>();
//        //查询用户实力描述上传相关图片
//        Iterable<UserStrengthImgUrl> userStrengthImgUrls = userStrengthImgUrlDAO.findByUserIdAndEnableFlagOrderBySort(user.getId(),EnableFlag.Y);
//        Iterator<UserStrengthImgUrl> it = userStrengthImgUrls.iterator();
//        while(it.hasNext()){
//            UserStrengthImgUrlVO userStrengthImgUrlVO = new UserStrengthImgUrlVO();
//            UserStrengthImgUrl userStrengthImgUrl = it.next();
//            BeanUtil.copyPropertiesIgnoreNullFilds(userStrengthImgUrl,userStrengthImgUrlVO);
//            userStrengthImgUrlVOS.add(userStrengthImgUrlVO);
//        }
       // viewVO.setUserStrengthImgUrlVOS(userStrengthImgUrlVOS);
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


}
