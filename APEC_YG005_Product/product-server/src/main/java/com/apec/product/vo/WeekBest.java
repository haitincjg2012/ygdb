package com.apec.product.vo;

import com.apec.framework.common.enumtype.UserType;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by hmy on 2017/12/7.
 * 每周上榜的用户信息
 * @author hmy
 */
@Data
public class WeekBest {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String name;

    /**
     * 用户头像
     */
    private String imageUrl;

    /**
     * 用户身份
     */
    private String userType;

    /**
     * 用户所在组织号
     */
    private Long userOrgId;

    /**
     * 上传总次数
     */
    private Integer time;

    /**
     * 统计时间
     */
    private Date createDate;

    private List<Long> userIds;

}
