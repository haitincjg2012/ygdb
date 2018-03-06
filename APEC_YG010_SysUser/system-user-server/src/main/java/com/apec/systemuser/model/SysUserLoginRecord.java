package com.apec.systemuser.model;

import com.apec.framework.common.Constants;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 对应数据库中user_login表
 * @author Administrator
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_user_login_record")
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.IDENTITY)
public class SysUserLoginRecord extends BaseModel<Long>
{

    private static final long serialVersionUID = 6364667460550764857L;

    /**
     * 用户id
     */
    @Column(name = "USER_ID")
    private String userId;

    /**
     * 最后登陆时间
     */
    @Column(name = "LAST_LOGIN_TIME")
    private Date lastLoginTime;

    /**
     * sessionID
     */
    @Column(name = "ACCESS_TOKEN")
    private String accessToken;

}
