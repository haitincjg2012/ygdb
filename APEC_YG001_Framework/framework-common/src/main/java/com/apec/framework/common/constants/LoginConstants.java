package com.apec.framework.common.constants;

/**
 * Login Constants
 */
public interface LoginConstants {
    
    /**
     * 校验登陆状态，参数：userNo、token
     */
    String METHOD_VALIDATATOKEN = "validateToken";

    /**
     * token userNo
     */
    String PREFIX_TOKEN_USERNO = "token_userNo_";

    /**
     * Token
     */
    String PREFIX_TOKEN = "token_";

    /**
     * Session ID
     */
    String PREFIX_SESSIONID = "sessionId_";

    /**
     * 登陆失败的次数
     */
    String PREFIX_LOGIN_FAILED = "login_failed_";

    /**
     * 登陆失败次数的过期时间 ： 10分钟
     */
    int LOGIN_FAILED_COUNT_EXPREISE = 10;

    /**
     * 最大运行登陆失败的次数 10次
     */
    int LOGIN_FAILED_MAX_COUNT = 10;

}
