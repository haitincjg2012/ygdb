package com.apec.systemuser.service.impl;

import com.apec.framework.common.StringUtil;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.systemuser.dao.SysRoleResouceDao;
import com.apec.systemuser.model.QSysRoleResource;
import com.apec.systemuser.model.SysRoleResource;
import com.apec.systemuser.service.SysResourceService;
import com.apec.systemuser.service.SysRoleResourceService;
import com.apec.systemuser.util.SnowFlakeKeyGen;
import com.apec.systemuser.vo.SysResourceVO;
import com.apec.systemuser.vo.SysRoleResourceVO;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SysRoleResourceServiceImpl implements SysRoleResourceService {

	@Autowired
    private SysRoleResouceDao dao;

    @Autowired
    private SnowFlakeKeyGen inGen;
    
    @Autowired
    private SysResourceService sysResourceService;
	@Override
	public void save(SysRoleResourceVO vo, String userId) {
		SysRoleResourceVO tem = new SysRoleResourceVO();
		tem.setRid(vo.getRid());
		Iterable<SysRoleResource> iterable = dao.findAll(getInputCondition(tem));
		Iterator<SysRoleResource> it = iterable.iterator();
		while(it.hasNext()){
			dao.delete(it.next().getId());
		}
		String[] reids = vo.getReids().split(",");
		for(int i=0;i<reids.length;i++){
			SysRoleResource srr = new SysRoleResource();
			srr.setRid(vo.getRid());
			srr.setReid(reids[i]);
			srr.setId(inGen.nextId());
			srr.setEnableFlag(EnableFlag.Y);
			srr.setCreateBy(userId);
			dao.save(srr);
		}
	}

	@Override
    public List<SysRoleResourceVO> selectAll(SysRoleResourceVO vo)
    {
        Iterable<SysRoleResource> iterable = dao.findAll(getInputCondition(vo));
        Iterator<SysRoleResource> it = iterable.iterator();
        Map<String,SysRoleResource> map = new HashMap<String,SysRoleResource>();
        List<SysRoleResourceVO> list = new ArrayList<SysRoleResourceVO>();
        while (it.hasNext()){
        	SysRoleResource entity = it.next();
        	map.put(entity.getReid(), entity);
        }
        List<SysResourceVO> rlist = sysResourceService.selectAll(null);
        for(int i=0;i<rlist.size();i++){
        	SysResourceVO tem = rlist.get(i);
        	if(null!=map.get(tem.getId()+"")){
        		SysRoleResource temRr = map.get(tem.getId()+"");
        		SysRoleResourceVO ssr = new SysRoleResourceVO();
        		ssr.setReid(temRr.getReid());
        		ssr.setRid(temRr.getRid());
        		ssr.setResource(tem.getResource());
        		ssr.setResourceName(tem.getName());
        		ssr.setType(tem.getType());
        		list.add(ssr);
        	}
        }
        return list;
    }
	
	@Override
    public List<SysResourceVO> selectAllResource(SysRoleResourceVO vo)
    {
        Iterable<SysRoleResource> iterable = dao.findAll(getInputCondition(vo));
        Iterator<SysRoleResource> it = iterable.iterator();
        Map<String,SysRoleResource> map = new HashMap<String,SysRoleResource>();
        while (it.hasNext()){
        	SysRoleResource entity = it.next();
        	map.put(entity.getReid(), entity);
        }
        List<SysResourceVO> rlist = sysResourceService.selectAll(null);
        for(int i=0;i<rlist.size();i++){
        	SysResourceVO tem = rlist.get(i);
        	if(null!=map.get(tem.getId()+"")){
        		tem.setFlag("1");
        	}else{
        		tem.setFlag("0");
        	}
        }
        return rlist;
    }
	
	private Predicate getInputCondition(SysRoleResourceVO vo)
    {
        List<BooleanExpression> predicates = new ArrayList<BooleanExpression>();
        if(null != vo)
        {
            if(!StringUtil.isEmpty(vo.getRid()))
            {
                predicates.add(QSysRoleResource.sysRoleResource.rid.eq(vo.getRid()));
            }
            if(!StringUtil.isEmpty(vo.getReid()))
            {
                predicates.add(QSysRoleResource.sysRoleResource.reid.eq(vo.getReid()));
            }
        }
        predicates.add(QSysRoleResource.sysRoleResource.enableFlag.eq(EnableFlag.Y));
        Predicate predicate = BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
        return predicate;
    }
}
