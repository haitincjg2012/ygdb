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
public class RankingMan extends BaseModel<Long> {

    private static final long serialVersionUID = -6561460455388065970L;

    /**
     * 所属排行榜id
     */
    private Long rankingId;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

}
