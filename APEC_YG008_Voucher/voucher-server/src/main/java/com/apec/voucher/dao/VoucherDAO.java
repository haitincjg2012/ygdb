package com.apec.voucher.dao;

import java.util.List;

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
	
	/**
	 * 代办个人数量排名信息
	 *@param userId Long
	 *@return Object[][]
	 * */
	@Query(value = "select t.rankNo,t.name,t.user_type,t.sumNuber from "
			+ "(SELECT (@rank_no\\:=@rank_no+1) as rankNo,v.user_id,u.name,u.user_type,sum(g.number) as sumNuber FROM (select @rank_no\\:=0) r"
			+ " inner join voucher_goods g inner join voucher v inner join user u "
			+ "on v.id = g.voucher_id and v.user_id = u.id and u.user_type = 'DB' and "
			+ "g.enable_flag='Y' and v.enable_flag= 'Y' and u.enable_flag='Y' group by v.user_id ORDER BY sumNuber DESC) t "
			+ "where t.user_id=:userId",nativeQuery = true)
	List<Object[]> findDBNumberRankInfo(@Param("userId")Long userId);
	
	/**
	 * 代办个人规格数量取前5名
	 * @param userId Long
	 * @return List<Object[]>
	 * */
	@Query(value = "select substring_index(substring_index(g.sku_name,' ',2),' ',-1) as attrvalue,sum(g.number) as partnumber from "
			+ "voucher_goods g inner join `user` u inner join voucher v on "
			+ "u.user_type='db' and u.id=v.user_id and v.id=g.voucher_id and u.enable_flag='Y' and v.enable_flag='Y' and g.enable_flag='Y' "
			+ "and v.user_id=:userId and substring_index(substring_index(g.sku_name,' ',2),' ',-1) in "
			+ "(select attr_value from product_attr where attr_name='规格' group by attr_value) "
			+ "group by attrvalue order by partnumber desc limit 0,5", nativeQuery = true)
	List<Object[]> listAttrNumber(@Param("userId") Long userId);
	
	/**
	 * 查询代办调果排行最后一名
	 * @return int
	 * */
	@Query(value = "select COUNT(DISTINCT(v.user_id)) FROM voucher v INNER JOIN `user` u ON"
			+ " v.user_id=u.id and v.enable_flag='Y' and u.enable_flag='Y' and u.user_type='DB'",nativeQuery = true)
	int findLastRankNo();
}
