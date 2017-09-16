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

//关注组织用户的标志
router.post('/_node_user/_org_atten_flag' + config.urlSuffix , bodyParser, function(req, res, next) {
    var token = req.get("token");
    var orgId = req.body.orgId;
    var done = (succeed,response,errorMsg,errorCode) => {
        return resdata(res,succeed,response,errorMsg,errorCode);
    };
    var returnObj = {
        "attenFlag":false
    };
    if((!token) || (!orgId)) return  done(true,returnObj);

    return ef(done, bind$(redis, 'get'), config.userTokenPrefix + token, function(json){
        if(!json) return  done(true,returnObj);
        return ef(done, bind$(redis, 'hget'), config.userInfoPrefix + json, config.userOrgSaveKey , function(obj){
          if(obj){
             var objArr = obj.split(",");
             if(objArr.indexOf(orgId) != -1){   //check repeat value
                returnObj.attenFlag = true;
                return done(true,returnObj);
             }
          }
         return done(true,returnObj);
       });
    });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
