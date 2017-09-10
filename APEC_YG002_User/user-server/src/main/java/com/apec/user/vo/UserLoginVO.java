package com.apec.user.vo;

import com.apec.framework.common.enums.Source;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Title: 用户登录VO
 *
 * @author yirde  2017/7/12.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO {

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 密码
     */
    private String password;

    /**
     * 来源
     */
    private Source source;

    /**
     * IP 地址
     */
    private String ip;

}
