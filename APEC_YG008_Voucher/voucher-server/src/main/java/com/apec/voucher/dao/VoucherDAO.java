package com.apec.voucher.dao;

import java.util.Date;
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
	 * @param userId 用户id
	 * @return List<Long>
	 * */
	@Query(value = "select id from voucher where user_id=:userId", nativeQuery = true)
	List<Long> listIdByUserId(@Param("userId") Long userId);
	
	/**
	 * 软删除上交单数据
	 * @param voucherId voucherId
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
	 * 平台个人数量排名信息（代办、客商）
	 *@param userId Long
	 *@return Object[][]
	 * */
	@Query(value = "SELECT * FROM (SELECT obj.user_id,obj.name,obj.user_type,obj.sumNuber, CASE WHEN @rowtotal = obj.sumNuber THEN "
			+ "@rownum WHEN @rowtotal\\:= obj.sumNuber THEN @rownum\\:=@rownum + 1 WHEN @rowtotal = 0 THEN @rownum\\:=@rownum + 1 END "
			+ "AS rownum FROM(SELECT v.user_id,u.name ,u.user_type, SUM(g.number) AS sumNuber FROM voucher_goods g INNER JOIN voucher v "
			+ "INNER JOIN user u ON v.id = g.voucher_id AND v.user_id = u.id AND u.user_type IN ('DB','KS') AND g.enable_flag='Y' AND "
			+ "v.enable_flag= 'Y' and v.audit_state = 'Y'  AND u.enable_flag='Y' GROUP BY v.user_id ORDER BY sumNuber DESC) AS obj,(SELECT @rownum \\:= 0,"
			+ "@rowtotal \\:= NULL) r) T WHERE T.user_id =:userId",nativeQuery = true)
	List<Object[]> findDBNumberRankInfo(@Param("userId")Long userId);
	
	/**
	 * 代办个人规格数量取前5名
	 * @param userId Long
	 * @return List<Object[]>
	 * */
	@Query(value = "select substring_index(substring_index(g.sku_name,' ',2),' ',-1) as attrvalue,sum(g.number) as partnumber from "
			+ "voucher_goods g inner join `user` u inner join voucher v on "
			+ "u.user_type='db' and u.id=v.user_id and v.id=g.voucher_id and u.enable_flag='Y' and v.audit_state = 'Y' and v.enable_flag='Y' and g.enable_flag='Y' "
			+ "and v.user_id=:userId and substring_index(substring_index(g.sku_name,' ',2),' ',-1) in "
			+ "(select attr_value from product_attr where attr_name='规格' group by attr_value) "
			+ "group by attrvalue order by partnumber desc limit 0,5", nativeQuery = true)
	List<Object[]> listAttrNumber(@Param("userId") Long userId);
	
	/**
	 * 查询平台调果排行最后一名
	 * @return int
	 * */
	@Query(value = "select COUNT(DISTINCT(v.user_id)) FROM voucher v INNER JOIN `user` u ON"
			+ " v.user_id=u.id and v.enable_flag='Y' and v.audit_state = 'Y' and u.enable_flag='Y' and u.user_type in ('DB','KS')",nativeQuery = true)
	int findLastRankNo();

	/**
	 * 查询符合条件的用户id
	 * @param sumweight sumweight 交收总量的条件
	 * @param time time 交收次数
	 * @return List<Object[]>
	 * */
	@Query(value = "select user_id,ifnull(sum(b.number),0) as sumweight,count(a.id) as time from voucher a inner join voucher_goods b on a.id = b.voucher_id where a.enable_flag= 'Y' and a.audit_state = 'Y' and b.enable_flag= 'Y'  and a.create_date > ?3 and a.create_date < ?4 group by a.user_id having(sumweight >= ?1 and time > ?2) ", nativeQuery = true)
	List<Object[]> checkVoucherMan(Integer sumweight, Integer time, Date startTime,Date endTime);

	/**
	 * 查询用户上传的交收单总次数和总数量
	 * @param ids ids
	 * @return List<Object[]>
	 * */
	@Query(value = "select user_id,ifnull(sum(b.number),0) as sumweight,count(a.id) as time from voucher a inner join voucher_goods b on a.id = b.voucher_id where a.enable_flag= 'Y' and a.audit_state = 'Y' and b.enable_flag= 'Y' and a.user_id in ?1 group by a.user_id  ", nativeQuery = true)
	List<Object[]> queryUserNum(List<Long> ids);


	@Query(value = "select count(1),DATE_FORMAT(v.create_date,'%Y/%m') d,v.user_id,sum(g.number),sum(g.total_amount),u.name,u.user_type,u.mobile FROM voucher v inner join voucher_goods g inner join user u  on v.id = g.voucher_id and  v.user_id = u.id /* #pageable*/ " +
			" where (FALSE = ?2 or  (u.name like %?1%) ) and (FALSE = ?4 or  (v.user_id in (?3) ) )  " +
			" and  v.audit_state = 'Y' and (FALSE = ?6 or  (v.create_date >= ?5) )  and (FALSE = ?8 or  (v.create_date <= ?7) ) " +
			"group by d,v.user_id order by d desc",
			countQuery = "select count(1) from (select 1 FROM voucher v inner join voucher_goods g inner join user u  on v.id = g.voucher_id and  v.user_id = u.id" +
					" where (FALSE = ?2 or  ( u.name like %?1%) ) and (FALSE = ?4 or  (v.user_id in (?3) ) )  " +
					" and v.audit_state = 'Y' and (FALSE = ?6 or  (v.create_date >= ?5) )  and (FALSE = ?8 or  (v.create_date <= ?7) ) " +
					"  group by DATE_FORMAT(v.create_date,'%Y/%m'),v.user_id ) a",
			nativeQuery = true)
	Page<Object[]> staticVoucherForMonthAndUserId(String userName,boolean isUserName,List<Long> userIds,boolean isUserIds,
												 Date startTime,boolean isStartTime,Date endTime,boolean isEndTime,Pageable pageable);


}
