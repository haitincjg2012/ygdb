package com.apec.voucher.service;

import org.springframework.data.domain.PageRequest;

import com.apec.framework.common.PageDTO;
import com.apec.voucher.dto.VoucherDTO;
import com.apec.voucher.viewvo.DBNumberRankViewVO;
import com.apec.voucher.viewvo.DBVoucherViewVO;
import com.apec.voucher.viewvo.VoucherBSViewVO;
import com.apec.voucher.viewvo.VoucherRespViewVO;
import com.apec.voucher.viewvo.VoucherViewVO;
import com.apec.voucher.vo.VoucherGoodsVO;
import com.apec.voucher.vo.VoucherVO;

/**
 * 上传凭据service
 * @author gunj
 * create by 2017-07-14
 * */
public interface VoucherService {

	/**
	 * 添加上传凭据信息
	 * @param voucherVO 请求对象体
	 * */
	public String addVoucherInfo(VoucherVO voucherVO);
	
	/**
	 * 查询上传凭据信息
	 * @param VoucherVO
	 * @return PageDTO<VoucherVO>
	 * */
	public VoucherRespViewVO listVoucherInfo(VoucherDTO voucherDTO,PageRequest pageRequest);

	/**
	 * 交收单明细
	 * @param voucherId voucher表id
	 * @return VoucherViewVO
	 * */
	public VoucherViewVO findVoucherInfoById(Long voucherId);
	
	/**
	 * 后台交收单列表
	 * @param VoucherDTO
	 * @return PageDTO<VoucherBSViewVO>
	 * */
	public PageDTO<VoucherBSViewVO> listVoucherBSViewVOGroubById(VoucherDTO voucherDTO);
	
	/**
	 * 删除交收单数据
	 * @param id Long voucher表id
	 * @return String 是否成功
	 * */
	int deleteVoucherInfo(Long id);
	
	/**
	 * 增加交收单商品数据
	 * @param voucherId Long
	 * @param VoucherGoodsVO
	 * @return String 是否成功
	 * */
	String addVoucherGoodsInfo(VoucherGoodsVO voucherGoodsVO);
	
	/**
	 * 更新交收单数据
	 * @param VoucherVO
	 * @return String 是否成功
	 * */
	String updateVoucherInfo(VoucherVO voucherVO);
	
	/**
	 * 后台代办调货分析信息
	 * @param  voucherDTO VoucherDTO
	 * @return DBVoucherViewVO
	 * */
	DBVoucherViewVO listDBVoucherInfo(VoucherDTO voucherDTO);
	
	/**
	 * 获取代办个人调果排行
	 * @param Long userId
	 * @return DBNumberRankViewVO
	 * */
	DBNumberRankViewVO findNumberRankViewVO(Long userId);
	
	/**
	 * 获取当月代办上传单据数量排行旁
	 * @param VoucherDTO voucherDTO
	 * @return PageDTO<DBNumberRankViewVO>
	 * */
	PageDTO<DBNumberRankViewVO> listMonthDBNumberRankViewVO(VoucherDTO voucherDTO);
	
	/**
	 * 获取代办上传单据数量总榜
	 * @param VoucherDTO voucherDTO
	 * @return PageDTO<DBNumberRankViewVO>
	 * */
	PageDTO<DBNumberRankViewVO> listTotalDBNumberRankViewVO(VoucherDTO voucherDTO);
	
}
