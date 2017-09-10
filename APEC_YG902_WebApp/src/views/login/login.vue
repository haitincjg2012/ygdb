<template>
  <div class="login-page">
    <top-bar title="登录"></top-bar>
    <div class="pure-g mainform">
      <div class="pure-g-l login-content">
        <div class="loggin-panel">
          <form onsubmit="return false">
          	<div class="phone-input">
          		<img src="../../../src/assets/img/phoneOn.png">
          		<input id="input-phonenum" oninvalid="setCustomValidity(' ')" type="tel" v-model="phoneNum" placeholder="请输入手机号码"
              oninput="setCustomValidity('')" required maxlength="11" pattern="^1(3|4|5|7|8)\d{9}$"/>
          		<!--<input type="button" @click="sendMessage" id="btn-message" value="获取验证码"></input>-->
          	</div>
            <div class="dash-line">
            </div>
            <!--<div style="display: none;" class="language-phone-input">-->
              <!--<a @click="voiceVerifyBtn" class="mc" style="font-size: 14px; text-decoration: underline;color: #0bbe06;">收不到短信验证码，尝试语音验证码</a>-->
            <!--</div>-->
          	<div class="code-input">
              <img src="../../../src/assets/img/Password.png">
          		<input oninvalid="setCustomValidity(' ')" id="input-code" type="password" class="pure-input-1" v-model="code" oninput="setCustomValidity('')"
              placeholder="请输入登录密码" required minlength="6">
              <img @click.stop="openText()" class="i-password-hide" :src="imgSrc">
          	</div>
            <!--<div class="code-input-text" style="display: none;">-->
              <!--<img src="../../../src/assets/img/Password.png">-->
              <!--<input oninvalid="setCustomValidity(' ')" id="input-code-text" type="text" class="pure-input-1" v-model="code" oninput="setCustomValidity('')"-->
                     <!--placeholder="请输入登录密码" required maxlength="4" pattern="^\d{4}$">-->
              <!--<img class="i-password-hide" src="../../../src/assets/img/show.png">-->
            <!--</div>-->
            <div class="dash-line-v">
            </div>
          	<div class="login-btn">
              <input type="submit" id="btn-login-code" value="登录" @click.stop="login"></input>
            </div>
            <div class="l-info">
              <div class="l-info-z" @click.stop="pushRUrl()">
                <span>
                                  注册账号
                </span>
              </div>
              <div class="flex-main"></div>
              <div class="l-info-p" @click.stop="pushFUrl()">
                <span>
                  找回密码
                </span>

              </div>
            </div>
            <div class="l-info-call">
              <div class="call-info"><span>联系客服</span>&nbsp;<a href="tel:0535-3143298">0535-3143298</a></div>
              <div class="call-time"><span>联系时间&nbsp;&nbsp;08:00-17:30</span></div>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import $ from 'zepto'
  import axios from 'axios'
  import codeGet from './js/getPhoneCode'
  import API from '../../api/api'
  import c_js from '../../assets/js/common'
  import {Indicator} from 'mint-ui';
  import { Toast } from 'mint-ui';
  import { MessageBox } from 'mint-ui';
  import topBar from '../../components/topBar/topBar'

  import showImgSrc from '../../../src/assets/img/show.png'
  import hideImgSrc from '../../../src/assets/img/hide.png'

  const api = new API();
  export default {
    data(){
      return {
        phoneNum: '',
        code: '',
        imgSrc:hideImgSrc
      }
    },
    activated(){
      this.code='';
    },
    created(){
      var phoneStorage = c_js.getValue("login.phone");
      if (phoneStorage)
        this.phoneNum = phoneStorage;
    },
    methods: {
      fixedS(){
          $('.l-info-call').show();
      },
      fixedH(){
        $('.l-info-call').hide();
      },
      openText(){
        var demoInput = document.getElementById("input-code");
        //隐藏text block，显示password block
          if (demoInput.type == "password") {
            demoInput.type = "text";
            this.imgSrc = showImgSrc;
          }else {
            demoInput.type = "password";
            this.imgSrc = hideImgSrc;
          }
      },
      pushRUrl(){
        this.$router.push('register')
      },
      pushFUrl(){
        this.$router.push('pwFindOut')
      },
      login(){
        const self = this;
        $('.l-info-call').hide();
        var phoneNum = self.phoneNum;
        var code = self.code;
        /*验证用户名和密码是否为空*/
        if (!self.check({
            phoneNum: phoneNum,
            code: code
          })) return;
        Indicator.open({
          text: '加载中...',
          spinnerType: 'fading-circle'
        });
        let params = {
          api: "/yg-user-service/login/doLogin.apec",
          data: {
            "mobile": self.phoneNum,
            "password": self.code,
          }
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if(item.succeed){
              var data = item.data;
              if (data.token) {
                c_js.setValue('token',data.token);
                self.$store.commit("incrementToken", {'token': data.token});
              }
              if(data.imgUrl){
                self.$store.commit("incrementImgUrl", {'imgUrl': data.imgUrl});//用户头像
                c_js.setLocalValue('imgUrl',data.imgUrl);
              }else{
                self.$store.commit("incrementImgUrl", {'imgUrl': ''});
                c_js.setLocalValue('imgUrl','');
              }
              if(data.name){
                self.$store.commit("incrementName", {'name': data.name});//用户名
                c_js.setLocalValue('name',data.name);
              }else{
                self.$store.commit("incrementName", {'name': ''});
                c_js.setLocalValue('name','');
              }
              if(data.userId){
                self.$store.commit("incrementUserID", {'userId': data.userId});//用户ID
                c_js.setLocalValue('userId',data.userId);
              }
              self.$store.commit("incrementPoint", {'point': data.point || 0});//积分
              c_js.setLocalValue('point',data.point || 0);

              self.$store.commit("incrementUserLevelName", {'userLevelName': data.userLevelName || ''});//等级积分 'QT'
              c_js.setLocalValue('userLevelName',data.userLevelName || '');

              self.$store.commit("incrementUserLevelKey", {'userLevelKey': data.userLevelKey || ''});//等级积分 '青铜'
              c_js.setLocalValue('userLevelKey',data.userLevelKey || '');

              self.$store.commit("incrementAurKey", {'userRealAuthKey': data.userRealAuthKey || ''});//验证情况
              c_js.setLocalValue('userRealAuthKey',data.userRealAuthKey || '');

              self.$store.commit("incrementUType", {'userTypeName': data.userTypeName});//用户身份 'DB'
              c_js.setLocalValue('userTypeName',data.userTypeName);

              self.$store.commit("incrementUTypeKey", {'userTypeKey': data.userTypeKey});//用户身份 'DB'
              c_js.setLocalValue('userTypeKey',data.userTypeKey);

              self.$store.commit("incrementAurName", {'userRealAuthName': data.userRealAuthName});//用户验证情况
              c_js.setLocalValue('userRealAuthName',data.userRealAuthName);

              c_js.setValue('login.phone', self.phoneNum);
              self.$store.commit("incrementPhone", {'phone': self.phoneNum});
              //var redirect = decodeURIComponent(self.$route.query.redirect || '/home');
              self.$router.push({name:'home'});
            }else{
              Toast({
                message:item.errorMsg,
                duration:1000
              })
            }
          Indicator.close();
          }).catch((error) => {
            console.log(error)
          Indicator.close();
          })
        } catch (error) {
          console.log(error)
          Indicator.close();
        }

      },
      check(){
        var phone_ID = document.getElementById("input-phonenum");
        var code = document.getElementById("input-code");
        if (!phone_ID.checkValidity()) {
          Toast("手机号码格式错误(11位手机号码)");
          return false;
        }
        if (!code.checkValidity()) {
          Toast("登录密码不能为空且最小长度为6位!");
          return false;
        }
        return true
      }
    },
    components: {
      topBar
    }
  }
</script>

  <style>.l-info-call{visibility:visible}
  @media screen and (max-height:400px)
  {.l-info-call{visibility:hidden}}
</style>

<style lang="stylus" rel="stylesheet/stylus" scoped>
  _rem=20rem;
  .login-page
    position:fixed;
    top:0;
    left:0;
    height 100%;
    width 100%;
    .mainform
      margin-top (92/_rem);
      .phone-input
        padding 0 (15/_rem)
        img
          width (17/_rem)
          height:(19/_rem)
          vertical-align middle
        #input-phonenum
          margin-left (10/_rem)
          height (24/_rem)
          font-size (16/_rem)
          color #999999
      .dash-line
        margin 0 (15/_rem)
        height 2px
        background-color #28cba7
        margin-top (16/_rem)
      .code-input,.code-input-text
        padding 0 (15/_rem)
        margin-top: (15/_rem)
        position relative
        img
          width: (17/_rem)
          height: (20/_rem)
          vertical-align middle
        .i-password-hide
          right (15/_rem)
          top 50%
          transform translateY(-50%)
          position absolute
        .pure-input-1
          margin-left (10/_rem)
          height (24/_rem)
          font-size (16/_rem)
          color #999999
      .dash-line-v
        margin 0 (15/_rem)
        height 1px
        background-color #D7D7D7
        margin-top (16/_rem)
      .login-btn
        margin (25/_rem) (15/_rem) 0 (15/_rem)
        #btn-login-code
          color #ffffff
          line-height (15/_rem)
          height (40/_rem)
          font-size (15/_rem)
          background-color: #28CBA7;
          border-radius: 0;
          display: inline-block;
          width: 100%;
          border: 1px solid #0bbe06;
      .l-info
        margin (17/_rem) (15/_rem) 0 (15/_rem)
        display: flex
        height (40/_rem)
        .l-info-z
          border 1px solid #28CBA7
          width (150/_rem)
          text-align center
          flex 1
          span
            height (40/_rem)
            line-height (40/_rem)
            font-size (16/_rem)
            color #28CBA7
        .flex-main
          width (45/_rem)
        .l-info-p
            border 1px solid #28CBA7
            width (150/_rem)
            text-align center
            flex 1
            span
              height (40/_rem)
              line-height (40/_rem)
              font-size (16/_rem)
              color #28CBA7
      .l-info-call
        width 100%
        position fixed;
        bottom (38/_rem)
        text-align center
        .call-info
          a
            font-size (16/_rem)
            color #28CBA7
            height (16/_rem)
            line-height (16/_rem)
          span
            font-size (16/_rem)
            color #28CBA7
            height (16/_rem)
            line-height (16/_rem)
        .call-time
          span
            font-size (12/_rem)
            color #666666
            height (16/_rem)
            line-height (16/_rem)
</style>
