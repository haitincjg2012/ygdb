package com.apec.voucher.dto;

import com.apec.framework.common.enumtype.RankingType;
import com.apec.framework.dto.BaseDTO;
import lombok.Data;

import java.util.Date;

/**
 * Created by hmy on 2017/12/19.
 *
 * @author hmy
 */
@Data
public class RankingDTO extends BaseDTO {

    /**
     * 排行榜类型
     */
    private RankingType rankingType;

    /**
     * 是否实时
     */
    private Boolean actualTime;

    /**
     * 上线时间
     */
    private Date startTime;

    /**
     * 下线时间
     */
    private Date endTime;


}
