package com.apec.message.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.message.model.MessageBody;

/**
 * 站内信DAO
 * @author gunj
 * create by 2017-07-11
 * */
public interface MessageBodyDAO extends BaseDAO<MessageBody, Long>{
	
	/**
	 * 查询站内信内容列表
	 * @param ids List<BigInteger>
	 * @param pageRequest Pageable
	 * @return Page<MessageBody>
	 * */
	@Query(value = "select * from message_body where enable_flag='Y' and id in(:ids) order by send_time desc /* #pageable*/ ",
			countQuery = "select count(*) from message_body where enable_flag='Y' and id in(:ids)", nativeQuery = true)
	public Page<MessageBody> findByIdIn(@Param("ids")List<Long> ids, Pageable pageRequest);

}
