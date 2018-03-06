package com.apec.message.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.message.model.Message;


/**
 * Title: 客户DAO
 * @author yirde  2017/3/21
 */
public interface MessageDAO extends BaseDAO<Message, Long> {
	
	/**
	 * 根据接收人查询消息列表
	 * @param receiver 接收者
	 * @return List<BigInteger> body_id列表
	 * */
	@Query(value = "select m.body_id from message m where m.receiver=:receiver and m.enable_flag='Y'", nativeQuery = true)
	List<Long> findByReceiver(@Param("receiver") Long receiver);
	
	/**
	 * 根据receiver和bodyId更新状态
	 * @param messageStatus MessageStatus状态枚举类
	 * @param receiver 接收者
	 * @param bodyId 消息体主键
	 * @return 修改行数
	 * */
	@Modifying
	@Query(value = "UPDATE message SET message_status=:messageStatus WHERE receiver=:receiver AND body_id=:bodyId", nativeQuery = true)
	int updateMessageStatusByReceiverAndBodyId(@Param("messageStatus") String messageStatus, @Param("receiver") Long receiver,
			@Param("bodyId") Long bodyId);
	
	/**
	 * 软删除站内信消息体及记录
	 * @param bodyId Long message_body表id
	 * @return int 影响条数
	 * */
	@Modifying
	@Query(value = "UPDATE message m ,message_body b SET m.enable_flag='N',b.enable_flag='N' "
			+ "WHERE b.id=:bodyId and m.body_id=:bodyId",nativeQuery = true)
	int updateEnableFlagByBodyId(@Param("bodyId")Long bodyId);
	
}
