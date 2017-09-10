'use strict';

var router,bodyParser;
router = require('express').Router();
module.exports = router;
bodyParser = require('body-parser')();
var config = require('config');
var redis = require('../midware-server/redis');
var ef = require('../ef');
var resdata = require('../resdata');

//获取用户的Message条数
router.post('/_node_user/_message' + config.urlSuffix , bodyParser, function(req, res, next) {
    var token = req.get("token");
    if(!token){
        return resdata(res,true,0);
    }

    var done = (succeed,response,errorMsg,errorCode) => {
        return resdata(res,succeed,response,errorMsg,errorCode);
    };

    return ef(done, bind$(redis, 'get'), config.userTokenPrefix + token, function(json){
        if(!json){
           return done(true,0);
        }
        return ef(done, bind$(redis, 'hget'), config.userInfoPrefix + json, config.userMessageCountKey , function(obj){
           return done(true,obj);
       });
    });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
