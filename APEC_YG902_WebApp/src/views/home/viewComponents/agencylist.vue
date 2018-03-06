<template>
   <li class="z-agencylist">
     <div class="z-com-f" @click="xq">
       <div class="primaryMain clearfix">
         <div class="pic-com" v-html="item.portraitHtml"></div>
         <div class="z-ag-info clearfix">
           <div class="z-ag-real-info" v-html="item.orgNameHtml"></div>
           <div class="z-ag-tagNames" v-html="item.orgTagsHtml">
             <!--<span class="z-ag-tagName-sp">调果代办</span><span class="z-ag-tagName-sp">调果代办</span><span class="z-ag-tagName-sp">调果代办</span><span class="z-ag-tagName-sp">调果代办代办代办</span>-->
             <!--<span class="z-ag-tagName-sp">调果代办</span>-->
           </div>
           <div v-html="item.orgAddressHtml">
             <!--<div class="z-ag-info-addr" >-->
               <!--{{item.address}}-->
             <!--</div>-->
           </div>
           <div class="z-ag-main" v-html="item.mainOperatingHtml"></div>
           <div class="z-ag-result" :class="{zAgResultCompensate:item.compensate}"  v-html="item.voucherNumHtml"></div>

           <!--<div class="z-ag-address">-->
             <!--<img class="pos-pic" src="../../../assets/img/pos.png">-->
             <!--<span class="z-ag-addr">{{item.address}}</span>-->
           <!--</div>-->
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
                this.$router.push({name:"xqframe",query:{flag:"ag",id:id,orgId:orgId,userId:userId}});

              this.$store.state.agencyScroll = 1;
           }
       },
       props:{
           item:null
       }
   }
</script>
