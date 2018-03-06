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
 * Created by wubi on 2017/9/25.
 * @author wubi
 */
@FeignClient(name = "YG-SYSTEMCONFIG-SERVICE", fallback=SysConfigClient.HystrixClientFallback.class)
public interface SysConfigClient {

    /**
     * reIndexJobFeign
     * @return 任务处理结果
     */
    @RequestMapping(value = "/esConfig/reIndexJob", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultData<String> reIndexJobFeign();

    @Component
    class HystrixClientFallback implements SysConfigClient {
        @InjectLogger
        Logger logger;

        @Override
        public ResultData<String> reIndexJobFeign() {
            String serverName = "YG-SYSTEMCONFIG-SERVICE";
            String url = "/esConfig/reIndexJob";
            String msg = String.format("调用服务:%s  URL:%s  发生异常,进入fallback方法", serverName, url);
            logger.info(msg);
            ResultData<String> resultData = new ResultData<>();
            resultData.setErrorCode(Constants.SYS_ERROR);
            resultData.setErrorMsg(msg);
            return resultData;
        }
    }
}
