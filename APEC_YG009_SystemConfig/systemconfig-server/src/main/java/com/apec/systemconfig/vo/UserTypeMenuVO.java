package com.apec.systemconfig.vo;

import com.apec.framework.common.enumtype.UserType;
import lombok.Data;

/**
 * Created by hmy on 2017/12/1.
 *
 * @author hmy
 */
@Data
public class UserTypeMenuVO {

    private Long id;

    /**
     * 用户身份
     */
    private UserType userType;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单对应的字段名称
     */
    private String nameStr;
}
