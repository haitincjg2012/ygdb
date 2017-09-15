<template>
  <li class="z-coldlist">
    <div class="z-com-f"
         :data-path = "item.path"
         :data-id = "item.id"
         @click="xq">
      <div class="primaryMain clearfix" :data-id = "item.id">
        <div class="pic-com" :data-id = "item.id">
          <img :src ="item.orgFirstBannerUrl" :data-id = "item.id"/>
        </div>
        <div class="z-c-info clearfix" :data-id = "item.id">
          <div class="z-c-real-info" :data-id = "item.id">
            <div class="c-r-info-title">
              <span class="z-c-r-name">{{item.orgName}}</span>
            </div>
            <div class="c-r-info-bs">
                 <img src="../../../assets/img/bs.png">
                 <span> {{item.viewNum}}</span>
            </div>
          </div>
          <div class="z-c-storage">
            <span class="z-c-storage-text">库容 {{item.orgStockCap}}</span>
          </div>
          <div class="z-c-address clearfix" :data-id = "item.id">
            <img class="pos-pic" src="../../../assets/img/pos.png">
            <span class="z-c-add">{{item.address}}</span>
          </div>
          <div class="z-c-real">
            <span class="z-c-r" v-if="item.showOrgTagsInfo.QYRZ"> 企业认证</span>
            <span class="z-c-r-e" v-if="item.showOrgTagsInfo.QYJRL">供应链金融合作库</span>
          </div>

        </div>
      </div>
    </div>
  </li>
</template>
<style scoped>
  @import "../../../assets/css/coldlist.css";
</style>
<script>
  export default{
    methods:{
      xq(){
          let id = this.item.id;
        let orgId = this.item.orgId;
        let userId = this.item.userId;
        this.$router.push({name:"xqframe",query:{flag:"cold",id:id,orgId:orgId,userId:userId}})
      }
    },
    props:{
      item:null
    }
  }
</script>

