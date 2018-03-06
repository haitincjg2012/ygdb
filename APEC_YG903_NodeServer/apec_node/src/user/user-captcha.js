'use strict';

//用户验证码校验
var router,bodyParser;
router = require('express').Router();
module.exports = router;
bodyParser = require('body-parser')();
var config = require('config');
var redis = require('../midware_server/redis');
var ef = require('../ef');
var resdata = require('../resdata');

//获取用户的手机验证码是否正确
router.post('/_node_user/_captcha' + config.urlSuffix , bodyParser, function(req, res, next) {
    var userMobile =  req.body.mobile;
    var userCaptcha = req.body.captcha;

    if(!userMobile){
        return resdata(res,false,null,"请输入手机号!");
    }
    if(!userCaptcha){
        return resdata(res,false,null,"请输入验证码!");
    }

    var done = (err,response) => {
        return resdata(res,true,err,response);
    };

    return ef(done,bind$(redis, 'get'), config.userCaptchaPrefix + userMobile, function(json){
        if(!json){
           return done(false,"验证码已失效!");
        }
        if(json === userCaptcha){
            redis.del(config.userCaptchaPrefix + userMobile);
            return done(true,true);
        }
        return done(false,"验证码错误!");
    });
});

function bind$(obj, key, target){
    return function(){ return (target || obj)[key].apply(obj, arguments) };
}
