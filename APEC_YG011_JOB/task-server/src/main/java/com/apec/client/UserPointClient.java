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
 */
@FeignClient(name = "yg-user-service", fallback=UserPointClient.HystrixClientFallback.class)
public interface UserPointClient {

    @RequestMapping(value = "/userpoint/perfectUserPoint",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultData<String> perfectUserPoint();

    @Component
    class HystrixClientFallback implements UserPointClient {
        @InjectLogger
        Logger logger;

        @Override
        public ResultData<String> perfectUserPoint() {
            String serverName = "yg-user-service";
            String url = "/userpoint/perfectUserPoint";
            String msg = String.format("调用服务:%s  URL:%s  发生异常,进入fallback方法", serverName, url);
            logger.info(msg);
            ResultData<String> resultData = new ResultData();
            resultData.setErrorCode(Constants.SYS_ERROR);
            resultData.setErrorMsg(msg);
            return resultData;
        }
    }

}
