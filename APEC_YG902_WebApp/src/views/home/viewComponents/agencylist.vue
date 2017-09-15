<template>
   <li class="z-agencylist">
     <div class="z-com-f"
          :data-path = "item.path"
          :data-id = "item.id"
          @click="xq">
       <div class="primaryMain clearfix" :data-id = "item.id">
         <div class="pic-com" :data-id = "item.id">
           <img :src ="item.orgFirstBannerUrl" :data-id = "item.id"/>
         </div>
         <div class="z-ag-info clearfix" :data-id = "item.id">
           <div class="z-ag-real-info" :data-id = "item.id">
              <div class="z-ag-r-f-partO">
                <span class="z-ag-r-name">{{item.orgName}}</span>
                <img :src="item.userLevelName" class="z-ag-level">
                <span class="z-ag-r" v-if="item.userRealAuthKey">实名认证</span>

              </div>
               <div class="z-ag-r-f-partT">
                    <img src="../../../assets/img/bs.png" class="z-ag-r-img">
                    <span class="z-ag-r-text">{{item.viewNum}}</span>
               </div>
           </div>
           <div class="z-ag-result">
              <span class="z-ag-res">累计调果:</span>
              <span class="z-ag-r-num">{{item.voucherNum}}</span>
           </div>
           <div class="z-ag-main clearfix" :data-id = "item.id">
             <span class="z-ag-main-com-t">主营:</span>
             <span class="z-ag-main-com-t">{{item.mainOperating}}</span>
           </div>
           <div class="z-ag-address">
             <img class="pos-pic" src="../../../assets/img/pos.png">
             <span class="z-ag-addr">{{item.address}}</span>
           </div>
         </div>
       </div>
     </div>
   </li>
</template>
<style scoped>
  @import "../../../assets/css/agencylist.css";
</style>
<script>
   export default{
       methods:{
           xq(){
               let id = this.item.id;
               let orgId = this.item.orgId;
             let userId = this.item.userId;
                this.$router.push({name:"xqframe",query:{flag:"ag",id:id,orgId:orgId,userId:userId}})
           }
       },
       props:{
           item:null
       }
   }
</script>
