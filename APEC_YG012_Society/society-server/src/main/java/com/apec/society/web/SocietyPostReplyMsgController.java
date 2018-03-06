package com.apec.society.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.log.InjectLogger;
import com.apec.society.dto.SocietyPostReplyMsgDTO;
import com.apec.society.service.SocietyPostReplyMsgService;
import com.apec.society.view.SocietyPostReplyMsgViewVO;
import com.apec.society.vo.SocietyPostReplyMsgVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hmy on 2017/11/1.
 *
 * @author hmy
 */
@RestController
@RequestMapping(value = "/societyPostReplyMsg")
public class SocietyPostReplyMsgController extends MyBaseController {

    @Autowired
    private SocietyPostReplyMsgService societyPostReplyMsgService;

    @InjectLogger
    private Logger log;

    /**
     * 添加回复通知消息
     */
    @RequestMapping(value = "/addSocietyPostReplyMsg", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String addSocietyPostReplyMsg(@RequestBody String json){
        try {
            SocietyPostReplyMsgVO societyPostReplyMsgVO = getFormJSON(json, SocietyPostReplyMsgVO.class);
            boolean flag = societyPostReplyMsgVO == null || StringUtils.isBlank(societyPostReplyMsgVO.getMessage())
                    || societyPostReplyMsgVO.getReceiver() == null || societyPostReplyMsgVO.getReceiver() == 0L
                    || societyPostReplyMsgVO.getSender() == null || societyPostReplyMsgVO.getSender() == 0L
                    || societyPostReplyMsgVO.getRelativeId() == null || societyPostReplyMsgVO.getRelativeId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            String returnCode = societyPostReplyMsgService.addSocietyPostReplyMsg(societyPostReplyMsgVO);
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }
        } catch (BusinessException e) {
            log.error("[societyPostReplyMsg.addSocietyPostReplyMsg] Add societyPostReplyMsg  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[societyPostReplyMsg.addSocietyPostReplyMsg] Add societyPostReplyMsg Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 删除重复的回复通知消息
     */
    @RequestMapping(value = "/deleteSocietyPostReplyMsg", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String deleteSocietyPostReplyMsg(@RequestBody String json){
        try {
            SocietyPostReplyMsgVO societyPostReplyMsgVO = getFormJSON(json, SocietyPostReplyMsgVO.class);
            boolean flag = societyPostReplyMsgVO == null;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            String returnCode = societyPostReplyMsgService.deleteSocietyPostReplyMsg(societyPostReplyMsgVO);
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }
        } catch (BusinessException e) {
            log.error("[societyPostReplyMsg.deleteSocietyPostReplyMsg] delete societyPostReplyMsg  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[societyPostReplyMsg.deleteSocietyPostReplyMsg] delete societyPostReplyMsg Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 回复通知消息分页查询
     */
    @RequestMapping(value = "/societyPostReplyMsgPage", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String societyPostReplyMsgPage(@RequestBody String json){
        try {
            SocietyPostReplyMsgDTO societyPostReplyMsgDTO = getFormJSON(json, SocietyPostReplyMsgDTO.class);
            PageRequest pageRequest = genPageRequest(societyPostReplyMsgDTO);
            societyPostReplyMsgDTO.setReceiver(getUserId(json));
            PageDTO<SocietyPostReplyMsgViewVO> result = societyPostReplyMsgService.societyPostReplyMsgPage(societyPostReplyMsgDTO,pageRequest);
            return super.getResultJSONStr(true,result,null);
        } catch (BusinessException e) {
            log.error("[societyPostReplyMsg.societyPostReplyMsgPage] Add societyPostReplyMsg  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[societyPostReplyMsg.societyPostReplyMsgPage] Add societyPostReplyMsg Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 消息设置为已读
     */
    @RequestMapping(value = "/setReadedMsg", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String setReadedMsg(@RequestBody String json){
        try {
            SocietyPostReplyMsgVO societyPostReplyMsgVO = getFormJSON(json, SocietyPostReplyMsgVO.class);
            boolean flag = societyPostReplyMsgVO == null || societyPostReplyMsgVO.getId() == null || societyPostReplyMsgVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            String returnCode = societyPostReplyMsgService.setReadedMsg(societyPostReplyMsgVO);
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }
        } catch (BusinessException e) {
            log.error("[societyPostReplyMsg.setReadedMsg] setReadedMsg  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[societyPostReplyMsg.setReadedMsg] setReadedMsg Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }


}
