package com.apec.voucher.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.voucher.model.VoucherGoods;

/**
 * 上传凭证商品信息dao
 * @author gunj
 * create by 2017-07-14
 * */
public interface VoucherGoodsDAO extends BaseDAO<VoucherGoods, Long>{
	
	/**
	 * 查询上传凭据商品信息
	 * @param voucherId voucherId
	 * @return Page<VoucherGoodsVO> voucherGoods分页对象
	 * */
	@Query(value = "select * from voucher_goods where voucher_id=:voucherId and enable_flag='Y' order by create_date desc", nativeQuery = true)
	public List<VoucherGoods> listByVoucherId(@Param("voucherId") Long voucherId);
	
	/**
	 * 根据用户id查询用户交收单数量总数
	 * @param userId 用户id
	 * @return Double 交收单数量总数
	 * */
	@Query(value = "select sum(number) from voucher_goods where enable_flag='Y' and voucher_id in (select id from voucher v where v.enable_flag='Y'"
			+ " and v.user_id=:userId)",nativeQuery = true)
	public Double findTotalNumberByUserId(@Param("userId")Long userId);
	
	/**
	 * 根据voucherId查询交收单数量
	 * @param voucherId Long
	 * @return double 交收单单次数量
	 * */
	@Query(value = "select sum(number) from voucher_goods where enable_flag='Y' and voucher_id=:voucherId", nativeQuery = true)
	Double findNumberByVoucherId(@Param("voucherId") Long voucherId);
}
