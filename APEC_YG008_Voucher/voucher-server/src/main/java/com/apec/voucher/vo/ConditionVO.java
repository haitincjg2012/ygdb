package com.apec.voucher.vo;

import com.apec.framework.common.enumtype.ConditionsType;
import com.apec.framework.common.enumtype.StatisticCategories;
import lombok.Data;

import java.util.Date;

/**
 * Created by hmy on 2017/12/19.
 *
 * @author hmy
 */
@Data
public class ConditionVO {

    private Long id;

    private Date createDate;

    /**
     * 所属排行榜id
     */
    private Long rankingId;

    /**
     * 排行榜类型
     */
    private ConditionsType conditionsType;

    /**
     * 排行榜类型
     */
    private String conditionsTypeKey;

    /**
     * 条件数量
     */
    private Integer num;

    /**
     * 统计类别
     */
    private StatisticCategories categories;

    /**
     * 统计类别
     */
    private String categoriesKey;

    /**
     * 备注
     */
    private String remark;

}
