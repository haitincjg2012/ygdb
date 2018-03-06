package com.apec.user.vo;

import lombok.Data;

/**
 * Created by hmy on 2018/1/8.
 *
 * @author hmy
 */
@Data
public class UserOfWmsVO {

    /**
     * 组织ID
     */
    private Long userOrgId;

    /**
     * 果来乐用户ID
     */
    private Long userId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 组织代码
     */
    private String orgCode;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 仓库负责人
     */
    private String manager;

    /**
     * 地址
     */
    private String address;

    /**
     * 详细地址
     */
    private String addressDetail;

    /**
     * 单位
     */
    private String unit;

    /**
     * 库容
     */
    private String capacity;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 镇
     */
    private String town;

    /**
     * 颜值
     */
    private String salt;

    /**
     * 加密后密码
     */
    private String passphrase;

}
