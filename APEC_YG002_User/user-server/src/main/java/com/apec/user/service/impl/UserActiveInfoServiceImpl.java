package com.apec.user.service.impl;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.user.dao.UserActiveInfoDAO;
import com.apec.user.model.UserActiveInfo;
import com.apec.user.service.UserActiveInfoService;
import com.apec.user.util.SnowFlakeKeyGen;
import com.apec.user.vo.UserActiveInfoVO;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by hmy on 2018/2/7.
 *
 * @author hmy
 */
@Service
public class UserActiveInfoServiceImpl implements UserActiveInfoService {

    @InjectLogger
    private Logger logger;

    @Autowired
    private SnowFlakeKeyGen idGen;

    @Autowired
    private UserActiveInfoDAO userActiveInfoDAO;

    @Override
    public String addUserActiveInfo(UserActiveInfoVO userActiveInfoVO) {
        UserActiveInfo userActiveInfo = new UserActiveInfo();
        BeanUtil.copyPropertiesIgnoreNullFilds(userActiveInfoVO,userActiveInfo);
        userActiveInfo.setId(idGen.nextId());
        userActiveInfo.setEnableFlag(EnableFlag.Y);
        userActiveInfo.setCreateDate(new Date());
        userActiveInfoDAO.save(userActiveInfo);
        userActiveInfoVO.setId(userActiveInfo.getId());
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String updateUserActiveInfo(UserActiveInfoVO userActiveInfoVO) {
        UserActiveInfo userActiveInfo = userActiveInfoDAO.findOne(userActiveInfoVO.getId());
        if(userActiveInfo == null){
            return Constants.ENABLE_NOT_NULL;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(userActiveInfoVO,userActiveInfo);
        userActiveInfo.setLastUpdateDate(new Date());
        userActiveInfoDAO.save(userActiveInfo);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public UserActiveInfoVO findUserActiveInfo(UserActiveInfoVO userActiveInfoVO) {
        UserActiveInfo userActiveInfo = userActiveInfoDAO.findOne(userActiveInfoVO.getId());
        if(userActiveInfo == null){
            return null;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(userActiveInfo,userActiveInfoVO);
        return userActiveInfoVO;
    }


}
