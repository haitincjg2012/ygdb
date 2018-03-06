<template>
  <div class="mask" v-if="pageflag" ref="stopMove">
      <div class="transparentLayer"></div>
     <slot></slot>
  </div>
</template>
<style>
@import "../assets/css/mask.css";
</style>
<script>
  export default{
      data(){
          return{
              mask:null,
          }
      },
      props:['pageflag'],
      mounted(){
        var el = this.$refs.stopMove;
        el.addEventListener("touchmove",function (e) {
             e.preventDefault();
          }, false)
      },
      created(){

      }

  }
</script>
