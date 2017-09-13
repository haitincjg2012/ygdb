'use strict';
var config = require('config');
var elasticsearch = require('elasticsearch');

if (config.elasticsearch) {
    if (config.elasticsearch.clusterNodes) {
        var clusterNodes = config.elasticsearch.clusterNodes;
        delete config.elasticsearch.clusterNodes;
        module.exports =  new elasticsearch.Client({hosts: clusterNodes});
    } else {
        module.exports = new elasticsearch.Client({host: config.elasticsearch});
    }
}
