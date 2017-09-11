package com.apec.user.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.UserAccountType;
import com.apec.framework.jpa.model.BaseModel;
import com.sun.org.apache.xpath.internal.operations.String;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Title: 用户组织
 * @author yirde  2017/9/5.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class UserOrgClient extends BaseModel<Long> {

    private static final long serialVersionUID = 6277834166852240263L;

    /**
     * 仓库组名称
     */
    private String orgName;

    /**
     * 组织的Banner图的URL
     */
    private String orgBannerUrl;

    /**
     * 组织的缩略图
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
     * 账号类型：
     *   ORG_ACCOUNT("组织账户"),
     *   IND_ACCOUNT("个体账户")
     */
    @Enumerated(EnumType.STRING)
    private UserAccountType userAccountType;

}
