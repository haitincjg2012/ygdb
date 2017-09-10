package com.apec.voucher.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.voucher.model.Voucher;

/**
 * 上传凭证dao
 * @author gunj
 * create by 2017-07-14
 * */
public interface VoucherDAO extends BaseDAO<Voucher, Long>{
	
	/**
	 * 根据userId查询id集合
	 * @return List<Long>
	 * */
	@Query(value = "select id from voucher where user_id=:userId", nativeQuery = true)
	public List<Long> listIdByUserId(@Param("userId") Long userId);
	
	/**
	 * 软删除上交单数据
	 * @param EnableFlag enableFlag
	 * @param Long voucherId
	 * @return int
	 * */
	@Modifying
	@Query(value = "update voucher as v,voucher_goods as g set v.enable_flag='N',g.enable_flag='N' "
			+ "where v.id=:voucherId and g.voucher_id=:voucherId",nativeQuery = true)
	int deleteVoucher(@Param("voucherId")Long voucherId);
	
	/**
	 * 查询交收单详情信息
	 * @param voucherId Long
	 * @return Voucher
	 * */
	@Query(value = "select * from voucher where id=:voucherId and enable_flag='Y' order by create_date desc",nativeQuery = true)
	Voucher findVoucherInfo(@Param("voucherId")Long voucherId);
}
