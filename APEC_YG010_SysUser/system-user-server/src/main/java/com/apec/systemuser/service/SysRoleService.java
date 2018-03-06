package com.apec.systemuser.service;

import com.apec.framework.common.PageDTO;
import com.apec.systemuser.vo.SysRoleVO;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * @author xxx
 */
public interface SysRoleService
{

    /**
     * 查询角色信息
     * @param vo 查询条件对象
     * @param pageRequest 分页对象
     * @return 分页结果
     */
    PageDTO<SysRoleVO> searchPage(SysRoleVO vo, PageRequest pageRequest);

    /**
     * 插入对象信息
     * @param vo SysRoleVO
     * @param userId 操作人id
     */
    void insert(SysRoleVO vo, String userId);

    /**
     * 删除对象信息
     * @param  id 要删除的对象id
     * @param userId 操作人id
     */
    void delete(Long id, Long userId);

    /**
     * 修改对象信息
     * @param vo SysRoleVO
     * @param userId 操作人id
     */
    void update(SysRoleVO vo, String userId);

    /**
     * 查询对象信息
     * @param id SysRoleVO
     * @return 角色信息
     */
    SysRoleVO findOne(Long id);

    /**
     * 查询所有的角色信息
     * @param vo 查询条件对象
     * @return 结果集合
     */
    List<SysRoleVO> selectAll(SysRoleVO vo);

}
