
let commonJs = {
  //客户认证设置cookie
  setCetifateCookie: function (name, value) {
    var expireTime=30;//过期时间为30min
    var exp = new Date();
    exp.setTime(exp.getTime() + expireTime*60*1000);
    document.cookie = name + "=" + escape(value) +
      ((expireTime == null) ? "" : ";expires=" + exp.toGMTString());
  },
  //设置cookie
  setCookie: function (c_name, value, expiredays) {
    var exdate = new Date()
    exdate.setDate(exdate.getDate() + expiredays)
    document.cookie = c_name + "=" + escape(value) +
      ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString())
  },
//取回cookie
  getCookie: function (c_name) {
    if (document.cookie.length > 0) {
      let c_start = document.cookie.indexOf(c_name + "=");
      if (c_start != -1) {
        c_start = c_start + c_name.length + 1;
        let c_end = document.cookie.indexOf(";", c_start);
        if (c_end == -1) c_end = document.cookie.length;
        return unescape(document.cookie.substring(c_start, c_end));
      }
    }
    return ""
  },
  //获取存在localstorage或者cookie中的值
  getValue:function (cl_key) {
    return window.localStorage.getItem(cl_key) || this.getCookie(cl_key);
  },
  //将值存入storage和cookie
  setValue:function (c_name, value) {
    this.setCookie(c_name,value,365);//默认365天
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
}
};

export  default  commonJs;
