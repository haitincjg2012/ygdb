package com.apec.systemuser.service;


import com.apec.systemuser.vo.SysResourceVO;
import com.apec.systemuser.vo.SysRoleResourceVO;

import java.util.List;

public interface SysRoleResourceService
{
	void save(SysRoleResourceVO vo, String userId);
	
	public List<SysRoleResourceVO> selectAll(SysRoleResourceVO vo);
	
	public List<SysResourceVO> selectAllResource(SysRoleResourceVO vo);
}
