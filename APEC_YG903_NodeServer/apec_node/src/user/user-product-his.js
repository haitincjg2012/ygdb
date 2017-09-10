'use strict';

var router,bodyParser;
router = require('express').Router();
module.exports = router;
bodyParser = require('body-parser')();
var redis = require('../midware-server/redis');
var config = require('config');
var ef = require('../ef');
var resdata = require('../resdata');

//获取用户创建产品的历史纪录
router.post('/_node_user/_product_his' + config.urlSuffix , bodyParser, function(req, res, next) {
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
        return ef(done, bind$(redis, 'hget'), config.userInfoPrefix + json, config.userProductHisKey , function(obj){
          var returnObj = [];
          try {
             if(obj){
                var objJson = eval('('+ obj + ')');
                var ind;
                for(ind in objJson){
                  returnObj.push({"skuId":ind,"skuName":objJson[ind]});
                }
             }
          } catch (e) {
              console.error(e);
          }
          return done(true,returnObj);
       });
    });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
