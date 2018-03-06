package com.apec.user.vo;

import com.apec.framework.common.enumtype.UserLevel;
import lombok.Data;

import java.util.Date;

/**
 * Created by hmy on 2017/12/18.
 * 用户等级对象
 * @author hmy
 */
@Data
public class UserLevelRuleVO {

    private Long id;

    private Date createDate;

    /**
     *  用户等级
     */
    private UserLevel userLevel;

    /**
     * 等级头衔
     */
    private String userLevelName;

    /**
     *  积分
     */
    private Integer point;

    /**
     * 等级图片url
     */
    private String url;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否为禁用状态
     */
    private Boolean frezzing;

}
