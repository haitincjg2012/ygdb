'use strict';

//用户浏览
var router,bodyParser;
router = require('express').Router();
module.exports = router;
bodyParser = require('body-parser')();
var config = require('config');
var redis = require('../midware_server/redis');
var ef = require('../ef');
var resdata = require('../resdata');

//代办客商仓库浏览数、关注数、电话数查询
router.post('/_node_user_org/_view_org_info' + config.urlSuffix , bodyParser, function(req, res, next) {
    var orgId = req.body.orgId;
    if(!orgId){
         return resdata(res,false,null,"");
    }
    var done = (succeed,response,errorMsg,errorCode) => {
        return resdata(res,succeed,response,errorMsg,errorCode);
    };
    var pipeline =  redis.pipeline();
    pipeline.hget(config.userOrgInfoPrefix + orgId,config.userOrgViewNumPrefix);
    pipeline.hget(config.userOrgInfoPrefix + orgId,config.userOrgPhoneKey);
    pipeline.hget(config.userOrgInfoPrefix + orgId,config.userOrgAttentionKey);
    return ef(done, bind$(pipeline, 'exec') ,function(results){
        var returnObj = {
          "viewNum": 0,
          "phoneNum": 0,
          "attenNum": 0
        };
        try {
           if(results){
               returnObj.viewNum = results[0][1]? results[0][1]:0;
               returnObj.phoneNum = results[1][1]? results[1][1]:0;
               returnObj.attenNum = results[2][1] && results[2][1]>0 ? results[2][1]:0;
           }
        } catch (e) {
            console.error(e);
        }
        return done(true,returnObj);
    });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
