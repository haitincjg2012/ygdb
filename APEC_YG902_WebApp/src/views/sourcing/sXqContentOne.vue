<template>
  <div class="c-sXq-goodslist">
     <div class="c-sXq-one">
       <div class="c-sXq-goods">
         <div class="c-sXq-sku">{{item.skuName}}</div>
         <!--<div class="c-sXq-sku">红富士85#12级</div>-->
         <!--<div class="c-sXq-orignIcon c-ksh"></div>-->
         <div class="c-sXq-orignIcon c-gnh"></div>
       </div>
       <div class="c-sXq-number">
         <!--数量：100桶以下-->
         数量:{{item.number}}
       </div>
     </div>
     <div class="c-sXq-direction">
       <div class="c-sXq-unit">元/{{item.priceUnit}}</div>
       <div class="c-sXq-digital">{{item.amount}}</div>
       <div class="c-sXq-label">参考价</div>

     </div>

  </div>
</template>
<style>
  @import '../../assets/css/sXqContentOne.css';
</style>
<script>
export default{
    props:{
        item:null,
    },
}
</script>
