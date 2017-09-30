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
var listData = require('./listdata-depot');

//找冷库的列表查询
router.post('/_node_user_org/_depot_list' + config.urlSuffix, bodyParser, function(req, res, next) {
   var pageNum = req.body.pageNumber?req.body.pageNumber:1;  //页数
   var searchType = req.body.searchType; //搜索类型
   var keyWord = req.body.keyWord; //搜索关键字
   var perPage = config.elasticsearchSize; //每页的大小
   var flag = true;
   if (!(keyWord && keyWord.replace(/\s+/g, "").length !=0)) {
      keyWord = "";
      flag = false;
   }
   //Search 参数拼装
   var searchParams = {
      index: config.orgDepotIndex.index,
      type: config.orgDepotIndex.type,
      body: {
        query:{
          bool:{}
        },
        from: (pageNum - 1) * perPage,
        size: perPage
      }
  }
  searchParams.body.sort = {
    "_score":{
       "order":"desc"
     },
     "createDate": {
        "order": "desc"
     }
  };
  switch(searchType){
    case "QYRZ":
      searchParams.body.query.bool.filter = {
         "term" : {"orgTags.tagName":"企业认证"}
      };
      break;
    case "GYLJRHZK":
      searchParams.body.query.bool.filter = {
          "term" : {"orgTags.tagName":"供应链金融合作库"}
      };
      break;
  }
  if(flag || searchType){
    if(!searchType) searchType = "";
    searchParams.body.query.bool.must =  {
         multi_match: {
           query:  searchType + keyWord,
           fields: config.orgDepotSearchFields,
           type:  "cross_fields",
           operator: "and" ,
           minimum_should_match: "30%"
         }
     };
  };
  var done = (returnData,total,err) => {
    if(err != 200 ){
      console.log("#############API:/_node_user_org/_depot_list/ [Error]: ")
      console.log(err);
      console.log("##############[END] ")
      return resdata(res,true,pageData(pageNum,0,[]),"没有数据!");
    }
    return resdata(res,true,pageData(pageNum,Math.ceil(total / perPage),returnData ));
  };
  return ef(done, bind$(elasticsearch,"search"),searchParams,function(response,err){
      return listData(response.hits.hits,response.hits.total,err,done);
   });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
