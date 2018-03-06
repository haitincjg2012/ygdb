package com.apec.society.model;

import com.apec.framework.mongodb.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by hmy on 2017/10/30.
 * 贴字上传的图片
 * @author hmy
 */
@Getter
@Setter
@Document
public class SocietyPostImages extends BaseModel<Long> {

    /**
     * 图片地址
     */
    private String imgUrl;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 帖子id
     */
    private Long societyPostId;



}
