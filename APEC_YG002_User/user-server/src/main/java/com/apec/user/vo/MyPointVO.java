package com.apec.user.vo;

import com.apec.framework.common.enumtype.UserLevel;
import lombok.Data;

/**
 * Created by hmy on 2017/12/27.
 *
 * @author hmy
 */
@Data
public class MyPointVO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 该用户总的可用积分
     */
    private Integer availablePoints;

    /**
     * 该用户的等级
     * 等级规则计算每日凌晨1:00计算
     */
    private UserLevel userLevel;

    private String userLevelKey;

    /**
     * 到达下一个等级所需的积分
     */
    private Integer lastLevelPoints;

    /**
     * 该用户的下一个积分等级
     * 等级规则计算每日凌晨1:00计算
     */
    private UserLevel lastUserLevel;

    private String lastUserLevelKey;

}
