package com.apec.framework.rockmq.client;

import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.springcloud.SpringCloudClient;
import com.apec.framework.vo.MQMessageLogVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Title: 远程连接调用
 *
 * @author yirde  2017/7/4.
 */
@Component
public class RemotingClient {

    @InjectLogger
    private Logger logger;

    @Autowired
    private SpringCloudClient springCloudClient;

    /**
     * //Mq消息的URL
     */
    @Value("${mq_message_url}")
    private String mqMessageUrl;

    /**
     * 推送 MQ 消息发送错误的数据
     * @param messageLogVO 消息日志
     */
    public void sendMqProducerFailLog(MQMessageLogVO messageLogVO){
        try {
            String paramStr =  BaseJsonUtil.toJSONString(messageLogVO);
            logger.info("Send the  MQ Producer Fail MessageLog  Start. paramSr:{}", paramStr);
            String respStr = springCloudClient.post(mqMessageUrl, paramStr);
            logger.info("Send the  MQ Producer Fail MessageLog End. respStr:{}", respStr);
            ResultData resultData= BaseJsonUtil.parseObject(respStr, ResultData.class);
            if((!resultData.isSucceed()) && StringUtils.isNotBlank(resultData.getErrorCode())){
                logger.error(mqMessageUrl
                        +"\n result:"+resultData.getErrorCode()+",msg:"+resultData.getErrorMsg());
                throw new BusinessException(ErrorCodeConst.ERROR_MESSAGE_SAVEMQMESSAGE_FAIL);
            }
        }catch (Exception ex){
            logger.error("Send the MQ Producer Fail MessageLog Failed. ",ex);
            //TODO  MQ消息失败消息落地异常，发邮件、或者微信通知管理员

        }
    }

}
