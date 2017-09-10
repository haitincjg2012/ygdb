package com.apec.user.vo;

import com.apec.framework.common.enumtype.UserAccountType;
import com.apec.framework.dto.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Title: 用户组织
 *
 * @author yirde  2017/9/6.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOrgClientVO  extends BaseVO<Long> {

    /**
     * 仓库组名称
     */
    private String orgName;

    /**
     * 组织的Banner图的URL
     */
    private String orgBannerUrl;

    /**
     * 组织缩略图
     */
    private String orgFirstBannerUrl;

    /**
     * 仓库的库存容量
     */
    private String orgStockCap;

    /*
     * 关注数
     */
    private int attentionNum;

    /**
     * 浏览数
     */
    private int viewNum;

    /**
     * 供求数
     */
    private int productNum;

    /**
     * 所在地区
     */
    private String  address;

    /**
     * 详细地址
     */
    private String addressDetail;

    /**
     * 标签集合
     */
    private String tags;

    /**
     * 实力描述
     */
    private String remark;

    /**
     * 账户类型
     */
    private UserAccountType userAccountType;

    /**
     * 实力描述上传的照片
     */
    private List<UserOrgImageVO> userOrgImageVOS;

}