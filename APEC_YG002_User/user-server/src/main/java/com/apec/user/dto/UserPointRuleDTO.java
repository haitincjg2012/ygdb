package com.apec.user.dto;

import com.apec.framework.common.enumtype.PointRuleType;
import com.apec.framework.common.enumtype.PointsChangedType;
import com.apec.framework.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by hmy on 2017/7/14.
 * @author hmy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPointRuleDTO extends BaseDTO {

    /**
     * 积分来源 标记为：产生此次积分的规则的详细规则 方便查询用
     */
    private PointRuleType pointRuleType;

    /**
     * 积分变化 得到积分:结算的时候需要关联到vip等级，为正 消费积分:用户兑换商品等。。。为负
     */
    private Integer pointsChanged;

    /**
     * 积分变化类型
     */
    private PointsChangedType pointsChangedType;

    /**
     * 备注
     */
    private String remark;
}
