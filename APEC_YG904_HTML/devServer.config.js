const servers=[
  '/yg-systemuser-service/',
  '/yg-user-service/',
  '/yg-systemConfig-service/',
  '/images/',
  '/common/',
  '/yg-cms-service/',
  '/_node_image/',
  '/_node_product/',
  '/yg-product-service/',
  '/yg-goods-service/',
  '/yg-voucher-service/',
  '/yg-society-service/',
  '/yg-game-service'
];
let proxyObj={};
let serverObj={};

servers.map(function(item,index){
  pathRewrite={};
  serverObj={};
  serverObj[servers[index]]={
    target:'http://192.168.7.111',
    changeOrigin:true
  };
  proxyObj=Object.assign(proxyObj,serverObj);
});

module.exports=proxyObj;
