package com.apec.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by wubi on 2017/10/9.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsapiTicket {
    // 获取到的凭证
    private String ticket;
    // 凭证有效时间，单位：秒
    private int expiresIn;
}
