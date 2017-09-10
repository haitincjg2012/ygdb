<template>
  <div class="pwfindout-page">
    <top-bar title="找回密码"></top-bar>
    <div class="pure-g mainform">
      <div class="pure-g-l login-content">
        <div class="loggin-panel">
          <form onsubmit="return false">
            <div class="phone-input">
              <span>手机号</span>
              <input id="input-phonenum" oninvalid="setCustomValidity(' ')" type="tel" v-model="phoneNum" placeholder="请输入手机号码"
                     oninput="setCustomValidity('')" required maxlength="11" pattern="^1(3|4|5|7|8)\d{9}$"/>
            </div>
            <div class="dash-line">
            </div>
            <div class="code-input">
              <span>验证码</span>
              <input oninvalid="setCustomValidity(' ')" id="input-code" type="tel" class="pure-input-1" v-model="code" oninput="setCustomValidity('')"
                     placeholder="请输入验证码" required maxlength="4" pattern="^\d{4}$">
              <input @click.stop="sendMessage" type="button" id="btn-message" value="获取验证码">
            </div>
            <div class="dash-line-f-v">
            </div>
            <div class="login-btn">
              <input :class="loginBtnCls" type="submit" id="btn-login-code" value="下一步" @click="login"></input>
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
  import topBar from '../../components/topBar/topBar'
  import codeGet from './js/getPhoneCode'
  import API from '../../api/api'
  import c_js from '../../assets/js/common'
  import {Indicator} from 'mint-ui';
  import { Toast } from 'mint-ui';
  import { MessageBox } from 'mint-ui';

  const api = new API();
  export default {
    data(){
      return {
        phoneNum: '',
        code: '',
        loginBtnClass:'btn-login-c',
      }
    },
    watch: {
      code: function (newVal,oldVal) {
        if(newVal!=oldVal && newVal.length)
          this.loginBtnClass = 'btn-login-c login-confirm';
        else
          this.loginBtnClass = 'btn-login-c';
      },
    },
    activated(){
        this.code='';
    },
    deactivated(){
    },
    computed:{
      loginBtnCls(){
        return this.loginBtnClass;
      }
    },
    created(){
      var phoneStorage = c_js.getValue("login.phone");

      if (phoneStorage && phoneStorage!='undefined'){
        this.phoneNum = phoneStorage;
      }
    },
    methods: {
      login(){
        const self = this;
        const vaData = [
          {
            handler: document.getElementById("input-phonenum"),
            message: '手机号码格式错误(11位手机号码)'
          },
          {
            handler: document.getElementById("input-code"),
            message: '请输入正确的验证码(4位数字)'
          }
        ];
        /*验证用户名和密码是否为空*/
        if (!self.check(vaData)) return;
//        Indicator.open({
//          text: '加载中...',
//          spinnerType: 'fading-circle'
//        });
        let openId = c_js.getValue("openId");
        let params = {
          api: "/_node_user/_captcha.apno",
          data: {
            "mobile": self.phoneNum,
            "captcha": self.code,
          }
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if(item.succeed){
              var data = item.data;
              self.$store.commit("incrementICode", {'id_code': self.code});
              self.$store.commit("incrementPhone", {'phone': self.phoneNum});
              c_js.setValue("login.phone",self.phoneNum);
              self.$router.push({
                path: '/pwFindOutCom'
              });
            }else{
              Toast({
                message:"请求失败:" + item.errorMsg,
                duration:1000
              })
            }
            //Indicator.close();
          }).catch((error) => {
            console.log(error)
            //Indicator.close();
          })
        } catch (error) {
          console.log(error)
          //Indicator.close();
        }

      },
      // 城市信息与用户手机号绑定
      sendMessage(){
        if (!this.check_send_message())
          return;
        this.sendCode($("#btn-message"), $("#input-phonenum"));
      },
      //发送验证码
      sendCode(obj, phonenumObj) {
        var phonenum = phonenumObj.val();
        if (phonenum) {
          this.doPostBack();
          codeGet.addCookie("secondsremained", 60, 60);//添加cookie记录,有效时间60s
          codeGet.settime(obj);//开始倒计时
        }
      },
      //将手机利用ajax提交到后台的发短信接口
      doPostBack() {
        const self = this;
        let params = {
          api: "/yg-message-service/smsMessage/captcha.apec",
          data: {
            "mobile": self.phoneNum,
            "templateKey": 'FIND_PASSWORD_CAPTCHA_TEMPLATE'
          }
        }
        try {
          api.post(params).then((res)=>{
            var item = res.data;
            if(item.succeed){
              Toast("短信发送成功");
            }else {
              Toast("请求失败:"+item.errorMsg+"稍后重试");
            }
          }).catch((error) => {
            console.log(error);
            Toast("请求失败:"+error);
          })
        } catch (error) {
          console.log(error);
          Toast("请求失败:"+error);
        }
      },
      check_send_message(){
        var phone_ID = document.getElementById("input-phonenum");
        if (!phone_ID.checkValidity()) {
          Toast("手机号码格式错误(11位手机号码)");
          return false;
        }
        return true;
      },
      check(object){
        var flag = true;
        var toastMessage='';
        object.forEach((item)=>{
          if (!item.handler.checkValidity()) {
            toastMessage = item.message;
            flag=false;
            return false;
          }
        });

        if(!flag){
          Toast(toastMessage);
          return false;
        }else{
          return true;
        }
      }
    },
    components: {
      topBar
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus" scoped>
  _rem=20rem;
  .pwfindout-page
    position:fixed;
    top:0;
    left:0;
    height 100%;
    width 100%;
    input
      height (24/_rem)!important
    .mainform
      margin-top (18/_rem)
      .phone-input
        padding 0 (15/_rem)
        span
          font-size (16/_rem)
          color #3C424F
        #input-phonenum
          margin-left (10/_rem)
          height (17/_rem)
          font-size (16/_rem)
          color #999999
      .dash-line-f-v
        margin 0 (15/_rem)
        height 1px
        background-color #D7D7D7
        margin-top (15/_rem)
      .dash-line
        margin 0 (15/_rem)
        height 1px
        background-color #D7D7D7
        margin-top (15/_rem)
      .code-input
        padding 0 (15/_rem)
        margin-top: (15/_rem)
        span
          font-size (16/_rem)
          color #3C424F
        #btn-message
          width: (90/_rem)
          height: (25/_rem)
          background-color #28CBA7
          color #fff
          border-radius 4px
        #btn-message:disabled
          color: #fff;
          border: 1px solid #d8d8d8;
          background-color: #d8d8d8;
          opacity: 1;
        #input-code
          margin-left (10/_rem)
          height (16/_rem)
          font-size (16/_rem)
          color #999999
          width (180/_rem)
      .login-btn
        margin (25/_rem) (15/_rem) 0 (15/_rem)
        .btn-login-c
          color #ffffff
          line-height (15/_rem)
          height (40/_rem)!important
          font-size (15/_rem)

          border-radius: 0;
          display: inline-block;
          width: 100%;

        .login-confirm
          background-color: #28CBA7;
          border: 1px solid #0bbe06;
          color #ffffff
      .register-law
        height (20/_rem)
        margin-top (22/_rem)
        text-align center
        line-height (20/_rem)
        span
          font-size (12/_rem)
          color #999999
          height (20/_rem)
        a
          font-size (12/_rem)
          color #28CBA7
          height (20/_rem)
</style>
