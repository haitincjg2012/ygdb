package com.apec.society.service;

import com.apec.framework.common.PageDTO;
import com.apec.society.dto.SocietyPostDTO;
import com.apec.society.view.QuotationView;
import com.apec.society.view.SocietyPostViewVO;
import com.apec.society.vo.SocietyPostAggreVO;
import com.apec.society.vo.SocietyPostVO;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;

/**
 * Created by hmy on 2017/10/30.
 *
 * @author hmy
 */
public interface SocietyPostService {

    /**
     * 查询符合条件的用户
     * @param num 帖子数
     * @param startTime  查询开始时间
     * @param endTime 查询结束时间
     * @return
     */
    List<SocietyPostAggreVO> queryUserForRanking(int num, Date startTime, Date endTime);

    /**
     * 查询符合条件的用户
     * @param num 帖子数
     * @param startTime  查询开始时间
     * @param endTime 查询结束时间
     * @return 符合条件的用户
     */
    List<SocietyPostAggreVO> queryUserForRankingForReply(int num, Date startTime, Date endTime);

    /**
     * 增加帖子
     * @param societyPostVO 帖子对象
     * @return 操作结果
     */
    String addSocietyPost(SocietyPostVO societyPostVO);

    /**
     * 添加行情竞猜信息
     * @param quotationView 行情竞猜信息
     * @param userId 用户id
     * @return 处理结果
     */
    String addQuotation(QuotationView quotationView,String userId);

    /**
     * 修改行情竞猜信息
     * @param quotationView 行情竞猜信息
     * @return 处理结果
     */
    String updateQuotation(QuotationView quotationView);

    /**
     * 行情竞猜下线
     * @param quotationView quotationView
     * @return 下线结果
     */
    String outLineQuotation(QuotationView quotationView);

    /**
     * 增加帖子阅读量
     * @param societyPostVO 帖子对象
     * @return 操作结果
     */
    String addSocietyPostViewCount(SocietyPostVO societyPostVO);

    /**
     * 增加帖子分享数量
     * @param societyPostVO 帖子对象
     * @return 操作结果
     */
    String addSocietyPostTransCount(SocietyPostVO societyPostVO);

    /**
     * 点赞/取消点赞
     * @param societyPostVO 帖子对象
     * @param userId 用户id
     * @return 操作结果
     */
    String likeSocietyPostOrNot(SocietyPostVO societyPostVO,String userId);

    /**
     * 查询帖子详情
     * @param societyPostVO 帖子  id
     * @param userId 操作人
     * @return 帖子详情
     */
    SocietyPostViewVO findSocietyPostById(SocietyPostVO societyPostVO,String userId);


    /**
     * 分页查询帖子信息
     * @param societyPostDTO 查询条件
     * @param pageRequest 分页对象
     * @param userId 操作人id
     * @return 分页结果
     */
    PageDTO<SocietyPostViewVO> societyPostSerachPage(SocietyPostDTO societyPostDTO, PageRequest pageRequest,String userId);

    /**
     * 查询要导出的果有圈数据
     * @param societyPostDTO 查询条件
     * @return 果有圈信息
     */
    List<Object[]> selectSocietyInfoForExcel(SocietyPostDTO societyPostDTO);

    /**
     * 分页查询帖子信息
     * @param societyPostDTO 查询条件
     * @param pageRequest 分页对象
     * @param userId 操作人id
     * @return 分页结果
     */
    PageDTO<SocietyPostViewVO> mySocietyPostPage(SocietyPostDTO societyPostDTO, PageRequest pageRequest,String userId);

    /**
     * 置顶/取消置顶
     * @param societyPostVO 帖子对象
     * @return 操作结果
     */
    String stickSocietyPost(SocietyPostVO societyPostVO);

    /**
     * 修改信息
     * @param societyPostVO 帖子对象
     * @return 操作结果
     */
    String updateSocietyPostInfo(SocietyPostVO societyPostVO);

    /**
     * 删除信息
     * @param societyPostVO 帖子对象
     * @return 操作结果
     */
    String deleteSocietyPostById(SocietyPostVO societyPostVO);

    /**
     * 文章审核
     * @param societyPostVO 帖子对象
     * @return 操作结果
     */
    String articleReview(SocietyPostVO societyPostVO);

    /**
     * 查询按时间降序，图片不为空的帖子/行情
     * @param societyPostDTO 查询条件
     * @param pageRequest 分页对象
     * @return 分页结果
     */
    PageDTO<SocietyPostViewVO> queryNewsListByTopPic(SocietyPostDTO societyPostDTO, PageRequest pageRequest);

    /**
     * 系统创建行情竞猜
     * @return 操作结果
     */
    String newQuotation();

    /**
     * 获取信息的行情竞猜信息
     * @param userId 登陆者id
     * @return 操作结果
     */
    QuotationView newsQuo(String userId);

    /**
     * 获取最新的已出结果的行情竞猜信息
     * @return 竞猜结果
     */
    QuotationView newsReviewQuo();

    /**
     * 行情竞猜结果
     * @param quotationView 结果
     * @param userId 操作人id
     * @return 操作结果
     */
    String quotationReview(QuotationView quotationView,String userId);

    /**
     * 返回行情竞猜信息
     * @param societyPostDTO 查询条件
     * @param pageRequest 分页对象
     * @param userId 操作人id
     * @return 分页结果
     */
    PageDTO<QuotationView> newsQuoPage(SocietyPostDTO societyPostDTO,PageRequest pageRequest, String userId);

}
