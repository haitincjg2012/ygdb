package com.apec.society.service.impl;

import com.apec.framework.cache.CacheHashService;
import com.apec.framework.common.Constants;
import com.apec.framework.common.RedisHashConstants;
import com.apec.framework.common.enums.MqTag;
import com.apec.framework.common.enumtype.ArticleMsgType;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.dto.UserInfoVO;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.rockmq.client.MqProducerClient;
import com.apec.society.dao.SocietyLzlReplyDAO;
import com.apec.society.dao.SocietyPostDAO;
import com.apec.society.dao.SocietyPostReplyDAO;
import com.apec.society.model.*;
import com.apec.society.service.SocietyLzlReplyService;
import com.apec.society.util.SnowFlakeKeyGen;
import com.apec.society.view.SocietyLzlReplyViewVO;
import com.apec.society.vo.SocietyLzlReplyVO;
import com.apec.society.vo.SocietyPostReplyMsgVO;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hmy on 2017/10/30.
 *
 * @author hmy
 */
@Service
public class SocietyLzlReplyServiceImpl implements SocietyLzlReplyService {

    @Autowired
    private SocietyLzlReplyDAO societyLzlReplyDAO;

    @Autowired
    private SocietyPostReplyDAO societyPostReplyDAO;

    @Autowired
    private SocietyPostDAO societyPostDAO;

    @InjectLogger
    private Logger logger;

    @Autowired
    private SnowFlakeKeyGen idGen;

    @Autowired
    private CacheHashService cacheHashService;

    @Autowired
    private MqProducerClient mqProducerClient;

    private UserInfoVO getUserInfo(String userId){
        String userInfoJson = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.HASH_OBJCONTENT_CACHE);
        UserInfoVO userInfo ;
        if(StringUtils.isBlank(userInfoJson)){
            //获取不到数据,记录日志
            logger.warn("[SocietyLzlReplyServiceImpl][getUserInfo]Can't find user hash cache. userNo:{}",userId);
            userInfo = new UserInfoVO();
        }else{
            userInfo = BaseJsonUtil.parseObject(userInfoJson,UserInfoVO.class);
        }
        return userInfo;
    }

    /**
     * 添加楼中楼回复
     * @param societyLzlReplyVO 楼中楼数据回复对象
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addSocietyLzlReply(SocietyLzlReplyVO societyLzlReplyVO){
        SocietyLzlReply societyLzlReply = new SocietyLzlReply();
        BeanUtil.copyPropertiesIgnoreNullFilds(societyLzlReplyVO,societyLzlReply);
        if(societyLzlReplyVO.getFormSystem() == null){
            societyLzlReply.setFormSystem(false);
        }
        SocietyLzlReply societyLzlReply1 = societyLzlReplyDAO.findByIdAndEnableFlag(societyLzlReplyVO.getToReplyId(),EnableFlag.Y);
        SocietyPostReply societyPostReply;
        if(societyLzlReply1 == null){
            //回复对象在楼中楼数据中不存在，则回复的对象存在于评论中
            societyPostReply = societyPostReplyDAO.findByIdAndEnableFlag(societyLzlReplyVO.getToReplyId(),EnableFlag.Y);
            if(societyPostReply == null){
                return Constants.ENABLE_NOT_NULL;
            }
            societyLzlReply.setToUserId(societyPostReply.getUserId());
        }else{
            //查询楼主评论
            societyPostReply = societyPostReplyDAO.findByIdAndEnableFlag(societyLzlReply1.getToFuReplyId(),EnableFlag.Y);
            if(societyPostReply == null){
                return Constants.ENABLE_NOT_NULL;
            }
            //要回复的对象uid为该回复对象的uid
            societyLzlReply.setToUserId(societyLzlReply1.getUserId());
        }
        if(societyPostReply.getFormSystem() != null && societyPostReply.getFormSystem()){
            //系统回复则不需要发送通知消息
            return Constants.RETURN_SUCESS;
        }
        //设置楼主评论id
        societyLzlReply.setToFuReplyId(societyPostReply.getId());
        //设置帖子id
        societyLzlReply.setSocietyPostId(societyPostReply.getSocietyPostId());
        societyLzlReply.setId(idGen.nextId());
        societyLzlReply.setEnableFlag(EnableFlag.Y);
        societyLzlReply.setCreateDate(new Date());
        societyLzlReplyDAO.save(societyLzlReply);
        BeanUtil.copyPropertiesIgnoreNullFilds(societyLzlReply,societyLzlReplyVO);
        //增加楼主回复数
        societyPostReply.setReplyNum(societyPostReply.getReplyNum() == null?1:societyPostReply.getReplyNum() + 1);
        societyPostReplyDAO.save(societyPostReply);
        //回复通知
        SocietyPostReplyMsgVO societyPostReplyMsgVO = new SocietyPostReplyMsgVO();
        societyPostReplyMsgVO.setId(idGen.nextId());
        //发送者为回复人
        societyPostReplyMsgVO.setSender(societyLzlReply.getUserId());
        //接收者为回复对象人
        societyPostReplyMsgVO.setReceiver(societyLzlReply.getToUserId());
        //关联id为本回复id
        societyPostReplyMsgVO.setRelativeId(societyLzlReply.getId());
        SocietyPost societyPost = societyPostDAO.findOne(societyLzlReply.getSocietyPostId());
        //关联来源id为帖子id
        societyPostReplyMsgVO.setRelativeMainId(societyPost.getId());
        //类型为回复
        societyPostReplyMsgVO.setArticleMsgType(ArticleMsgType.GIVE_THE_COMMENT);
        //消息内容为评论内容
        societyPostReplyMsgVO.setMessage(societyLzlReply.getContent());
        //是否为系统回复
        societyPostReplyMsgVO.setFormSystem(societyLzlReply.getFormSystem());
        //来源为帖子
        societyPostReplyMsgVO.setRealm(societyPost.getRealm());
        mqProducerClient.sendConcurrently(MqTag.REPLY_MSG.getKey(),String.valueOf(societyPostReplyMsgVO.getId()),societyPostReplyMsgVO);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String deleteSocietyLzlReply(SocietyLzlReplyVO societyLzlReplyVO) {
        SocietyLzlReply societyLzlReply = societyLzlReplyDAO.findOne(societyLzlReplyVO.getId());
        if(societyLzlReply == null){
            return Constants.ENABLE_NOT_NULL;
        }
        societyLzlReply.setLastUpdateDate(new Date());
        societyLzlReply.setEnableFlag(EnableFlag.N);
        societyLzlReplyDAO.save(societyLzlReply);
        SocietyPostReply societyPostReply = societyPostReplyDAO.findOne(societyLzlReply.getToFuReplyId());
        societyPostReply.setReplyNum(societyPostReply.getReplyNum() - 1 < 0?0:societyPostReply.getReplyNum() - 1);
        societyPostReply.setLastUpdateDate(new Date());
        societyPostReplyDAO.save(societyPostReply);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 获取楼中楼回复
     * @param societyLzlReplyVO 查询条件
     * @return 所有符合条件的楼中楼回复
     */
    @Override
    public List<SocietyLzlReplyViewVO> getLzlReplyFormReply(SocietyLzlReplyVO societyLzlReplyVO){
        Iterable<SocietyLzlReply> societyLzlReplies = societyLzlReplyDAO.findAll(getInputCondition(societyLzlReplyVO));
        Iterator<SocietyLzlReply> it = societyLzlReplies.iterator();
        List<SocietyLzlReplyViewVO> societyLzlReplyViewVOS = new ArrayList<>();
        while(it.hasNext()){
            SocietyLzlReply societyLzlReply = it.next();
            SocietyLzlReplyViewVO societyLzlReplyViewVO = new SocietyLzlReplyViewVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(societyLzlReply,societyLzlReplyViewVO);
            if(societyLzlReply.getFormSystem() != null && societyLzlReply.getFormSystem()){
                //系统评论
                societyLzlReplyViewVO.setName(Constants.SYSTEM_ADMIN);
            }else{
                UserInfoVO userInfoVO1 = getUserInfo(String.valueOf(societyLzlReplyViewVO.getUserId()));
                societyLzlReplyViewVO.setName(userInfoVO1.getName());
            }
            SocietyLzlReply societyLzlReply1 = societyLzlReplyDAO.findByIdAndEnableFlag(societyLzlReply.getToReplyId(),EnableFlag.Y);
            if(societyLzlReply1 == null){
                SocietyPostReply societyPostReply = societyPostReplyDAO.findByIdAndEnableFlag(societyLzlReply.getToReplyId(),EnableFlag.Y);
                if(societyPostReply != null && societyPostReply.getFormSystem() != null && societyPostReply.getFormSystem()){
                    societyLzlReplyViewVO.setToUserName(Constants.SYSTEM_ADMIN);
                }else{
                    UserInfoVO toUserInfo = getUserInfo(String.valueOf(societyLzlReplyViewVO.getToUserId()));
                    societyLzlReplyViewVO.setToUserName(toUserInfo.getName());
                }
            }else{
                if(societyLzlReply1.getFormSystem() != null && societyLzlReply1.getFormSystem()){
                    societyLzlReplyViewVO.setToUserName(Constants.SYSTEM_ADMIN);
                }else{
                    UserInfoVO toUserInfo = getUserInfo(String.valueOf(societyLzlReplyViewVO.getToUserId()));
                    societyLzlReplyViewVO.setToUserName(toUserInfo.getName());
                }
            }
            societyLzlReplyViewVOS.add(societyLzlReplyViewVO);
        }
        return societyLzlReplyViewVOS;
    }

    /**
     * 多条件查询帖子信息
     * 根据多种情况查询
     * 包括like:content,eq:userId,societyPostId
     * @param societyLzlReplyVO 查询条件对象
     * @return Predicate
     */
    private Predicate getInputCondition(SocietyLzlReplyVO societyLzlReplyVO)
    {
        List<BooleanExpression> predicates = new ArrayList<>();
        if(null != societyLzlReplyVO)
        {
            if(!StringUtils.isEmpty(societyLzlReplyVO.getContent()))
            {
                predicates.add(QSocietyLzlReply.societyLzlReply.content.like("%" + societyLzlReplyVO.getContent() + "%"));
            }
            if(societyLzlReplyVO.getUserId() != null && societyLzlReplyVO.getUserId() != 0L)
            {
                predicates.add(QSocietyLzlReply.societyLzlReply.userId.eq(societyLzlReplyVO.getUserId()));
            }
            if(societyLzlReplyVO.getToUserId() != null && societyLzlReplyVO.getToUserId() != 0L)
            {
                predicates.add(QSocietyLzlReply.societyLzlReply.toUserId.eq(societyLzlReplyVO.getToUserId()));
            }
            if(societyLzlReplyVO.getSocietyPostId() != null && societyLzlReplyVO.getSocietyPostId() != 0L)
            {
                predicates.add(QSocietyLzlReply.societyLzlReply.societyPostId.eq(societyLzlReplyVO.getSocietyPostId()));
            }
            if(societyLzlReplyVO.getToFuReplyId() != null && societyLzlReplyVO.getToFuReplyId() != 0L)
            {
                predicates.add(QSocietyLzlReply.societyLzlReply.toFuReplyId.eq(societyLzlReplyVO.getToFuReplyId()));
            }
            if(societyLzlReplyVO.getToReplyId() != null && societyLzlReplyVO.getToReplyId() != 0L)
            {
                predicates.add(QSocietyLzlReply.societyLzlReply.toReplyId.eq(societyLzlReplyVO.getToReplyId()));
            }
        }
        predicates.add(QSocietyPost.societyPost.enableFlag.eq(EnableFlag.Y));
        return BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
    }

}
