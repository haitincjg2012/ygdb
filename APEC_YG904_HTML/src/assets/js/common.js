
let commonJs = {

  //设置cookie
  setCookie: function (name, value, expiresMinute) {
    var cookieString = name + "=" + escape(value);
    //判断是否设置过期时间,0代表关闭浏览器时失效
    if (expiresMinute > 0) {
      var date = new Date();
      date.setTime(date.getTime() + expiresMinute * 60 * 1000);
      cookieString = cookieString + ";expires=" + date.toUTCString();
    }
    document.cookie = cookieString;
  },

  //根据名字获取cookie的值
  getCookie: function (name) {
    var strCookie = document.cookie;
    //console.log("strCookie:"+strCookie);
    if(strCookie.length>0){
      var arrCookie = strCookie.split("; ");
      for (var i = 0; i < arrCookie.length; i++) {
        var arr=arrCookie[i].split("=");
        if(arr[0]==name){
          return unescape(arr[1]);
          break;
        }
      }
    }
    return "";
  },



  //获取存在localstorage或者cookie中的值
  getValue:function (cl_key) {
    return window.localStorage.getItem(cl_key) || this.getCookie(cl_key);
  },
  //将值存入storage和cookie
  setValue:function (c_name, value) {
    this.setCookie(c_name,value,365*24*60);//默认365天
    window.localStorage.setItem(c_name,value);
  },

  //trim方法
  trim:function(param) {
  for (var key in param) {
    // 消除字符串两边的空格
    if (typeof param[key] == 'string') {
      param[key] = param[key].trim();
    }
  }
},

  //将当前时间转换成“year/month/day h:m:s”
  fmtCurrentTime:function(date) {
  var year = date.getFullYear();
  var month = date.getMonth() + 1;
  var day = date.getDate();

  var hour = date.getHours();
  var minute = date.getMinutes();
  var second = date.getSeconds();


  return [year, month, day].map(this.formatNumber).join('/') + ' ' + [hour, minute, second].map(this.formatNumber).join(':')
},

  //当year、month、day为1位数时，前面加‘0’
  formatNumber:function (n) {
  n = n.toString();
  return n[1] ? n : '0' + n
  },

  //日期转换函数 obj--》string
  //Eg；var time = timePattern(new Date(timestamp), 'yyyy-MM-dd')
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
  //将“yyyy-MM-dd”转成时间戳
  timesdPattern:function(strtime) {
    var date="";
  if (strtime) {
    date = new Date(strtime.replace(/-/g, '/'));
  }
  else {
    date = new Date();
  }

  var time = date.getTime();
  return time;
},

  //获取市、区名 根据item.para1的值遍历数据，获取item.para2的值
  //myarray 数据集   value：item[para1]  para1:code  para2:name  Eg:根据item[code]获取item[name]的值
  getArrayVal(myarray,value,para1,para2){
    var val="";
    myarray.forEach((item)=>{
      if(item[para1]==value){
      val=item[para2];
    }
  });
    return val;
  },

  //遍历对象,数据集合之间赋值（无需格式转化时）obj2:后台返回数据集  obj1：表单数据集
  //将obj2的属性按照obj1的属性遍历赋值 eg： vm.commonJs.getDataFomat(vm.newsForm,data.data);
  getDataFomat(obj1,obj2){
      for(var i in obj1){
        for(var j in obj2){
          if(i==j){
            obj1[i]=obj2[j]
          }
        }
      }
    return obj1;
  }
};

export  default  commonJs;
