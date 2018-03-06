package com.apec.systemconfig.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.systemconfig.model.RegionLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by hmy on 2017/7/28.
 * @author hmy
 */
public interface RegionLevelDAO extends BaseDAO<RegionLevel,Long> {

    /**
     * 搜索地区表中最低的层级
     * @return String
     */
    @Query(value = "select min(level) from region_level ",nativeQuery = true)
    String findHighLevelFromRegionLevel();

    /**
     * 查询表中字段level为level的地区信息
     * @param level 层级
     * @param pageable 分页数据
     * @return 地区分页数据结果
     */
    Page<RegionLevel> findByLevelOrderByCode(String level, Pageable pageable);

    /**
     * 查询表中字段level为level的地区信息
     * @param level 地区层级
     * @return 地区信息集合
     */
    List<RegionLevel> findByLevelOrderByCode(String level);

    /**
     * 通过编码查询地区信息
     * @param code 编码
     * @return 地区信息
     */
    RegionLevel findByCode(String code);

    /**
     * 查询该层级第一个地区信息
     * @param enableFlag 状态码
     * @return 结果
     */
    RegionLevel findFirstByEnableFlagOrderByLevel(EnableFlag enableFlag);
}
