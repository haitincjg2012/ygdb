package com.apec.cms.web;

import com.apec.cms.dto.NewsDTO;
import com.apec.cms.service.CmsService;
import com.apec.cms.vo.ArticleVO;
import com.apec.cms.vo.ChannelListVO;
import com.apec.cms.vo.ChannelVO;
import com.apec.cms.vo.NewsVO;
import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.dto.UserInfoVO;
import com.apec.framework.log.InjectLogger;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * cms模块接口
 *
 * @author yirder
 */
@RestController
@RequestMapping("/cms")
public class CmsController extends MyBaseController {

    @InjectLogger
   private Logger log;

    @Autowired
    private CmsService cmsService;

    /**
     * 获取分类和分类下的所有的栏目
     * @return ResultData
     */
    @RequestMapping(value = "/channelList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<List<ChannelListVO>> listChannel(@RequestBody String jsonStr) {
        ResultData<List<ChannelListVO>> resultData = new ResultData<>();
        try {
            List<ChannelListVO> resultMap = new ArrayList<>();
            String returnCode = cmsService.listChannelInfo(resultMap);
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                resultData.setData(resultMap);
                resultData.setSucceed(true);
            } else {
                setErrorResultDate(resultData, returnCode);
            }
        }catch (Exception e) {
            log.error("[Cms][channelList] Find Channel List Exception", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }
    /**
     * 创建一个栏目
     * @param jsonStr  channelName 栏目名称  whichCategory 栏目分类
     * @return ResultData
     */
    @RequestMapping(value = "/channelCreate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<String> createChannel(@RequestBody String jsonStr) {
        ResultData<String> resultData = new ResultData<>();
        try {
            ChannelVO channelVO  = getFormJSON(jsonStr,ChannelVO.class);
            String returnCode = cmsService.createChannelInfo(channelVO,getUserInfo(jsonStr));
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                resultData.setSucceed(true);
            } else {
                setErrorResultDate(resultData, returnCode);
            }
        } catch (BusinessException e) {
            log.error("[Cms][createChannel] Create Channel BusinessException", e);
            setErrorResultDate(resultData, e.getErrorCode());
        }catch (Exception e) {
            log.error("[Cms][createChannel]  Create Channel Exception", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     * 创建文章
     * @param jsonStr 字符
     * @return ResultData
     */
    @RequestMapping(value = "/articleCreate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<String> createArticle(@RequestBody String jsonStr) {
        ResultData<String> resultData = new ResultData<>();
        try {
            ArticleVO articleVO = getFormJSON(jsonStr,ArticleVO.class);
            boolean flag = articleVO.getCategory() == null || StringUtils.isBlank(articleVO.getChannelCode()) ||
                    StringUtils.isBlank(articleVO.getTitle()) || StringUtils.isBlank(articleVO.getContent());
            if(flag){
                log.warn("[CmsServiceImpl][createArticleInfo] Can't create Article  info . Parameter verification failed!");
                setErrorResultDate(resultData, Constants.COMMON_MISSING_PARAMS);
            }
            UserInfoVO userInfoVO = getUserInfo(jsonStr);
            if(userInfoVO == null){
                setErrorResultDate(resultData, Constants.SYS_ERROR);
            }
            if(articleVO.isPersonPub()){
                //如果是个人发布，则手动设置发布人姓名
                articleVO.setAuthor(userInfoVO.getName());
            }
            String returnCode = cmsService.createArticleInfo(articleVO,userInfoVO);
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                resultData.setSucceed(true);
            } else {
                setErrorResultDate(resultData, returnCode);
            }
        } catch (BusinessException e) {
            log.error("[Cms][createArticle] Create Article BusinessException", e);
            setErrorResultDate(resultData, e.getErrorCode());
        }catch (Exception e) {
            log.error("[Cms][createArticle]  Create Article Exception", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     * 根据ID查找文章
     * @param jsonStr 字符
     * @return ResultData
     */
    @RequestMapping(value = "/articleQueryById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String getArticleById(@RequestBody String jsonStr) {
        try {
            ArticleVO articleVO = getFormJSON(jsonStr,ArticleVO.class);
            articleVO = cmsService.queryById(articleVO);
            if(articleVO == null) {
                return super.getResultJSONStr(false, null, Constants.DATA_ISNULL);
            }
            return super.getResultJSONStr(true, articleVO, "", "");
        } catch (BusinessException e) {
            log.error("[Cms][createArticle] Create Article BusinessException", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[Cms][createArticle]  Create Article Exception", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 根据ID查找文章
     * @param jsonStr 字符
     * @return ResultData
     */
    @RequestMapping(value = "/articleUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<String> updateArticleById(@RequestBody String jsonStr) {
        ResultData<String> resultData = new ResultData<>();
        try {
            ArticleVO articleVO = getFormJSON(jsonStr,ArticleVO.class);
            String returnCode = cmsService.updateArticleInfo(articleVO, getUserInfo(jsonStr));
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                resultData.setSucceed(true);
            } else {
                setErrorResultDate(resultData, returnCode);
            }
        } catch (BusinessException e) {
            log.error("[Cms][updateArticleById] Create Article BusinessException", e);
            setErrorResultDate(resultData, e.getErrorCode());
        }catch (Exception e) {
            log.error("[Cms][updateArticleById]  Create Article Exception", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     * 根据ID查找文章
     * @param jsonStr 字符
     * @return ResultData
     */
    @RequestMapping(value = "/articleDelete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<String> deleteArticleById(@RequestBody String jsonStr) {
        ResultData<String> resultData = new ResultData<>();
        try {
            ArticleVO articleVO = getFormJSON(jsonStr,ArticleVO.class);
            String returnCode = cmsService.deleteArticleInfo(articleVO);
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                resultData.setSucceed(true);
            } else {
                setErrorResultDate(resultData, returnCode);
            }
        } catch (BusinessException e) {
            log.error("[Cms][updateArticleById] Create Article BusinessException", e);
            setErrorResultDate(resultData, e.getErrorCode());
        }catch (Exception e) {
            log.error("[Cms][updateArticleById]  Create Article Exception", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     * 文章审核
     * @param jsonStr 字符
     * @return ResultData
     */
    @RequestMapping(value = "/articleReview", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<String> articleReview(@RequestBody String jsonStr) {
        ResultData<String> resultData = new ResultData<>();
        try {
            ArticleVO articleVO = getFormJSON(jsonStr,ArticleVO.class);
            if(articleVO == null || StringUtils.isBlank(articleVO.getAuditState()) || articleVO.getId() == null || articleVO.getId() == 0L){
                setErrorResultDate(resultData, Constants.ERROR_100003);
                return resultData;
            }
            String returnCode = cmsService.articleReview(articleVO,String.valueOf(getUserId(jsonStr)));
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                resultData.setSucceed(true);
            } else {
                setErrorResultDate(resultData, returnCode);
            }
        } catch (BusinessException e) {
            log.error("[Cms][articleReview] ", e);
            setErrorResultDate(resultData, e.getErrorCode());
        }catch (Exception e) {
            log.error("[Cms][articleReview] ", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     * 获取栏目下所有的行情
     * @param jsonStr 字符
     * @return ResultData
     */
    @RequestMapping(value = "/newsList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<PageDTO<NewsVO>> queryNewsList(@RequestBody String jsonStr) {
        try {
            NewsDTO newsDTO = getFormJSON(jsonStr,NewsDTO.class);
            PageRequest pageRequest = genPageRequest(newsDTO);
            PageDTO<NewsVO> newsList = cmsService.queryNewsList(newsDTO, pageRequest);
            return super.getResultData(true, newsList, "");
        } catch (Exception e) {
            log.error("[Cms][createArticle]  Create Article Exception", e);
            return super.getResultData(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 获取我发布的栏目下所有的行情
     * @param jsonStr 字符
     * @return ResultData
     */
    @RequestMapping(value = "/myNewsList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<PageDTO<NewsVO>> myNewsList(@RequestBody String jsonStr) {
        try {
            NewsDTO newsDTO = getFormJSON(jsonStr,NewsDTO.class);
            PageRequest pageRequest = genPageRequest(newsDTO);
            PageDTO<NewsVO> newsList = cmsService.queryMyNewsList(newsDTO, pageRequest,String.valueOf(getUserId(jsonStr)));
            return super.getResultData(true, newsList, null);
        } catch (Exception e) {
            log.error("[Cms][myNewsList]  Create Article Exception", e);
            return super.getResultData(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 获取栏目下所有的行情
     * @param jsonStr 字符
     * @return ResultData
     */
    @RequestMapping(value = "/newsBannerList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<PageDTO<NewsVO>> newsBannerList(@RequestBody String jsonStr) {
        try {
            NewsDTO newsDTO = getFormJSON(jsonStr,NewsDTO.class);
            newsDTO.setPageSize(3);
            PageRequest pageRequest = genPageRequest(newsDTO);
            PageDTO<NewsVO> newsList = cmsService.queryNewsListByTopPic(newsDTO, pageRequest);
            return super.getResultData(true, newsList, "", "");
        } catch (Exception e) {
            log.error("[Cms][createArticle]  get Article list top n Exception", e);
            return super.getResultData(false, null, Constants.SYS_ERROR, "系统异常");
        }
    }


    /**
     * 获取栏目下的文章
     * @return ResultData
     */
    @RequestMapping(value = "/articleList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String listArticle(@RequestBody String jsonStr) {
        try {
            ArticleVO articleVO = getFormJSON(jsonStr,ArticleVO.class);
            List<ArticleVO> list = cmsService.listArticleInfo(articleVO);
            if(list == null){
                return super.getResultJSONStr(true, StringUtils.EMPTY, "", "");
            }else {
                return super.getResultJSONStr(true, list, "", "");
            }
        }catch (Exception e) {
            log.error("[Cms][listArticle] Find Article List Exception", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 用户是否关注了该文章的作者
     * @return ResultData
     */
    @RequestMapping(value = "/isAttentionArticleUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String isAttentionArticleUser(@RequestBody String jsonStr) {
        try {
            ArticleVO articleVO = getFormJSON(jsonStr,ArticleVO.class);
            if(articleVO == null || articleVO.getId() == null || articleVO.getId() == 0L){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            Map<String,Boolean> resultMap = new HashedMap();
            String result = cmsService.isAttentionArticleUser(articleVO,String.valueOf(getUserId(jsonStr)),resultMap);
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true, resultMap, null);
            }else{
                return super.getResultJSONStr(false, null, result);
            }
        }catch (Exception e) {
            log.error("[Cms][isAttentionArticleUser] Find Article List Exception", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 置顶文章
     * @return ResultData
     */
    @RequestMapping(value = "/stickArticle", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String stickArticle(@RequestBody String jsonStr) {
        try {
            ArticleVO articleVO = getFormJSON(jsonStr,ArticleVO.class);
            if(articleVO == null || articleVO.getId() == null || articleVO.getId() == 0L){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            String result = cmsService.stickArticle(articleVO);
            if (StringUtils.equals(result, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true, null, null);
            }else{
                return super.getResultJSONStr(false, null, result);
            }

        }catch (Exception e) {
            log.error("[Cms][stickArticle] Exception {} ", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 取消置顶文章
     * @return ResultData
     */
    @RequestMapping(value = "/closeStickArticle", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String closeStickArticle(@RequestBody String jsonStr) {
        try {
            ArticleVO articleVO = getFormJSON(jsonStr,ArticleVO.class);
            if(articleVO == null || articleVO.getId() == null || articleVO.getId() == 0L){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            String result = cmsService.closeStickArticle(articleVO);
            if (StringUtils.equals(result, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true, null, null);
            }else{
                return super.getResultJSONStr(false, null, result);
            }

        }catch (Exception e) {
            log.error("[Cms][closeStickArticle] Exception {} ", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 点赞文章
     * @return ResultData
     */
    @RequestMapping(value = "/praiseArticle", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String praiseArticle(@RequestBody String jsonStr) {
        try {
            ArticleVO articleVO = getFormJSON(jsonStr,ArticleVO.class);
            if(articleVO == null || articleVO.getId() == null || articleVO.getId() == 0L){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            String result = cmsService.praiseArticle(articleVO,String.valueOf(getUserId(jsonStr)));
            if (StringUtils.equals(result, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true, null, null);
            }else{
                return super.getResultJSONStr(false, null, result);
            }

        }catch (Exception e) {
            log.error("[Cms][stickArticle] Exception {} ", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }


}
