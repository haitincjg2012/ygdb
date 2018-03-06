package com.apec.user.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.UserLevel;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Title:
 *
 * @author yirde  2017/7/29.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class UserLevelRule extends BaseModel<Long> {

    private static final long serialVersionUID = 6277891523852240264L;

    /**
     *  用户等级
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserLevel userLevel;

    /**
     * 等级头衔
     */
    private String userLevelName;

    /**
     *  积分
     */
    @Column(nullable = false)
    private Integer point;

    /**
     * 等级图片url
     */
    private String url;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否为禁用状态
     */
    private boolean frezzing;


}
