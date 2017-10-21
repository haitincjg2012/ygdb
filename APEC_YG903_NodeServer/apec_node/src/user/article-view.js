'use strict';

//用户浏览文章，更新阅读数
var router,bodyParser;
router = require('express').Router();
module.exports = router;
bodyParser = require('body-parser')();
var config = require('config');
var redis = require('../midware_server/redis');
var ef = require('../ef');
var resdata = require('../resdata');

//用户浏览文章，更新阅读数
router.post('/_node_article/_read_article' + config.urlSuffix , bodyParser, function(req, res, next) {
    var articleId = req.body.articleId;
    if((!articleId)){
         return resdata(res,false,null,"");
    }
    var done = (succeed,response,errorMsg,errorCode) => {
        return resdata(res,succeed,response,errorMsg,errorCode);
    };
    return ef(done, bind$(redis, 'get'), config.articleAllRead ,function(results){
        var allViewNum ;
        if(results){
            allViewNum = new Number(results);
        }else{
            allViewNum = new Number(0);
        }
        redis.set(config.productAllView,allViewNum + 1);
        redis.hincrby(config.articleInfoPrefix + articleId, config.readedNumKey, 1);
        return done(true,"");
    });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
