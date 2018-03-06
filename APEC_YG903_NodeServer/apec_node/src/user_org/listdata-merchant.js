'use strict';

var config = require('config');
var datautil = require('../datautil');
var redis = require('../midware_server/redis');
var ef = require('../ef');

//API 返回结果集
exports = module.exports = function (elastic,total,err,done) {
    var returnData =[];
    if((!elastic) || elastic.length == 0) return done(returnData,total,err);
    var returnMsg,elasticSource,showOrgTagsInfo;
    var elasticUserOrgId = [];
    elastic.forEach(function(elasticObj,index){
        elasticSource = elasticObj._source;
        if(!elasticSource){
            console.warn("=============listdata-Merchant: _source not found. Id:"+elasticObj._id);
            return;
        }
        elasticUserOrgId.push({"elasticId":elasticObj._id,"orgId": elasticSource.orgId,"userId":elasticSource.userId});
        returnMsg = {
           "id": elasticObj._id,
           "orgName": elasticSource.orgName,
           "orgFirstBannerUrl": elasticSource.orgFirstBannerUrl,
           "address": elasticSource.address,
           "mainOperating": elasticSource.mainOperating,
           "showOrgTagsInfo": elasticSource.orgTags,
           "userId": elasticSource.userId,
           "orgId": elasticSource.orgId,
           "userLevelName":"QT",  //用户等级
           "userRealAuthKey":"",
           "viewNum": 0     //浏览数
       };
       returnData.push(returnMsg);
    });
    try {
      var resultManager = [];
      var pipeline =  redis.pipeline();
      elasticUserOrgId.forEach(function(obj,index){
        pipeline.hget(config.userOrgInfoPrefix + obj.orgId,config.userOrgViewNumPrefix);
        pipeline.hget(config.userInfoPrefix + obj.userId,config.userViewKey);
      });
      return ef(done, bind$(pipeline, 'exec') ,function(results){
          if(!results) return done(returnData,total,err);
          var k = 0;
          var jsonObj;
          for(var i = 0 ; i < elasticUserOrgId.length; i++){
             elasticUserOrgId[i].view_num = (results[k][1])?results[k][1]:0;
             if(results[k+1][1]){
               jsonObj = JSON.parse(results[k+1][1]);
               elasticUserOrgId[i].userLevelName = (jsonObj.userLevelName)?jsonObj.userLevelName:'QT';
               elasticUserOrgId[i].userRealAuthKey = (jsonObj.userRealAuthName)=="NORMAL"?"实名认证":'';
             }else{
               elasticUserOrgId[i].userLevelName = 'QT';
               elasticUserOrgId[i].userRealAuthKey = '';
             }
             k =  k + 2;
          }
          var userObj;
          returnData.forEach(function(obj,ind){
             userObj = elasticUserOrgId.filter((p) => {
                 return p.elasticId == obj.id;
             })[0];
             if(userObj){
               obj.viewNum = userObj.view_num;
               obj.userLevelName = userObj.userLevelName;
               obj.userRealAuthKey = userObj.userRealAuthKey;
             }
             userObj = null;
         });
         return done(returnData,total,err);
      });
    } catch (e) {
       console.log("============List Merchant Error==============");
       console.log(e);
       return done(returnData,total,err);
    }
};
function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
