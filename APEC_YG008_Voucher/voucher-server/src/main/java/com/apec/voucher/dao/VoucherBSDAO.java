package com.apec.voucher.dao;

import java.util.List;

import com.apec.framework.common.PageDTO;
import com.apec.voucher.dto.VoucherDTO;

public interface VoucherBSDAO {

	/**
	 * 后台交收单列表
	 * @param VoucherDTO
	 * @return Map<String, Object>
	 * */
	PageDTO<Object[]> listVoucherBS(VoucherDTO voucherDTO);
	
	/**
	 * 查询代办上传交收单数量排行
	 * @return PageDTO<Object[]>
	 * */
	PageDTO<Object[]> listDBVoucherInfo(VoucherDTO voucherDTO);
	
	/**
	 * 查询代办各规格等级数量
	 * @return List<Object[]>
	 * */
	List<Object[]> listPartNumber(VoucherDTO voucherDTO);
	
	/**
	 * 查询代办总数量
	 * @return double
	 * */
	Double findDBTotalNumber(VoucherDTO voucherDTO);

	/**
	 * 查询用户上传单据总数
	 * @return
	 */
	List<Object[]> countVoucherOfUser();
}
