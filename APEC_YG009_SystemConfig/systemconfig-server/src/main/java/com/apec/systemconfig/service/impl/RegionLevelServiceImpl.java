package com.apec.systemconfig.service.impl;

import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.util.BeanUtil;
import com.apec.systemconfig.dao.RegionLevelDAO;
import com.apec.systemconfig.model.QRegionLevel;
import com.apec.systemconfig.model.RegionLevel;
import com.apec.systemconfig.service.RegionLevelService;
import com.apec.systemconfig.vo.RegionLevelVO;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hmy on 2017/7/28.
 * @author hmy
 */
@Service
public class RegionLevelServiceImpl implements RegionLevelService {

    @Autowired
    private RegionLevelDAO dao;

    @Override
    public List<RegionLevelVO> selectAll(RegionLevelVO vo)
    {
        List<RegionLevelVO> list = new ArrayList<>();
        if(StringUtils.isBlank(vo.getParentId())){
            //查询最大等级的code
            RegionLevel regionLevel = dao.findFirstByEnableFlagOrderByLevel(EnableFlag.Y);
            if(regionLevel == null){
                return list;
            }
            vo = new RegionLevelVO();
            vo.setParentId(regionLevel.getCode());
        }
        Iterable<RegionLevel> iterable = dao.findAll(getInputCondition(vo));
        Iterator<RegionLevel> it = iterable.iterator();
        while (it.hasNext())
        {
            RegionLevel entity = it.next();
            RegionLevelVO dto = new RegionLevelVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(entity, dto);
            list.add(dto);
        }
        return list;
    }

    @Override
    public List<RegionLevelVO> getNeedRegionLevel(List<String> regionCodes) {
        List<RegionLevelVO> list = new ArrayList<>();
        if(regionCodes != null && regionCodes.size() > 0){
            for(String regionCode:regionCodes){
                RegionLevel regionLevel = dao.findByCode(regionCode);
                RegionLevelVO regionLevelVO = new RegionLevelVO();
                regionLevelVO.setParentId(regionLevel.getCode());
                List<RegionLevelVO> regionLevelVOS = this.selectAll(regionLevelVO);
                regionLevelVO.setRegionLevelVOS(regionLevelVOS);
                list.add(regionLevelVO);
            }
        }else{
            String highLevel = dao.findHighLevelFromRegionLevel();
            if(StringUtils.isBlank(highLevel)){
                return null;
            }
            List<RegionLevel> regionLevels = dao.findByLevelOrderByCode(highLevel);
            for(RegionLevel regionLevel:regionLevels){
                RegionLevelVO regionLevelVO = new RegionLevelVO();
                regionLevelVO.setParentId(regionLevel.getCode());
                List<RegionLevelVO> regionLevelVOS = selectAll(regionLevelVO);
                //查询该地区下的所有直辖管理地区
                BeanUtil.copyPropertiesIgnoreNullFilds(regionLevel,regionLevelVO);
                regionLevelVO.setRegionLevelVOS(regionLevelVOS);
                list.add(regionLevelVO);
            }
        }
        return list;
    }

    @Override
    public PageDTO<RegionLevelVO> pageRegionLevel(RegionLevelVO vo, PageRequest pageRequest) {
        Page<RegionLevel> page = dao.findAll(getInputCondition(vo),pageRequest);
        PageDTO<RegionLevelVO> result = new PageDTO<>();
        List<RegionLevelVO> list = new ArrayList<>();
        for(RegionLevel regionLevel:page){
            RegionLevelVO regionLevelVO = new RegionLevelVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(regionLevel,regionLevelVO);
            list.add(regionLevelVO);
        }
        result.setNumber(page.getNumber()+1);
        result.setTotalElements(page.getTotalElements());
        result.setTotalPages(page.getTotalPages());
        result.setRows(list);
        return result;
    }

    /**
     * 查询数据库中存储的最顶级的地区分页信息
     */
    @Override
    public PageDTO<RegionLevelVO> pagemaxRegionLevel(PageRequest pageRequest) {
        String highLevel = dao.findHighLevelFromRegionLevel();
        PageDTO<RegionLevelVO> result = new PageDTO<>();
        if(StringUtils.isBlank(highLevel)){
            return result;
        }
        Page<RegionLevel> page = dao.findByLevelOrderByCode(highLevel,pageRequest);
        List<RegionLevelVO> list = new ArrayList<>();
        for(RegionLevel regionLevel:page){
            RegionLevelVO regionLevelVO = new RegionLevelVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(regionLevel,regionLevelVO);
            list.add(regionLevelVO);
        }
        result.setNumber(page.getNumber()+1);
        result.setTotalElements(page.getTotalElements());
        result.setTotalPages(page.getTotalPages());
        result.setRows(list);
        return result;
    }

    @Override
    public String getAddressInfo(String addId) {
        StringBuffer address = new StringBuffer();
        List<String> list = new ArrayList<>();
        while(!StringUtils.equals(addId, Constants.ZERO)){
            //查询地区信息，从低往高查
            RegionLevel regionLevel = dao.findByCode(addId);
            if(regionLevel == null){
                break;
            }
            list.add(regionLevel.getName());
            addId = regionLevel.getParentId();
        }
        if(!CollectionUtils.isEmpty(list)){
            for(int i = list.size() - 1 ; i >= 0; i--){
                address.append(list.get(i));
                address.append("|");
            }
        }
        return address.toString();
    }

    private Predicate getInputCondition(RegionLevelVO vo)
    {
        List<BooleanExpression> predicates = new ArrayList<>();
        if(null != vo)
        {
            if(!StringUtils.isEmpty(vo.getName()))
            {
                predicates.add(QRegionLevel.regionLevel.name.like("%" + vo.getName() + "%"));
            }
            if(!StringUtils.isEmpty(vo.getParentId()))
            {
                predicates.add(QRegionLevel.regionLevel.parentId.eq(vo.getParentId()));
            }
            if(!StringUtils.isEmpty(vo.getLevel()))
            {
                predicates.add(QRegionLevel.regionLevel.level.eq(vo.getLevel()));
            }
            if(!StringUtils.isEmpty(vo.getCode()))
            {
                predicates.add(QRegionLevel.regionLevel.code.eq(vo.getCode()));
            }
        }
        predicates.add(QRegionLevel.regionLevel.enableFlag.eq(EnableFlag.Y));
        Predicate predicate = BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
        return predicate;
    }
}
