package com.apec.society.vo;

import com.apec.framework.dto.BaseVO;
import lombok.Data;

/**
 * Created by hmy on 2017/10/30.
 *
 * @author hmy
 */
@Data
public class SocietyPostImagesVO extends BaseVO<Long> {

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
