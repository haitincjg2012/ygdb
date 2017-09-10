package com.apec.systemuser.service;

import com.apec.framework.common.PageDTO;
import com.apec.systemuser.vo.SysRoleVO;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface SysRoleService
{

    PageDTO<SysRoleVO> searchPage(SysRoleVO vo, PageRequest pageRequest);

    void insert(SysRoleVO vo, String userId);

    void delete(Long id, Long userId);

    void update(SysRoleVO vo, String userId);

    SysRoleVO findOne(Long id);

    List<SysRoleVO> selectAll(SysRoleVO vo);

}
