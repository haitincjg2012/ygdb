package com.apec.society.service;

import com.apec.framework.common.PageDTO;
import com.apec.society.dto.SocietyPostReplyMsgDTO;
import com.apec.society.view.SocietyPostReplyMsgViewVO;
import com.apec.society.vo.SocietyPostReplyMsgVO;
import org.springframework.data.domain.PageRequest;

/**
 * Created by hmy on 2017/11/1.
 *
 * @author hmy
 */
public interface SocietyPostReplyMsgService {

    /**
     * 添加回复评论帖子消息
     * @param societyPostReplyMsgVO 消息体
     * @return  操作结果
     */
    String addSocietyPostReplyMsg(SocietyPostReplyMsgVO societyPostReplyMsgVO);

    /**
     * 分页查询我的帖子相关信息
     * @param societyPostReplyMsgDTO  查询条件
     * @param pageRequest  分页对象
     * @return 分页结果
     */
    PageDTO<SocietyPostReplyMsgViewVO> societyPostReplyMsgPage(SocietyPostReplyMsgDTO societyPostReplyMsgDTO, PageRequest pageRequest);

    /**
     * 消息设置为已读状态
     * @param societyPostReplyMsgVO 消息体
     * @return 操作结果
     */
    String setReadedMsg(SocietyPostReplyMsgVO societyPostReplyMsgVO);

    /**
     * 删除重复的帖子点赞消息
     * @param societyPostReplyMsgVO 消息体
     * @return  操作结果
     */
    String deleteSocietyPostReplyMsg(SocietyPostReplyMsgVO societyPostReplyMsgVO);

}
