<template>
  <div class="register-page">
    <top-bar title="注册"></top-bar>
    <div class="pure-g mainform">
      <div class="pure-g-l login-content">
        <div class="loggin-panel">
          <form onsubmit="return false">
            <div class="phone-input-t" :class="{rphone:showPFlag}">
              <!--<img src="../../assets/img/phone.png">-->
              <!--placeholder="请输入手机号码" v-on:blur="phoneCheck()" v-on:focus="phoneClear()"-->
              <input id="input-phonenum-T" oninvalid="setCustomValidity(' ')" type="tel" v-model="phoneNum"
                     placeholder="请输入手机号码" v-on:focus="phoneClear()"
                     oninput="setCustomValidity('')" required maxlength="11" pattern="^1(3|4|5|7|8)\d{9}$"/>
            </div>
            <div :class="rClass">该用户已注册~</div>
            <div class="dash-line" :class="{line:showPFlag}">
            </div>
            <div class="code-input-t c-z-code-t" :class="{rcode:showCodeFlag}">
              <!--<img src="../../assets/img/Verification.png">-->
              <!--<input oninvalid="setCustomValidity(' ')" id="input-code" type="tel" class="pure-input-1 c-input-code" v-model="code"-->
              <input oninvalid="setCustomValidity(' ')" type="tel" class="pure-input-1 c-input-code" v-model="code"
                     oninput="setCustomValidity('')"
                     @focus="showFlag('code')"
                     @blur="showFlag('no')"
                     placeholder="请输入验证码" required maxlength="4" pattern="^\d{4}$">

              <!--<input type="button" @click.stop="sendMessageO" id="btn-message" value="获取验证码">-->
              <input type="button" @click.stop="phoneCheck" id="btn-message" value="获取验证码">
            </div>
            <div class="dash-line-v" :class="{line:showCodeFlag}">
            </div>
            <div class="code-input-t c-z-password-t" :class="{rpassword:showPassWordFlag}">
              <!--<img src="../../assets/img/PasswordOn.png">-->
              <input oninvalid="setCustomValidity(' ')" id="input-code-password" type="password" class="pure-input-1"
                     v-model="password" oninput="setCustomValidity('')"
                     @focus="showFlag('password')"
                     @blur="showFlag('no')"
                     placeholder="请输入登录密码" required minlength="6">
              <img @click.stop="openText()" class="i-password-hide" :src="imgSrc">
            </div>
            <div class="dash-line-p" :class="{line:showPassWordFlag}">
            </div>
            <div class="s-a">
              <div class="s-a-label">
                <img src="../../assets/img/identity.png">
                <span>请选择你的身份</span>
              </div>
              <div class="s-a-box">
                <div v-for="item in dataAur" @click.stop="AurSelect($event,item)" :class="item.class"><span>{{item.name}}</span>
                </div>
              </div>
              <div class="dash-line-s"></div>
            </div>
            <div class="dash-line-v">
            </div>
            <div class="login-btn">
              <input :class="loginBtnCls" type="submit" id="btn-login-code" value="注册" @click="register"></input>
            </div>
            <div class="register-law">
              <span>点击上面注册，代表您同意</span><a @click.stop="show();">《平台用户协议》</a>
            </div>
          </form>
        </div>
      </div>
    </div>
      <pdWin></pdWin>
  </div>
</template>
<style>
@import "../../assets/css/regist.css";
</style>
<script>
  import $ from 'zepto'
  import axios from 'axios'
  import topBar from '../../components/topBar/topBar'
  import codeGet from './js/getPhoneCode'
  import API from '../../api/api'
  import c_js from '../../assets/js/common'
  import {Indicator} from 'mint-ui';
  import {Toast} from 'mint-ui';
  import {MessageBox} from 'mint-ui';
  import pdWin from './viewComponents/proWindow.vue'
  import showImgSrc from '../../../src/assets/img/show.png'
  import hideImgSrc from '../../../src/assets/img/hide.png'
  const api = new API();

  export default {
    data(){
      return {
        phoneFlag:false,//电话注册的标志位
        phoneNum: '',
        code: '',
        smsRecord:0,//用于记录点击的次数
        imgSrc: hideImgSrc,
        password: '',
        aur: 'DB',//默认代办
        loginBtnClass:'btn-login-c',//登录按钮样式
        rClass:'r-val',
        showWin:false,
        dataAur: [
          {
            aur: 'DB',
            name: '代办',
            class: 's-a-box-db visited'
          },
          {
            aur: 'KS',
            name: '客商',
            class: 's-a-box-db'
          },
          {
            aur: 'LK',
            name: '冷库',
            class: 's-a-box-db'
          },
          {
            aur: 'ZZH',
            name: '果农',
            class: 's-a-box-db'
          },
          {
            aur: 'HZS',
            name: '合作社',
            class: 's-a-box-db'
          }
        ],
        showPFlag:false,//默认情况电话是没有焦点
        showCodeFlag:false,//默认情况先验证码是没有焦点
        showPassWordFlag:false,//默认情况密码是没有焦点
      }
    },
    created(){
    },
    watch: {
      password: function (newVal,oldVal) {
        const self =this;
        if(newVal!=oldVal && newVal.length && self.code.length && self.phoneNum.length)
          this.loginBtnClass = 'btn-login-c login-confirm';
        else
          this.loginBtnClass = 'btn-login-c';
      },
      code: function (newVal,oldVal) {
        const self =this;
//        if(newVal!=oldVal && newVal.length && self.password.length && self.phoneNum.length)
//          this.loginBtnClass = 'btn-login-c login-confirm';
//        else
//          this.loginBtnClass = 'btn-login-c';


      },
      phoneNum: function (newVal,oldVal) {
        const self =this;
        if(newVal!=oldVal && newVal.length && self.password.length && self.code.length)
          this.loginBtnClass = 'btn-login-c login-confirm';
        else
          this.loginBtnClass = 'btn-login-c';
      },
    },
    computed:{
        loginBtnCls(){
            return this.loginBtnClass;
        }
    },
    activated(){
       this.phoneFlag = false;
       this.phoneNum = "";
       this.code = "";
       this.password = "";
    },
    methods: {
      show () {
        this.$root.eventHub.$emit('windowEvent');
      },
      showInit(){
        this.showPFlag = false;
        this.showCodeFlag = false;
        this.showPassWordFlag = false;
      },
      showFlag(flag){
          this.showInit();
          if(flag == "no"){
            return;
          }
          switch (flag){
            case "phone":
              this.showPFlag = true;
                break;
            case "code":
              this.showCodeFlag = true;
              break;
            case "password":
              this.showPassWordFlag = true;
              break;
          }
      },
      IsRSource() {//注册来源
        var s = 'PC';
        var userAgentInfo = navigator.userAgent;
        var Agents = ["Android", "iPhone",
          "SymbianOS", "Windows Phone",
          "iPad", "iPod"];
        for (var v = 0; v < Agents.length; v++) {
          if (userAgentInfo.indexOf(Agents[v]) > 0) {
            s = Agents[v];
            break;
          }
        }
        return s;
      },
      phoneCheck(){//检查用户是否注册过
        this.smsRecord ++;
        if(this.smsRecord != 1){
            return;
        }
        var phoneNum = this.phoneNum;
        var self = this;
        this.showFlag("no");
        if(!phoneNum.length)
            return;

//        $("#btn-message").attr("disabled", true);
        /*验证手机号码是否为空*/
        if (!this.check([{
            handler: document.getElementById("input-phonenum-T"),
            message:'手机号码格式错误(11位手机号码)'
          }])) return;
        let params = {
          api: "/yg-user-service/user/isHasMobile.apec",
          data: {
            "mobile": this.phoneNum
          }
        }
        try {
          api.post(params).then((res) => {
            var data = res.data;
            if(data.succeed && !data.data){//未注册用户
              this.sendMessage();
            }else{
              this.rClass='vic'
              this.phoneNum = '';
              $("#btn-message").attr("disabled", true);
            }
//            else{
//              Toast({
//                message:"请求失败:" + data.errorMsg,
//                duration:1000
//              })
//            }
          }).catch((error) => {
            console.log(error)
          })
        } catch (error) {
          console.log(error)
        }
      },
      noPhone(){
        //未注册手机号码
        this.sendMessage();
      },
      hasPhone(){
        //已注册手机号码
        $("#btn-message").attr("disabled", true);
      },
      phoneClear(){
        this.smsRecord = 0;
        $("#btn-message").removeAttr("disabled");
        this.showFlag("phone");
        this.rClass = 'r-val';
      },
      AurSelect(event, item){
        const self = this;
        self.dataAur.forEach((i) => {
          if (item.aur === i.aur) {
            i.class = 's-a-box-db visited'
            self.aur = item.aur;
          }
          else
            i.class = 's-a-box-db'
        })
      },
      openText(){
        var demoInput = document.getElementById("input-code-password");
        //隐藏text block，显示password block
        if (demoInput.type == "password") {
          demoInput.type = "text";
          this.imgSrc = showImgSrc;
        } else {
          demoInput.type = "password";
          this.imgSrc = hideImgSrc;
        }
      },
      register(){
        const self = this;
        /*验证用户名和密码是否为空*/
        const vaData = [
          {
            handler: document.getElementById("input-phonenum-T"),
            message: '手机号码格式错误(11位手机号码)'
          },
          {
//            handler: document.getElementById("input-code"),
            handler: document.querySelector(".c-input-code"),
            message: '验证码格式错误(4位数字)'
          },
          {
            handler: document.getElementById("input-code-password"),
            message: '登录密码不能为空且最小长度为6位!'
          }
        ];
        if (!self.check(vaData)) return;
        Indicator.open({
          text: '加载中...',
          spinnerType: 'fading-circle'
        });
        let params = {
          api: "/yg-user-service/user/addNewUser.apec",
          data: {
            "mobile": self.phoneNum,
            "password": self.password,
            "userType":self.aur,
            "vaildCode":self.code
          }
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;

            if (item.succeed) {
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
            } else {
              Toast({
                message: "请求失败:" + item.errorMsg,
                duration: 1000
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
      sendMessageO(){

      },
      sendMessage(){
        if (!this.check_send_message())
          return;
        this.sendCode($("#btn-message"), $("#input-phonenum-T"));
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
        let params = {
          api: "/yg-message-service/smsMessage/captcha.apec",
          data: {
            "mobile": this.phoneNum,
            "templateKey": 'REGISTER_CAPTCHA_TEMPLATE'
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
        var phone_ID = document.getElementById("input-phonenum-T");
        if (!phone_ID.checkValidity()) {
          Toast("验证码格式错误");
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

//        var phone_ID = document.getElementById("input-phonenum");
//        var code = document.getElementById("input-code");
//        if (!phone_ID.checkValidity()) {
//          Toast("手机号码格式错误(11位手机号码)");
//          return false;
//        }
//        if (!code.checkValidity()) {
//          Toast("短信验证码格式错误(4位数字)");
//          return false;
//        }
//        return true
      }
    },
    components:{
      pdWin,
      topBar
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus" scoped>
  _rem = 20rem;
  .register-page
    position: fixed;
    top: 0;
    left: 0;
    height 100%;
    width 100%;
    input
      height (24/_rem)!important
    .mainform
      margin-top (16 /_rem)
      .phone-input-t
        margin 0 (15 /_rem)
        img
          width (17 /_rem)
          height: (19 /_rem)
          vertical-align middle
        #input-phonenum-T
          margin-left (30 /_rem)
          height (17 /_rem)
          font-size (16 /_rem)
          color #999999
      .vic
        visibility: visible;
        font-size: 12px;
        color: red;
        margin-left: (48/_rem)
      .r-val
        font-size: 12px;
        color: red;
        visibility: hidden;
        margin-left: (48/_rem)
      .dash-line
        margin 0 (15 /_rem)
        height 1px
        background-color #D7D7D7
      .dash-line-v
        margin 0 (15 /_rem)
        height 1px
        background-color #D7D7D7
        margin-top (15 /_rem)
      .code-input-t
        margin  0 (15 /_rem)
        margin-top: (15 /_rem)
        position relative
        img
          width: (17 /_rem)
          height: (20 /_rem)
          vertical-align middle
        #btn-message
          width: (90 /_rem)
          height: (25 /_rem)
          position absolute
          top 50%
          transform translateY(-50%)
          right (15/_rem)
          background-color #28CBA7
          color #fff
          border-radius 4px
        #btn-message:disabled
          color: #fff;
          border: 1px solid #d8d8d8;
          background-color: #d8d8d8;
          opacity: 1;
        .i-password-hide
          right (15 /_rem)
          top 50%
          transform translateY(-50%)
          position absolute
      .c-input-code, #input-code-password
          margin-left (30 /_rem)
          height (16 /_rem)
          font-size (16 /_rem)
          color #999999
          vertical-align middle
      .dash-line-p
        margin 0 (15 /_rem)
        height 1px
        background-color #d7d7d7
        margin-top (16 /_rem)
      .login-btn
        margin (25 /_rem) (15 /_rem) 0 (15 /_rem)
        .btn-login-c
          color #ffffff
          line-height (15 /_rem)
          height (40 /_rem)!important
          font-size (15 /_rem)

          border-radius: 0;
          display: inline-block;
          width: 100%;
          border-radius (5/_rem)
        .login-confirm
          background-color: #28CBA7;
          color #FFFFFF;
          border-radius (5/_rem)
      .register-law
        height (20 /_rem)
        margin-top (22 /_rem)
        text-align center
        line-height (20 /_rem)
        span
          font-size (12 /_rem)
          color #999999
          height (20 /_rem)
        a
          font-size (12 /_rem)
          color #28CBA7
          height (20 /_rem)
      .l-info
        margin (17 /_rem) (15 /_rem) 0 (15 /_rem)
        display: flex
        height (40 /_rem)
        .l-info-z
          border 1px solid #28CBA7
          width (150 /_rem)
          text-align center
          flex 1
          span
            height (40 /_rem)
            line-height (40 /_rem)
            font-size (16 /_rem)
            color #28CBA7
        .flex-main
          width (45 /_rem)
        .l-info-p
          border 1px solid #28CBA7
          width (150 /_rem)
          text-align center
          flex 1
          span
            height (40 /_rem)
            line-height (40 /_rem)
            font-size (16 /_rem)
            color #28CBA7
      .l-info-call
        width 100%
        position absolute
        bottom (38 /_rem)
        text-align center
        .call-info
          a
            font-size (16 /_rem)
            color #28CBA7
            height (16 /_rem)
            line-height (16 /_rem)
          span
            font-size (16 /_rem)
            color #28CBA7
            height (16 /_rem)
            line-height (16 /_rem)
        .call-time
          span
            font-size (16 /_rem)
            color #28CBA7
            height (16 /_rem)
            line-height (16 /_rem)
      .s-a
        margin (18 /_rem) (15 /_rem) 0 (15 /_rem)
        .s-a-label
          img
            width (18 /_rem)
            height (16 /_rem)
            vertical-align middle
          span
            font-size (15 /_rem)
            color: #999999
        .s-a-box
          padding (15 /_rem) (5 /_rem) 0 (5 /_rem)
          font-size 0
          .s-a-box-db
            width (55 /_rem)
            border 1px solid #CCD0D1
            border-radius 4px
            text-align center
            display inline-block
            cursor pointer
            height (25 /_rem)
            line-height (25 /_rem)
            font-size 0
            color #999999
            span
              font-size (15 /_rem)
          .visited
            background-color #28CBA7
            color #ffffff !important
            border-color #28CBA7
        .s-a-box-db:not(:first-child)
          margin-left (10 /_rem)

</style>

<style>

</style>
