package com.apec.game.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.LimitedAward;
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
 * Title: 抽奖奖品设置
 *
 * @author hmy  2017/12/20.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
@Data
public class PrizeAward extends BaseModel<Long> {

    private static final long serialVersionUID = 6377345566852240201L;

    /**
     * 是否奖励为积分
     */
    private boolean checkPrizePoint;

    /**
     * 奖品名称
     */
    private String prizeName;

    /**
     * 奖项
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PrizeNobel prizeNobel;

    /**
     * 奖品数量
     */
    private Integer num;

    /**
     * 奖品总数量
     */
    private Integer totalNum;

    /**
     * 中奖率
     */
    private Double winningRate;

    /**
     * 中奖限制数，每人整次限制数量
     */
    private Integer limitedAward;

    /**
     * 奖品图片
     */
    private String imageUrl;

    /**
     * 备注
     */
    private String remark;

    /**
     * 抽奖活动id
     */
    private Long lotteryId;


}
