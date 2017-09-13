<template>
  <li class="z-traderlist">
    <div class="z-com-f"
         :data-path = "item.path"
         :data-id = "item.id"
         @click="xq">
      <div class="primaryMain clearfix" :data-id = "item.id">
        <div class="pic-com" :data-id = "item.id">
          <img :src ="item.img" :data-id = "item.id"/>
        </div>
        <div class="z-t-info clearfix" :data-id = "item.id">
          <div class="z-t-real-info" :data-id = "item.id">
             <span class="z-t-r-name">张三丰</span>
             <!--<img src="../../../assets/img/rateOne.png" class="z-t-level">-->
            <img :src="r" class="z-t-level">
             <span class="z-t-r">实名认证</span>
          </div>
          <div class="z-t-main clearfix" :data-id = "item.id">
            <span class="z-t-main-com-t">主营：</span>
            <span class="z-t-main-com-t">红富士 70# 80#</span>
          </div>
          <div class="z-t-address">
            <span class="z-t-addr-T">调果区域：</span>
            <span class="z-t-addr">烟台栖霞</span>
          </div>

        </div>
      </div>
    </div>
  </li>
</template>
<style scoped>
  @import "../../../assets/css/traderlist.css";
</style>
<script>
  import R from "../../../assets/img/AuthenticationTips.png"
  export default{
    data(){
      return{
        r:R
      }
    },
    methods:{

        xq(){
          this.$router.push({name:"xqframe",query:{id:"trader"}})
        }
    },
    props:{
        item:""
    }
  }
</script>
