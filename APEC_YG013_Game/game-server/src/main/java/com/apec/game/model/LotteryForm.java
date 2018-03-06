package com.apec.game.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.LotteryFormType;
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
 * Title: 抽奖形式
 *
 * @author hmy  2017/12/20.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
@Data
public class LotteryForm extends BaseModel<Long> {

    private static final long serialVersionUID = 6377345566852240201L;

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



}
