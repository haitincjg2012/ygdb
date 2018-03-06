package com.apec.user.vo;

import com.apec.framework.common.enumtype.UserLevel;
import com.apec.framework.common.enumtype.UserType;
import lombok.Data;

import java.util.Date;

/**
 * @author hmy
 * Created by hmy on 2017/7/5.
 */
@Data
public class UserPointVO {

    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 用户身份
     */
    private UserType userType;

    /**
     * 用户身份KEY
     */
    private String userTypeKey;

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
     * 最近一个消费积分的时间
     */
    private Date lastConsumeTime;


}
