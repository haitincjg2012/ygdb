package com.apec.cms.service;

import com.apec.cms.vo.ArticleVO;
import com.apec.cms.vo.ChannelListVO;
import com.apec.cms.vo.ChannelVO;
import com.apec.framework.common.enumtype.CategoryType;
import com.apec.framework.dto.UserInfoVO;

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

}
