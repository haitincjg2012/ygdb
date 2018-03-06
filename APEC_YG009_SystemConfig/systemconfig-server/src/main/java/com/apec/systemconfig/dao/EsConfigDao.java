package com.apec.systemconfig.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.systemconfig.model.EsConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by wubi on 2017/9/22.
 * @author wubi
 */
public interface EsConfigDao extends BaseDAO<EsConfig, Long> {
    /**
     * 查询所有正在使用的es配置
     *
     * @param status 状态
     * @param enableFlag 状态码
     * @return es配置
     */
    List<EsConfig> findByStatusAndEnableFlag(String status, EnableFlag enableFlag);

    /**
     * 查询所有的配置信息
     * @param enableFlag 状态码
     * @param pageable 分页对象
     * @return 所有的配置分页信息
     */
    Page<EsConfig> findByEnableFlag(EnableFlag enableFlag, Pageable pageable);
}
