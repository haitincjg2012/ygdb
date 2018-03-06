package com.apec.cms.util;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;
/**
 * Title:
 *
 * @author yirde  2017/5/19.
 */
public class CharUtil {

    private static final Pattern PATTERN = Pattern.compile("[\\u4E00-\\u9FBF]+");

    /**
     * 根据Unicode编码完美的判断中文汉字和符号
     * @param c c
     * @return boolean
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        boolean flag = ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
        return flag;
    }

    /**
     * 完整的判断中文汉字和符号
     * @param strName strName
     * @return boolean
     */
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 只能判断部分CJK字符（CJK统一汉字）
     * @param str str
     * @return boolean
     */
    public static boolean isChineseByREG(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return PATTERN.matcher(str.trim()).find();
    }


    /**
     * 时间转字符串
     */
    public static String getStrFromDate(Date date){
        String str;
        Date t = new Date();

        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(t);

        int year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        long month = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
        long day = c2.get(Calendar.DATE) - c1.get(Calendar.DATE);
        long hour = c2.get(Calendar.HOUR) - c1.get(Calendar.HOUR);
        long minute = c2.get(Calendar.MINUTE) - c1.get(Calendar.MINUTE);

//        Long now = t.getTime();
//        Long dateMillis = date.getTime();
//        Long diff = now - dateMillis;

//        long year = diff/(1000*60*60*24*365);
//        long month = diff/(1000*60*60*24*30);
//        long day = diff/(1000*60*60*24);
//        long hour = diff/(1000*60*60);
//        long minute = diff/(1000*60);
        if(year > 0){
            str = year + "年前";
        }else if(month > 0){
            str = month + "月前";
        }else if(day > 0){
            str = day + "天前";
        }else if(hour > 0){
            str = hour + "小时前";
        }else if(minute > 0){
            str = minute + "分钟前";
        }else{
            str = "刚刚";
        }
        return str;
    }

}
