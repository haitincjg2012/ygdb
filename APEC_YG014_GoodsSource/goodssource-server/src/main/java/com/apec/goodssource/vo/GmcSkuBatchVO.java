package com.apec.goodssource.vo;

import lombok.Data;

import java.util.List;

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
 * </p>
 * <p>
 * Date:  -
 * </p>
 * @version 1.0
 */
@Data
public class GmcSkuBatchVO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * OrgId 用户组织ID
     */
    private Long  userOrgId;


    /**
     * 是否执行修改
     */
    private Boolean  execUpdate;

    /**
     * List
     */
    private List<GmcSkuInfoVO> listSkuInfo;


}
