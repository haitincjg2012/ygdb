package com.apec.goodssource.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.goodssource.model.GmcSkuInfo;

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
 * Company: 信云科技有限公司
 * </p>
 * <p>
 * Date:  -
 * </p>
 *
 * @author yirde <532186767@qq.com>
 * @version 1.0
 */
public interface GmcSkuInfoDAO extends BaseDAO<GmcSkuInfo, Long> {

    /**
     * 根据ID和状态查询
     * @param id 主键Id
     * @param flag 标记
     * @return
     */
    GmcSkuInfo findByIdAndEnableFlag(Long id, EnableFlag flag);

}
