'use strict';

//API 返回结果集
exports = module.exports = function (currentNo,pageCount,rows) {
   return {
      "currentNo":currentNo,
      "pageCount":pageCount,
      "rows":rows,
   };
};
