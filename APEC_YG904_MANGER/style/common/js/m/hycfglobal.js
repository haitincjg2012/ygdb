define(['jquery'],function($) {
      
      var _url = window.location.href.split('.')[1];
      var hycf = {};
      hycf.domain_www = "http://www." + _url + ".com/";
      hycf.login_url = "http://login." + _url + ".com/";
      hycf.reg_url = "http://reg." + _url + ".com/";
      hycf.member_url = "http://member." + _url + ".com/";
		hycf.shop_url = "http://shop." + _url + ".com/";
      hycf.help_url = "http://help." + _url + ".com/";
      hycf.static_url = "http://static." + _url + ".com/";
      hycf.list_url = "http://list." + _url + ".com/";
      hycf.item_url="http://item." + _url + ".com/";
      hycf.cart_url = "http://item." + _url + ".com/cart";
      hycf.mama_url = "http://mama." + _url + ".com/";
      hycf.nv_url = "http://nv." + _url + ".com/";
      hycf.nan_url = "http://nan." + _url + ".com/";
      hycf.tong_url = "http://tong." + _url + ".com/";
      hycf.yunfu_url = "http://yunfu." + _url + ".com/";
      //cookie 设置
      hycf.cookie = function(name, value, options) {
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
                              var cookie = $.trim(cookies[i]);
                              if (cookie.substring(0, name.length + 1) == (name + '=')) {
                                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                                    break;
                              }
                        }
                  }
                  return cookieValue;
            }
      };
      hycf.getCookie=function(name) {
        var cookie = document.cookie;
        var nameStart, nameEnd, valEnd;
        if (cookie.length > 0) {
          nameStart = cookie.indexOf(name + '=');
          if (nameStart !== -1) {
            nameEnd = nameStart + name.length + 1;
            valEnd = cookie.indexOf(';', nameEnd);
            if (valEnd === -1) {
              valEnd = cookie.length;
            }
            return decodeURIComponent(cookie.substring(nameEnd, valEnd));
          }
        }
        return null;
      };
      //添加CSS
      hycf.loadCSS=function (url){
            var link = document.createElement('link');
            link.rel = "stylesheet";
            link.type = "text/css";
            link.href = url;
            document.getElementsByTagName("head")[0].appendChild(link);
      };

      //手机号码检测；
      hycf.isPhone = function(argument) { 
            var reg = /^1[3|4|5|7|8][0-9]\d{8}$/;
            return reg.test(argument);
      };

      //电话号码检测；
      hycf.isTel = function(argument) { 
            var reg = /^([0|4\+]\d{2,3}[—]?-)?(0|8\d{2,3}-)?(\d{4,8})(-\d{1,4})?$/;
            return reg.test(argument);
      };

      //Email检测；
      hycf.isEmail = function(argument) {
            var reg = /^[a-z0-9_\-]+(\.[_a-z0-9\-]+)*@([_a-z0-9\-]+\.)+([a-z]+)$/;
            return reg.test(argument);
      };

      //邮政编码检测；
      hycf.iszip = function(argument) {
            var reg = /^\d{6}$/;
            return reg.test(argument);
      };
      hycf.cart = function() {
            var user = this.info() || "",
            cart_num=document.getElementById('cart_num');
            if (user != '' && cart_num) {
                  $.ajax({
                        url: hycf.cart_url + "/get_cart_num",
                        type: 'post',
                        dataType: 'jsonp',
                        beforeSend: function(){
                              //$("#num_txt").html('0');
                        },
                        success: function (data) {
                              cart_num.innerHTML=Number(data);
                        }
                  });
            };
      };

      //顶部数据条
      hycf.init = function() {
           // var user = this.info() || "";
           // wrap=$("#j_userinfo");
            // html='';     
            //if(user!=""){
                  //用户登录状态；
                  //html='<span>'+user+'</span>';
                  //html+='<a href="'+this.user_url+'">个人中心</a><a href="'+this.login_url+'home/loginout" >退出</a>';                        
           // }else{
                  //用户未登录状态；
                  //html='<span>欢迎来到赫雁财富信息平台</span>';
                  //html  +='<a href="'+this.login_url+'">登录</a>';
           // };
            //wrap.append(html); 
           // hycf.hyToggle();
            hycf.cart();
            // document.body.firstChild.nodeType === 3 &&
            // document.body.removeChild(document.body.firstChild);        
      };

      //cookie 解析
      hycf.info=function() {
            var cookie = this.getCookie('HYUSER');
            if (!cookie || cookie==undefined || cookie == null) {
                  return null;
            }
            return cookie;
      };
      hycf.hyToggle = function(){
            $(".j_hytoggle").on("click",function(){
                  var _this=$(this),
                  _target=_this.data("target"),
                  _targetobj=$(_target);
                  if(_targetobj.hasClass("in")){
                              _targetobj.removeClass("in");
                        }else{
                              _targetobj.addClass("in"); 
                        }
                     var _shodaw='<div class="shadow"></div>';   
                   _targetobj.data("backdrop") && _targetobj.append(_shodaw);
            });
           $("[data-dismiss]").on("click",function(){
            var _this=$(this),
            _val=_this.data("dismiss");
            console.log(_val);
            _this.parents('.'+_val).removeClass("in");
           });
      };
      hycf.toTop = function() {
            
            var a = function(c, f, e) {
                  var d = $(c).click(function() {
                              $("body,html").animate({
                                    scrollTop: 0
                              }, e);
                              return false
                        }),
                        g = $(window),
                        b = 1140;
                  if (g.width() >= b) {
                        $(this).scroll(function() {
                              $(this).scrollTop() > f ? d.fadeIn(e) : d.fadeOut(e)
                        })
                  }
            };

            $('<a id="backToTop" title="\u56de\u5230\u9876\u90e8" target="_self" href="javascript:;"><i>回到顶部</i></a>').appendTo("body");
            a("#backToTop", 0, 300);

      };


      return hycf;
})