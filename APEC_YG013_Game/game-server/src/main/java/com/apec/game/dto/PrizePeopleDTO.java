package com.apec.game.dto;

import com.apec.framework.common.enumtype.PrizeNobel;
import com.apec.framework.dto.BaseDTO;
import lombok.Data;

/**
 * Created by hmy on 2017/12/25.
 *
 * @author hmy
 */
@Data
public class PrizePeopleDTO extends BaseDTO {

    /**
     * 抽奖活动id
     */
    private Long lotteryId;

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

}
