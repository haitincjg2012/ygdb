package com.apec.systemuser.service.impl;

import com.apec.framework.common.util.BeanUtil;
import com.apec.systemuser.dao.SysUserLoginRecordDao;
import com.apec.systemuser.model.SysUserLoginRecord;
import com.apec.systemuser.service.SysUserLoginRecordService;
import com.apec.systemuser.util.SnowFlakeKeyGen;
import com.apec.systemuser.vo.SysUserLoginRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wubi on 2017/8/1.
 * @author wubi
 */
@Service
public class SysUserLoginRecordServiceImpl implements SysUserLoginRecordService {

    @Autowired
    private SysUserLoginRecordDao sysUserLoginRecordDao;
    @Autowired
    private SnowFlakeKeyGen inGen;
    @Override
    public void insert(SysUserLoginRecordVO vo, String userId) {
        //保存用户登录信息
        SysUserLoginRecord userLoginRecord = new SysUserLoginRecord();
        BeanUtil.copyPropertiesIgnoreNullFilds(vo,userLoginRecord);
        userLoginRecord.setId(inGen.nextId());
        sysUserLoginRecordDao.save(userLoginRecord);
    }
}
