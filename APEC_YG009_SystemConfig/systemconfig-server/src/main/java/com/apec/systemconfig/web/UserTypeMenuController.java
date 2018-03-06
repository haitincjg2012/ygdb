package com.apec.systemconfig.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.UserType;
import com.apec.systemconfig.service.UserTypeMenuService;
import com.apec.systemconfig.vo.UserTypeMenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hmy on 2017/12/1.
 *
 * @author hmy
 */
@RestController
@RequestMapping("/userTypeMenu")
public class UserTypeMenuController extends MyBaseController {

    @Autowired
    private UserTypeMenuService userTypeMenuService;

    @RequestMapping(value = "/findMenuByUserType",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String findMenuByUserType(@RequestBody String json){
        try{
            UserTypeMenuVO userType = getFormJSON(json,UserTypeMenuVO.class);
            return getResultJSONStr(true,userTypeMenuService.findMenuByUserType(userType.getUserType()),null);
        }catch (Exception e){
            return getResultJSONStr(false,null, Constants.SYS_ERROR);
        }
    }


}
