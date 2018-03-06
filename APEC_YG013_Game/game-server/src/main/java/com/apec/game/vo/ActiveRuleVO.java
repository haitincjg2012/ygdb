package com.apec.game.vo;

import com.apec.framework.common.enumtype.ParticipationNumType;
import lombok.Data;

/**
 * Created by hmy on 2017/12/21.
 *
 * @author hmy
 */
@Data
public class ActiveRuleVO {

    private Long id;

    /**
     * 参与条件
     */
    private Long conditionId;

    /**
     * 参与条件名称
     */
    private String conditionName;

    /**
     * 参与次数类型
     */
    private ParticipationNumType participationNumType;

    /**
     * 参与次数类型
     */
    private String participationNumTypeKey;

    /**
     * 参与活动所消耗积分
     */
    private Integer points;

    /**
     * 参与活动送积分（未中奖也会给予一定的奖励）
     */
    private Integer participationPoints;

    /**
     * 是否仅送给未中奖的用户
     */
    private Boolean checkUnPrize;

    /**
     * 抽奖活动id
     */
    private Long lotteryId;

}
