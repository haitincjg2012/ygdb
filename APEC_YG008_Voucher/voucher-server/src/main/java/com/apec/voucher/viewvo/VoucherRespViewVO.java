package com.apec.voucher.viewvo;

import com.apec.framework.common.PageDTO;
import lombok.Data;

/**
 * 返回页面信息
 * @author gunj
 * */
@Data
public class VoucherRespViewVO {

	private PageDTO<VoucherViewVO> voucherVVOs;
	
	private Double totalNumber;


}
