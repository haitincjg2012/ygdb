'use strict';

//API 时间格式化
exports.toDateFormat = function(date,format){
  var o = {
      "M+" :  date.getMonth()+1,  //month
      "d+" :  date.getDate(),     //day
      "h+" :  date.getHours(),    //hour
      "m+" :  date.getMinutes(),  //minute
      "s+" :  date.getSeconds(), //second
      "q+" :  Math.floor((date.getMonth()+3)/3),  //quarter
      "S"  :  date.getMilliseconds() //millisecond
  }
  if(/(y+)/.test(format)) {
      format = format.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
  }
  for(var k in o) {
      if(new RegExp("("+ k +")").test(format)) {
          format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
      }
  }
  return format;
}

//API  增加日期
exports.addDay = function (date, dayNum) {
			if( date  &&  dayNum  && date instanceof Date && typeof dayNum == 'number'){
				date.setDate(date.getDate() + dayNum);
			}else{
				console.warn('date or dayNum format wrong');
			}
			return date;
}

//API 计算时间区别
exports.countDateDiff = function(startTime, endTime, diffType) {
    //将xxxx-xx-xx的时间格式，转换为 xxxx/xx/xx的格式
    startTime = startTime.replace(/\-/g, "/");
    endTime = endTime.replace(/\-/g, "/");
    //将计算间隔类性字符转换为小写
    diffType = diffType.toLowerCase();
    var sTime = new Date(startTime);      //开始时间
    var eTime = new Date(endTime);  //结束时间
    //作为除数的数字
    var divNum = 1;
    switch (diffType) {
        case "second":
            divNum = 1000;
            break;
        case "minute":
            divNum = 1000 * 60;
            break;
        case "hour":
            divNum = 1000 * 3600;
            break;
        case "day":
            divNum = 1000 * 3600 * 24;
            break;
        default:
            break;
    }
    return parseInt((eTime.getTime() - sTime.getTime()) / parseInt(divNum));
}
