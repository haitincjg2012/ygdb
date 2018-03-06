package com.apec.voucher.service;

import com.apec.framework.common.PageDTO;
import com.apec.voucher.dto.RankingDTO;
import com.apec.voucher.viewvo.WeekBest;
import com.apec.voucher.vo.RankingVO;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * Created by hmy on 2017/12/19.
 *
 * @author hmy
 */
public interface RankingService {

    /**
     * 分页查询排行榜信息
     * @param rankingDTO 查询条件
     * @param pageRequest 分页对象
     * @return 分页结果
     */
    PageDTO<RankingVO> rankingPage(RankingDTO rankingDTO, PageRequest pageRequest);

    /**
     * 查询排行榜详情
     * @param rankingVO rankingVO
     * @return rankingVO
     */
    RankingVO findRankingInfo(RankingVO rankingVO);

    /**
     * 添加排行榜信息
     * @param rankingVO rankingVO
     * @param userId 操作人id
     * @return 操作结果
     */
    String addRankingInfo(RankingVO rankingVO,String userId);

    /**
     * 修改排行榜信息
     * @param rankingVO rankingVO
     * @param userId 操作人id
     * @return 操作结果
     */
    String updateRankingInfo(RankingVO rankingVO,String userId);

    /**
     * 下线排行榜信息
     * @param rankingVO rankingVO
     * @param userId 操作人id
     * @return 操作结果
     */
    String outlineRankingInfo(RankingVO rankingVO,String userId);

    /**
     * 根据上榜条件查询符合条件的用户信息
     * @param rankingVO 上榜条件
     * @param pageRequest 分页条件
     * @return 符合条件的用户信息
     */
    PageDTO<WeekBest> queryUserInfoForRanking(RankingVO rankingVO,PageRequest pageRequest);

    /**
     * 查询排行榜用户信息
     * @return 用户信息
     */
    List<WeekBest> queryRankingMan(RankingVO rankingVO);

}
