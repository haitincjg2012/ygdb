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
    MESSAGE_TAG("MESSAGE_TAG","http://yg-message-service/message/addMessage");

    private final String key;

    private final String tagUrl;

    private MqTag(String key,String tagUrl) {
        this.key = key;
        this.tagUrl = tagUrl;
    }
    public String getKey() {
        return key;
    }

    public String getTagUrl() {
        return tagUrl;
    }


}
