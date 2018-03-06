package com.apec.game.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.ParticipationNumType;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;

/**
 * Title: 抽奖条件
 *
 * @author hmy  2017/12/20.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
@Data
public class ActiveRule extends BaseModel<Long> {

    private static final long serialVersionUID = 6377345566852240201L;

    /**
     * 参与条件id
     */
    private Long conditionId;

    /**
     * 参与次数类型
     */
    private ParticipationNumType participationNumType;

    /**
     * 参与活动所需积分
     */
    private Integer points;

    /**
     * 参与活动送积分（未中奖也会给予一定的奖励）
     */
    private Integer participationPoints;

    /**
     * 是否仅送给未中奖的用户
     */
    private boolean checkUnPrize;

    /**
     * 抽奖活动id
     */
    private Long lotteryId;


}
