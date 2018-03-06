package com.apec.game.vo;

import com.apec.framework.common.enumtype.LotteryFormType;
import lombok.Data;

/**
 * Created by hmy on 2017/12/21.
 *
 * @author hmy
 */
@Data
public class LotteryFormVO {

    private Long id;

    /**
     * 抽奖形式
     */
    private LotteryFormType lotteryFormType;

    /**
     * 抽奖形式
     */
    private String lotteryFormTypeKey;

    /**
     * 链接地址
     */
    private String url;
}
