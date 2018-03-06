package com.apec.systemuser.service;

import com.apec.framework.common.PageDTO;
import com.apec.systemuser.vo.SysResourceVO;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * @author xxx
 */
public interface SysResourceService
{

    /**
     * 查询角色权限
     * @param vo 查询条件对象
     * @param pageRequest 分页对象
     * @return 分页结果
     */
    PageDTO<SysResourceVO> searchPage(SysResourceVO vo, PageRequest pageRequest);

    /**
     * 插入角色权限
     * @param vo SysResourceVO
     * @param userId 操作人id
     */
    void insert(SysResourceVO vo, String userId);

    /**
     * 删除对象
     * @param id 删除对象id
     * @param userId 操作人id
     */
    void delete(Long id, Long userId);

    /**
     * 修改角色权限
     * @param vo SysResourceVO
     * @param userId 操作人id
     */
    void update(SysResourceVO vo, String userId);

    /**
     * 查询权限信息
     * @param id  id
     * @return SysResourceVO
     */
    SysResourceVO findOne(Long id);

    /**
     * 查询所有的权限信息
     * @param vo 查询条件对象
     * @return 结果集合
     */
    List<SysResourceVO> selectAll(SysResourceVO vo);

}
