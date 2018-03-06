<template>
  <div class="c-znw-goods" @click="sxq(item)">
    <!--gmc 来源于中农网另外的app数据-->
    <div class="c-znw-goods-com c-goods-title" v-html="item.skuHtml">

    </div>
    <div class="c-znw-goods-com" >
      <div class="c-gmc-goods-weight" v-html="item.weightHtml">
      </div>
      <div class="c-gmc-goods-price">{{item.priceText}}</div>
    </div>
    <div class="c-goods-xq">
      <div class="c-znw-goods-com c-goods-xq-com">
        <div class="c-goods-i-icon c-goods-name-icon"></div>
        <div class="c-goods-xq-text">{{item.coldName}}</div>
      </div>
      <div class="c-znw-goods-com c-goods-xq-com">
        <div class="c-goods-i-icon c-goods-address-icon"></div>
        <div class="c-goods-xq-text">{{item.address + item.addressDetail}}</div>
        <!--<div class="c-goods-xq-text">烟台市蓬莱市刘家沟镇刘家沟村</div>-->
      </div>
    </div>
  </div>
</template>
<style>
   @import "../../assets/css/sourceContent.css";
</style>
<script>
  export default{
      props:['item'],
      methods:{
        sxq(item){
          //调整到详情
          var id = this.item.id;
          var orgId = this.item.userOrgId;

          this.$router.push({name:"sourcingXq", query:{id:id,orgId:orgId}})
        }
      }
  }
</script>
