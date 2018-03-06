package com.apec.game.vo;

import com.apec.framework.common.enumtype.PrizeNobel;
import com.apec.framework.common.enumtype.UserType;
import lombok.Data;

/**
 * Created by hmy on 2017/12/25.
 *
 * @author hmy
 */
@Data
public class PrizePeopleVO {

    /**
     * 抽奖活动id
     */
    private Long lotteryId;

    /**
     * 抽奖用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 头像地址
     */
    private String imgUrl;

    /**
     * 用户身份
     */
    private UserType userType;

    /**
     * 用户身份
     */
    private String userTypeKey;

    /**
     * 是否中奖
     */
    private Boolean lackPeople;

    /**
     * 中奖奖品名称
     */
    private String prizeAwardName;

    /**
     * 奖项
     */
    private PrizeNobel prizeNobel;

    /**
     * 奖项
     */
    private String prizeNobelKey;

}
