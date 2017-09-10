'use strict';

var config = require('config');
var datautil = require('./datautil');
var redis = require('./midware-server/redis');
var ef = require('./ef');

//API 返回结果集
exports = module.exports = function (elastic,total,err,done,saveProductArr) {
    var returnData =[];
    if((!elastic) || elastic.length == 0) return done(returnData,total,err);
    var returnMsg,elasticSource,showSecondInfo;
    var elasticUserId = [],userId = [];
    elastic.forEach(function(elasticObj,index){
      elasticSource = elasticObj._source;
      if(!elasticSource){
          console.warn("=============listData: _source not found. Id:"+elasticObj._id);
          return;
      }
      elasticUserId.push({"elasticId":elasticObj._id,"userId": elasticSource.userId});
      userId.push({"userId":elasticSource.userId,"userLevelName":"QT"});
      returnMsg = {
         "id": elasticObj._id,
         "skuName": elasticSource.skuName,
         "firstImageUrl": elasticSource.firstImageUrl,
         "address": elasticSource.address,
         "showUserName": elasticSource.showUserName,
         "productTypeName": elasticSource.productTypeName,
         "amount": elasticSource.amount,
         "priceUnit": elasticSource.priceUnit,
         "weightUnit": elasticSource.weightUnit,
         "viewNum": 0 ,   //浏览数
         "phoneNum": 0 ,   //电话数
         "saveNum": 0 ,  //收藏人数
         "saveFlag": false,   //是否收藏
         "userLevelName":"QT",  //用户等级
         "marketPreFix":elasticSource.marketPreFix,
         "startAmount": elasticSource.startAmount,
         "endAmount": elasticSource.endAmount,
         "weight": elasticSource.weight,
         "userTypeName": elasticSource.userTypeName,
         "userId": elasticSource.userId,
         "showCredateTime": datautil.getDateDiff(elasticSource.createDate,elasticSource.timeout)
     };
     showSecondInfo = [];
     if(elasticSource.productAttrs){
       var objInd ;
        for(objInd in elasticSource.productAttrs){
            if(elasticSource.productAttrs[objInd].attributeShowLevel == "SECOND"){
               showSecondInfo.push(elasticSource.productAttrs[objInd].attrValue);
            }
        }
     }
     returnMsg.showSecondInfo = showSecondInfo;
     returnData.push(returnMsg);
    });
    // if(!returnData) return done(returnData,total,err);   // 为空则返回
    //创建管道 , 匹配浏览人数、电话人数、用户等级,　用户的登
    try {
      var resultManager = [];
      var pipeline =  redis.pipeline();
      elasticUserId.forEach(function(obj,index){
         pipeline.hgetall(config.productInfoPrefix + obj.elasticId);
      });
      uniqueArr(userId);
      userId.forEach(function(obj,index){
         pipeline.hget(config.userInfoPrefix + obj.userId,config.userViewKey);
      });
      return ef(done, bind$(pipeline, 'exec') ,function(results){
          if(!results) return done(returnData,total,err);
          var i  = 0 ;
          for(i = 0 ; i < elasticUserId.length; i++){
             elasticUserId[i].phone_num  = (results[i][1].phone_num)?results[i][1].phone_num:0;
             elasticUserId[i].view_num = (results[i][1].view_num)?results[i][1].view_num:0;
             elasticUserId[i].save_num = (results[i][1].save_num)?results[i][1].save_num:0;
          }
          for(var j = 0  ; j < userId.length ; j++ ){
              if(results[i][1] && results[i][1].userLevelName) userId[j].userLevelName = results[i][1].userLevelName;
              i++;
          }
          var flagUserName;
          elasticUserId.forEach(function(obj,ind){
              flagUserName =  userId.filter((p) => {
                  return p.userId == obj.userId;
              })[0];
              if(flagUserName) obj.userLevelName = flagUserName.userLevelName;
              flagUserName = null;
          });
          userId = [];
          var userObj,saveProduct;
          returnData.forEach(function(obj,ind){
             userObj = elasticUserId.filter((p) => {
                 return p.elasticId == obj.id;
             })[0];
             if(saveProductArr.length > 0){
               saveProduct = saveProductArr.filter((p) => {
                  return p == obj.id;
               })[0];
               if(saveProduct){
                   obj.saveFlag = true;
                   saveProduct = null;
               }
             }
             if(userObj){
               obj.viewNum = userObj.view_num;
               obj.phoneNum = userObj.phone_num;
               obj.userLevelName = userObj.userLevelName;
               obj.saveNum = userObj.save_num;
             }
             userObj = null;
         });
         elasticUserId = [];
         saveProductArr = [];
         return done(returnData,total,err);
      });
    } catch (e) {
       console.log("============List Product Error==============");
       console.log(e);
       console.log("============List Product Error==============");
       return done(returnData,total,err);
    }
};
function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}

function uniqueArr(arr) {
	 for (var i = 0; i < arr.length; i++) {
			 for (var j =i+1; j <arr.length; ) {
					 if (arr[i].userId == arr[j].userId && arr[i].userLevelName == arr[j].userLevelName) {
							 arr.splice(j, 1);
					 }
					 else j++;
			 }
	 }
};
