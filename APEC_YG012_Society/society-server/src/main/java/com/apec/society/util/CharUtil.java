package com.apec.society.util;

import com.apec.framework.common.Constants;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Title:
 *
 * @author yirde  2017/5/19.
 */
public class CharUtil {

    private static final Pattern PATTERN = Pattern.compile("[\\u4E00-\\u9FBF]+");

    private static long YEAR_LONG = ((long)1000)*((long)60)*((long)60)*((long)24)*((long)365);
    private static long MONTH_LONG = ((long)1000)*((long)60)*((long)60)*((long)24)*((long)30);

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
     * @param date 时间对象
     * @return 字符串
     */
    public static String getStrFromDate(Date date){
        String str;
        Date t = new Date();
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(t);

        long l1 = c1.getTimeInMillis();
        long l2 = c2.getTimeInMillis();

        long diff = l2 - l1;

        long year = diff/YEAR_LONG;
        long month = diff/MONTH_LONG;
        long day = diff/(1000*60*60*24);
        long hour = diff/(1000*60*60);
        long minute = diff/(1000*60);
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

    /**
     * 字符串转集合类型
     * @param str 字符串
     * @return 集合对象
     */
    public static List<String> getListFromStr(String str){
        List<String> list = new ArrayList<>();
        if(StringUtils.isBlank(str)){
            return list;
        }
        String[] array = str.split(Constants.COMMA);
        if(array != null && array.length > 0){
            for(String string:array){
                if(StringUtils.isNotBlank(string)){
                    list.add(string);
                }
            }
        }
        return list;
    }


}
