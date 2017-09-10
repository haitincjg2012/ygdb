package com.apec.voucher.viewvo;

import com.apec.framework.common.PageDTO;

/**
 * 返回页面信息
 * @author gunj
 * */
public class VoucherRespViewVO {

	private PageDTO<VoucherViewVO> voucherVVOs;
	
	private Double totalNumber;

	public PageDTO<VoucherViewVO> getVoucherVVOs() {
		return voucherVVOs;
	}

	public void setVoucherVVOs(PageDTO<VoucherViewVO> voucherVVOs) {
		this.voucherVVOs = voucherVVOs;
	}

	public Double getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Double totalNumber) {
		this.totalNumber = totalNumber;
	}
}
