package com.apec.systemconfig.vo;

import com.apec.framework.common.enumtype.FeedBackType;
import com.apec.framework.dto.BaseDTO;
import lombok.Data;

import java.util.Date;

/**
 * Created by hmy on 2017/8/28.
 */
@Data
public class FeedBackVO{

    private Long id;

    //反馈/举报
    private FeedBackType feedBackType;

    //反馈/举报信息
    private String feedBackInfo;

    //补充原因
    private String supplementary;

    //凭证(图片url)
    private String certificateUrl;

    //被举报人id
    private Long reportedUserId;

    //被举报人姓名
    private String reportedUser;

    //举报人id
    private Long informantUserId;

    //举报人
    private String informantUser;

    //举报时间
    private Date reportTime;

    //关联单据
    private String relatedIds;



}
