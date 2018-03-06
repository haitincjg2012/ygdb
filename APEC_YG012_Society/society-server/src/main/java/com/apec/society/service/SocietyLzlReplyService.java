package com.apec.society.service;

import com.apec.society.view.SocietyLzlReplyViewVO;
import com.apec.society.vo.SocietyLzlReplyVO;

import java.util.List;

/**
 * Created by hmy on 2017/10/30.
 *
 * @author hmy
 */
public interface SocietyLzlReplyService {

    /**
     * 添加楼中楼回复
     * @param societyLzlReplyVO 楼中楼数据回复对象
     * @return 操作结果
     */
    String addSocietyLzlReply(SocietyLzlReplyVO societyLzlReplyVO);

    /**
     * 删除楼中楼回复
     * @param societyLzlReplyVO 楼中楼数据回复对象
     * @return 操作结果
     */
    String deleteSocietyLzlReply(SocietyLzlReplyVO societyLzlReplyVO);

    /**
     * 获取楼中楼回复
     * @param societyLzlReplyVO 查询条件
     * @return 所有符合条件的楼中楼回复
     */
    List<SocietyLzlReplyViewVO> getLzlReplyFormReply(SocietyLzlReplyVO societyLzlReplyVO);

}
