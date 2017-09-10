package com.apec.voucher.vo;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.apec.framework.common.enumtype.UserType;
import com.apec.framework.dto.BaseVO;

/**
 * 上传凭据VO
 * @author gunj
 * create by 2017-07-14
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherVO extends BaseVO<Long>{
	
	private Long voucherId;

	/**
	 * 用户id
	 * */
	private Long userId;
	
	/**
	 * 手机号
	 * */
	private String mobile;
	
	/**
	 * 城市id
	 * */
	private String cityId;
	
	/**
	 * 县id
	 * */
	private String countyId;
	
	/**
	 * 镇id
	 * */
	private String townId;

	/**
	 * 卖货方类型：果农，客商
	 * */
	private UserType type;
	
	/**
	 * 出货仓库
	 * */
	private String shipWarehouse;
	
	/**
	 * 售货市场
	 * */
	private String saleMarket;
	
	/**
	 * 买方姓名
	 * */
	private String name;
	
	/**
	 * 交收日期
	 * */
	private Date deliveryTime;
	
	/**
	 * 凭据图片路径
	 * */
	private String voucherUrl;
	
	private List<VoucherGoodsVO> voucherGoodsVO;
}
