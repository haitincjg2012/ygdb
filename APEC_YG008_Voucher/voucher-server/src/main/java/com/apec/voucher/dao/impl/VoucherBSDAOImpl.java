package com.apec.voucher.dao.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.apec.framework.common.PageDTO;
import com.apec.framework.log.InjectLogger;
import com.apec.voucher.dao.VoucherBSDAO;
import com.apec.voucher.dto.VoucherDTO;

/**
 * @author xxx
 */
@Repository
public class VoucherBSDAOImpl implements VoucherBSDAO{

	@Autowired
	private EntityManagerFactory emf;
	
	@InjectLogger
	private Logger log;
	
	@Override
	public PageDTO<Object[]> listVoucherBS(VoucherDTO voucherDTO){
		StringBuffer sb = new StringBuffer("SELECT v.create_date,SUM(number) AS totalNumber,SUM(total_amount) AS totalAmount,sale_market,ship_warehouse,delivery_time,voucher_url,v.id,v.user_id,u.`name` as userName,v.name as name,v.audit_state "
				+ " FROM voucher_goods AS g INNER JOIN voucher AS v INNER JOIN `user` u on v.user_id=u.id and v.id=g.voucher_id where v.enable_flag='Y' and g.enable_flag='Y' and u.enable_flag='Y' ");
        
        //条件查询
        String condition = getBSConditionQuery(voucherDTO);
        sb.append(condition);
		sb.append(" GROUP BY v.id ORDER BY v.create_date desc,v.id desc");
		
		//查询总条数
        StringBuffer countSql = new StringBuffer("SELECT COUNT(DISTINCT(v.id)) FROM voucher_goods AS g INNER JOIN voucher AS v INNER JOIN `user` u "
        		+ "on v.user_id=u.id and v.id=g.voucher_id where v.enable_flag='Y' and g.enable_flag='Y' and u.enable_flag='Y'");
        countSql.append(condition);
        
        PageDTO<Object[]> pageDTO = listObjectInfo(voucherDTO, sb.toString(), countSql.toString());
        return pageDTO;
    }
	
	/**
	 * 后台交收单管理多条件
	 * @param voucherDTO
	 * @return String
	 * */
	private String getBSConditionQuery(VoucherDTO voucherDTO){
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotEmpty(voucherDTO.getUserName())){
			sb.append(" AND v.user_id IN(SELECT id FROM `user` WHERE `name` LIKE ").append("'%").append(voucherDTO.getUserName()).append("%')");
		}
		if (voucherDTO.getStartDate() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String startDate = sdf.format(voucherDTO.getStartDate());
			sb.append(" AND date(v.create_date)>=").append("'").append(startDate).append("'");
		}
		if (voucherDTO.getEndDate() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String endDate = sdf.format(voucherDTO.getEndDate());
			sb.append(" AND date(v.create_date)<=").append("'").append(endDate).append("'");
		}
		if (voucherDTO.getDeliveryStartDate() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String deliveryStartDate = sdf.format(voucherDTO.getDeliveryStartDate());
			sb.append(" AND v.delivery_time>=").append("'").append(deliveryStartDate).append("'");
		}
		if (voucherDTO.getDeliveryEndDate() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String deliveryEndDate = sdf.format(voucherDTO.getDeliveryEndDate());
			sb.append(" AND v.delivery_time<=").append("'").append(deliveryEndDate).append("'");
		}
		if (StringUtils.isNotEmpty(voucherDTO.getAuditState())){
			sb.append(" AND v.audit_state = '").append(voucherDTO.getAuditState()).append("'");
		}
		return sb.toString();
	}

	@Override
	public PageDTO<Object[]> listDBVoucherInfo(VoucherDTO voucherDTO) {
		
		StringBuffer sb = new StringBuffer("SELECT u.name,u.user_type,sum(g.number) as sumNuber,u.img_url,u.id,u.user_org_id  FROM "
				+ "voucher_goods g "
				+ "inner join voucher v "
				+ "inner join user u "
				+ "on v.id = g.voucher_id and v.user_id = u.id and u.user_type = 'DB' "
				+ "and g.enable_flag='Y' and v.enable_flag= 'Y' and v.audit_state = 'Y' and u.enable_flag='Y'");
		
		//条件查询
		String condition = getBSConditionQuery(voucherDTO);
		sb.append(condition);
		sb.append(" group by v.user_id ORDER BY sumNuber DESC");
		
		StringBuffer countSql = new StringBuffer("SELECT count(DISTINCT v.user_id) FROM "
				+ "voucher_goods g "
				+ "inner join voucher v "
				+ "inner join user u "
				+ "on v.id = g.voucher_id and v.user_id = u.id and u.user_type = 'DB' "
				+ "and g.enable_flag='Y' and v.enable_flag= 'Y' and u.enable_flag='Y' ");
        countSql.append(condition);
        PageDTO<Object[]> pageDTO = listObjectInfo(voucherDTO, sb.toString(), countSql.toString());
        return pageDTO;
	}
	
	/**
	 * 分页查询Object信息
	 * @param voucherDTO 查询条件对象
	 * @param sql 查询object信息语句
	 * @param countSql 查询条数语句
	 * @return 分页结果
	 * */
	@SuppressWarnings("unchecked")
	private PageDTO<Object[]> listObjectInfo(VoucherDTO voucherDTO,String sql,String countSql){
		EntityManager em = null;
		PageDTO<Object[]> pageDTO = new PageDTO<>();
		StringBuffer sb = new StringBuffer(sql);
		
        //分页
  		int pageNumber = 1;
          int pageSize = 10;
          if (voucherDTO.getPageNumber() > 0) {
              pageNumber = voucherDTO.getPageNumber();
          }
          if (voucherDTO.getPageSize() > 0 && voucherDTO.getPageSize() < 100) {
              pageSize = voucherDTO.getPageSize();
          }
          sb.append(" limit ").append((pageNumber-1)*pageSize).append(",").append(pageSize);
          log.info(sb.toString());
          long count = 0;
          try {
        	  em = emf.createEntityManager();
              Query query = em.createNativeQuery(sb.toString());
              List<Object[]> list = query.getResultList();
              
              //查询总条数
              log.info(countSql);
  			  query = em.createNativeQuery(countSql);
  	          Object obj = query.getSingleResult();
  	          if (obj != null) {
  	        	  count = Long.valueOf(obj.toString());
  	          }
			  //当前页数
              pageDTO.setNumber(pageNumber);
              pageDTO.setRows(list);
              pageDTO.setTotalElements(count);
			  //总页数
              double totalPages = Math.ceil((double)count/pageSize);
              pageDTO.setTotalPages((int)totalPages);
		} catch (Exception e){
            throw e;
        } finally{
            if(null != em){
                em.close();
            }
        }
		return pageDTO;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> listPartNumber(VoucherDTO voucherDTO) {
		StringBuffer sb = new StringBuffer("SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(g.sku_name,' ',2),' ',-1) as attrValue,SUM(g.number) as partNumber FROM"
				+ " voucher_goods g "
				+ "INNER JOIN `user` u "
				+ "INNER JOIN voucher v "
				+ "ON u.user_type='DB' and u.id=v.user_id and v.id=g.voucher_id and u.enable_flag='Y' and v.enable_flag='Y' and g.enable_flag='Y' and "
				+ "SUBSTRING_INDEX(SUBSTRING_INDEX(g.sku_name,' ',2),' ',-1) in "
				+ "(SELECT attr_value FROM product_attr where attr_name='规格' GROUP BY attr_value)");
		//条件查询
        String condition = getBSConditionQuery(voucherDTO);
        sb.append(condition);      
		sb.append(" GROUP BY attrValue");	
		log.info(sb.toString());
		EntityManager em = null;
		List<Object[]> list;
		try {
			em = emf.createEntityManager();
            Query query = em.createNativeQuery(sb.toString());
            list = query.getResultList();
		} catch (Exception e){
            throw e;
        } finally{
            if(null != em){
                em.close();
            }
        }
		return list;
	}

	@Override
	public Double findDBTotalNumber(VoucherDTO voucherDTO) {
		StringBuffer sb = new StringBuffer("select SUM(number) as totalNumber FROM voucher_goods g "
				+ "INNER JOIN voucher v "
				+ "INNER JOIN `user` u "
				+ "ON u.id=v.user_id and v.id=g.voucher_id and u.enable_flag='Y' and v.enable_flag='Y' and g.enable_flag='Y' and u.user_type='DB'");
		//条件查询
        String condition = getBSConditionQuery(voucherDTO);
        sb.append(condition);
        log.info(sb.toString());
        EntityManager em = null;
        double totalNumber = 0;
		try {
			em = emf.createEntityManager();
            Query query = em.createNativeQuery(sb.toString());
            Object obj = query.getSingleResult();
            if (obj != null){
            	totalNumber = (double)obj;
            }
		} catch (Exception e){
            throw e;
        } finally{
            if(null != em){
                em.close();
            }
        }
		return totalNumber;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> countVoucherOfUser() {
		StringBuffer sb = new StringBuffer("select user_id,ifnull(sum(b.number),0) from voucher a inner join voucher_goods b on a.id = b.voucher_id where a.enable_flag= 'Y' and a.audit_state = 'Y' and b.enable_flag= 'Y' group by a.user_id");
		log.info(sb.toString());
		EntityManager em = null;
		List<Object[]> list = null;
		try{
			em = emf.createEntityManager();
			Query query = em.createNativeQuery(sb.toString());
			list = query.getResultList();
		}catch(Exception e){
			log.error("【voucher】[countVoucherOfUser]exception:{}",e);
		}finally{
			if(null != em){
				em.close();
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> maxVoucherOfUser() {
		StringBuffer sb = new StringBuffer("select user_id,ifnull(sum(b.number),0) as sumweight,count(a.id) from voucher a inner join voucher_goods b on a.id = b.voucher_id where a.enable_flag= 'Y' and b.enable_flag= 'Y' group by a.user_id");
		sb.append(" order by sumweight desc limit 2 ");
		log.info(sb.toString());
		EntityManager em = null;
		List<Object[]> list = null;
		try{
			em = emf.createEntityManager();
			Query query = em.createNativeQuery(sb.toString());
			list = query.getResultList();
		}catch(Exception e){
			log.error("【voucher】[countVoucherOfUser]exception:{}",e);
		}finally{
			if(null != em){
				em.close();
			}
		}
		return list;
	}

}
