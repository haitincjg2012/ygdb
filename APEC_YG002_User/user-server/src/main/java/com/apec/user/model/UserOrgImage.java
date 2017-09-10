package com.apec.user.model;

import com.apec.framework.common.Constants;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Title: 用户组织实例描述图片集合
 *
 * @author yirde  2017/9/6.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class UserOrgImage  extends BaseModel<Long> {

    private static final long serialVersionUID = 6277833211124240313L;

    /**
     * OrgId
     */
    private Long  userOrgId;

    /**
     * 图片地址
     */
    @Column(nullable = false)
    private String imageUrl;

    /**
     * 顺序
     */
    private Integer sort;
}
