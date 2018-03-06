package com.apec.message.service;

import org.springframework.data.domain.PageRequest;

import com.apec.framework.common.PageDTO;
import com.apec.message.dto.MessageDTO;
import com.apec.message.vo.MessageVO;
import com.apec.message.vo.MessageVVO;

/**
 *  消息相关服务
 *  @author xxx
 */
public interface MessageService {

    /**
     * 添加站内信
     * @param stockInVO stockInVO
     * @return 结果码
     */
    String addMessageInfo(MessageVO stockInVO);

    /**
     * 根据发送人查询站内信列表
     * @param messageDTO messageDTO
     * @return PageDTO<MessageVVO> 站内信消息列表
     * */
    PageDTO<MessageVVO> listMessageInfoBySender(MessageDTO messageDTO);

    /**
     * 根据接收人查询站内信列表
     * @param receiver 接收人
     * @param pageRequest 分页请求对象
     * @return PageDTO<MailMessageBodyVO> 站内信消息列表
     * */
     PageDTO<MessageVVO> listMessageInfoByReceiver(Long receiver, PageRequest pageRequest);
    
    /**
     * 根据接收者和信息体id更新状态
     * @param receiver 接收者
     * @param bodyId 信息体主键
     * @return 结果码
     * */
    String updateStatus(Long receiver, Long bodyId);
    
    /**
     * 后台软删除站内信信息
     * @param bodyId Long 消息体主键
     * @return String 
     * */
    String deleteMessage(Long bodyId);

    /**
     * 清空消息
     * @param messageVO messageVO
     * @return 结果码
     */
    String clearMessage(MessageVO messageVO);
}
