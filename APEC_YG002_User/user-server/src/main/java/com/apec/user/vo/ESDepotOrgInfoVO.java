package com.apec.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Title: 冷库 ES VO
 *
 * @author yirde  2017/9/12.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ESDepotOrgInfoVO extends  ESOrgInfoVO {

    /**
     * 仓库的库存容量
     */
    private String orgStockCap;

    /**
     * 主营品种
     */
    private String mainOperating;


    public  ESDepotOrgInfoVO(ESOrgInfoVO esOrgInfoVO,String orgStockCap,String mainOperating){
         this.mainOperating = mainOperating;
         this.orgStockCap = orgStockCap;
         super.setAddress(esOrgInfoVO.getAddress());
         super.setOrgName(esOrgInfoVO.getOrgName());
         super.setOrgTags(esOrgInfoVO.getOrgTags());
         super.setOrderWeight(esOrgInfoVO.getOrderWeight());
         super.setOrgFirstBannerUrl(esOrgInfoVO.getOrgFirstBannerUrl());
         super.setOrgId(esOrgInfoVO.getOrgId());
         super.setCreateDate(esOrgInfoVO.getCreateDate());
    }

}
