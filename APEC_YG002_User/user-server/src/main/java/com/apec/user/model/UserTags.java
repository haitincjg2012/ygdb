package com.apec.user.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.TagLayoutType;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 *
 * 用户标签
 * Created by yirpro on 2017/9/10.
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class UserTags extends BaseModel<Long> {

    private static final long serialVersionUID = 6277834166832140263L;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 样式名称
     */
    private String className;

    /**
     * 标签对应的布局类型
     */
    @Enumerated(EnumType.STRING)
    private TagLayoutType tagLayoutType;


    /**
     * 排序
     */
    private int sort;

    /**
     * OrgId 用户组织ID
     */
    private Long  userOrgId;
}
