'use strict';

var config = require('config');
var datautil = require('./datautil');
var redis = require('./midware_server/redis');
var ef = require('./ef');

//API 返回结果集
exports = module.exports = function (elastic,total,err,done,saveProductFlag) {
    var returnObj ={};
    if((!elastic) || elastic.length == 0) return done(returnObj,total,err);
    var elasticSource = elastic[0]._source;
    var elasticUserId = {"elasticId":elastic[0]._id,"userId": elasticSource.userId};
    returnObj = {
       "id": elasticUserId.elasticId,
       "skuName": elasticSource.skuName,
       "firstImageUrl": elasticSource.firstImageUrl,
       "address": elasticSource.address,
       "addressDetail": elasticSource.addressDetail,
       "showUserName": elasticSource.showUserName,
       "productTypeName": elasticSource.productTypeName,
       "amount": elasticSource.amount,
       "priceUnit": elasticSource.priceUnit,
       "mobile": elasticSource.mobile,
       "productAttrs": elasticSource.productAttrs,
       "productImages": elasticSource.productImages,
       "remark": elasticSource.remark,
       "weightUnit": elasticSource.weightUnit,
       "imgUrl":"",
       "viewNum": 0 ,   //浏览数
       "phoneNum": 0 ,   //电话数
       "saveNum": 0 ,  //收藏人数
       "saveFlag": false,   //是否收藏
       "userLevelName":"QT",  //用户等级
       "orgId":"", //用户组织
       "marketPreFix":elasticSource.marketPreFix,
       "startAmount": elasticSource.startAmount,
       "endAmount": elasticSource.endAmount,
       "weight": elasticSource.weight,
       "userTypeName": elasticSource.userTypeName,
       "userId": elasticSource.userId,
       "showCredateTime": datautil.getDateDiff(elasticSource.createDate,elasticSource.timeout)
   };
   var showSecondInfo = [];
   if(elasticSource.productAttrs){
     var objInd ;
      for(objInd in elasticSource.productAttrs){
          if(elasticSource.productAttrs[objInd].attributeShowLevel == "SECOND"){
             showSecondInfo.push(elasticSource.productAttrs[objInd].attrValue);
          }
      }
   }
   returnObj.showSecondInfo = showSecondInfo;
    //创建管道 , 匹配浏览人数、电话人数、用户等级,　用户的登
    try {
      var resultManager = [];
      var pipeline = redis.pipeline();
      pipeline.hgetall(config.productInfoPrefix + elasticUserId.elasticId);
      pipeline.hget(config.userInfoPrefix + elasticUserId.userId,config.userDataKey);
      pipeline.hget(config.userInfoPrefix + elasticUserId.userId,config.userViewKey);

      return ef(done, bind$(pipeline, 'exec') ,function(results){
          if(!results) return done(returnObj,total,err);
          returnObj.phoneNum  = (results[0][1].phone_num)?results[0][1].phone_num:0;
          returnObj.viewNum = (results[0][1].view_num)?results[0][1].view_num:0;
          returnObj.saveNum = (results[0][1].save_num)?results[0][1].save_num:0;

          var userInfo = JSON.parse(results[1][1]);
          returnObj.userTypeName =(userInfo.userTypeKey)?userInfo.userTypeKey:"" ;
          returnObj.imgUrl =  (userInfo.imgUrl)?(userInfo.imgUrl):"";
          returnObj.orgId =  (userInfo.userOrgId)?(userInfo.userOrgId):"";
          returnObj.showUserName = (userInfo.name)?(userInfo.name):"";
          userInfo = JSON.parse(results[2][1]);
          returnObj.userLevelName =(userInfo.userLevelName)?userInfo.userLevelName:"QT" ;
          returnObj.userRealAuthFlag = (userInfo.userRealAuthName=="NORMAL")?true:false;
          returnObj.saveFlag = saveProductFlag;
          return done(returnObj,total,err);
      });
    } catch (e) {
       console.log("============Detail  Product Error==============");
       console.log(e);
       console.log("============Detail Product Error==============");
       return done(returnObj,total,err);
    }
};
function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
