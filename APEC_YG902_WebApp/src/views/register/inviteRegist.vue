<template>
  <div class="c-gll-code" ref="code">
     <div class="c-gll-layout">
       <img src="../../assets/img/glltitle.png" class="c-gll-title">
       <img src="../../assets/img/qrcode.jpg" class="c-gll-verificationCode" ref="verificationCode">
       <img src="../../assets/img/gllCode.png" class="c-gll-Text">
     </div>
  </div>
</template>
<style>
   @import "../../assets/css/code.css";
</style>
<script>
  export default{
      mounted(){
          this.$nextTick(function () {
            var el = this.$refs.code;
            var elT = this.$refs.verificationCode;
            el.addEventListener("touchstart",function (e) {
              var target = e.srcElement || e.target;
              if(elT != target){
                  e.preventDefault();
              }

            }, false);
            el.addEventListener("touchmove",function (e) {
              var target = e.srcElement || e.target;
              if(elT != target){
                e.preventDefault();
              }
            }, false);
            el.addEventListener("touchend",function (e) {
              var target = e.srcElement || e.target;
              if(elT != target){
                e.preventDefault();
              }
            }, false);
          })
      }
  }
</script>
