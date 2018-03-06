package com.apec.society.service.impl;

import com.apec.framework.cache.CacheHashService;
import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.RedisHashConstants;
import com.apec.framework.common.enums.MqTag;
import com.apec.framework.common.enumtype.ArticleMsgType;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.dto.UserInfoVO;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.rockmq.client.MqProducerClient;
import com.apec.society.dao.SocietyPostDAO;
import com.apec.society.dao.SocietyPostReplyDAO;
import com.apec.society.dto.SocietyPostReplyDTO;
import com.apec.society.model.*;
import com.apec.society.service.SocietyLzlReplyService;
import com.apec.society.service.SocietyPostReplyService;
import com.apec.society.util.CharUtil;
import com.apec.society.util.SnowFlakeKeyGen;
import com.apec.society.view.SocietyPostReplyViewVO;
import com.apec.society.vo.SocietyLzlReplyVO;
import com.apec.society.vo.SocietyPostReplyMsgVO;
import com.apec.society.vo.SocietyPostReplyVO;
import com.apec.society.vo.SocietyPostVO;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hmy on 2017/10/30.
 *
 * @author hmy
 */
@Service
public class SocietyPostReplyServiceImpl implements SocietyPostReplyService {

    @Autowired
    private SocietyPostReplyDAO societyPostReplyDAO;

    @Autowired
    private SocietyPostDAO societyPostDAO;

    @Autowired
    private SocietyLzlReplyService societyLzlReplyService;

    @Autowired
    private SnowFlakeKeyGen idGen;

    @Autowired
    private CacheHashService cacheHashService;

    @Autowired
    private MqProducerClient mqProducerClient;

    @InjectLogger
    private Logger logger;

    private UserInfoVO getUserInfo(String userId){
        String userInfoJson = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.HASH_OBJCONTENT_CACHE);
        UserInfoVO userInfo ;
        if(StringUtils.isBlank(userInfoJson)){
            //获取不到数据,记录日志
            logger.warn("[SocietyPostReplyServiceImpl][getUserInfo]Can't find user hash cache. userNo:{}",userId);
            userInfo = new UserInfoVO();
        }else{
            userInfo = BaseJsonUtil.parseObject(userInfoJson,UserInfoVO.class);
        }
        return userInfo;
    }

    /**
     * 增加帖子回复
     * @param societyPostReplyVO 回复对象
     * @return 操作结果
     */
    @Override
    public String addSocietyPostReply(SocietyPostReplyVO societyPostReplyVO){
        SocietyPost societyPost = societyPostDAO.findOne(societyPostReplyVO.getSocietyPostId());
        if(societyPost == null){
            return Constants.ENABLE_NOT_NULL;
        }
        SocietyPostReply societyPostReply = new SocietyPostReply();
        BeanUtil.copyPropertiesIgnoreNullFilds(societyPostReplyVO,societyPostReply);
        if(societyPostReplyVO.getFormSystem() == null){
            societyPostReply.setFormSystem(false);
        }
        societyPostReply.setReplyNum(0);
        societyPostReply.setId(idGen.nextId());
        societyPostReply.setEnableFlag(EnableFlag.Y);
        societyPostReply.setCreateDate(new Date());
        //修改最新回复时间
        societyPost.setLastReplyTime(societyPostReply.getCreateDate());
        //增加回复量
        societyPost.setReplyCount(societyPost.getReplyCount() == null?1:societyPost.getReplyCount() + 1);
        societyPostReplyDAO.save(societyPostReply);
        BeanUtil.copyPropertiesIgnoreNullFilds(societyPostReply,societyPostReplyVO);
        //修改帖子的评论数
        societyPostDAO.save(societyPost);
        if(societyPost.getPersonPub() == null || !societyPost.getPersonPub()){
            //不是个人发布的信息则不通知发帖人
            return Constants.RETURN_SUCESS;
        }
        //给发帖人发送通知消息
        SocietyPostReplyMsgVO societyPostReplyMsgVO = new SocietyPostReplyMsgVO();
        societyPostReplyMsgVO.setId(idGen.nextId());
        //发送者为评论人
        societyPostReplyMsgVO.setSender(societyPostReplyVO.getUserId());
        //接收者为发帖人
        societyPostReplyMsgVO.setReceiver(societyPost.getUserId());
        //关联id为本评论id
        societyPostReplyMsgVO.setRelativeId(societyPostReply.getId());
        //关联来源id为帖子id
        societyPostReplyMsgVO.setRelativeMainId(societyPost.getId());
        //类型为发表评论
        societyPostReplyMsgVO.setArticleMsgType(ArticleMsgType.GIVE_THE_COMMENT);
        //消息内容为评论内容
        societyPostReplyMsgVO.setMessage(societyPostReplyVO.getContent());
        //来源为帖子
        societyPostReplyMsgVO.setRealm(societyPost.getRealm());
        mqProducerClient.sendConcurrently(MqTag.REPLY_MSG.getKey(),String.valueOf(societyPostReplyMsgVO.getId()),societyPostReplyMsgVO);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String deleteSocietyPostReply(SocietyPostReplyVO societyPostReplyVO) {
        SocietyPostReply societyPostReply = societyPostReplyDAO.findOne(societyPostReplyVO.getId());
        if(societyPostReply == null){
            return Constants.ENABLE_NOT_NULL;
        }
        societyPostReply.setEnableFlag(EnableFlag.N);
        societyPostReply.setLastUpdateDate(new Date());
        societyPostReplyDAO.save(societyPostReply);
        SocietyPost societyPost = societyPostDAO.findOne(societyPostReply.getSocietyPostId());
        societyPost.setReplyCount(societyPost.getReplyCount() - 1 < 0?0:societyPost.getReplyCount() - 1);
        societyPost.setLastUpdateDate(new Date());
        societyPostDAO.save(societyPost);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 查询回复量最多的评论
     * @param societyPostVO 帖子信息
     * @param userId 操作用户id
     * @return 回复量最多的评论
     */
    @Override
    public SocietyPostReplyViewVO findMaxReplyNum(SocietyPostVO societyPostVO, String userId){
        SocietyPostReplyViewVO societyPostReplyViewVO = new SocietyPostReplyViewVO();
        SocietyPostReply societyPostReply = societyPostReplyDAO.findFirstBySocietyPostIdAndEnableFlagOrderByReplyNumDesc(societyPostVO.getId(),EnableFlag.Y);
        if(societyPostReply == null){
            return societyPostReplyViewVO;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(societyPostReply,societyPostReplyViewVO);
        UserInfoVO userInfoVO = getUserInfo(String.valueOf(societyPostReplyViewVO.getUserId()));
        societyPostReplyViewVO.setName(userInfoVO.getName());
        societyPostReplyViewVO.setImgUrl(userInfoVO.getImgUrl());
        //查询该用户点过赞的帖子
        String userLikePostReplys = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.LIKE_POST_REPLY);
        List<String> userLikePostReplysArray = CharUtil.getListFromStr(userLikePostReplys);

        boolean isLike = false;
        if(!CollectionUtils.isEmpty(userLikePostReplysArray)){
            isLike = userLikePostReplysArray.contains(String.valueOf(societyPostReply.getId()));
        }
        societyPostReplyViewVO.setLikeUser(isLike);
        SocietyLzlReplyVO societyLzlReplyVO = new SocietyLzlReplyVO();
        societyLzlReplyVO.setToFuReplyId(societyPostReply.getId());
        //获取楼中楼数据
        societyPostReplyViewVO.setSocietyLzlReplyViewVOS(societyLzlReplyService.getLzlReplyFormReply(societyLzlReplyVO));
        return societyPostReplyViewVO;
    }



    /**
     * 多条件查询帖子信息
     * 根据多种情况查询
     * 包括like:content,eq:userId,societyPostId
     * @param societyPostReplyDTO 查询条件对象
     * @return Predicate
     */
    private Predicate getInputCondition(SocietyPostReplyDTO societyPostReplyDTO)
    {
        List<BooleanExpression> predicates = new ArrayList<>();
        if(null != societyPostReplyDTO)
        {
            if(!StringUtils.isEmpty(societyPostReplyDTO.getContent()))
            {
                predicates.add(QSocietyPostReply.societyPostReply.content.like("%" + societyPostReplyDTO.getContent() + "%"));
            }
            if(societyPostReplyDTO.getUserId() != null && societyPostReplyDTO.getUserId() != 0L)
            {
                predicates.add(QSocietyPostReply.societyPostReply.userId.eq(societyPostReplyDTO.getUserId()));
            }
            if(societyPostReplyDTO.getSocietyPostId() != null && societyPostReplyDTO.getSocietyPostId() != 0L)
            {
                predicates.add(QSocietyPostReply.societyPostReply.societyPostId.eq(societyPostReplyDTO.getSocietyPostId()));
            }
            if(!CollectionUtils.isEmpty(societyPostReplyDTO.getIds()) && societyPostReplyDTO.getIds().size() > 0)
            {
                //不存在相关id
                predicates.add(QSocietyPostReply.societyPostReply.id.notIn(societyPostReplyDTO.getIds()));
            }
        }
        predicates.add(QSocietyPostReply.societyPostReply.enableFlag.eq(EnableFlag.Y));
        return BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
    }

    /**
     * 分页查询帖子评论
     * @param societyPostReplyDTO 查询条件对象
     * @param pageRequest 分页对象
     * @return 分页结果
     */
    @Override
    public PageDTO<SocietyPostReplyViewVO> findSocietyPostReplyPage(SocietyPostReplyDTO societyPostReplyDTO, PageRequest pageRequest,String userId){
        PageDTO<SocietyPostReplyViewVO> result = new PageDTO<>();
        Page<SocietyPostReply> page = societyPostReplyDAO.findAll(getInputCondition(societyPostReplyDTO),pageRequest);
        //查询该用户点过赞的帖子
        String userLikePostReplys = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.LIKE_POST_REPLY);
        List<String> userLikePostReplysArray = CharUtil.getListFromStr(userLikePostReplys);

        List<SocietyPostReplyViewVO> societyPostReplyViewVOS = new ArrayList<>();
        if(page != null){
            for(SocietyPostReply societyPostReply:page){
                SocietyPostReplyViewVO societyPostReplyViewVO = new SocietyPostReplyViewVO();
                BeanUtil.copyPropertiesIgnoreNullFilds(societyPostReply,societyPostReplyViewVO);
                societyPostReplyViewVO.setCreateDateStr(CharUtil.getStrFromDate(societyPostReplyViewVO.getCreateDate()));
                if(societyPostReply.getFormSystem() != null && societyPostReply.getFormSystem()){
                    //系统评论
                    societyPostReplyViewVO.setName(Constants.SYSTEM_ADMIN);
                }else{
                    UserInfoVO userInfoVO = getUserInfo(String.valueOf(societyPostReply.getUserId()));
                    societyPostReplyViewVO.setName(userInfoVO.getName());
                    societyPostReplyViewVO.setImgUrl(userInfoVO.getImgUrl());
                }

                boolean isLike = false;
                if(!CollectionUtils.isEmpty(userLikePostReplysArray)){
                    isLike = userLikePostReplysArray.contains(String.valueOf(societyPostReply.getId()));
                }
                societyPostReplyViewVO.setLikeUser(isLike);
                //查询楼层评论楼中楼回复
                SocietyLzlReplyVO societyLzlReplyVO = new SocietyLzlReplyVO();
                societyLzlReplyVO.setToFuReplyId(societyPostReply.getId());
                //获取楼中楼数据
                societyPostReplyViewVO.setSocietyLzlReplyViewVOS(societyLzlReplyService.getLzlReplyFormReply(societyLzlReplyVO));
                societyPostReplyViewVOS.add(societyPostReplyViewVO);
            }
            result.setTotalElements(page.getTotalElements());
            result.setTotalPages(page.getTotalPages());
            result.setNumber(page.getNumber() + 1);
        }
        result.setRows(societyPostReplyViewVOS);
        return result;
    }

    /**
     * 点赞/取消点赞
     * @param societyPostReplyVO 帖子对象
     * @param userId 操作用户id
     * @return 操作结果
     */
    @Override
    public String likeSocietyPostOrNot(SocietyPostReplyVO societyPostReplyVO,String userId){
        SocietyPostReply societyPostReply = societyPostReplyDAO.findOne(societyPostReplyVO.getId());
        if(societyPostReply == null){
            return Constants.ENABLE_NOT_NULL;
        }
        SocietyPost societyPost = societyPostDAO.findOne(societyPostReply.getSocietyPostId());
        if(societyPost == null){
            return Constants.ENABLE_NOT_NULL;
        }
        String userLikePostReplys = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.LIKE_POST_REPLY);
        List<String> userLikePostReplysArray = CharUtil.getListFromStr(userLikePostReplys);

        //点赞发送异步消息
        SocietyPostReplyMsgVO societyPostReplyMsgVO = new SocietyPostReplyMsgVO();
        societyPostReplyMsgVO.setId(idGen.nextId());
        //发送者为点赞人
        societyPostReplyMsgVO.setSender(NumberUtils.createLong(userId));
        //接收者为评论发布者
        societyPostReplyMsgVO.setReceiver(societyPostReply.getUserId());
        //关联id为帖子id
        societyPostReplyMsgVO.setRelativeId(societyPostReply.getId());
        //关联来源id为帖子id
        societyPostReplyMsgVO.setRelativeMainId(societyPost.getId());
        //类型为点赞
        societyPostReplyMsgVO.setArticleMsgType(ArticleMsgType.GIVE_THE_THUMBS_UP);

        //消息来源
        societyPostReplyMsgVO.setRealm(societyPost.getRealm());

        //增加/减少帖子点赞数
        if(societyPostReplyVO.getLikePostReply() != null && societyPostReplyVO.getLikePostReply()){
            //点赞
            if(!CollectionUtils.isEmpty(userLikePostReplysArray) && userLikePostReplysArray.contains(String.valueOf(societyPostReply.getId()))){
                //已经给该帖子点过赞了
                return ErrorCodeConst.DOUBLE_LIKE;
            }if(CollectionUtils.isEmpty(userLikePostReplysArray)){
                userLikePostReplysArray = new ArrayList<>();
            }
            userLikePostReplysArray.add(String.valueOf(societyPostReply.getId()));

            //增加评论的点赞数
            societyPostReply.setLikeCount(societyPostReply.getLikeCount() == null?1:societyPostReply.getLikeCount() + 1);
            //消息内容为点赞
            societyPostReplyMsgVO.setMessage(ArticleMsgType.GIVE_THE_THUMBS_UP.getKey());
            //发送点赞通知消息
            mqProducerClient.sendConcurrently(MqTag.REPLY_MSG.getKey(),String.valueOf(societyPostReplyMsgVO.getId()),societyPostReplyMsgVO);
        }else{
            //取消点赞
            if(!CollectionUtils.isEmpty(userLikePostReplysArray) && userLikePostReplysArray.contains(String.valueOf(societyPostReply.getId()))){
                //已经给该帖子点过赞了
                userLikePostReplysArray.remove(String.valueOf(societyPostReply.getId()));
            }else{
                //并没有点过赞
                return ErrorCodeConst.NOT_LIKE;
            }

            //减少帖子的点赞数
            societyPostReply.setLikeCount(societyPostReply.getLikeCount() == null?0:societyPostReply.getLikeCount() - 1);
            //删除之前点赞的消息
            mqProducerClient.sendConcurrently(MqTag.DELETE_REPLY_MSG.getKey(),String.valueOf(societyPostReplyMsgVO.getId()),societyPostReplyMsgVO);
        }
        StringBuilder userLikePostsArraySb = new StringBuilder();
        userLikePostReplysArray.forEach(userLikePostsSb ->{
            userLikePostsArraySb.append(userLikePostsSb);
            userLikePostsArraySb.append(Constants.COMMA);
        });
        if(userLikePostsArraySb.length() > 1){
            userLikePostReplys = userLikePostsArraySb.substring(0,userLikePostsArraySb.length()-1);
        }else{
            userLikePostReplys = "";
        }

        //修改用户中国保存的点赞的帖子id
        cacheHashService.hset(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.LIKE_POST_REPLY,userLikePostReplys);

        //修改数据库表中存储的点赞数
        societyPostReplyDAO.save(societyPostReply);

        return Constants.RETURN_SUCESS;
    }

    /**
     * 查询回复量最多的评论
     * @param societyPostReplyVO 评论对象id
     * @param userId 操作用户id
     * @return 回复量最多的评论
     */
    @Override
    public SocietyPostReplyViewVO queryReplyById(SocietyPostReplyVO societyPostReplyVO, String userId){
        SocietyPostReplyViewVO societyPostReplyViewVO = new SocietyPostReplyViewVO();
        SocietyPostReply societyPostReply = societyPostReplyDAO.findOne(societyPostReplyVO.getId());
        if(societyPostReply == null){
            return societyPostReplyViewVO;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(societyPostReply,societyPostReplyViewVO);
        UserInfoVO userInfoVO = getUserInfo(String.valueOf(societyPostReplyViewVO.getUserId()));
        societyPostReplyViewVO.setName(userInfoVO.getName());
        societyPostReplyViewVO.setImgUrl(userInfoVO.getImgUrl());
        //查询该用户点过赞的帖子
        String userLikePostReplys = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.LIKE_POST_REPLY);
        List<String> userLikePostReplysArray = CharUtil.getListFromStr(userLikePostReplys);

        boolean isLike = false;
        if(!CollectionUtils.isEmpty(userLikePostReplysArray)){
            isLike = userLikePostReplysArray.contains(String.valueOf(societyPostReply.getId()));
        }
        societyPostReplyViewVO.setLikeUser(isLike);
        SocietyLzlReplyVO societyLzlReplyVO = new SocietyLzlReplyVO();
        societyLzlReplyVO.setToFuReplyId(societyPostReply.getId());
        //获取楼中楼数据
        societyPostReplyViewVO.setSocietyLzlReplyViewVOS(societyLzlReplyService.getLzlReplyFormReply(societyLzlReplyVO));
        return societyPostReplyViewVO;
    }


}
