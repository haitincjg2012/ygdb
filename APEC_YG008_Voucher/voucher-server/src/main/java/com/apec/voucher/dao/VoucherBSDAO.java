package com.apec.voucher.dao;

import java.util.List;

import com.apec.framework.common.PageDTO;
import com.apec.voucher.dto.VoucherDTO;

/**
 * @author xxx
 */
public interface VoucherBSDAO {

	/**
	 * 后台交收单列表
	 * @param voucherDTO voucherDTO
	 * @return 交收单分页列表
	 * */
	PageDTO<Object[]> listVoucherBS(VoucherDTO voucherDTO);
	
	/**
	 * 查询代办上传交收单数量排行
	 * @param voucherDTO voucherDTO
	 * @return 代办上传交收单数量排行
	 * */
	PageDTO<Object[]> listDBVoucherInfo(VoucherDTO voucherDTO);
	
	/**
	 * 查询代办各规格等级数量
	 * @param voucherDTO voucherDTO
	 * @return 代办各规格等级数量
	 * */
	List<Object[]> listPartNumber(VoucherDTO voucherDTO);
	
	/**
	 * 查询代办总数量
	 * @param voucherDTO voucherDTO
	 * @return double 代办总数量
	 * */
	Double findDBTotalNumber(VoucherDTO voucherDTO);

	/**
	 * 查询用户上传单据总数
	 * @return 用户上传单据总数
	 */
	List<Object[]> countVoucherOfUser();

	/**
	 * 查询用户上传单据总数(最大)
	 * @return 用户上传单据总数
	 */
	List<Object[]> maxVoucherOfUser();
}
