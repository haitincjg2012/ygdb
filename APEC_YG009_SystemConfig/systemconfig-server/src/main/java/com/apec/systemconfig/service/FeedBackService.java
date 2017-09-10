package com.apec.systemconfig.service;

import com.apec.framework.common.PageDTO;
import com.apec.systemconfig.dto.FeedBackDTO;
import com.apec.systemconfig.vo.FeedBackVO;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * Created by hmy on 2017/8/28.
 */
public interface FeedBackService {

    /**
     * 增加反馈信息
     * @param feedBackVO
     * @param userId
     * @return
     */
    String addFeedBackInfo(FeedBackVO feedBackVO, String userId);

    /**
     * 分页查询反馈信息
     * @param pageRequest
     * @param dto 查询相应的条件信息
     * @return
     */
    PageDTO<FeedBackVO> queryFeedBackPage(PageRequest pageRequest, FeedBackDTO dto);

    /**
     * 查询举报具体信息
     * @param feedBackVO
     * @return
     */
    FeedBackVO queryFeedBackInfo(FeedBackVO feedBackVO);

    /**
     * 删除反馈信息
     * @param feedBackVO
     * @param userId
     * @return
     */
    String deleteFeedBackInfo(FeedBackVO feedBackVO, String userId);

    /**
     * 批量删除反馈信息
     * @param ids
     * @param userId
     * @return
     */
    String deleteFeedBackList(List<Long> ids, String userId);


}
