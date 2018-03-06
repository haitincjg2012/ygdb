package com.apec.systemconfig.service;

import com.apec.framework.common.PageDTO;
import com.apec.systemconfig.vo.RegionLevelVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * Created by hmy on 2017/7/28.
 * @author hmy
 */
public interface RegionLevelService {

    /**
     * 查询所有符合条件的地区信息（名称的模糊查询，父级地区id）
     * @param  vo vo
     * @return 所有符合条件的地区信息
     */
    List<RegionLevelVO> selectAll(RegionLevelVO vo);

    /**
     * 获取业务所需的所有地区及其所有直系管辖地区
     * @param regionLevelIds regionLevelIds
     * @return 业务所需的所有地区及其所有直系管辖地区
     */
    List<RegionLevelVO> getNeedRegionLevel(List<String> regionLevelIds);

    /**
     * 查询传入的地区直属管辖地区分页信息
     * @param vo vo
     * @param pageRequest 分页对象
     * @return 分页结果
     */
    PageDTO<RegionLevelVO> pageRegionLevel(RegionLevelVO vo, PageRequest pageRequest);

    /**
     * 查询最大层级的地区分页信息
     * @param pageRequest 分页对象
     * @return 分页结果
     */
    PageDTO<RegionLevelVO> pagemaxRegionLevel(PageRequest pageRequest);

    /**
     * 查询地址信息
     * @param addId 地址id
     * @return 具体的地址信息（省|市|县|街道）
     */
    String getAddressInfo(String addId);


}
