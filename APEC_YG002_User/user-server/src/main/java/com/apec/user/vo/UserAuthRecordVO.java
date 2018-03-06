package com.apec.user.vo;

import com.apec.framework.dto.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by hmy on 2017/7/17.
 * @author hmy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthRecordVO extends BaseVO<Long> {

    /**
     * 用户真实姓名
     */
    private String realName;

    /**
     * 身份证
     */
    private String idNumber;

    /**
     * 用户Id
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
