'use strict';

var router,bodyParser;
router = require('express').Router();
module.exports = router;
bodyParser = require('body-parser')();
var config = require('config');
var resdata = require('../resdata');
var ALY = require("aliyun-sdk")

//Aliyun TST 授权接口
router.post('/_node_image/_oauth' + config.urlSuffix , bodyParser, function(req, res, next) {
    var done = (succeed,response,errorMsg,errorCode) => {
        return resdata(res,succeed,response,errorMsg,errorCode);
    };
    // 构建一个 Aliyun Client, 用于发起请求
    // 构建Aliyun Client时需要设置AccessKeyId和AccessKeySevcret
    var sts = new ALY.STS({
       accessKeyId: config.aliTSTAccessKeyId,
       secretAccessKey: config.aliTSTSecretAccessKey,
       endpoint: config.aliTSTEndpoint,
       apiVersion: config.aliTSTApiVersion
    });
    // 构造AssumeRole请求
    sts.assumeRole({
          Action: 'AssumeRole',
          // 指定角色Arn
          RoleArn: config.aliTSTRoleArn,
          //设置Token的附加Policy，可以在获取Token时，通过额外设置一个Policy进一步减小Token的权限；
          //Policy: '{"Version":"1","Statement":[{"Effect":"Allow", "Action":"*", "Resource":"*"}]}',
          DurationSeconds: 3600,
          RoleSessionName: 'ygdb-01'
    }, function (err, res) {
        if(err) return done(false,null,"授权失败","000000")
        return done(true,res.Credentials);
    });
});
