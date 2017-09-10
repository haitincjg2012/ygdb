package com.apec.systemuser.model;


import com.apec.framework.cache.CacheHashService;
import com.apec.framework.cache.CacheService;
import com.apec.framework.cache.service.RedisHashCacheServiceImpl;
import com.apec.framework.common.Constants;
import com.apec.framework.jpa.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class SysUser extends BaseModel<Long> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -336253842706171643L;

	@Column(name = "name")
    private String name;

    @Column(name = "mobile")
    private String mobile;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "email")
    private String email;

    @Column(name = "last_login_time")
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;
    
    @Column(name = "needchangepassword")
    private String needChangePassword;
    
    @Column(name = "source")
    private String source;
    
    @Column(name = "user_role_no")
    private String userRoleNo;







}
