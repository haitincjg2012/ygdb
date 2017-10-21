package com.apec.cms.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.regex.Pattern;
/**
 * Title:
 *
 * @author yirde  2017/5/19.
 */
public class CharUtil {

    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    // 完整的判断中文汉字和符号
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

    // 只能判断部分CJK字符（CJK统一汉字）
    public static boolean isChineseByREG(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
        return pattern.matcher(str.trim()).find();
    }

    /**
     * 时间转字符串
     */
    public static String getStrFromDate(Date date){
        String str = "";
        Long now = (new Date()).getTime();
        Long dateMillis = date.getTime();
        Long diff = now - dateMillis;
        Date t = new Date(diff);
        if(t.getYear() > 1970){
            str = (t.getYear() - 1970) + "年前";
        }else if(t.getMonth() > 1){
            str = (t.getMonth() - 1) + "月前";
        }else if(t.getDate() > 1){
            str = (t.getDate() - 1) + "天前";
        }else if(t.getHours() > 8){
            str = (t.getHours()) + "小时前";
        }else if(t.getMinutes() > 0){
            str = (t.getMinutes()) + "分钟前";
        }else{
            str = "刚刚";
        }
        return str;
    }

}
