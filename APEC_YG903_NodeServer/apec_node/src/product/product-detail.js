'use strict';

var router,bodyParser;
router = require('express').Router();
module.exports = router;
bodyParser = require('body-parser')();
var elasticsearch = require('../midware_server/elasticsearch');
var redis = require('../midware_server/redis');
var ef = require('../ef');
var config = require('config');
var resdata = require('../resdata');
var detaildata = require('../detaildata');

//供求的详情数据
router.post('/_node_product/_info' + config.urlSuffix, bodyParser, function(req, res, next) {
   var elasticId = req.body.elasticId; //搜索Id
   var token = req.get("token");   //Token
   if(!elasticId){
      return resdata(res,false,null,"没有该记录");
   }
   //Search 参数拼装
   var searchParams = {
     index: config.productIndex.index,
     body: {
       query:{
         ids:{
           type: config.productIndex.type,
           values: elasticId
         }
       }
     }
  };
  var done = (returnData,total,err) => {
    if(err != 200 ){
      console.log("#############API:/_node_product/_info/ [Error]: ")
      console.log(err);
      console.log("##############[END] ")
      return resdata(res,true, [],"没有数据!");
    }
    return resdata(res,true,returnData);
  };
  if(token){
     return ef(done, bind$(redis, 'get'), config.userTokenPrefix + token, function(json){
       if(!json){
         return ef(done, bind$(elasticsearch,"search"),searchParams,function(response,err){
           return detaildata(response.hits.hits,response.hits.total,err,done,false);
         });
       }
       return ef(done, bind$(redis, 'hget'), config.userInfoPrefix + json, config.userProSaveKey , function(productObj){
            var saveProductArr = [];
            if(productObj) saveProductArr = productObj.split(",");
            if(contains$(saveProductArr,elasticId)){
              return ef(done, bind$(elasticsearch,"search"),searchParams,function(response,err){
                return detaildata(response.hits.hits,response.hits.total,err,done,true);
              });
            }else{
              return ef(done, bind$(elasticsearch,"search"),searchParams,function(response,err){
                return detaildata(response.hits.hits,response.hits.total,err,done,false);
              });
            }
       });
    });
  }else{
    return ef(done, bind$(elasticsearch,"search"),searchParams,function(response,err){
      return detaildata(response.hits.hits,response.hits.total,err,done,false);
    });
  }
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}

function contains$ (arr,needle) {
  if(!arr) return false;
  var i;
  for (i in arr) {
    if (arr[i] == needle) return true;
  }
  return false;
}
