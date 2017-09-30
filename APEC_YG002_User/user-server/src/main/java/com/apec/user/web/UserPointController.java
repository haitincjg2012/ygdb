package com.apec.user.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.util.JsonUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.user.dto.UserDTO;
import com.apec.user.dto.UserPointRuleDTO;
import com.apec.user.service.UserPointRecordService;
import com.apec.user.vo.UserPointRecordVO;
import com.apec.user.vo.UserPointRecordViewVO;
import com.apec.user.vo.UserPointRuleVO;
import com.apec.user.vo.UserPointVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by hmy on 2017/7/5.
 */
@RestController
@RequestMapping(value="/userpoint")
public class UserPointController extends MyBaseController {

    @InjectLogger
    private Logger log;

    @Autowired
    private UserPointRecordService userPointRecordService;

    /**
     * 给新注册的用户初始化积分及记录
     * @param json
     * @return
     */
    @RequestMapping(value="/addNewPoint",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String addNewUserPoint(@RequestBody String json){
        String result = Constants.SYS_ERROR;
        try{
            //获取数据
            UserPointRecordVO userPointRecordVO = getFormJSON(json,UserPointRecordVO.class);
            if(userPointRecordVO == null || userPointRecordVO.getUserIds() == null || userPointRecordVO.getUserIds().size() <= 0){
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }
            result = userPointRecordService.addUserPoint(userPointRecordVO);

        }catch(Exception e){
            log.error("[userpoint][addNewPoint] exception:{}" , e);
        }
        if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
            return super.getResultJSONStr(true, null, "");
        }else{
            return super.getResultJSONStr(false, null, result);
        }

    }

    /**
     * 给用户加减积分,传入积分规则id不为空时，按积分规则表进行积分变换，为空时则必须保证积分规则描述和改变的积分不为空，按传入的积分改变时来修改用户积分
     * @param json
     * @return
     */
    @RequestMapping(value="/reducePoint",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String reducePoint(@RequestBody String json){
        String result = Constants.SYS_ERROR;
        try{
            //获取数据
            UserPointRecordVO userPointRecordVO = getFormJSON(json,UserPointRecordVO.class);
            if(userPointRecordVO == null || userPointRecordVO.getPointRuleType() == null){
                //积分来源不能为空
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }
            if(userPointRecordVO.getUserIds() == null || userPointRecordVO.getUserIds().size() <= 0){
                //来自前端直接触发的积分记录
                Long userId = getUserId(json);
                if(userId == null || userId == 0L){
                    return super.getResultJSONStr(false,null,Constants.ERROR_100003);
                }
                List<Long> userIds = new ArrayList<>();
                userIds.add(userId);
                userPointRecordVO.setUserIds(userIds);
            }
            result = userPointRecordService.addUserPoint(userPointRecordVO);

        }catch(Exception e){
            log.error("[userpoint][reducePoint] exception:{}" , e);
        }
        if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
            return super.getResultJSONStr(true, null, "");
        }else{
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 修改积分规则表中规则分数
     */
    @RequestMapping(value = "/updateRulePoints",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String updateRulePoints(@RequestBody String json){
        String result = Constants.SYS_ERROR;
        try{
            //获取数据
            UserPointRuleVO ruleVO = getFormJSON(json,UserPointRuleVO.class);
            if(ruleVO == null || ruleVO.getId() == null || ruleVO.getId() == 0L || ruleVO.getPointsChanged() == null){
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }
            result = userPointRecordService.updateUserPointRule(ruleVO,String.valueOf(getUserId(json)));
        }catch(Exception e){
            log.error("[userpoint][updateRulePoints] exception:{}" , e);
        }
        if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
            return super.getResultJSONStr(true, null, "");
        }else{
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 分页查询积分规则
     */
    @RequestMapping(value = "/pageUserPointRule",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String pageUserPointRule(@RequestBody String json){
        try{
            UserPointRuleDTO dto = getFormJSON(json,UserPointRuleDTO.class);
            PageRequest pageRequest = genPageRequest(dto);
            PageDTO<UserPointRuleVO> page = userPointRecordService.pageUserPointRule(pageRequest);
            return super.getResultJSONStr(true, page, "");

        }catch(Exception e){
            log.error("[userpoint][pageUserPointRule] exception {}:" ,e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 分页查询用户积分信息
     */
    @RequestMapping(value = "/pageUserPoints",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String getUserPointsPage(@RequestBody String json){
        try{
            UserDTO dto = getFormJSON(json,UserDTO.class);
            List<Sort.Order> orders = new ArrayList<>();
            orders.add(new Sort.Order(Sort.Direction.DESC, "availablePoints"));
            orders.add(new Sort.Order(Sort.Direction.DESC, "createDate"));
            int pageNumber = 1;
            int pageSize = 10;
            if (dto.getPageNumber() > 0) {
                pageNumber = dto.getPageNumber();
            }
            if (dto.getPageSize() > 0 && dto.getPageSize() < 1000) {
                pageSize = dto.getPageSize();
            }
            PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, new Sort(orders));
            PageDTO<UserPointVO> page = userPointRecordService.pageUserPoints(dto, pageRequest);
            return super.getResultJSONStr(true, page, "");
        }catch(Exception e){
            log.error("[userpoint][pageUserPoints] exception {}:" ,e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 分页查询用户积分记录信息
     */
    @RequestMapping(value = "/pageUserPointRecords",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String pageUserPointRecords(@RequestBody String json){
        try{
            UserDTO dto = getFormJSON(json,UserDTO.class);

            if(dto == null){
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }
            if( dto.getId() == null || dto.getId() == 0L){
                //如果为前台传来的信息，form中没有用户id，将RequestAttrMap中的用户id赋予用户对象
                Long userId = getUserId(json);
                dto.setId(userId);
            }
            PageRequest pageRequest = genPageRequest(dto);
            PageDTO<UserPointRecordViewVO> page = userPointRecordService.pageUserPointRecords(dto, pageRequest);
            return super.getResultJSONStr(true, page, "");
        }catch(Exception e){
            log.error("[userpoint][pageUserPointRecords] exception {}:" ,e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 查询用户积分是否准确落地，补偿和更新缓存(定时任务)
     */
    @RequestMapping(value = "/perfectUserPoint")
    public String perfectUserPoint(){
        try{

            String result = userPointRecordService.perfectUserPoint();
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true, null, null);
            }else{
                return super.getResultJSONStr(false, null, result);
            }

        }catch(Exception e){
            log.error("[userpoint][perfectUserPoint] exception {}:" ,e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

}
