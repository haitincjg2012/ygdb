"use strict";
module.exports = {
    "proxy": {
        "backend": "http://127.0.0.1:1800",
    },
    "redis": {
        port: 6379,          // Redis port
        host: '127.0.0.1',   // Redis host
        family: 4,           // 4 (IPv4) or 6 (IPv6)
        password: 'foobared',
        db: 0
    },
    "elasticsearch":{    //ES-node
       clusterNodes: [
           "192.168.7.22:9200",
           "192.168.7.21:9200"
      ]
    }
}
