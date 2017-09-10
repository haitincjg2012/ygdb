
$(function(){
	var _url = window.location.href.split('.')[1];
	
	window.domain_www = "http://www." + _url + ".com/";
	window.login_url = "http://login." + _url + ".com/";
	window.reg_url = "http://reg." + _url + ".com/";
	window.member_url = "http://member." + _url + ".com/";
	window.shop_url = "http://shop." + _url + ".com/";
	window.help_url = "http://help." + _url + ".com/";
	window.static_url = "http://static." + _url + ".com/";
	window.list_url = "http://list." + _url + ".com/";
	window.item_url = "http://item." + _url + ".com/";
	window.cart_url = "http://item." + _url + ".com/cart";
	window.mama_url = "http://mama." + _url + ".com/";
	window.nv_url = "http://nv." + _url + ".com/";
	window.nan_url = "http://nan." + _url + ".com/";
	window.tong_url = "http://tong." + _url + ".com/";
	window.yunfu_url = "http://yunfu." + _url + ".com/";
	//cookie 设置

var hycf = (function(){
	return{
		cookie : function (name, value, options) {
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
	},

	getCookie : function (name) {
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
	},

	init : function () {
		var user = this.info() || "",
				  _wrap = $("#j_userinfo"),
				  _wrapc = $("#j_userinfoc"),
				  _flag = true;

		$(document).on('click', function (e) {
			var html2 = '';
			if (user != "") {
				//用户登录状态；
				html2 += '<ul class="m-subnavc">';
				html2 += '<li class="clearfix"><a href="' + window.member_url + '" class="logined profile">我的资料</a></li>';
				html2 += '<li class="clearfix"><a href="' + window.member_url + 'order" class="logined orders">我的订单</a></li>';
				html2 += '<li class="clearfix"><a href="' + window.member_url + 'address" class="logined addresses">地址管理</a></li>';
				html2 += '<li class="clearfix"><a href="' + window.login_url + 'home/loginout" class="logined loginout">退出登录</a></li>';
				html2 += '</ul>';

			} else {
				// 用户未登录状态；
				html2 += '<ul class="m-subnavc">';
				html2 += '<li class="clearfix"><a href="' + window.login_url + '" class="login notlogin">登录</a></li>';
				html2 += '<li class="clearfix"><a href="' + window.reg_url + '" class="reg notlogin">注册</a></li>';
				html2 += '</ul>';
			}
			;
			if (_flag && e.target.id == 'j_user') {
				_wrapc.html(html2);
				_wrapc.addClass('z-crt');
				_flag = false;
			} else {
				_wrapc.removeClass('z-crt');
				_wrapc.html('');
				_flag = true
			}


		});


	},
	info : function () {
		var cookie = this.getCookie('HYUSER');
		if (!cookie || cookie == undefined || cookie == null) {
			return null;
		}
		return cookie;
	}





	}
})();	

hycf.init();
	

})
	


