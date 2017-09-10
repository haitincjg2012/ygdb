package com.apec.framework.jpa.common;

import java.util.Calendar;

import org.springframework.data.auditing.DateTimeProvider;

/**
 * 类 编 号：BL_PU1010202_DbDateTimeProvider
 * 类 名 称：DbDateTimeProvider
 * 内容摘要：实体必填字段当前时间
 * 完成日期：2016-07-14
 * 编码作者：
 */
public class DbDateTimeProvider implements DateTimeProvider
{

    /**
     * 数据库时间必填字段时间戳
     */
    @Override
    public Calendar getNow()
    {
        return Calendar.getInstance();
    }

}
