package com.apec.product.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.PageJSON;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.common.util.JsonUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.product.service.ProductService;
import com.apec.product.vo.ProductInfoVO;
import com.apec.product.vo.ProductSkuInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 产品模块接口
 * @author yirder
 */
@RestController
@RequestMapping("/product")
public class ProductController extends MyBaseController {

    @InjectLogger
    private Logger log;

    /**
     * 产品服务
     */
    @Autowired
    private ProductService productService;

    /**
     * 添加求购信息
     * @return ResultData
     */
    @RequestMapping(value = "/testEsInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public  ResultData<String>  testESInfo() {
        ResultData<String> resultData = new ResultData<>();
        resultData.setSucceed(true);
        return resultData;
    }

    /**
     * 添加Product的sku 属性
     * @param jsonStr
     * @return
     */
    @RequestMapping(value = "/addProductAttr", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<List<Map<String,String>>> addProductAttr(@RequestBody String jsonStr) {
        ResultData<List<Map<String,String>>> resultData = new ResultData<>();
        try {
            PageJSON<String> pageJSON = super.getPageJSON(jsonStr, String.class);
            ProductSkuInfoVO productInfoVO = JsonUtil.parseObject(pageJSON.getFormJSON(), ProductSkuInfoVO.class);
            List<Map<String,String>> resultMap = new ArrayList<>();
            String returnCode = productService.addProductAttr(getUserInfo(pageJSON), productInfoVO,resultMap);
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                resultData.setData(resultMap);
                resultData.setSucceed(true);
            } else {
                setErrorResultDate(resultData, returnCode);
            }

        } catch (BusinessException e) {
            log.error("Add  ProductAttr BusinessException", e);
            setErrorResultDate(resultData, e.getErrorCode());
        }catch (Exception e) {
            log.error("Add  ProductAttr Exception", e);
            setErrorResultDate(resultData,Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     * 添加求购信息
     * @return ResultData
     */
    @RequestMapping(value = "/addNewProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<String> addNewProduct(@RequestBody String jsonStr) {
        ResultData<String> resultData = new ResultData<>();
        try {
            PageJSON<String> pageJSON = super.getPageJSON(jsonStr, String.class);
            ProductInfoVO productInfoVO = JsonUtil.parseObject(pageJSON.getFormJSON(), ProductInfoVO.class);
            String returnCode = productService.addNewProduct(getUserInfo(pageJSON), productInfoVO);
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                resultData.setSucceed(true);
            } else {
                setErrorResultDate(resultData, returnCode);
            }

        } catch (BusinessException e) {
            log.error("Add ProductInfo BusinessException", e);
            setErrorResultDate(resultData, e.getErrorCode());
        }catch (Exception e) {
            log.error("Add  ProductInfo Exception", e);
            setErrorResultDate(resultData,Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     * 求购信息异步落地
     * @return ResultData
     */
    @RequestMapping(value = "/saveProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<String> saveProduct(@RequestBody String jsonStr){
        ResultData<String> resultData = new ResultData<>();
        try {
            ProductInfoVO productInfoVO  = getFormJSON(jsonStr,ProductInfoVO.class);
            String returnCode = productService.saveProduct(productInfoVO);
            if(StringUtils.equals(returnCode, Constants.RETURN_SUCESS)){
                resultData.setSucceed(true);
            }else{
                setErrorResultDate(resultData,returnCode);
            }
        }catch (BusinessException e){
            log.error("Save saveProduct BusinessException", e);
            setErrorResultDate(resultData,e.getErrorCode());
        } catch (Exception e) {
            log.error("Save  saveProduct Exception", e);
            setErrorResultDate(resultData,Constants.SYS_ERROR);
        }
        return resultData;
    }

    @RequestMapping("/offSellJob")
    public ResultData<String> offSellJob(){
        ResultData<String> resultData = new ResultData<>();
        setErrorResultDate(resultData,Constants.RETURN_SUCESS);
        try{
            productService.offSellProductJob();
        }catch (Exception e){
            log.error("run offSell job Exception", e);
            setErrorResultDate(resultData,Constants.SYS_ERROR);
        }
        return resultData;
    }

    @RequestMapping(value = "/offSellProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<String> offSellProduct(@RequestBody String jsonStr) {
        ResultData<String> resultData = new ResultData<>();
        try {
            PageJSON<String> pageJSON = super.getPageJSON(jsonStr, String.class);
            ProductInfoVO productInfoVO = JsonUtil.parseObject(pageJSON.getFormJSON(), ProductInfoVO.class);
            if (StringUtils.isBlank(productInfoVO.getElasticId())) {
                log.warn("[ProductController][offSellProduct] Can't Update Product , Parameter verification failed!");
                setErrorResultDate(resultData, Constants.COMMON_MISSING_PARAMS);
                return resultData;
            }
            String returnCode = productService.offSellProduct(getUserInfo(pageJSON), productInfoVO);
            if(StringUtils.equals(returnCode, Constants.RETURN_SUCESS)){
                resultData.setSucceed(true);
            }else{
                setErrorResultDate(resultData,returnCode);
            }
        }catch (BusinessException e){
            log.error("offSell Product BusinessException", e);
            setErrorResultDate(resultData,e.getErrorCode());
        } catch (Exception e) {
            log.error("offSell Product Exception", e);
            setErrorResultDate(resultData,Constants.SYS_ERROR);
        }
        return resultData;
    }

    @RequestMapping(value = "/updateProductByES", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<String> updateProductByES(@RequestBody String jsonStr) {
        ResultData<String> resultData = new ResultData<>();
        try {
            PageJSON<String> pageJSON = super.getPageJSON(jsonStr, String.class);
            ProductInfoVO productInfoVO = JsonUtil.parseObject(pageJSON.getFormJSON(), ProductInfoVO.class);
            //参数校验
            if (null == productInfoVO.getElasticId() || (productInfoVO.getWeight() == null && null == productInfoVO.getAmount())) {
                log.warn("[ProductController][updateProductByES] Can't Update Product , Parameter verification failed!");
                setErrorResultDate(resultData, Constants.COMMON_MISSING_PARAMS);
                return resultData;
            }
            String returnCode = productService.updateProductByES(getUserInfo(pageJSON), productInfoVO);
            if(StringUtils.equals(returnCode, Constants.RETURN_SUCESS)){
                resultData.setSucceed(true);
            }else{
                setErrorResultDate(resultData,returnCode);
            }
        }catch (BusinessException e){
            log.error("update Product ES BusinessException", e);
            setErrorResultDate(resultData,e.getErrorCode());
        } catch (Exception e) {
            log.error("update Product ES Exception", e);
            setErrorResultDate(resultData,Constants.SYS_ERROR);
        }
        return resultData;
    }

    @RequestMapping(value = "/updateBuyProductByES", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<String> updateBuyProductByES(@RequestBody String jsonStr) {
        ResultData<String> resultData = new ResultData<>();
        try {
            PageJSON<String> pageJSON = super.getPageJSON(jsonStr, String.class);
            ProductInfoVO productInfoVO = JsonUtil.parseObject(pageJSON.getFormJSON(), ProductInfoVO.class);
            //参数校验
            if (null == productInfoVO.getElasticId() ||
                    ((productInfoVO.getStartAmount() == null || null == productInfoVO.getEndAmount()) && null == productInfoVO.getWeight())) {
                log.warn("[ProductController][updateBuyProductByES] Can't Update Product , Parameter verification failed!");
                setErrorResultDate(resultData, Constants.COMMON_MISSING_PARAMS);
                return resultData;
            }else if(productInfoVO.getStartAmount() != null && productInfoVO.getEndAmount() != null){
                //判断金额区间
                if(productInfoVO.getStartAmount().compareTo(productInfoVO.getEndAmount()) != -1){
                    log.warn("[ProductController][updateBuyProductByES] Can't Update Product ,endAmount <= startAmount parameter verification failed!");
                    setErrorResultDate(resultData, ErrorCodeConst.ERROR_PRODUCT_AMOUNT_RANGE);
                    return resultData;
                }
            }
            String returnCode = productService.updateProductByES(getUserInfo(pageJSON), productInfoVO);
            if(StringUtils.equals(returnCode, Constants.RETURN_SUCESS)){
                resultData.setSucceed(true);
            }else{
                setErrorResultDate(resultData,returnCode);
            }
        }catch (BusinessException e){
            log.error("update Product ES BusinessException", e);
            setErrorResultDate(resultData,e.getErrorCode());
        } catch (Exception e) {
            log.error("update Product ES Exception", e);
            setErrorResultDate(resultData,Constants.SYS_ERROR);
        }
        return resultData;
    }

    @RequestMapping(value = "/updateProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<String> updateProduct(@RequestBody String jsonStr) {
        ResultData<String> resultData = new ResultData<>();
        try {
            ProductInfoVO productInfoVO  = getFormJSON(jsonStr,ProductInfoVO.class);
            String returnCode = productService.updateProduct(productInfoVO);
            if(StringUtils.equals(returnCode, Constants.RETURN_SUCESS)){
                resultData.setSucceed(true);
            }else{
                setErrorResultDate(resultData,returnCode);
            }
        }catch (BusinessException e){
            log.error("update Product BusinessException", e);
            setErrorResultDate(resultData,e.getErrorCode());
        } catch (Exception e) {
            log.error("update Product Exception", e);
            setErrorResultDate(resultData,Constants.SYS_ERROR);
        }
        return resultData;
    }

}
