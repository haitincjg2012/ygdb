package com.apec.user.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.UserType;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by hmy on 2018/2/7.
 *
 * @author hmy
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class UserActiveInfo extends BaseModel<Long>{

    /**
     * 登陆名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 头像地址
     */
    private String imgUrl;

    /**
     * 地址
     */
    private String address;

    /**
     * 用户身份
     */
    @Enumerated(EnumType.STRING)
    private UserType userType;

    /**
     * 生成活动图片地址
     */
    private String activeUrl;

    /**
     * 生成活动图片地址时间
     */
    private Date createActiveUrlDate;


}
