package com.apec.user.vo;

import com.apec.framework.common.enumtype.PointRuleType;
import com.apec.framework.common.enumtype.PointsChangedType;
import lombok.Data;

import java.util.Date;

/**
 * Created by hmy on 2017/8/3.
 */
@Data
public class UserPointRecordViewVO {

    private Date createDate;

    /**
     * 积分变化 得到积分:结算的时候需要关联到vip等级，为正 消费积分:用户兑换商品等。。。为负 默认为null
     */
    private Integer pointsChanged;

    /**
     * 描述
     */
    private String remark;


}
