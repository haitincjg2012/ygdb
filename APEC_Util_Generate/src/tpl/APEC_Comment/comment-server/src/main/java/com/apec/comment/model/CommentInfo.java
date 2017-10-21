package com.apec.comment.model;

import com.apec.framework.common.Constants;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;

/**
 * Title: Comment 评论
 * @author yirde  2017/10/16.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class CommentInfo extends BaseModel<Long> {

    private static final long serialVersionUID = 6277831566123244213L;

    /**
     * 文章行情ID
     */
    private Long articleId;

    /**
     * 文章行情标题
     */
    private String articleTitle;

    /**
     *  评论人的ID
     */
    private Long userId;

    /**
     *  评论人的姓名
     */
    private String userName;

     //target_user_id, //评论的目标用户id
     // content, //评论内容
    // likeCount, //该评论被点赞的数量
    // created_at, //创建时间
    // parent_id, //评论的目标id
    // parent_type //评论的目标类型


}
