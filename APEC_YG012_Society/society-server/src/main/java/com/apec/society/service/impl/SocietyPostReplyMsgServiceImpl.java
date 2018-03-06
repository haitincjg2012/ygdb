package com.apec.society.service.impl;

import com.apec.framework.cache.CacheHashService;
import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.RedisHashConstants;
import com.apec.framework.common.enumtype.ArticleMsgType;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.dto.UserInfoVO;
import com.apec.framework.log.InjectLogger;
import com.apec.society.dao.SocietyPostDAO;
import com.apec.society.dao.SocietyPostReplyMsgDAO;
import com.apec.society.dto.SocietyPostReplyMsgDTO;
import com.apec.society.model.QSocietyPostReplyMsg;
import com.apec.society.model.SocietyPost;
import com.apec.society.model.SocietyPostReplyMsg;
import com.apec.society.service.SocietyPostReplyMsgService;
import com.apec.society.util.SnowFlakeKeyGen;
import com.apec.society.view.SocietyPostReplyMsgViewVO;
import com.apec.society.vo.SocietyPostReplyMsgVO;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hmy on 2017/11/1.
 *
 * @author hmy
 */
@Service
public class SocietyPostReplyMsgServiceImpl implements SocietyPostReplyMsgService {

    @Autowired
    private SnowFlakeKeyGen idGen;

    @Autowired
    private SocietyPostReplyMsgDAO societyPostReplyMsgDAO;

    @Autowired
    private SocietyPostDAO societyPostDAO;

    @Autowired
    private CacheHashService cacheHashService;

    @InjectLogger
    private Logger logger;

    private UserInfoVO getUserInfo(Long userId){
        String userInfoJson = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.HASH_OBJCONTENT_CACHE);
        UserInfoVO userInfo ;
        if(StringUtils.isBlank(userInfoJson)){
            //获取不到数据,记录日志
            logger.warn("[SocietyPostReplyMsgServiceImpl][getUserInfo]Can't find user hash cache. userNo:{}",userId);
            userInfo = new UserInfoVO();
        }else{
            userInfo = BaseJsonUtil.parseObject(userInfoJson,UserInfoVO.class);
        }
        return userInfo;
    }

    /**
     * 添加回复评论帖子消息
     * @param societyPostReplyMsgVO 消息体
     * @return  操作结果
     */
    @Override
    public String addSocietyPostReplyMsg(SocietyPostReplyMsgVO societyPostReplyMsgVO){
        SocietyPostReplyMsg societyPostReplyMsg = new SocietyPostReplyMsg();
        BeanUtil.copyPropertiesIgnoreNullFilds(societyPostReplyMsgVO,societyPostReplyMsg);
        societyPostReplyMsg.setId(idGen.nextId());
        societyPostReplyMsg.setCreateDate(new Date());
        societyPostReplyMsg.setEnableFlag(EnableFlag.Y);
        societyPostReplyMsg.setReadFlag(false);
        societyPostReplyMsgDAO.save(societyPostReplyMsg);
        if(societyPostReplyMsg.getArticleMsgType() == ArticleMsgType.GIVE_THE_THUMBS_UP){
            //点赞消息数加1
            cacheHashService.hinc(RedisHashConstants.HASH_USER_PREFIX + societyPostReplyMsg.getReceiver(),RedisHashConstants.HASH_USER_GIVEUP_MSG_COUNT,1);
        }
        if(societyPostReplyMsg.getArticleMsgType() == ArticleMsgType.GIVE_THE_COMMENT){
            //点赞消息数加1
            cacheHashService.hinc(RedisHashConstants.HASH_USER_PREFIX + societyPostReplyMsg.getReceiver(),RedisHashConstants.HASH_USER_ARTICLE_MSG_COUNT,1);
        }
        return Constants.RETURN_SUCESS;
    }

    /**
     * 消息设置为已读
     * @param societyPostReplyMsgVO 消息体
     * @return  操作结果
     */
    @Override
    public String setReadedMsg(SocietyPostReplyMsgVO societyPostReplyMsgVO){
        SocietyPostReplyMsg societyPostReplyMsg = societyPostReplyMsgDAO.findOne(societyPostReplyMsgVO.getId());
        if(societyPostReplyMsg == null){
            return Constants.ENABLE_NOT_NULL;
        }
        //设置为已读
        societyPostReplyMsg.setReadFlag(true);
        societyPostReplyMsg.setLastUpdateDate(new Date());
        societyPostReplyMsgDAO.save(societyPostReplyMsg);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 分页查询我的帖子相关信息
     * @param societyPostReplyMsgDTO  查询条件
     * @param pageRequest  分页对象
     * @return 分页结果
     */
    @Override
    public PageDTO<SocietyPostReplyMsgViewVO> societyPostReplyMsgPage(SocietyPostReplyMsgDTO societyPostReplyMsgDTO, PageRequest pageRequest){
        PageDTO<SocietyPostReplyMsgViewVO> result = new PageDTO<>();
        List<SocietyPostReplyMsgViewVO> list = new ArrayList<>();
        Page<SocietyPostReplyMsg> page = societyPostReplyMsgDAO.findAll(getInputCondition(societyPostReplyMsgDTO),pageRequest);
        if(page != null){
            for(SocietyPostReplyMsg societyPostReplyMsg:page){
                SocietyPostReplyMsgViewVO societyPostReplyMsgViewVO = new SocietyPostReplyMsgViewVO();
                BeanUtil.copyPropertiesIgnoreNullFilds(societyPostReplyMsg,societyPostReplyMsgViewVO);
                if(societyPostReplyMsg.getFormSystem() != null && societyPostReplyMsg.getFormSystem()){
                    societyPostReplyMsgViewVO.setSenderName(Constants.SYSTEM_ADMIN);
                }else{
                    UserInfoVO sender = getUserInfo(societyPostReplyMsg.getSender());
                    societyPostReplyMsgViewVO.setSenderName(sender.getName());
                    societyPostReplyMsgViewVO.setSenderImgUrl(sender.getImgUrl());
                }
                UserInfoVO receiver = getUserInfo(societyPostReplyMsg.getReceiver());
                societyPostReplyMsgViewVO.setReceiverName(receiver.getName());
                SocietyPost societyPost = societyPostDAO.findOne(societyPostReplyMsg.getRelativeMainId());
                if(societyPost != null){
                    societyPostReplyMsgViewVO.setUrl(societyPost.getUrl());
                    societyPostReplyMsgViewVO.setSocietyPostContent(societyPost.getContent());
                }
                list.add(societyPostReplyMsgViewVO);
            }
            result.setNumber(page.getNumber() + 1);
            result.setTotalPages(page.getTotalPages());
            result.setTotalElements(page.getTotalElements());
        }
        result.setRows(list);
        return result;
    }

    /**
     * 多条件查询帖子信息
     * 根据多种情况查询
     * 包括like:message,eq:sender,receiver,relativeId,articleMsgType
     * @param societyPostReplyMsgDTO 查询条件对象
     * @return Predicate
     */
    private Predicate getInputCondition(SocietyPostReplyMsgDTO societyPostReplyMsgDTO)
    {
        List<BooleanExpression> predicates = new ArrayList<>();
        if(null != societyPostReplyMsgDTO)
        {
            if(!StringUtils.isEmpty(societyPostReplyMsgDTO.getMessage()))
            {
                predicates.add(QSocietyPostReplyMsg.societyPostReplyMsg.message.like("%" + societyPostReplyMsgDTO.getMessage() + "%"));
            }
            if(societyPostReplyMsgDTO.getSender() != null && societyPostReplyMsgDTO.getSender() != 0L)
            {
                predicates.add(QSocietyPostReplyMsg.societyPostReplyMsg.sender.eq(societyPostReplyMsgDTO.getSender()));
            }
            if(societyPostReplyMsgDTO.getReceiver() != null && societyPostReplyMsgDTO.getReceiver() != 0L)
            {
                predicates.add(QSocietyPostReplyMsg.societyPostReplyMsg.receiver.eq(societyPostReplyMsgDTO.getReceiver()));
            }
            if(societyPostReplyMsgDTO.getRelativeId() != null && societyPostReplyMsgDTO.getRelativeId() != 0L)
            {
                predicates.add(QSocietyPostReplyMsg.societyPostReplyMsg.relativeId.eq(societyPostReplyMsgDTO.getRelativeId()));
            }
            if(societyPostReplyMsgDTO.getArticleMsgType() != null )
            {
                predicates.add(QSocietyPostReplyMsg.societyPostReplyMsg.articleMsgType.eq(societyPostReplyMsgDTO.getArticleMsgType()));
            }
        }
        predicates.add(QSocietyPostReplyMsg.societyPostReplyMsg.enableFlag.eq(EnableFlag.Y));
        return BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
    }

    /**
     * 删除相应的消息
     * @param societyPostReplyMsgVO 消息体
     * @return  操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteSocietyPostReplyMsg(SocietyPostReplyMsgVO societyPostReplyMsgVO){
        SocietyPostReplyMsgDTO societyPostReplyMsgDTO = new SocietyPostReplyMsgDTO();
        //消息接收者
        if(societyPostReplyMsgVO.getReceiver() != null || societyPostReplyMsgVO.getReceiver() != 0L){
            societyPostReplyMsgDTO.setReceiver(societyPostReplyMsgVO.getReceiver());
        }
        //消息发送者
        if(societyPostReplyMsgVO.getSender() != null || societyPostReplyMsgVO.getSender() != 0L){
            societyPostReplyMsgDTO.setSender(societyPostReplyMsgVO.getSender());
        }
        //消息相关的id
        if(societyPostReplyMsgVO.getRelativeId() != null || societyPostReplyMsgVO.getRelativeId() != 0L){
            societyPostReplyMsgDTO.setRelativeId(societyPostReplyMsgVO.getRelativeId());
        }
        //消息类型，点赞/评论
        if(societyPostReplyMsgVO.getArticleMsgType() != null){
            societyPostReplyMsgDTO.setArticleMsgType(societyPostReplyMsgVO.getArticleMsgType());
        }
        Iterable<SocietyPostReplyMsg> societyPostReplyMsgs = societyPostReplyMsgDAO.findAll(getInputCondition(societyPostReplyMsgDTO));
        Iterator<SocietyPostReplyMsg> iterator = societyPostReplyMsgs.iterator();
        while(iterator.hasNext()){
            SocietyPostReplyMsg societyPostReplyMsg = iterator.next();
            //删除消息
            societyPostReplyMsg.setEnableFlag(EnableFlag.N);
            societyPostReplyMsg.setLastUpdateDate(new Date());
            societyPostReplyMsgDAO.save(societyPostReplyMsg);
        }
        return Constants.RETURN_SUCESS;
    }


}
