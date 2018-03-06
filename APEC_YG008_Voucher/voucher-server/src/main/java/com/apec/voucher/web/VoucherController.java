package com.apec.voucher.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.apec.framework.springcloud.SpringCloudClient;
import com.apec.voucher.viewvo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONArray;
import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.PageJSON;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.dto.ImageUploadVO;
import com.apec.framework.log.InjectLogger;
import com.apec.voucher.dto.VoucherDTO;
import com.apec.voucher.service.VoucherService;
import com.apec.voucher.vo.VoucherGoodsVO;
import com.apec.voucher.vo.VoucherVO;

/**
 * 上传凭证controller
 * @author gunj
 * create by 2017-07-14
 * */
@RestController
@RequestMapping(value = "/voucher")
public class VoucherController extends MyBaseController{
	
	@InjectLogger
	private Logger log;
	
	@Autowired
	private VoucherService voucherService;

	@Autowired
	private SpringCloudClient springCloudClient;

	/**
	 * 添加上传凭证信息
	 * */
	@RequestMapping(value = "/addVoucherInfo", method = RequestMethod.POST)
	public ResultData<String> addVoucherInfo(@RequestBody String json){

		try {
			json = URLDecoder.decode(json, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			log.error("addVoucherInfo Excetion :{}",e1);
			return getResultData(false, null, Constants.SYS_ERROR);
		}
		//获取上传凭据信息
		PageJSON<String> pageJson = super.getPageJSON(json, String.class);
		VoucherVO voucherVO = BaseJsonUtil.parseObject(pageJson.getFormJSON(), VoucherVO.class);
		if (voucherVO == null){
			return getResultData(false, null, Constants.ERROR_100003);
		}
		if (voucherVO.getType() == null || StringUtils.isBlank(voucherVO.getName()) || voucherVO.getDeliveryTime() == null ){
			log.warn("param is null,[cityId:{},countyId:{},townId:{},userType:{},name:{},deliveryTime:{}]",voucherVO.getCityId()
					,voucherVO.getCountyId(),voucherVO.getTownId(),voucherVO.getType(),voucherVO.getName(),voucherVO.getDeliveryTime());
			return getResultData(false, null, Constants.COMMON_MISSING_PARAMS);
		}
		if (voucherVO.getDeliveryTime().getTime()>System.currentTimeMillis()){
			return getResultData(false, "", ErrorCodeConst.VOUCHER_DELIVERY_TIME_PASS);
		}
		if (CollectionUtils.isEmpty(voucherVO.getVoucherGoodsVO())){
			log.warn("voucherGoodsVO is null");
			return getResultData(false, null, Constants.COMMON_MISSING_PARAMS);
		}
		for (VoucherGoodsVO voucherGoodsVO : voucherVO.getVoucherGoodsVO()){
			if (voucherGoodsVO.getNumber() == null || voucherGoodsVO.getAmount() == null || voucherGoodsVO.getTotalAmount() == null
					|| voucherGoodsVO.getSkuId() == null || StringUtils.isEmpty(voucherGoodsVO.getSkuName())){
				log.warn("param is null,[number:{},amount:{},totalAmont:{},skuId:{},skuName:{}]",voucherGoodsVO.getNumber(),voucherGoodsVO.getAmount(),
						voucherGoodsVO.getTotalAmount(),voucherGoodsVO.getSkuId(),voucherGoodsVO.getSkuName());
				return getResultData(false, null, Constants.COMMON_MISSING_PARAMS);
			}
		}
		if(voucherVO.getUserId() == null || voucherVO.getUserId() == 0L){
			//获取用户信息
			voucherVO.setUserId(getUserId(json));
		}
		//获取用户上传图片路径
		JSONArray imageListJsonArray = (JSONArray)pageJson.getRequestAttrMap().get("imageItems");
		if (CollectionUtils.isNotEmpty(imageListJsonArray)){
			List<ImageUploadVO> imageUploadVOS = BaseJsonUtil.parseArray(imageListJsonArray.get(0).toString(), ImageUploadVO.class);
			if (CollectionUtils.isNotEmpty(imageUploadVOS)){
				StringBuilder sb = new StringBuilder();
				String voucherUrl = sb.append(imageUploadVOS.get(0).getImagePath()).toString();
				voucherVO.setVoucherUrl(voucherUrl);
			}
		}
		
		ResultData<String> resultData = new ResultData<>();
		try {
			String resultCode = voucherService.addVoucherInfo(voucherVO);
			if (StringUtils.equals(Constants.RETURN_SUCESS, resultCode) || StringUtils.equals(Constants.VOUCHER_NOT_EXCEED, resultCode)){
				resultData.setSucceed(true);
			}else{
				setErrorResultDate(resultData, resultCode);
			}
		} catch(BusinessException e){
			log.error("addVoucherInfo BusinessException :{}",e);
			setErrorResultDate(resultData, Constants.SERVER_RESEST_EXCEPTION);;
		} catch (Exception e) {
			log.error("addVoucherInfo Excetion :{}",e);
			setErrorResultDate(resultData, Constants.SYS_ERROR);
		}
		return resultData;
	}

	/**
	 * 添加上传凭证信息
	 * */
	@RequestMapping(value = "/addNewVoucherInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<String> addNewVoucherInfo(@RequestBody String json){
		ResultData<String> resultData = new ResultData<>();
		VoucherVO voucherVO = getFormJSON(json,VoucherVO.class);
		if (voucherVO == null){
			return getResultData(false, null, Constants.ERROR_100003);
		}
		boolean flag = voucherVO.getType() == null || StringUtils.isBlank(voucherVO.getName()) || voucherVO.getDeliveryTime() == null;
		if (flag){
			log.warn("param is null,[cityId:{},countyId:{},townId:{},userType:{},name:{},deliveryTime:{}]",voucherVO.getCityId()
					,voucherVO.getCountyId(),voucherVO.getTownId(),voucherVO.getType(),voucherVO.getName(),voucherVO.getDeliveryTime());
			return getResultData(false, null, Constants.COMMON_MISSING_PARAMS);
		}
		if (voucherVO.getDeliveryTime().getTime()>System.currentTimeMillis()){
			return getResultData(false, "", ErrorCodeConst.VOUCHER_DELIVERY_TIME_PASS);
		}
		if (CollectionUtils.isEmpty(voucherVO.getVoucherGoodsVO())){
			log.warn("voucherGoodsVO is null");
			return getResultData(false, null, Constants.COMMON_MISSING_PARAMS);
		}
		for (VoucherGoodsVO voucherGoodsVO : voucherVO.getVoucherGoodsVO()){
			if (voucherGoodsVO.getNumber() == null || voucherGoodsVO.getAmount() == null || voucherGoodsVO.getTotalAmount() == null
					|| voucherGoodsVO.getSkuId() == null || StringUtils.isEmpty(voucherGoodsVO.getSkuName())){
				log.warn("param is null,[number:{},amount:{},totalAmont:{},skuId:{},skuName:{}]",voucherGoodsVO.getNumber(),voucherGoodsVO.getAmount(),
						voucherGoodsVO.getTotalAmount(),voucherGoodsVO.getSkuId(),voucherGoodsVO.getSkuName());
				return getResultData(false, null, Constants.COMMON_MISSING_PARAMS);
			}
		}
		if(voucherVO.getUserId() == null || voucherVO.getUserId() == 0L){
			//获取用户信息
			voucherVO.setUserId(getUserId(json));
		}
		try {
			String resultCode = voucherService.addVoucherInfo(voucherVO);
			if (StringUtils.equals(Constants.RETURN_SUCESS, resultCode) || StringUtils.equals(Constants.VOUCHER_NOT_EXCEED, resultCode)){
				resultData.setSucceed(true);
			}else{
				setErrorResultDate(resultData, resultCode);
			}
		} catch(BusinessException e){
			log.error("addNewVoucherInfo BusinessException :{}",e);
			setErrorResultDate(resultData, Constants.SERVER_RESEST_EXCEPTION);
		} catch (Exception e) {
			log.error("addNewVoucherInfo Excetion :{}",e);
			setErrorResultDate(resultData, Constants.SYS_ERROR);
		}
		return resultData;
	}

	/**
	 * 文章审核
	 */
	@RequestMapping(value = "/voucherReview", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String voucherReview(@RequestBody String json) {
		try {
			VoucherVO voucherVO = getFormJSON(json,VoucherVO.class);
			boolean flag = voucherVO == null || voucherVO.getId() == null || voucherVO.getId() == 0L;
			if(flag){
				return super.getResultJSONStr(false,null,Constants.ERROR_100003);
			}
			if(StringUtils.isBlank(voucherVO.getAuditState()) || (!StringUtils.equals(voucherVO.getAuditState(),Constants.ISSUCCESS) && !StringUtils.equals(voucherVO.getAuditState(),Constants.ISNOTSUCCESS))){
				return Constants.COMMON_ERROR_PARAMS;
			}
			String returnCode = voucherService.voucherReview(voucherVO,String.valueOf(getUserId(json)));
			if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
				return super.getResultJSONStr(true,null,null);
			} else {
				return super.getResultJSONStr(false,null,returnCode);
			}
		}catch (BusinessException e) {
			log.error("[societyPost][articleReview] articleReview BusinessException", e);
			return super.getResultJSONStr(false,null,e.getErrorCode());
		}catch (Exception e) {
			log.error("[societyPost][articleReview]  articleReview Exception", e);
			return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
		}
	}

	
	/**
	 * 查询上传凭据信息
	 * */
	@RequestMapping(value = "/getVoucherInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<VoucherRespViewVO> getVoucherInfo(@RequestBody String json){
		
	    try {
	    	//获取用户id
	    	VoucherDTO voucherDTO = getFormJSON(json, VoucherDTO.class);
	    	Long userId = getUserId(json);
	    	if (userId == null || userId == 0L){
		    	return getResultData(false, null, Constants.ERROR_100003);
		    }
	    	voucherDTO.setUserId(userId);
			List<Sort.Order> orders = new ArrayList<>();
			orders.add(new Sort.Order(Sort.Direction.DESC, "createDate"));
			orders.add(new Sort.Order(Sort.Direction.DESC, "id"));
			int pageNumber = 1;
			int pageSize = 10;
			if (voucherDTO.getPageNumber() > 0) {
				pageNumber = voucherDTO.getPageNumber();
			}
			if (voucherDTO.getPageSize() > 0 && voucherDTO.getPageSize() < Integer.valueOf(Constants.MAX_FETCHSIZE)) {
				pageSize = voucherDTO.getPageSize();
			}
			PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, new Sort(orders));
	    	//获取分页信息
	    		    	
	    	VoucherRespViewVO voucherRespVVO = voucherService.listVoucherInfo(voucherDTO, pageRequest);
	    	return getResultData(true, voucherRespVVO, "");
	    } catch(BusinessException e){
			log.error("Add BusinessException :{}",e);
			return getResultData(false, null, Constants.SERVER_RESEST_EXCEPTION);
		} catch (Exception e) {
			log.error("Add Excetion :{}",e);
			return getResultData(false, null, Constants.SYS_ERROR);
	    }
	}
	
	/**
	 * 后台交收单管理列表
	 * */
	@RequestMapping(value = "/getVoucherBSViewVO", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<PageDTO<VoucherBSViewVO>> listVoucherBSViewVOGroubById(@RequestBody String json){
		
		try {
			VoucherDTO voucherDTO = getFormJSON(json, VoucherDTO.class);
			PageRequest pageRequest = genPageRequest(voucherDTO);
			List<Long> userId = null;
			if(voucherDTO != null && StringUtils.isNotBlank(voucherDTO.getUserName())){
				//通过作者查询信息
				//发送client请求用户系统，查询相关的用户id信息
				String server = "yg-user-service";
				String method = "user/listUserId";
				Map<String,String> map = new HashMap<>(16);
				map.put("name",voucherDTO.getUserName());
				ResultData<List<Long>> userIds = callServer(server,method,map);
				if(userIds.isSucceed()){
					userId = userIds.getData();
				}
				if(CollectionUtils.isEmpty(userId)){
					return getResultData(true, new PageDTO<VoucherBSViewVO>(), "");
				}
			}

			PageDTO<VoucherBSViewVO> voucherBSViewVOList = voucherService.listVoucherBSViewVOGroubById(voucherDTO,userId,pageRequest);
			return getResultData(true, voucherBSViewVOList, "");
		} catch(BusinessException e){
			log.error("getVoucherBSViewVO BusinessException :{}",e);
			return getResultData(false, null, Constants.SERVER_RESEST_EXCEPTION);
		} catch (Exception e) {
			log.error("getVoucherBSViewVO Excetion :{}",e);
			return getResultData(false, null, Constants.SYS_ERROR);
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
	 * 后台交收单管理页合计总重量
	 * */
	@RequestMapping(value = "/getVoucherBSSumWeight", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String getVoucherBSSumWeight(@RequestBody String json){

		try {
			VoucherDTO voucherDTO = getFormJSON(json, VoucherDTO.class);
			List<Long> userId = null;
			if(voucherDTO != null && StringUtils.isNotBlank(voucherDTO.getUserName())){
				//通过作者查询信息
				//发送client请求用户系统，查询相关的用户id信息
				String server = "yg-user-service";
				String method = "user/listUserId";
				Map<String,String> map = new HashMap<>(16);
				map.put("name",voucherDTO.getUserName());
				ResultData<List<Long>> userIds = callServer(server,method,map);
				if(userIds.isSucceed()){
					userId = userIds.getData();
				}
				if(CollectionUtils.isEmpty(userId)){
					return getResultJSONStr(true, new VoucherBSViewVO(), null);
				}
			}
			return getResultJSONStr(true, voucherService.getVoucherBSSumWeight(voucherDTO,userId), "");
		} catch(BusinessException e){
			log.error("getVoucherBSSumWeight BusinessException :{}",e);
			return getResultJSONStr(false, null, Constants.SERVER_RESEST_EXCEPTION);
		} catch (Exception e) {
			log.error("getVoucherBSSumWeight Excetion :{}",e);
			return getResultJSONStr(false, null, Constants.SYS_ERROR);
		}
	}
	
	/**
	 * 交收单详情
	 * */
	@RequestMapping(value = "/getVoucherInfoById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<VoucherViewVO> getVoucherInfoById(@RequestBody String json){
		
		try {
			VoucherDTO voucherDTO = getFormJSON(json, VoucherDTO.class);
			if (voucherDTO.getVoucherId() == null){
				log.warn("param is null,[id:{}]" ,voucherDTO.getVoucherId());
				return getResultData(false, null, Constants.ERROR_100003);
			}
			VoucherViewVO voucherViewVO = voucherService.findVoucherInfoById(voucherDTO.getVoucherId());
			return getResultData(true, voucherViewVO, "");
		} catch (BusinessException e) {
			log.error("Add BusinessException :{}",e);
			return getResultData(false, null, Constants.SERVER_RESEST_EXCEPTION);
		} catch (Exception e) {
			log.error("Add Excetion :{}",e);
			return getResultData(false, null, Constants.SYS_ERROR);
		}
	}
	
	/**
	 * 后台删除交收单数据
	 * */
	@RequestMapping(value = "/deleteBSVoucherInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<Integer> deleteBSVoucherInfo(@RequestBody String json){
		
		VoucherDTO voucherDTO = getFormJSON(json, VoucherDTO.class);
		if (voucherDTO.getVoucherId() == null || voucherDTO.getUserId() == null){
			log.warn("param is null,[voucherid:{},userid:{}]" ,voucherDTO.getVoucherId(),voucherDTO.getUserId());
			return getResultData(false, null, Constants.ERROR_100003);
		}
		try {
			int resultCode = voucherService.deleteVoucherInfo(voucherDTO);
			return getResultData(true, resultCode, "");
		} catch(BusinessException e){
			log.error("Add BusinessException :{}",e);
			return getResultData(false, null, Constants.SERVER_RESEST_EXCEPTION);
		} catch (Exception e) {
			log.error("Add Excetion :{}",e);
			return getResultData(false, null, Constants.SYS_ERROR);
		}
	}
	
	/**
	 * 删除交收单数据
	 * */
	@RequestMapping(value = "/deleteVoucherInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<Integer> deleteVoucherInfo(@RequestBody String json){
		
		Long userId = getUserId(json);
		VoucherDTO voucherDTO = getFormJSON(json, VoucherDTO.class);
		voucherDTO.setUserId(userId);
		if (voucherDTO.getVoucherId() == null || voucherDTO.getUserId() == null){
			log.warn("param is null,[voucherid:{},userid:{}]" ,voucherDTO.getVoucherId(),voucherDTO.getUserId());
			return getResultData(false, null, Constants.ERROR_100003);
		}
		try {
			int resultCode = voucherService.deleteVoucherInfo(voucherDTO);
			return getResultData(true, resultCode, "");
		} catch(BusinessException e){
			log.error("deleteVoucherInfo BusinessException :{}",e);
			return getResultData(false, null, Constants.SERVER_RESEST_EXCEPTION);
		} catch (Exception e) {
			log.error("deleteVoucherInfo Excetion :{}",e);
			return getResultData(false, null, Constants.SYS_ERROR);
		}
	}

	/**
	 * 代办调货分析信息
	 * */
	@RequestMapping(value = "/getDBVoucherInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<DBVoucherViewVO> getDBVoucherInfo(@RequestBody String json){
		
		try {
			VoucherDTO voucherDTO = getFormJSON(json, VoucherDTO.class);
			DBVoucherViewVO dbVoucherViewVO = voucherService.listDBVoucherInfo(voucherDTO);
			return getResultData(true, dbVoucherViewVO, "");
		} catch(BusinessException e){
			log.error("Add BusinessException :{}",e);
			return getResultData(false, null, Constants.SERVER_RESEST_EXCEPTION);
		} catch (Exception e) {
			log.error("Add Excetion :{}",e);
			return getResultData(false, null, Constants.SYS_ERROR);
	    }
	}
	
	/**
	 * 首页获取个人平台调果排行
	 * */
	@RequestMapping(value = "/getNumberRankViewVO", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<DBNumberRankViewVO> getNumberRankViewVO(@RequestBody String json){
		
		try {
			PageJSON<String> pageJson = super.getPageJSON(json, String.class);
			VoucherDTO voucherDTO = BaseJsonUtil.parseObject(pageJson.getFormJSON(), VoucherDTO.class);
			Long userId = voucherDTO.getUserId();
			if (userId == null){
				return getResultData(false, null, Constants.ERROR_100003);
			}
			DBNumberRankViewVO data = voucherService.findNumberRankViewVO(userId);
			return getResultData(true, data, "");		
		} catch(BusinessException e){
			log.error("Add BusinessException :{}",e);
			return getResultData(false, null, Constants.SERVER_RESEST_EXCEPTION);
		} catch (Exception e) {
			log.error("Add Excetion :{}",e);
			return getResultData(false, null, Constants.SYS_ERROR);
	    }
	}

	/**
	 * 个人主页获取个人平台调果排行
	 * */
	@RequestMapping(value = "/getSelfNumberRankViewVO", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<DBNumberRankViewVO> getSelfNumberRankViewVO(@RequestBody String json){

		try {
			Long userId = getUserId(json);
			if (userId == null){
				return getResultData(false, null, Constants.ERROR_100003);
			}
			DBNumberRankViewVO data = voucherService.findNumberRankViewVO(userId);
			return getResultData(true, data, "");
		} catch(BusinessException e){
			log.error("getSelfNumberRankViewVO Add BusinessException :{}",e);
			return getResultData(false, null, Constants.SERVER_RESEST_EXCEPTION);
		} catch (Exception e) {
			log.error("getSelfNumberRankViewVO Add Excetion :{}",e);
			return getResultData(false, null, Constants.SYS_ERROR);
		}
	}
	
	/**
	 * 获取代办上传单据数量月榜
	 * */
	@RequestMapping(value = "/listMonthDBNumberRankViewVO", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<PageDTO<DBNumberRankViewVO>> listMonthDBNumberRankViewVO(@RequestBody(required = false) String json){

		try {
			VoucherDTO voucherDTO = getFormJSON(json, VoucherDTO.class);
			PageDTO<DBNumberRankViewVO> data = voucherService.listMonthDBNumberRankViewVO(voucherDTO);
			return getResultData(true, data, "");		
		} catch(BusinessException e){
			log.error("Add BusinessException :{}",e);
			return getResultData(false, null, Constants.SERVER_RESEST_EXCEPTION);
		} catch (Exception e) {
			log.error("Add Excetion :{}",e);
			return getResultData(false, null, Constants.SYS_ERROR);
	    }
	}
	
	/**
	 * 获取代办上传单据数量总榜
	 * */
	@RequestMapping(value = "/listTotalDBNumberRankViewVO", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<PageDTO<DBNumberRankViewVO>> listTotalDBNumberRankViewVO(@RequestBody(required = false) String json){

		try {
			VoucherDTO voucherDTO = getFormJSON(json, VoucherDTO.class);
			PageDTO<DBNumberRankViewVO> data = voucherService.listTotalDBNumberRankViewVO(voucherDTO);
			return getResultData(true, data, "");		
		} catch(BusinessException e){
			log.error("Add BusinessException :{}",e);
			return getResultData(false, null, Constants.SERVER_RESEST_EXCEPTION);
		} catch (Exception e) {
			log.error("Add Excetion :{}",e);
			return getResultData(false, null, Constants.SYS_ERROR);
	    }
	}

	/**
	 * 更新用户缓存量(定时任务)
	 * */
	@RequestMapping(value = "/countVoucherOfUser")
	public String countVoucherOfUser(){
		try {
			String result = voucherService.countVoucherOfUser();
			if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
				return getResultJSONStr(true, null, null);
			}else{
				return getResultJSONStr(false, null, result);
			}
		} catch(BusinessException e){
			log.error("【voucher】[countVoucherOfUser]Add BusinessException :{}",e);
			return getResultJSONStr(false, null, Constants.SERVER_RESEST_EXCEPTION);
		} catch (Exception e) {
			log.error("【voucher】[countVoucherOfUser]Add Excetion :{}",e);
			return getResultJSONStr(false, null, Constants.SYS_ERROR);
		}
	}

	/**
	 *
	 * */
	@RequestMapping(value = "/maxVoucherOfUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String maxVoucherOfUser(){
		try {
			List<WeekBest> weekBest = voucherService.maxVoucherOfUser();
			return getResultJSONStr(true, weekBest, null);

		} catch(BusinessException e){
			log.error("【voucher】[maxVoucherOfUser]Add BusinessException :{}",e);
			return getResultJSONStr(false, null, Constants.SERVER_RESEST_EXCEPTION);
		} catch (Exception e) {
			log.error("【voucher】[maxVoucherOfUser]Add Excetion :{}",e);
			return getResultJSONStr(false, null, Constants.SYS_ERROR);
		}
	}

	/**
	 * 后台交收单统计报表信息
	 * */
	@RequestMapping(value = "/staticVoucherInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String staticVoucherInfo(@RequestBody String json){

		try {
			VoucherDTO voucherDTO = getFormJSON(json, VoucherDTO.class);
			PageRequest pageRequest = genPageRequest(voucherDTO);
			return getResultJSONStr(true, voucherService.staticVoucherInfo(voucherDTO,pageRequest), null);
		} catch(BusinessException e){
			log.error("staticVoucherInfo BusinessException :{}",e);
			return getResultJSONStr(false, null, Constants.SERVER_RESEST_EXCEPTION);
		} catch (Exception e) {
			log.error("staticVoucherInfo Excetion :{}",e);
			return getResultJSONStr(false, null, Constants.SYS_ERROR);
		}
	}


}
