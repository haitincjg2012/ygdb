package com.apec.product.web;

import com.alibaba.fastjson.JSONObject;
import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.PageJSON;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.excel.ExcelExportUtils;
import com.apec.framework.common.excel.XlsVO;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.common.util.DateTimeUtils;
import com.apec.framework.common.util.FileUtils;
import com.apec.framework.ftp.service.FtpService;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.springcloud.SpringCloudClient;
import com.apec.product.dto.ProductInfoDTO;
import com.apec.product.service.ProductService;
import com.apec.product.vo.ProductInfoVO;
import com.apec.product.vo.ProductSkuInfoVO;
import com.apec.product.vo.WeekBest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;


/**
 * 产品模块接口
 * @author yirder
 */
@RestController
@RequestMapping("/product")
public class ProductController extends MyBaseController {

    @InjectLogger
    private Logger log;

    @Autowired
    private ProductService productService;

    @Autowired
    private SpringCloudClient springCloudClient;

    @Autowired
    private FtpService ftpService;

    @Value("${EXCELFILEPATH}")
    String excelPath = "";

    @Value("${EXCELFILE_URL}")
    String excelUrl = "";

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
     */
    @RequestMapping(value = "/addProductAttr", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<List<Map<String,String>>> addProductAttr(@RequestBody String jsonStr) {
        ResultData<List<Map<String,String>>> resultData = new ResultData<>();
        try {
            PageJSON<String> pageJSON = super.getPageJSON(jsonStr, String.class);
            ProductSkuInfoVO productInfoVO = BaseJsonUtil.parseObject(pageJSON.getFormJSON(), ProductSkuInfoVO.class);

            List<Map<String,String>> resultMap = new ArrayList<>();
            String returnCode;
            if( productInfoVO.getUserId() != null){
                returnCode = productService.addProductAttrByInstead(productInfoVO.getUserId(), productInfoVO, resultMap);
            }else {
                returnCode = productService.addProductAttr(getUserInfo(pageJSON), productInfoVO, resultMap);
            }
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
            ProductInfoVO productInfoVO = BaseJsonUtil.parseObject(pageJSON.getFormJSON(), ProductInfoVO.class);
            String returnCode;
            if( productInfoVO.getUserId() != null){
                returnCode = productService.addNewProductByInstead(productInfoVO.getUserId(), productInfoVO);
            }else {
                returnCode = productService.addNewProduct(getUserInfo(pageJSON), productInfoVO);
            }
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
            ProductInfoVO productInfoVO = BaseJsonUtil.parseObject(pageJSON.getFormJSON(), ProductInfoVO.class);
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

    @RequestMapping(value = "/offSellProductByManager", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<String> offSellProductByManager(@RequestBody String jsonStr) {
        ResultData<String> resultData = new ResultData<>();
        try {
            PageJSON<String> pageJSON = super.getPageJSON(jsonStr, String.class);
            ProductInfoVO productInfoVO = BaseJsonUtil.parseObject(pageJSON.getFormJSON(), ProductInfoVO.class);
            if (StringUtils.isBlank(productInfoVO.getElasticId())) {
                log.warn("[ProductController][offSellProductByManager] Can't Update Product , Parameter verification failed!");
                setErrorResultDate(resultData, Constants.COMMON_MISSING_PARAMS);
                return resultData;
            }
            String returnCode = productService.offSellProductByManager(productInfoVO);
            if(StringUtils.equals(returnCode, Constants.RETURN_SUCESS)){
                resultData.setSucceed(true);
            }else{
                setErrorResultDate(resultData,returnCode);
            }
        }catch (BusinessException e){
            log.error("OffSell Product By Manager BusinessException", e);
            setErrorResultDate(resultData,e.getErrorCode());
        } catch (Exception e) {
            log.error("OffSell Product By Manager  Exception", e);
            setErrorResultDate(resultData,Constants.SYS_ERROR);
        }
        return resultData;
    }


    @RequestMapping(value = "/updateProductByES", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<String> updateProductByES(@RequestBody String jsonStr) {
        ResultData<String> resultData = new ResultData<>();
        try {
            PageJSON<String> pageJSON = super.getPageJSON(jsonStr, String.class);
            ProductInfoVO productInfoVO = BaseJsonUtil.parseObject(pageJSON.getFormJSON(), ProductInfoVO.class);
            boolean flag = StringUtils.isBlank(productInfoVO.getElasticId()) || (productInfoVO.getWeight() == null && null == productInfoVO.getAmount());
            //参数校验
            if (flag) {
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
            ProductInfoVO productInfoVO = BaseJsonUtil.parseObject(pageJSON.getFormJSON(), ProductInfoVO.class);
            boolean flag = StringUtils.isBlank(productInfoVO.getElasticId()) ||
                    ((productInfoVO.getStartAmount() == null || null == productInfoVO.getEndAmount()) && null == productInfoVO.getWeight());
            //参数校验
            if (flag) {
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

    @RequestMapping(value = "/pushProductToEs", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<String> pushProductToEs(@RequestBody String jsonStr){
        ResultData<String> resultData = getResultData(true, null, Constants.RETURN_SUCESS);
        try{

            PageJSON<String> pageJSON = super.getPageJSON(jsonStr, String.class);
            JSONObject formObj = BaseJsonUtil.parseObject(pageJSON.getFormJSON(), JSONObject.class);
            String indexUrl = (String) BaseJsonUtil.getValueBykey(jsonStr,"indexUrl");
            if(formObj!= null) {
                indexUrl = (String)formObj.get("indexUrl");
            }
            String returnCode = productService.pushProductToEs(indexUrl);
            if(!Constants.RETURN_SUCESS.equals(returnCode)) {
                setErrorResultDate(resultData, returnCode);
            }
        }catch (Exception e){
            log.error("[PrductController] [pushProductToEs] Exception", e);
            setErrorResultDate(resultData,Constants.SYS_ERROR);
        }
        return resultData;
    }

    @RequestMapping(value = "/pushOffSellProductToEs", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<String> pushOffSellProductToEs(@RequestBody String jsonStr){
        ResultData<String> resultData = getResultData(true, null, Constants.RETURN_SUCESS);
        try{
            PageJSON<String> pageJSON = super.getPageJSON(jsonStr, String.class);
            JSONObject formObj = BaseJsonUtil.parseObject(pageJSON.getFormJSON(), JSONObject.class);
            String indexUrl = (String) BaseJsonUtil.getValueBykey(jsonStr,"indexUrl");
            if(formObj!= null) {
                indexUrl = (String)formObj.get("indexUrl");
            }
            String returnCode = productService.pushOffSellProductToEs(indexUrl);
            if(!Constants.RETURN_SUCESS.equals(returnCode)) {
                setErrorResultDate(resultData, returnCode);
            }
        }catch (Exception e){
            log.error("[PrductController] [pushOffSellProductToEs] Exception", e);
            setErrorResultDate(resultData,Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     * 供求达人
     */
    @RequestMapping(value = "/maxProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String maxProduct(){
        try{
            List<WeekBest> weekBest = productService.maxProduct();
            return super.getResultJSONStr(true,weekBest,null);
        }catch (Exception e){
            log.error("[PrductController] [maxProduct] Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 供求达人
     */
    @RequestMapping(value = "/queryUserForRanking", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String queryUserForRanking(@RequestParam int num, @RequestParam String startTime ,@RequestParam String endTime){
        try{
            return super.getResultJSONStr(true, productService.queryUserForRanking(num,
                    DateTimeUtils.transferStrToDate(startTime), DateTimeUtils.transferStrToDate(endTime)),null);
        }catch (Exception e){
            log.error("[PrductController] [maxProduct] Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 查询用户上传的供求数
     */
    @RequestMapping(value = "/queryUserInfoForRanking", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String queryUserInfoForRanking(@RequestBody String json){
        try{
            WeekBest weekBest = getFormJSON(json,WeekBest.class);
            if(weekBest == null || CollectionUtils.isEmpty(weekBest.getUserIds())){
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }
            return super.getResultJSONStr(true, productService.queryUserInfoForRanking(weekBest.getUserIds()),null);
        }catch (Exception e){
            log.error("[PrductController] [maxProduct] Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 分页查询供求信息
     */
    @RequestMapping(value = "/productPage", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String productPage(@RequestBody String json){
        try {
            ProductInfoDTO productInfoDTO = getFormJSON(json, ProductInfoDTO.class);
            List<Sort.Order> orders = new ArrayList<>();
            orders.add(new Sort.Order(Sort.Direction.DESC, "orderWeight"));
            orders.add(new Sort.Order(Sort.Direction.DESC, "createDate"));
            int pageNumber = 1;
            int pageSize = 10;
            if (productInfoDTO.getPageNumber() > 0) {
                pageNumber = productInfoDTO.getPageNumber();
            }
            if (productInfoDTO.getPageSize() > 0 && productInfoDTO.getPageSize() < Integer.valueOf(Constants.MAX_FETCHSIZE)) {
                pageSize = productInfoDTO.getPageSize();
            }
            PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, new Sort(orders));
            if(StringUtils.isNotBlank(productInfoDTO.getUserName())){
                String server = "yg-user-service";
                String method = "user/listUserId";
                Map<String,String> map = new HashMap<>(16);
                map.put("name",productInfoDTO.getUserName());
                ResultData<List<Long>> userIds = callServer(server,method,map);
                if(userIds.isSucceed()){
                    List<Long> userList = new ArrayList<>();
                    userList.addAll(userIds.getData());
                    productInfoDTO.setUserIds(userList);
                }
            }
            return super.getResultJSONStr(true,productService.queryProductPage(productInfoDTO,pageRequest),null);

        } catch (BusinessException e) {
            log.error("[product.productPage] productPage  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[product.productPage] productPage Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 请求其他服务
     */
    private ResultData callServer(String server, String method, Map<String,String> reqMap){
        ResultData resultData;
        String url = Constants.HTTP_COLON + Constants.DOUBLE_SLASH + server + Constants.SINGLE_SLASH + method;
        try{
            String res = springCloudClient.post(url, BaseJsonUtil.toJSONString(reqMap));
            resultData = BaseJsonUtil.parseObject(res, ResultData.class);
        }catch (Exception e){
            log.error("调用后台服务异常 " + url, e);
            resultData = getResultData(false, null, Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     * 查询供求详情通过esId
     */
    @RequestMapping(value = "/productInfoByEsId", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String productInfoByEsId(@RequestBody String json){
        try {
            ProductInfoVO productInfoVO = getFormJSON(json, ProductInfoVO.class);
            boolean flag = null == productInfoVO || StringUtils.isBlank(productInfoVO.getElasticId());
            return super.getResultJSONStr(true,productService.queryProductInfoByEsId(productInfoVO),null);

        } catch (BusinessException e) {
            log.error("[product.productInfoByEsId] productInfoByEsId  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,e.getErrorCode());
        }catch (Exception e) {
            log.error("[product.productInfoByEsId] productInfoByEsId Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 给供求设置标签
     */
    @RequestMapping(value = "/setProductTags", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String setProductTags(@RequestBody String json){
        try {
            ProductInfoVO productInfoVO = getFormJSON(json, ProductInfoVO.class);
            boolean flag = null == productInfoVO || StringUtils.isBlank(productInfoVO.getElasticId());
            if(flag){
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }
            String result = productService.setProductTags(productInfoVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true,null,null);
            }else{
                return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
            }

        } catch (BusinessException e) {
            log.error("[product.setProductTags] setProductTags  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,e.getErrorCode());
        }catch (Exception e) {
            log.error("[product.setProductTags] setProductTags Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 置顶供求信息
     */
    @RequestMapping(value = "/stickProductInfo", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String stickProductInfo(@RequestBody String json){
        try {
            ProductInfoVO productInfoVO = getFormJSON(json, ProductInfoVO.class);
            boolean flag = null == productInfoVO || productInfoVO.getId() == null || productInfoVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }
            String result = productService.stickProductInfo(productInfoVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true,null,null);
            }else{
                return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
            }

        } catch (BusinessException e) {
            log.error("[product.stickProductInfo] stickProductInfo  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,e.getErrorCode());
        }catch (Exception e) {
            log.error("[product.stickProductInfo] stickProductInfo Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 取消置顶供求信息
     */
    @RequestMapping(value = "/closeStickProductInfo", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String closeStickProductInfo(@RequestBody String json){
        try {
            ProductInfoVO productInfoVO = getFormJSON(json, ProductInfoVO.class);
            boolean flag = null == productInfoVO || productInfoVO.getId() == null || productInfoVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }
            String result = productService.closeStickProductInfo(productInfoVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true,null,null);
            }else{
                return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
            }

        } catch (BusinessException e) {
            log.error("[product.closeStickProductInfo] closeStickProductInfo  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,e.getErrorCode());
        }catch (Exception e) {
            log.error("[product.closeStickProductInfo] closeStickProductInfo Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 导出供求信息
     */
    @RequestMapping(value = "/exportExcel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String exportExcel(@RequestBody String json) {
        try {
            ProductInfoDTO productInfoDTO = getParamJSON(json, ProductInfoDTO.class);
            if(StringUtils.isNotBlank(productInfoDTO.getUserName())){
                String server = "yg-user-service";
                String method = "user/listUserId";
                Map<String,String> map = new HashMap<>(16);
                map.put("name",productInfoDTO.getUserName());
                ResultData<List<Long>> userIds = callServer(server,method,map);
                if(userIds.isSucceed()){
                    List<Long> userList = new ArrayList<>();
                    userList.addAll(userIds.getData());
                    productInfoDTO.setUserIds(userList);
                }
            }
            String[] excelHeader = new String[] {"供求", "编号", "标题","供应量", "单价(元/斤)", "区域","发布人","身份","发布日期","标签","下架剩余天数","状态"};
            String fileName = ExcelExportUtils.getExcelFileName("product");
            List<Object[]> results = productService.selectProductInfoForExcel(productInfoDTO);
            String filePath = FileUtils.getFileRelativePath(excelPath);
            ByteArrayOutputStream os = ExcelExportUtils.exportExcel(excelHeader, results, false);
            byte[] b = os.toByteArray();
            ByteArrayInputStream in = new ByteArrayInputStream(b);
            ftpService.uploadFile(filePath, fileName, in);
            XlsVO xlsVO = new XlsVO();
            xlsVO.setFileName(fileName);
            xlsVO.setUrl(excelUrl +filePath + fileName);
            return super.getResultJSONStr(true, xlsVO, null);
        } catch (Exception e) {
            log.error("[user][exportExcel] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }

    }



}
