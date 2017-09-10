package com.apec.user.vo;

import com.apec.framework.common.enums.Source;
import com.apec.framework.dto.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Title: 登陆记录
 *
 * @author yirde  2017/7/12.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRecordVO  extends BaseVO<Long>{

    /**
     * 用户
     */
    private Long userId;

    /**
     * 来源
     */
    private Source source;

    /**
     * 是否成功登陆
     */
    private boolean success;

    /**
     * 登陆凭证
     */
    private String credential;

    /**
     * 登陆的IP地址
     */
    private String ipAdress;

}
