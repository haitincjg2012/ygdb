<template>
  <div @click="back" class="c-pictureOriginal">
      <div class="c-picture-pos">
        <img :src="src" class="c-pictureOriginal-img">
      </div>
  </div>
</template>
<style>
  @import "../../assets/css/pictureOriginal.css";
</style>
<script>
  export default{
      data(){
          return {
              src:null,//原图的路径
          }
      },
      methods:{
          back(){
              this.src = null;
              this.$router.go(-1);
          }
      },
      activated(){
          var src = this.$route.query.src;
         this.src = src + "?x-oss-process=style/_show";
      },
  }
</script>
