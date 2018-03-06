package com.apec.systemuser.service.impl;


import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.StringUtil;
import com.apec.framework.common.constants.RoleConstants;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.exception.ServerException;
import com.apec.framework.common.util.BeanUtil;
import com.apec.systemuser.dao.SysResourceDao;
import com.apec.systemuser.model.QSysResource;
import com.apec.systemuser.model.SysResource;
import com.apec.systemuser.service.SysResourceService;
import com.apec.systemuser.util.SnowFlakeKeyGen;
import com.apec.systemuser.vo.SysResourceVO;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *@author xxx
 */
@Service
public class SysResourceServiceImpl implements SysResourceService
{
    @Autowired
    private SysResourceDao dao;

    @Autowired
    private SnowFlakeKeyGen inGen;

    @Override
    public PageDTO<SysResourceVO> searchPage(SysResourceVO dto, PageRequest pageRequest)
    {
        Page<SysResource> page = dao.findAll(getInputCondition(dto), pageRequest);
        PageDTO<SysResourceVO> pageDto = new PageDTO<>();
        List<SysResourceVO> list = new ArrayList<>();
        for(SysResource emtity : page)
        {
            SysResourceVO vo = new SysResourceVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(emtity, vo);
            list.add(vo);
        }
        pageDto.setRows(list);
        pageDto.setTotalPages(page.getTotalPages());
        pageDto.setTotalElements(page.getTotalElements());
        pageDto.setNumber(page.getNumber()+1);
        return pageDto;
    }

    @Override
    public SysResourceVO findOne(Long id)
    {
        SysResource entity = dao.findOne(id);
        SysResourceVO vo = null;
        if(entity != null)
        {
            vo = new SysResourceVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(entity, vo);
        }
        return vo;
    }

    @Override
    public List<SysResourceVO> selectAll(SysResourceVO vo)
    {
    	Sort sort = new Sort(Sort.Direction.ASC, "parent");
    	Sort sort1 = new Sort(Sort.Direction.ASC, "createDate");
    	sort = sort.and(sort1);
        Iterable<SysResource> iterable = dao.findAll(getInputCondition(vo),sort);
        Iterator<SysResource> it = iterable.iterator();
        List<SysResourceVO> list = new ArrayList<>();
        while (it.hasNext())
        {
            SysResource entity = it.next();
            SysResourceVO dto = new SysResourceVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(entity, dto);
            list.add(dto);
        }
        return list;
    }

    @Override
    public void insert(SysResourceVO dto, String userId)
    {
        SysResource entity = new SysResource();
        BeanUtil.copyPropertiesIgnoreNullFilds(dto, entity);
        entity.setId(inGen.nextId());
        if(RoleConstants.RESOURCE_TYPE_1.equals(dto.getType())){
        	entity.setParent(entity.getId());
        }
        entity.setCreateBy(userId);
        entity.setCreateDate(new Date());
        entity.setEnableFlag(EnableFlag.Y);
        dao.save(entity);
    }

    @Override
    public void update(SysResourceVO vo, String userId)
    {
        SysResource entity = dao.findOne(vo.getId());
        if(entity == null)
        {
            throw new ServerException(Constants.SYS_ERROR, "找不到该资源");
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(vo, entity);
        entity.setLastUpdateBy(userId);
        entity.setLastUpdateDate(new Date());
        dao.save(entity);
    }

    @Override
    public void delete(Long id, Long userId)
    {
        SysResource entity = dao.findOne(id);
        if(entity == null)
        {
            throw new ServerException(Constants.SYS_ERROR, "找不到该资源");
        }
        entity.setEnableFlag(EnableFlag.N);
        dao.save(entity);
    }


    /**
     * 根据多种情况查询
     * 包括like：name，eq：mobile，eq：orgCode，like：loginName
     */
    private Predicate getInputCondition(SysResourceVO vo)
    {
        List<BooleanExpression> predicates = new ArrayList<>();
        if(null != vo)
        {
            if(!StringUtil.isEmpty(vo.getName()))
            {
                predicates.add(QSysResource.sysResource.name.like("%" + vo.getName() + "%"));
            }
            if(null!=vo.getParent()&&vo.getParent()>0)
            {
                predicates.add(QSysResource.sysResource.parent.eq(vo.getParent()));
            }
            if(!StringUtil.isEmpty(vo.getType()))
            {
                predicates.add(QSysResource.sysResource.type.eq(vo.getType()));
            }
        }
        predicates.add(QSysResource.sysResource.enableFlag.eq(EnableFlag.Y));
        Predicate predicate = BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
        return predicate;
    }
}
