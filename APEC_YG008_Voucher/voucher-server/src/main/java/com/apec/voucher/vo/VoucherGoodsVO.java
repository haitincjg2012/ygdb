package com.apec.voucher.vo;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.apec.framework.dto.BaseVO;

/**
 * 上传凭据商品信息VO
 * @author gunj
 * create by 2017-07-14
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherGoodsVO extends BaseVO<Long>{
	
	/**
	 * 商品名称
	 * */
	private String skuName;
	
	/**
	 * skuId
	 * */
	private Long skuId;
	
	/**
	 * 数量(斤)
	 * */
	private Double number;
	
	/**
	 * 单价（元）
	 * */
	private BigDecimal amount;
	
	/**
	 * 总金额
	 * */
	private BigDecimal totalAmount;
	
	private Long voucherGoodsId;
}
