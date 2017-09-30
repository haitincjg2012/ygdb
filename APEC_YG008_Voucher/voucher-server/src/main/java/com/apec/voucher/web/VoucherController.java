package com.apec.voucher.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
import com.apec.framework.common.util.JsonUtil;
import com.apec.framework.dto.ImageUploadVO;
import com.apec.framework.dto.UserInfoVO;
import com.apec.framework.log.InjectLogger;
import com.apec.voucher.dto.VoucherDTO;
import com.apec.voucher.service.VoucherService;
import com.apec.voucher.viewvo.DBNumberRankViewVO;
import com.apec.voucher.viewvo.DBVoucherViewVO;
import com.apec.voucher.viewvo.VoucherBSViewVO;
import com.apec.voucher.viewvo.VoucherRespViewVO;
import com.apec.voucher.viewvo.VoucherViewVO;
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

	/**
	 * 添加上传凭证信息
	 * @param
	 * @return ResultData<String>
	 * */
	@RequestMapping(value = "/addVoucherInfo", method = RequestMethod.POST)
	public ResultData<String> addVoucherInfo(@RequestBody String json){
		
		try {
			json = URLDecoder.decode(json, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			log.error("Add Excetion :{}",e1);
			return getResultData(false, null, Constants.SYS_ERROR);
		}
		//获取上传凭据信息
		PageJSON<String> pageJson = super.getPageJSON(json, String.class);
		VoucherVO voucherVO = JsonUtil.parseObject(pageJson.getFormJSON(), VoucherVO.class);
		if (voucherVO == null){
			log.warn("voucherVO is null,voucherVO:{}",voucherVO);
			return getResultData(false, null, Constants.ERROR_100003);
		}
		if (StringUtils.isBlank(voucherVO.getCityId()) || StringUtils.isBlank(voucherVO.getCountyId()) || StringUtils.isBlank(voucherVO.getTownId()) 
				|| voucherVO.getType() == null || StringUtils.isBlank(voucherVO.getName()) || voucherVO.getDeliveryTime() == null ){
			log.warn("param is null,[cityId:{},countyId:{},townId:{},userType:{},name:{},deliveryTime:{}]",voucherVO.getCityId()
					,voucherVO.getCountyId(),voucherVO.getTownId(),voucherVO.getType(),voucherVO.getName(),voucherVO.getDeliveryTime());
			return getResultData(false, null, Constants.COMMON_MISSING_PARAMS);
		}
		if (voucherVO.getDeliveryTime().getTime()>new Date().getTime()){
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
		
		//获取用户信息
		UserInfoVO userInfoVO = getUserInfo(pageJson);
		if (userInfoVO.getUserId() == null || StringUtils.isBlank(userInfoVO.getMobile())){
			log.warn("userId is null,[userId:{},mobile:{}]",userInfoVO.getUserId(),userInfoVO.getMobile());
			return getResultData(false, null, Constants.ERROR_100003);
		}
		voucherVO.setUserId(userInfoVO.getUserId());
		voucherVO.setMobile(userInfoVO.getMobile());
		
		//获取用户上传图片路径
		JSONArray imageListJsonArray = (JSONArray)pageJson.getRequestAttrMap().get("imageItems");
		if (CollectionUtils.isNotEmpty(imageListJsonArray)){
			List<ImageUploadVO> imageUploadVOS = JsonUtil.parseArray(imageListJsonArray.get(0).toString(), ImageUploadVO.class);
			if (CollectionUtils.isNotEmpty(imageUploadVOS)){
				StringBuilder sb = new StringBuilder();
				String voucherUrl = sb.append(imageUploadVOS.get(0).getImagePath()).toString();
				voucherVO.setVoucherUrl(voucherUrl);
			}
		}
		
		ResultData<String> resultData = new ResultData<String>();
		try {
			String resultCode = voucherService.addVoucherInfo(voucherVO);
			if (StringUtils.equals(Constants.RETURN_SUCESS, resultCode) || StringUtils.equals(Constants.VOUCHER_NOT_EXCEED, resultCode)){
				resultData.setSucceed(true);
			}else{
				setErrorResultDate(resultData, resultCode);
			}
		} catch(BusinessException e){
			log.error("Add BusinessException :{}",e);
			setErrorResultDate(resultData, Constants.SERVER_RESEST_EXCEPTION);;
		} catch (Exception e) {
			log.error("Add Excetion :{}",e);
			setErrorResultDate(resultData, Constants.SYS_ERROR);
		}
		return resultData;
	}
	
	/**
	 * 查询上传凭据信息
	 * @param json
	 * @return ResultData<PageDTO<VoucherVO>>
	 * */
	@RequestMapping(value = "/getVoucherInfo", method = RequestMethod.POST)
	public ResultData<VoucherRespViewVO> getVoucherInfo(@RequestBody String json){
		
	    try {
	    	//获取用户id
	    	VoucherDTO voucherDTO = getFormJSON(json, VoucherDTO.class);
	    	Long userId = getUserId(json);
	    	if (userId == null || userId == 0L){
		    	return getResultData(false, null, Constants.ERROR_100003);
		    }
	    	voucherDTO.setUserId(userId);
	    	
	    	//获取分页信息
	    	PageRequest pageRequest = genPageRequest(voucherDTO);
	    		    	
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
	 * @param json
	 * @return ResultData<PageDTO<VoucherBSViewVO>>
	 * */
	@RequestMapping(value = "/getVoucherBSViewVO", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<PageDTO<VoucherBSViewVO>> listVoucherBSViewVOGroubById(@RequestBody String json){
		
		try {
			VoucherDTO voucherDTO = getFormJSON(json, VoucherDTO.class);
			PageDTO<VoucherBSViewVO> voucherBSViewVOList = voucherService.listVoucherBSViewVOGroubById(voucherDTO);
			return getResultData(true, voucherBSViewVOList, "");
		} catch(BusinessException e){
			log.error("Add BusinessException :{}",e);
			return getResultData(false, null, Constants.SERVER_RESEST_EXCEPTION);
		} catch (Exception e) {
			log.error("Add Excetion :{}",e);
			return getResultData(false, null, Constants.SYS_ERROR);
	    }
	}
	
	/**
	 * 交收单详情
	 * @param json
	 * @return ResultData<VoucherViewVO>
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
	 * @param json
	 * @return ResultData<String>
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
	 * @param json
	 * @return ResultData<String>
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
			log.error("Add BusinessException :{}",e);
			return getResultData(false, null, Constants.SERVER_RESEST_EXCEPTION);
		} catch (Exception e) {
			log.error("Add Excetion :{}",e);
			return getResultData(false, null, Constants.SYS_ERROR);
		}
	}
	
	/**
	 * 更新交收单数据
	 * @param json
	 * @returnResultData<String>
	 * */
	@RequestMapping(value = "/updateVoucherInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<String> updateVoucherInfo(@RequestBody String json){
		
		PageJSON<String> pageJson = super.getPageJSON(json, String.class);
		VoucherVO voucherVO = JsonUtil.parseObject(pageJson.getFormJSON(), VoucherVO.class);
		if (voucherVO == null){
			log.warn("voucherVO is null,voucherVO:{}",voucherVO);
			return getResultData(false, null, Constants.ERROR_100003);
		}
		if (StringUtils.isBlank(voucherVO.getCityId()) || StringUtils.isBlank(voucherVO.getCountyId()) || StringUtils.isBlank(voucherVO.getTownId()) 
				|| voucherVO.getType() == null || StringUtils.isBlank(voucherVO.getName()) || voucherVO.getDeliveryTime() == null ){
			log.warn("param is null,[cityId:{},countyId:{},townId:{},userType:{},name:{},deliveryTime:{}]",voucherVO.getCityId()
					,voucherVO.getCountyId(),voucherVO.getTownId(),voucherVO.getType(),voucherVO.getName(),voucherVO.getDeliveryTime());
			return getResultData(false, null, Constants.COMMON_MISSING_PARAMS);
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
		ResultData<String> resultData = new ResultData<String>();
		try {
			String resultCode = voucherService.updateVoucherInfo(voucherVO);
			if (StringUtils.equals(Constants.RETURN_SUCESS, resultCode)){
				resultData.setSucceed(true);
			}else{
				setErrorResultDate(resultData, resultCode);
			}
		} catch(BusinessException e){
			log.error("Add BusinessException :{}",e);
			setErrorResultDate(resultData, Constants.SERVER_RESEST_EXCEPTION);;
		} catch (Exception e) {
			log.error("Add Excetion :{}",e);
			setErrorResultDate(resultData, Constants.SYS_ERROR);
		}
		return resultData;
	}
	
	/**
	 * 代办调货分析信息
	 * @param json
	 * @return ResultData<DBVoucherViewVO>
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
	 * @param json
	 * @return ResultData<PageDTO<DBNumberRankViewVO>>
	 * */
	@RequestMapping(value = "/getNumberRankViewVO", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResultData<DBNumberRankViewVO> getNumberRankViewVO(@RequestBody String json){
		
		try {
			PageJSON<String> pageJson = super.getPageJSON(json, String.class);
			VoucherDTO voucherDTO = JsonUtil.parseObject(pageJson.getFormJSON(), VoucherDTO.class);
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
	 * @param json
	 * @return ResultData<PageDTO<DBNumberRankViewVO>>
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
	 * @param json
	 * @return ResultData<PageDTO<DBNumberRankViewVO>>
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
	 * @param json
	 * @return ResultData<PageDTO<DBNumberRankViewVO>>
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


}
