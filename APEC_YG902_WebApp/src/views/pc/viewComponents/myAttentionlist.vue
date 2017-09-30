<template>
   <li class="gsy-agencylist">
     <div class="z-com-f"
          :data-path = "item.path"
          :data-id = "item.id"
          @click="xq">
       <div class="primaryMain clearfix" :data-id = "item.id">
         <div class="pic-com" :data-id = "item.id">
           <img :src ="item.orgFirstBannerUrl" :data-id = "item.id" />
           <!--<img src="../../../assets/img/Agency.png"/>-->
         </div>
         <div class="z-ag-info clearfix" :data-id = "item.id">
           <div class="z-ag-real-info" :data-id = "item.id">
              <div class="z-ag-r-f-partO">
                <span class="z-ag-r-name">{{item.orgName}}</span>
                <!--<img :src="item.imgLevel" class="z-ag-level">-->
                <!--<img :src="item.userLevelName" class="z-ag-level">-->
                <span class="z-ag-r" v-if="item.userRealAuthKey">实名认证</span>
              </div>
               <div class="z-ag-r-f-partT">
                    <img src="../../../assets/img/fireIcon.png" class="z-ag-r-img">
                    <span class="z-ag-r-text">{{item.viewNum}}</span>
               </div>
           </div>
           <div class="z-ag-result">
              <span class="z-ag-res">累计调果：</span>
              <span class="z-ag-r-num">{{item.voucherNum}}</span>
              <span class="z-ag-res">斤</span>
           </div>
           <div class="z-ag-main clearfix" :data-id = "item.id">
             <span class="z-ag-main-com-t">主营：</span>
             <span class="z-ag-main-com-t">{{item.mainOperating}}</span>
           </div>
           <div class="z-ag-address">
             <img class="pos-pic" src="../../../assets/img/placeIcon.png">
             <span class="z-ag-addr">{{item.address}}</span>
           </div>
         </div>
       </div>
     </div>
   </li>
</template>
<style scoped>
  @import "../../../assets/css/myAttentionlist.css";
</style>
<script>
  export default{
    methods:{
      xq(){
        let id = this.item.id;
        let orgId = this.item.orgId;//组织的id
        let userId = this.item.userId;//用户id

        this.$router.push({name:"xqframe",query:{flag:"ag",id:id,orgId:orgId,userId:userId}})
      }
    },
    props:{
      item:null
    }
  }
</script>
