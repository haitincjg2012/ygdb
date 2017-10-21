package com.apec.society.model;

import com.apec.framework.mongodb.model.BaseModel;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Title: 圈子公告
 *
 * @author yirde  2017/10/20.
 */
@Data
@Document
public class SocietyNotice extends BaseModel<Long> {

    private static final long serialVersionUID = 7376067820374540874L;

    /**
     *   圈子ID
     */
    private Long  societyId;

    /**
     * 内容
     */
    private String content;

    /**
     * 图片URL
     */
    private String imageUrl;

    /**
     * 页面路由地址
     */
    private String routerUrl;

}
