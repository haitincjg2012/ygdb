package com.apec.framework.dto;

/**
 * Title:
 *
 * @author yirde  2017/9/7.
 */
public class UserOrgImageInfoVO {

    /**
     * 地址
     */
    private String imageUrl;

    /**
     * 顺序
     */
    private Integer sort;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString(){
        return super.toString();
    }

}
