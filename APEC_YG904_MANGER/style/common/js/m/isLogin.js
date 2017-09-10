  //登录弹出窗口
  define(['jquery','global','dialog'],function($,global){
    var mod={},
    isLoad=true;
 mod.login=function(){
        if(isLoad!=false){
            var cssUrl =global.static_url+"common/css/ui-dialog.css";
            var cssUrl2 =global.static_url+"common/css/m/loginframe.css";
            // global.loadCSS(cssUrl);
            // global.loadCSS(cssUrl2);
            isLoad=false;
        }
       var loginhtml='';
            loginhtml+= '<div class="m-login-form m-login-global" >';
            // loginhtml+='<div class="loginhd">登陆</div>';
            loginhtml+=  '<form action="/login" id="loginForm1" autocomplete="off" method="post" class="login-form" role="form">';
             loginhtml+= '<ul class="m-loginform">';
             loginhtml+= '<li class="u-loginform u-texticon u-texticon-un j_control"><input class="form-control" type="text" name="userPhone" autofocus="" placeholder="请输入手机号码" value="" id="J_userName1"><span class="j_clean"></span></li>';
             loginhtml+='<li class="m-tip"><span class="J-tip">请输入一个有效的手机号码! </span></li>';
             loginhtml+='<li class="u-loginform u-texticon u-texticon-pw j_control"><input class="form-control" type="password" name="password1" autofocus="" placeholder="请输入密码" value="" id="J_pwd1"><div class="j_pw"></div></li>';
             loginhtml+='<li class="m-tip"><span class="J-tip">请输入一个有效的手机号码! </span></li>';
             loginhtml+='<li class="checkbox checkbox-login clearfix"><div class="f-fl"><a href="javascript:;" data-href="'+global.login_url+'home/findpwd" class="j_markcookie">忘记密码</a></div><div class="f-fr"><a href="javascript:;" data-href="'+global.reg_url+'" class="j_markcookie">免费注册</a></div></li>';
             loginhtml+='<li><input type="button" id="J_submit1" class="btn btn-login" value="登录"></li>';
             loginhtml+='</ul>';
            loginhtml+='</form>';
             loginhtml+='</div>';
            art.dialog({
                title: '登陆',
                content: loginhtml
            });
      $('<div class="shadow"></div>').appendTo(document.body);
      $('.aui_close').on('click',function(){
        $('.shadow').remove();
      });
            var $J_userName = $('#J_userName1'),
            $J_pwd = $('#J_pwd1'),
            loginForm=$("#loginForm1"),
            $J_submit = $('#J_submit1');


        loginForm.on('focus', '.form-control', function () {
            var _this=$(this),
            index = $(".form-control", loginForm).index($(this)),
                      _objtip = $(".J-tip", loginForm).eq(index);

            if (_objtip) {
                _objtip.removeClass("J-show").addClass("J-hide");
                _this.addClass("z-click");
                _this.attr("placeholder","")
            };
        });

        $J_userName.on("change keyup",function(){
            var _this=$(this),_obj1=$(".j_clean",loginForm);
            if(_this.val().length != 0){
                _obj1.show();
            }else{
               _obj1.hide(); 
            }
                
             });
         loginForm.on("click",".j_clean",function(){
            $J_userName.val('').focus();
            $(this).hide();

         });
//密码是否可见
        var _objshowpw = $(".j_pw",loginForm);
         $J_pwd.on("change keyup",function(){
            var _this=$(this);
            if(_this.val().length != 0){
                _objshowpw.show();
            }else{
               _objshowpw.hide(); 
            }    
             });
         _objshowpw.on("click",function(){
            var _this=$(this);
            if(_this.hasClass("j_showpw")){
                $J_pwd[0].type = 'text'
                _this.removeClass("j_showpw");
                _this.addClass("j_hidepw");
            }else{
                $J_pwd[0].type ='password';
                _this.removeClass("j_hidepw");
                _this.addClass("j_showpw");
            }
         });


        $J_submit.on("click",_login);
        function _login(){
            var userName=$J_userName.val(),
            pwd=$J_pwd.val();
             if (!global.isPhone(userName)) {
                wrongCss($J_userName,'请输入正确的手机号码');
                return false;
            }
            if (pwd=='') {
                wrongCss($J_pwd,'请输入密码！');
                return false;
            }       
        $.getJSON(global.login_url + "home/domain_login?callback=?", {
                    "userName": $J_userName.val(),
                    "pwd"     : $J_pwd.val()
                },
                function(data) { 
                    if (data.success === true) {
							  var url = window.location.href;
                      // window.location.href=url;
                      $('.shadow').remove();
                      $('.aui_state_focus').hide();
                        return true;
                    }
                    if (data.success === false) {
                        var msg='' || data.box_name;
                        if(data.prompt=='on_phone'){
                            wrongCss($J_userName,msg);
                            return false;
                        }
                        if(data.prompt=='no_passwd'){
                            wrongCss($J_pwd,msg);
                            return false;
                        }
								if(data.prompt=='go'){
                            wrongCss($J_pwd,msg);
                            return false;
                        }
                        
                    }

                }, "json"); 
        }


          


         



   /*验证正确时添加 right CSS
         *   _obj ：jq对象
         *   msg ：信息
        */
        function rightCss(_obj,msg){
            _obj.parents(".j_control").next(".m-tip").find(".J-tip")
                .removeClass("J-show")
                .addClass("J-hide")
                .text(msg);
        };

        /*验证错误时添加 wrong CSS
         *   _obj ：jq对象
         *   msg ：信息
        */
        function wrongCss(_obj,msg){
            _obj.parents(".j_control").next(".m-tip").find(".J-tip")
                .removeClass("J-hide")
                .addClass("J-show")
                .text(msg);
        };
  
    }
    return mod;
  })
    