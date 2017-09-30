package com.apec.job;

import com.apec.client.ProductClient;
import com.apec.framework.common.Constants;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.enumtype.MailType;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.mail.service.MailService;
import com.apec.framework.mail.vo.Mail;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by wubi on 2017/8/7.
 * 计算下架时间定时job
 */
@Component
public class OffSellJob implements SimpleJob {
    @InjectLogger
    private Logger logger;

    @Autowired
    ProductClient productClient;

    @Autowired
    MailService mailService;

    @Override
    public void execute(ShardingContext shardingContext) {
        logger.info("================开始执行offSellJob定时任务================");

        logger.info("JobName="+shardingContext.getJobName()+",JobParameter="+shardingContext.getJobParameter());
        logger.info("ShardingItem="+shardingContext.getShardingItem()+",ShardingParameter="+shardingContext.getShardingParameter());
        logger.info("ShardingTotalCount="+shardingContext.getShardingTotalCount());

        //调用下架定时服务
        ResultData<String> resultData = productClient.offSellJobFeign();
        //如果不成功则发送邮件通知
        if(!resultData.getErrorCode().equals(Constants.RETURN_SUCESS)){
            Mail mail = new Mail();
            mail.setType(MailType.TEXT);
            mail.setSubject("定时任务调度失败[JobName:"+ shardingContext.getJobName() +"]");
            mail.setContent(resultData.getErrorMsg());
            try {
                mailService.sendMail(mail, true);
            } catch (Exception e) {
                logger.error("发送邮件失败:", e);
            }
        }

        logger.info("================结束执行offSellJob定时任务================");
    }
}
