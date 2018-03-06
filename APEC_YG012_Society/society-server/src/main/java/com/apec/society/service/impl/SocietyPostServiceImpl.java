package com.apec.society.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apec.framework.cache.CacheHashService;
import com.apec.framework.common.*;
import com.apec.framework.common.enums.MqTag;
import com.apec.framework.common.enums.Realm;
import com.apec.framework.common.enumtype.*;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.common.util.DateTimeUtils;
import com.apec.framework.dto.UserInfoVO;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.rockmq.client.MqProducerClient;
import com.apec.framework.rockmq.vo.MessageBodyVO;
import com.apec.framework.rockmq.vo.MessageVO;
import com.apec.framework.rockmq.vo.UserPointRecordVO;
import com.apec.society.dao.SocietyPostDAO;
import com.apec.society.dao.SocietyPostImagesDAO;
import com.apec.society.dto.SocietyPostDTO;
import com.apec.society.dto.SocietyPostReplyDTO;
import com.apec.society.model.QSocietyPost;
import com.apec.society.model.SocietyPost;
import com.apec.society.model.SocietyPostImages;
import com.apec.society.model.SocietyPostReply;
import com.apec.society.service.SocietyPostReplyService;
import com.apec.society.service.SocietyPostService;
import com.apec.society.util.CharUtil;
import com.apec.society.util.SnowFlakeKeyGen;
import com.apec.society.view.QuotationView;
import com.apec.society.view.SocietyPostImagesViewVO;
import com.apec.society.view.SocietyPostReplyViewVO;
import com.apec.society.view.SocietyPostViewVO;
import com.apec.society.vo.SocietyPostAggreVO;
import com.apec.society.vo.SocietyPostImagesVO;
import com.apec.society.vo.SocietyPostReplyMsgVO;
import com.apec.society.vo.SocietyPostVO;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hmy on 2017/10/30.
 *
 * @author hmy
 */
@Service
public class SocietyPostServiceImpl implements SocietyPostService {

    @Autowired
    private SnowFlakeKeyGen idGen;

    @Autowired
    private SocietyPostDAO societyPostDAO;

    @Autowired
    private SocietyPostImagesDAO societyPostImagesDAO;

    @Autowired
    private CacheHashService cacheHashService;

    @Autowired
    private SocietyPostReplyService societyPostReplyService;

    @Autowired
    private MqProducerClient mqProducerClient;

    @Autowired
    private MongoTemplate mongoTemplate;

    @InjectLogger
    private Logger logger;

    private UserInfoVO getUserInfo(String userId){
        String userInfoJson = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.HASH_OBJCONTENT_CACHE);
        UserInfoVO userInfo ;
        if(StringUtils.isBlank(userInfoJson)){
            //获取不到数据,记录日志
            logger.warn("[SocietyPostServiceImpl][getUserInfo]Can't find user hash cache. userNo:{}",userId);
            userInfo = new UserInfoVO();
        }else{
            userInfo = BaseJsonUtil.parseObject(userInfoJson,UserInfoVO.class);
        }
        return userInfo;
    }

    /**
     * 增加帖子
     * @param societyPostVO 帖子对象
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addSocietyPost(SocietyPostVO societyPostVO){
        SocietyPost societyPost = new SocietyPost();
        BeanUtil.copyPropertiesIgnoreNullFilds(societyPostVO,societyPost);
        if(StringUtils.isBlank(societyPost.getUrl())){
            //地址为空则将地址置为空，hasImage为false
            societyPost.setUrl(null);
            societyPost.setHasImage(false);
        }
        societyPost.setId(idGen.nextId());
        societyPost.setEnableFlag(EnableFlag.Y);
        societyPost.setCreateDate(new Date());
        //默认来源帖子
        societyPost.setRealm(societyPostVO.getRealm() == null ? Realm.SOCIETYPOST:societyPostVO.getRealm());
        //如果为个人发布的行情，则需要审核,其他情况默认为审核通过,行情竞猜,该结果需最终判定
        boolean needAudit = (societyPostVO.getRealm() == Realm.ARTICLE && societyPostVO.getPersonPub() != null && societyPostVO.getPersonPub()) || societyPostVO.getRealm() == Realm.QUOTATION;
        if(needAudit){
            societyPost.setAuditState(Constants.ZERO);
        }else{
            societyPost.setAuditState(Constants.ISSUCCESS);
            societyPost.setPassDate(new Date());
        }
        if(societyPostVO.getPersonPub() == null ){
            societyPost.setPersonPub(false);
        }
        if(!CollectionUtils.isEmpty(societyPostVO.getSocietyPostImagesVOS())){
            List<SocietyPostImages> societyPostImagesArrayList = new ArrayList<>();
            for(SocietyPostImagesVO societyPostImagesVO:societyPostVO.getSocietyPostImagesVOS()){
                SocietyPostImages societyPostImages = new SocietyPostImages();
                BeanUtil.copyPropertiesIgnoreNullFilds(societyPostImagesVO,societyPostImages);
                societyPostImages.setSocietyPostId(societyPost.getId());
                societyPostImages.setId(idGen.nextId());
                societyPostImages.setEnableFlag(EnableFlag.Y);
                societyPostImages.setCreateDate(new Date());
                societyPostImagesArrayList.add(societyPostImages);
            }
            //保存用户上传的图片
            societyPostImagesDAO.save(societyPostImagesArrayList);
        }
        societyPostDAO.save(societyPost);
        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateQuotation(QuotationView quotationView){
        SocietyPost societyPost = societyPostDAO.findOne(quotationView.getId());
        if(societyPost == null){
            return Constants.ENABLE_NOT_NULL;
        }
        Long now = (new Date()).getTime();
        if(quotationView.getStartTime() != null && quotationView.getEndTime() != null){
            //数据库中存在开始时间比输入的结束时间先开始,结束时间比输入的开始时间后结束的为重复
            List<SocietyPost> societyPost1 = societyPostDAO.findByRealmAndEnableFlagAndStartTimeBeforeAndEndTimeAfterOrderByCreateDateDesc(Realm.QUOTATION,EnableFlag.Y,quotationView.getEndTime(),quotationView.getStartTime());
            if(!CollectionUtils.isEmpty(societyPost1)){
                if(societyPost1.size() > 1){
                    return ErrorCodeConst.ERROR_TIME;
                }
                //只有一个的情况,看是否等于本竞猜
                SocietyPost societyPost2 = societyPost1.get(0);
                if(societyPost2 != null && !Objects.equals(societyPost.getId(),societyPost2.getId())){
                    return ErrorCodeConst.ERROR_TIME;
                }
            }
        }
        if(quotationView.getStartTime() != null){
//            if(societyPost.getStartTime().getTime() - now > 0){
//                //该竞猜已经超过设置的开始时间(已上线),不能再修改开始时间了
//                return ErrorCodeConst.ERROR_EDIT_TIME;
//            }
//            if(quotationView.getStartTime().getTime() - now < 0){
//                //上线时间不可以设置小于当前时间
//                return ErrorCodeConst.ERROR_EDIT_TIME;
//            }
            //数据库中存在开始时间比输入的开始时间先开始,结束时间比输入的开始时间后结束的为重复
            List<SocietyPost> societyPost1 = societyPostDAO.findByRealmAndEnableFlagAndStartTimeBeforeAndEndTimeAfterOrderByCreateDateDesc(Realm.QUOTATION,EnableFlag.Y,quotationView.getStartTime(),quotationView.getStartTime());
            if(!CollectionUtils.isEmpty(societyPost1)){
                if(societyPost1.size() > 1){
                    return ErrorCodeConst.ERROR_TIME;
                }
                //只有一个的情况,看是否等于本竞猜
                SocietyPost societyPost2 = societyPost1.get(0);
                if(societyPost2 != null && !Objects.equals(societyPost.getId(),societyPost2.getId())){
                    return ErrorCodeConst.ERROR_TIME;
                }
            }
        }
        if(quotationView.getEndTime() != null){
//            if(societyPost.getEndTime().getTime() - now > 0){
//                //该竞猜目前已经超过设置的下线时间(已下线),不能再修改下线时间了
//                return ErrorCodeConst.ERROR_EDIT_TIME;
//            }
            if(quotationView.getEndTime().getTime() - now < 0){
                //下线时间不可以设置小于当前时间
                return ErrorCodeConst.ERROR_EDIT_TIME;
            }
            //数据库中存在开始时间比输入的结束时间先开始,结束时间比输入的结束时间后结束的为重复
            List<SocietyPost> societyPost1 = societyPostDAO.findByRealmAndEnableFlagAndStartTimeBeforeAndEndTimeAfterOrderByCreateDateDesc(Realm.QUOTATION,EnableFlag.Y,quotationView.getEndTime(),quotationView.getEndTime());
                if(!CollectionUtils.isEmpty(societyPost1)){
                    if(societyPost1.size() > 1){
                        return ErrorCodeConst.ERROR_TIME;
                    }
                    //只有一个的情况,看是否等于本竞猜
                SocietyPost societyPost2 = societyPost1.get(0);
                if(societyPost2 != null && !Objects.equals(societyPost.getId(),societyPost2.getId())){
                    return ErrorCodeConst.ERROR_TIME;
                }
            }
        }

        if(quotationView.getStartTime() != null){
            societyPost.setStartTime(quotationView.getStartTime());
        }
        if(quotationView.getEndTime() != null){
            societyPost.setEndTime(quotationView.getEndTime());
        }
        if(StringUtils.isNotBlank(quotationView.getTitle())){
            societyPost.setTitle(quotationView.getTitle());
        }
        if(StringUtils.isNotBlank(quotationView.getContent())){
            societyPost.setContent(quotationView.getContent());
        }
        if(StringUtils.isNotBlank(quotationView.getIssue())){
            societyPost.setIssue(quotationView.getIssue());
        }
        if(StringUtils.isNotBlank(quotationView.getRemark())){
            societyPost.setRemark(quotationView.getRemark());
        }
        societyPost.setLastUpdateDate(new Date());
        societyPostDAO.save(societyPost);
        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String outLineQuotation(QuotationView quotationView){
        SocietyPost societyPost = societyPostDAO.findOne(quotationView.getId());
        if(societyPost == null){
            return Constants.ENABLE_NOT_NULL;
        }
        Long now = (new Date()).getTime();
        if(societyPost.getStartTime().getTime() - now > 0 || societyPost.getEndTime().getTime() - now < 0){
            //只有进行中的才可以进行下线操作
            return ErrorCodeConst.ERROR_AUDITSTATE;
        }
        societyPost.setEndTime(new Date());
        societyPost.setLastUpdateDate(new Date());
        societyPostDAO.save(societyPost);
        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addQuotation(QuotationView quotationView,String userId){
        //统计该时间段内是否有重复的行情竞猜
        //数据库中存在开始时间比输入的结束时间先开始,结束时间比输入的开始时间后结束的为重复
        Long num1 = societyPostDAO.countByRealmAndEnableFlagAndStartTimeBeforeAndEndTimeAfter(Realm.QUOTATION,EnableFlag.Y,quotationView.getEndTime(),quotationView.getStartTime());
        if(num1 > 0){
            return ErrorCodeConst.ERROR_TIME;
        }
        //数据库中存在开始时间比输入的结束时间先开始,结束时间比输入的结束时间后结束的为重复
        Long num2 = societyPostDAO.countByRealmAndEnableFlagAndStartTimeBeforeAndEndTimeAfter(Realm.QUOTATION,EnableFlag.Y,quotationView.getEndTime(),quotationView.getEndTime());
        if(num2 > 0){
            return ErrorCodeConst.ERROR_TIME;
        }
        //数据库中存在开始时间比输入的开始时间先开始,结束时间比输入的开始时间后结束的为重复
        Long num3 = societyPostDAO.countByRealmAndEnableFlagAndStartTimeBeforeAndEndTimeAfter(Realm.QUOTATION,EnableFlag.Y,quotationView.getStartTime(),quotationView.getStartTime());
        if(num3 > 0){
            return ErrorCodeConst.ERROR_TIME;
        }
        SocietyPost societyPost = new SocietyPost();
        BeanUtil.copyPropertiesIgnoreNullFilds(quotationView,societyPost);
        if(StringUtils.isBlank(societyPost.getIssue())){
            societyPost.setIssue(DateTimeUtils.date2String(quotationView.getStartTime(),"yyyyMMdd") + "期");
        }
        societyPost.setRealm(Realm.QUOTATION);
        societyPost.setId(idGen.nextId());
        societyPost.setEnableFlag(EnableFlag.Y);
        societyPost.setCreateDate(new Date());
        societyPost.setAuditState(Constants.ZERO);
        societyPost.setUserId(NumberUtils.createLong(userId));
        societyPostDAO.save(societyPost);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 增加帖子阅读量
     * @param societyPostVO 帖子对象
     * @return 操作结果
     */
    @Override
    public String addSocietyPostViewCount(SocietyPostVO societyPostVO){
        SocietyPost societyPost = societyPostDAO.findOne(societyPostVO.getId());
        if(societyPost == null){
            return Constants.ENABLE_NOT_NULL;
        }
        //增加回复量
        societyPost.setViewCount(societyPost.getViewCount() == null?1:societyPost.getViewCount() + 1);
        societyPostDAO.save(societyPost);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 增加帖子分享数量
     * @param societyPostVO 帖子对象
     * @return 操作结果
     */
    @Override
    public String addSocietyPostTransCount(SocietyPostVO societyPostVO){
        SocietyPost societyPost = societyPostDAO.findOne(societyPostVO.getId());
        if(societyPost == null){
            return Constants.ENABLE_NOT_NULL;
        }
        //增加帖子分享数量
        societyPost.setTransCount(societyPost.getTransCount() == null?1:societyPost.getTransCount() + 1);
        societyPostDAO.save(societyPost);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 点赞/取消点赞
     * @param societyPostVO 帖子对象
     * @param userId 操作用户id
     * @return 操作结果
     */
    @Override
    public String likeSocietyPostOrNot(SocietyPostVO societyPostVO,String userId){
        SocietyPost societyPost = societyPostDAO.findOne(societyPostVO.getId());
        if(societyPost == null){
            return Constants.ENABLE_NOT_NULL;
        }
        String userLikePosts = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.LIKE_POST);
        List<String> userLikePostsArray = CharUtil.getListFromStr(userLikePosts);

        String usersLikeInPost = cacheHashService.hget(RedisHashConstants.HASH_ARTICLE_PREFIX + societyPost.getId(),RedisHashConstants.LIKE_USER);
        List<String> usersLikeInPostArray = CharUtil.getListFromStr(usersLikeInPost);

        //点赞发送异步消息，有人点赞
        SocietyPostReplyMsgVO societyPostReplyMsgVO = new SocietyPostReplyMsgVO();
        societyPostReplyMsgVO.setId(idGen.nextId());
        //发送者为点赞人
        societyPostReplyMsgVO.setSender(NumberUtils.createLong(userId));
        //接收者为帖子发布者
        societyPostReplyMsgVO.setReceiver(societyPost.getUserId());
        //关联id为帖子id
        societyPostReplyMsgVO.setRelativeId(societyPost.getId());
        //关联来源id为帖子id
        societyPostReplyMsgVO.setRelativeMainId(societyPost.getId());
        //类型为点赞
        societyPostReplyMsgVO.setArticleMsgType(ArticleMsgType.GIVE_THE_THUMBS_UP);

        //来源为帖子的来源
        societyPostReplyMsgVO.setRealm(societyPost.getRealm());

        //增加/减少帖子点赞数
        if(societyPostVO.getLikeSocietyPost() != null && societyPostVO.getLikeSocietyPost()){
            //点赞
            if(!CollectionUtils.isEmpty(userLikePostsArray) && userLikePostsArray.contains(String.valueOf(societyPostVO.getId()))){
                //已经给该帖子点过赞了
                logger.info("该用户保存的帖子，{},{}",userLikePosts,userId);
                return ErrorCodeConst.DOUBLE_LIKE;
            }if(CollectionUtils.isEmpty(userLikePostsArray)){
                userLikePostsArray = new ArrayList<>();
            }
            userLikePostsArray.add(String.valueOf(societyPostVO.getId()));
            if(!CollectionUtils.isEmpty(usersLikeInPostArray) && usersLikeInPostArray.contains(userId)){
                //已经给该帖子点过赞了
                logger.info("该帖子保存的用户，{},{}",usersLikeInPost,userId);
                return ErrorCodeConst.DOUBLE_LIKE;
            }if(CollectionUtils.isEmpty(usersLikeInPostArray)){
                usersLikeInPostArray = new ArrayList<>();
            }
            usersLikeInPostArray.add(userId);
            //增加帖子的点赞数
            societyPost.setLikeCount(societyPost.getLikeCount() == null?1:societyPost.getLikeCount() + 1);
            //消息内容为空
            societyPostReplyMsgVO.setMessage(ArticleMsgType.GIVE_THE_THUMBS_UP.getKey());
            mqProducerClient.sendConcurrently(MqTag.REPLY_MSG.getKey(),String.valueOf(societyPostReplyMsgVO.getId()),societyPostReplyMsgVO);
        }else{
            //取消点赞
            if(!CollectionUtils.isEmpty(userLikePostsArray) && userLikePostsArray.contains(String.valueOf(societyPostVO.getId()))){
                //已经给该帖子点过赞了
                userLikePostsArray.remove(String.valueOf(societyPostVO.getId()));
            }else{
                //并没有点过赞
                logger.info("该用户保存的帖子，{},{}",userLikePosts,userId);
                return ErrorCodeConst.NOT_LIKE;
            }
            if(!CollectionUtils.isEmpty(usersLikeInPostArray) && usersLikeInPostArray.contains(userId)){
                //已经给该帖子点过赞了
                usersLikeInPostArray.remove(userId);
            }else{
                //并没有点过赞
                logger.info("该帖子保存的用户，{},{}",usersLikeInPost,userId);
                return ErrorCodeConst.NOT_LIKE;
            }
            //减少帖子的点赞数
            societyPost.setLikeCount(societyPost.getLikeCount() == null?0:societyPost.getLikeCount() - 1);
            //删除之前的点赞通知消息
            mqProducerClient.sendConcurrently(MqTag.DELETE_REPLY_MSG.getKey(),String.valueOf(societyPostReplyMsgVO.getId()),societyPostReplyMsgVO);
        }
        StringBuilder userLikePostsArraySb = new StringBuilder();
        userLikePostsArray.forEach(userLikePostsSb ->{
            userLikePostsArraySb.append(userLikePostsSb);
            userLikePostsArraySb.append(Constants.COMMA);
        });
        if(userLikePostsArraySb.length() > 1){
            userLikePosts = userLikePostsArraySb.substring(0,userLikePostsArraySb.length()-1);
        }else{
            userLikePosts = "";
        }
        StringBuilder usersLikeInPostArraySb = new StringBuilder();
        usersLikeInPostArray.forEach(userLikeInPostsSb ->{
            usersLikeInPostArraySb.append(userLikeInPostsSb);
            usersLikeInPostArraySb.append(Constants.COMMA);
        });
        if(usersLikeInPostArraySb.length() > 1){
            usersLikeInPost = usersLikeInPostArraySb.substring(0,usersLikeInPostArraySb.length()-1);
        }else{
            usersLikeInPost = "";
        }
        //修改用户中国保存的点赞的帖子id
        cacheHashService.hset(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.LIKE_POST,userLikePosts);
        //修改帖子中保存的点过赞的用户id
        cacheHashService.hset(RedisHashConstants.HASH_ARTICLE_PREFIX + societyPost.getId(),RedisHashConstants.LIKE_USER,usersLikeInPost);
        //修改数据库表中存储的点赞数
        societyPostDAO.save(societyPost);

        return Constants.RETURN_SUCESS;
    }

    /**
     * 查询帖子详情
     * @param societyPostVO 帖子  id
     * @return 帖子详情
     */
    @Override
    public SocietyPostViewVO findSocietyPostById(SocietyPostVO societyPostVO,String userId){
        SocietyPostViewVO societyPostViewVO = new SocietyPostViewVO();
        SocietyPost societyPost = societyPostDAO.findOne(societyPostVO.getId());
        if(societyPost == null){
            return societyPostViewVO;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(societyPost,societyPostViewVO);
        UserInfoVO userInfo = getUserInfo(String.valueOf(societyPost.getUserId()));
        societyPostViewVO.setName(userInfo.getName());
        if(societyPost.getPersonPub() != null && societyPost.getPersonPub()){
            societyPostViewVO.setAuthor(userInfo.getName());
        }
        societyPostViewVO.setImgUrl(userInfo.getImgUrl());
        Long userOrgId = userInfo.getUserOrgId();
        societyPostViewVO.setUserOrgId(userOrgId);
        boolean isAttention = false;
        String userOrgIds = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.ORG_SAVE);
        if(StringUtils.isNotBlank(userOrgIds)){
            String[] orgIds = userOrgIds.split(",");
            if(orgIds.length > 0){
                isAttention = Arrays.asList(orgIds).contains(String.valueOf(userOrgId));
            }
        }
        boolean isLike = false;
        //查询该用户点过赞的帖子
        String userPostIds = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.LIKE_POST);
        if(StringUtils.isNotBlank(userPostIds)){
            String[] postIds = userPostIds.split(",");
            if(postIds.length > 0){
                isLike = Arrays.asList(postIds).contains(String.valueOf(societyPostViewVO.getId()));
            }
        }
        societyPostViewVO.setLikeUser(isLike);
        societyPostViewVO.setAttentionUser(isAttention);
        societyPostViewVO.setSocietyPostImagesViewVOS(getImagesFormPost(societyPost.getId()));
        return societyPostViewVO;
    }


    /**
     * 分页查询帖子信息
     * @param societyPostDTO 查询条件
     * @param pageRequest 分页对象
     * @param userId 操作者id
     * @return 分页结果
     */
    @Override
    public PageDTO<SocietyPostViewVO> societyPostSerachPage(SocietyPostDTO societyPostDTO, PageRequest pageRequest,String userId){
        PageDTO<SocietyPostViewVO> result = new PageDTO<>();
        //查询用户关注的组织id
        String userOrgIds = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.ORG_SAVE);
        List<String> orgIdList = null;
        if(StringUtils.isNotBlank(userOrgIds)){
            String[] orgIds = userOrgIds.split(",");
            if(orgIds.length > 0){
                orgIdList = Arrays.asList(orgIds);
            }
        }
        //查询该用户点过赞的帖子
        String userPostIds = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.LIKE_POST);
        List<String> postIdList = null;
        if(StringUtils.isNotBlank(userPostIds)){
            String[] postIds = userPostIds.split(",");
            if(postIds.length > 0){
                postIdList = Arrays.asList(postIds);
            }
        }
        List<Long> userIds = societyPostDTO.getUserIds();
        //查询关注的帖子
        if(societyPostDTO.getAttentionUser() != null && societyPostDTO.getAttentionUser()){
            if(userIds == null){
                userIds = new ArrayList<>();
            }
            if(CollectionUtils.isEmpty(orgIdList)){
                //未关注过任何人，则返回为空
                return result;
            }
            for(String orgId:orgIdList){
                String users = cacheHashService.hget(RedisHashConstants.HASH_USER_ORG_PREFIX + orgId,RedisHashConstants.HASH_ORG_USERLIST);
                if(StringUtils.isNotBlank(users)){
                    JSONArray jsonArray = JSONObject.parseArray(users);
                    for(int i = 0; i < jsonArray.size();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        userIds.add(NumberUtils.createLong(jsonObject.getString("userId")));
                    }
                    if(CollectionUtils.isEmpty(userIds)){
                        return result;
                    }
                }
            }
        }
        societyPostDTO.setUserIds(userIds);
        //查询相关条件的帖子分页信息
        Page<SocietyPost> page = societyPostDAO.findAll(getInputCondition(societyPostDTO),pageRequest);
        if(page == null || page.getTotalElements() == 0L ){
            return result;
        }
        List<SocietyPostViewVO> societyPostViewVOS = new ArrayList<>();
        for(SocietyPost societyPost:page){
            SocietyPostViewVO societyPostViewVO = new SocietyPostViewVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(societyPost,societyPostViewVO);
            //发布时间，如“一天前”之类的时间标识
            societyPostViewVO.setPubDateStr(CharUtil.getStrFromDate(societyPostViewVO.getCreateDate()));
            UserInfoVO userInfo = getUserInfo(String.valueOf(societyPostViewVO.getUserId()));
            societyPostViewVO.setName(userInfo.getName());
            societyPostViewVO.setImgUrl(userInfo.getImgUrl());
            societyPostViewVO.setMobile(userInfo.getMobile());
            if(societyPostViewVO.getPersonPub() != null && societyPostViewVO.getPersonPub()){
                //个人发布的行情的作者名称
                societyPostViewVO.setAuthor(userInfo.getName());
            }
            Long userOrgId = userInfo.getUserOrgId();
            societyPostViewVO.setUserOrgId(userOrgId);
            boolean isAttention = false;
            if(userOrgId != null && userOrgId != 0L){
                if(!CollectionUtils.isEmpty(orgIdList)){
                    isAttention = orgIdList.contains(String.valueOf(userOrgId));
                }
            }
            societyPostViewVO.setAttentionUser(isAttention);
            boolean isLike = false;
            if(!CollectionUtils.isEmpty(postIdList)){
                isLike = postIdList.contains(String.valueOf(societyPostViewVO.getId()));
            }
            societyPostViewVO.setLikeUser(isLike);
            //查询帖子上传的图片
            societyPostViewVO.setSocietyPostImagesViewVOS(getImagesFormPost(societyPostViewVO.getId()));
            //查询N条评论（列表页最多展示n条评论）
            List<SocietyPostReplyViewVO> societyPostReplyViewVOS = new ArrayList<>();
            Integer replyNum = societyPostDTO.getReplyNum();
            if(replyNum != null && replyNum > 0){
                List<Long> ids = new ArrayList<>();
                SocietyPostVO societyPostVO = new SocietyPostVO();
                societyPostVO.setId(societyPostViewVO.getId());
                SocietyPostReplyViewVO societyPostReplyViewVO = societyPostReplyService.findMaxReplyNum(societyPostVO,userId);
                if(societyPostReplyViewVO != null && societyPostReplyViewVO.getId() != null){
                    //第一条为回复量最多的评论
                    societyPostReplyViewVOS.add(societyPostReplyViewVO);
                    replyNum--;
                    ids.add(societyPostReplyViewVO.getId());
                }
                if(replyNum > 0){
                    SocietyPostReplyDTO societyPostReplyDTO = new SocietyPostReplyDTO();
                    societyPostReplyDTO.setIds(ids);
                    societyPostReplyDTO.setSocietyPostId(societyPostViewVO.getId());
                    PageDTO<SocietyPostReplyViewVO> societyPostReplyViewVOPageDTO = societyPostReplyService.findSocietyPostReplyPage(societyPostReplyDTO,new PageRequest(0,replyNum,new Sort(Sort.Direction.DESC,"createDate")),userId);
                    societyPostReplyViewVOS.addAll(societyPostReplyViewVOPageDTO.getRows());
                }
            }
            societyPostViewVO.setSocietyPostReplyViewVOS(societyPostReplyViewVOS);
            societyPostViewVOS.add(societyPostViewVO);
        }
        result.setRows(societyPostViewVOS);
        result.setTotalPages(page.getTotalPages());
        result.setTotalElements(page.getTotalElements());
        result.setNumber(page.getNumber() + 1);
        return result;
    }

    @Override
    public List<Object[]> selectSocietyInfoForExcel(SocietyPostDTO societyPostDTO) {
        List<Object[]> list = new ArrayList<>();
        //查询相关条件的帖子分页信息
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Iterable<SocietyPost> iterable = societyPostDAO.findAll(getInputCondition(societyPostDTO),sort);
        iterable.forEach(societyPost -> {
            Object[] objects = new Object[7];
            objects[0] = DateTimeUtils.getTimeFormat(societyPost.getCreateDate());
            objects[1] = societyPost.getContent();
            objects[2] = societyPost.getViewCount() == null ? "0":String.valueOf(societyPost.getViewCount());
            objects[3] = societyPost.getLikeCount() == null ? "0":String.valueOf(societyPost.getLikeCount());
            objects[4] = societyPost.getReplyCount() == null ? "0":String.valueOf(societyPost.getReplyCount());
            objects[5] = societyPost.getTransCount() == null ? "0":String.valueOf(societyPost.getTransCount());
            UserInfoVO userInfo = getUserInfo(String.valueOf(societyPost.getUserId()));
            if(userInfo != null){
                objects[6] = userInfo.getName();
            }
            list.add(objects);
        });
        return list;
    }

    /**
     * 分页查询帖子信息
     * @param societyPostDTO 查询条件
     * @param pageRequest 分页对象
     * @param userId 操作者id
     * @return 分页结果
     */
    @Override
    public PageDTO<SocietyPostViewVO> mySocietyPostPage(SocietyPostDTO societyPostDTO, PageRequest pageRequest,String userId){
        PageDTO<SocietyPostViewVO> result = new PageDTO<>();
        if(societyPostDTO == null){
            societyPostDTO = new SocietyPostDTO();
        }
        //加入本人发布的条件
        societyPostDTO.setUserId(NumberUtils.createLong(userId));
        Page<SocietyPost> page = societyPostDAO.findAll(getInputCondition(societyPostDTO),pageRequest);
        if(page == null || page.getTotalElements() == 0L ){
            return result;
        }
        List<SocietyPostViewVO> societyPostViewVOS = new ArrayList<>();
        for(SocietyPost societyPost:page){
            SocietyPostViewVO societyPostViewVO = new SocietyPostViewVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(societyPost,societyPostViewVO);
            societyPostViewVOS.add(societyPostViewVO);
        }
        result.setRows(societyPostViewVOS);
        result.setTotalPages(page.getTotalPages());
        result.setTotalElements(page.getTotalElements());
        result.setNumber(page.getNumber() + 1);
        return result;
    }

    private List<SocietyPostImagesViewVO> getImagesFormPost(Long societyPostId){
        List<SocietyPostImages> societyPostImagesList = societyPostImagesDAO.findBySocietyPostIdAndEnableFlagOrderBySort(societyPostId,EnableFlag.Y);
        List<SocietyPostImagesViewVO> societyPostImagesViewVOS = new ArrayList<>();
        if(!CollectionUtils.isEmpty(societyPostImagesList)){
            for(SocietyPostImages societyPostImages:societyPostImagesList){
                SocietyPostImagesViewVO societyPostImagesViewVO = new SocietyPostImagesViewVO();
                BeanUtil.copyPropertiesIgnoreNullFilds(societyPostImages,societyPostImagesViewVO);
                societyPostImagesViewVOS.add(societyPostImagesViewVO);
            }
        }
        return societyPostImagesViewVOS;
    }

    /**
     * 多条件查询帖子信息
     * 根据多种情况查询
     * 包括like:title,content,author,eq:userId，realm，id，enableFlag ,in:userId
     * @param societyPostDTO 查询条件对象
     * @return Predicate
     */
    private Predicate getInputCondition(SocietyPostDTO societyPostDTO)
    {
        List<BooleanExpression> predicates = new ArrayList<>();
        if(null != societyPostDTO)
        {
            if(!StringUtils.isEmpty(societyPostDTO.getTitle()))
            {
                predicates.add(QSocietyPost.societyPost.title.like("%" + societyPostDTO.getTitle() + "%"));
            }
            if(!StringUtils.isEmpty(societyPostDTO.getContent()))
            {
                predicates.add(QSocietyPost.societyPost.content.like("%" + societyPostDTO.getContent() + "%"));
            }
            if(societyPostDTO.getUserId() != null && societyPostDTO.getUserId() != 0L)
            {
                predicates.add(QSocietyPost.societyPost.userId.eq(societyPostDTO.getUserId()));
            }
            //来源类型  行情/帖子
            if(societyPostDTO.getRealm() != null)
            {
                predicates.add(QSocietyPost.societyPost.realm.eq(societyPostDTO.getRealm()));
            }
            if(StringUtils.isNotBlank(societyPostDTO.getAuthor())){
                predicates.add(QSocietyPost.societyPost.author.like("%" + societyPostDTO.getAuthor() + "%").or(QSocietyPost.societyPost.userId.in(societyPostDTO.getUserIds())));
            }else{
                if(!CollectionUtils.isEmpty(societyPostDTO.getUserIds()))
                {
                    predicates.add(QSocietyPost.societyPost.userId.in(societyPostDTO.getUserIds()));
                }
            }
            if(societyPostDTO.getId() != null && societyPostDTO.getId() != 0L)
            {
                predicates.add(QSocietyPost.societyPost.id.eq(societyPostDTO.getId()));
            }
            if(StringUtils.isNotBlank(societyPostDTO.getAuditState()))
            {
                predicates.add(QSocietyPost.societyPost.auditState.eq(societyPostDTO.getAuditState()));
            }
            if(StringUtils.isNotBlank(societyPostDTO.getPersonPub()))
            {
                if(StringUtils.equals(societyPostDTO.getPersonPub(),Constants.ISSUCCESS)){
                    predicates.add(QSocietyPost.societyPost.personPub.eq(true));
                }else{
                    predicates.add(QSocietyPost.societyPost.personPub.eq(false));
                }
            }
            //开始时间
            if(societyPostDTO.getBeginDate() != null)
            {
                predicates.add(QSocietyPost.societyPost.createDate.goe(DateTimeUtils.startTimeOfTheDate(societyPostDTO.getBeginDate())));
            }
            //结束时间
            if(societyPostDTO.getEndDate() != null)
            {
                predicates.add(QSocietyPost.societyPost.createDate.loe(DateTimeUtils.endTimeOfTheDate(societyPostDTO.getEndDate())));
            }
            if(StringUtils.isNotBlank(societyPostDTO.getIssue())){
                predicates.add(QSocietyPost.societyPost.issue.like("%" + societyPostDTO.getIssue() + "%"));
            }
        }
        predicates.add(QSocietyPost.societyPost.enableFlag.eq(EnableFlag.Y));
        return BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
    }

    /**
     * 置顶/取消置顶
     * @param societyPostVO 帖子对象
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String stickSocietyPost(SocietyPostVO societyPostVO){
        SocietyPost societyPost = societyPostDAO.findOne(societyPostVO.getId());
        if(societyPost == null){
            return Constants.ENABLE_NOT_NULL;
        }
        if(societyPostVO.getIsTop()){
            SocietyPost societyPost1 = societyPostDAO.findFirstByEnableFlagOrderByPrivDesc(EnableFlag.Y);
            if(societyPost1 == null){
                //从未有设置过置顶值的，则设为1
                societyPost.setPriv("1");
            }else{
                //最大的置顶值+1，为新的最大的置顶值
                societyPost.setPriv(StringUtils.isBlank(societyPost1.getPriv())?"1":String.valueOf(NumberUtils.createInteger(societyPost1.getPriv()) + 1));
            }
        }else{
            //取消置顶将置顶值设为null
            societyPost.setPriv(null);
        }
        societyPost.setLastUpdateDate(new Date());
        societyPostDAO.save(societyPost);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 修改信息
     * @param societyPostVO 帖子对象
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateSocietyPostInfo(SocietyPostVO societyPostVO){
        SocietyPost societyPost = societyPostDAO.findOne(societyPostVO.getId());
        if(societyPost == null){
            return Constants.ENABLE_NOT_NULL;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(societyPostVO,societyPost);
        societyPostDAO.save(societyPost);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 删除信息
     * @param societyPostVO 帖子对象
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteSocietyPostById(SocietyPostVO societyPostVO){
        SocietyPost societyPost = societyPostDAO.findOne(societyPostVO.getId());
        if(societyPost == null){
            return Constants.ENABLE_NOT_NULL;
        }
        societyPost.setEnableFlag(EnableFlag.N);
        societyPostDAO.save(societyPost);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 文章审核
     * @param societyPostVO 帖子对象
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String articleReview(SocietyPostVO societyPostVO){
        SocietyPost societyPost = societyPostDAO.findOne(societyPostVO.getId());
        if(societyPost == null){
            return Constants.ENABLE_NOT_NULL;
        }
        if(StringUtils.equals(societyPost.getAuditState(),Constants.ISSUCCESS) || StringUtils.equals(societyPost.getAuditState(),Constants.ISNOTSUCCESS)){
            return ErrorCodeConst.CANOT_DUMBLE_ENTRUE;
        }
        societyPost.setAuditState(societyPostVO.getAuditState());
        societyPost.setPassDate(new Date());
        societyPost.setLastUpdateDate(new Date());
        societyPostDAO.save(societyPost);
        //发送消息通知行情通过
        if(societyPost.getPersonPub() != null && societyPost.getPersonPub()){
            //发送审核结果的站内信息消息
            sendMessageMqInfo(String.valueOf(societyPost.getUserId()),societyPost);
        }
        return Constants.RETURN_SUCESS;
    }

    /**
     * 查询按时间降序，图片不为空的帖子/行情
     * @param societyPostDTO 查询条件
     * @param pageRequest 分页对象
     * @return 分页结果
     */
    @Override
    public PageDTO<SocietyPostViewVO> queryNewsListByTopPic(SocietyPostDTO societyPostDTO, PageRequest pageRequest){
        PageDTO<SocietyPostViewVO> result = new PageDTO<>();
        Page<SocietyPost> page = societyPostDAO.findByRealmAndEnableFlagAndAuditStateAndUrlNotNullOrderByCreateDateDesc(societyPostDTO.getRealm(),EnableFlag.Y,Constants.ISSUCCESS,pageRequest);
        if(page == null || page.getTotalElements() == 0L ){
            return result;
        }
        List<SocietyPostViewVO> societyPostViewVOS = new ArrayList<>();
        for(SocietyPost societyPost:page){
            SocietyPostViewVO societyPostViewVO = new SocietyPostViewVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(societyPost,societyPostViewVO);
            societyPostViewVO.setPubDateStr(CharUtil.getStrFromDate(societyPostViewVO.getCreateDate()));
            societyPostViewVOS.add(societyPostViewVO);
        }
        result.setRows(societyPostViewVOS);
        result.setTotalPages(page.getTotalPages());
        result.setTotalElements(page.getTotalElements());
        result.setNumber(page.getNumber() + 1);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String newQuotation() {
        SocietyPostVO societyPostVO = new SocietyPostVO();
        societyPostVO.setTitle("果价猜猜猜");
        societyPostVO.setContent("猜果价，赢积分");
        societyPostVO.setAuthor("系统");
        societyPostVO.setHasImage(false);
        //类型为行情竞猜
        societyPostVO.setRealm(Realm.QUOTATION);
        addSocietyPost(societyPostVO);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public QuotationView newsQuo(String userId) {
        List<SocietyPost> list = societyPostDAO.findByRealmAndEnableFlagAndStartTimeBeforeAndEndTimeAfterOrderByCreateDateDesc(Realm.QUOTATION,EnableFlag.Y,new Date(),new Date());
        if(CollectionUtils.isEmpty(list) && list.size() < 1){
            return null;
        }
        SocietyPost societyPost = list.get(0);
        QuotationView quotationView = new QuotationView();
        BeanUtil.copyPropertiesIgnoreNullFilds(societyPost,quotationView);
        quotationView.setIssue(DateTimeUtils.date2String(quotationView.getCreateDate(),"yyyyMMdd"));
        String high = cacheHashService.hget(RedisHashConstants.HASH_QUOTATION_PREFIX + quotationView.getId(), RedisHashConstants.HASH_QUOTATION_HIGH);
        String light = cacheHashService.hget(RedisHashConstants.HASH_QUOTATION_PREFIX + quotationView.getId(), RedisHashConstants.HASH_QUOTATION_LIGHT);
        //看涨的人数
        quotationView.setQuotationHigh(StringUtils.isBlank(high)?"0":high);
        //看跌的人数
        quotationView.setQuotationLight(StringUtils.isBlank(light)?"0":light);
        quotationView.setTotalUser((StringUtils.isBlank(high)?0:NumberUtils.createInteger(high)) + (StringUtils.isBlank(light)?0:NumberUtils.createInteger(light)));
        quotationView.setIsHigh(false);
        quotationView.setIsLight(false);
        if(StringUtils.isBlank(userId) || StringUtils.equals(Constants.ZERO,userId)){
            return quotationView;
        }
        String highUser = cacheHashService.hget(RedisHashConstants.HASH_QUOTATION_PREFIX + quotationView.getId(), RedisHashConstants.HASH_QUOTATION_HIGH_USER);
        if(StringUtils.isNotBlank(highUser)){
            if(highUser.contains(userId)){
                quotationView.setIsHigh(true);
                return quotationView;
            }
        }
        String lightUser = cacheHashService.hget(RedisHashConstants.HASH_QUOTATION_PREFIX + quotationView.getId(), RedisHashConstants.HASH_QUOTATION_LIGHT_USER);
        if(StringUtils.isNotBlank(lightUser)){
            if(lightUser.contains(userId)){
                quotationView.setIsLight(true);
            }
        }
        return quotationView;
    }

    @Override
    public QuotationView newsReviewQuo() {
//        List<SocietyPost> list = societyPostDAO.findByRealmAndEnableFlagOrderByCreateDateDesc(Realm.QUOTATION,);
        return null;
    }

    @Override
    public PageDTO<QuotationView> newsQuoPage(SocietyPostDTO societyPostDTO,PageRequest pageRequest, String userId) {
        PageDTO<QuotationView> result = new PageDTO<>();
        List<QuotationView> list = new ArrayList<>();
        Page<SocietyPost> page = societyPostDAO.findAll(getInputCondition(societyPostDTO),pageRequest);
        page.forEach(societyPost -> {
            QuotationView quotationView = new QuotationView();
            BeanUtil.copyPropertiesIgnoreNullFilds(societyPost,quotationView);
            quotationView.setAuditStateStr(quotationAudit(quotationView));
            String high = cacheHashService.hget(RedisHashConstants.HASH_QUOTATION_PREFIX + quotationView.getId(), RedisHashConstants.HASH_QUOTATION_HIGH);
            String light = cacheHashService.hget(RedisHashConstants.HASH_QUOTATION_PREFIX + quotationView.getId(), RedisHashConstants.HASH_QUOTATION_LIGHT);
            //看涨的人数
            quotationView.setQuotationHigh(StringUtils.isBlank(high)?"0":high);
            //看跌的人数
            quotationView.setQuotationLight(StringUtils.isBlank(light)?"0":light);
            quotationView.setTotalUser((StringUtils.isBlank(high)?0:NumberUtils.createInteger(high)) + (StringUtils.isBlank(light)?0:NumberUtils.createInteger(light)));
            list.add(quotationView);
        });
        if(page != null){
            result.setTotalPages(page.getTotalPages());
            result.setNumber(page.getNumber() + 1);
            result.setTotalElements(page.getTotalElements());
        }
        result.setRows(list);
        return result;
    }

    private String quotationAudit(QuotationView quotationView){
        String str = "未开始";
        Date date = new Date();
        if(quotationView.getPassDate() != null && !StringUtils.equals(quotationView.getAuditState(),Constants.ZERO)){
            //已经审核的情况
            str = "已结束";
        }else if(date.getTime() - quotationView.getEndTime().getTime() > 0){
            //当前时间已经超过下线时间
            str = "已下线";
        }else if(date.getTime() - quotationView.getStartTime().getTime() > 0){
            //当前时间已经超过上线时间
            str = "进行中";
        }
        return str;
    }

    @Override
    public String quotationReview(QuotationView quotationView, String userId) {
        SocietyPost societyPost = societyPostDAO.findOne(quotationView.getId());
        if(societyPost == null){
            return Constants.ENABLE_NOT_NULL;
        }
        if(StringUtils.equals(societyPost.getAuditState(),Constants.ISSUCCESS) || StringUtils.equals(societyPost.getAuditState(),Constants.ISNOTSUCCESS)){
            return ErrorCodeConst.CANOT_DUMBLE_ENTRUE;
        }
        if(societyPost.getEndTime().getTime() > (new Date()).getTime()){
            //未下线的竞猜不能评定结果
            return ErrorCodeConst.ERROR_AUDITSTATE;
        }
        societyPost.setAuditState(quotationView.getAuditState());
        societyPost.setPassDate(new Date());
        societyPost.setLastUpdateDate(new Date());
        societyPostDAO.save(societyPost);
        UserPointRecordVO userPointRecordVO = new UserPointRecordVO();
        List<Long> userIds = new ArrayList<>();
        List<String> users = new ArrayList<>();
        if(StringUtils.equals(quotationView.getAuditState(),Constants.ISSUCCESS)){
            //竞猜结果为涨
            String highUser = cacheHashService.hget(RedisHashConstants.HASH_QUOTATION_PREFIX + quotationView.getId(), RedisHashConstants.HASH_QUOTATION_HIGH_USER);
            users = CharUtil.getListFromStr(highUser);
        }
        else if(StringUtils.equals(quotationView.getAuditState(),Constants.ISNOTSUCCESS)){
            //竞猜结果为跌
            String lightUser = cacheHashService.hget(RedisHashConstants.HASH_QUOTATION_PREFIX + quotationView.getId(), RedisHashConstants.HASH_QUOTATION_LIGHT_USER);
            users = CharUtil.getListFromStr(lightUser);
        }
        if(CollectionUtils.isEmpty(users)){
            return Constants.RETURN_SUCESS;
        }
        users.forEach(user->{
            userIds.add(NumberUtils.createLong(user));
        });
        //给猜中的人发送竞猜结果通知
        sendMessageMqInfoForQuotation(userIds,societyPost);
        userPointRecordVO.setUserIds(userIds);
        userPointRecordVO.setRemark("行情竞猜获胜，获得积分奖励哦");
        userPointRecordVO.setPointRuleType(PointRuleType.SUCCESS_QUOTATION);
        userPointRecordVO.setPointsChanged(2);
        userPointRecordVO.setPointsChangedType(PointsChangedType.PLUS);
        userPointRecordVO.setId(idGen.nextId());
        //给获胜者加积分
        mqProducerClient.sendConcurrently(MqTag.USER_POINT_TAG_SUBSCRI_POINT.getKey(),String.valueOf(userPointRecordVO.getId()),userPointRecordVO);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 发送 通知行情竞猜结果结果的站内信
     */
    private void sendMessageMqInfoForQuotation(List<Long> receivers,SocietyPost societyPost){
        MessageBodyVO messageBodyVO = new MessageBodyVO();
        //系统通知
        messageBodyVO.setType(MessageType.NOTIFICATION);
        //使用动态模板发送
        messageBodyVO.setTemplateFlag(true);
        Map<String, String> contentMap = new HashMap<>(16);
        contentMap.put("title",StringUtils.isBlank(societyPost.getTitle())?"":societyPost.getTitle());
        contentMap.put("issue",StringUtils.isBlank(societyPost.getIssue())?"":societyPost.getIssue());
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //转换审核时间格式
        contentMap.put("passDate",sdf.format(societyPost.getPassDate()));
        contentMap.put("result",StringUtils.equals(societyPost.getAuditState(),Constants.ISSUCCESS)?"涨":"跌");
        messageBodyVO.setRealm(Realm.QUOTATION);
        messageBodyVO.setContentMap(contentMap);
        messageBodyVO.setTemplateKey(MessageTemplate.RESULT_OF_QUOTATION);
        //发送站内信
        MessageVO messageVO = new MessageVO();
        messageVO.setBody(messageBodyVO);
        messageVO.setId(idGen.nextId());
        //接收者
        messageVO.setReceivers(receivers);
        //用户状态
        messageVO.setMessageStatus(MessageStatus.NEW);
        mqProducerClient.sendConcurrently(MqTag.MESSAGE_TAG.getKey(),String.valueOf(messageVO.getId()),messageVO);
    }

    /**
     * 发送 通知文章审核结果的站内信
     */
    private void sendMessageMqInfo(String userId,SocietyPost societyPost){
        MessageBodyVO messageBodyVO = new MessageBodyVO();
        //系统通知
        messageBodyVO.setType(MessageType.NOTIFICATION);
        //使用动态模板发送
        messageBodyVO.setTemplateFlag(true);
        Map<String, String> contentMap = new HashMap<>(16);
        contentMap.put("title",StringUtils.isBlank(societyPost.getTitle())?"":societyPost.getTitle());
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //转换审核时间格式
        contentMap.put("passDate",sdf.format(societyPost.getPassDate()));
        contentMap.put("result",StringUtils.equals(societyPost.getAuditState(),Constants.ISSUCCESS)?"通过":"驳回");
        messageBodyVO.setRealm(Realm.ARTICLE);
        messageBodyVO.setContentMap(contentMap);
        messageBodyVO.setTemplateKey(MessageTemplate.RESULT_OF_ARTICLE);
        //发送站内信，通知用户积分变动消息
        MessageVO messageVO = new MessageVO();
        messageVO.setBody(messageBodyVO);
        messageVO.setId(idGen.nextId());
        List<Long> receivers = new ArrayList<>();
        receivers.add(NumberUtils.createLong(userId));
        //接收者
        messageVO.setReceivers(receivers);
        //用户状态
        messageVO.setMessageStatus(MessageStatus.NEW);
        mqProducerClient.sendConcurrently(MqTag.MESSAGE_TAG.getKey(),String.valueOf(messageVO.getId()),messageVO);
    }

    @Override
    public List<SocietyPostAggreVO> queryUserForRanking(int num, Date startTime, Date endTime) {

        Aggregation agg = newAggregation(
                match(Criteria.where("createDate").lt(endTime)),
                match(Criteria.where("createDate").gt(startTime)),
                group("userId").count().as("totalNum"),
                project("totalNum").and("userId").previousOperation(),
                match(Criteria.where("totalNum").gte(num)),
                sort(Sort.Direction.DESC, "totalNum")
        );

        AggregationResults<SocietyPostAggreVO> groupResults
                =  mongoTemplate.aggregate(agg, SocietyPost.class, SocietyPostAggreVO.class);
        return groupResults.getMappedResults();

    }

    @Override
    public List<SocietyPostAggreVO> queryUserForRankingForReply(int num, Date startTime, Date endTime) {

        Aggregation agg = newAggregation(
                match(Criteria.where("createDate").lt(endTime)),
                match(Criteria.where("createDate").gt(startTime)),
                group("userId").count().as("totalNum"),
                project("totalNum").and("userId").previousOperation(),
                match(Criteria.where("totalNum").gte(num)),
                sort(Sort.Direction.DESC, "totalNum")
        );

        AggregationResults<SocietyPostAggreVO> groupResults
                =  mongoTemplate.aggregate(agg, SocietyPostReply.class, SocietyPostAggreVO.class);
        return groupResults.getMappedResults();

    }



}
