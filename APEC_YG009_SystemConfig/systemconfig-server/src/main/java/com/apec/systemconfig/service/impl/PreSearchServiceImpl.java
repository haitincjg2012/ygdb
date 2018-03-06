package com.apec.systemconfig.service.impl;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.enumtype.SearchType;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.systemconfig.dao.PreSearchDAO;
import com.apec.systemconfig.model.PreSearch;
import com.apec.systemconfig.service.PreSearchService;
import com.apec.systemconfig.vo.PreSearchVO;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by hmy on 2017/7/31.
 * @author hmy
 */
@Service
public class PreSearchServiceImpl implements PreSearchService {

    @Autowired
    private PreSearchDAO preSearchDAO;


    @Override
    public List<PreSearchVO> listRMPreSearch() {
        List<PreSearchVO> list = new ArrayList<>();
        Iterable<PreSearch> iterable = preSearchDAO.findBySearchTypeAndEnableFlag(SearchType.RMSS, EnableFlag.Y);
        Iterator<PreSearch> it = iterable.iterator();
        while(it.hasNext()){
            PreSearch preSearch = it.next();
            PreSearchVO preSearchVO = new PreSearchVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(preSearch,preSearchVO);
            list.add(preSearchVO);
        }
        return list;
    }

    /**
     * 随机查询一个预制搜索关键字
     */
    @Override
    public PreSearchVO getYZPreSearch() {
        Integer l = preSearchDAO.countBySearchTypeAndEnableFlag(SearchType.YZSS,EnableFlag.Y);
        Random random = new Random();
        Integer sort = random.nextInt(l);
        PreSearch preSearch = preSearchDAO.findBySearchTypeAndEnableFlagAndSort(SearchType.YZSS,EnableFlag.Y,sort);
        PreSearchVO preSearchVO = new PreSearchVO();
        BeanUtil.copyPropertiesIgnoreNullFilds(preSearch,preSearchVO);
        return preSearchVO;
    }
}
