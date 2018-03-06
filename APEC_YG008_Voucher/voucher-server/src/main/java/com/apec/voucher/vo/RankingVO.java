package com.apec.voucher.vo;

import com.apec.framework.common.enumtype.RankingType;
import com.apec.voucher.viewvo.WeekBest;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by hmy on 2017/12/19.
 *
 * @author hmy
 */
@Data
public class RankingVO {

    private Long id;

    private Date createDate;

    /**
     * 排行榜类型
     */
    private RankingType rankingType;

    /**
     * 排行榜类型
     */
    private String rankingTypeKey;

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

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private String auditState;

    /**
     * 上榜条件
     */
    private List<ConditionVO> conditions;

    /**
     * 上榜客户
     */
    private List<WeekBest> rankingMans;


}
