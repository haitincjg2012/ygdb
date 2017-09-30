package com.apec.systemconfig.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.systemconfig.model.EsConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by wubi on 2017/9/22.
 */
public interface EsConfigDao extends BaseDAO<EsConfig, Long> {
    /**
     * 查询所有正在使用的es配置
     *
     * @param status
     * @param enableFlag
     * @return
     */
    List<EsConfig> findByStatusAndEnableFlag(String status, EnableFlag enableFlag);

    /**
     * 查询所有的配置信息
     *
     * @param enableFlag
     * @param pageable
     * @return
     */
    Page<EsConfig> findByEnableFlag(EnableFlag enableFlag, Pageable pageable);
}
