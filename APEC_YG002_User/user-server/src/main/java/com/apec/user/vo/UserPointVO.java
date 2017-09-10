package com.apec.user.vo;

import com.apec.framework.common.enumtype.UserLevel;
import lombok.Data;

import java.util.Date;

/**
 *
 * Created by hmy on 2017/7/5.
 */
@Data
public class UserPointVO {

    private Long id;

//    private User user;
    /**
     * 用户ID
     */
    private String userName;

    /**
     * 该用户总的可用积分
     */
    private Integer availablePoints;

    /**
     * 该用户的等级
     * 等级规则计算每日凌晨1:00计算
     */
    private UserLevel userLevel = UserLevel.QT;

    private String userLevelKey;

    /**
     * 最近一个消费积分的时间
     */
    private Date lastConsumeTime;


}
