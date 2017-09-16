'use strict';
 
var router,bodyParser;
router = require('express').Router();
module.exports = router;
bodyParser = require('body-parser')();
var config = require('config');
var redis = require('../midware_server/redis');
var ef = require('../ef');
var resdata = require('../resdata');
var datautil = require('../datautil');

//关注组织用户
router.post('/_node_user/_save_user_org' + config.urlSuffix , bodyParser, function(req, res, next) {
    var token = req.get("token");
    var orgId = req.body.orgId;
    var saveFlag = req.body.saveFlag; // saveFlag: true 收藏  false 取消收藏
    if(!orgId){
         return resdata(res,false,null,"没有该记录");
    }
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
        return ef(done, bind$(redis, 'hget'), config.userInfoPrefix + json, config.userOrgSaveKey , function(obj){
          var arr = [];
          if(obj){
             if(obj.indexOf(orgId) != -1 && saveFlag){   //check repeat value
                return done(true,"");
             }
             arr = obj.split(",");
          }
          if(saveFlag){
             arr.unshift(orgId);
             redis.hincrby(config.userOrgInfoPrefix + orgId ,config.userOrgAttentionKey,1);
          }else{
             datautil.arrRemove(arr,orgId);
             redis.hincrby(config.userOrgInfoPrefix + orgId ,config.userOrgAttentionKey,-1);
          }
          redis.hset(config.userInfoPrefix + json, config.userOrgSaveKey, arr.join(","));

          return ef(done, bind$(redis, 'hget'), config.userOrgInfoPrefix + orgId ,config.userOrgAttentUserKey ,function(results){
              var arr = [];
              if(results){
                 if(results.indexOf(json) != -1 && saveFlag){   //check repeat value
                    return done(true,"");
                 }
                 arr = results.split(",");
              }
              if(saveFlag){
                  arr.unshift(json);
              }else{
                  datautil.arrRemove(arr,json);
              }
              redis.hset(config.userOrgInfoPrefix + orgId,config.userOrgAttentUserKey,arr.join(","));
              return done(true,"");
          });
       });
    });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
