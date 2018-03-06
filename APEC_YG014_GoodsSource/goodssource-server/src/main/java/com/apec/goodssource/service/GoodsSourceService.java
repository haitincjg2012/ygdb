package com.apec.goodssource.service;

import com.apec.framework.common.PageDTO;
import com.apec.goodssource.dto.GoodsSourceDTO;
import com.apec.goodssource.vo.GmcSkuBatchVO;
import com.apec.goodssource.vo.GmcSkuInfoVO;
import org.springframework.data.domain.PageRequest;


/**
 * <p>
 * Title: 货源
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * Date: 2018/1/5 - 14:55
 * </p>
 * @version 1.0
 */
public interface GoodsSourceService {

    /**
     * 添加新的货源
     * @param userId 用户id
     * @return 处理结果码
     */
    String addNewGoodsSourceInfo(GmcSkuBatchVO gmcSkuBatchVO, String userId);

    /**
     * 修改货源
     * @param gmcSkuBatchVO 货源
     * @return
     */
    String updateGoodsSourceInfo(GmcSkuBatchVO gmcSkuBatchVO);

    /**
     * 分页查询
     * @param goodsSourceDTO 查询条件
     * @param pageRequest 分页条件
     * @return 分页结果
     */
    PageDTO<GmcSkuInfoVO> queryGmcSkuInfoPage(GoodsSourceDTO goodsSourceDTO, PageRequest pageRequest);



}
