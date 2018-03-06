<template>
  <li class="z-traderlist">
    <div class="z-com-f" @click="xq">

      <div class="primaryMain clearfix" >
        <div class="pic-com" v-html="item.portraitHtml"></div>
        <div class="z-t-info clearfix" >
          <div class="z-t-real-info" v-html="item.orgNameHtml"></div>
          <div class="z-t-info-addr">
            {{item.address}}
          </div>
          <!--<div class="z-t-real-info" :data-id = "item.id">-->
            <!--<div class="z-t-r-orgName" :class="{ztrActive:!item.userRealAuthKey}">-->
              <!--<span class="z-t-r-name">{{item.orgName}}</span>-->
            <!--</div>-->
             <!--<div class="z-t-r-icon">-->
               <!--<img :src="item.userLevelName" class="z-t-level">-->
             <!--</div>-->
             <!--<div class="z-t-r-real" v-if="item.userRealAuthKey">-->
               <!--<span class="z-t-r">实名认证</span>-->
             <!--</div>-->

          <!--</div>-->
          <div class="z-t-main" v-html="item.mainOperatingHtml"></div>
          <!--<div class="z-t-main clearfix" :data-id = "item.id">-->
            <!--<span class="z-t-main-com-t">主营：</span>-->
            <!--<span class="z-t-main-com-t c-t-main-t">{{item.mainOperating}}</span>-->
          <!--</div>-->
          <div class="z-t-result" :class="{zTResultCompensate:item.compensate}"  v-html="item.voucherNumHtml"></div>
          <!--<div class="z-t-address">-->
            <!--<span class="z-t-addr-T">地址：</span>-->
            <!--<span class="z-t-addr">{{item.address}}</span>-->
          <!--</div>-->

        </div>
      </div>
    </div>
  </li>
</template>
<style scoped>
  @import "../../../assets/css/traderlist.css";
</style>
<script>
//  import R from "../../../assets/img/AuthenticationTips.png"
  export default{
    data(){
      return{
//        r:R
      }
    },
    methods:{

        xq(){
          let id= this.item.id;
          let orgId = this.item.orgId;
          let userId = this.item.userId;
          this.$router.push({name:"xqframe",query:{flag:"trader",id:id,orgId:orgId,userId:userId}})
          this.$store.state.traderScroll = 1;
        }
    },
    props:{
        item:""
    }
  }
</script>
