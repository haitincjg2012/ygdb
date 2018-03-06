package com.apec.voucher.vo;

import lombok.Data;

/**
 * Created by hmy on 2018/2/6.
 *
 * @author hmy
 */
@Data
public class StaticVoucherInfoVO {

    /**
     * 创建日期
     * */
    private String createDate;

    /**
     * 交收单总重量（斤）
     * */
    private String totalNumber;

    /**
     * 合计金额（元）
     * */
    private String totalAmount;

    /**
     * 交收单条数
     */
    private String sumCount;

    /**
     * 上传人
     * */
    private String userName;

    /**
     * 用户身份
     */
    private String userType;

    /**
     * 用户手机号
     */
    private String mobile;

    /**
     * 用户id
     * */
    private String userId;

}
