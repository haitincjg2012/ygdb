package com.apec.comment.web;

import com.apec.comment.service.CommentService;
import com.apec.framework.common.ResultData;
import com.apec.framework.log.InjectLogger;
import com.apec.product.web.MyBaseController;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 评论模块接口
 * @author yirder
 */
@RestController
@RequestMapping("/comment")
public class CommentController extends MyBaseController {

    @InjectLogger
    private Logger log;

    /**
     * 评论服务
     */
    @Autowired
    private CommentService commentService;

    /**
     * 添加求购信息
     * @return ResultData
     */
    @RequestMapping(value = "/testEsInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public  ResultData<String>  testESInfo() {
        ResultData<String> resultData = new ResultData<>();
        resultData.setSucceed(true);
        return resultData;
    }


}
