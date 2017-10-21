'use strict';

//API 返回结果集
exports = module.exports = function (currentNo,pageCount,totalElements,rows) {
   return {
      "currentNo":currentNo,
      "pageCount":pageCount,
      "totalElements":totalElements,
      "rows":rows,
   };
};
