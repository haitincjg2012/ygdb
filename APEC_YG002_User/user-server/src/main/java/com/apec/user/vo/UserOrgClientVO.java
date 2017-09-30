package com.apec.user.vo;

import com.apec.framework.common.enumtype.UserAccountType;
import com.apec.framework.dto.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 *
 * Title: 用户组织
 *
 * @author yirde  2017/9/6.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOrgClientVO{

    private Long id;

    private Date createDate;

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

    /**
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
     * 主营品种
     */
    private String mainOperating;

    /**
     * 销售地址
     */
    private String saleAddress;

    /**
     * 综合排序权重分数
     * 每晚凌晨三点进行重新排序 更新
     */
    private Long orderWeight;

    /**
     * ES ID
     */
    private String elasticId;

    /**
     * 实力描述
     */
    private String remark;

    /**
     * 账户类型
     */
    private UserAccountType userAccountType;

    /**
     * 账户类型
     */
    private String userAccountTypeKey;

    /**
     * 实力描述上传的照片
     */
    private List<UserOrgImageVO> userOrgImageVOS;

    /**
     * 组织拥有的标签
     */
    private List<UserTagsVO> userTagsVOS;

    /**
     * 开关，
     */
    private boolean pushFlag;

    /**
     * 组织账户下的用户账号信息
     */
    private String orgClientUsers;


}
