<!--
头部组件
-->
<template>
  <div class="c-znw-header">
     <div class="c-znw-header-ret" @click.stop.prevent="back">
        <img src="../../assets/img/whiteBack.png" v-if="icon== 1">
        <img src="../../assets/img/ret.png" v-else>
     </div>
     <slot name="pack"></slot>
  </div>
</template>
<style>
@import "../../assets/css/header.css";
</style>
<script>
  export default{
     methods:{
          back(){
              this.$router.go(-1);
              this.$emit("receive");
          }
     },
    props:["icon"]
  }
</script>
