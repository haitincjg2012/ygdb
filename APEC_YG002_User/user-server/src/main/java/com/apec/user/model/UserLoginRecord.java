package com.apec.user.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enums.Source;
import com.apec.framework.jpa.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Title: 用户登录记录
 *
 * @author yirde  2017/6/28.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class UserLoginRecord  extends BaseModel<Long>{

    private static final long serialVersionUID = 6277891566853240263L;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 来源
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Source source;

    /**
     * 是否成功登陆
     */
    @Column(nullable = false)
    private boolean success;

    /**
     * 登陆时间
     */
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date loginTime;

    /**
     * 登陆凭证
     */
    private String credential;

    /**
     * 登陆的IP地址
     */
    private String ipAddress;

}
