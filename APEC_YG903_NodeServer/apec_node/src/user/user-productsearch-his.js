'use strict';

var router,bodyParser;
router = require('express').Router();
module.exports = router;
bodyParser = require('body-parser')();
var config = require('config');
var redis = require('../midware-server/redis');
var ef = require('../ef');
var resdata = require('../resdata');

//用户搜索历史记录
router.post('/_node_user/_search_his' + config.urlSuffix , bodyParser, function(req, res, next) {
    var token = req.get("token");
    if(!token){
        return resdata(res,true,null); // 为空不记录返回异常
    }

    var done = (succeed,response,errorMsg,errorCode) => {
        return resdata(res,succeed,response,errorMsg,errorCode);
    };

    return ef(done, bind$(redis, 'get'), config.userTokenPrefix + token, function(json){
        if(!json){
           return done(false,null,"session超时!",config.userSessionOutCode);
        }
        return ef(done, bind$(redis, 'hget'), config.userInfoPrefix + json, config.userSearchKey , function(obj){
           try {
             if(obj){
                obj = obj.split(",");
             }
           } catch (e) {
             console.error(e);
           }
           return done(true,obj);
       });
    });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
