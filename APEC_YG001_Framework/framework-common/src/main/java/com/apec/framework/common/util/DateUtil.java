package com.apec.framework.common.util;

import com.google.common.base.Strings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zhaolei
 * @create 2016-09-18 13:40
 */
public class DateUtil
{

    /**
     * 常用时间格式
     */
    public static final String Format_Date = "yyyy-MM-dd";

    public static final String Format_Time = "HH:mm:ss";

    public static Date string2Date(String str)
    {
        if(Strings.isNullOrEmpty(str))
            return new Date();
        return java.sql.Date.valueOf(str);
    }

    /**
     * 根据传入的格式，将日期对象格式化为日期字符串
     * 
     * @param date 待被格式化日期
     * @param format 自定义日期格式器
     * @return 格式后的日期字符串
     */
    public static String formatDate(Date date, String format)
    {
        String s = "";

        if(date != null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            s = sdf.format(date);
        }

        return s;
    }
    
   
    public static Date formatDates(String date, String format)
    {
    	SimpleDateFormat sim = new SimpleDateFormat(format);
    	try {
			return sim.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
		}
    }
    
    /**
     * 获取前n天开始时间
     * @param n
     * @return
     */
    public static Date getStartPreDay(int n) {
        n = -n;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, n);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }
    
    
}
