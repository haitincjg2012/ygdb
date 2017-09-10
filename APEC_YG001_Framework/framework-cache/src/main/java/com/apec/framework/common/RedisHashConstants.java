package com.apec.framework.common;

/**
 * Title: Hash结构的定义
 *
 * @author yirde  2017/7/17.
 */
public interface RedisHashConstants {

    /**
     * Hash Cache 结构中的对象内容 字段
     * eg:
     *      user:用户ID
     *       data:用户相关属性
     *       ...其它业务Key
     */
    String  HASH_OBJCONTENT_CACHE = "data";

    /**
     * User 创建供求的历史纪录
     */
    String HASH_USER_CREATEPRODUCT_HIS = "product_his";

    /**
     * 创建的ProductInfo
     */
    String HASH_USER_CREATEPRODUCT_INFO="product_info";

    /**
     * 下架Product 前缀
     */
    String HASH_PRODUCT_OFF_SELL_PREFIX = "product_off_sell_info";

    /**
     * 用户积分等级\实名状态
     */
    String HASH_USER_CREATEUSERVIEW_INFO = "view_user";

    /**
     * 站内信未读通知
     */
    String HASH_USER_MESSAGE_COUNT = "message_count";

    /**
     * User  前缀
     */
    String HASH_USER_PREFIX = "user:";

    /**
     * User Org  前缀
     */
    String HASH_USER_ORG_PREFIX = "user_org:";

    /**
     * Product 前缀
     */
    String HASH_PRODUCT_PREFIX="product:";

    /**
     * 产品收藏量(HASH_PRODUCT_PREFIX)
     */
    String HASH_PRO_SAVE_NUM = "save_num";

    /**
     * 产品浏览量(HASH_PRODUCT_PREFIX)
     */
    String HASH_PRO_VIEW_NUM = "view_num";

}
