package com.apec.user.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.UserLevel;
import com.apec.framework.jpa.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Title: 用户积分
 *
 * @author yirde  2017/6/28.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
@Data
public class UserPoint extends BaseModel<Long> {

    private static final long serialVersionUID = 6277345566852240263L;
    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;


    /**
     * 该用户总的可用积分
     */
    private Integer availablePoints;

    /**
     * 该用户的等级
     * 等级规则计算每日凌晨1:00计算
     */

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserLevel userLevel = UserLevel.QT;

    /**
     * 最近一个消费积分的时间
     */
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastConsumeTime;

}
