/**
* Created by gsy on 2017/6/26.
* header
<!-- <header :headTitle="title"></header>-->
*/
<template>
  <div class="header">
    <h1>
      <i class="return" @click="goBack" v-if="iconFlag">
        <img src="../../assets/img/arrowLeft.png">
      </i>
      {{headTitle}}</h1>
  </div>
</template>

<script>
  export default{
        data(){
            return {
              iconFlag:true
            }
        },
        props:{
          headTitle:{
            type:String
          }

    },
        methods: {
          hideFlag(){
//            console.log("调用此法");
            this.iconFlag=false;
          },
          //返回
          goBack(){
              this.$router.go(-1);
          }
        },
        components: {}
    }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  @import "header.css";
</style>
