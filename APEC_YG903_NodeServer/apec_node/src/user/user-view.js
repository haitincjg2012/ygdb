'use strict';

//用户视图查询
var router,bodyParser;
router = require('express').Router();
module.exports = router;
bodyParser = require('body-parser')();
var config = require('config');
var redis = require('../midware-server/redis');
var ef = require('../ef');
var resdata = require('../resdata');

//获取用户的手机验证码是否正确
router.post('/_node_user/_view' + config.urlSuffix , bodyParser, function(req, res, next) {
    var token = req.get("token");
    if(!token){
        return resdata(res,false,null,"session超时!",config.userSessionOutCode);
    }

    var done = (succeed,response,errorMsg,errorCode) => {
        return resdata(res,succeed,response,errorMsg,errorCode);
    };

    return ef(done, bind$(redis, 'get'), config.userTokenPrefix + token, function(json){
        if(!json){
           return done(false,null,"session超时!",config.userSessionOutCode);
        }
        return ef(done, bind$(redis, 'hget'), config.userInfoPrefix + json, config.userViewKey , function(obj){
           return done(true,obj);
       });
    });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
