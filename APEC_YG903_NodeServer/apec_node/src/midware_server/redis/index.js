'use strict';
var config = require('config');
var Redis = require('ioredis');
if (config.redis) {
    if (config.redis.clusterNodes) {
        var clusterNodes = config.redis.clusterNodes;
        delete config.redis.clusterNodes;
        module.exports = new Redis.Cluster(clusterNodes, config.redis);
    } else {
        module.exports = new Redis(config.redis);
    }
}
