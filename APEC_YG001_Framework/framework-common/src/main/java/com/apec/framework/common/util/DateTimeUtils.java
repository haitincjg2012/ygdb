package com.apec.framework.common.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author XXX
 */
public class DateTimeUtils extends DateUtils
{

    public final static int SECOND = 0;
    public final static int MINUTE = 1;
    public final static int HOUR = 2;
    public final static int DAY = 3;

    private final static int ONE = 1;
    private final static int TWO = 2;
    private final static int THREE = 3;
    private final static int FOUR = 4;
    private final static int FIVE = 5;
    private final static int SIX = 6;
    private final static int SEVEN = 7;
    private final static int ENGIT = 8;
    private final static int NINE = 9;
    private final static int TEN = 10;
    private final static int ELEVEN = 11;
    private final static int TWELVE = 12;
    private final static int TWENTY_EIGHT = 28;
    private final static int TWENTY_NINE = 29;
    private final static int THIRTY = 30;
    private final static int THIRTY_ONE = 31;
    private final static int HUNDRED = 100;
    private final static int FOUR_HUNDRED = 400;

    private static final String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

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
        if ((month == ONE) || (month == THREE) || (month == FIVE) || (month == SEVEN) || (month == ENGIT) || (month == TEN)
                || (month == TWELVE))
        {
            return THIRTY_ONE;
        }
        else if ((month == FOUR) || (month == SIX) || (month == NINE) || (month == ELEVEN))
        {
            return THIRTY;
        }
        else
        {
            boolean flag = ((year % FOUR) == 0) && ((year % HUNDRED) != 0) || ((year % FOUR_HUNDRED) == 0);
            if (flag)
            {
                return TWENTY_NINE;
            }
            else
            {
                return TWENTY_EIGHT;
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
        int startDateM = startCal.get(Calendar.MONTH);
        int startDateY = startCal.get(Calendar.YEAR);
        int endDateM = endCal.get(Calendar.MONTH);
        int endDateY = endCal.get(Calendar.YEAR);
        int interval = (endDateY * 12 + endDateM) - (startDateY * 12 + startDateM);
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
        String returnStr;
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
        String returnStr;
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
        String returnStr;
        SimpleDateFormat f = new SimpleDateFormat(format);
        Date date = new Date();
        returnStr = f.format(date);
        return returnStr;
    }

    /**
     * 返回时间格式
     *
     * @param format format
     * @param date date
     * @return String
     */
    public static String getTimeFormat(String format, Date date)
    {
        String returnStr;
        SimpleDateFormat f = new SimpleDateFormat(format);
        returnStr = f.format(date);
        return returnStr;
    }

    public static String getTimeFormat(Date date)
    {
        String returnStr;
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
        return simpledateformat.format(calendar.getTime());
    }

    /**
     * 根据日期获取星期
     *
     * @param strDate strdate
     * @return String
     */
    public static String getWeekDayByDate(String strDate)
    {
        final String[] dayNames = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        try
        {
            date = sdfInput.parse(strDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0){
            dayOfWeek = 0;
        }
        return dayNames[dayOfWeek];
    }

    /**
     * 返回指定格式的字符型日期
     *
     * @param date date
     * @param formatString formatString
     * @return String
     */
    public static String date2String(Date date, String formatString)
    {
        if (null == date)
        {
            return null;
        }
        SimpleDateFormat simpledateformat = new SimpleDateFormat(formatString);
        return simpledateformat.format(date);
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
        return simpledateformat.format(calendar.getTime());
    }

    /**
     * 格式时间
     *
     * @param source source
     * @param format format
     * @return Date
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
        if (tmpDate != null){
            tmpString = (new SimpleDateFormat(dstDateFormat)).format(tmpDate);
        }
        if (tmpString != null){
            rtDate = (new SimpleDateFormat(dstDateFormat)).parse(tmpString, new ParsePosition(0));
        }
        return rtDate;
    }

    /**
     * 获取当前时间前几个月的时间
     *
     * @param n
     *        前几个月
     * @return Date
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
     * @param n v
     * @return Date
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
     * @param n n
     * @return Date
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
     * @param n n
     * @return Date
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
     * @param n n
     * @return Date
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
     * @param year year
     * @param month month
     * @return Date
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
     * @param year year
     * @param month month
     * @return Date
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
     * @param  dBegin dBegin
     * @param  dEnd dEnd
     * @return List<String>
     */
    public static List<String> findDates(Date dBegin, Date dEnd)
    {
        List<String> lDate = new ArrayList<>();
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
     * @param dBegin dBegin
     * @param  dEnd dEnd
     * @return List<String>
     */
    public static List<String> findMonths(Date dBegin, Date dEnd)
    {
        List<String> lDate = new ArrayList<>();
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
     * @param  strDate strDate
     * @return Date
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
     * @param strDate strDate
     * @return Date
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
    {
        // 结果为“0”是上午 结果为“1”是下午
        GregorianCalendar ca = new GregorianCalendar();
        return ca.get(GregorianCalendar.AM_PM);
    }
    
    /**
     * 计算两个时间的差值，spec为 SECOND 秒，MINUTE 分，HOUR 时，DAY 天
     * @param  d1 d1
     * @param  d2 d2
     * @param  spec spec
     * @return int
     */
    public static int getDifferTime(Date d1,Date d2,int spec){
        long t1 = d1.getTime()-d2.getTime();
        int result;
        switch(spec){
            case SECOND:
                result = (int)(t1/1000);
                break;
            case MINUTE:
                result = (int)(t1/1000/60);
                break;
            case HOUR:
                result = (int)(t1/1000/60/60);
                break;
            case DAY:
                result = (int)(t1/1000/60/60/24);
                break;
            default:
                result = 0;
        }
        return result;
    }

    /**
     * 得到当前月的第一天
     * @param format format
     * @return String
     */
    public static String getCurMonthFirstDate(String format) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        //设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH,1);
        return DateTimeUtils.getTimeFormat("yyyy-MM-dd",c.getTime());
    }

    /**
     * 获取下周一的日期
     * @return
     */
    public static Date getNextMonday() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int week = c.get(Calendar.DAY_OF_WEEK);
        if(week < 2){
            //小于2只有周日,只需往后加1天即可
            c.add(Calendar.DATE,1);
        }else{
            c.add(Calendar.DATE,7 - week + 2);
        }

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    public static Date endTimeOfTheDate(Date d){
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }
    public static Date startTimeOfTheDate(Date d){
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }


}