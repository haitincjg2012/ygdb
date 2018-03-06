package com.apec.voucher.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.ConditionsType;
import com.apec.framework.common.enumtype.StatisticCategories;
import com.apec.framework.jpa.model.BaseModel;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by hmy on 2017/12/19.
 *
 * @author hmy
 */
@Data
@Entity
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
@Table(name = "condition_ranking")
public class Condition extends BaseModel<Long> {

    private static final long serialVersionUID = -6561460455388065960L;

    /**
     * 所属排行榜id
     */
    private Long rankingId;

    /**
     * 排行榜类型
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConditionsType conditionsType;

    /**
     * 条件数量
     */
    private Integer num;

    /**
     * 统计类别
     */
    private StatisticCategories categories;

    /**
     * 备注
     */
    private String remark;

}
