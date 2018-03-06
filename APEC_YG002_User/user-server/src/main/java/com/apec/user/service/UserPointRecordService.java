package com.apec.user.service;

import com.apec.framework.common.PageDTO;
import com.apec.user.dto.UserDTO;
import com.apec.user.vo.*;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 *
 * Created by hmy on 2017/7/6.
 * @author hmy
 */

public interface UserPointRecordService {

    /**
     * 增加用户积分（减少用户积分）
     * @param userPointRecordVO 积分对象
     * @return 处理结果
     */
    String addUserPoint(UserPointRecordVO userPointRecordVO);

    /**
     * 增加积分规则
     * @param ruleVO 规则对象
     * @param userId 用户id
     * @return 处理结果
     */
    String addUserPointRule(UserPointRuleVO ruleVO, String userId);

    /**
     * 修改积分规则分数
     * @param ruleVO 规则对象
     * @param userId 用户id
     * @return 处理结果
     */
    String updateUserPointRule(UserPointRuleVO ruleVO, String userId);

    /**
     * 删除积分规则
     * @param ruleVO 规则对象
     * @param userId 用户id
     * @return 处理结果
     */
    String deleteRulePoints(UserPointRuleVO ruleVO, String userId);

    /**
     * 分页查询积分规则信息
     * @param pageRequest 分页对象
     * @return 分页查询积分规则信息
     */
    PageDTO<UserPointRuleVO> pageUserPointRule(PageRequest pageRequest);

    /**
     * 查询所有的积分规则
     * @return 积分规则
     */
    List<UserPointRuleVO> listUserPointRule();

    /**
     * 分页查询用户积分信息
     * @param userPointVO 查询条件对象
     * @param pageRequest 分页对象
     * @return 用户积分分页信息
     */
    PageDTO<UserPointVO> pageUserPoints(UserPointVO userPointVO, PageRequest pageRequest);

    /**
     * 分页查询用户积分记录信息
     * @param dto 查询条件对象
     * @param pageRequest 分页对象
     * @return 积分记录分页信息
     */
    PageDTO<UserPointRecordViewVO> pageUserPointRecords(UserDTO dto, PageRequest pageRequest);

    /**
     * 查询用户积分是否准确落地，补偿和更新缓存(定时任务)
     * @return String
     */
    String perfectUserPoint();

    /**
     * 分页查询积分等级信息
     * @param pageRequest 分页对象
     * @return 分页查询积分等级信息
     */
    PageDTO<UserLevelRuleVO> pageUserLevelRules(PageRequest pageRequest);

    /**
     * 查询所有的积分等级规则
     * @return
     */
    List<UserLevelRuleVO> listUserLevelRules();

    /**
     * 修改等级积分对象
     * @param userLevelRuleVO userLevelRuleVO
     * @param userId 操作人id
     * @return 处理结果
     */
    String updateUserLevelRule(UserLevelRuleVO userLevelRuleVO,String userId);

    /**
     * 查询我的用户积分情况
     * @param userId 用户id
     * @return 积分情况
     */
    MyPointVO queryMyPoint(Long userId);


}
