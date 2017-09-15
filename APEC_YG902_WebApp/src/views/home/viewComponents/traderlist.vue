<template>
  <li class="z-traderlist">
    <div class="z-com-f"
         :data-path = "item.path"
         :data-id = "item.id"
         @click="xq">

      <div class="primaryMain clearfix" :data-id = "item.id">
        <div class="pic-com" :data-id = "item.id">
          <img :src ="item.orgFirstBannerUrl" :data-id = "item.id"/>
        </div>
        <div class="z-t-info clearfix" :data-id = "item.id">
          <div class="z-t-real-info" :data-id = "item.id">
             <span class="z-t-r-name">{{item.orgName}}</span>
             <!--<img src="../../../assets/img/rateOne.png" class="z-t-level">-->
            <img :src="item.userLevelName" class="z-t-level">
             <span class="z-t-r" v-if="item.userRealAuthKey">实名认证</span>
          </div>
          <div class="z-t-main clearfix" :data-id = "item.id">
            <span class="z-t-main-com-t">主营：</span>
            <span class="z-t-main-com-t">{{item.mainOperating}}</span>
          </div>
          <div class="z-t-address">
            <span class="z-t-addr-T">地址：</span>
            <span class="z-t-addr">{{item.address}}</span>
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
        }
    },
    props:{
        item:""
    }
  }
</script>
