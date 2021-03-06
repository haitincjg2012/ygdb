package com.apec.systemconfig.service;

import com.apec.systemconfig.vo.PreSearchVO;

import java.util.List;

/**
 * Created by hmy on 2017/7/31.
 * @author hmy
 */
public interface PreSearchService {

    /**
     * 查询数据库中热门搜索的关键字
     * @return 数据库中热门搜索的关键字
     */
    List<PreSearchVO> listRMPreSearch();

    /**
     * 查询预制搜索关键字
     * @return  预制搜索关键字
     */
    PreSearchVO getYZPreSearch();


}
