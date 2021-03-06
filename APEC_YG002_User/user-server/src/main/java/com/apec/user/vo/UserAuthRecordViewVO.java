package com.apec.user.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by hmy on 2017/8/7.
 * @author hmy
 */
@Data
public class UserAuthRecordViewVO {

    private Long id;

    private Date createDate;

    /**
     * 用户真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 身份证
     */
    private String idNumber;

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
     * 备注
     */
    private String remark;

    private Date passDate;


}
