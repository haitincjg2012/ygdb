/**      .----.
*       _.'__    `.
*   .--($)($$)---/#\
* .' @          /###\
* :         ,   #####
*  `-..__.-' _.-\###/
*        `;_:    `"'
*      .'"""""`.
*     /,     ,\\
*    //  !BUG  \\
*    `-._______.-'
*    ___`. | .'___
*   (______|______)
*
* HYW全局-JS
*/

$(function() {
      //全局变量
      var _url = window.location.href.split('.')[1];
      window.domain_www = "http://www." + _url + ".com/";
      window.login_url = "http://login." + _url + ".com/";
      window.reg_url="http://reg."+_url+".com/";
      window.member_url="http://member."+_url+".com/";
		window.shop_url="http://shop."+_url+".com/";
      window.help_url="http://help."+_url+".com/";
      window.static_url="http://static."+_url+".com/";
	window.cart_url="http://item."+_url+".com/cart";  
	window.nan_url="http://nan."+_url+".com/";
	window.nv_url="http://nv."+_url+".com/";
	window.mama_url="http://mama."+_url+".com/";
	window.yunfu_url="http://yunfu."+_url+".com/";
	window.tong_url="http://tong."+_url+".com/";
	window.s_url="http://s."+_url+".com/";
      //hyw.init();
     // hyw.user.login()
});

//hyw 全局变量
var hyw = (function() {
      return {
            init: function(ele) {
                  hyw.user.init(ele);
            },
            cookie: function(name, value, options) {
                  if (typeof value != 'undefined') {
                        options = options || {};
                        if (value === null) {
                              value = '';
                              options = $.extend({}, options);
                              options.expires = -1;
                        }
                        var expires = '';
                        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
                              var date;
                              if (typeof options.expires == 'number') {
                                    date = new Date();
                                    date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
                                    //date.setTime(date.getTime() + (options.expires * 30 * 60 * 1000));
                              } else {
                                    date = options.expires;
                              }
                              expires = '; expires=' + date.toUTCString();
                        }
                        var path = options.path ? '; path=' + (options.path) : '';
                        var domain = options.domain ? '; domain=' + (options.domain) : '';
                        var secure = options.secure ? '; secure' : '';
                        document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
                  } else {
                        var cookieValue = null;
                        if (document.cookie && document.cookie != '') {
                              var cookies = document.cookie.split(';');
                              for (var i = 0; i < cookies.length; i++) {
                                    var cookie = jQuery.trim(cookies[i]);
                                    if (cookie.substring(0, name.length + 1) == (name + '=')) {
                                          cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                                          break;
                                    }
                              }
                        }
                        return cookieValue;
                  }
            },
            loadCSS:function (url){
                  var link = document.createElement('link');
                  link.rel = "stylesheet";
                  link.type = "text/css";
                  link.href = url;
                  document.getElementsByTagName("head")[0].appendChild(link);
            },
            isPhone:function(argument) {//手机号码检测；
                  var reg = /^1[3|4|5|7|8][0-9]\d{8}$/;
                  return reg.test(argument);
            },
            isTel:function (argument) {//电话号码检测；
                  var reg = /^([0|4\+]\d{2,3}[—]?-)?(0|8\d{2,3}-)?(\d{4,8})(-\d{1,4})?$/;
                  return reg.test(argument);
            },
            isEmail:function(argument){//Email检测；
                  var reg = /^[a-z0-9_\-]+(\.[_a-z0-9\-]+)*@([_a-z0-9\-]+\.)+([a-z]+)$/;
                  return reg.test(argument);
            }
      }
})();

//hyw user
hyw.user = (function() {
      var wrap;
      return {
            init : function(ele) {
                  var user = this.info()||"";
                  wrap=$(ele);
                  var html='';
                 /* var htlm='<div class="g-wrap m-top-nav">'
                              +'<div class="loginInfo f-fl">'
                              +     '<a href="'+domain_www+'" class="index"><span class="I-ico">&#xe607;</span>首页 </a>'　
                              +'</div>'
                              +'<div class="f-fr">'
                              +      '<div class="top-navMenu">'
                              +      '<p>'
                              +          '<a class="note" href="'+user_url+'message">我的中心</a>'
                              +          '<a class="shopping" href="'+cart_url+'"><span class="I-ico">&#xe601;</span>购物车</a>'
                              +          '<a class="favorite" href="'+user_url+'buyer/favorite"><span class="I-ico">&#xe606;</span>收藏夹</a>'
                              +          '<a class="pay_hl" target="_blank" href="http://www.hulianpay.com/"><span class="I-ico">&#xe604;</span>支付</a>'
                              +          '<a class="help" href="'+help_url+'" >帮助中心</a>'
                              +          '</p>'
                              +      '</div>'
                              +'</div>'
                        +'</div>';*/
                 // wrap.append(htlm);

                  if(user!=""){
                        $(".loginInfo").append('您好, <a style="color:red" href="'+user_url+'">'+user.name+'</a> [<a href="'+login_url+'home/logout?to=' + escape(window.location.href) +'">退出</a>]<a class="top-note" href="'+user_url+'message">我的消息<b id="unread1" class="top-unread"></b></a>' );                          
                  }else{
                        $(".loginInfo").append('[<a href="'+login_url+'">登录</a>] [<a href="'+reg_url+'">免费注册</a>]')
                  };
                  

            },
            login: function(return_url) {
                  var cssUrl =static_url+"common/css/artdialog_default.css";
                  var cssUrl2 =static_url+"/common/css/login_iframe.css";
                  hyw.loadCSS(cssUrl);
                  hyw.loadCSS(cssUrl2);
                  var login_iframe = '<div id="login-form" >' 
                  + '<div class="login_tips" id="login_tips">' 
                  + '</div>' + '<ul>' + '<li><span>用户名/邮箱:</span><input class="un" id="un" type="text"></li>' 
                  + '<li><span>密码:</span><br><input class="pw" id="pw" type="password" value=""></li>' 
                  + '<li><input class="login" id="login" type="submit" value="登 录"></li><li><a href="'+ reg_url +'" class="reg">免费注册</a><a class="fpw" href="'+user_url+'find_password">忘记密码？</a></li>' 
                  + '</ul>' 
                  + '</div>';

                 art.dialog({
                        id: 'login-form',
                        content: login_iframe,
                        title: '',
                        lock: true,
                        padding: '0 30px 25px 30px'
                  });

                 $("#login").on("click", function() {
                    if ($("#un").val() == '' || $("#pw").val() == '') {
                        $("#login_tips").html("用户名或密码不能为空！").show();
                        return false;
                    } else {
                        $.getJSON(login_url+'home/domain_login?callback=?',{
                            username: $("#un").val(),
                            password: $("#pw").val()
                        },
                        function(data) {
                                if(data.code=="SUCCESS"){
                                    url = window.location.href;
                                    window.location.href = login_url + 'home/success?to='+url;
                                     //window.location.reload();
                                }else if(data.code=="USER_NOT_FOUND"){
                                    $("#login_tips").html(data.data).show();
                                    $("#un").focus().addClass("error");
                                }else if(data.code=="WRONG_PASSWORD"){
                                    $("#login_tips").html(data.data).show();
                                    $("#pw").focus().addClass("error");
                                };
                        },"json");
                    }
                    return false;
                  });
                  
                  $("#un,#pw").on("click",function(){
                        $(this).removeClass("error");
                  })
                  
            },
            info: function() {
                  var cookie = hyw.cookie('hyw_index');
                  if (!cookie || cookie.split('|').length != 2) {
                        return null;
                  }
                  var info = cookie.split('|');
                  return {
                        name: info[0],
                        type: info[1]
                  };
            }
      }
})();

