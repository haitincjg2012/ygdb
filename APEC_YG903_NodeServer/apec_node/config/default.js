"use strict";
module.exports = {
  //Request suffix
  "urlSuffix": ".apno",
  //Aliyun TST
  "aliTSTAccessKeyId":"LTAIqFAp6BVbL2PA",
  "aliTSTSecretAccessKey":"B6hgPFfcoiGB0k61QMgcQ2PXX7lIZK",
  "aliTSTRoleArn": "acs:ram::1553306583222696:role/aliyunosstokengeneratorrole",
  "aliTSTEndpoint": "https://sts.aliyuncs.com",
  "aliTSTApiVersion": "2015-04-01",
  //product-index
  "productIndex": {
      index:"apec",
      type:"myblog"
  },
  "productOffIndex":{
      index: "product",
      type: "offsell"
  },
  "orgDepotIndex": {
      index:"depot",
      type:"orginfo"
  },
  "orgAgencyIndex": {
        index:"agency",
        type:"orginfo"
  },
  "orgMerchantIndex": {
      index:"merchant",
      type:"orginfo"
  },
  "productSearchFields": ["skuName^3","userTypeName^2", "productTypeName^2","showUserName^2","goodsName","marketPreFix","productAttrs.attrValue","address"],
  "orgDepotSearchFields": ["orgName^3","orgTags.tagName^2","address^2","mainOperating","orgStockCap"],
  "orgAgencySearchFields": ["orgName^3","orgTags.tagName^2","address^2","mainOperating","saleAddress"],
  "orgMerchantSearchFields": ["orgName^3","orgTags.tagName^2","address^2","mainOperating"],
  "elasticsearchSize": 15,    //Elasticsearch 分页查询默认页大小15
  "createProductNum": 100,  //未实名认证只能发布5次供求信息
  "userSessionOutCode": "600001", //Session out Eror Code
  "userTokenPrefix": "token_", //User Token Prefix
  "userSignPrefix":"sign_",   //用户签到的KEY  sign_token
  "userCaptchaPrefix": "captcha_", //
  "userInfoPrefix": "user:",  //User Info Prefix
  "userOrgInfoPrefix":"user_org:" , // User Org Prefix
  "articleInfoPrefix":"article:" , // article Prefix
  "userOrgUserInfoPrefix":"user_list" , // User Org  User List Prefix
  "userOrgViewNumPrefix": "view_num" ,  // User org view num prefix
  "userOrgAttentionKey": "atten_num",  // User org Attention num key
  "userOrgAttentUserKey": "atten_users",  // User org Attention  key
  "userOrgPhoneKey": "phone_num",   //  电话联系数KEY
  "userDataKey": "data", //User Info key
  "userViewKey": "view_user", //User View Key
  "userVoucherNumKey": "voucher_num", // 调果数量
  "userProductHisKey": "product_his",  //User Create ProductHis Key
  "userSearchKey": "search_his", //用户搜索记录
  "userProductNumKey": "product_num",  //客户发布供求次数的key
  "userProductPushKey": "product_info", //用户发布的供求Key
  "userProductOffKey": "product_off_sell_info",
  "userMessageCountKey": "message_count",  // User MessageCount key
  "userReplyCountKey":"article_msg_count",  //用户的评论通知未读消息数量
  "userPraiseCountKey":"give_up_msg_count",  //用户的点赞通知未读消息数量
  "userProSaveKey": "pro_save",    //用户收藏的key
  "userOrgSaveKey": "org_save",    //用户关注的组织用户的key
  "userArtPraiseKey": "article_praise",    //用户点赞的文章id
  "productReCommendKey": "pro_recommend", //热门推荐
  "productAllView": "pro_allviewnum", //产品总的浏览数
  "productAllSave": "pro_allsavenum",//产品总的收藏数
  "productInfoPrefix": "product:",  //产品 Hash的前缀
  "proViewNumKey": "view_num", //用户浏览数量的KEy
  "proSaveNumKey": "save_num", // 用户保存数的KEy
  "addressSaveKey": "address_his", //用户地址的历史记录
  "proPhoneKey": "phone_num",  //  电话联系数KEY
  "praiseNumKey": "praise_num",// 点赞数
  "readedNumKey": "readed_num", // 阅读数
  "articleAllRead": "art_allreadnum",//文章总的阅读数
  "quotationPrefix":"quotation:", //行情竞猜前缀
  "quotationHigh":"quotation_high", //行情竞猜看涨的人数
  "quotationLight":"quotation_light", //行情竞猜看跌的人数
  "quotationHighUser":"quotation_high_user", //行情竞猜看涨的人
  "quotationLightUser":"quotation_light_user", //行情竞猜看跌的人
  "highQuotation":"quotation_high", //看涨的行情竞猜
  "lightQuotation":"quotation_light" //看跌的行情竞猜
}
