package com.apec.systemuser.service.impl;

import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.StringUtil;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.exception.ServerException;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.common.util.JsonUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.springcloud.SpringCloudClient;
import com.apec.systemuser.dao.SysUserDao;
import com.apec.systemuser.model.QSysUser;
import com.apec.systemuser.model.SysUser;
import com.apec.systemuser.service.SysRoleService;
import com.apec.systemuser.service.SysUserService;
import com.apec.systemuser.util.SnowFlakeKeyGen;
import com.apec.systemuser.vo.SysRoleVO;
import com.apec.systemuser.vo.SysUserVO;
import com.apec.systemuser.vo.SysUserViewVO;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SysUserServiceImpl implements SysUserService
{
    @Autowired
    private SysUserDao dao;

    @Autowired
    private SnowFlakeKeyGen inGen;
    
    @Autowired
    private SpringCloudClient springCloudClient;
    
    @InjectLogger
    protected Logger log;

    @Autowired
    private SysRoleService sysRoleService;

    @Override
    public PageDTO<SysUserVO> searchPage(SysUserVO dto, PageRequest pageRequest)
    {
    	Map<String,String> reqMap = new HashMap<String,String>();
        //调用用户接口
        Map<String,String> map = new HashMap<String,String>();
        List<SysRoleVO> sysRoleList =sysRoleService.selectAll(new SysRoleVO());
        for (SysRoleVO sysRoleVO: sysRoleList) {
            map.put(String.valueOf(sysRoleVO.getId()), sysRoleVO.getName());
        }
        Page<SysUser> page = dao.findAll(getInputCondition(dto), pageRequest);
        PageDTO<SysUserVO> pageDto = new PageDTO<SysUserVO>();
        List<SysUserVO> list = new ArrayList<>();
        for(SysUser emtity : page)
        {
            SysUserVO vo = new SysUserVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(emtity, vo);
            vo.setRoleName(map.get(vo.getUserRoleNo()));
            list.add(vo);
        }
        pageDto.setRows(list);
        pageDto.setTotalPages(page.getTotalPages());
        pageDto.setTotalElements(page.getTotalElements());
        pageDto.setNumber(page.getNumber()+1);
        return pageDto;
    }

    @Override
    public SysUserVO findOne(Long id)
    {
        SysUser entity = dao.findOne(id);
        SysUserVO vo = null;
        if(entity != null)
        {
            vo = new SysUserVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(entity, vo);
        }
        return vo;
    }

    @Override
    public List<SysUserVO> selectAll(SysUserVO vo)
    {
        Iterable<SysUser> iterable = dao.findAll(getInputCondition(vo));
        Iterator<SysUser> it = iterable.iterator();
        List<SysUserVO> list = new ArrayList<SysUserVO>();
        while (it.hasNext())
        {
            SysUser entity = it.next();
            SysUserVO dto = new SysUserVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(entity, dto);
            list.add(dto);
        }
        return list;
    }

    @Override
    public List<SysUserVO> selectAll(Predicate predicate)
    {
        Iterable<SysUser> iterable = dao.findAll(predicate);
        Iterator<SysUser> it = iterable.iterator();
        List<SysUserVO> list = new ArrayList<SysUserVO>();
        while (it.hasNext())
        {
            SysUser entity = it.next();
            SysUserVO dto = new SysUserVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(entity, dto);
            list.add(dto);
        }
        return list;
    }

    @Override
    public String querySysUserById(Long id) {
        if(id == null || id == 0L){
            return "";
        }
        SysUser sysUser = dao.findOne(id);
        return sysUser.getName();
    }

    @Override
    public List<SysUserViewVO> listSysUserByIds(List<Long> ids) {
        if(ids == null || ids.size() == 0){
            return Collections.emptyList();
        }
        List<SysUser> list = dao.findByIdInAndEnableFlag(ids, EnableFlag.Y);
        List<SysUserViewVO> listView = new ArrayList<>();
        list.forEach(sysUser ->{
            listView.add(new SysUserViewVO(String.valueOf(sysUser.getId()),sysUser.getName()));
        });
        return listView;
    }

    @Override
    public void insert(SysUserVO dto, String userId)
    {
        SysUser entity = new SysUser();
        BeanUtil.copyPropertiesIgnoreNullFilds(dto, entity);
        entity.setId(inGen.nextId());
        entity.setCreateBy(userId);
        entity.setCreateDate(new Date());
        entity.setEnableFlag(EnableFlag.Y);
        dao.save(entity);
    }

    @Override
    public void update(SysUserVO vo, String userId)
    {
        SysUser entity = dao.findOne(vo.getId());
        if(entity == null)
        {
            throw new ServerException(Constants.SYS_ERROR, "找不到该用户");
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(vo, entity);
        entity.setLastUpdateBy(userId);
        entity.setLastUpdateDate(new Date());
        dao.save(entity);
    }

    @Override
    public void delete(Long id, Long userId)
    {
        SysUser entity = dao.findOne(id);
        if(entity == null)
        {
            throw new ServerException(Constants.SYS_ERROR, "找不到该用户");
        }
        entity.setEnableFlag(EnableFlag.N);
        entity.setLastUpdateBy(userId + "");
        entity.setLastUpdateDate(new Date());
        dao.save(entity);
    }

    @Override
    public boolean isExist(SysUserVO vo)
    {
        List<SysUserVO> list = this.selectAll(vo);
        if(list != null && list.size() > 0)
        {
            return true;
        }
        return false;
    }

    /**
     * 根据多种情况查询
     * 包括like：name，eq：mobile，like：loginName
     * @param vo
     * @return
     */
    private Predicate getInputCondition(SysUserVO vo)
    {
        List<BooleanExpression> predicates = new ArrayList<BooleanExpression>();
        if(null != vo)
        {
            if(!StringUtil.isEmpty(vo.getName()))
            {
                predicates.add(QSysUser.sysUser.name.like("%" + vo.getName() + "%"));
            }
            if(!StringUtil.isEmpty(vo.getMobile()))
            {
                predicates.add(QSysUser.sysUser.mobile.eq(vo.getMobile()));
            }
        }
        predicates.add(QSysUser.sysUser.enableFlag.eq(EnableFlag.Y));
        Predicate predicate = BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
        return predicate;
    }
    
    /**
     * 请求其他服务
     * @param server
     * @param method
     * @param reqMap
     * @return
     */
    private ResultData callServer(String server, String method, Map<String,String> reqMap){
        ResultData resultData = null;
        String url = Constants.HTTP_COLON + Constants.DOUBLE_SLASH + server + Constants.SINGLE_SLASH + method;
        try{
            String res = springCloudClient.post(url, JsonUtil.toJSONString(reqMap));
            resultData = JsonUtil.parseObject(res, ResultData.class);
        }catch (Exception e){
            log.error("调用后台服务异常 " + url, e);
            resultData =  new ResultData<>();
            resultData.setSucceed(false);
            resultData.setErrorCode(Constants.SYS_ERROR);
        }
        return resultData;
    }
}
