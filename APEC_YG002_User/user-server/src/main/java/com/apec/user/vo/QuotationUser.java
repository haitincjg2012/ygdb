package com.apec.user.vo;

import com.apec.framework.common.enumtype.UserType;
import lombok.Data;

/**
 * Created by hmy on 2017/12/13.
 * 参与行情竞猜的用户的信息
 * @author hmy
 */
@Data
public class QuotationUser {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String name;

    /**
     * 用户头像
     */
    private String imgUrl;

    /**
     * 用户身份
     */
    private UserType userType;

    /**
     * 用户身份
     */
    private String userTypeKey;

    /**
     * 用户参与的类型,涨/跌 ,Y/N
     */
    private String auditState;

    /**
     * 参与的行情竞猜id
     */
    private Long quotationId;


}
