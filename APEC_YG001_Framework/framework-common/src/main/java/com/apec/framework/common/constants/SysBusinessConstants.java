package com.apec.framework.common.constants;

/**
 * Title: 系统业务 控制/规则 常量
 *
 * @author yirde  2017/7/17.
 */
public interface SysBusinessConstants {

    /**
     * 未实名认证，发布供求的数量限定的redis key前缀, 实名认证完毕，移除掉该Redis key
     */
    String PREFIX_CREATEPRODUCT_NUM = "product_num";

    /**
     * 限定：未实名认证，只能发布5条供求
     */
    Integer CREATE_PRODUCT_NUM = 100;

    /**
     * 验证码的长度
     */
    Integer CAPTCHA_LENGTH = 4;

    /**
     * 验证码有效期 10分钟
     */
    int CAPTCHA_TIMEOUT = 10;

}
