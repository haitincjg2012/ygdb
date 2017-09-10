package com.apec.systemuser.vo;

import com.apec.framework.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by wubi on 2017/8/1.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserLoginRecordVO extends BaseDTO {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 最后登陆时间
     */
    private Date lastLoginTime;

    /**
     * sessionID
     */
    private String accessToken;
}
