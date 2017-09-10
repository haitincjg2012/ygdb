package com.apec.framework.common.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateTimeUtils extends DateUtils
{

    public final static int SECOND = 0;
    public final static int MINUTE = 1;
    public final static int HOUR = 2;
    public final static int DAY = 3;
    
    public static final String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static String formatUTC(Date date)
    {
        if (date == null)
        {
            return null;
        }
        return DateFormatUtils.format(date, UTC_FORMAT);
    }

    /**
     * 获取指定年份和月份对应的天数
     *
     * @param year
     *        指定的年份
     * @param month
     *        指定的月份
     * @return int 返回天数
     */
    public static int getDaysInMonth(int year, int month)
    {
        if ((month == 1) || (month == 3) || (month == 5) || (month == 7) || (month == 8) || (month == 10)
                || (month == 12))
        {
            return 31;
        }
        else if ((month == 4) || (month == 6) || (month == 9) || (month == 11))
        {
            return 30;
        }
        else
        {
            if (((year % 4) == 0) && ((year % 100) != 0) || ((year % 400) == 0))
            {
                return 29;
            }
            else
            {
                return 28;
            }
        }
    }

    /**
     * 根据所给的起止时间来计算间隔的天数
     *
     * @param startDate
     *        起始时间
     * @param endDate
     *        结束时间
     * @return int 返回间隔天数
     */
    public static int getIntervalDays(java.sql.Date startDate, java.sql.Date endDate)
    {
        long startdate = startDate.getTime();
        long enddate = endDate.getTime();
        long interval = enddate - startdate;
        int intervalday = (int) (interval / (1000 * 60 * 60 * 24));
        return intervalday;
    }

    /**
     * 根据所给的起止时间来计算间隔的月数
     *
     * @param startDate
     *        起始时间
     * @param endDate
     *        结束时间
     * @return int 返回间隔月数
     */
    public static int getIntervalMonths(java.sql.Date startDate, java.sql.Date endDate)
    {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        int startDateM = startCal.MONTH;
        int startDateY = startCal.YEAR;
        int enddatem = endCal.MONTH;
        int enddatey = endCal.YEAR;
        int interval = (enddatey * 12 + enddatem) - (startDateY * 12 + startDateM);
        return interval;
    }

    /**
     * 返回当前日期时间字符串<br>
     * 默认格式:yyyy-mm-dd hh:mm:ss
     *
     * @return String 返回当前字符串型日期时间
     */
    public static String getCurrentTime()
    {
        String returnStr = null;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        returnStr = f.format(date);
        return returnStr;
    }

    /**
     * 返回当前日期时间字符串<br>
     * 默认格式:yyyymmddhhmmss
     *
     * @return String 返回当前字符串型日期时间
     */
    public static BigDecimal getCurrentTimeAsNumber()
    {
        String returnStr = null;
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        returnStr = f.format(date);
        return new BigDecimal(returnStr);
    }

    /**
     * 返回自定义格式的当前日期时间字符串
     *
     * @param format
     *        格式规则
     * @return String 返回当前字符串型日期时间
     */
    public static String getCurrentTime(String format)
    {
        String returnStr = null;
        SimpleDateFormat f = new SimpleDateFormat(format);
        Date date = new Date();
        returnStr = f.format(date);
        return returnStr;
    }

    /**
     * 返回时间格式
     *
     * @param format
     * @param date
     * @return
     */
    public static String getTimeFormat(String format, Date date)
    {
        String returnStr = null;
        SimpleDateFormat f = new SimpleDateFormat(format);
        returnStr = f.format(date);
        return returnStr;
    }

    public static String getTimeFormat(Date date)
    {
        String returnStr = null;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        returnStr = f.format(date);
        return returnStr;
    }

    public static String getDayCustomer(int calendar, int step, String format)
    {
        Calendar cal = Calendar.getInstance();
        cal.add(calendar, step);
        return DateFormatUtils.format(cal.getTime(), format);
    }

    /**
     * 返回当前字符串型日期
     *
     * @return String 返回的字符串型日期
     */
    public static String getCurDate()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = simpledateformat.format(calendar.getTime());
        return strDate;
    }

    /**
     * 根据日期获取星期
     *
     * @param strdate
     * @return
     */
    public static String getWeekDayByDate(String strdate)
    {
        final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        try
        {
            date = sdfInput.parse(strdate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0)
            dayOfWeek = 0;
        return dayNames[dayOfWeek];
    }

    /**
     * 返回指定格式的字符型日期
     *
     * @param date
     * @param formatString
     * @return
     */
    public static String Date2String(Date date, String formatString)
    {
        if (null == date)
        {
            return null;
        }
        SimpleDateFormat simpledateformat = new SimpleDateFormat(formatString);
        String strDate = simpledateformat.format(date);
        return strDate;
    }

    /**
     * 返回当前字符串型日期
     *
     * @param format
     *        格式规则
     * @return String 返回的字符串型日期
     */
    public static String getCurDate(String format)
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat(format);
        String strDate = simpledateformat.format(calendar.getTime());
        return strDate;
    }

    /**
     * 格式时间
     *
     * @param source
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date getDate4String(String source, String... format) throws ParseException
    {
        return parseDate(source, format);
    }

    /**
     * 将字符串型日期转换为日期型
     *
     * @param strDate
     *        字符串型日期
     * @param srcDateFormat
     *        源日期格式
     * @param dstDateFormat
     *        目标日期格式
     * @return Date 返回的util.Date型日期
     */
    public static Date stringToDate(String strDate, String srcDateFormat, String dstDateFormat)
    {
        Date rtDate = null;
        Date tmpDate = (new SimpleDateFormat(srcDateFormat)).parse(strDate, new ParsePosition(0));
        String tmpString = null;
        if (tmpDate != null)
            tmpString = (new SimpleDateFormat(dstDateFormat)).format(tmpDate);
        if (tmpString != null)
            rtDate = (new SimpleDateFormat(dstDateFormat)).parse(tmpString, new ParsePosition(0));
        return rtDate;
    }

    /**
     * 获取当前时间前几个月的时间
     *
     * @param n
     *        前几个月
     * @return
     */
    public static Date getPreMonth(int n)
    {
        n = -n;
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, n);
        return c.getTime();

    }

    /**
     * 获取前n天时间
     *
     * @param n
     * @return
     */
    public static Date getPreDay(int n)
    {
        n = -n;
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, n);
        return c.getTime();
    }

    /**
     *  获取n天后的时间
     * @param n
     * @return
     */
    public static Date getAfterDate(int n)
    {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, n);
        return c.getTime();
    }

    /**
     * 获取前n天开始时间
     *
     * @param n
     * @return
     */
    public static Date getStartPreDay(int n)
    {
        n = -n;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, n);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取前n天开始时间
     *
     * @param n
     * @return
     */
    public static Date getEndPreDay(int n)
    {
        n = -n;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, n);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * 获取某年某月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getFisrtDayOfMonth(int year, int month)
    {
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, year);
        // 设置月份
        cal.set(Calendar.MONTH, month - 1);
        // 获取某月最小天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取某年某月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth(int year, int month)
    {
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, year);
        // 设置月份
        cal.set(Calendar.MONTH, month - 1);
        // 获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * 获取某年第一天日期
     *
     * @param year
     *        年份
     * @return Date
     */
    public static Date getCurrYearFirst(int year)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.getTime();
        return calendar.getTime();
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year
     *        年份
     * @return Date
     */
    public static Date getCurrYearLast(int year)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    /**
     * 查询时间段的所有日期
     *
     * @param dBegin
     * @param dEnd
     * @return
     */
    public static List<String> findDates(Date dBegin, Date dEnd)
    {
        List<String> lDate = new ArrayList<String>();
        lDate.add(getTimeFormat("yyyy-MM-dd", dBegin));
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime()))
        {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(getTimeFormat("yyyy-MM-dd", calBegin.getTime()));
        }
        return lDate;
    }

    /**
     * 查询时间段的所有月份
     *
     * @param dBegin
     * @param dEnd
     * @return
     */
    public static List<String> findMonths(Date dBegin, Date dEnd)
    {
        List<String> lDate = new ArrayList<String>();
        lDate.add(getTimeFormat("yyyy-MM", dBegin));
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (calBegin.before(calEnd))
        {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.MONTH, 1);
            lDate.add(getTimeFormat("yyyy-MM", calBegin.getTime()));
        }
        return lDate;
    }

    /**
     * String 转 Date
     *
     * @param strDate
     * @return
     */
    public static Date transferStrToDate(String strDate)
    {
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            return sim.parse(strDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * String 转 Date
     *
     * @param strDate
     * @return
     */
    public static Date transferStrToDates(String strDate)
    {
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            return sim.parse(strDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据所给的起止时间来计算间隔的天数
     *
     * @param startDate
     *        起始时间
     * @param endDate
     *        结束时间
     * @return int 返回间隔天数
     */
    public static int getIntervalDays(Date startDate, Date endDate)
    {
        long startdate = startDate.getTime();
        long enddate = endDate.getTime();
        long interval = enddate - startdate;
        int intervalday = (int) (interval / (1000 * 60 * 60 * 24));
        return intervalday;
    }

    public static int isAfternoon()
    {// 结果为“0”是上午 结果为“1”是下午
        GregorianCalendar ca = new GregorianCalendar();
        return ca.get(GregorianCalendar.AM_PM);
    }
    
    /**
     * 计算两个时间的差值，spec为 SECOND 秒，MINUTE 分，HOUR 时，DAY 天
     * @param d1
     * @param d2
     * @param spec
     * @return
     */
    public static int getDifferTime(Date d1,Date d2,int spec){
        long t1 = d1.getTime()-d2.getTime();
        int result = 0;
        switch(spec){
            case SECOND:
                result = (int)t1/1000;
                break;
            case MINUTE:
                result = (int)t1/1000/60;
                break;
            case HOUR:
                result = (int)t1/1000/60/60;
                break;
            case DAY:
                result = (int)t1/1000/60/60/24;
                break;
            default:
                result = 0;
        }
        return result;
    }

    /**
     * 得到当前月的第一天
     * @param format
     * @return
     */
    public static String getCurMonthFirstDate(String format) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        return DateTimeUtils.getTimeFormat("yyyy-MM-dd",c.getTime());
    }

}