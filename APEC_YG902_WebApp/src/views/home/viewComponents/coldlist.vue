<template>
  <li class="z-coldlist">
    <div class="z-com-f" @click="xq">
      <div class="primaryMain clearfix">
        <div class="pic-com" v-html="item.portraitHtml"></div>
        <div class="z-c-info clearfix">
          <div class="z-c-real-info" v-html="item.orgNameHtml"></div>
          <div class="z-c-info-addr">
            {{item.address}}
          </div>
          <div class="c-cold-auth" v-html="item.authHtml"></div>
          <!--<div class="z-c-real">-->
          <!--<span class="z-c-r" v-if="item.showOrgTagsInfo.QYRZ"> 企业认证</span>-->
          <!--<span class="z-c-r-e" v-if="item.showOrgTagsInfo.QYJRL">供应链金融合作库</span>-->
          <!--</div>

          <!--<div class="z-c-real-info">-->

            <!--&lt;!&ndash;<div class="c-r-info-title">&ndash;&gt;-->
              <!--&lt;!&ndash;<span class="z-c-r-name">{{item.orgName}}</span>&ndash;&gt;-->
            <!--&lt;!&ndash;</div>&ndash;&gt;-->
            <!--&lt;!&ndash;<div class="c-r-info-bs">&ndash;&gt;-->
                 <!--&lt;!&ndash;<img src="../../../assets/img/bs.png">&ndash;&gt;-->
                 <!--&lt;!&ndash;<span> {{item.viewNum}}</span>&ndash;&gt;-->
            <!--&lt;!&ndash;</div>&ndash;&gt;-->
          <!--</div>-->
          <div class="z-c-storage" v-html="item.orgStockCapHtml"></div>
            <!--<span class="z-c-storage-text">库容 {{item.orgStockCap}}</span>-->



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
        this.$store.state.coldScroll = 1;
      }
    },
    props:{
      item:null
    }
  }
</script>

