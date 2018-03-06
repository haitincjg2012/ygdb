package com.apec.society.service;

import com.apec.society.vo.SocietyInfoVO;

import java.util.List;

/**
 * 圈子相关服务
 * @author yirder
 */
public interface SocietyService {

    /**
     * 添加新圈子西悉尼
     * @param societyInfoVO 圈子信息
     * @return returnCode 返回码
     */
    String addSocietyInfo(SocietyInfoVO societyInfoVO);

    /**
     * 查看所有的圈子信息
     * @return 所有的圈子信息
     */
    List<SocietyInfoVO> getAllSocietyInfo();

}
