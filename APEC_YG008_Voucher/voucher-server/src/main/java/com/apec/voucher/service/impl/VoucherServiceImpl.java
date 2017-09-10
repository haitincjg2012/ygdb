package com.apec.voucher.service.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.apec.framework.cache.CacheService;
import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.enums.MqTag;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.enumtype.PointRuleType;
import com.apec.framework.common.enumtype.PointsChangedType;
import com.apec.framework.common.enumtype.UserType;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.common.util.DateTimeUtils;
import com.apec.framework.common.util.JsonUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.rockmq.client.MQProducerClient;
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
import com.apec.voucher.viewvo.DBNumberRankViewVO;
import com.apec.voucher.viewvo.DBVoucherViewVO;
import com.apec.voucher.viewvo.VoucherBSViewVO;
import com.apec.voucher.viewvo.VoucherGoodsViewVO;
import com.apec.voucher.viewvo.VoucherRespViewVO;
import com.apec.voucher.viewvo.VoucherViewVO;
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
	private MQProducerClient mqProducerClient;
	
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
	
	@Override
	@Transactional
	public String addVoucherInfo(VoucherVO voucherVO) {
		
		//校验上传次数是否超过3次
		if (cacheService.exists(Constants.VOUCHER+voucherVO.getUserId())){
			String times = cacheService.get(Constants.VOUCHER+voucherVO.getUserId());
			if (Integer.parseInt(times) >= 3){
				return ErrorCodeConst.VOUCHER_EXCEED_TIMES;
			}
		}
		//添加上传凭据
		Voucher voucher = new Voucher();
		BeanUtil.copyPropertiesIgnoreNullFilds(voucherVO, voucher);
		voucher.setId(idGen.nextId());
		
		//添加上传凭据商品信息
		List<VoucherGoods> voucherGoodsList = new LinkedList<VoucherGoods>();
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
		
		//校验上传数量是否超过10吨
		if (voucherNumber > 20000){
			return ErrorCodeConst.VOUCHER_EXCEED_NUMBER;
		}	
		//获取城市名称
		String[] strs = getName(voucher.getTownId());
		voucher.setCityName(strs[1]);
		voucher.setCountyName(strs[2]);
		voucher.setTownName(strs[3]);
		
		//查询上传数量
		Double totalNumber = voucherGoodsDAO.findTotalNumberByUserId(voucherVO.getUserId());
		
		//保存上传信息
		voucherDAO.save(voucher);	
		voucherGoodsDAO.save(voucherGoodsList);	
		
		//增加上传次数
		cacheService.increment(Constants.VOUCHER+voucherVO.getUserId(), 1L);
		//设置在零点过期
        Date oneDayAfterDate = DateTimeUtils.getStartPreDay(-1);
        int differTime = DateTimeUtils.getDifferTime(oneDayAfterDate,new Date(),DateTimeUtils.MINUTE);
        cacheService.expire(Constants.VOUCHER+voucherVO.getUserId(), differTime);
		
		//上传凭据累加积分
		Double number = 200000.00;
		totalNumber = totalNumber == null?0.0:totalNumber;
		Double remainder = totalNumber % number;
		if ((remainder+voucherNumber)<number){
			return Constants.VOUCHER_NOT_EXCEED;
		}
		
		UserPointRecordVO userPointRecordVO = new UserPointRecordVO();
		userPointRecordVO.setId(idGen.nextId());
		userPointRecordVO.setPointsChanged(50);//用户获取到的积分
		userPointRecordVO.setPointsChangedType(PointsChangedType.PLUS);
		userPointRecordVO.setPointRuleType(PointRuleType.MEACH_SEND_ORDER);
		List<Long> userIdList = new LinkedList<>();
		userIdList.add(voucherVO.getUserId());
		userPointRecordVO.setUserIds(userIdList);
		mqProducerClient.sendConcurrently(MqTag.USER_POINT_TAG_SUBSCRI_POINT.getKey(), String.valueOf(userPointRecordVO.getId()), userPointRecordVO);
		
		return Constants.RETURN_SUCESS;
	}
	
	/**
	 * 获取城市、县、镇名称
	 * */
	private String[] getName(String townId){
		JSONObject param = new JSONObject();
		param.put("code", townId);
		String json = springCloudClient.post(systemConfigUrl, param.toString());
		ResultData<String> resultData = JsonUtil.parseObject(json, new TypeReference<ResultData<String>>(){});
		String data = resultData.getData();
		String[] strs = data.split("\\|");
		return strs;
	}
	
	@Override
	@Transactional(readOnly = true)
	public VoucherRespViewVO listVoucherInfo(VoucherDTO voucherDTO, PageRequest pageRequest) {
		
		VoucherRespViewVO voucherRespVVO = new VoucherRespViewVO();
	    //分页查询voucher
		Page<Voucher> voucherList = voucherDAO.findAll(getConditionQuery(voucherDTO), pageRequest);
		PageDTO<VoucherViewVO> response = new PageDTO<VoucherViewVO>();
		//上传单据Voucher->VoucherViewVO
		Iterator<Voucher> iter = voucherList.iterator();
		List<VoucherViewVO> voucherVVOList = new LinkedList<VoucherViewVO>();
		while(iter.hasNext()){
			VoucherViewVO voucherVVO = new VoucherViewVO();
			Voucher voucher = iter.next();
			BeanUtil.copyPropertiesIgnoreNullFilds(voucher, voucherVVO);
			//商品信息VoucherGoods->VoucherGoodsViewVO
			List<VoucherGoodsViewVO> voucherGoodsVVOList = new LinkedList<VoucherGoodsViewVO>();
			Iterator<VoucherGoods> vgIter = voucher.getVoucherGoods().iterator();
			while(vgIter.hasNext()){
				VoucherGoodsViewVO voucherGoodsVVO = new VoucherGoodsViewVO();
				VoucherGoods voucherGoods = vgIter.next();
				if (!StringUtils.equals(EnableFlag.Y.name(),voucherGoods.getEnableFlag().name())){
					continue;
				}
				BeanUtil.copyPropertiesIgnoreNullFilds(voucherGoods, voucherGoodsVVO);
				voucherGoodsVVOList.add(voucherGoodsVVO);
			}
			voucherVVO.setVoucherGoodsVVO(voucherGoodsVVOList);
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
	 * @param voucherDTO
	 * @return Predicate
	 * */
	private Predicate getConditionQuery(VoucherDTO voucherDTO){
		List<BooleanExpression> predicates = new ArrayList<BooleanExpression>();
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
		}
		predicates.add(QVoucher.voucher.userId.eq(voucherDTO.getUserId()));
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
		voucherViewVO.setVoucherId(voucher.getId());
		
		List<VoucherGoods> voucherGoodsList = voucher.getVoucherGoods();
		List<VoucherGoodsViewVO> voucherGoodsViewVOList = new LinkedList<VoucherGoodsViewVO>();
		for (VoucherGoods voucherGoods : voucherGoodsList){
			if (!StringUtils.equals(EnableFlag.Y.name(),voucherGoods.getEnableFlag().name())){
				continue;
			}
			VoucherGoodsViewVO voucherGoodsViewVO = new VoucherGoodsViewVO();
			BeanUtil.copyProperties(voucherGoods, voucherGoodsViewVO);
			voucherGoodsViewVO.setVoucherGoodsId(voucherGoods.getId());
			voucherGoodsViewVOList.add(voucherGoodsViewVO);
		}
		voucherViewVO.setVoucherGoodsVVO(voucherGoodsViewVOList);
		return voucherViewVO;
	}

	@Override
	public PageDTO<VoucherBSViewVO> listVoucherBSViewVOGroubById(VoucherDTO voucherDTO) {
		PageDTO<Object[]> pageDTO = voucherBSDAO.listVoucherBS(voucherDTO);
		PageDTO<VoucherBSViewVO> response = new PageDTO<VoucherBSViewVO>();
		List<VoucherBSViewVO> voucherBSViewVOList = new LinkedList<VoucherBSViewVO>();
		List<Object[]> list = pageDTO.getRows();
		for (Object[] obj : list){
			VoucherBSViewVO voucherBSViewVO = new VoucherBSViewVO();
			voucherBSViewVO.setCreateDate((Date)obj[0]);
			voucherBSViewVO.setTotalNumber((Double)obj[1]);
			voucherBSViewVO.setTotalAmount((BigDecimal)obj[2]);
			voucherBSViewVO.setSaleMarket(String.valueOf(obj[3]));
			voucherBSViewVO.setShipWarehouse(String.valueOf(obj[4]));
			voucherBSViewVO.setDeliveryTime((Date)obj[5]);
			voucherBSViewVO.setVoucherUrl(String.valueOf(obj[6]));
			voucherBSViewVO.setVoucherId((Long)obj[7]);
			voucherBSViewVOList.add(voucherBSViewVO);
		}
		
		response.setNumber(pageDTO.getNumber());
		response.setRows(voucherBSViewVOList);
		response.setTotalElements(pageDTO.getTotalElements());
		response.setTotalPages(pageDTO.getTotalPages());
		return response;
	}

	@Override
	@Transactional
	public int deleteVoucherInfo(Long voucherId) {
		int number = voucherDAO.deleteVoucher(voucherId);
		return number;
	}

	@Override
	@Transactional
	public String addVoucherGoodsInfo(VoucherGoodsVO voucherGoodsVO) {
		VoucherGoods voucherGoods = new VoucherGoods();
		voucherGoods.setId(idGen.nextId());
		voucherGoods.setSkuName(voucherGoodsVO.getSkuName());
		voucherGoods.setSkuId(voucherGoodsVO.getSkuId());
		voucherGoods.setNumber(voucherGoodsVO.getNumber());
		voucherGoods.setAmount(voucherGoodsVO.getAmount());
		voucherGoods.setTotalAmount(voucherGoodsVO.getTotalAmount());
		Voucher voucher = new Voucher();
		voucher.setId(voucherGoodsVO.getVoucherGoodsId());
		voucherGoods.setVoucher(voucher);
		voucherGoodsDAO.save(voucherGoods);
		return Constants.RETURN_SUCESS;
	}

	@Override
	@Transactional
	public String updateVoucherInfo(VoucherVO voucherVO) {
		
		Voucher voucher = new Voucher();
		BeanUtil.copyProperties(voucherVO, voucher);
		voucher.setId(voucherVO.getVoucherId());
		voucher.setEnableFlag(EnableFlag.Y);
		String[] strs = getName(voucher.getTownId());
		voucher.setCityName(strs[1]);
		voucher.setCountyName(strs[2]);
		voucher.setTownName(strs[3]);
		
		List<VoucherGoods> voucherGoodsList = new LinkedList<VoucherGoods>();
		List<VoucherGoodsVO> VoucherGoodsVOList = voucherVO.getVoucherGoodsVO();
		List<Long> ids = new LinkedList<Long>();
		for (VoucherGoodsVO voucherGoodsVO : VoucherGoodsVOList){
			VoucherGoods voucherGoods = new VoucherGoods();
			BeanUtil.copyPropertiesIgnoreNullFilds(voucherGoodsVO, voucherGoods);
			
			//是否是新添加数据
			if (voucherGoodsVO.getVoucherGoodsId() == null){
				voucherGoods.setId(idGen.nextId());
				voucherGoods.setVoucher(voucher);
				voucherGoodsList.add(voucherGoods);
				continue;
			}
			voucherGoods.setId(voucherGoodsVO.getVoucherGoodsId());
			voucherGoods.setVoucher(voucher);
			ids.add(voucherGoodsVO.getVoucherGoodsId());		
			voucherGoodsList.add(voucherGoods);
		}
		
		voucher.setVoucherGoods(voucherGoodsList);
		voucherDAO.save(voucher);
		return Constants.RETURN_SUCESS;
	}

	@Override
	public DBVoucherViewVO listDBVoucherInfo(VoucherDTO voucherDTO) {
		
		//获取数量排行信息
		PageDTO<Object[]> pageDTO = voucherBSDAO.listDBVoucherInfo(voucherDTO);
		PageDTO<DBNumberRankViewVO> dbNumberRankViewVOPage = new PageDTO<DBNumberRankViewVO>();
		List<Object[]> objs = pageDTO.getRows();
		List<DBNumberRankViewVO> dbNumberRankViewVOList = new LinkedList<DBNumberRankViewVO>();
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
	 * @param VoucherDTO
	 * @return Map<String, String>
	 * */
	private Map<String, String> getDBPropertionInfo(VoucherDTO voucherDTO) {
		
		List<Object[]> objs = voucherBSDAO.listPartNumber(voucherDTO);
		Double totalNumber = voucherBSDAO.findDBTotalNumber(voucherDTO);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(4);
		Map<String, String> proportionMap = new HashMap<String, String>();
		for (Object[] obj : objs){
			String propertion = nf.format(((double)obj[1]/totalNumber)*100);
			proportionMap.put(String.valueOf(obj[0]), propertion+"%");
		}
		return proportionMap;
	}
}

