package com.apec.framework.mongodb.vo;

import com.apec.framework.common.enumtype.EnableFlag;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Title:
 *
 * @author yirde  2017/10/20.
 */
@Data
public class BaseVO<PK extends Serializable> {

    private static final long serialVersionUID = -2477722490139503211L;

    /**
     * ID
     */
    private Long id;

    /**
     * 状态控制
     */
    private EnableFlag enableFlag;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改时间
     */
    private Date lastUpdateDate;


}
