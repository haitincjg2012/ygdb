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
public class WeixinShareVO {
    //需要调用js的接口url
    private String url;
    //JSAPI调用凭证
    private String jsapiTicket;
    //随机字符串
    private String nonceStr;
    //当前时间戳
    private String timestamp;
    //签名
    private String signature;
    //app Id
    private String appid;


}
