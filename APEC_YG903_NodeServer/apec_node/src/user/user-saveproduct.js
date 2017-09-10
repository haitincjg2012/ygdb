'use strict';

//用户收藏商品
var router,bodyParser;
router = require('express').Router();
module.exports = router;
bodyParser = require('body-parser')();
var config = require('config');
var redis = require('../midware-server/redis');
var ef = require('../ef');
var resdata = require('../resdata');
var datautil = require('../datautil');

//用户收藏商品
router.post('/_node_user/_save_product' + config.urlSuffix , bodyParser, function(req, res, next) {
    var token = req.get("token");
    var elasticId = req.body.elasticId;
    var saveFlag = req.body.saveFlag; // saveFlag: true 收藏  false 取消收藏
    if(!elasticId){
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
        return ef(done, bind$(redis, 'hget'), config.userInfoPrefix + json, config.userProSaveKey , function(obj){
          var arr = [];
          if(obj){
             if(obj.indexOf(elasticId) != -1 && saveFlag){   //check repeat value
                return done(true,"");
             }
             arr = obj.split(",");
          }
          if(saveFlag){
             arr.unshift(elasticId);
             redis.hincrby(config.productInfoPrefix + elasticId ,config.proSaveNumKey,1);
          }else{
             datautil.arrRemove(arr,elasticId);
             redis.hincrby(config.productInfoPrefix + elasticId ,config.proSaveNumKey,-1);
          }
          redis.hset(config.userInfoPrefix + json, config.userProSaveKey, arr.join(","));

          return ef(done, bind$(redis, 'get'), config.productAllSave ,function(results){
            var allSaveNum ;
            if(results){
                allSaveNum = new Number(results);
            }else{
               allSaveNum = new Number(0);
            }
            if(saveFlag){
               redis.set(config.productAllSave,allSaveNum + 1);
            }else{
                redis.set(config.productAllSave,allSaveNum==0?0:(allSaveNum-1));
            }
            return done(true,"");
          });
       });
    });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
