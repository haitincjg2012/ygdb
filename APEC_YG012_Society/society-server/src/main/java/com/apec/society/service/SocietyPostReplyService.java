package com.apec.society.service;

import com.apec.framework.common.PageDTO;
import com.apec.society.dto.SocietyPostReplyDTO;
import com.apec.society.view.SocietyPostReplyViewVO;
import com.apec.society.vo.SocietyPostReplyVO;
import com.apec.society.vo.SocietyPostVO;
import org.springframework.data.domain.PageRequest;

/**
 * Created by hmy on 2017/10/30.
 *
 * @author hmy
 */
public interface SocietyPostReplyService {

    /**
     * 增加帖子回复
     * @param societyPostReplyVO 回复对象
     * @return 操作结果
     */
    String addSocietyPostReply(SocietyPostReplyVO societyPostReplyVO);

    /**
     * 增加帖子回复
     * @param societyPostReplyVO 回复对象
     * @return 操作结果
     */
    String deleteSocietyPostReply(SocietyPostReplyVO societyPostReplyVO);

    /**
     * 查询回复量最多的评论
     * @param societyPostVO 帖子信息
     * @param userId 操作用户id
     * @return 回复量最多的评论
     */
    SocietyPostReplyViewVO findMaxReplyNum(SocietyPostVO societyPostVO, String userId);

    /**
     * 分页查询帖子评论
     * @param societyPostReplyDTO 查询条件对象
     * @param pageRequest 分页对象
     * @param userId 操作人用户id
     * @return 分页结果
     */
    PageDTO<SocietyPostReplyViewVO> findSocietyPostReplyPage(SocietyPostReplyDTO societyPostReplyDTO, PageRequest pageRequest,String userId);

    /**
     * 点赞/取消点赞
     * @param societyPostReplyVO 评论对象
     * @param userId 操作用户id
     * @return 操作结果
     */
    String likeSocietyPostOrNot(SocietyPostReplyVO societyPostReplyVO,String userId);

    /**
     * 查询评论
     * @param societyPostReplyVO 评论对象id
     * @param userId 操作用户id
     * @return 评论
     */
    SocietyPostReplyViewVO queryReplyById(SocietyPostReplyVO societyPostReplyVO, String userId);

}
