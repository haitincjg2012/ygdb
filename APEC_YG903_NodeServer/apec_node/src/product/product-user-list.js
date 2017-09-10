'use strict';

var router,bodyParser;
router = require('express').Router();
module.exports = router;
bodyParser = require('body-parser')();
var elasticsearch = require('../midware-server/elasticsearch');
var redis = require('../midware-server/redis');
var ef = require('../ef');
var config = require('config');
var resdata = require('../resdata');
var pageData = require('../pagedata');
var listData = require('../listdata');

//用户的供求收藏列表
router.post('/_node_product/_user_list' + config.urlSuffix, bodyParser, function(req, res, next) {
   var pageNum = req.body.pageNumber?req.body.pageNumber:1;  //页数
   var perPage = config.elasticsearchSize; //每页的大小
   var token = req.get("token");   //Token
   if(!token) return resdata(res,false,null,"session超时!",config.userSessionOutCode);

   //call back
   var done = (returnData,total,err) => {
     if(err != 200 ){
       console.log("#############API:/_node_product/_user_list/ [Error]: ")
       console.log(err);
       console.log("##############[END] ")
       return resdata(res,true,pageData(pageNum,0,[]),"没有数据!");
     }
     return resdata(res,true,pageData(pageNum,Math.ceil(total / perPage),returnData ));
   };

   return ef(done, bind$(redis, 'get'), config.userTokenPrefix + token, function(json){
       if(!json)  return done([],0,400);
       return ef(done, bind$(redis, 'hget'), config.userInfoPrefix + json, config.userProSaveKey , function(obj){
           var arr = [];
           if(!obj) return done(arr,0,400); //没有数据
           arr = obj.split(",");
           var pageArr = [],j = 0 ;
           for(var i = (pageNum - 1) * perPage ; i < pageNum*perPage ; i ++ ){
              if(!arr[i]) break;
              pageArr[j] = arr[i];
              j++;
           }
           if(pageArr.length == 0)  return done(arr,0,400); //没有数据
           //Search 参数拼装
           var searchParams = {
              index: config.productIndex.index,
              body: {
                query:{
                  ids:{
                    type: config.productIndex.type,
                    values: pageArr
                  }
                },
                from: 0,
                size: perPage
              }
          };
          return ef(done, bind$(elasticsearch,"search"),searchParams,function(response,err){
            return listData(response.hits.hits,response.hits.total,err,done,pageArr);
          });
       });
   });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
