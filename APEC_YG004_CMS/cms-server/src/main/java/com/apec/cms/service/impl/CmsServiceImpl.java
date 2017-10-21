package com.apec.cms.service.impl;

import com.apec.cms.dao.ArticleDAO;
import com.apec.cms.dao.ChannelDAO;
import com.apec.cms.dto.NewsDTO;
import com.apec.cms.model.Article;
import com.apec.cms.model.Channel;
import com.apec.cms.service.CmsService;
import com.apec.cms.util.CharUtil;
import com.apec.cms.util.SnowFlakeKeyGen;
import com.apec.cms.vo.ArticleVO;
import com.apec.cms.vo.ChannelListVO;
import com.apec.cms.vo.ChannelVO;
import com.apec.cms.vo.NewsVO;
import com.apec.framework.cache.CacheHashService;
import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.RedisHashConstants;
import com.apec.framework.common.enums.Enums;
import com.apec.framework.common.enums.MqTag;
import com.apec.framework.common.enums.Realm;
import com.apec.framework.common.enumtype.*;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.common.util.JsonUtil;
import com.apec.framework.dto.UserInfoVO;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.rockmq.client.MQProducerClient;
import com.apec.framework.rockmq.vo.MessageBodyVO;
import com.apec.framework.rockmq.vo.MessageVO;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CmsServiceImpl implements CmsService {

    @Autowired
    private ChannelDAO channelDAO;

    @Autowired
    private ArticleDAO articleDAO;

    @Autowired
    private SnowFlakeKeyGen idGen;

    @InjectLogger
    private Logger logger;

    @Autowired
    private MQProducerClient mqProducerClient;

    @Autowired
    private CacheHashService cacheHashService;  //缓存服务

    /**
     * 发送 通知文章审核结果的站内信
     */
    private void sendMessageMqInfo(String userId,Article article){
        MessageBodyVO messageBodyVO = new MessageBodyVO();
        messageBodyVO.setType(MessageType.NOTIFICATION);//系统通知
        messageBodyVO.setTemplateFlag(true);//使用动态模板发送
        Map<String, String> contentMap = new HashMap<>();
        contentMap.put("title",StringUtils.isBlank(article.getTitle())?"":article.getTitle());
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //转换审核时间格式
        contentMap.put("passDate",sdf.format(article.getPassDate()));
        contentMap.put("result",StringUtils.equals(article.getAuditState(),Constants.ISSUCCESS)?"通过":"驳回");
        messageBodyVO.setRealm(Realm.ARTICLE);
        messageBodyVO.setContentMap(contentMap);
        messageBodyVO.setTemplateKey(MessageTemplate.RESULT_OF_ARTICLE);
        //发送站内信，通知用户积分变动消息
        MessageVO messageVO = new MessageVO();
        messageVO.setBody(messageBodyVO);
        messageVO.setId(idGen.nextId());
        List<Long> receivers = new ArrayList<>();
        receivers.add(NumberUtils.createLong(userId));
        messageVO.setReceivers(receivers);//接收者
        messageVO.setMessageStatus(MessageStatus.NEW);//用户状态
        mqProducerClient.sendConcurrently(MqTag.MESSAGE_TAG.getKey(),String.valueOf(messageVO.getId()),messageVO);
    }

    @Override
    public String listChannelInfo(List<ChannelListVO> resultData) {
        List<Channel> list = channelDAO.findByEnableFlagOrderByCategory(EnableFlag.Y);
        CategoryType[] categoryTypes =  CategoryType.values();
        for(CategoryType categoryType : categoryTypes){
            resultData.add(new ChannelListVO(categoryType.name(),categoryType.getKey(), null));
        }
        if(list != null){
            CategoryType type = null ;
            int i = 0;
            List<ChannelVO> listVO = new ArrayList<>();
            Map<String,List<ChannelVO>> map = new HashMap<>();
            for(Channel channel : list){
                ChannelVO channelVO = new ChannelVO();
                BeanUtil.copyPropertiesIgnoreNullFilds(channel,channelVO);
                if(i == 0) { type = channel.getCategory(); }
                if(channel.getCategory() == type){
                    listVO.add(channelVO);
                }else{
                    map.put(type.name(),listVO);
                    listVO = new ArrayList<>();
                    listVO.add(channelVO);
                }
                type = channel.getCategory();
                i++;
            }
            if(type != null) { map.put(type.name(), listVO); }
            resultData.forEach(channelListVO -> {
                channelListVO.setList(map.get(channelListVO.getChannelKey()));
            });
        }
        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional
    public String createChannelInfo(ChannelVO channelVO,UserInfoVO userInfoVO) {
        boolean flag = channelVO.getCategory() == null || StringUtils.isBlank(channelVO.getName()) || StringUtils.isBlank(channelVO.getCode());
        if(flag){
            logger.warn("[CmsServiceImpl][createChannelInfo] Can't create channel info . Parameter verification failed!");
            return Constants.COMMON_MISSING_PARAMS;
        }
        Long num = channelDAO.countByCodeAndEnableFlag(channelVO.getCode(),EnableFlag.Y);
        if(num > 0){
            logger.warn("[CmsServiceImpl][createChannelInfo] Can't create channel info .The Channel encoding already exists");
            return ErrorCodeConst.ERROR_CMS_CHANNELCODE_EXISTS;
        }

        Channel channel = new Channel();
        BeanUtil.copyPropertiesIgnoreNullFilds(channelVO,channel);
        channel.setId(idGen.nextId());
        channel.setCreateBy(String.valueOf(userInfoVO.getUserId()));
        channel.setCreateDate(new Date());
        channel.setEnableFlag(EnableFlag.Y);

        channelDAO.save(channel);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String createArticleInfo(ArticleVO articleVO, UserInfoVO userInfoVO) {

        Channel channel = channelDAO.findByCodeAndEnableFlag(articleVO.getChannelCode(),EnableFlag.Y);
        if(channel == null){
            logger.warn("[CmsServiceImpl][createArticleInfo] Can't create Article info .The Channel encoding not exists");
            return ErrorCodeConst.ERROR_CMS_CHANNELCODE_EXISTS;
        }
        Object[] obj = articleDAO.getMaxOrdinal(String.valueOf(channel.getId()), EnableFlag.Y.name());
        Article article = new Article();
        BeanUtil.copyPropertiesIgnoreNullFilds(articleVO,article);
        article.setId(idGen.nextId());
        int orderId = 0;
        if(obj != null && obj[0] != null){
            orderId = Integer.valueOf(String.valueOf(obj[0])) + 1;
        }
        article.setOrdinal(orderId);
        if(!articleVO.isPersonPub()){
            //如果是平台系统发布，则不需要经过审核
            article.setAuditState(Constants.ISSUCCESS);
            article.setPassDate(new Date());
        }
        article.setCreateBy(String.valueOf(userInfoVO.getUserId()));
        article.setCreateDate(new Date());
        article.setPubDate(new Date());
        article.setEnableFlag(EnableFlag.Y);
        article.setChannel(channel);
        articleDAO.save(article);
        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional
    public String updateArticleInfo(ArticleVO articleVO, UserInfoVO userInfoVO) {
        boolean flag = articleVO.getCategory() == null || StringUtils.isBlank(articleVO.getChannelCode()) || articleVO.getId() == null
                || StringUtils.isBlank(articleVO.getTitle()) || StringUtils.isBlank(articleVO.getContent());
        if(flag){
            logger.warn("[CmsServiceImpl][updateArticleInfo] Can't create Article  info . Parameter verification failed!");
            return Constants.COMMON_MISSING_PARAMS;
        }
        Channel channel = channelDAO.findByCodeAndEnableFlag(articleVO.getChannelCode(),EnableFlag.Y);
        if(channel == null){
            logger.warn("[CmsServiceImpl][createArticleInfo] Can't create Article info .The Channel encoding not exists");
            return ErrorCodeConst.ERROR_CMS_CHANNELCODE_EXISTS;
        }
        Article article = new Article();
        BeanUtil.copyPropertiesIgnoreNullFilds(articleVO,article,"personPub","priority");
        article.setLastUpdateBy(String.valueOf(userInfoVO.getUserId()));//设置最后更新人
        article.setLastUpdateDate(new Date());//设置最后更新日期
        article.setChannel(channel);
        articleDAO.saveAndFlush(article);
        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional
    public String deleteArticleInfo(ArticleVO articleVO) {
        if(articleVO.getId() == null){
            logger.warn("[CmsServiceImpl][deleteArticleInfo] Can't create Article  info . Parameter verification failed!");
            return Constants.COMMON_MISSING_PARAMS;
        }
        Article article = articleDAO.findOne(articleVO.getId());
        if(article == null){
            return  Constants.DATA_ISNULL;
        }
        article.setEnableFlag(EnableFlag.N); //软删除
        articleDAO.saveAndFlush(article);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 文章审核
     * @param articleVO
     * @param userId
     * @return
     */
    @Override
    public String articleReview(ArticleVO articleVO, String userId) {
        Article article = articleDAO.findOne(articleVO.getId());
        if(article == null){
            return  Constants.DATA_ISNULL;
        }
        if(StringUtils.isNotBlank(article.getAuditState())){
            return ErrorCodeConst.CANOT_DUMBLE_ENTRUE;
        }
        article.setPassDate(new Date());
        article.setAuditState(articleVO.getAuditState());
        article.setLastUpdateDate(new Date());
        article.setLastUpdateBy(userId);
        articleDAO.saveAndFlush(article);
        //发送审核结果的站内信息消息
        sendMessageMqInfo(article.getCreateBy(),article);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public List<ArticleVO> listArticleInfo(ArticleVO articleVO) {
        boolean flag = articleVO.getCategory() == null || StringUtils.isBlank(articleVO.getChannelCode());
        if(flag){
            logger.warn("[CmsServiceImpl][listArticleInfo] Can't find  Article  info . Parameter verification failed!");
            return Collections.emptyList();
        }
        Channel channel = channelDAO.findByCodeAndEnableFlag(articleVO.getChannelCode(),EnableFlag.Y);
        if(channel == null){
            logger.warn("[CmsServiceImpl][listArticleInfo] Can't find Article info .The Channel encoding not exists");
            return  Collections.emptyList();
        }

        List<Article> list = articleDAO.findByChannelAndCategoryAndPriorityAndEnableFlagOrderByOrdinal(channel,articleVO.getCategory(),true,EnableFlag.Y);
        if(list == null || list.size() == 0) { return Collections.emptyList(); }
        List<ArticleVO> listArticle = new ArrayList<>();
        list.forEach(article -> {
            ArticleVO art = new ArticleVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(article,art);
            listArticle.add(art);
        });
        return listArticle;
    }

    @Override
    public ArticleVO queryById(ArticleVO articleVO) {
        if(articleVO.getId() == null){
            logger.warn("[CmsServiceImpl][queryById] Can't find  Article  info . Parameter verification failed!");
            return null;
        }
        ArticleVO art = new ArticleVO();
        Article article = articleDAO.queryArticleById(articleVO.getId(),EnableFlag.Y.name());
        if(article == null) {
            logger.warn("[CmsServiceImpl][queryById] Can't find  Article info !");
            return art;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(article,art);
        art.setChannelCode(article.getChannel().getCode());
        return art;
    }

    @Override
    public PageDTO<NewsVO> queryNewsList(NewsDTO newsDTO, PageRequest pageRequest) throws ParseException {
        PageDTO<NewsVO> res = new PageDTO<>();
        boolean flag = newsDTO.getCategory() == null || StringUtils.isBlank(newsDTO.getChannelCode());
        if(flag){
            logger.warn("[CmsServiceImpl][listArticleInfo] Can't find  Article  info . Parameter verification failed!");
            return new PageDTO<>();
        }
        Channel channel = channelDAO.findByCodeAndEnableFlag(newsDTO.getChannelCode(),EnableFlag.Y);
        if(channel == null){
            logger.warn("[CmsServiceImpl][listArticleInfo] Can't find Article info .The Channel encoding not exists");
            return  new PageDTO<>();
        }
        boolean nameFlag = !(newsDTO.getAuthor() == null || StringUtils.isBlank(newsDTO.getAuthor()));
        boolean beginDateFlag = !(newsDTO.getBeginDate() == null);
        boolean endDateFlag = !(newsDTO.getEndDate() == null);
        boolean personPubFlag = !(StringUtils.isBlank(newsDTO.getPersonPub()));
        boolean personPub = false;
        if(StringUtils.isNotBlank(newsDTO.getPersonPub())){
            if(StringUtils.equals(newsDTO.getPersonPub(),Constants.ISSUCCESS)){
                //为Y时查询平台用户
                personPub = true;
            }
        }
        boolean auditStateFlag = !StringUtils.isBlank(newsDTO.getAuditState());

        newsDTO.getPubDate();
        PageRequest page = new PageRequest(pageRequest.getPageNumber(),pageRequest.getPageSize());
        Page<Object[]> listModel = articleDAO.queryByChannelAndEnableFlagOrderByCreateDate(String.valueOf(channel.getId()), EnableFlag.Y.name(), nameFlag, newsDTO.getAuthor(),
                beginDateFlag, newsDTO.getBeginDate(), endDateFlag, newsDTO.getEndDate(),personPubFlag,personPub,auditStateFlag,newsDTO.getAuditState(), page);
        logger.info("[APP]Query News List: ChannelCode:{},size:{}",newsDTO.getChannelCode(),listModel.getTotalElements());
        List<NewsVO> listNews = getNewsVOS(listModel);
        res.setNumber(listModel.getNumber()+1);
        res.setRows(listNews);
        res.setTotalElements(listModel.getTotalElements());
        res.setTotalPages(listModel.getTotalPages());
        return res;
    }

    /**
     * 查询我的行情列表
     * @param newsDTO
     * @param pageRequest
     * @return
     */
    @Override
    public PageDTO<NewsVO> queryMyNewsList(NewsDTO newsDTO, PageRequest pageRequest,String userId) throws ParseException{
        PageDTO<NewsVO> res = new PageDTO<>();
        boolean flag = newsDTO.getCategory() == null || StringUtils.isBlank(newsDTO.getChannelCode());
        if(flag){
            logger.warn("[CmsServiceImpl][listArticleInfo] Can't find  Article  info . Parameter verification failed!");
            return new PageDTO<>();
        }
        Channel channel = channelDAO.findByCodeAndEnableFlag(newsDTO.getChannelCode(),EnableFlag.Y);
        if(channel == null){
            logger.warn("[CmsServiceImpl][listArticleInfo] Can't find Article info .The Channel encoding not exists");
            return  new PageDTO<>();
        }
        boolean auditStateFlag = !StringUtils.isBlank(newsDTO.getAuditState());

        newsDTO.getPubDate();
        PageRequest page = new PageRequest(pageRequest.getPageNumber(),pageRequest.getPageSize());
        Page<Object[]> listModel = articleDAO.queryMyNewsList(String.valueOf(channel.getId()), EnableFlag.Y.name(),auditStateFlag,newsDTO.getAuditState(),userId, page);
        logger.info("[APP]Query News List: ChannelCode:{},size:{}",newsDTO.getChannelCode(),listModel.getTotalElements());
        List<NewsVO> listNews = getNewsVOS(listModel);
        res.setNumber(listModel.getNumber()+1);
        res.setRows(listNews);
        res.setTotalElements(listModel.getTotalElements());
        res.setTotalPages(listModel.getTotalPages());
        return res;
    }

    @Override
    public PageDTO<NewsVO> queryNewsListByTopPic(NewsDTO newsDTO, PageRequest pageRequest) throws ParseException {
        PageDTO<NewsVO> res = new PageDTO<>();
        boolean flag = newsDTO.getCategory() == null || StringUtils.isBlank(newsDTO.getChannelCode());
        if(flag){
            logger.warn("[CmsServiceImpl][listArticleInfo] Can't find  Article  info . Parameter verification failed!");
            return new PageDTO<>();
        }
        Channel channel = channelDAO.findByCodeAndEnableFlag(newsDTO.getChannelCode(),EnableFlag.Y);
        if(channel == null){
            logger.warn("[CmsServiceImpl][listArticleInfo] Can't find Article info .The Channel encoding not exists");
            return  new PageDTO<>();
        }
        PageRequest page = new PageRequest(pageRequest.getPageNumber(),pageRequest.getPageSize()); //默认去top3的数据
        Page<Object[]> listModel = articleDAO.queryByChannelAndEnableFlagOrderByCreateDate(String.valueOf(channel.getId()), EnableFlag.Y.name(), page);
        logger.info("[APP]Query News List: ChannelCode:{},size:{}",newsDTO.getChannelCode(),listModel.getTotalElements());
        List<NewsVO> listNews = getNewsVOS(listModel);
        res.setNumber(listModel.getNumber()+1);
        res.setRows(listNews);
        res.setTotalElements(listModel.getTotalElements());
        res.setTotalPages(listModel.getTotalPages());
        return res;
    }

    private List<NewsVO> getNewsVOS(Page<Object[]> listModel) throws ParseException {
        List<NewsVO> listNews = new ArrayList<>();
        if(!CollectionUtils.isEmpty(listModel.getContent())) {
            String obj;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for(Object[] ob : listModel.getContent()){
                NewsVO newsVO = new NewsVO();
                obj = String.valueOf(ob[0]);
                newsVO.setId(Long.valueOf(obj));
                obj = String.valueOf(ob[1]);
                newsVO.setCreateBy(StringUtils.isBlank(obj)||StringUtils.equals(obj,"null")?"":obj);
                obj = String.valueOf(ob[2]);
                if(!(StringUtils.isBlank(obj)||StringUtils.equals(obj,"null"))){
                    Date d = sdf.parse(obj);
                    newsVO.setCreateDate(d);
                }
                obj = String.valueOf(ob[4]);
                newsVO.setAddress(StringUtils.isBlank(obj)||StringUtils.equals(obj,"null")?"":obj);
                obj = String.valueOf(ob[5]);
                newsVO.setAuthor(StringUtils.isBlank(obj)||StringUtils.equals(obj,"null")?"":obj);
                obj = String.valueOf(ob[6]);
                CategoryType categoryType = Enums.getEnumByNameOrNull(CategoryType.class, obj);
                newsVO.setCategory(categoryType);
                obj = String.valueOf(ob[8]);
                newsVO.setMedia(StringUtils.isBlank(obj)||StringUtils.equals(obj,"null")?"":obj);
                obj = String.valueOf(ob[9]);
                newsVO.setPriv(StringUtils.isBlank(obj)||StringUtils.equals(obj,"null")?"":obj);
                obj = String.valueOf(ob[10]);
                if(!(StringUtils.isBlank(obj)||StringUtils.equals(obj,"null"))){
                    Date d = sdf.parse(obj);
                    newsVO.setPubDate(d);
                }
                obj = String.valueOf(ob[11]);
                newsVO.setTitle(StringUtils.isBlank(obj)||StringUtils.equals(obj,"null")?"":obj);
                obj = String.valueOf(ob[12]);
                newsVO.setUrl(StringUtils.isBlank(obj)||StringUtils.equals(obj,"null")?"":obj);
                obj = String.valueOf(ob[13]);
                newsVO.setChannelId(StringUtils.isBlank(obj)||StringUtils.equals(obj,"null")? 0: Long.valueOf(obj));
                obj = String.valueOf(ob[14]);
                newsVO.setOrdinal(StringUtils.isBlank(obj)||StringUtils.equals(obj,"null")? 0: Integer.valueOf(obj));
                obj = String.valueOf(ob[15]);
                newsVO.setPersonPub(StringUtils.isBlank(obj)?false:new Boolean(obj));
                obj = String.valueOf(ob[16]);
                newsVO.setAuditState(StringUtils.isBlank(obj)||StringUtils.equals(obj,"null")?"":obj);
                obj = String.valueOf(ob[17]);
                if(!(StringUtils.isBlank(obj)||StringUtils.equals(obj,"null"))){
                    Date d = sdf.parse(obj);
                    newsVO.setPassDate(d);
                }
                newsVO.setPubDateStr(CharUtil.getStrFromDate(newsVO.getCreateDate()));
                String readNum = cacheHashService.hget(RedisHashConstants.HASH_ARTICLE_PREFIX+newsVO.getId(),RedisHashConstants.HASH_ARTICLE_READ_NUM);
                newsVO.setReadNum(StringUtils.isBlank(readNum)?"0":readNum);
                listNews.add(newsVO);
            }
        }
        return listNews;
    }

    /**
     * 用户是否关注了该文章的作者
     * @param articleVO
     * @return
     */
    @Override
    public String isAttentionArticleUser(ArticleVO articleVO, String userId,Map<String,Boolean> resultMap){
        boolean isAttention = false;
        Article article = articleDAO.findOne(articleVO.getId());
        if(article == null) {
            logger.warn("[CmsServiceImpl][queryById] Can't find  Article info !");
            return Constants.DATA_ISNULL;
        }
        String id = article.getCreateBy();
        String userInfoJson = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + id,RedisHashConstants.HASH_OBJCONTENT_CACHE);
        UserInfoVO userInfo ;
        if(StringUtils.isBlank(userInfoJson)){
            //获取不到数据,记录日志
            logger.warn("[UserServiceImpl][updateUserInfoCache]Can't find user hash cache. userNo:{}",id);
            userInfo = new UserInfoVO();
        }else{
            userInfo = JsonUtil.parseObject(userInfoJson,UserInfoVO.class);
        }
        Long userOrgId = userInfo.getUserOrgId();
        if(userOrgId != null && userOrgId != 0L){
            String userOrgIds = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.ORG_SAVE);
            if(StringUtils.isNotBlank(userOrgIds)){
                String[] orgIds = userOrgIds.split(",");
                logger.info("the attention userOrgs is {} , the article author's org is {}",userOrgIds,userOrgId);
                isAttention = Arrays.asList(orgIds).contains(String.valueOf(userOrgId));
            }
        }
        resultMap.put("attentionArticleUser",isAttention);
        boolean isPraise = false;
        String articlePraise = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.ARTICLE_PRAISE);
        if(StringUtils.isNotBlank(articlePraise)){
            String[] articlePraiseArray = articlePraise.split(",",-1);
            logger.info("the attention userOrgs is {} , the article author's org is {}",articlePraise,articleVO.getId());
            isPraise = Arrays.asList(articlePraiseArray).contains(String.valueOf(articleVO.getId()));
        }
        resultMap.put("praiseArticle",isPraise);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 置顶文章
     * @param articleVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String stickArticle(ArticleVO articleVO){
        Article article = articleDAO.findOne(articleVO.getId());
        if(article == null) {
            logger.warn("[CmsServiceImpl][stickArticle] Can't find  Article info !");
            return Constants.DATA_ISNULL;
        }
        articleDAO.setStickArticle(article.getId(),EnableFlag.Y.name());
        return Constants.RETURN_SUCESS;
    }

    /**
     * 置顶文章
     * @param articleVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String closeStickArticle(ArticleVO articleVO){
        Article article = articleDAO.findOne(articleVO.getId());
        if(article == null) {
            logger.warn("[CmsServiceImpl][stickArticle] Can't find  Article info !");
            return Constants.DATA_ISNULL;
        }
        if(StringUtils.isBlank(article.getPriv())){
            return ErrorCodeConst.ERROR_ARTICLE_PRIV;
        }
        articleDAO.closeStickArticle(article.getId(),EnableFlag.Y.name());
        return Constants.RETURN_SUCESS;
    }

    /**
     * 点赞文章
     * @param articleVO
     * @return
     */
    @Override
    public String praiseArticle(ArticleVO articleVO,String userId){
        Article article = articleDAO.findOne(articleVO.getId());
        if(article == null) {
            logger.warn("[CmsServiceImpl][praiseArticle] Can't find  Article info !");
            return Constants.DATA_ISNULL;
        }
        String articlePraise = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.ARTICLE_PRAISE);
        if(StringUtils.isBlank(articlePraise)){
            articlePraise = String.valueOf(article.getId());
        }else{
            articlePraise = articlePraise + "," + String.valueOf(article.getId());
        }
        //讲文章id保存在用户redis中
        cacheHashService.hset(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.ARTICLE_PRAISE,articlePraise);
        int praiseNum = 0;
        //查询文章的点赞数
        String praiseNumStr = cacheHashService.hget(RedisHashConstants.HASH_ARTICLE_PREFIX + article.getId(),RedisHashConstants.HASH_ARTICLE_PRAISE_NUM);
        if(StringUtils.isNotBlank(praiseNumStr)){
            praiseNum = Integer.valueOf(praiseNumStr);
        }
        praiseNum++;
        cacheHashService.hset(RedisHashConstants.HASH_ARTICLE_PREFIX + article.getId(),RedisHashConstants.HASH_ARTICLE_PRAISE_NUM,String.valueOf(praiseNum));
        return Constants.RETURN_SUCESS;
    }


}
