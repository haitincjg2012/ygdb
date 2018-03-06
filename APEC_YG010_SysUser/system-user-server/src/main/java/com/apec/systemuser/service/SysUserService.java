package com.apec.systemuser.service;

import com.apec.framework.common.PageDTO;
import com.apec.systemuser.vo.SysUserVO;
import com.apec.systemuser.vo.SysUserViewVO;
import com.mysema.query.types.Predicate;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * @author xxxx
 */
public interface SysUserService
{

    /**
     * 分页查询系统用户对象信息
     * @param vo 查询条件对象
     * @param pageRequest 分页对象
     * @return 分页结果
     */
    PageDTO<SysUserVO> searchPage(SysUserVO vo, PageRequest pageRequest);

    /**
     * 插入系统用户信息
     * @param vo 系统用户对象
     * @param userId 操作人id
     */
    void insert(SysUserVO vo, String userId);

    /**
     * 删除系统用户
     * @param id id
     * @param userId 操作人id
     */
    void delete(Long id, Long userId);

    /**
     * 修改用户信息
     * @param vo 用户对象
     * @param userId 操作人id
     */
    void update(SysUserVO vo, String userId);

    /**
     * 查询系统用户具体信息
     * @param id id
     * @return 系统用户对象信息
     */
    SysUserVO findOne(Long id);

    /**
     * 查询所有的用户信息
     * @param vo 查询条件对象
     * @return 结果集合
     */
    List<SysUserVO> selectAll(SysUserVO vo);

    /**
     * 用户是否存在
     * @param vo 用户信息
     * @return 是/否
     */
    boolean isExist(SysUserVO vo);

    /**
     * 查询所有的用户信息
     * @param predicate 查询条件对象
     * @return 结果集合
     */
    List<SysUserVO> selectAll(Predicate predicate);

    /**
     * 查询系统用户具体信息
     * @param id id
     * @return 系统用户对象信息
     */
    String querySysUserById(Long id);

    /**
     * 查询ids下的用户信息集合
     * @param ids ids
     * @return 结果集合
     */
    List<SysUserViewVO> listSysUserByIds(List<Long> ids);

}
