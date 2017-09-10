'use strict';

//API  日期格式化
exports.toDateString = function(dateStr,splitChar, timeFlag) {
	splitChar = splitChar == undefined ? "-" : splitChar;
  dateStr = new Date(dateStr);
	var _tmp = dateStr.getFullYear() + splitChar + lpad(dateStr.getMonth() + 1) + splitChar + lpad(dateStr.getDate());
	if(timeFlag) {
		_tmp += " " + lpad(dateStr.getHours()) + ":" + lpad(dateStr.getMinutes()) + ":" + lpad(dateStr.getSeconds());
	}
	return _tmp;
};

var lpad = function(n, maxLength) {
	return n < 10 ? ("0" + n) : n;
}

exports.getDateDiff = function(dateTimeStamp,timeOut){
  var result;
	if(timeOut){
		if(timeOut == 3){
			return result = "三天后下架";
		}else if(timeOut == 2){
			return result = "后天下架";
		}else if(timeOut == 1){
			return result = "明天下架"
		}
	}
	var minute = 1000 * 60;
	var hour = minute * 60;
	var day = hour * 24;
	var halfamonth = day * 15;
	var month = day * 30;
	var now = new Date().getTime();
	var diffValue = now - dateTimeStamp;
	if(diffValue < 0){return this.toDateString(dateTimeStamp,"-");}
	var monthC =diffValue/month;
	var weekC =diffValue/(7*day);
	var dayC =diffValue/day;
	var hourC =diffValue/hour;
	var minC =diffValue/minute;

  if(monthC>=1){
		 result = this.toDateString(dateTimeStamp,"-");
	}else if(weekC>=1){
		 result="" + parseInt(weekC) + "周前";
	}else if(dayC>=1){
		 result=""+ parseInt(dayC) +"天前";
	}else if(hourC>=1){
		 result=""+ parseInt(hourC) +"小时前";
	}else if(minC>=1){
		 result=""+ parseInt(minC) +"分钟前";
	}else{
	   result="刚刚";
  }
	return result;
};


//数组元素的删除
exports.arrRemove = (arr,val) => {
	var index = ayyIndexOf(arr,val);
	if (index > -1) {
		return arr.splice(index, 1);
	}
	return arr;
};

var ayyIndexOf = (arr,val) => {
	for (var i = 0; i < arr.length; i++) {
		if (arr[i] == val) return i;
	}
	return -1;
};
