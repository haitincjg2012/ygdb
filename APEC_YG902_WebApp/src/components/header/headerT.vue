/**
*Created by gsy,zy on 2017/11/29.
* <!--
头部组件需要扩展
-->
* 参数说明：
*title                {string}            头部标题
* backflag             {Boolean}           default:true 执行返回上一级函数；false不执行
* initpage             {Function}          设置最大高度(可选)
*/
<!-- <header :headTitle="title" v-on:initPage="initpage" :backFlag="backflag"></header>-->
*/


<template>
  <div class="myHeader">
    <div class="header">
      <i class="return" @click="goBack" v-if="iconFlag" >
        <img src="../../assets/img/headArrow.png">
        <!--<img src="../../assets/img/arrowLeft.png">-->
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
      },
      backFlag:{
        type:Boolean,
        default:true
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
        vm.$emit("initPage");
      }
    },
    components: {}
  }
</script>

<style scoped>
  @import "header.css";
</style>
