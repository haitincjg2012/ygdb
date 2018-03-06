package com.apec.systemconfig.dto;

import com.apec.framework.common.enums.Realm;
import com.apec.framework.common.enumtype.FeedBackType;
import com.apec.framework.dto.BaseDTO;
import lombok.Data;

import java.util.Date;

/**
 * Created by hmy on 2017/8/28.
 * @author hmy
 */
@Data
public class FeedBackDTO extends BaseDTO {

    private Long id;

    /**
     * 反馈/举报
     */
    private FeedBackType feedBackType;

    /**
     * 反馈/举报信息
     */
    private String feedBackInfo;

    /**
     * 凭证(图片url)
     */
    private String certificateUrl;

    /**
     * 被举报人id
     */
    private Long reportedUserId;

    /**
     * 被举报人
     */
    private String reportedUser;

    /**
     * 举报人id
     */
    private Long informantUserId;

    /**
     * 举报人
     */
    private String informantUser;

    /**
     * 举报时间
     */
    private Date reportTime;

    /**
     * 关联单据
     */
    private String relatedIds;

    /**
     * 关联数据来源(举报的数据来源,用户判断关联单据来源何方,去往何处)
     */
    private Realm realm;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;

}
