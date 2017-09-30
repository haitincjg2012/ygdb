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
import com.apec.framework.common.util.JsonUtil;
import com.apec.framework.dto.UserViewVO;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.rockmq.client.MQProducerClient;
import com.apec.framework.rockmq.vo.MessageBodyVO;
import com.apec.framework.rockmq.vo.MessageVO;
import com.apec.user.dao.*;
import com.apec.user.dto.UserDTO;
import com.apec.user.model.*;
import com.apec.user.service.UserPointRecordService;
import com.apec.user.util.SnowFlakeKeyGen;
import com.apec.user.vo.*;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nullable;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Created by hmy on 2017/7/6.
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
    private CacheHashService cacheHashService;

    @Autowired
    private MQProducerClient mqProducerClient;

    /*
     * 用户等级计算
     */
    private void updateUserLevel(UserPoint userPoint){
        UserLevelRule userLevelRule = userLevelRuleDAO.findFirstByPointLessThanEqualAndEnableFlagOrderByPointDesc(
                                                                            userPoint.getAvailablePoints(),EnableFlag.Y);
        if(userLevelRule != null){
            userPoint.setUserLevel(userLevelRule.getUserLevel());
        }
    }

    /**
     * 更新用户缓存
     * @param userId 用户ID
     * @return 返回码
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
            userViewVO = JsonUtil.parseObject(json,UserViewVO.class);
        }
        if(userPoint != null) {
            userViewVO.setPoint(userPoint.getAvailablePoints());
            userViewVO.setUserLevelKey(userPoint.getUserLevel().getKey());
            userViewVO.setUserLevelName(userPoint.getUserLevel().name());
        }
        cacheHashService.hset(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.HASH_USER_CREATEUSERVIEW_INFO, JsonUtil.toJSONString(userViewVO));
    }

    /**
     * 发送 积分变动的站内信
     * @param rule 用户积分规则
     * @param user 用户
     */
    private void sendMessageMqInfo(UserPointRule rule,User user){
        MessageBodyVO messageBodyVO = new MessageBodyVO();
        messageBodyVO.setType(MessageType.NOTIFICATION);//系统通知
        messageBodyVO.setTemplateFlag(true);//使用动态模板发送
        Map<String, String> contentMap = new HashMap<>();
        contentMap.put("point",String.valueOf(rule.getPointsChanged()));
        contentMap.put("remark",rule.getRemark());
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
        messageVO.setReceivers(receivers);//接收者
        messageVO.setMessageStatus(MessageStatus.NEW);//用户状态
        mqProducerClient.sendConcurrently(MqTag.MESSAGE_TAG.getKey(),String.valueOf(messageVO.getId()),messageVO);
    }

    /**
     * 初始化积分
     * @param user
     * @return
     */
    @Transactional
    public UserPoint initUserPint(User user){
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
     * @param user
     * @return
     */
    public UserPointRecord initUserPointRecord(User user,UserPointRule rule,String userId){
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
        userPointRecord.setRuleId(String.valueOf(rule.getId()));
        userPointRecord.setCreateDate(new Date());
        userPointRecord.setCreateBy(userId);
        userPointRecord.setEnableFlag(EnableFlag.Y);
        return userPointRecord;
    }

    @Override
    @Transactional
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
            BeanUtil.copyPropertiesIgnoreNullFilds(userPointRecordVO,rule);
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
    @Transactional
    public String updateUserPointRule(UserPointRuleVO ruleVO, String userId) {
        //查询相应的积分规则信息
        UserPointRule rule = ruleDAO.findByIdAndEnableFlag(ruleVO.getId(),EnableFlag.Y);
        if(rule == null){
            logger.warn("rule is not exist ruleId{}:" + ruleVO.getId());
            return Constants.COMMON_ERROR_PARAMS;
        }
        //修改积分规则中改变积分的信息
        rule.setLastUpdateBy(userId);
        rule.setLastUpdateDate(new Date());
        if(ruleVO.getPointsChanged() != null){
            rule.setPointsChanged(ruleVO.getPointsChanged());
        }if(ruleVO.getPointsChangedType() != null){
            rule.setPointsChangedType(ruleVO.getPointsChangedType());
        }
        ruleDAO.save(rule);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 分页查询积分规则
     * @param pageRequest
     * @return
     */
    @Override
    public PageDTO<UserPointRuleVO> pageUserPointRule(PageRequest pageRequest) {
        Page<UserPointRule> page = ruleDAO.findAll(pageRequest);
        PageDTO<UserPointRuleVO> userPointRuleVOPageDTO = new PageDTO<>();
        List<UserPointRuleVO> rules = new ArrayList<>();
        for(UserPointRule rule:page){
            UserPointRuleVO pointRuleVO = new UserPointRuleVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(rule,pointRuleVO);
            pointRuleVO.setPointsChangedTypeKey(rule.getPointsChangedType().getKey());
            pointRuleVO.setPointRuleTypeKey(rule.getPointRuleType().getKey());
            rules.add(pointRuleVO);
        }
        userPointRuleVOPageDTO.setRows(rules);
        userPointRuleVOPageDTO.setNumber(page.getNumber()+1);
        userPointRuleVOPageDTO.setTotalPages(page.getTotalPages());
        userPointRuleVOPageDTO.setTotalElements(page.getTotalElements());
        return userPointRuleVOPageDTO;
    }

    /**
     * 分页查询用户积分情况，可通过用户信息模糊查询
     * @param dto
     * @param pageRequest
     * @return
     */
    @Override
    public PageDTO<UserPointVO> pageUserPoints(UserDTO dto, PageRequest pageRequest) {
//        UserVO userVO = new UserVO();
//        BeanUtil.copyPropertiesIgnoreNullFilds(dto,userVO);
//
//        Page<UserPoint> page = userPointDAO.findAll(getInputCondition(userVO),pageRequest);
        PageDTO<UserPointVO> result = new PageDTO<>();
//        List<UserPointVO> list = new ArrayList<>();
//        if(page != null){
//            User user;
//            for(UserPoint point:page){
//                user = userDAO.findOne(point.getUserId());
//                UserPointVO vo = new UserPointVO();
//                BeanUtil.copyPropertiesIgnoreNullFilds(point,vo);
//                if(user != null){
//                    vo.setUserName(user.getName());
//                }
//                list.add(vo);
//            }
//            result.setNumber(page.getNumber()+1);
//            result.setTotalElements(page.getTotalElements());
//            result.setTotalPages(page.getTotalPages());
//        }
//        result.setRows(list);
        return result;
    }

    /**
     * 查询用户的积分记录信息
     * @param dto
     * @param pageRequest
     * @return
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
     * @return
     */
     @Transient
     public String perfectUserPoint(){
         logger.info("#################user###########time job begin ##########################");
         Iterable<User> iterable = userDAO.findAll();
         Iterator<User> it = iterable.iterator();
         while(it.hasNext()){
             User user = it.next();
             Long userId = user.getId();
             /*********************积分统计开始***************/

             /**
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
                 }
             }

             /**
              * 用户上传的单据是否有未加积分的情况
              */
             //获取用户上传单据的总数
             String sumWeight = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.HASH_VOUCHER_NUM);
             double sumWeights = 0;
             if(StringUtils.isNotBlank(sumWeight)){
                 sumWeights = Double.valueOf(sumWeight);
             }
             int points = (int)sumWeights/200000;//计算通过上传的单据应该加的积分次数
             if(points > 0){//用户有通过上传单据需要添加的积分
                 int size = points;//需要补偿的积分次数
                 //查询用户通过上传单据所加的用户积分记录
                 List<UserPointRecord> weightPoints = userPointRecordDAO.findByUserIdAndEnableFlagAndPointRuleType(userId,EnableFlag.Y,PointRuleType.MEACH_SEND_ORDER);
                 if(!CollectionUtils.isEmpty(weightPoints)){
                     int weightSize = weightPoints.size();//已经添加的积分次数
                     size = points - weightSize;
                 }
                 if(size > 0){
                     //查询注册需加的分数和备注信息
                     List<UserPointRule> rules = ruleDAO.findByPointRuleTypeAndEnableFlagOrderByCreateDateDesc(PointRuleType.MEACH_SEND_ORDER,EnableFlag.Y);
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

             /**
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
             int size = 0;//用户发布的供求数
             if(gqOnlines != null && gqOnlines.length > 0){
                 size += gqOnlines.length;//用户发布的在线的供求数
             }if(gqOffsells != null && gqOffsells.length > 0){
                 size += gqOffsells.length;//用户发布的已下架的供求数
             }
             if(size > 0){//用户有通过上传单据需要添加的积分
                 int gqSize = size;//需要补偿的积分次数
                 //查询用户通过发布供求所加的用户积分记录
                 List<UserPointRecord> gqPoints = userPointRecordDAO.findByUserIdAndEnableFlagAndPointRuleType(userId,EnableFlag.Y,PointRuleType.SINGLE_ONCE_SEND_REQUEST);
                 if(!CollectionUtils.isEmpty(gqPoints)){
                     int weightSize = gqPoints.size();//已经添加的积分次数
                     gqSize = size - weightSize;
                 }
                 if(gqSize > 0){
                     //查询注册需加的分数和备注信息
                     List<UserPointRule> rules = ruleDAO.findByPointRuleTypeAndEnableFlagOrderByCreateDateDesc(PointRuleType.SINGLE_ONCE_SEND_REQUEST,EnableFlag.Y);
                     if(!CollectionUtils.isEmpty(rules)){
                         UserPointRule rule = rules.get(0);
                         for(int i = 0; i < gqSize; i++) {
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
             userPointDAO.save(userPoint);//更新用户总积分
             updateUserCache(userId,userPoint);//更新用户积分缓存
             /*********************积分统计结束***************/
         }
         logger.info("#################user###########time job end ##########################");
         return Constants.RETURN_SUCESS;
     }

}
