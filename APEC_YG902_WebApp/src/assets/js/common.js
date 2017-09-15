import { MessageBox } from 'mint-ui';

let commonJs = {
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
      let c_start = document.cookie.indexOf(c_name + "=")
      if (c_start != -1) {
        c_start = c_start + c_name.length + 1
        let c_end = document.cookie.indexOf(";", c_start)
        if (c_end == -1) c_end = document.cookie.length
        return unescape(document.cookie.substring(c_start, c_end))
      }
    }
    return ""
  },
  //清除cookie
  clearCookie: function (name) {
    this.setCookie(name, "", -1);
  },
//获取存在localstorage或者cookie中的值
  getValue: function (cl_key) {
    //let c_value = this.getCookie(cl_key);
    //let l_value = window.localStorage.getItem(cl_key);
    return window.localStorage.getItem(cl_key) || this.getCookie(cl_key);
  },
  //将值存入storage和cookie
  setValue: function (c_name, value) {
    this.setCookie(c_name, value, 365);//默认365天
    window.localStorage.setItem(c_name, value);
  },
  setLocalValue: function (c_name, value) {
    window.localStorage.setItem(c_name, value);
  },
  getLocalValue: function (cl_key) {
    return window.localStorage.getItem(cl_key)
  },

  //清空local缓存和cookie
  clearCache:function () {
    var storage = window.localStorage;
    storage.clear();
    //清除cookie
    this.clearCookie('authToken');
    this.clearCookie('cityId');
    this.clearCookie('login.phone');
    this.clearCookie('openId');
    this.clearCookie('authToken');
    storage.setItem('userInfoRes','true');
    MessageBox({
      title: '提示',
      message: "网络状况不好哦，数据未初始化成功,请点击 '重新进入商城' 进行购物",
      confirmButtonText:'重新进入商城'
    }).then(action =>{
      window.location.href='https://shop.ap-ec.cn/welcome.html';
    });
  },
  //trim方法
  trimStr:function(str){
    return str.replace(/(^\s*)|(\s*$)/g,"");
  }

};

export  default  commonJs;
