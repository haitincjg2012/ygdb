<template>
   <li class="z-agencylist">
     <div class="z-com-f"
          :data-path = "item.path"
          :data-id = "item.id"
          @click="xq">
       <div class="primaryMain clearfix" :data-id = "item.id">
         <div class="pic-com" :data-id = "item.id">
           <img :src ="item.img" :data-id = "item.id"/>
         </div>
         <div class="z-ag-info clearfix" :data-id = "item.id">
           <div class="z-ag-real-info" :data-id = "item.id">
              <div class="z-ag-r-f-partO">
                <span class="z-ag-r-name">张三丰</span>
                <span class="z-ag-r">实名认证</span>
                <img :src="item.imgLevel" class="z-ag-level">
              </div>
               <div class="z-ag-r-f-partT">
                    <img src="#">
                    <span>1200</span>
               </div>
           </div>
           <div class="z-ag-result">
              <span class="z-ag-res">累计调果:</span>
              <span class="z-ag-r-num">283000斤</span>
           </div>
           <div class="z-ag-main clearfix" :data-id = "item.id">
             <span class="z-ag-main-com-t">主营:</span>
             <span class="z-ag-main-com-t">红富士 70# 80#</span>
           </div>
           <div class="z-ag-address">
             <img class="pos-pic" src="../../../assets/img/pos.png">
             <span class="z-ag-addr">烟台栖霞桃村镇</span>
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
                this.$router.push({name:"xqframe",query:{name:"ag"}})
           }
       },
       props:{
           item:null
       }
   }
</script>
