package com.apec.cms.service;

import com.apec.cms.dto.NewsDTO;
import com.apec.cms.vo.ArticleVO;
import com.apec.cms.vo.ChannelListVO;
import com.apec.cms.vo.ChannelVO;
import com.apec.cms.vo.NewsVO;
import com.apec.framework.common.PageDTO;
import com.apec.framework.dto.UserInfoVO;
import org.springframework.data.domain.PageRequest;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 *  CMS相关服务
 */
public interface CmsService {

    /**
     * 查询所有的栏目
     * @return ReturnCode
     */
   String listChannelInfo(List<ChannelListVO> resultData);

    /**
     * 创建栏目
     * @param  userInfoVO 用户VO
     * @param channelVO 栏目VO
     * @return ReturnCode
     */
    String createChannelInfo(ChannelVO channelVO, UserInfoVO userInfoVO);

    /**
     * 创建文章
     * @param articleVO
     * @param userInfoVO
     * @return
     */
    String createArticleInfo(ArticleVO articleVO, UserInfoVO userInfoVO);

    /**
     *
     * @param articleVO
     * @return
     */
    List<ArticleVO> listArticleInfo(ArticleVO articleVO);


    /**
     * 查询行情列表
     * @param newsDTO
     * @param pageRequest
     * @return
     */
    PageDTO<NewsVO> queryNewsList(NewsDTO newsDTO, PageRequest pageRequest) throws ParseException;

   /**
    * 查询我的行情列表
    * @param newsDTO
    * @param pageRequest
    * @return
    */
    PageDTO<NewsVO> queryMyNewsList(NewsDTO newsDTO, PageRequest pageRequest,String userId) throws ParseException;

    /**
     *
     * @param newsDTO
     * @return
     * @throws ParseException
     */
    PageDTO<NewsVO> queryNewsListByTopPic(NewsDTO newsDTO, PageRequest pageRequest) throws ParseException;

    /**
     * 根据ID查询文章
     * @param articleVO
     * @return
     */
    ArticleVO queryById(ArticleVO articleVO);

    /**
     * 修改文章
     * @param articleVO
     * @param userInfoVO
     * @return
     */
    String updateArticleInfo(ArticleVO articleVO, UserInfoVO userInfoVO);

     /**
      * 删除文章
      * @param articleVO
      * @return
      */
     String deleteArticleInfo(ArticleVO articleVO);

    /**
     * 文章审核
     * @param articleVO
     * @return
     */
    String articleReview(ArticleVO articleVO, String userId);

    /**
     * 用户是否关注了该文章的作者
     * @param articleVO
     * @return
     */
    String isAttentionArticleUser(ArticleVO articleVO, String userId,Map<String,Object> resultMap);

    /**
     * 置顶文章
     * @param articleVO
     * @return
     */
    String stickArticle(ArticleVO articleVO);

    /**
     * 取消置顶文章
     * @param articleVO
     * @return
     */
    String closeStickArticle(ArticleVO articleVO);

    /**
     * 点赞文章
     * @param articleVO
     * @return
     */
    String praiseArticle(ArticleVO articleVO,String userId);



}
