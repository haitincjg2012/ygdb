'use strict';

//用户点赞文章
var router,bodyParser;
router = require('express').Router();
module.exports = router;
bodyParser = require('body-parser')();
var config = require('config');
var redis = require('../midware_server/redis');
var ef = require('../ef');
var resdata = require('../resdata');
var datautil = require('../datautil');

//用户点赞文章
router.post('/_node_user/_praise_article' + config.urlSuffix , bodyParser, function(req, res, next) {
    var token = req.get("token");
    var articleId = req.body.articleId;
    var praiseFlag = req.body.praiseFlag; // praiseFlag: true 点赞  false 取消点赞
    if(!articleId){
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
        return ef(done, bind$(redis, 'hget'), config.userInfoPrefix + json, config.userArtPraiseKey , function(obj){
          //用户点过赞的文章id
          var arr = [];
          if(obj){
             if(obj.indexOf(articleId) != -1 && praiseFlag){   //check repeat value
                return done(true,"");
             }
             arr = obj.split(",");
          }
          if(praiseFlag){
             arr.unshift(articleId);
             redis.hincrby(config.articleInfoPrefix + articleId ,config.praiseNumKey,1);
          }else{
             datautil.arrRemove(arr,articleId);
             redis.hincrby(config.articleInfoPrefix + articleId ,config.praiseNumKey,-1);
          }
          redis.hset(config.userInfoPrefix + json, config.userArtPraiseKey, arr.join(","));

          return done(true,"");
          
       });
    });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
