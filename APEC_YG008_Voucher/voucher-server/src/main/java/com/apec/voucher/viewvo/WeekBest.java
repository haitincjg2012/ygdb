package com.apec.voucher.viewvo;

import lombok.Data;

import java.util.Date;

/**
 * Created by hmy on 2017/12/7.
 * 每周上榜的用户信息
 * @author hmy
 */
@Data
public class WeekBest {

    /**
     * 统计时间
     */
    private Date createDate;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String name;

    /**
     * 用户头像
     */
    private String imageUrl;

    /**
     * 用户身份
     */
    private String userType;

    /**
     * 用户所在组织号
     */
    private Long userOrgId;

    /**
     * 上传总次数
     */
    private Integer time;

    /**
     * 上传总重量
     */
    private Double sumWeight;



}
