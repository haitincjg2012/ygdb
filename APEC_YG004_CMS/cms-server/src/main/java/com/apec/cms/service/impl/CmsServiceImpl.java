package com.apec.cms.service.impl;

import com.apec.cms.dao.ArticleDAO;
import com.apec.cms.dao.ChannelDAO;
import com.apec.cms.dto.NewsDTO;
import com.apec.cms.model.Article;
import com.apec.cms.model.QArticle;
import com.apec.cms.model.Channel;
import com.apec.cms.service.CmsService;
import com.apec.cms.util.SnowFlakeKeyGen;
import com.apec.cms.vo.ArticleVO;
import com.apec.cms.vo.ChannelVO;
import com.apec.framework.cache.CacheHashService;
import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.RedisHashConstants;
import com.apec.framework.common.enumtype.*;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.dto.UserInfoVO;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.rockmq.client.MqProducerClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * @author xxx
 */
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
    private MqProducerClient mqProducerClient;

    /**
     *   //缓存服务
     */
    @Autowired
    private CacheHashService cacheHashService;

    private UserInfoVO getUserInfo(String userId){
        String userInfoJson = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.HASH_OBJCONTENT_CACHE);
        UserInfoVO userInfo ;
        if(StringUtils.isBlank(userInfoJson)){
            //获取不到数据,记录日志
            logger.warn("[CmsServiceImpl][getUserInfo]Can't find user hash cache. userNo:{}",userId);
            userInfo = new UserInfoVO();
        }else{
            userInfo = BaseJsonUtil.parseObject(userInfoJson,UserInfoVO.class);
        }
        return userInfo;
    }

    @Override
    public List<ChannelVO> listChannelInfo() {
        List<ChannelVO> channelVOList = new ArrayList<>();
        List<Channel> list = channelDAO.findByEnableFlagOrderByCategory(EnableFlag.Y);
        if(list != null){
            list.forEach(channel -> {
                ChannelVO channelVO = new ChannelVO();
                BeanUtil.copyPropertiesIgnoreNullFilds(channel,channelVO);
                channelVOList.add(channelVO);
            });
        }
        return channelVOList;
    }

    @Override
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
        article.setPriority(true);
        article.setOrdinal(orderId);
        article.setCreateBy(String.valueOf(userInfoVO.getUserId()));
        article.setCreateDate(new Date());
        article.setEnableFlag(EnableFlag.Y);
        article.setChannel(channel);
        articleDAO.save(article);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String updateArticleInfo(ArticleVO articleVO, UserInfoVO userInfoVO) {
        boolean flag =  articleVO.getId() == null;
        if(flag){
            logger.warn("[CmsServiceImpl][updateArticleInfo] Can't create Article  info . Parameter verification failed!");
            return Constants.COMMON_MISSING_PARAMS;
        }
        Article article = articleDAO.findOne(articleVO.getId());
        BeanUtil.copyPropertiesIgnoreNullFilds(articleVO,article,"priority");
        article.setUrl(articleVO.getUrl());
        //设置最后更新人
        article.setLastUpdateBy(String.valueOf(userInfoVO.getUserId()));
        //设置最后更新日期
        article.setLastUpdateDate(new Date());
        articleDAO.save(article);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String deleteArticleInfo(ArticleVO articleVO) {
        if(articleVO.getId() == null){
            logger.warn("[CmsServiceImpl][deleteArticleInfo] Can't create Article  info . Parameter verification failed!");
            return Constants.COMMON_MISSING_PARAMS;
        }
        Article article = articleDAO.findOne(articleVO.getId());
        if(article == null){
            return  Constants.DATA_ISNULL;
        }
        //软删除
        article.setEnableFlag(EnableFlag.N);
        articleDAO.saveAndFlush(article);
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
        Article article = articleDAO.findOne(articleVO.getId());
        if(article == null) {
            logger.warn("[CmsServiceImpl][queryById] Can't find  Article info !");
            return art;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(article,art);
        art.setChannelCode(article.getChannel().getCode());
        return art;
    }


    @Override
    public PageDTO<ArticleVO> queryArticlePage(NewsDTO newsDTO, PageRequest pageRequest){
        PageDTO<ArticleVO> res = new PageDTO<>();
        List<ArticleVO> listNews = new ArrayList<>();
        Page<Article> page = articleDAO.findAll(getInputCondition(newsDTO),pageRequest);
        page.forEach(article -> {
            ArticleVO articleVO1 = new ArticleVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(article,articleVO1);
            articleVO1.setChannelId(article.getChannel().getId());
            articleVO1.setChannelCode(article.getChannel().getCode());
            articleVO1.setChannelName(article.getChannel().getName());
            listNews.add(articleVO1);
        });

        res.setNumber(page.getNumber()+1);
        res.setRows(listNews);
        res.setTotalElements(page.getTotalElements());
        res.setTotalPages(page.getTotalPages());
        return res;
    }

    /**
     * 多条件查询帖子信息
     * 根据多种情况查询
     * 包括like:title,content,,eq:category，ChannelId，enableFlag
     * @param articleVO 查询条件对象
     * @return Predicate
     */
    private Predicate getInputCondition(NewsDTO newsDTO)
    {
        List<BooleanExpression> predicates = new ArrayList<>();
        if(null != newsDTO)
        {
            if(!StringUtils.isEmpty(newsDTO.getTitle()))
            {
                predicates.add(QArticle.article.title.like("%" + newsDTO.getTitle() + "%"));
            }
            if(newsDTO.getCategory() != null)
            {
                predicates.add(QArticle.article.category.eq(newsDTO.getCategory()));
            }
            if(newsDTO.getChannelId() != null && newsDTO.getChannelId() != 0L)
            {
                predicates.add(QArticle.article.channel.id.eq(newsDTO.getChannelId()));
            }
            if(newsDTO.getStartDate() != null){
                predicates.add(QArticle.article.createDate.goe(newsDTO.getStartDate()));
            }
            if(newsDTO.getEndDate() != null){
                predicates.add(QArticle.article.createDate.loe(newsDTO.getEndDate()));
            }
        }
        predicates.add(QArticle.article.enableFlag.eq(EnableFlag.Y));
        return BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
    }

}
