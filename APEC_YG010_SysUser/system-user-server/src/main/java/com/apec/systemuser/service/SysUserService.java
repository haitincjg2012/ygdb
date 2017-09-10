package com.apec.systemuser.service;

import com.apec.framework.common.PageDTO;
import com.apec.systemuser.vo.SysUserVO;
import com.apec.systemuser.vo.SysUserViewVO;
import com.mysema.query.types.Predicate;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface SysUserService
{

    PageDTO<SysUserVO> searchPage(SysUserVO vo, PageRequest pageRequest);

    void insert(SysUserVO vo, String userId);

    void delete(Long id, Long userId);

    void update(SysUserVO vo, String userId);

    SysUserVO findOne(Long id);

    List<SysUserVO> selectAll(SysUserVO vo);

    public boolean isExist(SysUserVO vo);

    List<SysUserVO> selectAll(Predicate predicate);

    String querySysUserById(Long id);

    List<SysUserViewVO> listSysUserByIds(List<Long> ids);

}
