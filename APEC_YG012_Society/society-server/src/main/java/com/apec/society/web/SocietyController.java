package com.apec.society.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.PageJSON;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.society.service.SocietyService;
import com.apec.society.vo.SocietyInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 评论模块接口
 * @author yirder
 */
@RestController
@RequestMapping("/society")
public class SocietyController extends MyBaseController {

    @InjectLogger
    private Logger log;

    /**
     * 圈子服务
     */
    @Autowired
    private SocietyService societyService;

    /**
     * 添加圈子信息
     * @return ResultData
     */
    @RequestMapping(value = "/addSocietyInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public  ResultData<String>  addSocietyInfo(@RequestBody String jsonStr) {
        ResultData<String> resultData = new ResultData<>();
        try {
            PageJSON<String> pageJSON = super.getPageJSON(jsonStr, String.class);
            SocietyInfoVO societyInfoVO = BaseJsonUtil.parseObject(pageJSON.getFormJSON(), SocietyInfoVO.class);
            String returnCode = societyService.addSocietyInfo(societyInfoVO);
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                resultData.setSucceed(true);
            } else {
                setErrorResultDate(resultData, returnCode);
            }

        } catch (BusinessException e) {
            log.error("[SocietyController.addSocietyInfo] Add SocietyInfo  BusinessException", e);
            setErrorResultDate(resultData, e.getErrorCode());
        }catch (Exception e) {
            log.error("[SocietyController.addSocietyInfo] Add  SocietyInfo Exception", e);
            setErrorResultDate(resultData,Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     * 查看所有的圈子信息
     */
    @RequestMapping(value = "/getAllSocietyInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String getAllSocietyInfo(@RequestBody String jsonStr) {
        try {
            List<SocietyInfoVO> result = societyService.getAllSocietyInfo();
            return super.getResultJSONStr(true,result,null);
        } catch (BusinessException e) {
            log.error("[SocietyController.addSocietyInfo] Add SocietyInfo  BusinessException", e);
            return super.getResultJSONStr(false,null,e.getErrorCode());
        }catch (Exception e) {
            log.error("[SocietyController.addSocietyInfo] Add  SocietyInfo Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }


}
