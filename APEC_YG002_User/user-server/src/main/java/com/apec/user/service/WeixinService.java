package com.apec.user.service;

import com.apec.user.vo.WeixinShareVO;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wubi on 2017/10/9.
 * @author hmy
 */
public interface WeixinService {

    /**
     * 获取微信信息
     * @param weixinShareVO 微信分享对象
     * @return WeixinShareVO
     * @throws  IOException IOException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    WeixinShareVO getWeixinInfo(WeixinShareVO weixinShareVO) throws IOException, NoSuchAlgorithmException;
}
