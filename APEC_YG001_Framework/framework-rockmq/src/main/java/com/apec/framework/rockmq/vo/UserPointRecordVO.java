package com.apec.framework.rockmq.vo;

import com.apec.framework.common.enumtype.PointRuleType;
import com.apec.framework.common.enumtype.PointsChangedType;
import com.apec.framework.dto.BaseVO;

import java.util.List;

/**
 * Title: 用户积分
 *
 * @author yirde  2017/7/4.
 */
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
     * 等级规则计算每日凌晨1:00计算
     */
    private PointRuleType pointRuleType;

    /**
     * 积分变化备注
     */
    private String remark;

    /**
     * 积分变化类型
     */
    private PointsChangedType pointsChangedType;

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public Integer getPointsChanged() {
        return pointsChanged;
    }

    public void setPointsChanged(Integer pointsChanged) {
        this.pointsChanged = pointsChanged;
    }

    public PointRuleType getPointRuleType() {
        return pointRuleType;
    }

    public void setPointRuleType(PointRuleType pointRuleType) {
        this.pointRuleType = pointRuleType;
    }

    public PointsChangedType getPointsChangedType() {
        return pointsChangedType;
    }

    public void setPointsChangedType(PointsChangedType pointsChangedType) {
        this.pointsChangedType = pointsChangedType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString(){
        return super.toString();
    }

}
