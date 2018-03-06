package com.apec.voucher.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.common.util.DateTimeUtils;
import com.apec.framework.log.InjectLogger;
import com.apec.voucher.dto.RankingDTO;
import com.apec.voucher.service.RankingService;
import com.apec.voucher.viewvo.WeekBest;
import com.apec.voucher.vo.ConditionVO;
import com.apec.voucher.vo.RankingVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hmy on 2017/12/19.
 * 排行榜
 * @author hmy
 */
@RestController
@RequestMapping(value = "/ranking")
public class RankingController extends MyBaseController {

    @InjectLogger
    private Logger log;

    @Autowired
    private RankingService rankingService;

    /**
     * 排行榜分页信息
     */
    @RequestMapping(value = "/rankingPage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String rankingPage(@RequestBody String json){
        try {
            RankingDTO rankingDTO = getFormJSON(json,RankingDTO.class);
            PageRequest pageRequest = genPageRequest(rankingDTO);
            return getResultJSONStr(true, rankingService.rankingPage(rankingDTO,pageRequest), null);

        } catch(BusinessException e){
            log.error("【ranking】[rankingPage]rankingPage :{}",e);
            return getResultJSONStr(false, null, Constants.SERVER_RESEST_EXCEPTION);
        } catch (Exception e) {
            log.error("【ranking】[rankingPage]rankingPage :{}",e);
            return getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 排行榜信息
     */
    @RequestMapping(value = "/rankingInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String rankingInfo(@RequestBody String json){
        try {
            RankingVO rankingVO = getFormJSON(json,RankingVO.class);
            return getResultJSONStr(true, rankingService.findRankingInfo(rankingVO), null);

        } catch(BusinessException e){
            log.error("【ranking】[rankingInfo]rankingInfo :{}",e);
            return getResultJSONStr(false, null, Constants.SERVER_RESEST_EXCEPTION);
        } catch (Exception e) {
            log.error("【ranking】[rankingInfo]rankingInfo :{}",e);
            return getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 增加排行榜信息
     */
    @RequestMapping(value = "/addRankingInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String addRankingInfo(@RequestBody String json){
        try {
            RankingVO rankingVO = getFormJSON(json,RankingVO.class);
            boolean flag = rankingVO ==null || rankingVO.getStartTime() == null || rankingVO.getEndTime() == null
                    || rankingVO.getRankingType() == null ;
            if(flag){
                return getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            if(DateTimeUtils.getDifferTime(rankingVO.getStartTime(),rankingVO.getEndTime(),DateTimeUtils.SECOND) >= 0){
                return super.getResultJSONStr(false,null, ErrorCodeConst.ERROR_EDIT_TIME);
            }
            if(!CollectionUtils.isEmpty(rankingVO.getConditions())){
                for(int i = 0 ; i < rankingVO.getConditions().size(); i++){
                    ConditionVO conditionVO = rankingVO.getConditions().get(i);
                    if(conditionVO.getCategories() == null || conditionVO.getConditionsType() == null){
                        return getResultJSONStr(false, null, Constants.ERROR_100003);
                    }
                }
            }
            if(!CollectionUtils.isEmpty(rankingVO.getRankingMans())){
                for(int i = 0 ; i < rankingVO.getRankingMans().size(); i++){
                    WeekBest weekBest = rankingVO.getRankingMans().get(i);
                    if(weekBest.getUserId() == null || weekBest.getUserId() == 0L){
                        return getResultJSONStr(false, null, Constants.ERROR_100003);
                    }
                }
            }
            String result = rankingService.addRankingInfo(rankingVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return getResultJSONStr(true, null, null);
            }else{
                return getResultJSONStr(false, null, result);
            }

        } catch(BusinessException e){
            log.error("【ranking】[addRankingInfo]addRankingInfo :{}",e);
            return getResultJSONStr(false, null, Constants.SERVER_RESEST_EXCEPTION);
        } catch (Exception e) {
            log.error("【ranking】[addRankingInfo]addRankingInfo :{}",e);
            return getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 修改排行榜信息
     */
    @RequestMapping(value = "/updateRankingInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String updateRankingInfo(@RequestBody String json){
        try {
            RankingVO rankingVO = getFormJSON(json,RankingVO.class);
            boolean flag = rankingVO ==null || rankingVO.getId() == null || rankingVO.getId() == 0L;
            if(flag){
                return getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            String result = rankingService.updateRankingInfo(rankingVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return getResultJSONStr(true, null, null);
            }else{
                return getResultJSONStr(false, null, result);
            }

        } catch(BusinessException e){
            log.error("【ranking】[updateRankingInfo]updateRankingInfo :{}",e);
            return getResultJSONStr(false, null, Constants.SERVER_RESEST_EXCEPTION);
        } catch (Exception e) {
            log.error("【ranking】[updateRankingInfo]updateRankingInfo :{}",e);
            return getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 下线排行榜信息
     */
    @RequestMapping(value = "/outlineRankingInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String outlineRankingInfo(@RequestBody String json){
        try {
            RankingVO rankingVO = getFormJSON(json,RankingVO.class);
            boolean flag = rankingVO ==null || rankingVO.getId() == null || rankingVO.getId() == 0L;
            if(flag){
                return getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            String result = rankingService.outlineRankingInfo(rankingVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return getResultJSONStr(true, null, null);
            }else{
                return getResultJSONStr(false, null, result);
            }

        } catch(BusinessException e){
            log.error("【ranking】[outlineRankingInfo]outlineRankingInfo :{}",e);
            return getResultJSONStr(false, null, Constants.SERVER_RESEST_EXCEPTION);
        } catch (Exception e) {
            log.error("【ranking】[outlineRankingInfo]outlineRankingInfo :{}",e);
            return getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 查询符合条件的用户信息
     */
    @RequestMapping(value = "/queryUserInfoForRanking", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String queryUserInfoForRanking(@RequestBody String json){
        try {
            RankingVO rankingVO = getFormJSON(json,RankingVO.class);
            boolean flag = rankingVO == null || rankingVO.getRankingType() == null;
            if(flag){
                return getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            RankingDTO rankingDTO = getFormJSON(json,RankingDTO.class);
            PageRequest pageRequest = genPageRequest(rankingDTO);
            return getResultJSONStr(true, rankingService.queryUserInfoForRanking(rankingVO,pageRequest), null);

        } catch(BusinessException e){
            log.error("【ranking】[queryUserInfoForRanking]queryUserInfoForRanking BusinessException :{}",e);
            return getResultJSONStr(false, null, Constants.SERVER_RESEST_EXCEPTION);
        } catch (Exception e) {
            log.error("【ranking】[queryUserInfoForRanking]queryUserInfoForRanking :{}",e);
            return getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 查询排行榜用户信息
     */
    @RequestMapping(value = "/queryRankingMan", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String queryRankingMan(@RequestBody String json){
        try {
            RankingVO rankingVO = getFormJSON(json,RankingVO.class);
            boolean flag = rankingVO == null || rankingVO.getRankingType() == null;
            if(flag){
                return getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            return getResultJSONStr(true, rankingService.queryRankingMan(rankingVO), null);

        } catch(BusinessException e){
            log.error("【ranking】[queryRankingMan]queryRankingMan BusinessException :{}",e);
            return getResultJSONStr(false, null, Constants.SERVER_RESEST_EXCEPTION);
        } catch (Exception e) {
            log.error("【ranking】[queryRankingMan]queryRankingMan :{}",e);
            return getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }


}
