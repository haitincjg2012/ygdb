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
public class SysResourceVO extends BaseDTO
{
	private Long id;
	
	private String name;
	
    private String type;
	
    private String resource;
	
    private Integer sorts;
	
    private Long parent;
    
    private String remake;
    
    private String flag;
}
