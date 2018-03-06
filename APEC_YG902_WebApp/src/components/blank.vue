<template>
<div class="c-blank" v-if="isShow">
  <slot></slot>
  <img src="../assets/img/noData.png"/>
  <p>暂无数据</p>
</div>
</template>
<style>
@import "../assets/css/blank.css";
</style>
<script>
 export default{
     data(){
         return{

         }
     },
     props:[
         'isShow'
     ]
 }
</script>
