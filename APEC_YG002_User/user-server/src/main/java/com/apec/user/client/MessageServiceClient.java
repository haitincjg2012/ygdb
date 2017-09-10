package com.apec.user.client;

import com.apec.framework.vo.MessageVO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Title:
 *
 * @author yirde  2017/7/3.
 */
@FeignClient(name = "YG-MESSAGE-SERVICE")
public interface MessageServiceClient {

    @RequestMapping(method = RequestMethod.POST, value = "/message/addMessage", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String addMessage(MessageVO messageVO);

}
