package com.apec.game.dto;

import com.apec.framework.common.enumtype.LimitedAward;
import com.apec.framework.common.enumtype.PrizeNobel;
import com.apec.framework.dto.BaseDTO;
import lombok.Data;

/**
 * Created by hmy on 2017/12/21.
 *
 * @author hmy
 */
@Data
public class PrizeAwardDTO extends BaseDTO {

    private Long id;

    /**
     * 是否奖励为积分
     */
    private Boolean checkPrizePoint;

    /**
     * 奖品名称
     */
    private String prizeName;

    /**
     * 奖项
     */
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
     * 中奖限制
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
