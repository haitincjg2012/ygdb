'use strict';

//用户签到
var router,bodyParser;
router = require('express').Router();
module.exports = router;
bodyParser = require('body-parser')();
var config = require('config');
var redis = require('../midware-server/redis');
var ef = require('../ef');
var resdata = require('../resdata');
var dateutil = require('../dateutil');

//用户签到
router.post('/_node_user/_save_sign' + config.urlSuffix , bodyParser, function(req, res, next) {
    var token = req.get("token");
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
        var newDate = new Date();
        var newDateStr = dateutil.toDateFormat(newDate,"yyyy-MM-dd hh:mm:ss");
        var afterDateStr = dateutil.toDateFormat(dateutil.addDay(newDate,1),"yyyy-MM-dd 00:00:00");
        var diffDate = dateutil.countDateDiff(newDateStr,afterDateStr,"second");
        console.log(diffDate);
        redis.set(config.userSignPrefix + json,1,'EX',diffDate);
        return done(true,"");
    });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
