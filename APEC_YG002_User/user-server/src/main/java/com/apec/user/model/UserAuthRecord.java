package com.apec.user.model;

import com.apec.framework.common.Constants;
import com.apec.framework.jpa.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Title: 用户实名认证审核记录
 *
 * @author yirde  2017/6/29.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class UserAuthRecord  extends BaseModel<Long> {

    private static final long serialVersionUID = 6233891566852240263L;

    /**
     * 用户ID
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 用户真实姓名
     */
    private String realName;

    /**
     * 身份证
     */
    private String idNumber;

    /**
     * 第一张正面图片
     */
    private String imgOneURL;

    /**
     *  第二张反面图
     */
    private String imgTwoURL;

    /**
     * Y 通过  N 驳回
     */
    private String success;

    /**
     * 审核人
     */
    private String  reviewEmp;

    /**
     * 审核时间
     */
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewDate;

    /**
     * 备注
     */
    private String remark;


}
