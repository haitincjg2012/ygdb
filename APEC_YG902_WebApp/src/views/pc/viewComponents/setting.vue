<template>
  <div class="c-setting">
    <div class="c-s-head c-s-box-com">
       <div class="c-s-h-back" @click="back">
         <img src="../../../assets/img/ret.png">
       </div>
       <div class="c-s-h-title"><h4>设置</h4></div>
    </div>
    <div class="c-s-line"></div>
    <div class="c-s-password c-s-box-com"  @click="updateSecret">
       <div class="c-s-p-text">修改密码</div>
       <div class="c-s-p-forward">
         <img src="../../../assets/img/back.png">
       </div>
    </div>
    <split></split>
    <div class="c-s-logout c-s-box-com" @click="logout">
       安全退出
    </div>
  </div>
</template>
<style>
  @import "../../../assets/css/setting.css";
</style>
<script>
  import split from '../../../components/split/split';
  import c_js from '../../../assets/js/common'
  import API from '../../../api/api';
  const api = new API();
export default{
  data(){
      return{}
  },
  methods:{
      back(){
          this.$router.go(-1);

      },
      updateSecret(){
         this.$router.push({name: 'updateSecret'});
      },
     logout(){
       //安全退出到登录页面

       const self = this;

       let params = {
         api: "/yg-user-service/login/loginOut.apec",
         data: {}
       }

       try {
         api.post(params).then((res) => {
           var item = res.data;
           if (item.succeed) {
             window.localStorage.clear();
//             c_js.clearCookie("login.phone");
             self.delCookie("login.phone");

             self.$router.push({name:'login'})
           }
         }).catch((error) => {
         })
       } catch (error) {
         console.log(error)
       }
     },
    delCookie(name)
    {
      var exp = new Date();
      exp.setTime(exp.getTime() - 1000);
//      var cval=getCookie(name);
      var cval="";

//      if(cval!=null)
        document.cookie= name + "="+cval+";expires="+exp.toGMTString();
    }
  },
  components:{
    split
  }
}
</script>
