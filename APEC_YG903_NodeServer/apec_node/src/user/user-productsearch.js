'use strict';

var router,bodyParser;
router = require('express').Router();
module.exports = router;
bodyParser = require('body-parser')();
var config = require('config');
var redis = require('../midware-server/redis');
var ef = require('../ef');
var resdata = require('../resdata');

//用户搜索关键字保存
router.post('/_node_user/_search_save' + config.urlSuffix , bodyParser, function(req, res, next) {
    var token = req.get("token");
    var keyWord = req.body.keyWord;
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
             var searchArr = [];
             if(obj){
                searchArr = obj.split(",");
                for(var j = 0 ; j < searchArr.length; j ++){
                   if(searchArr[j] == keyWord )   return done(true,"");
                }
             }
             if(searchArr.length >= 3){
                 searchArr.splice(3,searchArr.length-3);
             }
             searchArr.unshift(keyWord);
             redis.hset(config.userInfoPrefix + json, config.userSearchKey, searchArr.join(","));
           } catch (e) {
              console.error(e);
           }
           return done(true,"");
       });
    });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
