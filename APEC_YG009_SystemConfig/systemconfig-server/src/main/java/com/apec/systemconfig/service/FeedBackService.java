package com.apec.systemconfig.service;

import com.apec.framework.common.PageDTO;
import com.apec.systemconfig.dto.FeedBackDTO;
import com.apec.systemconfig.vo.FeedBackVO;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * Created by hmy on 2017/8/28.
 * @author hmy
 */
public interface FeedBackService {

    /**
     * 增加反馈信息
     * @param feedBackVO 反馈信息
     * @param userId 操作人id
     * @return 处理结果
     */
    String addFeedBackInfo(FeedBackVO feedBackVO, String userId);

    /**
     * 分页查询反馈信息
     * @param pageRequest 分页对象
     * @param dto 查询相应的条件信息
     * @return 分页结果
     */
    PageDTO<FeedBackVO> queryFeedBackPage(PageRequest pageRequest, FeedBackDTO dto);

    /**
     * 查询举报具体信息
     * @param feedBackVO 反馈信息
     * @return 反馈信息
     */
    FeedBackVO queryFeedBackInfo(FeedBackVO feedBackVO);

    /**
     * 删除反馈信息
     * @param feedBackVO 反馈信息
     * @param userId 操作人id
     * @return 处理结果
     */
    String deleteFeedBackInfo(FeedBackVO feedBackVO, String userId);

    /**
     * 批量删除反馈信息
     * @param ids 要删除的id集合
     * @param userId 操作人id
     * @return 处理结果
     */
    String deleteFeedBackList(List<Long> ids, String userId);


}
