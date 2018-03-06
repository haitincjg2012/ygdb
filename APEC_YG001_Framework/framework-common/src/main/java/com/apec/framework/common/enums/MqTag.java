package com.apec.framework.common.enums;

/**
 * Title:MQ TAG
 *
 * @author yirde  2017/7/5.
 */
public enum MqTag implements BaseEnum {

    //===============================User Point ====================================/
    USER_POINT_TAG_INIT_POINT("USER_POINT_INIT","http://yg-user-service/userpoint/addNewPoint"),
    USER_POINT_TAG_SUBSCRI_POINT("USER_POINT_SUB","http://yg-user-service/userpoint/reducePoint"),

    //======================================Product  ======================================/
    PRODUCT_SAVE_NEW("PRODUCT_SAVE_NEW","http://yg-product-service/product/saveProduct"),
    PRODUCT_UPDATE("PRODUCT_UPDATE","http://yg-product-service/product/updateProduct"),

    //======================================Message ======================================/
    MESSAGE_TAG("MESSAGE_TAG","http://yg-message-service/message/addMessage"),

    /**
     * 发送短信/彩信
     */
    SMS_MESSAGE_TAG("SMS_MESSAGE_TAG","http://yg-message-service/smsMessage/sendSmsMessage"),

    //======================================cms ======================================/
    ARTICLE_MSG("ARTICLE_MSG","http://yg-cms-service/comment/addArticleMsg"),

    //======================================society ======================================/
    /**
     * 添加通知消息
     */
    REPLY_MSG("REPLY_MSG","http://yg-society-service/societyPostReplyMsg/addSocietyPostReplyMsg"),

    /**
     * 删除相应的通知消息
     */
    DELETE_REPLY_MSG("DELETE_REPLY_MSG","http://yg-society-service/societyPostReplyMsg/deleteSocietyPostReplyMsg");

    private final String key;

    private final String tagUrl;

    MqTag(String key,String tagUrl) {
        this.key = key;
        this.tagUrl = tagUrl;
    }
    @Override
    public String getKey() {
        return key;
    }

    public String getTagUrl() {
        return tagUrl;
    }


}
