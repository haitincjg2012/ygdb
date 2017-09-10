package com.apec.systemconfig.vo;

import com.apec.framework.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionLevelVO extends BaseDTO
{
    private Long id;
    
    private String code;

    private String parentId;
    
    private String name;
    
    private String level;

    /**
     * 其下级所有地区信息
     */
    private List<RegionLevelVO> regionLevelVOS;


}
