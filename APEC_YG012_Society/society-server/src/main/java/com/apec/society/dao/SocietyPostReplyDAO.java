package com.apec.society.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.mongodb.dao.BaseDAO;
import com.apec.society.model.SocietyPostReply;

/**
 * Created by hmy on 2017/10/30.
 *
 * @author hmy
 */
public interface SocietyPostReplyDAO extends BaseDAO<SocietyPostReply,Long>{

    /**
     * 查询回复量最多的楼层评论
     * @param societyPostId 帖子id
     * @param enableFlag 状态码
     * @return 一级评论
     */
    SocietyPostReply findFirstBySocietyPostIdAndEnableFlagOrderByReplyNumDesc(Long societyPostId,EnableFlag enableFlag);

    /**
     * 通过id查询帖子信息
     * @param id id
     * @param enableFlag 状态码
     * @return 帖子信息
     */
    SocietyPostReply findByIdAndEnableFlag(Long id, EnableFlag enableFlag);

}
