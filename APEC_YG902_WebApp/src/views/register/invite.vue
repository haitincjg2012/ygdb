<template>
  <div class="c-invite">
    <div class="c-invite-firstPiece">
      <div class="c-invite-f-wrapper">
        <div class="c-invite-f-style-com">
          <input type="tel" class="c-invite-f-input-com" placeholder="请输入手机号" v-model="phone" required maxlength="11"/>
        </div>
        <div class="c-invite-f-style-com">
          <input type="number" v-model="verificationCode" class="c-invite-s-input-com" placeholder="请输入验证码" required />
          <input type="button" class="c-invite-t-input-com" value="获取验证码" ref="VerificationCode" @click="getVerificationCode" >
        </div>
        <div class="c-invite-f-style-com">
          <input type="password" v-model="password" class="c-invite-f-input-com"  placeholder="请设置登录密码" required />
        </div>
        <h6 class="c-invite-identify-tips">请选择你的身份:</h6>
        <div class="c-invite-identify-item">
          <div v-for="item in enumTagS" class="c-invite-id-item-content">
            <div class="c-invite-id-item-child"
                 :class="{'c-invite-identify-active':item.flag}"
                 @click="choiceIdentity(item)"
            >{{item.name}}</div>
          </div>
        </div>
        <div class="c-invite-regist">
          <input type="button" value="注册" class="c-invite-submitBtn" @click="invitLogin"/>
          <!--<form id="formLogin" @submit="invitLogin" >-->
          <!---->
          <!--</form>-->
        </div>
        <p class="c-invite-protocol"><span class="c-invite-protocol-first-sp">点击注册即代表您同意</span><span class="c-invite-protocol-second-sp" @click="protocol">《平台用户协议》</span></p>
      </div>
    </div>
    <!--<div @click="getVerificationCode">测试二位码</div>-->
    <div class="c-invite-guide">
      <img src="../../assets/img/guide.png" class="c-invite-guide-picture" />
    </div>
    <pdWin></pdWin>
  </div>
</template>
<style>
  @import "../../assets/css/invite.css";
</style>
<script>
  import scrollbg from '../../components/scroll/scrollbg.vue'
  import pdWin from '../login/viewComponents/proWindow.vue'
  import p from "../../components/post.vue"
  import codeGet from '../login/js/getPhoneCode'
  import {Toast, Indicator} from 'mint-ui'

  export default{
      data(){
          return{
            enumTagS:[{name:"代办",
                        type:"DB",
                        flag:false,},
                      {name:"客商",
                       type:"KS",
                       flag:false,},
                      {name:"冷库",
                        type:"LK",
                        flag:false,},
                     {name:"果农",
                       type:"ZZH",
                       flag:false,},
                    {name:"合作社",
                      type:"HZS",
                      flag:false,},],
            phone:"",//电话
            verificationCode:"",//验证码
            password:"",//密码
            idType:"",//身份类型
          }
      },
      methods:{
        protocol(){
          var that = this;
          that.$root.eventHub.$emit('windowEvent');
        },
        qrcode(){
          var that = this;
          that.$router.push({name:'qrcode'});//跳转页面
        },
          resetEnumTags(){
              var that = this;
              that.enumTagS.forEach(function (current) {
                if(current.flag){
                  current.flag = false;
                }
              });
          },
        choiceIdentity(item){
          var that = this;
          that.resetEnumTags();
          item.flag = true;
          that.idType = item.type;
        },
        invitLogin(){
            var that = this;
            var phonePattern = /^1(3|4|5|7|8)\d{9}$/g;
            //手机号码不能为空,手机号码格式错误两种不同行为
            if(!that.phone){
              Toast("手机号码不能为空!");
               return;
            }else if(!phonePattern.test(that.phone)){
                Toast("手机号码格式错误!");
                return;
            }else if(!that.verificationCode){
                Toast("验证码不能为空!");
                return;
            }else if(!that.password){
              Toast("密码不能为空!");
              return;
            }else if(!that.idType){
              Toast("请选择您的身份!");
                return
            }

          var id = this.$route.query.id;
          Indicator.open({
            text: '加载中...',
            spinnerType: 'fading-circle'
          });

          let params = {
            api: "/yg-user-service/user/addNewUser.apec",
            data: {
              "mobile": that.phone,
              "password": that.password,
              "userType":that.idType,
              "vaildCode":that.verificationCode,
              "referralId":id,//推荐人id
            }
          }

          p.post(params, function (data) {
            Indicator.close();
            if(data.succeed){
              that.$router.push({name:'qrcode'});//跳转页面
            }else{
              Toast("请求失败:"+data.errorMsg+" 稍后重试");
            }
          }, function (error) {
            Indicator.close();
            Toast("请求失败:"+error+"  稍后重试");
          });
        },
        getVerificationCode(){
            var that = this,
                el = that.$refs.VerificationCode,
                attr = el.attributes;


          let params = {
            api: "/yg-user-service/user/isHasMobile.apec",
            data: {
              "mobile": that.phone
            }
          }

          p.post(params, function (data) {
              if(data.succeed){
                 if(!data.data){
                     //号码不存在
                   var typ=document.createAttribute("disabled");
                   typ.value = "disabled";
                   attr.setNamedItem(typ);
                   var addClass = attr.getNamedItem("class").value + " c-verfication-code-active";

                   attr.getNamedItem("class").value = addClass;
                   that.doPostBack(attr, el);
                 }else{
                     Toast("该号码已经存在!");
                     that.$router.push({name:'qrcode'});//跳转页面
                 }
              }

          });
        },
        doPostBack(attr, el) {
          var that = this;
          let params = {
            api: "/yg-message-service/smsMessage/captcha.apec",
            data: {
              "mobile": that.phone,
              "templateKey": 'REGISTER_CAPTCHA_TEMPLATE'
            }
          }

          p.post(params, function (data) {
            if(data.succeed){
              Toast("短信发送成功");
//              codeGet.addCookie("secondsremained", 60, 60);//添加cookie记录,有效时间60s
              that.settime(attr, el);
//              codeGet.settime(obj);//开始倒计时
            }else{
              Toast("请求失败:"+data.errorMsg+"稍后重试");
            }
          },function (error) {
            Toast("请求失败:"+error);
          })
        },
        //开始倒计时
        settime: function (attr, el) {
          var that = this;
            var countdown = 60,timeId;
            timeId = setInterval(function () {
              if (countdown == 0) {
                clearInterval(timeId);//消除定时器
                el.classList.remove('c-verfication-code-active');
                attr.removeNamedItem("disabled");
                attr.getNamedItem("value").value = "重新发送"
                return;
              } else {
                attr.getNamedItem("value").value = "重新发送(" + countdown + ")";
                countdown--;
              }
            }, 1000)
        }
      },
      activated(){
//        let params = {
//          api: "/yg-user-service/user/isHasMobile.apec",
//          data: {
//            "mobile": that.phone
//          }
//        }
//          p.post();
      },
    components:{
      pdWin,
      scrollbg
    }
  }
</script>
