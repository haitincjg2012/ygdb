/*短信验证码代码逻辑 利用cookie 页面刷新不重新计时*/
import API from '../../../api/api'
const api = new API();
var phonecodeGet = {
  //发送验证码的时候添加cookie
  addCookie: function (name, value, expiresHours) {
    var cookieString = name + "=" + escape(value);
    //判断是否设置过期时间,0代表关闭浏览器时失效
    if (expiresHours > 0) {
      var date = new Date();
      date.setTime(date.getTime() + expiresHours * 1000);
      cookieString = cookieString + ";expires=" + date.toUTCString();
    }
    document.cookie = cookieString;
  },
  //修改cookie的值
  editCookie: function (name, value, expiresHours) {
    var cookieString = name + "=" + escape(value);
    if (expiresHours > 0) {
      var date = new Date();
      date.setTime(date.getTime() + expiresHours * 1000); //单位是毫秒
      cookieString = cookieString + ";expires=" + date.toGMTString();
    }
    document.cookie = cookieString;
  },
//根据名字获取cookie的值
  getCookieValue: function (name) {
    var strCookie = document.cookie;
    var arrCookie = strCookie.split("; ");
    for (var i = 0; i < arrCookie.length; i++) {
      var arr=arrCookie[i].split("=");
      if(arr[0]==name){
        return unescape(arr[1]);
        break;
      }
    }
    return "";
  },
//开始倒计时
  settime: function (obj) {
    var countdown = this.getCookieValue("secondsremained");
    if (countdown == 0) {
      obj.removeAttr("disabled");
      obj.val("重新发送");
      return;
    } else {
      obj.attr("disabled", true);
      obj.val("重新发送(" + countdown + ")");
      countdown--;
      this.editCookie("secondsremained", countdown, countdown + 1);
    }
    var that = this;
    setTimeout(function () {
      that.settime(obj)
    }, 1000) //每1000毫秒执行一次
  }
}

export default  phonecodeGet;
