package com.apec.systemconfig.service;

import com.apec.framework.common.PageDTO;
import com.apec.systemconfig.dto.EsConfigDTO;
import com.apec.systemconfig.vo.EsConfigVO;
import org.springframework.data.domain.PageRequest;

/**
 * Created by wubi on 2017/9/22.
 */
public interface EsConfigService {
    /**
     * 新增es配置 在es中新建索引
     *
     * @param esConfigVO
     * @return
     */
    String addConfig(EsConfigVO esConfigVO, String userId);

    /**
     * 修改ES配置， 修改在ES中=重建索引+重新映射+数据同步
     *
     * @param esConfigVO
     * @return
     */
    String updateConfigForReIndex(EsConfigVO esConfigVO, String userId);

    /**
     * 删除ES配置
     *
     * @param esConfigVO
     * @return
     */
    String deleteConfig(EsConfigVO esConfigVO, String userId);

    /**
     * 重建索引job
     */
    void reIndexJob() throws Throwable;

    /**
     * 查询
     *
     * @param pageRequest
     * @param dto
     * @return
     */
    PageDTO<EsConfigVO> queryConfigByPage(PageRequest pageRequest, EsConfigDTO dto);

}
