package com.apec.user.vo;

import com.apec.framework.common.enumtype.UserType;
import lombok.Data;

import java.util.Date;

/**
 * Created by hmy on 2018/2/7.
 *
 * @author hmy
 */
@Data
public class UserActiveInfoVO {

    private Long id;

    private Date createDate;

    /**
     * 登陆名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 头像地址
     */
    private String imgUrl;

    /**
     * 地址
     */
    private String address;

    /**
     * 用户身份
     */
    private UserType userType;

    /**
     * 生成活动图片地址
     */
    private String activeUrl;

    /**
     * 生成活动图片地址时间
     */
    private Date createActiveUrlDate;


}
