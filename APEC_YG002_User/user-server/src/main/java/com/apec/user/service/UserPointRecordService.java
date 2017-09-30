package com.apec.user.service;

import com.apec.framework.common.PageDTO;
import com.apec.user.dto.UserDTO;
import com.apec.user.vo.UserPointRecordVO;
import com.apec.user.vo.UserPointRecordViewVO;
import com.apec.user.vo.UserPointRuleVO;
import com.apec.user.vo.UserPointVO;
import org.springframework.data.domain.PageRequest;

/**
 *
 * Created by hmy on 2017/7/6.
 */

public interface UserPointRecordService {

    /**
     * 增加用户积分（减少用户积分）
     * @param userPointRecordVO
     * @return
     */
    String addUserPoint(UserPointRecordVO userPointRecordVO);

    /**
     * 修改积分规则分数
     * @param ruleVO
     * @param userId
     * @return
     */
    String updateUserPointRule(UserPointRuleVO ruleVO, String userId);

    /**
     * 分页查询积分规则信息
     */
    PageDTO<UserPointRuleVO> pageUserPointRule(PageRequest pageRequest);

    /**
     * 分页查询用户积分信息
     */
    PageDTO<UserPointVO> pageUserPoints(UserDTO dto, PageRequest pageRequest);

    /**
     * 分页查询用户积分记录信息
     */
    PageDTO<UserPointRecordViewVO> pageUserPointRecords(UserDTO dto, PageRequest pageRequest);

    /**
     * 查询用户积分是否准确落地，补偿和更新缓存(定时任务)
     * @return
     */
    String perfectUserPoint();


}
