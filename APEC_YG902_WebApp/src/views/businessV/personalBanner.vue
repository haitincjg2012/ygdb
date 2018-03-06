<template>
  <div class="c-bannerImg">
    <img :src="bannerImgUrl" class="c-bannerImg-bg">
    <div class="c-bannerImg-mask">
      <div class="c-bannerImg-picture-mask"></div>
      <div class="c-bannerImg-personPicture">
        <img :src="portrait" class="c-bannerImg-img" @click="picture">
        <p class="c-bannerImg-text">{{name}}</p>
      </div>
    </div>
  </div>
</template>
<style>
@import "../../assets/css/personBanner.css";
</style>
<script>
export default{
    props:['bannerImgUrl','portrait','name','portraitSrc'],
    methods:{
        picture(){
            var self = this;
            if(self.portraitSrc){
              this.$router.push({name:"pictureOriginal",query:{src:self.portraitSrc}});
            }

        }
    }
}
</script>

