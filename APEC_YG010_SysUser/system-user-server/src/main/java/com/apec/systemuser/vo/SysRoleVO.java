package com.apec.systemuser.vo;

import com.apec.framework.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Title:
 *
 * @author wubi  2017/7/31.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleVO extends BaseDTO
{
	private Long id;
	
	private String name;
    
    private String remake;
    
    private String type;

}
