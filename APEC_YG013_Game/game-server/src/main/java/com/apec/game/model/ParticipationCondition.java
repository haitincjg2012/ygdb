package com.apec.game.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.ParticipationState;
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
public class ParticipationCondition extends BaseModel<Long> {

    private static final long serialVersionUID = 6377345566852240201L;

    /**
     * 参与条件名称
     */
    private String conditionName;

    /**
     * 参与活动所需积分
     */
    private Integer points;



}
