package com.apec.message.dao.impl;

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
import com.apec.message.dao.BSMessageDAO;
import com.apec.message.dto.MessageDTO;

/**
 * 后台系统通知
 * @author gunj
 * create by 2017-09-01
 * */
@Repository
public class BSMessageDAOImpl implements BSMessageDAO{

	@InjectLogger
	private Logger log;
	
	@Autowired
	private EntityManagerFactory emf;
	
	@Override
	public PageDTO<Object[]> listBSMessage(MessageDTO messageDTO) {
		StringBuffer sb = new StringBuffer("select m.sender,b.id,b.title,b.content,b.send_time from message m "
				+ "inner join message_body b "
				+ "on m.body_id=b.id and m.enable_flag='Y' and b.enable_flag='Y' and m.sender != 'messagesendersystem'");
		//查询条件
		String bsCondition = getBSConditionQuery(messageDTO);
		sb.append(bsCondition);
		sb.append(" group by b.id order by b.send_time");
		
		//查询总条数
		StringBuffer countSql = new StringBuffer("select count(*) from (select b.id from message m "
				+ "inner join message_body b on m.body_id=b.id and b.enable_flag='y' and m.sender != 'messagesendersystem'");
		countSql.append(bsCondition);
		countSql.append(" group by b.id) c");
		PageDTO<Object[]> objs = listObjectInfo(messageDTO, sb.toString(), countSql.toString());
		return objs;
	}

	/**
	 * 分页查询Object信息
	 * @param messageDTO 查询条件对象
	 * @param sql 查询object信息语句
	 * @param countSql 查询条数语句
	 * @return 分页结果
	 * */
	@SuppressWarnings("unchecked")
	private PageDTO<Object[]> listObjectInfo(MessageDTO messageDTO,String sql,String countSql){
		EntityManager em = null;
		PageDTO<Object[]> pageDTO = new PageDTO<>();
		StringBuffer sb = new StringBuffer(sql);
		
        //分页
  		int pageNumber = 1;
          int pageSize = 10;
          if (messageDTO.getPageNumber() > 0) {
              pageNumber = messageDTO.getPageNumber();
          }
          if (messageDTO.getPageSize() > 0 && messageDTO.getPageSize() < 100) {
              pageSize = messageDTO.getPageSize();
          }
          sb.append(" limit ").append((pageNumber-1)*pageSize).append(",").append(pageNumber*pageSize);
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
	
	/**
	 * 后台系统通知多条件
	 * @param messageDTO messageDTO
	 * @return String
	 * */
	private String getBSConditionQuery(MessageDTO messageDTO){
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotEmpty(messageDTO.getSender())){
			sb.append( "and m.sender like ").append("'%").append(messageDTO.getSender()).append("%'");
		}
		return sb.toString();
	}
}
