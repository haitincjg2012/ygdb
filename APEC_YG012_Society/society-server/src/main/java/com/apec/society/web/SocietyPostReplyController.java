package com.apec.society.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.log.InjectLogger;
import com.apec.society.dto.SocietyPostReplyDTO;
import com.apec.society.service.SocietyPostReplyService;
import com.apec.society.view.SocietyPostReplyViewVO;
import com.apec.society.vo.SocietyPostReplyVO;
import com.apec.society.vo.SocietyPostVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
@RequestMapping(value = "/societyPostReply")
public class SocietyPostReplyController extends MyBaseController {

    @Autowired
    private SocietyPostReplyService societyPostReplyService;

    @InjectLogger
    private Logger log;

    /**
     * 添加帖子回复
     */
    @RequestMapping(value = "/addSocietyPostReply", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String addSocietyPostReply(@RequestBody String json){
        try {
            SocietyPostReplyVO societyPostReplyVO = getFormJSON(json, SocietyPostReplyVO.class);
            boolean flag = societyPostReplyVO == null || StringUtils.isBlank(societyPostReplyVO.getContent()) || societyPostReplyVO.getSocietyPostId() == null || societyPostReplyVO.getSocietyPostId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            societyPostReplyVO.setUserId(getUserId(json));
            String returnCode = societyPostReplyService.addSocietyPostReply(societyPostReplyVO);
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,societyPostReplyVO,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }

        } catch (BusinessException e) {
            log.error("[societyPostReply.addSocietyPostReply] Add societyPostReply  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[societyPostReply.addSocietyPostReply] Add societyPostReply Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 删除帖子评论
     */
    @RequestMapping(value = "/deleteSocietyPostReply", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String deleteSocietyPostReply(@RequestBody String json){
        try {
            SocietyPostReplyVO societyPostReplyVO = getFormJSON(json, SocietyPostReplyVO.class);
            boolean flag = societyPostReplyVO == null || societyPostReplyVO.getId() == null || societyPostReplyVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            String returnCode = societyPostReplyService.deleteSocietyPostReply(societyPostReplyVO);
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,societyPostReplyVO,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }

        } catch (BusinessException e) {
            log.error("[societyPostReply.deleteSocietyPostReply] deleteSocietyPostReply societyPostReply  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[societyPostReply.deleteSocietyPostReply] deleteSocietyPostReply societyPostReply Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 查询帖子回复量最大的楼层评论
     */
    @RequestMapping(value = "/findMaxReplyNum", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String findMaxReplyNum(@RequestBody String json){
        try {
            SocietyPostVO societyPostVO = getFormJSON(json, SocietyPostVO.class);
            boolean flag = societyPostVO == null || societyPostVO.getId() == null || societyPostVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            SocietyPostReplyViewVO societyPostReplyViewVO = societyPostReplyService.findMaxReplyNum(societyPostVO,String.valueOf(getUserIdFormToken(json)));
            return super.getResultJSONStr(true,societyPostReplyViewVO,null);

        } catch (BusinessException e) {
            log.error("[societyPostReply.findMaxReplyNum]findMaxReplyNum BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[societyPostReply.findMaxReplyNum]findMaxReplyNum Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 查询楼层评论根据评论id
     */
    @RequestMapping(value = "/queryReplyById", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String queryReplyById(@RequestBody String json){
        try {
            SocietyPostReplyVO societyPostReplyVO = getFormJSON(json, SocietyPostReplyVO.class);
            boolean flag = societyPostReplyVO == null || societyPostReplyVO.getId() == null || societyPostReplyVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            SocietyPostReplyViewVO societyPostReplyViewVO = societyPostReplyService.queryReplyById(societyPostReplyVO,String.valueOf(getUserIdFormToken(json)));
            return super.getResultJSONStr(true,societyPostReplyViewVO,null);

        } catch (BusinessException e) {
            log.error("[societyPostReply.queryReplyById]queryReplyById BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[societyPostReply.queryReplyById]queryReplyById Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 分页查询评论信息
     */
    @RequestMapping(value = "/findSocietyPostReplyPage", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String findSocietyPostReplyPage(@RequestBody String json){
        try {
            SocietyPostReplyDTO societyPostReplyDTO = getFormJSON(json, SocietyPostReplyDTO.class);
            boolean flag = societyPostReplyDTO == null || societyPostReplyDTO.getSocietyPostId() == null || societyPostReplyDTO.getSocietyPostId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            PageRequest pageRequest = genPageRequest(societyPostReplyDTO);
            PageDTO<SocietyPostReplyViewVO> result = societyPostReplyService.findSocietyPostReplyPage(societyPostReplyDTO,pageRequest,String.valueOf(getUserIdFormToken(json)));
            return super.getResultJSONStr(true,result,null);

        } catch (BusinessException e) {
            log.error("[societyPostReply.findSocietyPostReplyPage] findSocietyPostReplyPage  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[societyPostReply.findSocietyPostReplyPage] findSocietyPostReplyPage Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 点赞/取消点赞
     */
    @RequestMapping(value = "/likeSocietyPostOrNot", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String likeSocietyPostOrNot(@RequestBody String json){
        try {
            SocietyPostReplyVO societyPostReplyVO = getFormJSON(json, SocietyPostReplyVO.class);
            boolean flag = societyPostReplyVO == null || societyPostReplyVO.getId() == null || societyPostReplyVO.getId() == 0L || societyPostReplyVO.getLikePostReply() == null ;
            if(flag){
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }
            String returnCode = societyPostReplyService.likeSocietyPostOrNot(societyPostReplyVO, String.valueOf(getUserId(json)));
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }

        } catch (BusinessException e) {
            log.error("[societyPost.likeSocietyPostOrNot] likeSocietyPostOrNot  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[societyPost.likeSocietyPostOrNot] likeSocietyPostOrNot Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }



}
