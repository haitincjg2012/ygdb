package com.apec.game.service;

import com.apec.framework.common.PageDTO;
import com.apec.game.dto.LotteryDTO;
import com.apec.game.dto.PrizeAwardDTO;
import com.apec.game.dto.PrizePeopleDTO;
import com.apec.game.vo.*;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * Created by hmy on 2017/12/21.
 *
 * @author hmy
 */
public interface LotteryService {

    /**
     * 添加抽奖活动
     * @param lotteryVO  抽奖活动
     * @param userId 用户id
     * @return 处理结果码
     */
    String addLottery(LotteryVO lotteryVO,String userId);

    /**
     * 修改抽奖活动
     * @param lotteryVO  抽奖活动
     * @param userId 用户id
     * @return 处理结果码
     */
    String updateLottery(LotteryVO lotteryVO,String userId);

    /**
     * 删除抽奖活动
     * @param lotteryVO  抽奖活动
     * @param userId 用户id
     * @return 处理结果码
     */
    String deleteLottery(LotteryVO lotteryVO,String userId);

    /**
     * 修改抽奖活动提示
     * @param lotteryHintVO  抽奖活动
     * @param userId 用户id
     * @return 处理结果码
     */
    String updateLotteryHoint(LotteryHintVO lotteryHintVO, String userId);

    /**
     * 删除抽奖活动提示
     * @param lotteryHintVO  抽奖活动
     * @param userId 用户id
     * @return 处理结果码
     */
    String deleteLotteryHoint(LotteryHintVO lotteryHintVO, String userId);

    /**
     * 分页查询抽奖活动
     * @param lotteryDTO 查询条件
     * @param pageRequest 分页条件
     * @return 分页结果
     */
    PageDTO<LotteryVO> queryLotteryPage(LotteryDTO lotteryDTO, PageRequest pageRequest);

    /**
     * 查询抽奖活动信息
     * @param lotteryVO 查询条件
     * @return 分页结果
     */
    LotteryVO queryLotteryInfo(LotteryVO lotteryVO);

    /**
     * 查询所有的抽奖形式
     * @return 所有的抽奖形式信息
     */
    List<LotteryFormVO> queryLotteryFormList();

    /**
     * 查询所有的抽奖条件
     * @return 所有的抽奖条件信息
     */
    List<ParticipationConditionVO> queryParticipationConditionList();

    /**
     * 添加抽奖规则
     * @param activeRuleVO  抽奖规则
     * @param userId 用户id
     * @return 处理结果码
     */
    String addActiveRule(ActiveRuleVO activeRuleVO, String userId);

    /**
     * 修改抽奖规则
     * @param activeRuleVO  抽奖规则
     * @param userId 用户id
     * @return 处理结果码
     */
    String updateActiveRule(ActiveRuleVO activeRuleVO, String userId);

    /**
     * 查询抽奖规则
     * @param activeRuleVO  抽奖规则
     * @return 处理结果码
     */
    ActiveRuleVO queryActiveRuleInfoOfLottery(ActiveRuleVO activeRuleVO);

    /**
     * 奖品设置
     * @param prizeAwardVO  奖品
     * @param userId 用户id
     * @return 处理结果码
     */
    String addLotteryPrize(PrizeAwardVO prizeAwardVO, String userId);

    /**
     * 修改奖品设置
     * @param prizeAwardVO  奖品
     * @param userId 用户id
     * @return 处理结果码
     */
    String updateLotteryPrize(PrizeAwardVO prizeAwardVO, String userId);

    /**
     * 删除奖品设置
     * @param prizeAwardVO  奖品
     * @param userId 用户id
     * @return 处理结果码
     */
    String deleteLotteryPrize(PrizeAwardVO prizeAwardVO, String userId);

    /**
     * 查询奖品设置信息
     * @param prizeAwardVO 奖品设置信息
     * @return 奖品设置信息
     */
    PrizeAwardVO queryLotteryPrizeInfo(PrizeAwardVO prizeAwardVO);

    /**
     * 分页查询设置的奖品
     * @param prizeAwardDTO 查询条件
     * @param pageRequest 分页条件
     * @return 分页结果
     */
    PageDTO<PrizeAwardVO> queryLotteryPrizePage(PrizeAwardDTO prizeAwardDTO,PageRequest pageRequest);

    /**
     * 分页查询设置的奖品
     * @param prizePeopleDTO 查询条件
     * @param pageRequest 分页条件
     * @return 分页结果
     */
    PageDTO<PrizePeopleVO> queryLotteryPeoplePage(PrizePeopleDTO prizePeopleDTO, PageRequest pageRequest);

    /**
     * 统计抽奖用户人数
     * @param lotteryVO 抽奖id
     * @return 抽奖人数
     */
    LotteryVO countLotteryPeople(LotteryVO lotteryVO);


}
