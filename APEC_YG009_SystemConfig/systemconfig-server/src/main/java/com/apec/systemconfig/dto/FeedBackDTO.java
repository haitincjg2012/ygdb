package com.apec.systemconfig.dto;

import com.apec.framework.common.enumtype.FeedBackType;
import com.apec.framework.dto.BaseDTO;
import lombok.Data;

import java.util.Date;

/**
 * Created by hmy on 2017/8/28.
 */
@Data
public class FeedBackDTO extends BaseDTO {

    private Long id;

    //反馈/举报
    private FeedBackType feedBackType;

    //反馈/举报信息
    private String feedBackInfo;

    //凭证(图片url)
    private String certificate_url;

    //被举报人id
    private Long reportedUserId;

    //被举报人
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
