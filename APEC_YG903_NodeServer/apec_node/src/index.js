'use strict';

var express = require('express');
var server = exports = module.exports = express();
var config = require('config');

// Reids Error Hand
var redis = require('./midware_server/redis');
redis.on('error', function(err){
  console.log("\n[Error] Redis error: 请确认redis是否连接正确\n");
  redis.disconnect();
  throw err;
});

//Elasticsearch
var elasticsearch = require('./midware_server/elasticsearch');
elasticsearch.ping({
  requestTimeout: 3000,
}, function (error) {
  if (error) {
    console.error('\n[Error] elasticsearch 请确认elasticsearch是否连接正确!');
  } else {
    console.log('\n[Success] elasticsearch 集群连接正常');
  }
});

//routter
server.use(require('./product'));
server.use(require('./user'));
server.use(require('./user_org'));
server.use(require('./quotation'));
server.use(require('./ali_oss'));
