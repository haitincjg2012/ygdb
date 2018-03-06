package com.apec.mqcenter.client;

import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.springcloud.SpringCloudClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Title:
 *
 * @author yirde  2017/7/5.
 */
@Component
public class RemotingClient {

    @InjectLogger
    private Logger logger;

    @Autowired
    private SpringCloudClient springCloudClient;

    /**
     * 推送 MQ 消息消费错误的数据
     * @param paramJson 参数字符串
     * @param remotingURL remotingURL
     */
    public void sendMqConsumerExecute(String paramJson,String remotingURL){
        try {
            logger.info("Send the  MQ Consumer Execute  Start.ULR:{}, paramSr:{}",remotingURL, paramJson);
            String respStr = springCloudClient.post(remotingURL, paramJson);
            logger.info("Send the MQ Consumer Execute  End. respStr:{}", respStr);
            ResultData resultData= BaseJsonUtil.parseObject(respStr, ResultData.class);
            if((!resultData.isSucceed()) && StringUtils.isNotBlank(resultData.getErrorCode())){
                logger.error(remotingURL
                        +"\n result:"+resultData.getErrorCode()+",msg:"+resultData.getErrorMsg());
                throw new BusinessException(ErrorCodeConst.ERROR_MQ_CONSUMER_FAIL);
            }
        }catch (Exception ex){
            logger.error("Send the MQ Consumer Execute Failed. ",ex);
            throw new BusinessException(ErrorCodeConst.ERROR_MQ_CONSUMER_FAIL);
        }
    }

}
