package com.apec.mqcenter.web;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;
import com.apec.framework.log.InjectLogger;


/**
 *  模块接口
 *
 * @author yirder
 */
@RestController
@RequestMapping("/mqCenter")
public class MqCenterController extends MyBaseController {

    @InjectLogger
   private Logger log;
    

}
