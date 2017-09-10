package com.apec.framework.common.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;

/**
 * 功能：MD5签名
 * 版本：3.3
 * 修改日期：2012-08-17
 * */
public abstract class MD5
{
    private static Log log = LogFactory.getLog( MD5.class );

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset)
    {
        text = text + key;
        return DigestUtils.md5Hex( getContentBytes( text, input_charset ) );
    }

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String input_charset)
    {
        text = text + key;
        String mysign = DigestUtils.md5Hex( getContentBytes( text, input_charset ) );
        return mysign.equals( sign );
    }

    /**
     * @param content 内容
     * @param charset 编码方式
     * @return 字节数组
     */
    private static byte[] getContentBytes(String content, String charset)
    {
        if(charset == null || "".equals( charset ))
        {
            return content.getBytes();
        }
        try
        {
            return content.getBytes( charset );
        }
        catch (UnsupportedEncodingException e)
        {
            log.error( "It occured error in excuting MD5.getContentBytes case:" + e.getMessage() );
            throw new RuntimeException( "MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset );
        }
    }

}