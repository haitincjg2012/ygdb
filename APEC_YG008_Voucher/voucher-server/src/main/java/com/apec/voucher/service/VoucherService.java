package com.apec.voucher.service;

import com.apec.voucher.viewvo.*;
import com.apec.voucher.vo.StaticVoucherInfoVO;
import org.springframework.data.domain.PageRequest;

import com.apec.framework.common.PageDTO;
import com.apec.voucher.dto.VoucherDTO;
import com.apec.voucher.vo.VoucherVO;

import java.util.List;

/**
 * 上传凭据service
 * @author gunj
 * create by 2017-07-14
 * */
public interface VoucherService {

	/**
	 * 添加上传凭据信息
	 * @param voucherVO 请求对象体
	 * @return 结果码
	 * */
	String addVoucherInfo(VoucherVO voucherVO);

	/**
	 * 交收单审核
	 * @param voucherVO 交收单数据
	 * @param userId 操作人id
	 * @return 处理结果
	 */
	String voucherReview(VoucherVO voucherVO,String userId);
	
	/**
	 * 查询上传凭据信息
	 * @param voucherDTO 单据信息
	 * @param pageRequest 分页对象
	 * @return PageDTO<VoucherVO>
	 * */
	VoucherRespViewVO listVoucherInfo(VoucherDTO voucherDTO,PageRequest pageRequest);

	/**
	 * 交收单明细
	 * @param voucherId voucher表id
	 * @return VoucherViewVO
	 * */
	VoucherViewVO findVoucherInfoById(Long voucherId);
	
	/**
	 * 后台交收单列表
	 * @param voucherDTO 单据信息
	 * @param userIds 上传人信息
	 * @param pageRequest 分页信息
	 * @return 交收单分页列表
	 * */
	PageDTO<VoucherBSViewVO> listVoucherBSViewVOGroubById(VoucherDTO voucherDTO,List<Long> userIds,PageRequest pageRequest);

	/**
	 * 查询条件下的交收单总重量和总价格
	 * @param voucherDTO 查询条件
	 * @param userIds 用户
	 * @return 总重量和总金额(总计)
	 */
	VoucherBSViewVO getVoucherBSSumWeight(VoucherDTO voucherDTO,List<Long> userIds);

	/**
	 * 统计报表数据
	 * @param voucherDTO 查询条件
	 * @param pageRequest 分页查询条件
	 * @return 分页对象
	 */
	PageDTO<StaticVoucherInfoVO> staticVoucherInfo(VoucherDTO voucherDTO, PageRequest pageRequest);
	
	/**
	 * 删除交收单数据
	 * @param voucherDTO voucherDTO
	 * @return String 是否成功
	 * */
	int deleteVoucherInfo(VoucherDTO voucherDTO);
	
	/**
	 * 后台代办调货分析信息
	 * @param  voucherDTO VoucherDTO
	 * @return DBVoucherViewVO
	 * */
	DBVoucherViewVO listDBVoucherInfo(VoucherDTO voucherDTO);
	
	/**
	 * 获取代办个人调果排行
	 * @param userId 用户id
	 * @return DBNumberRankViewVO
	 * */
	DBNumberRankViewVO findNumberRankViewVO(Long userId);
	
	/**
	 * 获取当月代办上传单据数量排行旁
	 * @param voucherDTO voucherDTO
	 * @return PageDTO<DBNumberRankViewVO>
	 * */
	PageDTO<DBNumberRankViewVO> listMonthDBNumberRankViewVO(VoucherDTO voucherDTO);
	
	/**
	 * 获取代办上传单据数量总榜
	 * @param voucherDTO voucherDTO
	 * @return PageDTO<DBNumberRankViewVO>
	 * */
	PageDTO<DBNumberRankViewVO> listTotalDBNumberRankViewVO(VoucherDTO voucherDTO);

	/**
	 * 更新用户缓存中的上传总数
	 * @return 结果码
	 */
	String countVoucherOfUser();

	/**
	 * 每周上榜信息
	 * @return 结果码
	 */
	List<WeekBest> maxVoucherOfUser();
	
}
