package com.apec.systemconfig.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.FeedBackType;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

/**
 * 反馈
 * Created by hmy on 2017/8/28.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class FeedBack extends BaseModel<Long>{

    //反馈/举报
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeedBackType feedBackType;

    //反馈/举报信息
    @Column(length = 2000,nullable = false)
    private String feedBackInfo;

    //补充原因
    private String supplementary;

    //凭证(图片url)
    private String certificateUrl;

    //被举报人id
    private Long reportedUserId;

    //被举报人姓名
    private String reportedUser;

    //举报/反馈人id
    private Long informantUserId;

    //举报/反馈人姓名
    private String informantUser;

    //举报时间
    private Date reportTime;

    //关联单据
    private String relatedIds;


}
