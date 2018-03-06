package com.apec.goodssource.dto;

import com.apec.framework.dto.BaseDTO;
import lombok.Data;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Date:  -
 * </p>
 *
 * @author yirde <532186767@qq.com>
 * @version 1.0
 */
@Data
public class GoodsSourceDTO extends BaseDTO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * OrgId 用户组织ID
     */
    private Long  userOrgId;

    /**
     * 所在地区
     */
    private String  address;

    /**
     * 品种
     */
    private String category;

    /**
     * 规格
     */
    private String spec;

}
