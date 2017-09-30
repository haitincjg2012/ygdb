package com.apec.systemconfig.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.ResultData;
import com.apec.framework.log.InjectLogger;
import com.apec.systemconfig.dto.EsConfigDTO;
import com.apec.systemconfig.service.EsConfigService;
import com.apec.systemconfig.vo.EsConfigVO;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wubi on 2017/9/22.
 */
@RestController
@RequestMapping(value = "/esConfig")
public class EsConfigController extends MyBaseController {

    @InjectLogger
    private Logger logger;

    @Autowired
    private EsConfigService esConfigService;

    /**
     * 新增配置信息
     *
     * @param json
     * @return
     */
    @RequestMapping(value = "/addEsConfigInfo", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String addEsConfigInfo(@RequestBody String json) {
        try {
            EsConfigVO esConfigVO = getFormJSON(json, EsConfigVO.class);
            esConfigService.addConfig(esConfigVO, String.valueOf(getUserId(json)));
            return super.getResultJSONStr(true, null, null);
        } catch (Exception e) {
            logger.error("[esConfig][addEsConfigInfo] exception: {}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }

    }

    /**
     * 更新配置信息 根据ID更新 mapping关系
     *
     * @param json
     * @return
     */
    @RequestMapping(value = "/updateEsConfigInfo", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String updateEsConfigInfo(@RequestBody String json) {
        try {
            EsConfigVO esConfigVO = getFormJSON(json, EsConfigVO.class);
            esConfigService.updateConfigForReIndex(esConfigVO, String.valueOf(getUserId(json)));
            return super.getResultJSONStr(true, null, null);
        } catch (Exception e) {
            logger.error("[esConfig][updateEsConfigInfo] exception: {}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }

    }

    /**
     * 删除配置 软删除
     *
     * @param json
     * @return
     */
    @RequestMapping(value = "/deleteEsConfigInfo", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String deleteEsConfigInfo(@RequestBody String json) {
        try {
            EsConfigVO esConfigVO = getFormJSON(json, EsConfigVO.class);
            esConfigService.deleteConfig(esConfigVO, String.valueOf(getUserId(json)));
            return super.getResultJSONStr(true, null, null);
        } catch (Exception e) {
            logger.error("[esConfig][deleteEsConfigInfo] exception: {}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }

    }

    /**
     * 分页查询
     *
     * @param json
     * @return
     */
    @RequestMapping(value = "/queryByPage", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ResultData<PageDTO<EsConfigVO>> queryByPage(@RequestBody String json) {
        try {
            EsConfigDTO esConfigDTO = getFormJSON(json, EsConfigDTO.class);
            PageRequest pageRequest = super.genPageRequest(esConfigDTO);
            PageDTO<EsConfigVO> result = esConfigService.queryConfigByPage(pageRequest, esConfigDTO);
            return getResultData(true, result, "", null);
        } catch (Exception e) {
            logger.error("[EsConfigController] [queryByPage] Exception", e);
            return getResultData(false, null, Constants.SYS_ERROR);
        }

    }

    /**
     * 重建索引job
     *
     * @param json
     * @return
     */
    @RequestMapping(value = "/reIndexJob", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ResultData<String> reIndexJob(@RequestBody(required = false) String json) {
        ResultData<String> resultData = new ResultData<>();
        setErrorResultDate(resultData, Constants.RETURN_SUCESS);
        try {
            esConfigService.reIndexJob();
        } catch (Throwable e) {
            logger.error("[EsConfigController] [reIndexJob] run reIndexJob Exception", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;

    }
}
