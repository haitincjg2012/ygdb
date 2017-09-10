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
 */
public interface RegionLevelDAO extends BaseDAO<RegionLevel,Long> {

    @Query(value = "select min(level) from region_level ",nativeQuery = true)
    String findHighLevelFromRegionLevel();

    /**
     * 查询表中字段level为level的地区信息
     * @param level
     * @param pageable
     * @return
     */
    Page<RegionLevel> findByLevelOrderByCode(String level, Pageable pageable);

    /**
     * 查询表中字段level为level的地区信息
     * @param level
     * @return
     */
    List<RegionLevel> findByLevelOrderByCode(String level);

    RegionLevel findByCode(String code);

    RegionLevel findFirstByEnableFlagOrderByLevel(EnableFlag enableFlag);
}
