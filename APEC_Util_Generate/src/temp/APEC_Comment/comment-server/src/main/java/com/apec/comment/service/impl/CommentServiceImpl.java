package com.apec.comment.service.impl;

import com.apec.comment.service.CommentService;
import com.apec.framework.dto.UserInfoVO;
import com.apec.framework.log.InjectLogger;
import com.apec.product.util.SnowFlakeKeyGen;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private SnowFlakeKeyGen idGen;

    @InjectLogger
    private Logger logger;


    @Override
    public String addProductAttr(UserInfoVO userInfoVO) {
        return null;
    }
}
