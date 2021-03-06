package com.apec.product.task.callback;

import com.apec.framework.common.Constants;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.enumtype.MailType;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.mail.service.MailService;
import com.apec.framework.mail.vo.Mail;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * Created by wubi on 2017/8/14.
 * @author wubi
 */
@Component
public class OffSellJobFinishTask implements ListenableFutureCallback<ResultData> {

    @InjectLogger
    Logger logger;

    @Autowired
    private MailService mailService;

    @Override
    public void onFailure(Throwable throwable) {
        logger.error("call off sell job callback method failed : {}" , throwable.getMessage());
    }

    @Override
    public void onSuccess(ResultData result) {

        if(!result.getErrorCode().equals(Constants.RETURN_SUCESS)){
            //发送执行失败邮件
            Mail mail = new Mail();
            mail.setSubject("定时任务执行失败");
            mail.setContent("错误详情: \n" + result.getErrorMsg());
            mail.setType(MailType.TEXT);
            try {
                mailService.sendMail(mail, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            logger.info("call off sell job success");
        }
    }
}
