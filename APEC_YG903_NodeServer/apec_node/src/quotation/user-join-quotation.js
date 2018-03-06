'use strict';

//用户参与行情竞猜
var router,bodyParser;
router = require('express').Router();
module.exports = router;
bodyParser = require('body-parser')();
var config = require('config');
var redis = require('../midware_server/redis');
var ef = require('../ef');
var resdata = require('../resdata');
var datautil = require('../datautil');

//用户参与行情竞猜
router.post('/_node_quotation/_join_quotation' + config.urlSuffix , bodyParser, function(req, res, next) {
    var token = req.get("token");
    var quotationId = req.body.quotationId;
    var quotationFlag = req.body.quotationFlag; // quotationFlag: true 涨  false 跌
    if(!quotationId){
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
        var highArr = [];
        var lightArr = [];
    redis.hget(config.userInfoPrefix + json, config.highQuotation, function (err, reply) {
        console.log( "redis hget high:" + reply.toString());
          if(reply){
                 if(reply.toString().indexOf(quotationId) != -1){   //check repeat value
                    return done(false,"");
                 }
                 highArr = obj.split(",");
            }
      });
    redis.hget(config.userInfoPrefix + json, config.lightQuotation, function (err, reply) {
        console.log( "redis hget light:" + reply.toString());
          if(reply){
                 if(reply.toString().indexOf(quotationId) != -1){   //check repeat value
                    return done(false,"");
                 }
                 lightArr = obj.split(",");
            }
      });

        if(quotationFlag){
          //看涨操作
            //用户看涨的行情
            return ef(done, bind$(redis, 'hget'), config.quotationPrefix + quotationId, config.quotationHighUser, function(quotationObj){
              console.info("该行情看涨的人记录，" + quotationObj);
              var quotationArr = [];
              if(quotationObj){
                 if(quotationObj.indexOf(json) != -1){   //check repeat value
                    return done(false,"");
                 }
                 quotationArr = quotationObj.split(",");
              }
              highArr.unshift(quotationId);
              quotationArr.unshift(json);
               //看涨的行情人数加1
               redis.hincrby(config.quotationPrefix + quotationId ,config.quotationHigh,1);
               //记录看涨的行情竞猜
              redis.hset(config.userInfoPrefix + json, config.highQuotation, highArr.join(","));
              //记录看涨的行情竞猜
              redis.hset(config.quotationPrefix + quotationId, config.quotationHighUser, quotationArr.join(","));
              return done(true,"");
            });
        }
        //看跌操作
            return ef(done, bind$(redis, 'hget'), config.quotationPrefix + quotationId, config.quotationLightUser, function(quotationObj){
              //该行情看涨的人记录
             
              console.info("该行情看跌的人记录，" + quotationObj);
              var quotationArr = [];
              if(quotationObj){
                 if(quotationObj.indexOf(json) != -1){   //check repeat value
                    return done(false,"");
                 }
                 quotationArr = quotationObj.split(",");
              }
              lightArr.unshift(quotationId);
              quotationArr.unshift(json);
               //看跌的行情人数加1
               redis.hincrby(config.quotationPrefix + quotationId ,config.quotationLight,1);
               //记录看跌的行情竞猜
              redis.hset(config.userInfoPrefix + json, config.lightQuotation, lightArr.join(","));
              //记录看跌的行情竞猜
              redis.hset(config.quotationPrefix + quotationId, config.quotationLightUser, quotationArr.join(","));
              return done(true,"");
            });
  });
});
function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
