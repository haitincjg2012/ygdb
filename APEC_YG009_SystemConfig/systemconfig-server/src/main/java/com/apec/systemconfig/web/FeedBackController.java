package com.apec.systemconfig.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.enumtype.FeedBackType;
import com.apec.framework.dto.UserInfoVO;
import com.apec.framework.dto.UserViewVO;
import com.apec.framework.log.InjectLogger;
import com.apec.systemconfig.dto.FeedBackDTO;
import com.apec.systemconfig.service.FeedBackService;
import com.apec.systemconfig.vo.FeedBackVO;
import com.apec.systemconfig.vo.ViewVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hmy on 2017/8/28.
 * @author hmy
 */
@RestController
@RequestMapping(value = "/feedBack")
public class FeedBackController extends MyBaseController {

    @InjectLogger
    private Logger logger;

    @Autowired
    private FeedBackService feedBackService;

    /**
     * 增加用户反馈信息
     */
    @RequestMapping(value = "/saveFeedBackInfo",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String saveFeedBackInfo(@RequestBody String json){
        try{
            UserInfoVO userInfo = getUserInfo(json);
            FeedBackVO feedBackVO = getFormJSON(json,FeedBackVO.class);
            if(feedBackVO == null || StringUtils.isBlank(feedBackVO.getFeedBackInfo())){
                //反馈信息不能为空
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            if(feedBackVO.getFeedBackType() == FeedBackType.JB){
                //如果是举报，则被举报人和关联数据不能为空
                Boolean flag = feedBackVO.getRelatedIds() == null || feedBackVO.getReportedUserId() == null || StringUtils.isBlank(feedBackVO.getReportedUser());
                if(flag){
                    return super.getResultJSONStr(false,null, Constants.ERROR_100003);
                }
            }else{
                feedBackVO.setFeedBackType(FeedBackType.FK);
            }
            //用户直接反馈信息
            if(StringUtils.isBlank(feedBackVO.getInformantUser())){
                feedBackVO.setInformantUser(userInfo.getName());
            }
            if(feedBackVO.getInformantUserId() == null || feedBackVO.getInformantUserId() == 0L){
                feedBackVO.setInformantUserId(getUserId(json));
            }
            feedBackService.addFeedBackInfo(feedBackVO,String.valueOf(getUserId(json)));
            return super.getResultJSONStr(true,null, null);
        }catch (Exception e){
            logger.error("[feedBack][saveFeedBackInfo] exception: {}",e);
            return super.getResultJSONStr(false,null, Constants.SYS_ERROR);
        }

    }

    /**
     * 分页查询用户反馈信息
     */
    @RequestMapping(value = "/queryFeedBackPage",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String queryFeedBackPage(@RequestBody String json){
        try{
            FeedBackDTO dto = getFormJSON(json,FeedBackDTO.class);
            PageRequest pageRequest = genPageRequest(dto);
            PageDTO<FeedBackVO> pageDTO = feedBackService.queryFeedBackPage(pageRequest,dto);
            return super.getResultJSONStr(true,pageDTO, null);
        }catch (Exception e){
            logger.error("[feedBack][queryFeedBackPage] exception: {}",e);
            return super.getResultJSONStr(false,null, Constants.SYS_ERROR);
        }

    }

    /**
     * 查询用户反馈信息
     */
    @RequestMapping(value = "/queryFeedBackInfo",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String queryFeedBackInfo(@RequestBody String json){
        try{
            FeedBackVO feedBackVO = getFormJSON(json, FeedBackVO.class);
            if(feedBackVO == null || feedBackVO.getId() == null || feedBackVO.getId() == 0L){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            feedBackVO = feedBackService.queryFeedBackInfo(feedBackVO);
            return super.getResultJSONStr(true,feedBackVO, null);
        }catch (Exception e){
            logger.error("[feedBack][queryFeedBackInfo] exception: {}",e);
            return super.getResultJSONStr(false,null, Constants.SYS_ERROR);
        }

    }

    /**
     * 删除用户反馈信息
     */
    @RequestMapping(value = "/deleteFeedBackInfo",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String deleteFeedBackInfo(@RequestBody String json){
        try{
            FeedBackVO feedBackVO = getFormJSON(json, FeedBackVO.class);
            if(feedBackVO == null || feedBackVO.getId() == null || feedBackVO.getId() == 0L){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            feedBackService.deleteFeedBackInfo(feedBackVO,String.valueOf(getUserId(json)));
            return super.getResultJSONStr(true,null, null);
        }catch (Exception e){
            logger.error("[feedBack][deleteFeedBackInfo] exception: {}",e);
            return super.getResultJSONStr(false,null, Constants.SYS_ERROR);
        }

    }

    /**
     * 批量删除用户反馈信息
     */
    @RequestMapping(value = "/deleteFeedBackList",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String deleteFeedBackList(@RequestBody String json){
        try{
            ViewVO viewVO = getFormJSON(json, ViewVO.class);
            if(viewVO == null || viewVO.getIds() == null || viewVO.getIds().size() <= 0){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            feedBackService.deleteFeedBackList(viewVO.getIds(),String.valueOf(getUserId(json)));
            return super.getResultJSONStr(true,null, null);
        }catch (Exception e){
            logger.error("[feedBack][deleteFeedBackList] exception: {}",e);
            return super.getResultJSONStr(false,null, Constants.SYS_ERROR);
        }

    }



}
