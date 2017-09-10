package com.apec.voucher.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.apec.framework.common.Constants;
import com.apec.framework.jpa.model.BaseModel;

/**
 * 上传凭据商品信息Model
 * @author gunj
 * create by 2017-07-14
 * */
@Entity
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
@Table(name = "voucher_goods")
public class VoucherGoods extends BaseModel<Long>{

	private static final long serialVersionUID = -8870188473398852449L;
	
	/**
	 * 商品名称
	 * */
	@Column(name = "sku_name", nullable = false, length = 30)
	private String skuName;
	
	/**
	 * skuId
	 * */
	@Column(name = "sku_id", nullable = false)
	private Long skuId;
	
	/**
	 * 数量(斤)
	 * */
	@Column(name = "number", nullable = false)
	private Double number;
	
	/**
	 * 单价（元）
	 * */
	@Column(name = "amount", nullable = false)
	private BigDecimal amount;
	
	/**
	 * 总金额
	 * */
	@Column(name = "total_amount", nullable = false)
	private BigDecimal totalAmount;
	
	@ManyToOne
	@JoinColumn(name = "voucher_id", nullable = false)
	private Voucher voucher;

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Double getNumber() {
		return number;
	}

	public void setNumber(Double number) {
		this.number = number;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Voucher getVoucher() {
		return voucher;
	}

	public void setVoucher(Voucher voucher) {
		this.voucher = voucher;
	}
}
