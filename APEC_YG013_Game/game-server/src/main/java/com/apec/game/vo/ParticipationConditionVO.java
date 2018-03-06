package com.apec.game.vo;

import lombok.Data;

/**
 * Created by hmy on 2017/12/21.
 * 抽奖条件
 * @author hmy
 */
@Data
public class ParticipationConditionVO {

    private Long id;

    /**
     * 参与条件名称
     */
    private String conditionName;

    /**
     * 参与活动所需积分
     */
    private Integer points;

}
