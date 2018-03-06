package com.apec.game.dto;

import com.apec.framework.dto.BaseDTO;
import lombok.Data;

import java.util.Date;

/**
 * Created by hmy on 2017/12/21.
 *
 * @author hmy
 */
@Data
public class LotteryDTO extends BaseDTO {

    /**
     * 抽奖名称
     */
    private String lotteryName;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;


}
