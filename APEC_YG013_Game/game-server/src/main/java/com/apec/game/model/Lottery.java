package com.apec.game.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.LotteryFormType;
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
 * Title: 抽奖活动
 *
 * @author hmy  2017/12/20.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
@Data
public class Lottery extends BaseModel<Long> {

    private static final long serialVersionUID = 6377345566852240201L;

    /**
     * 抽奖名称
     */
    private String lotteryName;

    /**
     * 抽奖形式
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LotteryFormType lotteryFormType;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 上线时间
     */
    private Date startTime;

    /**
     * 下线时间
     */
    private Date endTime;

    /**
     * 备注
     */
    private String remark;


}
