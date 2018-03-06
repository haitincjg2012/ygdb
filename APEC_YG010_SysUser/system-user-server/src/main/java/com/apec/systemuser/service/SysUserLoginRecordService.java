package com.apec.systemuser.service;

import com.apec.systemuser.vo.SysUserLoginRecordVO;

/**
 * Created by wubi on 2017/8/1.
 * @author wubi
 */
public interface SysUserLoginRecordService {

    /**
     * 插入对象信息
     * @param vo 系统用户登录记录
     * @param userId 操作人id
     */
    void insert(SysUserLoginRecordVO vo, String userId);
}
