'use strict';

var router,bodyParser;
router = require('express').Router();
module.exports = router;
bodyParser = require('body-parser')();
var config = require('config');
var redis = require('../midware_server/redis');
var ef = require('../ef');
var resdata = require('../resdata');

//清空用户的未读行情消息数
router.post('/_node_user/_clear_msgcount' + config.urlSuffix , bodyParser, function(req, res, next) {
    var token = req.get("token");
    var articleMsgType = req.body.articleMsgType;
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
        if(articleMsgType == "GIVE_THE_THUMBS_UP"){
            redis.hset(config.userInfoPrefix + json, config.userPraiseCountKey , 0);
        }
        else if(articleMsgType == "GIVE_THE_COMMENT"){
            redis.hset(config.userInfoPrefix + json, config.userReplyCountKey , 0);
        }
        else{
            redis.hset(config.userInfoPrefix + json, config.userMessageCountKey , 0);
        }
        return done(true,"");
       
    });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
