package com.apec.society.model;

import com.apec.framework.mongodb.model.BaseModel;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Title:楼中楼回复
 *
 * @author yirde  2017/10/20.
 */
@Data
@Document
public class SocietyLzlReply extends BaseModel<Long>{

   private static final long serialVersionUID = 1664712834183374161L;

    /**
     * 帖子ID
     */
    private Long societyPostId;

    /**
     * 帖子评论的id(楼层id)
     */
    private Long toFuReplyId;

    /**
     * 回复对象id
     */
    private Long toReplyId;

    /**
     * 回复人ID
     */
    private Long userId;

    /**
     * 	回复对象uid
     */
    private Long toUserId;

    /**
     * 回复内容
     */
    private String content;
}
