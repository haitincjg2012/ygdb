package com.apec.game.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.common.util.DateTimeUtils;
import com.apec.framework.log.InjectLogger;
import com.apec.game.dto.LotteryDTO;
import com.apec.game.dto.PrizeAwardDTO;
import com.apec.game.dto.PrizePeopleDTO;
import com.apec.game.service.LotteryService;
import com.apec.game.vo.ActiveRuleVO;
import com.apec.game.vo.LotteryHintVO;
import com.apec.game.vo.LotteryVO;
import com.apec.game.vo.PrizeAwardVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by hmy on 2017/12/21.
 *
 * @author hmy
 */
@RestController
@RequestMapping(value = "/lottery")
public class LotteryController extends MyBaseController {

    @InjectLogger
    private Logger log;

    @Autowired
    private LotteryService lotteryService;

    /**
     * 添加抽奖活动
     */
    @RequestMapping(value = "/addLottery", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String addLottery(@RequestBody String json){
        try {
            LotteryVO lotteryVO = getFormJSON(json, LotteryVO.class);
            boolean flag = lotteryVO == null || StringUtils.isBlank(lotteryVO.getLotteryName()) || lotteryVO.getLotteryFormType() == null
                    || lotteryVO.getStartTime() == null || lotteryVO.getEndTime() == null;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            if(DateTimeUtils.getDifferTime(lotteryVO.getStartTime(),lotteryVO.getEndTime(),DateTimeUtils.SECOND) >= 0){
                return super.getResultJSONStr(false,null, ErrorCodeConst.ERROR_EDIT_TIME);
            }
            if(!CollectionUtils.isEmpty(lotteryVO.getLotteryHintVOS())){
                for(LotteryHintVO lotteryHintVO:lotteryVO.getLotteryHintVOS()){
                    if(lotteryHintVO.getParticipationState() == null || StringUtils.isBlank(lotteryHintVO.getRemark())){
                        //如果传入的提示条件没有提示内容和中奖类型则返回错误
                        return super.getResultJSONStr(false,null, Constants.ERROR_100003);
                    }
                }
            }
            String returnCode = lotteryService.addLottery(lotteryVO,String.valueOf(getUserId(json)));
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,lotteryVO,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }

        } catch (BusinessException e) {
            log.error("[lottery.addLottery] Add addLottery  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[lottery.addLottery] Add addLottery Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 修改抽奖活动
     */
    @RequestMapping(value = "/updateLottery", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String updateLottery(@RequestBody String json){
        try {
            LotteryVO lotteryVO = getFormJSON(json, LotteryVO.class);
            boolean flag = lotteryVO == null || lotteryVO.getId() == null || lotteryVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            String returnCode = lotteryService.updateLottery(lotteryVO,String.valueOf(getUserId(json)));
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }

        } catch (BusinessException e) {
            log.error("[lottery.updateLottery] updateLottery  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[lottery.updateLottery] updateLottery Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 下线抽奖活动
     */
    @RequestMapping(value = "/outlineLottery", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String outlineLottery(@RequestBody String json){
        try {
            LotteryVO lotteryVO = getFormJSON(json, LotteryVO.class);
            boolean flag = lotteryVO == null || lotteryVO.getId() == null || lotteryVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            lotteryVO.setEndTime(new Date());
            String returnCode = lotteryService.updateLottery(lotteryVO,String.valueOf(getUserId(json)));
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }

        } catch (BusinessException e) {
            log.error("[lottery.outlineLottery] outlineLottery  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[lottery.outlineLottery] outlineLottery Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 删除抽奖活动
     */
    @RequestMapping(value = "/deleteLottery", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String deleteLottery(@RequestBody String json){
        try {
            LotteryVO lotteryVO = getFormJSON(json, LotteryVO.class);
            boolean flag = lotteryVO == null || lotteryVO.getId() == null || lotteryVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            String returnCode = lotteryService.deleteLottery(lotteryVO,String.valueOf(getUserId(json)));
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }

        } catch (BusinessException e) {
            log.error("[lottery.deleteLottery] deleteLottery  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[lottery.deleteLottery] deleteLottery Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 修改抽奖活动提示
     */
    @RequestMapping(value = "/updateLotteryHoint", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String updateLotteryHoint(@RequestBody String json){
        try {
            LotteryHintVO lotteryHintVO = getFormJSON(json, LotteryHintVO.class);
            boolean flag = lotteryHintVO == null || lotteryHintVO.getId() == null || lotteryHintVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            String returnCode = lotteryService.updateLotteryHoint(lotteryHintVO,String.valueOf(getUserId(json)));
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }

        } catch (BusinessException e) {
            log.error("[lottery.updateLotteryHoint] updateLotteryHoint  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[lottery.updateLotteryHoint] updateLotteryHoint Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 修改抽奖活动提示
     */
    @RequestMapping(value = "/deleteLotteryHoint", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String deleteLotteryHoint(@RequestBody String json){
        try {
            LotteryHintVO lotteryHintVO = getFormJSON(json, LotteryHintVO.class);
            boolean flag = lotteryHintVO == null || lotteryHintVO.getId() == null || lotteryHintVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            String returnCode = lotteryService.deleteLotteryHoint(lotteryHintVO,String.valueOf(getUserId(json)));
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }

        } catch (BusinessException e) {
            log.error("[lottery.updateLotteryHoint] updateLotteryHoint  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[lottery.updateLotteryHoint] updateLotteryHoint Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 分页查询抽奖活动
     */
    @RequestMapping(value = "/lotteryPage", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String lotteryPage(@RequestBody String json){
        try {
            LotteryDTO lotteryDTO = getFormJSON(json, LotteryDTO.class);
            PageRequest pageRequest = genPageRequest(lotteryDTO);
            return super.getResultJSONStr(true,lotteryService.queryLotteryPage(lotteryDTO,pageRequest),null);

        } catch (BusinessException e) {
            log.error("[lottery.lotteryPage] lotteryPage BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[lottery.lotteryPage] lotteryPage Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 查询抽奖活动
     */
    @RequestMapping(value = "/lotteryInfo", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String lotteryInfo(@RequestBody String json){
        try {
            LotteryVO lotteryVO = getFormJSON(json, LotteryVO.class);
            boolean flag = lotteryVO == null || lotteryVO.getId() == null || lotteryVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            return super.getResultJSONStr(true,lotteryService.queryLotteryInfo(lotteryVO),null);

        } catch (BusinessException e) {
            log.error("[lottery.lotteryInfo] lotteryInfo  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[lottery.lotteryInfo] lotteryInfo Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 查询所有的抽奖形式
     */
    @RequestMapping(value = "/lotteryFormList", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String lotteryFormList(@RequestBody String json){
        try {
            return super.getResultJSONStr(true,lotteryService.queryLotteryFormList(),null);

        } catch (BusinessException e) {
            log.error("[lottery.lotteryFormList] lotteryFormList  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[lottery.lotteryFormList] lotteryFormList Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 查询所有的抽奖条件
     */
    @RequestMapping(value = "/participationConditionList", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String participationConditionList(@RequestBody String json){
        try {
            return super.getResultJSONStr(true,lotteryService.queryParticipationConditionList(),null);

        } catch (BusinessException e) {
            log.error("[lottery.participationConditionList] participationConditionList  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[lottery.participationConditionList] participationConditionList Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 添加抽奖规则
     */
    @RequestMapping(value = "/addActiveRule", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String addActiveRule(@RequestBody String json){
        try {
            ActiveRuleVO activeRuleVO = getFormJSON(json, ActiveRuleVO.class);
            boolean flag = activeRuleVO == null || activeRuleVO.getConditionId() == null || activeRuleVO.getConditionId() == 0L
                    || activeRuleVO.getLotteryId() == null || activeRuleVO.getLotteryId() == 0L || activeRuleVO.getParticipationNumType() == null;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            String returnCode = lotteryService.addActiveRule(activeRuleVO,String.valueOf(getUserId(json)));
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }

        } catch (BusinessException e) {
            log.error("[lottery.addActiveRule] addActiveRule  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[lottery.addActiveRule] addActiveRule Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 修改抽奖规则
     */
    @RequestMapping(value = "/updateActiveRule", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String updateActiveRule(@RequestBody String json){
        try {
            ActiveRuleVO activeRuleVO = getFormJSON(json, ActiveRuleVO.class);
            boolean flag = activeRuleVO == null || activeRuleVO.getId() == null || activeRuleVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            String returnCode = lotteryService.updateActiveRule(activeRuleVO,String.valueOf(getUserId(json)));
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }

        } catch (BusinessException e) {
            log.error("[lottery.updateActiveRule] updateActiveRule  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[lottery.updateActiveRule] updateActiveRule Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 查询抽奖活动的活动规则
     */
    @RequestMapping(value = "/activeRuleInfoOfLottery", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String activeRuleInfoOfLottery(@RequestBody String json){
        try {
            ActiveRuleVO activeRuleVO = getFormJSON(json, ActiveRuleVO.class);
            boolean flag = activeRuleVO == null || activeRuleVO.getLotteryId() == null || activeRuleVO.getLotteryId() == 0L ;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            return super.getResultJSONStr(true,lotteryService.queryActiveRuleInfoOfLottery(activeRuleVO),null);

        } catch (BusinessException e) {
            log.error("[lottery.activeRuleInfoOfLottery] activeRuleInfoOfLottery  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[lottery.activeRuleInfoOfLottery] activeRuleInfoOfLottery Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 设置活动奖品
     */
    @RequestMapping(value = "/addLotteryPrize", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String addLotteryPrize(@RequestBody String json){
        try {
            PrizeAwardVO prizeAwardVO = getFormJSON(json, PrizeAwardVO.class);
            boolean flag = prizeAwardVO == null || prizeAwardVO.getLimitedAward() == null || prizeAwardVO.getLimitedAward() == 0 || prizeAwardVO.getPrizeNobel() == null
                    || prizeAwardVO.getLotteryId() == null || prizeAwardVO.getLotteryId() == 0L || prizeAwardVO.getNum() == null || prizeAwardVO.getNum() == 0
                    || prizeAwardVO.getTotalNum() == null || prizeAwardVO.getTotalNum() == 0 || prizeAwardVO.getWinningRate() == null || prizeAwardVO.getWinningRate() == 0
                    || prizeAwardVO.getLimitedAward() == null || prizeAwardVO.getLimitedAward() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            String returnCode = lotteryService.addLotteryPrize(prizeAwardVO,String.valueOf(getUserId(json)));
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }

        } catch (BusinessException e) {
            log.error("[lottery.addLotteryPrize] addLotteryPrize  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[lottery.addLotteryPrize] addLotteryPrize Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 设置活动奖品
     */
    @RequestMapping(value = "/updateLotteryPrize", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String updateLotteryPrize(@RequestBody String json){
        try {
            PrizeAwardVO prizeAwardVO = getFormJSON(json, PrizeAwardVO.class);
            boolean flag = prizeAwardVO == null || prizeAwardVO.getId() == null || prizeAwardVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            String returnCode = lotteryService.updateLotteryPrize(prizeAwardVO,String.valueOf(getUserId(json)));
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }

        } catch (BusinessException e) {
            log.error("[lottery.updateLotteryPrize] updateLotteryPrize  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[lottery.updateLotteryPrize] updateLotteryPrize Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 删除活动奖品
     */
    @RequestMapping(value = "/deleteLotteryPrize", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String deleteLotteryPrize(@RequestBody String json){
        try {
            PrizeAwardVO prizeAwardVO = getFormJSON(json, PrizeAwardVO.class);
            boolean flag = prizeAwardVO == null || prizeAwardVO.getId() == null || prizeAwardVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            String returnCode = lotteryService.deleteLotteryPrize(prizeAwardVO,String.valueOf(getUserId(json)));
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }

        } catch (BusinessException e) {
            log.error("[lottery.deleteLotteryPrize] deleteLotteryPrize  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[lottery.deleteLotteryPrize] deleteLotteryPrize Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 删除活动奖品
     */
    @RequestMapping(value = "/lotteryPrizeInfo", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String lotteryPrizeInfo(@RequestBody String json){
        try {
            PrizeAwardVO prizeAwardVO = getFormJSON(json, PrizeAwardVO.class);
            boolean flag = prizeAwardVO == null || prizeAwardVO.getId() == null || prizeAwardVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            return super.getResultJSONStr(true,lotteryService.queryLotteryPrizeInfo(prizeAwardVO),null);

        } catch (BusinessException e) {
            log.error("[lottery.lotteryPrizeInfo] lotteryPrizeInfo  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[lottery.lotteryPrizeInfo] lotteryPrizeInfo Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 活动奖品分页查询
     */
    @RequestMapping(value = "/lotteryPrizePage", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String lotteryPrizePage(@RequestBody String json){
        try {
            PrizeAwardDTO prizeAwardDTO = getFormJSON(json, PrizeAwardDTO.class);
            PageRequest pageRequest = genPageRequest(prizeAwardDTO);
            return super.getResultJSONStr(true,lotteryService.queryLotteryPrizePage(prizeAwardDTO,pageRequest),null);

        } catch (BusinessException e) {
            log.error("[lottery.lotteryPrizePage] lotteryPrizePage  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[lottery.lotteryPrizePage] lotteryPrizePage Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 查询中奖用户信息
     */
    @RequestMapping(value = "/lotteryPeoplePage", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String lotteryPeoplePage(@RequestBody String json){
        try {
            PrizePeopleDTO prizePeopleDTO = getFormJSON(json, PrizePeopleDTO.class);
            PageRequest pageRequest = genPageRequest(prizePeopleDTO);
            return super.getResultJSONStr(true,lotteryService.queryLotteryPeoplePage(prizePeopleDTO,pageRequest),null);

        } catch (BusinessException e) {
            log.error("[lottery.lotteryPeoplePage] lotteryPeoplePage  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[lottery.lotteryPeoplePage] lotteryPeoplePage Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 统计抽奖用户人数
     */
    @RequestMapping(value = "/countLotteryPeople", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String countLotteryPeople(@RequestBody String json){
        try {
            LotteryVO lotteryVO = getFormJSON(json, LotteryVO.class);
            boolean flag = lotteryVO == null || lotteryVO.getId() == null || lotteryVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            return super.getResultJSONStr(true,lotteryService.countLotteryPeople(lotteryVO),null);

        } catch (BusinessException e) {
            log.error("[lottery.countLotteryPeople] countLotteryPeople  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[lottery.countLotteryPeople] countLotteryPeople Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

}
