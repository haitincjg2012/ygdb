package com.apec.cms.web;

import com.apec.cms.dto.NewsDTO;
import com.apec.framework.common.util.BeanUtil;
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
import java.util.HashMap;
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
     */
    @RequestMapping(value = "/channelList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String listChannel(@RequestBody String jsonStr) {
        try {
            return super.getResultJSONStr(true, cmsService.listChannelInfo(), null);
        }catch (Exception e) {
            log.error("[Cms][channelList] Find Channel List Exception", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
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
                     StringUtils.isBlank(articleVO.getContent());
            if(flag){
                log.warn("[CmsServiceImpl][createArticleInfo] Can't create Article  info . Parameter verification failed!");
                setErrorResultDate(resultData, Constants.COMMON_MISSING_PARAMS);
            }
            UserInfoVO userInfoVO = getUserInfo(jsonStr);
            if(userInfoVO == null){
                setErrorResultDate(resultData, Constants.SYS_ERROR);
                return resultData;
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
     * 获取栏目下所有的行情
     * @param jsonStr 字符
     * @return ResultData
     */
    @RequestMapping(value = "/newsList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String queryNewsList(@RequestBody String jsonStr) {
        try {
            NewsDTO newsDTO = getFormJSON(jsonStr,NewsDTO.class);
            PageRequest pageRequest = genPageRequest(newsDTO);
            return super.getResultJSONStr(true, cmsService.queryArticlePage(newsDTO, pageRequest), null);
        } catch (Exception e) {
            log.error("[Cms][createArticle]  Create Article Exception", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
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


}
