'use strict';

//用户浏览供求，更新数目
var router,bodyParser;
router = require('express').Router();
module.exports = router;
bodyParser = require('body-parser')();
var config = require('config');
var redis = require('../midware-server/redis');
var ef = require('../ef');
var resdata = require('../resdata');

//用户浏览供求,更新产看数 ，打电话，更新电话数
router.post('/_node_product/_view_product' + config.urlSuffix , bodyParser, function(req, res, next) {
    var elasticId = req.body.elasticId;
    var viewType = req.body.vieType;
    if((!elasticId) || (!viewType)){
         return resdata(res,false,null,"");
    }
    var proKey ;
    if(viewType == "VIEWNUM"){
       proKey = config.proViewNumKey;
    }else if(viewType == "PHONENUM"){
      proKey = config.proPhoneKey;
    }
    if(!proKey) return resdata(res,false,null,"");

    var done = (succeed,response,errorMsg,errorCode) => {
        return resdata(res,succeed,response,errorMsg,errorCode);
    };
    return ef(done, bind$(redis, 'get'), config.productAllView ,function(results){
      var allViewNum ;
      if(results){
          allViewNum = new Number(results);
      }else{
         allViewNum = new Number(0);
      }
      redis.set(config.productAllView,allViewNum + 1);
      redis.hincrby(config.productInfoPrefix + elasticId, proKey, 1);
      return done(true,"");
    });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
