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
 * CMS相关服务
 *  @author hmy
 */
public interface CmsService {

    /**
     * 创建栏目
     * @param  userInfoVO 用户VO
     * @param channelVO 栏目VO
     * @return ReturnCode
     */
    String createChannelInfo(ChannelVO channelVO, UserInfoVO userInfoVO);

    /**
     * 查询所有的栏目
     * @param resultData resultData
     * @return String
     */
    List<ChannelVO> listChannelInfo();

    /**
     * 创建文章
     * @param articleVO 行情对象
     * @param userInfoVO 用户对象
     * @return 处理结果
     */
    String createArticleInfo(ArticleVO articleVO, UserInfoVO userInfoVO);

   /**
    * 查询所有的行情消息
    * @param articleVO 查询条件对象
    * @return 所有满足条件的对象集合
    */
   List<ArticleVO> listArticleInfo(ArticleVO articleVO);

    /**
     * 查询所有的行情信息
     * @param newsDTO 查询条件
     * @param pageRequest 分页条 件
     * @return 分页结果
     */
    PageDTO<ArticleVO> queryArticlePage(NewsDTO newsDTO, PageRequest pageRequest);

    /**
     * 根据ID查询文章
     * @param articleVO articleVO
     * @return ArticleVO
     */
    ArticleVO queryById(ArticleVO articleVO);

    /**
     * 修改文章
     * @param articleVO 文章对象
     * @param userInfoVO 用户信息对象
     * @return 处理结果
     */
    String updateArticleInfo(ArticleVO articleVO, UserInfoVO userInfoVO);

     /**
      * 删除文章
      * @param articleVO 文章对象
      * @return 处理结果
      */
     String deleteArticleInfo(ArticleVO articleVO);

}
