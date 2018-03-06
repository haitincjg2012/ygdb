/**
 * Created by gsy on 2017/9/14.
 * 公共Filter
 */
import Vue from 'vue'

let format={
  timePattern:function(time, format) {
    var o = {
      "M+": time.getMonth() + 1, //month
      "d+": time.getDate(), //day
      "h+": time.getHours(), //hour
      "m+": time.getMinutes(), //minute
      "s+": time.getSeconds(), //second
      "q+": Math.floor((time.getMonth() + 3) / 3), //quarter
      "S": time.getMilliseconds() //millisecond
    }
    if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
      (time.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o) if (new RegExp("(" + k + ")").test(format))
      format = format.replace(RegExp.$1,
        RegExp.$1.length == 1 ? o[k] :
          ("00" + o[k]).substr(("" + o[k]).length));
    return format;
  },
  //将时间戳转为“yyyy-MM-dd”
  timestampPattern:function(timestamp) {
    var time = this.timePattern(new Date(timestamp), 'yyyy-MM-dd');
    return time;
  },
  //将时间戳转为“yyyy-MM-dd hh:mm:ss”
  datetampPattern:function(timestamp) {
    var time = this.timePattern(new Date(timestamp), 'yyyy-MM-dd hh:mm:ss');
    return time;
  }

};

//将时间戳转为yyyy-MM-dd
Vue.filter('ymdFilter',function(val){
  val=val?format.timestampPattern(val):"";
  return val;
});

//将时间戳转为yyyy-MM-dd
Vue.filter('dateFilter',function(val){
  val=val?format.datetampPattern(val):"";
  return val;
});
