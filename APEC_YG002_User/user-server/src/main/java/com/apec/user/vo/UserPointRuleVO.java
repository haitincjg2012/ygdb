package com.apec.user.vo;

import com.apec.framework.common.enumtype.PointRuleType;
import com.apec.framework.common.enumtype.PointsChangedType;
import com.apec.framework.dto.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * Created by hmy on 2017/7/13.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPointRuleVO{

    private Long id;

    /**
     * 积分来源 标记为：产生此次积分的规则的详细规则 方便查询用
     */
    private PointRuleType pointRuleType;

    private String pointRuleTypeKey;

    /**
     * 积分变化 得到积分:结算的时候需要关联到vip等级，为正 消费积分:用户兑换商品等。。。为负
     */
    private Integer pointsChanged;

    /**
     * 积分变化类型
     */
    private PointsChangedType pointsChangedType;

    private String pointsChangedTypeKey;

    /**
     * 备注
     */
    private String remark;

}
