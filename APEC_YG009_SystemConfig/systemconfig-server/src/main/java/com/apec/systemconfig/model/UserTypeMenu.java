package com.apec.systemconfig.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.UserType;
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
 * 用户身份展示的相应的字段信息
 * @author hmy
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class UserTypeMenu extends BaseModel<Long> {

    /**
     * 用户身份
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单对应的字段名称
     */
    private String nameStr;

    /**
     * 排序字段
     */
    private Integer sort;


}
