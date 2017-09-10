'use strict';

var router,bodyParser;
router = require('express').Router();
module.exports = router;
bodyParser = require('body-parser')();
var config = require('config');
var redis = require('../midware-server/redis');
var ef = require('../ef');
var resdata = require('../resdata');
var datautil = require('../datautil');

//用户地址保存历史记录记载
router.post('/_node_user/_save_address' + config.urlSuffix , bodyParser, function(req, res, next) {
    var token = req.get("token");
    var detailAddress = req.body.detailAddress;
    var cityId = req.body.cityId; //市级ID
    var areaId = req.body.areaId; //县级ID
    var townId = req.body.townId; //镇子ID
    if(!detailAddress){
         return resdata(res,true,null,"");
    }
    if(!token){
        return resdata(res,false,null,"session超时!",config.userSessionOutCode);
    }
    var done = (succeed,response,errorMsg,errorCode) => {
        return resdata(res,succeed,response,errorMsg,errorCode);
    };

    return ef(done, bind$(redis, 'get'), config.userTokenPrefix + token, function(json){
        if(!json){
           return done(false,null,"session超时!",config.userSessionOutCode);
        }
        var addressObj = {
            "address":detailAddress,
            "cityId":cityId,
            "areaId":areaId,
            "townId":townId
        }
        redis.hset(config.userInfoPrefix + json, config.addressSaveKey, JSON.stringify(addressObj));
        return done(true,"");
    });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
