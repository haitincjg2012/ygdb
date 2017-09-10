package com.apec.voucher.viewvo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 后台交收单管理页面信息
 * @author gunj
 * create by 2017-08-05
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherBSViewVO {
	
	/**
	 * 创建日期
	 * */
	private Date createDate;

	/**
	 * 交收日期
	 * */
	private Date deliveryTime;
	
	/**
	 * 出货仓库
	 * */
	private String shipWarehouse;
	
	/**
	 * 售货市场
	 * */
	private String saleMarket;
	
	/**
	 * 交收单总重量（斤）
	 * */
	private Double totalNumber;
	
	/**
	 * 合计金额（元）
	 * */
	private BigDecimal totalAmount;
	
	/**
	 * 上传人
	 * */
	private String userName;
	
	/**
	 * 图片路径
	 * */
	private String voucherUrl;
	
	/**
	 * id
	 * */
	private Long voucherId;
}
