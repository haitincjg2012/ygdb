package com.apec.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by wubi on 2017/10/9.
 * @author wubi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken {
    /**
     * token
     */
    private String token;
    /**
     * 有效时间
     */
    private int expiresIn;
}
