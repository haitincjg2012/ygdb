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
var pageData = require('../pagedata');
var listData = require('../listdata');

//热门推荐的API
router.post('/_node_product/_recommend' + config.urlSuffix, bodyParser, function(req, res, next) {
   var pageNum = req.body.pageNumber?req.body.pageNumber:1;  //页数
   var perPage = config.elasticsearchSize; //每页的大小
   var token = req.get("token");
   //call back
   var done = (returnData,total,err) => {
       if(err != 200 ){
         console.log("#############API:/_node_product/_recommend/ [Error]: ")
         console.log(err);
         console.log("##############[END] ")
         return resdata(res,true,pageData(pageNum,0,[]),"没有数据!");
       }
       return resdata(res,true,pageData(pageNum,Math.ceil(total / perPage),returnData ));
   };
   return ef(done, bind$(redis, 'get'), config.productReCommendKey, function(recomData){
        var arr = [];
        if(!recomData) return done(arr,0,400); //没有数据
        arr = recomData.split(",");
        var recomArr = [],j = 0 ;
        for(var i = (pageNum - 1) * perPage ; i < pageNum*perPage ; i ++ ){
           if(!arr[i]) break;
           recomArr[j] = arr[i];
           j++;
        }
        if(recomArr.length == 0)  return done(arr,0,400); //没有数据
        //搜索参数
        var searchParams = {
           index: config.productIndex.index,
           body: {
             query:{
               ids:{
                 type: config.productIndex.type,
                 values: recomArr
               }
             },
             from:0,
             size: perPage
           }
       };
       var saveProductArr = [];
       if(token){
          return ef(done, bind$(redis, 'get'), config.userTokenPrefix + token, function(json){
            if(!json){
              return ef(done, bind$(elasticsearch,"search"),searchParams,function(response,err){
                return listData(response.hits.hits,response.hits.total,err,done,saveProductArr);
              });
            }
            return ef(done, bind$(redis, 'hget'), config.userInfoPrefix + json, config.userProSaveKey , function(productObj){
                 if(productObj) saveProductArr = productObj.split(",");
                 return ef(done, bind$(elasticsearch,"search"),searchParams,function(response,err){
                   return listData(response.hits.hits,response.hits.total,err,done,saveProductArr);
                 });
            });
         });
       }else{
          // Don't have  session Token , to be null .
         return ef(done, bind$(elasticsearch,"search"),searchParams,function(response,err){
           return listData(response.hits.hits,response.hits.total,err,done,saveProductArr);
         });
       }
    });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
