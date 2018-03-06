package com.apec.voucher.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.apec.framework.common.*;
import com.apec.framework.common.enums.Realm;
import com.apec.framework.common.enumtype.*;
import com.apec.framework.dto.UserInfoVO;
import com.apec.framework.rockmq.vo.MessageBodyVO;
import com.apec.framework.rockmq.vo.MessageVO;
import com.apec.voucher.viewvo.*;
import com.apec.voucher.vo.StaticVoucherInfoVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.apec.framework.cache.CacheHashService;
import com.apec.framework.cache.CacheService;
import com.apec.framework.common.enums.MqTag;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.common.util.DateTimeUtils;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.rockmq.client.MqProducerClient;
import com.apec.framework.rockmq.vo.UserPointRecordVO;
import com.apec.framework.springcloud.SpringCloudClient;
import com.apec.voucher.dao.VoucherBSDAO;
import com.apec.voucher.dao.VoucherDAO;
import com.apec.voucher.dao.VoucherGoodsDAO;
import com.apec.voucher.dto.VoucherDTO;
import com.apec.voucher.model.QVoucher;
import com.apec.voucher.model.Voucher;
import com.apec.voucher.model.VoucherGoods;
import com.apec.voucher.service.VoucherService;
import com.apec.voucher.util.SnowFlakeKeyGen;
import com.apec.voucher.vo.VoucherGoodsVO;
import com.apec.voucher.vo.VoucherVO;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * 上传凭证服务实现类
 * @author gunj
 * create by 2017-07-14
 * */
@Service
public class VoucherServiceImpl implements VoucherService{
	
	@InjectLogger
	private Logger log;
	
	@Autowired
	private VoucherDAO voucherDAO;
	
	@Autowired
	private VoucherGoodsDAO voucherGoodsDAO;
	
	@Autowired
	private MqProducerClient mqProducerClient;
	
	@Autowired
	private SnowFlakeKeyGen idGen;
	
	@Autowired
	private CacheService cacheService;

	@Autowired
	private SpringCloudClient springCloudClient;
	
	@Value("${systemConfig_url}")
	private String systemConfigUrl;
	
	@Autowired
	private VoucherBSDAO voucherBSDAO;
	
	@Autowired
	private CacheHashService cacheHashService;

	private UserInfoVO getUserInfo(Long userId){
		String userInfoJson = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.HASH_OBJCONTENT_CACHE);
		UserInfoVO userInfo ;
		if(StringUtils.isBlank(userInfoJson)){
			//获取不到数据,记录日志
			log.warn("[VoucherServiceImpl][getUserInfo]Can't find user hash cache. userNo:{}",userId);
			userInfo = new UserInfoVO();
		}else{
			userInfo = BaseJsonUtil.parseObject(userInfoJson,UserInfoVO.class);
		}
		return userInfo;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String addVoucherInfo(VoucherVO voucherVO) {
		//添加上传凭据
		Voucher voucher = new Voucher();
		if(voucherVO.getIsSystem() == null || !voucherVO.getIsSystem()){
			//校验上传次数是否超过10次
			Integer uploadTime = 10;
			if (cacheService.exists(Constants.VOUCHER+voucherVO.getUserId())){
				String times = cacheService.get(Constants.VOUCHER+voucherVO.getUserId());
				if (Integer.parseInt(times) >= uploadTime){
					return ErrorCodeConst.VOUCHER_EXCEED_TIMES;
				}
			}
			voucher.setAuditState(Constants.ZERO);
		}else{
			//系统上传默认审核通过
			voucher.setAuditState(Constants.ISSUCCESS);
			voucher.setPassDate(new Date());
		}

		BeanUtil.copyPropertiesIgnoreNullFilds(voucherVO, voucher);
		voucher.setId(idGen.nextId());
		UserInfoVO userInfoVO = getUserInfo(voucherVO.getUserId());
		if(userInfoVO != null){
			voucher.setMobile(userInfoVO.getMobile());
		}
		//添加上传凭据商品信息
		List<VoucherGoods> voucherGoodsList = new LinkedList<>();
		Iterator<VoucherGoodsVO> iter = voucherVO.getVoucherGoodsVO().iterator();
		Double voucherNumber = 0.0;
		while (iter.hasNext()){
			VoucherGoods voucherGoods = new VoucherGoods();
			BeanUtil.copyPropertiesIgnoreNullFilds(iter.next(), voucherGoods);
			voucherGoods.setId(idGen.nextId());
			voucherGoods.setVoucher(voucher);			
			voucherGoodsList.add(voucherGoods);
			voucherNumber = voucherNumber + voucherGoods.getNumber();
		}
		
		//校验上传数量是否超过50吨
		if (voucherNumber > 100000 && (voucherVO.getIsSystem() == null || !voucherVO.getIsSystem())){
			return ErrorCodeConst.VOUCHER_EXCEED_NUMBER;
		}
		String townId = "";
		if(StringUtils.isNotBlank(voucher.getTownId())){
			townId = voucher.getTownId();
		}else if(StringUtils.isNotBlank(voucher.getCountyId())){
			townId = voucher.getCountyId();
		}else if(StringUtils.isNotBlank(voucher.getCityId())){
			townId = voucher.getCityId();
		}
		if(StringUtils.isNotBlank(townId)){
			//获取城市名称
			String[] strs = getName(townId);
			if(strs.length > 1){
				voucher.setCityName(strs[1]);
			}
			if(strs.length > 2){
				voucher.setCountyName(strs[2]);
			}
			if(strs.length > 3){
				voucher.setTownName(strs[3]);
			}
		}
		//查询上传数量
//		Double totalNumber = voucherGoodsDAO.findTotalNumberByUserId(voucherVO.getUserId());
		
		//保存上传信息
		voucherDAO.save(voucher);	
		voucherGoodsDAO.save(voucherGoodsList);	
		
		//增加上传次数
		cacheService.increment(Constants.VOUCHER+voucherVO.getUserId(), 1L);
		//设置在零点过期
        Date oneDayAfterDate = DateTimeUtils.getStartPreDay(-1);
        int differTime = DateTimeUtils.getDifferTime(oneDayAfterDate,new Date(),DateTimeUtils.MINUTE);
        cacheService.expire(Constants.VOUCHER+voucherVO.getUserId(), differTime);
		
//		//上传凭据累加积分
//		Double number = 200000.00;
//		totalNumber = totalNumber == null?0.0:totalNumber;
////
//		cacheHashService.hset(RedisHashConstants.HASH_USER_PREFIX+voucherVO.getUserId(),RedisHashConstants.HASH_VOUCHER_NUM,String.valueOf(totalNumber+voucherNumber));
//		Double remainder = totalNumber % number;
//		if ((remainder+voucherNumber)<number){
//			return Constants.VOUCHER_NOT_EXCEED;
//		}
//
//		UserPointRecordVO userPointRecordVO = new UserPointRecordVO();
//		userPointRecordVO.setId(idGen.nextId());
//		//用户获取到的积分
//		userPointRecordVO.setPointsChanged(50);
//		userPointRecordVO.setPointsChangedType(PointsChangedType.PLUS);
//		userPointRecordVO.setPointRuleType(PointRuleType.MEACH_SEND_ORDER);
//		List<Long> userIdList = new LinkedList<>();
//		userIdList.add(voucherVO.getUserId());
//		userPointRecordVO.setUserIds(userIdList);
//		mqProducerClient.sendConcurrently(MqTag.USER_POINT_TAG_SUBSCRI_POINT.getKey(), String.valueOf(userPointRecordVO.getId()), userPointRecordVO);
//
		return Constants.RETURN_SUCESS;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String voucherReview(VoucherVO voucherVO, String userId) {
		Voucher voucher = voucherDAO.findOne(voucherVO.getId());
		if(voucher == null){
			return Constants.ENABLE_NOT_NULL;
		}
		if(StringUtils.equals(voucher.getAuditState(),Constants.ISSUCCESS) || StringUtils.equals(voucher.getAuditState(),Constants.ISNOTSUCCESS)){
			return ErrorCodeConst.CANOT_DUMBLE_ENTRUE;
		}
		voucher.setAuditState(voucherVO.getAuditState());
		voucher.setPassDate(new Date());
		voucher.setLastUpdateBy(userId);
		voucher.setLastUpdateDate(new Date());
		voucherDAO.saveAndFlush(voucher);
		sendMessageMqInfo(voucher.getUserId(),voucher);
		if(StringUtils.equals(Constants.ISSUCCESS,voucher.getAuditState())){
			Double number = 200000.00;
			List<Long> list = new ArrayList<>();
			list.add(voucher.getId());
			//审核通过给用户加上相应的积分,并记录总数
			Double voucherNumber = voucherGoodsDAO.findNumberByVoucherId(list);
			//查询上传数量
			Double totalNumber = voucherGoodsDAO.findEffectiveTotalNumberByUserId(voucher.getUserId());
			cacheHashService.hset(RedisHashConstants.HASH_USER_PREFIX+voucher.getUserId(),RedisHashConstants.HASH_VOUCHER_NUM,String.valueOf(totalNumber));
			Double remainder = (totalNumber-voucherNumber) % number;
			if ((remainder+voucherNumber)<number){
				return Constants.RETURN_SUCESS;
			}
			UserPointRecordVO userPointRecordVO = new UserPointRecordVO();
			userPointRecordVO.setId(idGen.nextId());
			//用户获取到的积分
			userPointRecordVO.setPointsChanged(50);
			userPointRecordVO.setPointsChangedType(PointsChangedType.PLUS);
			userPointRecordVO.setPointRuleType(PointRuleType.MEACH_SEND_ORDER);
			List<Long> userIdList = new LinkedList<>();
			userIdList.add(voucher.getUserId());
			userPointRecordVO.setUserIds(userIdList);
			mqProducerClient.sendConcurrently(MqTag.USER_POINT_TAG_SUBSCRI_POINT.getKey(), String.valueOf(userPointRecordVO.getId()), userPointRecordVO);
		}
		return Constants.RETURN_SUCESS;
	}

	/**
	 * 发送 通知文章审核结果的站内信
	 */
	private void sendMessageMqInfo(Long userId,Voucher voucher){
		MessageBodyVO messageBodyVO = new MessageBodyVO();
		//系统通知
		messageBodyVO.setType(MessageType.NOTIFICATION);
		//使用动态模板发送
		messageBodyVO.setTemplateFlag(true);
		Map<String, String> contentMap = new HashMap<>(16);
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		contentMap.put("createDate",sdf.format(voucher.getCreateDate()));
		//转换审核时间格式
		contentMap.put("passDate",sdf.format(voucher.getPassDate()));
		contentMap.put("result",StringUtils.equals(voucher.getAuditState(),Constants.ISSUCCESS)?"通过":"驳回");
		messageBodyVO.setRealm(Realm.VOCHER);
		messageBodyVO.setContentMap(contentMap);
		messageBodyVO.setTemplateKey(MessageTemplate.RESULT_OF_VOUCHER);
		//发送站内信，通知用户积分变动消息
		MessageVO messageVO = new MessageVO();
		messageVO.setBody(messageBodyVO);
		messageVO.setId(idGen.nextId());
		List<Long> receivers = new ArrayList<>();
		receivers.add(userId);
		//接收者
		messageVO.setReceivers(receivers);
		//用户状态
		messageVO.setMessageStatus(MessageStatus.NEW);
		mqProducerClient.sendConcurrently(MqTag.MESSAGE_TAG.getKey(),String.valueOf(messageVO.getId()),messageVO);
	}

	/**
	 * 获取城市、县、镇名称
	 * */
	private String[] getName(String townId){
		JSONObject param = new JSONObject();
		param.put("code", townId);
		String json = springCloudClient.post(systemConfigUrl, param.toString());
		ResultData<String> resultData = BaseJsonUtil.parseObject(json, new TypeReference<ResultData<String>>(){});
		String data = resultData.getData();
		return data.split("\\|");
	}
	
	@Override
	@Transactional(readOnly = true,rollbackFor = Exception.class)
	public VoucherRespViewVO listVoucherInfo(VoucherDTO voucherDTO, PageRequest pageRequest) {
		
		VoucherRespViewVO voucherRespVVO = new VoucherRespViewVO();
	    //分页查询voucher
		Page<Voucher> voucherList = voucherDAO.findAll(getConditionQuery(voucherDTO), pageRequest);
		PageDTO<VoucherViewVO> response = new PageDTO<>();
		//上传单据Voucher->VoucherViewVO
		Iterator<Voucher> iter = voucherList.iterator();
		List<VoucherViewVO> voucherVVOList = new LinkedList<>();
		while(iter.hasNext()){
			VoucherViewVO voucherVVO = new VoucherViewVO();
			Voucher voucher = iter.next();
			BeanUtil.copyPropertiesIgnoreNullFilds(voucher, voucherVVO);
			voucherVVO.setVoucherId(voucher.getId());
			//商品信息VoucherGoods->VoucherGoodsViewVO
			List<VoucherGoodsViewVO> voucherGoodsVVOList = new LinkedList<>();
			Iterable<VoucherGoods> voucherGoodss = voucherGoodsDAO.listByVoucherId(voucher.getId());
			voucherGoodss.forEach(voucherGoods -> {
				VoucherGoodsViewVO voucherGoodsVVO = new VoucherGoodsViewVO();
				BeanUtil.copyPropertiesIgnoreNullFilds(voucherGoods, voucherGoodsVVO);
				voucherGoodsVVOList.add(voucherGoodsVVO);
			});
			voucherVVO.setVoucherGoodsVVO(voucherGoodsVVOList);
			voucherVVO.setAuditState(getAuditState(voucher.getAuditState()));
			voucherVVOList.add(voucherVVO);
		}
		response.setNumber(voucherList.getNumber()+1);
		response.setRows(voucherVVOList);
		response.setTotalElements(voucherList.getTotalElements());
		response.setTotalPages(voucherList.getTotalPages());
		
		//获取用户上传总数量
		Double totalNumber = voucherGoodsDAO.findTotalNumberByUserId(voucherDTO.getUserId());
		voucherRespVVO.setTotalNumber(totalNumber);
		voucherRespVVO.setVoucherVVOs(response);
		return voucherRespVVO;
	}
	
	/**
	 * 多条件查询
	 * */
	private Predicate getConditionQuery(VoucherDTO voucherDTO){
		List<BooleanExpression> predicates = new ArrayList<>();
		if (voucherDTO != null){
			if (voucherDTO.getDeliveryTime() != null){
				predicates.add(QVoucher.voucher.deliveryTime.eq(voucherDTO.getDeliveryTime()));
			}
			if (StringUtils.isNotEmpty(voucherDTO.getShipWarehouse())){
			    predicates.add(QVoucher.voucher.shipWarehouse.eq(voucherDTO.getShipWarehouse()));
			}
			if (StringUtils.isNotEmpty(voucherDTO.getName())){
			    predicates.add(QVoucher.voucher.name.eq(voucherDTO.getName()));
			}
			predicates.add(QVoucher.voucher.userId.eq(voucherDTO.getUserId()));
		}

		predicates.add(QVoucher.voucher.enableFlag.eq(EnableFlag.Y));
		return BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
	}

	@Override
	public VoucherViewVO findVoucherInfoById(Long voucherId) {
		Voucher voucher = voucherDAO.findVoucherInfo(voucherId);
		if (voucher == null){
			return null;
		}
		VoucherViewVO voucherViewVO = new VoucherViewVO();
		BeanUtil.copyProperties(voucher, voucherViewVO);
		voucherViewVO.setAddress(voucher.getCityName() + voucher.getCountyName() + voucher.getTownName());
		voucherViewVO.setVoucherId(voucher.getId());
		voucherViewVO.setAuditState(getAuditState(voucher.getAuditState()));
		UserInfoVO userInfoVO = getUserInfo(voucher.getUserId());
		if(userInfoVO != null){
			voucherViewVO.setUserName(userInfoVO.getName());
			voucherViewVO.setMobile(userInfoVO.getMobile());
			voucherViewVO.setUserType(userInfoVO.getUserType());
		}
		List<VoucherGoodsViewVO> voucherGoodsViewVOList = new LinkedList<>();
		Iterable<VoucherGoods> voucherGoodss = voucherGoodsDAO.listByVoucherId(voucher.getId());
		voucherGoodss.forEach(voucherGoods -> {
				VoucherGoodsViewVO voucherGoodsVVO = new VoucherGoodsViewVO();
				BeanUtil.copyPropertiesIgnoreNullFilds(voucherGoods, voucherGoodsVVO);
				voucherGoodsViewVOList.add(voucherGoodsVVO);
		});
		voucherViewVO.setVoucherGoodsVVO(voucherGoodsViewVOList);
		return voucherViewVO;
	}

	@Override
	public PageDTO<VoucherBSViewVO> listVoucherBSViewVOGroubById(VoucherDTO voucherDTO,List<Long> userIds,PageRequest pageRequest) {
		Page<Voucher> page = voucherDAO.findAll(getInputCondition(voucherDTO,userIds),pageRequest);
		PageDTO<VoucherBSViewVO> result = new PageDTO<>();
		List<VoucherBSViewVO> list = new LinkedList<>();
		page.forEach(voucher -> {
			VoucherBSViewVO voucherBSViewVO = new VoucherBSViewVO();
			BeanUtil.copyPropertiesIgnoreNullFilds(voucher,voucherBSViewVO);
			voucherBSViewVO.setVoucherId(voucher.getId());
			voucherBSViewVO.setAuditState(getAuditState(voucher.getAuditState()));
			List<Long> listId = new ArrayList<>();
			listId.add(voucher.getId());
			Double voucherNumber = voucherGoodsDAO.findNumberByVoucherId(listId);
			voucherBSViewVO.setTotalNumber(voucherNumber);
			Double voucherAmount = voucherGoodsDAO.findAmountByVoucherId(listId);
			voucherBSViewVO.setTotalAmount(voucherAmount);
			UserInfoVO userInfoVO = getUserInfo(voucher.getUserId());
			if(userInfoVO != null){
				voucherBSViewVO.setUserName(userInfoVO.getName());
				voucherBSViewVO.setMobile(userInfoVO.getMobile());
			}
			list.add(voucherBSViewVO);
		});
		result.setNumber(page.getNumber() + 1);
		result.setRows(list);
		result.setTotalElements(page.getTotalElements());
		result.setTotalPages(page.getTotalPages());
		return result;
	}

	@Override
	public VoucherBSViewVO getVoucherBSSumWeight(VoucherDTO voucherDTO,List<Long> userIds) {
		VoucherBSViewVO voucherBSViewVO = new VoucherBSViewVO();
		voucherBSViewVO.setTotalNumber(0.0);
		voucherBSViewVO.setTotalAmount(0.0);
		Iterable<Voucher> vouchers = voucherDAO.findAll(getInputCondition(voucherDTO,userIds));
		List<Long> voucherIds = new ArrayList<>();
		vouchers.forEach(voucher -> {
			voucherIds.add(voucher.getId());
		});
		if(!CollectionUtils.isEmpty(voucherIds)){
			Double voucherNumber = voucherGoodsDAO.findNumberByVoucherId(voucherIds);
			voucherBSViewVO.setTotalNumber(voucherNumber);
			Double voucherAmount = voucherGoodsDAO.findAmountByVoucherId(voucherIds);
			voucherBSViewVO.setTotalAmount(voucherAmount);
		}
		return voucherBSViewVO;
	}

	@Override
	public PageDTO<StaticVoucherInfoVO> staticVoucherInfo(VoucherDTO voucherDTO, PageRequest pageRequest) {
		PageRequest pageRequest1 = new PageRequest(pageRequest.getPageNumber(),pageRequest.getPageSize());
		Page<Object[]> page = voucherDAO.staticVoucherForMonthAndUserId(voucherDTO.getUserName(),StringUtils.isNotBlank(voucherDTO.getUserName()),null,false,voucherDTO.getStartDate(),
				voucherDTO.getStartDate() != null,voucherDTO.getEndDate() ,voucherDTO.getEndDate() != null, pageRequest1);
		PageDTO<StaticVoucherInfoVO> result = new PageDTO<>();
		List<StaticVoucherInfoVO> list = new ArrayList<>();
		page.forEach(objects -> {
			StaticVoucherInfoVO staticVoucherInfoVO = new StaticVoucherInfoVO();
			staticVoucherInfoVO.setSumCount(String.valueOf(objects[0]));
			staticVoucherInfoVO.setCreateDate(String.valueOf(objects[1]));
			staticVoucherInfoVO.setUserId(String.valueOf(objects[2]));
			staticVoucherInfoVO.setTotalNumber(String.valueOf(objects[3]));
			staticVoucherInfoVO.setTotalAmount(String.valueOf(objects[4]));
			staticVoucherInfoVO.setUserName(String.valueOf(objects[5]));
			staticVoucherInfoVO.setUserType(String.valueOf(objects[6]));
			staticVoucherInfoVO.setMobile(String.valueOf(objects[7]));
			list.add(staticVoucherInfoVO);
		});
		result.setNumber(page.getNumber() + 1);
		result.setRows(list);
		result.setTotalElements(page.getTotalElements());
		result.setTotalPages(page.getTotalPages());
		return result;
	}

	/**
	 * 根据多种情况查询用户信息
	 * 包括like：name， eq：mobile，auditState
	 * @param  vo vo
	 * @return  Predicate
	 */
	private Predicate getInputCondition(VoucherDTO vo,List<Long> userIds)
	{
		List<BooleanExpression> predicates = new ArrayList<>();
		if(null != vo)
		{
			if (StringUtils.isNotBlank(vo.getName())) {
				predicates.add(QVoucher.voucher.name.like("%" + vo.getName() + "%"));
			}
			if (StringUtils.isNotBlank(vo.getAuditState())) {
				predicates.add(QVoucher.voucher.auditState.eq(vo.getAuditState()));
			}
			if (StringUtils.isNotBlank(vo.getMobile())) {
				predicates.add(QVoucher.voucher.mobile.like("%" + vo.getMobile() + "%"));
			}
			if(vo.getStartDate() != null){
				predicates.add(QVoucher.voucher.createDate.goe(vo.getStartDate()));
			}
			if(vo.getEndDate() != null){
				predicates.add(QVoucher.voucher.createDate.loe(vo.getEndDate()));
			}
		}
		if(!org.springframework.util.CollectionUtils.isEmpty(userIds)){
			predicates.add(QVoucher.voucher.userId.in(userIds));
		}
		predicates.add(QVoucher.voucher.enableFlag.eq(EnableFlag.Y));
		return BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
	}

	private String getAuditState(String auditStateCode){
		String auditState = "未审核";
		if(StringUtils.equals(auditStateCode,Constants.ISSUCCESS)){
			auditState = "审核通过";
		}if(StringUtils.equals(auditStateCode,Constants.ISNOTSUCCESS)){
			auditState = "审核驳回";
		}
		return auditState;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteVoucherInfo(VoucherDTO voucherDTO) {
		Voucher voucher = voucherDAO.findOne(voucherDTO.getVoucherId());
		int updateNumber = voucherDAO.deleteVoucher(voucherDTO.getVoucherId());

		if(!StringUtils.equals(Constants.ISSUCCESS,voucher.getAuditState())){
			return updateNumber;
		}
		List<Long> listId = new ArrayList<>();
		listId.add(voucher.getId());
		//查询这条单据更改之前上传总数
		Double lastNumber = voucherGoodsDAO.findNumberByVoucherId(listId);
		//查询用户上传数量
		Double totalNumber = voucherGoodsDAO.findEffectiveTotalNumberByUserId(voucher.getUserId());
		
		//缓存用户上传数量
		cacheHashService.hset(RedisHashConstants.HASH_USER_PREFIX+voucher.getUserId(), RedisHashConstants.HASH_VOUCHER_NUM, String.valueOf(totalNumber-lastNumber));

		//如果余数小于单据总数减50积分
		Double number = 200000.00;
		Double remainder = (totalNumber + lastNumber) % number;
		if (remainder<lastNumber){
			UserPointRecordVO userPointRecordVO = new UserPointRecordVO();
			userPointRecordVO.setId(idGen.nextId());
			//用户获取到的积分
			userPointRecordVO.setPointsChanged(50);
			userPointRecordVO.setPointsChangedType(PointsChangedType.REDUCTION);
			userPointRecordVO.setPointRuleType(PointRuleType.REDUCE_VOCHER);
			List<Long> userIdList = new LinkedList<>();
			userIdList.add(voucherDTO.getUserId());
			userPointRecordVO.setUserIds(userIdList);
			userPointRecordVO.setRemark(userPointRecordVO.getPointRuleType().getKey());
			mqProducerClient.sendConcurrently(MqTag.USER_POINT_TAG_SUBSCRI_POINT.getKey(), String.valueOf(userPointRecordVO.getId()), userPointRecordVO);
		}
		
		return updateNumber;
	}

	@Override
	public DBVoucherViewVO listDBVoucherInfo(VoucherDTO voucherDTO) {
		
		//获取数量排行信息
		PageDTO<Object[]> pageDTO = voucherBSDAO.listDBVoucherInfo(voucherDTO);
		PageDTO<DBNumberRankViewVO> dbNumberRankViewVOPage = new PageDTO<>();
		List<Object[]> objs = pageDTO.getRows();
		List<DBNumberRankViewVO> dbNumberRankViewVOList = new LinkedList<>();
		for (Object[] obj : objs){
			DBNumberRankViewVO dbNumberRankViewVO = new DBNumberRankViewVO();
			dbNumberRankViewVO.setName((String)obj[0]);
			dbNumberRankViewVO.setType(UserType.valueOf(UserType.class, String.valueOf(obj[1])));
			dbNumberRankViewVO.setTotalNumber((double)obj[2]);
			dbNumberRankViewVOList.add(dbNumberRankViewVO);
		}
		dbNumberRankViewVOPage.setNumber(pageDTO.getNumber());
		dbNumberRankViewVOPage.setRows(dbNumberRankViewVOList);
		dbNumberRankViewVOPage.setTotalElements(pageDTO.getTotalElements());
		dbNumberRankViewVOPage.setTotalPages(pageDTO.getTotalPages());
		
		//获取数量规格占比信息
		Map<String, String> proportionMap = getDBPropertionInfo(voucherDTO);
		
		DBVoucherViewVO dbVoucherViewVO = new DBVoucherViewVO();
		dbVoucherViewVO.setDbNumberRankViewVOPage(dbNumberRankViewVOPage);
		dbVoucherViewVO.setProportionMap(proportionMap);
		return dbVoucherViewVO;
	}

	/**
	 * 查询代办数量规格占比信息
	 * */
	private Map<String, String> getDBPropertionInfo(VoucherDTO voucherDTO) {
		
		List<Object[]> objs = voucherBSDAO.listPartNumber(voucherDTO);
		Double totalNumber = voucherBSDAO.findDBTotalNumber(voucherDTO);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(4);
		Map<String, String> proportionMap = new HashMap<>(16);
		for (Object[] obj : objs){
			String propertion = nf.format(((double)obj[1]/totalNumber)*100);
			proportionMap.put(String.valueOf(obj[0]), propertion+"%");
		}
		return proportionMap;
	}

	@Override
	public DBNumberRankViewVO findNumberRankViewVO(Long userId) {
		
		//平台个人排名信息
		List<Object[]> objs = voucherDAO.findDBNumberRankInfo(userId);
		DBNumberRankViewVO dbNumberRankViewVO = new DBNumberRankViewVO();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		//没上传单据，排在最后一名
		if (CollectionUtils.isEmpty(objs)){
			int rankNo = voucherDAO.findLastRankNo();
			dbNumberRankViewVO.setTotalNumber(0.0);
			dbNumberRankViewVO.setWeight("万斤");
			dbNumberRankViewVO.setRankNo(String.valueOf(rankNo + 1));
			return dbNumberRankViewVO;
		}
		String rankNo = String.valueOf(objs.get(0)[4]);
		if(StringUtils.isBlank(rankNo)){
			rankNo = "1";
		}
		if(rankNo.indexOf(".") > 0){
			rankNo = rankNo.substring(0,rankNo.indexOf("."));
		}
		dbNumberRankViewVO.setRankNo(rankNo);
		dbNumberRankViewVO.setName(String.valueOf(objs.get(0)[1]));
		dbNumberRankViewVO.setType(UserType.valueOf(UserType.class,String.valueOf(objs.get(0)[2])));
		double totalNumber = (double)objs.get(0)[3];

		double millionPounds = 10000.0;
		double voucherTotalNumber = totalNumber/millionPounds;
		dbNumberRankViewVO.setTotalNumber(voucherTotalNumber);
		dbNumberRankViewVO.setWeight("万斤");
		
		//如果是客商则不需要个人规格数量
		if (dbNumberRankViewVO.getType().equals(UserType.KS)){
			return dbNumberRankViewVO;
		}
		
		//代办个人规格数量
		List<Object[]> attrNumbers = voucherDAO.listAttrNumber(userId);
		Map<String, String> attrNumberMap = new LinkedHashMap<>();
		double partNumber = 0;
		for (Object[] attrNumber : attrNumbers){
			attrNumberMap.put(String.valueOf(attrNumber[0]), String.valueOf(nf.format((Double)attrNumber[1]/millionPounds)));
			partNumber = partNumber+Double.parseDouble(String.valueOf(attrNumber[1]));
		}
		//其他数量
		attrNumberMap.put("其它", nf.format((totalNumber-partNumber)/millionPounds));
		String json = BaseJsonUtil.toJSONString(attrNumberMap);
		dbNumberRankViewVO.setAttrNumberMap(json);
		return dbNumberRankViewVO;
	}

	@Override
	public PageDTO<DBNumberRankViewVO> listMonthDBNumberRankViewVO(VoucherDTO voucherDTO) {
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
        //获取当前日期
        voucherDTO.setStartDate(DateTimeUtils.getFisrtDayOfMonth(year,month + 1));
        voucherDTO.setEndDate(new Date());
		//获取数量排行信息
		return listDBNumberRankViewVO(voucherDTO);
	}

	public PageDTO<DBNumberRankViewVO> listDBNumberRankViewVO(VoucherDTO voucherDTO){
//获取数量排行信息
		PageDTO<Object[]> pageDTO = voucherBSDAO.listDBVoucherInfo(voucherDTO);
		PageDTO<DBNumberRankViewVO> dbNumberRankViewVOPage = new PageDTO<>();
		List<Object[]> objs = pageDTO.getRows();
		List<DBNumberRankViewVO> dbNumberRankViewVOList = new LinkedList<>();
		for (Object[] obj : objs){
			DBNumberRankViewVO dbNumberRankViewVO = new DBNumberRankViewVO();
			dbNumberRankViewVO.setName((String)obj[0]);
			dbNumberRankViewVO.setType(UserType.valueOf(UserType.class, String.valueOf(obj[1])));
			dbNumberRankViewVO.setTotalNumber((double)obj[2]);
			dbNumberRankViewVO.setImgUrl(String.valueOf(obj[3]));
			dbNumberRankViewVO.setUserId(NumberUtils.createLong(StringUtils.isBlank(String.valueOf(obj[4]))?"0":String.valueOf(obj[4])));
			dbNumberRankViewVO.setOrgId(NumberUtils.createLong(StringUtils.isBlank(String.valueOf(obj[5]))?"0":String.valueOf(obj[5])));
			dbNumberRankViewVOList.add(dbNumberRankViewVO);
		}
		dbNumberRankViewVOPage.setNumber(pageDTO.getNumber());
		dbNumberRankViewVOPage.setRows(dbNumberRankViewVOList);
		dbNumberRankViewVOPage.setTotalElements(pageDTO.getTotalElements());
		dbNumberRankViewVOPage.setTotalPages(pageDTO.getTotalPages());
		return dbNumberRankViewVOPage;
	}

	@Override
	public PageDTO<DBNumberRankViewVO> listTotalDBNumberRankViewVO(VoucherDTO voucherDTO) {
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		int year = calendar.get(Calendar.YEAR);
		//获取当前日期
		voucherDTO.setStartDate(DateTimeUtils.getCurrYearFirst(year));
		voucherDTO.setEndDate(new Date());
		//获取数量排行信息
		return listDBNumberRankViewVO(voucherDTO);
	}

	/**
	 * 更新用户缓存中的上传总数(定时任务)
	 */
	@Override
	public String countVoucherOfUser(){
		log.info("#################voucher###########time job begin ##########################");
		List<Object[]> list = voucherBSDAO.countVoucherOfUser();
		if(!CollectionUtils.isEmpty(list)){
			list.forEach(objects -> {
				Long userId = ((BigInteger)objects[0]).longValue();
				Double sumWeight = (double)objects[1];
				//缓存用户上传数量
				cacheHashService.hset(RedisHashConstants.HASH_USER_PREFIX+userId, RedisHashConstants.HASH_VOUCHER_NUM, String.valueOf(sumWeight));
			});
		}
		log.info("#####################voucher#######time job end ##########################");
		return Constants.RETURN_SUCESS;
	}

	/**
	 * 每周上榜信息
	 */
	@Override
	public List<WeekBest> maxVoucherOfUser(){
		List<WeekBest> weekBests = null;
		String str = cacheService.get(RedisConstants.WEEKBEST_VOUCHER);
		if(StringUtils.isNotBlank(str)){
			weekBests = BaseJsonUtil.parseObject(str,List.class);
			if(!CollectionUtils.isEmpty(weekBests)){
				return weekBests;
			}
		}
		if(org.springframework.util.CollectionUtils.isEmpty(weekBests)){
			weekBests = new ArrayList<>();
		}
		List<Object[]> list = voucherBSDAO.maxVoucherOfUser();
		if(!CollectionUtils.isEmpty(list) && list.size() > 0){
			for(Object[] obj:list){
				WeekBest weekBest = new WeekBest();
				Long userId = ((BigInteger)obj[0]).longValue();
				Double sumWeight = (double)obj[1];
				//保留两位小数
				long l1 = Math.round(sumWeight*100);
				sumWeight = l1/100.0;
				Integer time = ((BigInteger)obj[2]).intValue();
				UserInfoVO userInfo = getUserInfo(userId);
				weekBest.setUserId(userId);
				weekBest.setCreateDate(new Date());
				weekBest.setSumWeight(sumWeight);
				weekBest.setTime(time);
				if(userInfo != null && userInfo.getUserId() != null){
					weekBest.setImageUrl(userInfo.getImgUrl());
					weekBest.setName(userInfo.getName());
					weekBest.setUserType(userInfo.getUserType().getKey());
					weekBest.setUserOrgId(userInfo.getUserOrgId());
				}
				weekBests.add(weekBest);
			}

			int timeOut = DateTimeUtils.getDifferTime(DateTimeUtils.getNextMonday(),new Date(),DateTimeUtils.MINUTE);
			cacheService.add(RedisConstants.WEEKBEST_VOUCHER,BaseJsonUtil.toJSONString(weekBests),timeOut);
		}
		return weekBests;
	}


}

