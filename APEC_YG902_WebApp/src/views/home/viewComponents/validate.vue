<template>
    <div>
      <div class="z-s-header">
        <div class="return" @click="back">
          <img src="../../../assets/img/ret.png">
        </div>
      </div>
        <div class="z-authentication">
             <p class="z-p-text">为了顺利发布信息,还需完善资料</p>
             <div class="z-pic-t">
               <img src="../../../assets/img/Personal.png" v-if="perfect" alt="完善资料">
               <img src="../../../assets/img/RealName.png" v-if="authentication" alt="实名认证">
             </div>
             <p class="z-p-text">{{msg?'完善资料':'实名认证'}}</p>
        </div>
      <div class="z-ok" @click="jump">
          {{msg?'请完善资料个人资料':'实名认证'}}
      </div>
    </div>
</template>
<style scoped>
    @import "../../../assets/css/auth.css";
</style>
<script>
  import topBar from '../../../components/topBar/topBar'

    export default{
        data(){
          return{
            perfect:false,
            authentication:false,
            msg:false
          }
        },
      methods:{
            jump(){
              var id = this.$route.query.id;
              var check = +this.$store.state.check;
              this.$store.state.check = '0';
              if(check == 1 || id == 1){
                this.$router.push({  name:'pcInfo'});
              }else if(check == 2 || id == 2){
                this.$router.push({name:'realNameAur'})
              }

            },
        back(){
          //const router =  window.sessionStorage.getItem('routerEnter');
          //this.$router.push({name:router || 'home'});
          this.$router.go(-1);
        }
      },
      activated(){
          var check = +this.$store.state.check;
          var id = this.$route.query.id;
          if(check == 1 || id == 1){
              this.perfect = true;
              this.msg = true;
              this.authentication = false;
          }else{
            this.perfect = false;
            this.msg = false;
            this.authentication = true;
          }
      },
//      beforeRouteEnter(to, from, next) {
//        next(vm=>{
//            var sesStor = window.sessionStorage;
//            sesStor.setItem('routerEnter',from.name);
//          }
//        )
//      },
      components: {
        topBar,
      }
    }
</script>
