/**
* Created by gsy on 2017/6/26.
* header
<!-- <header :headTitle="title"></header>-->
*/
<template>
  <div class="myHeader">
    <div class="header">
      <i class="return" @click="goBack" v-if="iconFlag">
        <img src="../../assets/img/arrowLeft.png">
      </i>
      <h1>{{headTitle}}</h1>
    </div>
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
              var vm=this;
              vm.$router.go(-1);
              vm.$emit("initPage");
          }
        },
        components: {}
    }
</script>

<style scoped>
  @import "header.css";
</style>
