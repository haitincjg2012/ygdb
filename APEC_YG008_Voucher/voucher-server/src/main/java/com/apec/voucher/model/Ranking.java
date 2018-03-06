package com.apec.voucher.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.RankingType;
import com.apec.framework.jpa.model.BaseModel;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

/**
 * Created by hmy on 2017/12/19.
 *
 * @author hmy
 */
@Data
@Entity
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class Ranking  extends BaseModel<Long> {

    private static final long serialVersionUID = -6561460455388065960L;

    /**
     * 排行榜类型
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RankingType rankingType;

    /**
     * 是否实时
     */
    private boolean actualTime;

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
