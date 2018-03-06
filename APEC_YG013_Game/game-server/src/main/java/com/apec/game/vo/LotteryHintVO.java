package com.apec.game.vo;

import com.apec.framework.common.enumtype.ParticipationState;
import lombok.Data;

/**
 * Created by hmy on 2017/12/21.
 *
 * @author hmy
 */
@Data
public class LotteryHintVO {

    private Long id;

    /**
     * 参与状态
     */
    private ParticipationState participationState;

    /**
     * 参与状态
     */
    private String participationStateKey;

    /**
     * 备注
     */
    private String remark;


}
