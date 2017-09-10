package com.apec.user.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.PointRuleType;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Title: 用户积分记录
 *
 * @author yirde  2017/6/28.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class UserPointRecord extends BaseModel<Long> {

    private static final long serialVersionUID = 6277891566852123263L;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 产生积分的规则id ruleId
     */
    @Column(name = "rule_id")
    private String ruleId;


    /**
     * 积分来源 标记为：产生此次积分的规则的详细规则 方便查询用
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointRuleType pointRuleType;

    /**
     * 积分变化 得到积分:结算的时候需要关联到vip等级，为正 消费积分:用户兑换商品等。。。为负 默认为null
     */
    private Integer pointsChanged;

    /**
     * 备注
     */
    @Column(length = 2000)
    private String remark;

}
