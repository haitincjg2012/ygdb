package com.apec.systemuser.service.impl;


import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.StringUtil;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.exception.ServerException;
import com.apec.framework.common.util.BeanUtil;
import com.apec.systemuser.dao.SysRoleDao;
import com.apec.systemuser.model.QSysRole;
import com.apec.systemuser.model.SysRole;
import com.apec.systemuser.service.SysRoleService;
import com.apec.systemuser.util.SnowFlakeKeyGen;
import com.apec.systemuser.vo.SysRoleVO;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author xxx
 */
@Service
public class SysRoleServiceImpl implements SysRoleService
{
    @Autowired
    private SysRoleDao dao;

    @Autowired
    private SnowFlakeKeyGen inGen;

    @Override
    public PageDTO<SysRoleVO> searchPage(SysRoleVO dto, PageRequest pageRequest)
    {
        Page<SysRole> page = dao.findAll(getInputCondition(dto), pageRequest);
        PageDTO<SysRoleVO> pageDto = new PageDTO<>();
        List<SysRoleVO> list = new ArrayList<>();
        for(SysRole emtity : page)
        {
            SysRoleVO vo = new SysRoleVO();
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
    public SysRoleVO findOne(Long id)
    {
        SysRole entity = dao.findOne(id);
        SysRoleVO vo = null;
        if(entity != null)
        {
            vo = new SysRoleVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(entity, vo);
        }
        return vo;
    }

    @Override
    public List<SysRoleVO> selectAll(SysRoleVO vo)
    {
        Iterable<SysRole> iterable = dao.findAll(getInputCondition(vo));
        Iterator<SysRole> it = iterable.iterator();
        List<SysRoleVO> list = new ArrayList<>();
        while (it.hasNext())
        {
            SysRole entity = it.next();
            SysRoleVO dto = new SysRoleVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(entity, dto);
            list.add(dto);
        }
        return list;
    }

    @Override
    public void insert(SysRoleVO dto, String userId)
    {
        SysRole entity = new SysRole();
        BeanUtil.copyPropertiesIgnoreNullFilds(dto, entity);
        entity.setId(inGen.nextId());
        entity.setCreateBy(userId);
        entity.setCreateDate(new Date());
        entity.setEnableFlag(EnableFlag.Y);
        dao.save(entity);
    }

    @Override
    public void update(SysRoleVO vo, String userId)
    {
        SysRole entity = dao.findOne(vo.getId());
        if(entity == null)
        {
            throw new ServerException(Constants.SYS_ERROR, "找不到该角色");
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(vo, entity);
        entity.setLastUpdateBy(userId);
        entity.setLastUpdateDate(new Date());
        dao.save(entity);
    }

    @Override
    public void delete(Long id, Long userId)
    {
        SysRole entity = dao.findOne(id);
        if(entity == null)
        {
            throw new ServerException(Constants.SYS_ERROR, "找不到该角色");
        }
        entity.setEnableFlag(EnableFlag.N);
        dao.save(entity);
    }


    /**
     * 根据多种情况查询
     * 包括like：name，eq：mobile，eq：orgCode，like：loginName
     */
    private Predicate getInputCondition(SysRoleVO vo)
    {
        List<BooleanExpression> predicates = new ArrayList<>();
        if(null != vo)
        {
            if(!StringUtil.isEmpty(vo.getName()))
            {
                predicates.add(QSysRole.sysRole.name.like("%" + vo.getName() + "%"));
            }
            if(!StringUtil.isEmpty(vo.getType()))
            {
                predicates.add(QSysRole.sysRole.type.eq(vo.getType()));
            }
        }
        predicates.add(QSysRole.sysRole.enableFlag.eq(EnableFlag.Y));
        Predicate predicate = BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
        return predicate;
    }
}
