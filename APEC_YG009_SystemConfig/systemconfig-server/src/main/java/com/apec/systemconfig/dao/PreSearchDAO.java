package com.apec.systemconfig.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.enumtype.SearchType;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.systemconfig.model.PreSearch;

import java.util.List;

/**
 * Created by hmy on 2017/7/31.
 * @author hmy
 */
public interface PreSearchDAO extends BaseDAO<PreSearch,Long> {

    /**
     * 查询对应的搜索数据
     * @param searchType searchType
     * @param enableFlag enableFlag
     * @return  搜索数据列表
     */
    List<PreSearch> findBySearchTypeAndEnableFlag(SearchType searchType, EnableFlag enableFlag);

    /**
     * 查询第sort个该类型的搜索数据
     * @param searchType 搜索类型
     * @param enableFlag 状态码
     * @param sort 顺序
     * @return 搜索数据结果
     */
    PreSearch findBySearchTypeAndEnableFlagAndSort(SearchType searchType, EnableFlag enableFlag,Integer sort);

    /**
     * 统计该类型的搜索数据的个数
     * @param searchType 搜索类型
     * @param enableFlag 状态码
     * @return 个数
     */
    Integer countBySearchTypeAndEnableFlag(SearchType searchType, EnableFlag enableFlag);

}
