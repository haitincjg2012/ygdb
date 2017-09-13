'use strict';

var router,bodyParser;
router = require('express').Router();
module.exports = router;
bodyParser = require('body-parser')();
var config = require('config');
var redis = require('../midware_server/redis');
var ef = require('../ef');
var resdata = require('../resdata');

//用户的创建供求的数量验证
router.post('/_node_user/_check_pronum' + config.urlSuffix , bodyParser, function(req, res, next) {
    var token = req.get("token");
    if(!token){
        return resdata(res,false,null,"session超时!",config.userSessionOutCode);
    }

    var done = (succeed,response,errorMsg,errorCode) => {
        return resdata(res,succeed,response,errorMsg,errorCode);
    };
    var returnObj = {
       "checkStatus": false,
       "realAuth":false,
       "realInfo":false
    };
    return ef(done, bind$(redis, 'get'), config.userTokenPrefix + token, function(json){
        if(!json){
           return done(false,null,"session超时!",config.userSessionOutCode);
        }
        return ef(done, bind$(redis, 'hget'), config.userInfoPrefix + json, config.userDataKey, function(obj){
           try {
             obj = JSON.parse(obj);
             if(!(obj && obj.name)){
                 returnObj.realInfo =  false;
                 return done(true,returnObj);
             }
             returnObj.realInfo =  true;
             return ef(done, bind$(redis, 'hget'), config.userInfoPrefix + json, config.userViewKey , function(userObj){
                 try{
                    userObj = JSON.parse(userObj);
                    if(userObj.userRealAuthName && userObj.userRealAuthName == 'NORMAL'){
                        returnObj.realAuth = true;
                        returnObj.checkStatus = true;
                        return done(true,returnObj);
                    }
                    returnObj.realAuth =  false;
                    return ef(done, bind$(redis, 'hget'), config.userInfoPrefix + json, config.userProductNumKey , function(numObj){
                        try{
                          if(!numObj) numObj = 0

                          if(numObj > config.createProductNum &&  returnObj.realAuth ==  false ){
                              returnObj.checkStatus =  false;
                          }else{
                              returnObj.checkStatus =  true;
                          }
                        } catch (e) {
                           console.error(e);
                       }
                       return done(true,returnObj);
                    });
                  } catch (e) {
                     console.error(e);
                     return done(true,returnObj);
                 }
              });
           } catch (e) {
             console.error(e);
             return done(true,returnObj);
           }
       });
    });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
