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

//供求大厅的列表查询
router.post('/_node_product/_all' + config.urlSuffix, bodyParser, function(req, res, next) {
   var pageNum = req.body.pageNumber?req.body.pageNumber:1;  //页数
   var orderType = req.body.orderType; //排序类型
   var searchType = req.body.searchType; //搜索类型
   var keyWord = req.body.keyWord; //搜索关键字
   var perPage = config.elasticsearchSize; //每页的大小
   var token = req.get("token");   //Token

   var flag = true;
   if (!(keyWord && keyWord.replace(/\s+/g, "").length !=0)) {
      keyWord = "";
      flag = false;
   }
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
  }
  switch(orderType){
    case "ZHPX":
      searchParams.body.sort = {
        "_score":{
           "order":"desc"
         },
         "orderWeight": {
            "order": "desc"
         }
      }
      break;
    case "DATEDES":
      searchParams.body.sort = {
        "_score":{
           "order":"desc"
         },
         "createDate": {
            "order": "desc"
         }
      }
      break;
    case "DATEASC":
      searchParams.body.sort = {
        "_score":{
           "order":"desc"
         },
         "createDate": {
            "order": "asc"
         }
      }
      break;
    case "GYTYPE":
      searchParams.body.query.bool.filter = {
          "term" : {"productTypeName":"供应"}
      };
      searchParams.body.sort = {
        "_score":{
           "order":"desc"
         },
         "createDate": {
            "order": "desc"
         }
      };
      break;
    case "QGTYPE":
      searchParams.body.query.bool.filter = {
          "term" : {"productTypeName":"求购"}
      };
      searchParams.body.sort = {
        "_score":{
           "order":"desc"
         },
         "createDate": {
            "order": "desc"
         }
      };
      break;
  }
  if(flag || searchType){
    if(!searchType) searchType = "";
    searchParams.body.query.bool.must =  {
         multi_match: {
           query:  searchType + keyWord,
           fields: config.productSearchFields,
           type:  "cross_fields",
           operator: "and" ,
           minimum_should_match: "30%"
         }
     };
  };
  var done = (returnData,total,err) => {
    if(err != 200 ){
      console.log("#############API:/_node_product/_all/ [Error]: ")
      console.log(err);
      console.log("##############[END] ")
      return resdata(res,true,pageData(pageNum,0,[]),"没有数据!");
    }
    return resdata(res,true,pageData(pageNum,Math.ceil(total / perPage),returnData ));
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

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
