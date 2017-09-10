package com.apec.framework.rockmq.producer;

import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;

/**
 * Title: MQ 发送回调
 *
 * @author yirde  2017/7/4.
 */
public class ApecMQSendCallback  implements SendCallback {

   //TODO 暂时不考虑采用异步回调的方式
    @Override
    public void onSuccess(SendResult sendResult) {

    }

    @Override
    public void onException(Throwable throwable) {

    }
}
