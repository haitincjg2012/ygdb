<template>
  <div class="c-cue" v-show="cueState">
    <div class="c-cue-shadow" ref="curWrapper"></div>
    <div class="c-cue-content">
        <div class="c-cue-top">
          <img src="../../assets/img/cue.png" class="c-cue-top-icon">
        </div>
        <div class="c-cue-bottom">
          <h6 class="c-cue-subtitle">完善个人资料</h6>
          <p class="c-cue-subtitle-content">为帮助您更好的匹配生意机会</p>
          <p class="c-cue-subtitle-content">请完善个人资料</p>
          <div class="c-cue-btn" @click="personInfo">去填写</div>
        </div>
    </div>
    <div class="c-cue-close" @click="close"></div>
  </div>
</template>
<style>
  @import "../../assets/css/cue.css";
</style>
<script>
  export default{
      data(){
          return{
              cueState:false,
          }
      },
      props:['cueFlag'],
      watch:{
        "cueFlag":function (curValue) {
           if(!curValue){
             this.cueState = true;
           }
        }
      },
      mounted(){
        var el = this.$refs.curWrapper;
        el.addEventListener("touchstart",function (e) {
          e.preventDefault();
        });
        el.addEventListener("touchmove",function (e) {
          e.preventDefault();
        });
        el.addEventListener("touchend",function (e) {
          e.preventDefault();
        });
      },
      methods:{
        personInfo(){
          this.$router.push({name: 'pcInfo'});
        },
        close(){
          this.cueState = false;
          this.$emit('switch', true)
        }
      },
      activated(){
          var self = this;
          setTimeout(function () {
            self.close();
          }, 2000)
      }
  }
</script>
