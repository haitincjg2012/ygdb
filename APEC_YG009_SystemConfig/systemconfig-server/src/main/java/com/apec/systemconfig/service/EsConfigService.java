package com.apec.systemconfig.service;

import com.apec.framework.common.PageDTO;
import com.apec.systemconfig.dto.EsConfigDTO;
import com.apec.systemconfig.vo.EsConfigVO;
import org.springframework.data.domain.PageRequest;

/**
 * Created by wubi on 2017/9/22.
 * @author wubi
 */
public interface EsConfigService {
    /**
     * 新增es配置 在es中新建索引
     * @param esConfigVO esConfigVO
     * @param userId userId
     * @return 处理结果
     */
    String addConfig(EsConfigVO esConfigVO, String userId);

    /**
     * 修改ES配置， 修改在ES中=重建索引+重新映射+数据同步
     *
     * @param esConfigVO esConfigVO
     * @param userId userId
     * @return 处理结果
     */
    String updateConfigForReIndex(EsConfigVO esConfigVO, String userId);

    /**
     * 删除ES配置
     * @param esConfigVO esConfigVO
     * @param userId userId
     * @return 处理结果
     */
    String deleteConfig(EsConfigVO esConfigVO, String userId);

    /**
     * 重建索引job
     * @throws  Throwable Throwable
     */
    void reIndexJob() throws Throwable;

    /**
     * 查询
     *
     * @param pageRequest 分页对象
     * @param dto 查询条件对象
     * @return 分页结果
     */
    PageDTO<EsConfigVO> queryConfigByPage(PageRequest pageRequest, EsConfigDTO dto);

}
