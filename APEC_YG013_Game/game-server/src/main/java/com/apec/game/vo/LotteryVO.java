package com.apec.game.vo;

import com.apec.framework.common.enumtype.LotteryFormType;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by hmy on 2017/12/21.
 *
 * @author hmy
 */
@Data
public class LotteryVO {

    private Long id;

    private Date createDate;

    /**
     * 抽奖名称
     */
    private String lotteryName;

    /**
     * 抽奖形式
     */
    private LotteryFormType lotteryFormType;

    /**
     * 抽奖形式
     */
    private String lotteryFormTypeKey;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 上线时间
     */
    private Date startTime;

    /**
     * 下线时间
     */
    private Date endTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 抽奖条件
     */
    private List<LotteryHintVO> lotteryHintVOS;

    /**
     * 参与人数
     */
    private Long totalPeople;

    /**
     * 中奖人数
     */
    private Long lackPeople;

    /**
     * 状态
     */
    private String auditState;

}
