package com.apec.systemuser.vo;

import com.apec.framework.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
/**
 * Title:
 *
 * @author wubi  2017/7/31.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserVO extends BaseDTO
{
    private Long id;
    
    private String name;

    private String loginName;

    private String mobile;

    private String password;

    private String email;

    private Date lastLoginTime;
    
    private String needChangePassword;

    private String source;

    private String userRoleNo;

    private String newPW;
    
    private String roleName;

}
