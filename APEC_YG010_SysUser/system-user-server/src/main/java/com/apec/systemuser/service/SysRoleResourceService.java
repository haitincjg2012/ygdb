package com.apec.systemuser.service;


import com.apec.systemuser.vo.SysResourceVO;
import com.apec.systemuser.vo.SysRoleResourceVO;

import java.util.List;

/**
 * @author xxx
 */
public interface SysRoleResourceService
{
	/**
	 * 保存
	 * @param vo vo
	 * @param userId 操作人id
	 */
	void save(SysRoleResourceVO vo, String userId);

	/**
	 * 查询所有
	 * @param vo 查询条件对象
	 * @return 结果集合
	 */
	List<SysRoleResourceVO> selectAll(SysRoleResourceVO vo);

	/**
	 * 查询所有
	 * @param vo 查询条件对象
	 * @return 结果集合
	 */
	List<SysResourceVO> selectAllResource(SysRoleResourceVO vo);
}
