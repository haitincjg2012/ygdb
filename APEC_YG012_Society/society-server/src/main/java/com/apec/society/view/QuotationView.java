package com.apec.society.view;

import lombok.Data;

import java.util.Date;

/**
 * Created by hmy on 2017/12/6.
 * 行情竞猜信息
 * @author hmy
 */
@Data
public class QuotationView {

    private Long id;

    private Date createDate;

    /**
     * 期号
     */
    private String issue;

    /**
     * 上线时间
     */
    private Date startTime;

    /**
     * 下线时间
     */
    private Date endTime;

    /**
     * 行情竞猜看涨的人数
     */
    private String quotationHigh;

    /**
     * 行情竞猜看跌的人数
     */
    private String quotationLight;

    /**
     * 参与投票的总人数
     */
     private Integer totalUser;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 备注
     */
    private String remark;

    /**
     * 评论量
     */
    private Integer replyCount;

    /**
     * 审核状态，Y为涨，N为跌，0为未审核
     */
    private String auditState;

    /**
     * 进行状态
     */
    private String auditStateStr;

    /**
     * 审核通过时间
     */
    private Date passDate;

    /**
     * 是否看涨
     */
    private Boolean isHigh;

    /**
     * 是否看跌
     */
    private Boolean isLight;

}
