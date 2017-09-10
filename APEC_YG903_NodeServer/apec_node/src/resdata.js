'use strict';

//API 返回结果集
exports = module.exports = function (res,succeed, data,errorMsg,errorCode) {
   return res.send({
      "succeed":succeed,
      "errorCode":errorCode==null?'':errorCode,
      "errorMsg":errorMsg==null?'':errorMsg,
      "data":data
   });
};
