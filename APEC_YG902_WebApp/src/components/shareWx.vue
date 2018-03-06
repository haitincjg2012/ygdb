<template>
  <div class="c-shareWx" v-show="Wxflag" ref="shareWrapper"></div>
</template>
<style>
  @import "../assets/css/shareWx.css";
</style>
<script>
  export default{
      data(){
          return {
            Wxflag:this.wxflag,
          }
      },
      props:['wxflag'],
      watch:{
          "wxflag":function (newValue, oldValue) {
              if(newValue){
                this.Wxflag = true;
              }else{
                this.Wxflag = false;
              }
          }
      },
      mounted(){
          var that = this;
          this.$nextTick(function () {
            var el = that.$refs.shareWrapper;
            el.addEventListener("touchstart",function (e) {
              that.Wxflag = false;
              that.$emit('getWx');
              e.preventDefault();
            },false);
            el.addEventListener("touchmove",function (e) {
              e.preventDefault();
            }, false);
            el.addEventListener("touchend",function (e) {
              e.preventDefault();
            }, false);
          })
      },
      activated(){

      }
  }
</script>
