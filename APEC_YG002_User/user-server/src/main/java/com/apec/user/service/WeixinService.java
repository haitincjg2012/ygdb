package com.apec.user.service;

import com.apec.user.vo.WeixinShareVO;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wubi on 2017/10/9.
 */
public interface WeixinService {

    WeixinShareVO getWeixinInfo(WeixinShareVO weixinShareVO) throws IOException, NoSuchAlgorithmException;
}
