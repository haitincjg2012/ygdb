package com.apec.user.dto;

import com.apec.framework.dto.BaseDTO;
import com.apec.user.vo.UserVO;
import lombok.Data;

import java.util.Date;

/**
 * Created by hmy on 2017/7/18.
 */
@Data
public class UserAuthRecordDTO extends BaseDTO {
    /**
     * 用户
     */
    private UserVO userVO;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 第一张正面图片
     */
    private String imgOneURL;

    /**
     *  第二张反面图
     */
    private String imgTwoURL;

    /**
     * Y 通过  N 驳回
     */
    private String success;

    /**
     * 审核人
     */
    private String  reviewEmp;

    /**
     * 审核时间
     */
    private Date reviewDate;

    /**
     * 备注
     */
    private String remark;
}
