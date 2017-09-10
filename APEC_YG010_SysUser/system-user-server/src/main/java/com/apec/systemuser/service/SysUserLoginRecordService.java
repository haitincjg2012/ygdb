package com.apec.systemuser.service;

import com.apec.systemuser.vo.SysUserLoginRecordVO;

/**
 * Created by wubi on 2017/8/1.
 */
public interface SysUserLoginRecordService {
    void insert(SysUserLoginRecordVO vo, String userId);
}
