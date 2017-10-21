package com.apec.user.util;

import com.apec.user.vo.WeixinShareVO;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Created by wubi on 2017/10/9.
 */
public class SignUtil {

    /**
     * 签名
     * @param jsapiTicket
     * @param url
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static WeixinShareVO sign(String jsapiTicket, String url) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        WeixinShareVO ret = new WeixinShareVO();
        String nonce_str = createNonceStr();
        String timestamp = createTimestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapiTicket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
        System.out.println(string1);


        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(string1.getBytes("UTF-8"));
        signature = byteToHex(crypt.digest());



        ret.setUrl(url);
        ret.setJsapiTicket(jsapiTicket);
        ret.setNonceStr(nonce_str);
        ret.setTimestamp(timestamp);
        ret.setSignature(signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String createNonceStr() {
        return UUIDGenerator.getUUID();
    }

    private static String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
