<template>
   <div>
     <div class="c-characteristic-first c-tab-com" :class="{'c-active':areaFlag}" @click="launch('area')" >区域</div>
     <div class="c-characteristic-first c-pz c-tab-com" :class="{'c-active':pzFlag}" @click="launch('pz')">品种</div>
     <div class="c-characteristic-first c-specifications c-tab-com" :class="{'c-active':specifFlag}" @click="launch('specifications')">规格</div>
     <div class="c-characteristic-second c-ranking c-tab-com" :class="{'c-ranking-active':rankFlag}" @click="launch('ranking')">排序</div>
   </div>
</template>
<style>
  @import "../../assets/css/fTab.css";
</style>
<script>
  export default{
      data(){
        return{
          areaFlag:false,//区域
          pzFlag:false,//品种
          specifFlag:false,//规格
          rankFlag:false,//排序
        }
      },
      methods:{
          reset(){
              //默认为初始化
              this.areaFlag = false;
              this.pzFlag = false;
              this.specifFlag = false;
              this.rankFlag = false;
          },
        launch(item){
            this.reset();
            switch (item){
              case "area":
                this.areaFlag = true;
                  break;
              case "pz":
                this.pzFlag = true;
                  break;
              case "specifications":
                this.specifFlag = true;
                  break;
              case "ranking":
                this.rankFlag = true;
                  break;
            }

          this.$emit("receive", item);
        }
      }
  }
</script>
