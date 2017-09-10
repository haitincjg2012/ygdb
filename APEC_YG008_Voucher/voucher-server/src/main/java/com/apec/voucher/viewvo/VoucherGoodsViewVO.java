package com.apec.voucher.viewvo;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回页面信息voucherGoods
 * @author gunj
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherGoodsViewVO {

	private Long voucherGoodsId;
	
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
}
