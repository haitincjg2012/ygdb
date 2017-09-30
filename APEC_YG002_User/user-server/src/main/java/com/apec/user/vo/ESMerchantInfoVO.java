package com.apec.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Title: 客商索引VO
 *
 * @author yirde  2017/9/12.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ESMerchantInfoVO extends  ESOrgInfoVO {

    /**
     * 主营品种
     */
    private String mainOperating;

    /**
     * 用户ID
     */
    private Long userId;


    public  ESMerchantInfoVO(ESOrgInfoVO esOrgInfoVO,String mainOperating, Long userId){
        this.userId = userId;
        this.mainOperating = mainOperating;
        super.setAddress(esOrgInfoVO.getAddress());
        super.setOrgName(esOrgInfoVO.getOrgName());
        super.setOrgTags(esOrgInfoVO.getOrgTags());
        super.setOrderWeight(esOrgInfoVO.getOrderWeight());
        super.setOrgFirstBannerUrl(esOrgInfoVO.getOrgFirstBannerUrl());
        super.setOrgId(esOrgInfoVO.getOrgId());
        super.setCreateDate(esOrgInfoVO.getCreateDate());
    }
}
