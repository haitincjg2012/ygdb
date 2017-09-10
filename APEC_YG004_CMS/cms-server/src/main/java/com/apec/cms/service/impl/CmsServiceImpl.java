package com.apec.cms.service.impl;

import com.apec.cms.dao.ArticleDAO;
import com.apec.cms.dao.ChannelDAO;
import com.apec.cms.model.Article;
import com.apec.cms.model.Channel;
import com.apec.cms.service.CmsService;
import com.apec.cms.util.SnowFlakeKeyGen;
import com.apec.cms.vo.ArticleVO;
import com.apec.cms.vo.ChannelListVO;
import com.apec.cms.vo.ChannelVO;
import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.enumtype.CategoryType;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.dto.UserInfoVO;
import com.apec.framework.log.InjectLogger;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                if(i == 0) type = channel.getCategory();
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
            if(type != null)  map.put(type.name(), listVO);
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
        boolean flag = articleVO.getCategory() == null || StringUtils.isBlank(articleVO.getChannelCode());
        if(flag){
            logger.warn("[CmsServiceImpl][createArticleInfo] Can't create Article  info . Parameter verification failed!");
            return Constants.COMMON_MISSING_PARAMS;
        }
        Channel channel = channelDAO.findByCodeAndEnableFlag(articleVO.getChannelCode(),EnableFlag.Y);
        if(channel == null){
            logger.warn("[CmsServiceImpl][createArticleInfo] Can't create Article info .The Channel encoding not exists");
            return ErrorCodeConst.ERROR_CMS_CHANNELCODE_EXISTS;
        }

        Article article = new Article();
        BeanUtil.copyPropertiesIgnoreNullFilds(articleVO,article);
        article.setId(idGen.nextId());
        article.setCreateBy(String.valueOf(userInfoVO.getUserId()));
        article.setCreateDate(new Date());
        article.setEnableFlag(EnableFlag.Y);
        article.setChannel(channel);
        articleDAO.save(article);
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
        if(list == null || list.size() == 0) return Collections.emptyList();
        List<ArticleVO> listArticle = new ArrayList<>();
        list.forEach(article -> {
            ArticleVO art = new ArticleVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(article,art);
            listArticle.add(art);
        });
        return listArticle;
    }
}
