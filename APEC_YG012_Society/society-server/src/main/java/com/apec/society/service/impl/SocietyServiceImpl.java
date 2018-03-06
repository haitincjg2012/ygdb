package com.apec.society.service.impl;

import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.enumtype.SocietyType;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.dto.UserInfoVO;
import com.apec.framework.log.InjectLogger;
import com.apec.society.dao.SocietyInfoDAO;
import com.apec.society.model.SocietyInfo;
import com.apec.society.util.SnowFlakeKeyGen;
import com.apec.society.service.SocietyService;
import com.apec.society.vo.SocietyInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 圈子服务实现类
 * @author yirder
 */
@Service
public class SocietyServiceImpl implements SocietyService {

    @Autowired
    private SnowFlakeKeyGen idGen;

    @InjectLogger
    private Logger logger;

    @Autowired
    private SocietyInfoDAO societyInfoDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addSocietyInfo(SocietyInfoVO societyInfoVO) {
        if(StringUtils.isBlank(societyInfoVO.getTitle())) {
            logger.warn("[SocietyServiceImpl][addSocietyInfo] Can't add SocietyInfo , Imperfect personal information!");
            return Constants.COMMON_MISSING_PARAMS;
        }
        SocietyInfo societyInfo = new SocietyInfo();
        BeanUtil.copyPropertiesIgnoreNullFilds(societyInfoVO,societyInfo);
        societyInfo.setId(idGen.nextId());
        societyInfo.setSocietyType(societyInfoVO.getSocietyType() == null? SocietyType.HOT_TOPIC:SocietyType.DEFAULT);
        societyInfo.setCreateDate(new Date());
        societyInfo.setLastUpdateDate(societyInfo.getCreateDate());
        societyInfo.setEnableFlag(EnableFlag.Y);
        societyInfoDAO.save(societyInfo);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 查看所有的圈子信息
     * @return 所有的圈子信息
     */
    @Override
    public List<SocietyInfoVO> getAllSocietyInfo(){
        List<SocietyInfoVO> societyInfoVOS = new ArrayList<>();
        List<SocietyInfo> societyInfos = societyInfoDAO.findAll();
        societyInfos.forEach(societyInfo -> {
            SocietyInfoVO societyInfoVO = new SocietyInfoVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(societyInfo,societyInfoVO);
            societyInfoVOS.add(societyInfoVO);
        });
        return societyInfoVOS;
    }

}
