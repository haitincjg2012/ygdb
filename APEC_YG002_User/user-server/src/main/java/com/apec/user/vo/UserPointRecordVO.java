package com.apec.user.vo;

import com.apec.framework.common.enumtype.PointRuleType;
import com.apec.framework.common.enumtype.PointsChangedType;
import com.apec.framework.dto.BaseVO;
import com.apec.framework.rockmq.vo.IMqBody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Title: 用户积分
 *
 * @author yirde  2017/7/4.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPointRecordVO extends BaseVO<Long>  implements IMqBody {

    /**
     * 用户 ID
     */
    private List<Long> userIds;

    /**
     * 积分变化 得到积分:结算的时候需要关联到vip等级，为正 消费积分:用户兑换商品等。。。为负 默认为null
     */
    private Integer pointsChanged;

    /**
     * 该用户的等级
     * 等级规则计算
     */
    private PointRuleType pointRuleType;

    /**
     * 积分变化类型
     */
    private PointsChangedType pointsChangedType;

    private String remark;

}
