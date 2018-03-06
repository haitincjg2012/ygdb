package com.apec.user.service.impl;

import com.apec.framework.cache.CacheHashService;
import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.RedisHashConstants;
import com.apec.framework.common.enums.MqTag;
import com.apec.framework.common.enums.Realm;
import com.apec.framework.common.enumtype.*;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.dto.UserInfoVO;
import com.apec.framework.dto.UserViewVO;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.rockmq.client.MqProducerClient;
import com.apec.framework.rockmq.vo.MessageBodyVO;
import com.apec.framework.rockmq.vo.MessageVO;
import com.apec.user.dao.*;
import com.apec.user.dto.UserDTO;
import com.apec.user.model.*;
import com.apec.user.service.UserPointRecordService;
import com.apec.user.service.UserService;
import com.apec.user.util.SnowFlakeKeyGen;
import com.apec.user.vo.*;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 *
 * Created by hmy on 2017/7/6.
 * @author hmy
 */
@Service
public class UserPointRecordServiceImpl implements UserPointRecordService {

    @Autowired
    private SnowFlakeKeyGen idGen;

    @InjectLogger
    private Logger logger;

    @Autowired
    private UserPointRecordDAO userPointRecordDAO;

    @Autowired
    private UserPointDAO userPointDAO;

    @Autowired
    private UserPointRuleDAO ruleDAO;

    @Autowired
    private UserLevelRuleDAO userLevelRuleDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private CacheHashService cacheHashService;

    @Autowired
    private MqProducerClient mqProducerClient;

    /**
     * 用户等级计算
     */
    private void updateUserLevel(UserPoint userPoint){
        UserLevelRule userLevelRule = userLevelRuleDAO.findFirstByPointLessThanEqualAndEnableFlagAndFrezzingIsFalseOrderByPointDesc(
                                                                            userPoint.getAvailablePoints(),EnableFlag.Y);
        if(userLevelRule != null){
            userPoint.setUserLevel(userLevelRule.getUserLevel());
        }
    }

    /**
     * 更新用户缓存
     * @param userId 用户id
     * @param userPoint 用户积分
     */
    private void updateUserCache(Long userId,UserPoint userPoint){
        //缓存更新异常,返回异常,数据回滚,Mq消息补偿机制重跑.
        String json = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.HASH_USER_CREATEUSERVIEW_INFO);
        UserViewVO userViewVO ;
        if(StringUtils.isBlank(json)){
            //获取不到数据,记录日志
            logger.warn("[UserPointRecordService][updateUserCache]Can't find user hash cache. userId:{}",userId);
            userViewVO = new UserViewVO();
        }else{
            userViewVO = BaseJsonUtil.parseObject(json,UserViewVO.class);
        }
        if(userPoint != null) {
            userViewVO.setPoint(userPoint.getAvailablePoints());
            userViewVO.setUserLevelKey(userPoint.getUserLevel().getKey());
            userViewVO.setUserLevelName(userPoint.getUserLevel().name());
        }
        cacheHashService.hset(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.HASH_USER_CREATEUSERVIEW_INFO, BaseJsonUtil.toJSONString(userViewVO));
    }

    private UserInfoVO getUserInfo(String userId){
        String userInfoJson = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.HASH_OBJCONTENT_CACHE);
        UserInfoVO userInfo = null;
        if(StringUtils.isBlank(userInfoJson)){
            //获取不到数据,记录日志
            logger.warn("[SocietyPostServiceImpl][getUserInfo]Can't find user hash cache. userNo:{}",userId);
            User user = userDAO.findByIdAndEnableFlag(NumberUtils.createLong(userId),EnableFlag.Y);
            BeanUtil.copyPropertiesIgnoreNullFilds(user,userInfo);
            userInfo.setUserId(user.getId());
            userInfo.setUserTypeKey(user.getUserType() == null?"":user.getUserType().getKey());
            cacheHashService.hset(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.HASH_OBJCONTENT_CACHE, BaseJsonUtil.toJSONString(userInfo));
        }else{
            userInfo = BaseJsonUtil.parseObject(userInfoJson,UserInfoVO.class);
        }
        return userInfo;
    }

    /**
     * 发送 积分变动的站内信
     * @param rule 用户积分规则
     * @param user 用户
     */
    private void sendMessageMqInfo(UserPointRule rule,User user){
        MessageBodyVO messageBodyVO = new MessageBodyVO();
        //系统通知
        messageBodyVO.setType(MessageType.NOTIFICATION);
        //使用动态模板发送
        messageBodyVO.setTemplateFlag(true);
        Map<String, String> contentMap = new HashMap<>(16);
        contentMap.put("point",String.valueOf(rule.getPointsChanged()));
        contentMap.put("remark",StringUtils.isBlank(rule.getRemark())?"":rule.getRemark());
        if(rule.getPointsChangedType() == PointsChangedType.PLUS){
            messageBodyVO.setTemplateKey(MessageTemplate.USER_POINT_ADD);
        }else{
            messageBodyVO.setTemplateKey(MessageTemplate.USER_POINT_REDUCE);
        }
        messageBodyVO.setRealm(Realm.POINT);
        messageBodyVO.setContentMap(contentMap);
        //发送站内信，通知用户积分变动消息
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
    }

    /**
     * 初始化积分
     * @param user 用户信息
     * @return 用户积分
     */
    @Transactional(rollbackFor = Exception.class)
    private UserPoint initUserPint(User user){
        UserPoint point = new UserPoint();
        point.setUserId(user.getId());
        point.setEnableFlag(EnableFlag.Y);
        point.setId(idGen.nextId());
        point.setCreateDate(new Date());
        point.setAvailablePoints(0);
        point.setUserLevel(UserLevel.QT);
        userPointDAO.saveAndFlush(point);
        return point;
    }

    /**
     * 初始化积分记录
     * @param user 用户信息
     * @param rule 积分规则
     * @param userId 用户id
     * @return 积分记录
     */
    private UserPointRecord initUserPointRecord(User user,UserPointRule rule,String userId){
        UserPointRecord userPointRecord = new UserPointRecord();
        userPointRecord.setId(idGen.nextId());
        userPointRecord.setUserId(user.getId());
        Integer pointsChanged = rule.getPointsChanged();
        if(PointsChangedType.REDUCTION == rule.getPointsChangedType()){
            pointsChanged = 0 - pointsChanged;
        }
        userPointRecord.setPointsChanged(pointsChanged);
        userPointRecord.setPointRuleType(rule.getPointRuleType());
        userPointRecord.setRemark(rule.getRemark());
        if(rule.getId() != null){
            userPointRecord.setRuleId(String.valueOf(rule.getId()));
        }
        userPointRecord.setCreateDate(new Date());
        userPointRecord.setCreateBy(userId);
        userPointRecord.setEnableFlag(EnableFlag.Y);
        return userPointRecord;
    }

    /**
     * 增加用户积分
     * @param userPointRecordVO 积分记录
     * @return 处理结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addUserPoint(UserPointRecordVO userPointRecordVO) {
        //查询积分规则表
        UserPointRule rule  ;
        List<UserPointRule> rules = ruleDAO.findByPointRuleTypeAndEnableFlagOrderByCreateDateDesc(userPointRecordVO.getPointRuleType(),EnableFlag.Y);
        if(rules == null || rules.size() <= 0){
            //积分规则表中不存在该条记录，则按该自定义积分规则来进行扣减分数
            if(userPointRecordVO.getPointsChanged() == null || userPointRecordVO.getPointsChangedType() == null){
                //不合理的逻辑，查询不到积分规则也没有传入相应的积分规则
                return ErrorCodeConst.NULL_POINTRULE;
            }
            rule = new UserPointRule();
            BeanUtil.copyPropertiesIgnoreNullFilds(userPointRecordVO,rule,"id");
        }else{
            rule = rules.get(0);
        }
        Integer pointsChanged = rule.getPointsChanged();
        if(PointsChangedType.REDUCTION == rule.getPointsChangedType()){
            pointsChanged = 0 - pointsChanged;
        }
        List<Long> userIds = userPointRecordVO.getUserIds();
        for(Long userId:userIds){
            if(userId == null || userId == 0L){
                logger.info("[UserPoint][addUserPoint] Can't add user point, userIds is null!");
                continue;
            }
            userDAO.flush();
            User user = userDAO.findByIdAndEnableFlag(userId,EnableFlag.Y);
            if(user == null){
                logger.info("[UserPoint][addUserPoint] Can't add user point, user is null! the userId is {}", userId);
                user = new User();
                user.setId(userId);
            }
            //查询当前用户积分信息
            UserPoint point = userPointDAO.findByUserIdAndEnableFlag(userId,EnableFlag.Y);
            if(point == null){
                //用户积分未曾初始化，则初始化用户积分
                logger.info("[UserPoint][addUserPoint] unit the user's point ,the userId is {}", userId);
                point = initUserPint(user);
            }
            //增加积分记录
            UserPointRecord userPointRecord = initUserPointRecord(user,rule,String.valueOf(userId));
            if(pointsChanged < 0){
                //本次为扣除积分，则记录为消费
                point.setLastConsumeTime(new Date());
            }
            //修改用户积分
            userPointDAO.updateUserPoints(pointsChanged,userId);
            //保存积分记录
            userPointRecordDAO.saveAndFlush(userPointRecord);

            //修改用户等级
            UserPoint userPoint = userPointDAO.findByUserIdAndEnableFlag(userId,EnableFlag.Y);
            userPoint.setLastConsumeTime(point.getLastConsumeTime());
            updateUserLevel(userPoint);
            userPointDAO.save(userPoint);
            //更新用户View缓存
            updateUserCache(userId, userPoint);
            //发送积分变动的站内信
            sendMessageMqInfo(rule,user);
        }
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String addUserPointRule(UserPointRuleVO ruleVO, String userId) {
        UserPointRule rule = new UserPointRule();
        BeanUtil.copyPropertiesIgnoreNullFilds(ruleVO,rule);
        rule.setId(idGen.nextId());
        rule.setCreateDate(new Date());
        rule.setCreateBy(userId);
        rule.setEnableFlag(EnableFlag.Y);
        ruleDAO.save(rule);
        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateUserPointRule(UserPointRuleVO ruleVO, String userId) {
        //查询相应的积分规则信息
        UserPointRule rule = ruleDAO.findByIdAndEnableFlag(ruleVO.getId(),EnableFlag.Y);
        if(rule == null){
            logger.warn("rule is not exist ruleId{}:" + ruleVO.getId());
            return Constants.COMMON_ERROR_PARAMS;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(ruleVO,rule);
        rule.setLastUpdateBy(userId);
        rule.setLastUpdateDate(new Date());
        ruleDAO.save(rule);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String deleteRulePoints(UserPointRuleVO ruleVO, String userId) {
        //查询相应的积分规则信息
        UserPointRule rule = ruleDAO.findByIdAndEnableFlag(ruleVO.getId(),EnableFlag.Y);
        if(rule == null){
            logger.warn("rule is not exist ruleId{}:" + ruleVO.getId());
            return Constants.COMMON_ERROR_PARAMS;
        }
        rule.setEnableFlag(EnableFlag.N);
        rule.setLastUpdateBy(userId);
        rule.setLastUpdateDate(new Date());
        ruleDAO.save(rule);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 分页查询积分规则
     * @param pageRequest 分页对象
     * @return 分页结果集
     */
    @Override
    public PageDTO<UserPointRuleVO> pageUserPointRule(PageRequest pageRequest) {
        Page<UserPointRule> page = ruleDAO.findAll(QUserPointRule.userPointRule.enableFlag.eq(EnableFlag.Y),pageRequest);
        PageDTO<UserPointRuleVO> userPointRuleVOPageDTO = new PageDTO<>();
        List<UserPointRuleVO> rules = new ArrayList<>();
        for(UserPointRule rule:page){
            UserPointRuleVO pointRuleVO = new UserPointRuleVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(rule,pointRuleVO);
            pointRuleVO.setPointsChangedTypeKey(rule.getPointsChangedType().getKey());
            rules.add(pointRuleVO);
        }
        userPointRuleVOPageDTO.setRows(rules);
        userPointRuleVOPageDTO.setNumber(page.getNumber()+1);
        userPointRuleVOPageDTO.setTotalPages(page.getTotalPages());
        userPointRuleVOPageDTO.setTotalElements(page.getTotalElements());
        return userPointRuleVOPageDTO;
    }

    @Override
    public List<UserPointRuleVO> listUserPointRule() {
        Iterable<UserPointRule> iterable = ruleDAO.findAll(QUserPointRule.userPointRule.enableFlag.eq(EnableFlag.Y));
        List<UserPointRuleVO> result = new ArrayList<>();
        iterable.forEach(userPointRule ->{
            UserPointRuleVO userPointRuleVO = new UserPointRuleVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(userPointRule,userPointRuleVO);
            result.add(userPointRuleVO);
        });
        return result;
    }

    /**
     * 分页查询用户积分情况，可通过用户信息模糊查询
     * @param userPointVO userPointVO1
     * @param pageRequest pageRequest
     * @return 结果集
     */
    @Override
    public PageDTO<UserPointVO> pageUserPoints(UserPointVO userPointVO, PageRequest pageRequest) {
        List<Long> userIds = new ArrayList<>();
        if(userPointVO != null && StringUtils.isNotBlank(userPointVO.getName())){
            //通过昵称模糊查询
            Iterable<BigInteger> objects = userDAO.findIdsByNameLike(userPointVO.getName());
            objects.forEach(orgId ->{
                userIds.add(orgId.longValue());
            });
        }
        PageDTO<UserPointVO> result = new PageDTO<>();
        List<UserPointVO> list = new ArrayList<>();

        Page<UserPoint> page = userPointDAO.findAll(getInputCondition(userPointVO,userIds),pageRequest);
        page.forEach(userPoint -> {
            UserPointVO userPointVO1 = new UserPointVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(userPoint,userPointVO1);
            if(userPointVO1.getUserLevel() != null){
                userPointVO1.setUserLevelKey(userPointVO1.getUserLevel().getKey());
            }
            UserInfoVO userInfoVO = getUserInfo(String.valueOf(userPoint.getUserId()));
            BeanUtil.copyPropertiesIgnoreNullFilds(userInfoVO,userPointVO1,"userId");
            if(userPointVO1.getUserType() != null && StringUtils.isBlank(userPointVO1.getUserTypeKey())){
                userPointVO1.setUserTypeKey(userPointVO1.getUserType().getKey());
            }
            list.add(userPointVO1);
        });
        if(page != null){
            result.setTotalPages(page.getTotalPages());
            result.setNumber(page.getNumber() + 1);
            result.setTotalElements(page.getTotalElements());
        }
        result.setRows(list);
        return result;
    }

    /**
     * 根据多种情况查询用户信息
     * 包括 eq：userLevel，in:userId
     * @param  vo vo
     * @return  Predicate
     */
    private Predicate getInputCondition(UserPointVO vo, List<Long> userIds)
    {
        List<BooleanExpression> predicates = new ArrayList<>();
        if(null != vo)
        {
            if (vo.getUserLevel() != null) {
                predicates.add(QUserPoint.userPoint.userLevel.eq(vo.getUserLevel()));
            }
            if (StringUtils.isNotBlank(vo.getName())) {
                predicates.add(QUserPoint.userPoint.userId.in(userIds));
            }
        }
        predicates.add(QUserPoint.userPoint.enableFlag.eq(EnableFlag.Y));
        return BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
    }

    /**
     * 查询用户的积分记录信息
     * @param dto dto
     * @param pageRequest pageRequest
     * @return 结果集
     */
    @Override
    public PageDTO<UserPointRecordViewVO> pageUserPointRecords(UserDTO dto, PageRequest pageRequest) {
        PageDTO<UserPointRecordViewVO> result = new PageDTO<>();
        Page<UserPointRecord> page = userPointRecordDAO.findByUserIdAndEnableFlag(dto.getId(), EnableFlag.Y,pageRequest);
        List<UserPointRecordViewVO> list = new ArrayList<>();
        if(page != null){
            for(UserPointRecord record:page){
                UserPointRecordViewVO vo = new UserPointRecordViewVO();
                BeanUtil.copyPropertiesIgnoreNullFilds(record,vo);
                list.add(vo);
            }
            result.setNumber(page.getNumber()+1);
            result.setTotalElements(page.getTotalElements());
            result.setTotalPages(page.getTotalPages());
        }
        result.setRows(list);
        return result;
    }

    /**
     * 查询用户积分是否准确落地，补偿和更新缓存(定时任务)
     */
     @Transactional(rollbackFor = Exception.class)
     @Async
     @Override
     public String perfectUserPoint(){
         logger.info("#################user###########time job begin ##########################");
         Iterable<User> iterable = userDAO.findAll();
         Iterator<User> it = iterable.iterator();
         while(it.hasNext()){
             User user = it.next();
             Long userId = user.getId();
             //更新用户缓存redis信息
             userService.updateUserInfoCache(String.valueOf(userId),user);
             if(user.getUserStatus() == UserStatus.FREEZE){
                 //已经冻结的用户不管
                 logger.info("the user is freezing ,the userId is {} ",userId);
                 continue;
             }
             UserOrgClient userOrgClient = new UserOrgClient();
             userOrgClient.setId(user.getUserOrgId());
             userOrgClient.setUserAccountType(UserAccountType.IND_ACCOUNT);
             userService.updateUserOrgInfoCache(userOrgClient);
             //*********************积分统计开始***************/

             /*
              * 用户是否注册时积分未落地
              */
             //查询通过注册所加的积分记录
             List<UserPointRecord> registPoints = userPointRecordDAO.findByUserIdAndEnableFlagAndPointRuleType(userId,EnableFlag.Y,PointRuleType.REGISTER_LOGIN);
             if(CollectionUtils.isEmpty(registPoints)){
                 //积分记录为空，说明注册时未给该用户加入积分，补偿
                 //查询注册需加的分数和备注信息
                 List<UserPointRule> rules = ruleDAO.findByPointRuleTypeAndEnableFlagOrderByCreateDateDesc(PointRuleType.REGISTER_LOGIN,EnableFlag.Y);
                 if(!CollectionUtils.isEmpty(rules)){
                     UserPointRule rule = rules.get(0);
                     //初始化积分记录
                     UserPointRecord userPointRecord = initUserPointRecord(user,rule,String.valueOf(userId));
                     //补偿积分
                     userPointRecordDAO.saveAndFlush(userPointRecord);
                     //发送消息提醒
                     sendMessageMqInfo(rule,user);
                     logger.info("user {} Compensation register integral",userId);
                 }
             }

             /*
              * 用户是否实名认证时积分未落地,
              */
             //用户实名认证已经通过
             if(user.getUserRealAuth() == UserRealAuth.NORMAL){
                 //查询通过实名认证所加的积分记录
                 List<UserPointRecord> userRealNamePoints = userPointRecordDAO.findByUserIdAndEnableFlagAndPointRuleType(userId,EnableFlag.Y,PointRuleType.VERIFIED_INFO);
                 if(CollectionUtils.isEmpty(userRealNamePoints)){
                     //积分记录为空，说明注册时未给该用户加入积分，补偿
                     //查询实名认证需加的分数和备注信息
                     List<UserPointRule> rules = ruleDAO.findByPointRuleTypeAndEnableFlagOrderByCreateDateDesc(PointRuleType.VERIFIED_INFO,EnableFlag.Y);
                     if(!CollectionUtils.isEmpty(rules)){
                         UserPointRule rule = rules.get(0);
                         //初始化积分记录
                         UserPointRecord userPointRecord = initUserPointRecord(user,rule,String.valueOf(userId));
                         //补偿积分
                         userPointRecordDAO.saveAndFlush(userPointRecord);
                         //发送消息提醒
                         sendMessageMqInfo(rule,user);
                         logger.info("user {} Compensation userRealName integral",userId);
                     }
                 }
             }


             /*
              * 用户上传的单据是否有未加积分的情况
              */
             //获取用户上传单据的总数
             String sumWeight = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.HASH_VOUCHER_NUM);
             double sumWeights = 0;
             if(StringUtils.isNotBlank(sumWeight)){
                 sumWeights = Double.valueOf(sumWeight);
             }
             //计算通过上传的单据应该加的积分次数
             int points = (int)sumWeights/200000;
             //用户有通过上传单据需要添加的积分
             if(points > 0){
                 //需要补偿的积分次数
                 int size = points;
                 //查询用户通过上传单据所加的用户积分记录
                 List<UserPointRecord> weightPoints = userPointRecordDAO.findByUserIdAndEnableFlagAndPointRuleType(userId,EnableFlag.Y,PointRuleType.MEACH_SEND_ORDER);
                 if(!CollectionUtils.isEmpty(weightPoints)){
                     //已经添加的积分次数
                     int weightSize = weightPoints.size();
                     size = points - weightSize;
                 }
                 if(size > 0){
                     addPoint(PointRuleType.MEACH_SEND_ORDER,size,user,userId);
                     logger.info("user {} Compensation voucher integral,the time id {},weight is {}",userId,size,sumWeights);
                 }
             }

             /*
              * 用户上传供求是否有积分未落地的情况
              */
             //查询用户上传的供求
             String gqOnline = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.HASH_USER_CREATEPRODUCT_INFO);
             String gqOffsell = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.HASH_PRODUCT_OFF_SELL_PREFIX);
             String[] gqOnlines = null;
             if(StringUtils.isNotBlank(gqOnline)){
                 gqOnlines = gqOnline.split(",");
             }
             String[] gqOffsells = null;
             if(StringUtils.isNotBlank(gqOffsell)){
                 gqOffsells = gqOffsell.split(",");
             }
             //用户发布的供求数
             int size = 0;
             if(gqOnlines != null && gqOnlines.length > 0){
                 //用户发布的在线的供求数
                 size += gqOnlines.length;
             }if(gqOffsells != null && gqOffsells.length > 0){
                 //用户发布的已下架的供求数
                 size += gqOffsells.length;
             }
             //用户有通过上传单据需要添加的积分
             if(size > 0){
                 //需要补偿的积分次数
                 int gqSize = size;
                 //查询用户通过发布供求所加的用户积分记录
                 List<UserPointRecord> gqPoints = userPointRecordDAO.findByUserIdAndEnableFlagAndPointRuleType(userId,EnableFlag.Y,PointRuleType.SINGLE_ONCE_SEND_REQUEST);
                 if(!CollectionUtils.isEmpty(gqPoints)){
                     //已经添加的积分次数
                     int weightSize = gqPoints.size();
                     gqSize = size - weightSize;
                 }
                 if(gqSize > 0){
                     //查询注册需加的分数和备注信息
                     addPoint(PointRuleType.SINGLE_ONCE_SEND_REQUEST,gqSize,user,userId);
                     logger.info("user {} Compensation gq integral,the time id {}，the sumSize {} ",userId,gqSize,size);
                 }
             }

             Object[] objects = userPointRecordDAO.findSumPointsByUserId(userId,EnableFlag.Y.name());
             int avaliPoints = 0;
             if(objects != null && objects.length > 0){
                 if(objects[0] != null){
                     avaliPoints = ((BigDecimal)objects[0]).intValue();
                 }
             }
             UserPoint userPoint = userPointDAO.findByUserIdAndEnableFlag(userId,EnableFlag.Y);
             if(userPoint == null){
                 userPoint = initUserPint(user);
             }
             userPoint.setAvailablePoints(avaliPoints);
             userPoint.setLastUpdateDate(new Date());
             userPoint.setLastUpdateBy(String.valueOf(userId));
             updateUserLevel(userPoint);
             //更新用户总积分
             userPointDAO.save(userPoint);
             //更新用户积分缓存
             updateUserCache(userId,userPoint);
             //*********************积分统计结束***************
         }
         logger.info("#################user###########time job end ##########################");
         return Constants.RETURN_SUCESS;
     }

    @Override
    public PageDTO<UserLevelRuleVO> pageUserLevelRules(PageRequest pageRequest) {
        PageDTO<UserLevelRuleVO> result = new PageDTO<>();
        List<UserLevelRuleVO> list = new ArrayList<>();
        Page<UserLevelRule> page = userLevelRuleDAO.findAll(QUserLevelRule.userLevelRule.enableFlag.eq(EnableFlag.Y),pageRequest);
        page.forEach(userLevelRule ->{
            UserLevelRuleVO userLevelRuleVO = new UserLevelRuleVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(userLevelRule,userLevelRuleVO);
            userLevelRuleVO.setUserLevelName(userLevelRule.getUserLevel().getKey());
            list.add(userLevelRuleVO);
        });
        if(page != null){
            result.setTotalPages(page.getTotalPages());
            result.setNumber(page.getNumber() + 1);
            result.setTotalElements(page.getTotalElements());
        }
        result.setRows(list);
        return result;
    }

    @Override
    public List<UserLevelRuleVO> listUserLevelRules() {
        List<UserLevelRuleVO> list = new ArrayList<>();
        Iterable<UserLevelRule> iterable = userLevelRuleDAO.findAll(QUserLevelRule.userLevelRule.enableFlag.eq(EnableFlag.Y).and(QUserLevelRule.userLevelRule.frezzing.eq(false)));
        iterable.forEach(userLevelRule ->{
            UserLevelRuleVO userLevelRuleVO = new UserLevelRuleVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(userLevelRule,userLevelRuleVO);
            userLevelRuleVO.setUserLevelName(userLevelRule.getUserLevel().getKey());
            list.add(userLevelRuleVO);
        });
        return list;
    }

    @Override
    public String updateUserLevelRule(UserLevelRuleVO userLevelRuleVO,String userId) {
        UserLevelRule userLevelRule = userLevelRuleDAO.findOne(userLevelRuleVO.getId());
        if(userLevelRule == null){
            return Constants.ENABLE_NOT_NULL;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(userLevelRuleVO,userLevelRule);
        userLevelRule.setLastUpdateDate(new Date());
        userLevelRule.setLastUpdateBy(userId);
        userLevelRuleDAO.save(userLevelRule);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public MyPointVO queryMyPoint(Long userId) {
        MyPointVO myPointVO = new MyPointVO();
        //查询用户积分情况
        UserPoint userPoint = userPointDAO.findByUserIdAndEnableFlag(userId,EnableFlag.Y);
        if(userPoint == null){
            userPoint = new UserPoint();
            userPoint.setAvailablePoints(0);
            updateUserLevel(userPoint);
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(userPoint,myPointVO);
        myPointVO.setUserId(userId);
        //查询下一个积分等级的情况
        UserLevelRule userLevelRule = userLevelRuleDAO.findFirstByPointGreaterThanAndEnableFlagAndFrezzingIsFalseOrderByPointAsc(userPoint.getAvailablePoints(),EnableFlag.Y);
        if(userLevelRule != null){
            myPointVO.setLastLevelPoints(userLevelRule.getPoint() - userPoint.getAvailablePoints());
            myPointVO.setLastUserLevel(userLevelRule.getUserLevel());
            myPointVO.setLastUserLevelKey(userLevelRule.getUserLevel().getKey());
        }
        return myPointVO;
    }

    /**
     * 补偿积分
     * @param pointRuleType 积分规则
     * @param size 需要补充的次数
     * @param user 用户
     * @param userId 用户id
     */
    @Transactional(rollbackFor = Exception.class)
     private void addPoint(PointRuleType pointRuleType,int size,User user,Long userId){
         //查询注册需加的分数和备注信息
         List<UserPointRule> rules = ruleDAO.findByPointRuleTypeAndEnableFlagOrderByCreateDateDesc(pointRuleType,EnableFlag.Y);
         if(!CollectionUtils.isEmpty(rules)){
             UserPointRule rule = rules.get(0);
             for(int i = 0; i < size; i++) {
                 //初始化积分记录
                 UserPointRecord userPointRecord = initUserPointRecord(user,rule,String.valueOf(userId));
                 //补偿积分
                 userPointRecordDAO.saveAndFlush(userPointRecord);
                 //发送消息提醒
                 sendMessageMqInfo(rule, user);
             }
         }
     }

}
