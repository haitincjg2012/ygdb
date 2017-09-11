<template>
  <li class="z-coldlist">
    <div class="z-com-f"
         :data-path = "item.path"
         :data-id = "item.id"
         @click="xq">
      <div class="primaryMain clearfix" :data-id = "item.id">
        <div class="pic-com" :data-id = "item.id">
          <img :src ="item.img" :data-id = "item.id"/>
        </div>
        <div class="z-c-info clearfix" :data-id = "item.id">
          <div class="z-c-real-info" :data-id = "item.id">
            <div class="z-ag-r-f-partO">
              <span class="z-c-r-name">栖霞庆丰冷库</span>
            </div>
            <!--<img src="#" class="z-c-level">-->
            <div class="z-ag-r-f-partT">
              <img src="../../../assets/img/fireIcon.png" class="z-ag-r-img">
              <span class="z-ag-r-text">1200</span>
            </div>
          </div>
          <div class="z-c-storage">
             <span class="z-c-storage-text">库容 5000 吨</span>
          </div>
           <div class="z-c-address clearfix" :data-id = "item.id">
              <img class="pos-pic" src="../../../assets/img/placeIcon.png">
              <span class="z-c-add">烟台栖霞桃村镇</span>
           </div>
          <div class="z-c-real">
            <span class="z-c-r">企业认证</span>
            <span class="z-c-r-e">供应链金融合作库</span>
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
          this.$router.push({name:"xqframe",query:{name:"cold"}})
        }
    },
    props:{
        item:null
    }
  }
</script>
