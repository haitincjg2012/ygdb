package com.apec.client;

import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.ResultData;
import com.apec.framework.log.InjectLogger;
import org.slf4j.Logger;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wubi on 2017/8/7.
 * yg-product-service服务调用类
 */
@FeignClient(name = "yg-product-service", fallback=ProductClient.HystrixClientFallback.class)
public interface ProductClient {
    @RequestMapping("/product/offSellJob")
    ResultData<String> offSellJobFeign();

    @Component
    class HystrixClientFallback implements ProductClient {
        @InjectLogger
        Logger logger;

        @Override
        public ResultData<String> offSellJobFeign() {
            String serverName = "yg-product-service";
            String url = "/product/offSellJob";
            String msg = String.format("调用服务:%s  URL:%s  发生异常,进入fallback方法", serverName, url);
            logger.info(msg);
            ResultData<String> resultData = new ResultData();
            resultData.setErrorCode(Constants.SYS_ERROR);
            resultData.setErrorMsg(msg);
            return resultData;
        }
    }
}
