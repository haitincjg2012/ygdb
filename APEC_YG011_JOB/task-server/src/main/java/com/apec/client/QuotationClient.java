package com.apec.client;

import com.apec.framework.common.Constants;
import com.apec.framework.common.ResultData;
import com.apec.framework.log.InjectLogger;
import org.slf4j.Logger;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by hmy on 2017/9/28.
 * @author hmy
 */
@FeignClient(name = "yg-society-service", fallback=QuotationClient.HystrixClientFallback.class)
public interface QuotationClient {

    /**
     * 添加行情竞猜
     * @return 任务处理结果
     */
    @RequestMapping(value = "/societyPost/newQuotation",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultData<String> newQuotation();

    @Component
    class HystrixClientFallback implements QuotationClient {
        @InjectLogger
        Logger logger;

        @Override
        public ResultData<String> newQuotation() {
            String serverName = "yg-society-service";
            String url = "/societyPost/newQuotation";
            String msg = String.format("调用服务:%s  URL:%s  发生异常,进入fallback方法", serverName, url);
            logger.info(msg);
            ResultData<String> resultData = new ResultData<>();
            resultData.setErrorCode(Constants.SYS_ERROR);
            resultData.setErrorMsg(msg);
            return resultData;
        }
    }

}
