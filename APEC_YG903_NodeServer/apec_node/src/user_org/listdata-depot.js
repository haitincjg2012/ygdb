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
          console.warn("=============listdata-depot: _source not found. Id:"+elasticObj._id);
          return;
      }
      elasticUserOrgId.push({"elasticId":elasticObj._id,"orgId": elasticSource.orgId});
      returnMsg = {
         "id": elasticObj._id,
         "orgName": elasticSource.orgName,
         "orgFirstBannerUrl": elasticSource.orgFirstBannerUrl,
         "orgStockCap": elasticSource.orgStockCap,
         "address": elasticSource.address,
         "mainOperating": elasticSource.mainOperating,
         "showOrgTagsInfo": elasticSource.orgTags,
         "orgId": elasticSource.orgId,
         "viewNum": 0     //浏览数
     };
     returnData.push(returnMsg);
    });
    try {
      var resultManager = [];
      var pipeline =  redis.pipeline();
      elasticUserOrgId.forEach(function(obj,index){
         pipeline.hget(config.userOrgInfoPrefix + obj.orgId,config.userOrgViewNumPrefix);
      });
      return ef(done, bind$(pipeline, 'exec') ,function(results){ 
          if(!results) return done(returnData,total,err);
          var i  = 0 ;
          for(i = 0 ; i < elasticUserOrgId.length; i++){
             elasticUserOrgId[i].view_num = (results[i][1])?results[i][1]:0;
          }
          var userObj;
          returnData.forEach(function(obj,ind){
             userObj = elasticUserOrgId.filter((p) => {
                 return p.elasticId == obj.id;
             })[0];
             if(userObj){
               obj.viewNum = userObj.view_num;
             }
             userObj = null;
         });
         return done(returnData,total,err);
      });
    } catch (e) {
       console.log("============List Depot Error==============");
       console.log(e);
       console.log("============List Depot Error==============");
       return done(returnData,total,err);
    }
};
function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
