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

//组织账号的供求列表查询
router.post('/_node_user/_product_list' + config.urlSuffix, bodyParser, function(req, res, next) {
   var pageNum = req.body.pageNumber?req.body.pageNumber:1;  //页数
   var orgId = req.body.orgId;   //组织ID
   var perPage = config.elasticsearchSize; //每页的大小
   //call back
   var done = (returnData,total,err) => {
     if(err != 200 ){
       console.log("#############API:/_node_org/_product_list/ [Error]: ")
       console.log(err);
       console.log("##############[END] ")
       return resdata(res,true,pageData(pageNum,0,0,[]),"没有数据!");
     }
     return resdata(res,true,pageData(pageNum,Math.ceil(total / perPage),total,returnData ));
   };

   if(!orgId) return done([],0,400);

   return ef(done, bind$(redis, 'hget'), config.userOrgInfoPrefix + orgId, config.userOrgUserInfoPrefix, function(json){
        if(!json)  return done([],0,400);
        try{
          var resutlObj = eval('(' + json + ')');
          var userIds = [];
          resutlObj.forEach(function(obj,ind){
               userIds.push(obj.userId);
          });
          if(userIds.length == 0)  return done(userIds,0,400); //没有数据
          //Search 参数拼装
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
         searchParams.body.sort = {
           "_score":{
              "order":"desc"
            },
            "createDate": {
               "order": "desc"
            }
         };
         searchParams.body.query.bool.filter = {
             "terms" : { "userId":userIds }
         };
         return ef(done, bind$(elasticsearch,"search"),searchParams,function(response,err){
             return listData(response.hits.hits,response.hits.total,err,done,[]);
         });
       } catch (e) {
          console.log("============_node_org/_product_list Error==============");
          console.log(e);
          console.log("============_node_org/_product_list Error==============");
          return done(userIds,0,400);
       }
   });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
