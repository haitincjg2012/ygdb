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
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private void updateUserCache(String userId,UserPoint userPoint,User user){
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
        userViewVO.setPoint(userPoint.getAvailablePoints());
        userViewVO.setUserLevelKey(userPoint.getUserLevel().getKey());
        userViewVO.setUserLevelName(userPoint.getUserLevel().name());
        userViewVO.setUserRealAuthKey(user.getUserRealAuth().getKey());
        userViewVO.setUserRealAuthName(user.getUserRealAuth().name());
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
            User user = userDAO.findOne(userId);
            if(user == null){
                logger.info("[UserPoint][addUserPoint] Can't add user point, user is null! the userId is {}", userId);
                continue;
            }
            //查询当前用户积分信息
            UserPoint point = userPointDAO.findByUserIdAndEnableFlag(userId,EnableFlag.Y);
            if(point == null){
                //用户积分未曾初始化，则初始化用户积分
                logger.info("[UserPoint][addUserPoint] unit the user's point ,the userId is {}", userId);
                point = initUserPint(user);
            }
            //增加积分记录
            UserPointRecord userPointRecord = new UserPointRecord();
            userPointRecord.setId(idGen.nextId());
//            BeanUtil.copyPropertiesIgnoreNullFilds(userPointRecordVO,userPointRecord);
            userPointRecord.setUserId(user.getId());
            userPointRecord.setPointsChanged(pointsChanged);
            userPointRecord.setPointRuleType(rule.getPointRuleType());
            userPointRecord.setRemark(rule.getRemark());
            if(pointsChanged < 0){
                //本次为扣除积分，则记录为消费
                point.setLastConsumeTime(new Date());
                userPointRecord.setPointsChanged(pointsChanged);
            }
            //修改用户积分
            userPointDAO.updateUserPoints(pointsChanged,userId);
            //更新Cache
            //保存积分记录
            userPointRecordDAO.saveAndFlush(userPointRecord);
            UserPoint userPoint = userPointDAO.findByUserIdAndEnableFlag(userId,EnableFlag.Y);
            userPoint.setLastConsumeTime(point.getLastConsumeTime());
            updateUserLevel(userPoint);//修改用户积分等级
            userPointDAO.save(userPoint);
            updateUserCache(String.valueOf(userId), userPoint, user);
            //发送积分变动的站内信
            sendMessageMqInfo(rule,user);
        }
        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional
    public String updateUserPointRule(UserPointRuleVO ruleVO, String userId) {
        //查询相应的积分规则信息
        UserPointRule rule = ruleDAO.findOne(ruleVO.getId());
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
        UserVO userVO = new UserVO();
        BeanUtil.copyPropertiesIgnoreNullFilds(dto,userVO);

        Page<UserPoint> page = userPointDAO.findAll(getInputCondition(userVO),pageRequest);
        PageDTO<UserPointVO> result = new PageDTO<>();
        List<UserPointVO> list = new ArrayList<>();
        if(page != null){
            User user;
            for(UserPoint point:page){
                user = userDAO.findOne(point.getUserId());
                UserPointVO vo = new UserPointVO();
                BeanUtil.copyPropertiesIgnoreNullFilds(point,vo);
                if(user != null){
                    vo.setUserName(user.getName());
                }
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
     * 根据多种情况查询用户积分信息
     * 包括like：name
     * @param vo 用户对象（以积分表中用户对象的信息去进行条件查询）
     * @return
     */
    private Predicate getInputCondition(UserVO vo)
    {
        List<BooleanExpression> predicates = new ArrayList<>();
        if(null != vo)
        {
            if(StringUtils.isNotBlank(vo.getName()))
            {
                predicates.add(QUserPoint.userPoint.user.name.like("%" + vo.getName() + "%"));
            }
        }
        predicates.add(QUserPoint.userPoint.enableFlag.eq(EnableFlag.Y));
        return BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
    }

}
