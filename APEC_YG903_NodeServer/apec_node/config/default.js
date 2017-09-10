"use strict";
module.exports = {
  //Request suffix
  "urlSuffix": ".apno",
  //product-index
  "productIndex": {
      index:"apec",
      type:"myblog"
  },
  "productOffIndex":{
      index: "product",
      type: "offsell"
  },
  "productSearchFields": ["skuName^3","userTypeName^2", "productTypeName^2","showUserName^2","goodsName","marketPreFix","productAttrs.attrValue","address","addressDetail","remark"],
  "elasticsearchSize": 15,    //Elasticsearch 分页查询默认页大小15
  "createProductNum": 100 ,  //未实名认证只能发布5次供求信息
  "userSessionOutCode": "600001", //Session out Eror Code
  "userTokenPrefix": "token_", //User Token Prefix
  "userSignPrefix":"sign_",   //用户签到的KEY  sign_token
  "userCaptchaPrefix": "captcha_", //
  "userInfoPrefix": "user:",  //User Info Prefix
  "userDataKey": "data", //User Info key
  "userViewKey": "view_user", //User View Key
  "userProductHisKey": "product_his",  //User Create ProductHis Key
  "userSearchKey": "search_his", //用户搜索记录
  "userProductNumKey": "product_num",  //客户发布供求次数的key
  "userProductPushKey": "product_info", //用户发布的供求Key
  "userProductOffKey": "product_off_sell_info",
  "userMessageCountKey": "message_count",  // User MessageCount key
  "userProSaveKey": "pro_save",    //用户收藏的key
  "productReCommendKey": "pro_recommend", //热门推荐
  "productAllView": "pro_allviewnum", //产品总的浏览数
  "productAllSave": "pro_allsavenum",//产品总的收藏数
  "productInfoPrefix": "product:",  //产品 Hash的前缀
  "proViewNumKey": "view_num", //用户浏览数量的KEy
  "proSaveNumKey": "save_num", // 用户保存数的KEy
  "addressSaveKey": "address_his", //用户地址的历史记录
  "proPhoneKey": "phone_num"   //  电话联系数KEY
}
