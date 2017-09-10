<template>
  <div class="pwfindout-page">
    <top-bar title="找回密码"></top-bar>
    <div class="pure-g mainform">
      <div class="pure-g-l login-content">
        <div class="loggin-panel">
          <form onsubmit="return false">
            <div class="phone-input">
              <span>新的密码</span>
              <input id="input-code" oninvalid="setCustomValidity(' ')" class="pure-input-1" type="password" v-model="code" placeholder="请输入新的登录密码"
                     oninput="setCustomValidity('')" required  minlength="6"/>
              <img @click.stop="openText($event,'input-code')" class="i-password-hide" :src="imgSrc">
            </div>
            <div class="dash-line">
            </div>
            <div class="code-input">
              <span>确认密码</span>
              <input oninvalid="setCustomValidity(' ')" id="input-codeCom" type="password" class="pure-input-1" v-model="codeCom" oninput="setCustomValidity('')"
                     placeholder="请输入确认密码" v-on:blur="codeComCheck()" required minlength="6">
              <!--<img @click.stop="openText($event,'input-codeCom')" class="i-password-hide" :src="imgSrc">-->
            </div>
            <!--<div :class="rClass">两次密码不相同，请重新输入~</div>-->
            <div class="dash-line-v">
            </div>
            <div class="login-btn">
              <input :class="loginBtnCls" type="submit" id="btn-login-code" value="确认" @click="login"></input>
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

  import showImgSrc from '../../../src/assets/img/show.png'
  import hideImgSrc from '../../../src/assets/img/hide.png'

  const api = new API();
  export default {
    data(){
      return {
        code: '',
        codeCom: '',
        imgSrc:hideImgSrc,
        loginBtnClass:'btn-login-c',
        rClass:'r-val'
      }
    },
    watch: {
      codeCom: function (newVal,oldVal) {
        if(newVal!=oldVal && newVal.length)
          this.loginBtnClass = 'btn-login-c login-confirm';
        else
          this.loginBtnClass = 'btn-login-c';
      }
    },
    computed:{
      loginBtnCls(){
        return this.loginBtnClass;
      }
    },
    created(){
    },
    activated(){
      this.code='';
      this.codeCom='';
    },
    methods: {
      codeComCheck(){
          if(this.code!==this.codeCom)
          {
              this.rClass = 'vic';
            this.codeCom = '';
          }else{
            this.rClass = 'r-val'
          }
      },
      openText(event,dom){
        var demoInput = document.getElementById(dom);
        //隐藏text block，显示password block
        if (demoInput.type == "password") {
          demoInput.type = "text";
          this.imgSrc = showImgSrc;
        }else {
          demoInput.type = "password";
          this.imgSrc = hideImgSrc;
        }
      },
      login(){
        const self = this;
        const vaildCode = self.$store.state.id_code;
        const phone = self.$store.state.phone;
        if(!vaildCode)
        {
          Toast("验证码失效，请重新获取!");
          return;
        }

        const vaData = [
          {
            handler: document.getElementById("input-code"),
            message: '新登录密码不能为空且最小长度为6位!'
          },
          {
            handler: document.getElementById("input-codeCom"),
            message: '确认密码不能为空且最小长度为6位!'
          }
        ];
        if (!self.check(vaData)) return;
        Indicator.open({
          text: '加载中...',
          spinnerType: 'fading-circle'
        });
        let params = {
          api: "/yg-user-service/user/getPassword.apec",
          data: {
            "mobile": phone,
            "password":self.code,
            "entruePassword":self.codeCom,
            "vaildCode": vaildCode
          }
        }
        console.log(params)
        try {
          api.post(params).then((res) => {
            var item = res.data;
          if(item.succeed){
            var data = item.data;
            self.$store.commit("incrementICode", {'id_code': ''});
            self.$router.push({
              path: '/login'
            });
          }else{
            self.$store.commit("incrementICode", {'id_code': ''});
            Toast({
              message:"请求失败:" + item.errorMsg,
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
        if(this.code!==this.codeCom){
          flag=false;
          toastMessage='两次密码不相同，请重新输入';
        }


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
        position relative
        span
          font-size (16/_rem)
          color #3C424F
        #input-phonenum
          margin-left (10/_rem)
          height (17/_rem)
          font-size (16/_rem)
          color #999999
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
          height (16/_rem)
          font-size (16/_rem)
          color #999999
      .vic
        visibility: visible;
        font-size: 12px;
        color: red;
        margin-left: (95/_rem)
      .r-val
        font-size: 12px;
        color: red;
        visibility: hidden;
        margin-left: (95/_rem)
      .dash-line
        margin 0 (15/_rem)
        height 1px
        background-color #D7D7D7
        margin-top (15/_rem)
      .dash-line-v
        margin 0 (15/_rem)
        height 1px
        background-color #D7D7D7
      .code-input
        padding 0 (15/_rem)
        margin-top: (15/_rem)
        position relative
        img
          width: (17/_rem)
          height: (20/_rem)
          vertical-align middle
        span
          font-size (16/_rem)
          color #3C424F
        .pure-input-1
          margin-left (10/_rem)
          height (16/_rem)
          font-size (16/_rem)
          color #999999
        #btn-message
          width: (90/_rem)
          height: (25/_rem)
          background-color #28CBA7
          color #fff
          border-radius 4px
        #input-code
          margin-left (10/_rem)
          height (16/_rem)
          font-size (16/_rem)
          color #999999
          width (180/_rem)
        .i-password-hide
          right (15/_rem)
          top 50%
          transform translateY(-50%)
          position absolute
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
</style>
