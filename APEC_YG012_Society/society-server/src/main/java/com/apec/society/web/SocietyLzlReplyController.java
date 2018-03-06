package com.apec.society.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.log.InjectLogger;
import com.apec.society.service.SocietyLzlReplyService;
import com.apec.society.vo.SocietyLzlReplyVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hmy on 2017/10/30.
 *
 * @author hmy
 */
@RestController
@RequestMapping(value = "/societyLzlReply")
public class SocietyLzlReplyController extends MyBaseController {

    @InjectLogger
    private Logger log;

    @Autowired
    private SocietyLzlReplyService societyLzlReplyService;

    /**
     * 添加帖子回复
     */
    @RequestMapping(value = "/addSocietyLzlReply", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String addSocietyLzlReply(@RequestBody String json){
        try {
            SocietyLzlReplyVO societyLzlReplyVO = getFormJSON(json, SocietyLzlReplyVO.class);
            boolean flag = societyLzlReplyVO == null || StringUtils.isBlank(societyLzlReplyVO.getContent()) || societyLzlReplyVO.getToReplyId() == null || societyLzlReplyVO.getToReplyId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            societyLzlReplyVO.setUserId(getUserId(json));
            String returnCode = societyLzlReplyService.addSocietyLzlReply(societyLzlReplyVO);
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,societyLzlReplyVO,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }

        } catch (BusinessException e) {
            log.error("[societyLzlReply.addSocietyLzlReply] Add societyLzlReply  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[societyLzlReply.addSocietyLzlReply] Add societyLzlReply Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 删除帖子回复
     */
    @RequestMapping(value = "/deleteSocietyLzlReply", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String deleteSocietyLzlReply(@RequestBody String json){
        try {
            SocietyLzlReplyVO societyLzlReplyVO = getFormJSON(json, SocietyLzlReplyVO.class);
            boolean flag = societyLzlReplyVO == null ||  societyLzlReplyVO.getId() == null || societyLzlReplyVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            String returnCode = societyLzlReplyService.deleteSocietyLzlReply(societyLzlReplyVO);
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,societyLzlReplyVO,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }

        } catch (BusinessException e) {
            log.error("[societyLzlReply.deleteSocietyLzlReply] deleteSocietyLzlReply BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[societyLzlReply.deleteSocietyLzlReply] deleteSocietyLzlReply Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

}
