package com.apec.game.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.PrizeNobel;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Title: 抽奖用户记录
 *
 * @author hmy  2017/12/20.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
@Data
public class PrizePeople extends BaseModel<Long> {

    private static final long serialVersionUID = 6377345566852240201L;

    /**
     * 抽奖活动id
     */
    private Long lotteryId;

    /**
     * 抽奖用户
     */
    private Long userId;

    /**
     * 是否中奖
     */
    private boolean lackPeople;

    /**
     * 中奖奖品id
     */
    private Long prizeAwardId;

    /**
     * 中奖奖品名称
     */
    private String prizeAwardName;

    /**
     * 奖项
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PrizeNobel prizeNobel;


}
