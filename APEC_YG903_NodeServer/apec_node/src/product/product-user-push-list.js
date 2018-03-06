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

//用户的发布的供求列表
router.post('/_node_product/_user_push_list' + config.urlSuffix, bodyParser, function(req, res, next) {
   var pageNum = req.body.pageNumber?req.body.pageNumber:1;  //页数
   var perPage = config.elasticsearchSize; //每页的大小
   var token = req.get("token");   //Token
   if(!token) return resdata(res,false,null,"session超时!",config.userSessionOutCode);

   //call back
   var done = (returnData,total,err) => {
     if(err != 200 ){
       console.log("#############API:/_node_product/_user_push_list/ [Error]: ")
       console.log(err);
       console.log("##############[END] ")
       return resdata(res,true,pageData(pageNum,0,0,[]),"没有数据!");
     }
     return resdata(res,true,pageData(pageNum,Math.ceil(total / perPage),total,returnData ));
   };

   return ef(done, bind$(redis, 'get'), config.userTokenPrefix + token, function(json){
       if(!json)  return done([],0,400);
       var searchParams = {
              index: config.productIndex.index,
              type: config.productIndex.type,
              body: {
                query:{
                  bool:{}
                },
                from: (pageNum - 1) * perPage,
                size: perPage
              }
          };
          searchParams.body.query.bool.filter = {
              "term" : {"userId":json}
          };
          searchParams.body.sort = {
            "_score":{
               "order":"desc"
             },
             "createDate": {
                "order": "desc"
             }
          };
       return ef(done, bind$(elasticsearch,"search"),searchParams,function(response,err){
            return listData(response.hits.hits,response.hits.total,err,done,[]); 
          });
   });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
