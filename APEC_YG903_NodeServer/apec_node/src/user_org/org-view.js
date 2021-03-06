'use strict';

var router,bodyParser;
router = require('express').Router();
module.exports = router;
bodyParser = require('body-parser')();
var config = require('config');
var redis = require('../midware_server/redis');
var ef = require('../ef');
var resdata = require('../resdata');

//代办客商冷库浏览数更新
router.post('/_node_user_org/_view_org' + config.urlSuffix , bodyParser, function(req, res, next) {
    var orgId = req.body.orgId;
    var viewType = req.body.vieType;
    if((!orgId) || (!viewType)){
         return resdata(res,false,null,"");
    }
    var proKey ;
    if(viewType == "VIEWNUM"){
       proKey = config.userOrgViewNumPrefix;
    }else if(viewType == "PHONENUM"){
       proKey = config.userOrgPhoneKey;
    }
    if(!proKey) return resdata(res,false,null,"");

    var done = (succeed,response,errorMsg,errorCode) => {
        return resdata(res,succeed,response,errorMsg,errorCode);
    };
    return ef(done, bind$(redis, 'hgetall'), config.userOrgInfoPrefix ,function(results){
        if(results){
          redis.hincrby(config.userOrgInfoPrefix + orgId, proKey, 1);
          return done(true,"");
        }else{
          return done(false,"");
        }
    });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
