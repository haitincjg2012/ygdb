package com.apec.goodssource.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.log.InjectLogger;
import com.apec.goodssource.dto.GoodsSourceDTO;
import com.apec.goodssource.service.GoodsSourceService;
import com.apec.goodssource.vo.GmcSkuBatchVO;
import com.apec.goodssource.vo.GmcSkuInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by hmy on 2017/12/21.
 *
 * @author hmy
 */
@RestController
@RequestMapping(value = "/goodssource")
public class GoodsSourceController extends MyBaseController {

    @InjectLogger
    private Logger log;

    @Autowired
    private GoodsSourceService goodsSourceService;

    /**
     * 添加货源
     */
    @RequestMapping(value = "/send", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public ResultData<String> sendGoodsSource(@RequestBody String json){
        ResultData<String> resultData = new ResultData<>();
        try {
            GmcSkuBatchVO gmcSkuInfoVO = getFormJSON(json, GmcSkuBatchVO.class);
            boolean flag = gmcSkuInfoVO == null ||  gmcSkuInfoVO.getUserId() == null
                    || gmcSkuInfoVO.getUserOrgId() == null || gmcSkuInfoVO.getListSkuInfo() == null  ;
            if(flag){
                setErrorResultDate(resultData, Constants.ERROR_100003);
                return resultData;
            }
            String returnCode;
            if(gmcSkuInfoVO.getExecUpdate()){
                returnCode =  goodsSourceService.updateGoodsSourceInfo(gmcSkuInfoVO);
            }else {
                returnCode  = goodsSourceService.addNewGoodsSourceInfo(gmcSkuInfoVO, String.valueOf(getUserId(json)));
            }
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                resultData.setSucceed(true);
            } else {
                setErrorResultDate(resultData, returnCode);
            }
        } catch (BusinessException e) {
            log.error("[GoodsSourceController.sendGoodsSource] Send addGoodsSource  BusinessException", e.getErrorCode());

        }catch (Exception e) {
            log.error("[GoodsSourceController.sendGoodsSource] Send addGoodsSource Exception", e);
        }
        return resultData;
    }

    /**
     * 活动奖品分页查询
     */
    @RequestMapping(value = "/listByPage", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String listGoodsSourceByPage(@RequestBody String json){
        try {
            GoodsSourceDTO goodsSourceDTO = getFormJSON(json, GoodsSourceDTO.class);
            PageRequest pageRequest = getPageRequest(goodsSourceDTO);
            return super.getResultJSONStr(true,goodsSourceService.queryGmcSkuInfoPage(goodsSourceDTO,pageRequest),null);

        } catch (BusinessException e) {
            log.error("[GoodsSourceController.listGoodsSourceByPage] listGoodsSourceByPage  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[GoodsSourceController.listGoodsSourceByPage] listGoodsSourceByPage Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

}
