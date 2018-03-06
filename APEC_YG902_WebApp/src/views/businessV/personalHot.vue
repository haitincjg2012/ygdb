<!--
c-personHot:这一个块的样式
c-pH：c-personHot简写
sy：style的简写
-->
<template>
    <div class="c-personHot">
      <div class="c-pH-com">
        <p class="c-pH-number">{{personHot.notice}}</p>
        <p><span class="z-p-style">关注</span></p>
      </div>
      <div class="c-pH-com">
        <p class="c-pH-number">{{personHot.bs}}</p>
        <p><span class="z-p-style">浏览</span></p>
      </div>
      <div class="c-pH-com">
        <p class="c-pH-number">{{personHot.concat}}</p>
        <p><span class="c-pH-textsy">联系</span></p>
      </div>
    </div>
</template>
<style>
@import "../../assets/css/personHot.css";
</style>
<script>
  export default{
      data(){
          return{

          }
      },
      methods:{},
      props:['personHot']
  }
</script>
