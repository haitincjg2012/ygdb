package com.apec.cms.service.impl;

import com.apec.cms.dao.ArticleDAO;
import com.apec.cms.dao.ChannelDAO;
import com.apec.cms.dto.NewsDTO;
import com.apec.cms.model.Article;
import com.apec.cms.model.Channel;
import com.apec.cms.service.CmsService;
import com.apec.cms.util.SnowFlakeKeyGen;
import com.apec.cms.vo.ArticleVO;
import com.apec.cms.vo.ChannelListVO;
import com.apec.cms.vo.ChannelVO;
import com.apec.cms.vo.NewsVO;
import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.enums.Enums;
import com.apec.framework.common.enumtype.CategoryType;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.dto.UserInfoVO;
import com.apec.framework.log.InjectLogger;
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
        boolean flag = articleVO.getCategory() == null || StringUtils.isBlank(articleVO.getChannelCode()) ||
                StringUtils.isBlank(articleVO.getTitle()) || StringUtils.isBlank(articleVO.getContent());
        if(flag){
            logger.warn("[CmsServiceImpl][createArticleInfo] Can't create Article  info . Parameter verification failed!");
            return Constants.COMMON_MISSING_PARAMS;
        }
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
        if(obj != null || obj[0] != null){
            orderId = Integer.valueOf(String.valueOf(obj[0])) + 1;
        }
        article.setOrdinal(orderId);
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
        BeanUtil.copyPropertiesIgnoreNullFilds(articleVO,article);
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
            return null;
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
        PageRequest page = new PageRequest(pageRequest.getPageNumber(),pageRequest.getPageSize());
        Page<Object[]> listModel = articleDAO.queryByChannelAndEnableFlagOrderByOrdinal(String.valueOf(channel.getId()), EnableFlag.Y.name(), page);
        logger.info("[APP]Query News List: ChannelCode:{},size:{}",newsDTO.getChannelCode(),listModel.getTotalElements());
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
                if(obj != null){
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
                if(obj != null){
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
                listNews.add(newsVO);
            }
        }
        res.setNumber(listModel.getNumber()+1);
        res.setRows(listNews);
        res.setTotalElements(listModel.getTotalElements());
        res.setTotalPages(listModel.getTotalPages());
        return res;
    }
}
