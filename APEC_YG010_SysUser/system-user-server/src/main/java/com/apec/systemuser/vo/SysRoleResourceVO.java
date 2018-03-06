package com.apec.systemuser.vo;

import com.apec.framework.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xxx
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleResourceVO extends BaseDTO
{
	private String rid;
	
	private String reid;
	
	private String resource;
	
	private String resourceName;
	
	private String type;
	
	private String reids;
}
