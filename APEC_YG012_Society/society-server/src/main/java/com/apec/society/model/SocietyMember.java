package com.apec.society.model;

import com.apec.framework.mongodb.model.BaseModel;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Title: 圈子成员表
 *
 * @author yirde  2017/10/20.
 */
@Data
@Document
public class SocietyMember extends BaseModel<Long> {

    private static final long serialVersionUID = -6664475534780692499L;

    /**
     * 圈子ID
     */
    private Long  societyId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 标签
     */
    private String label;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 活跃度
     */
    private Integer activity;

}
