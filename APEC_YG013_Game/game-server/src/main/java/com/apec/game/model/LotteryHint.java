package com.apec.game.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.LotteryFormType;
import com.apec.framework.common.enumtype.ParticipationState;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

/**
 * Title: 抽奖提示
 *
 * @author hmy  2017/12/20.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
@Data
public class LotteryHint extends BaseModel<Long> {

    private static final long serialVersionUID = 6377345566852240201L;

    /**
     * 参与状态
     */
    private ParticipationState participationState;

    /**
     * 备注
     */
    private String remark;

    /**
     * 抽奖活动id
     */
    private Long lotteryId;


}
