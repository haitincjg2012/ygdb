<template>
  <div class="c-onePiece-content">
      <h3 class="c-subtitle">{{warehouse}}</h3>
      <!--<h3 class="c-subtitle">栖霞墉胜冷库</h3>-->
      <p class="c-author c-text-com">联系人:{{author}}</p>
      <!--<p class="c-author c-text-com">联系人:刘小源</p>-->
      <p class="c-address c-text-com">地址:{{address}}</p>
      <!--<p class="c-address c-text-com">地址：烟台市蓬莱市刘家沟镇刘家沟村</p>-->
  </div>
</template>
<style>
  @import "../../assets/css/sXqH.css";
</style>
<script>
  export default{
     props:['warehouse','author','address']
  }
</script>
