<template>
  <div class="button-tip " :class="{flip:arrive, loading:loadFlag, refresh:refreshFlag}">
    <div class="c-z-pullup" :class="{hideVisable:hideVisableF}">
    </div>
    <div class="c-z-pullup-text">
      <span>{{load}}</span>
    </div>
  </div>
</template>
<style>
@import "../assets/css/loading.css";
</style>
<script>
  export default{
      data(){
          return{
            load:"数据正在加载中...",
            arrive:false,//到底底部箭头切换
            loadFlag:true,//加载数据
            hideVisableF:false,//默认显示的
            refreshFlag:false //刷新时使用
          }
      },

    methods:{
       init(){
           this.load = "数据正在加载中...";
           this.arrive =false,//到底底部箭头切换
           this.loadFlag =false,//箭头切换以后，加载数据
           this.hideVisableF = false;//默认显示的
       },
      loading(param){
           var param = param || "数据正在加载中...";
           this.load = param;
           this.hideVisableF = false;
           this.loadFlag = true;
      },
      end(param){
        var param = param || "数据加载完...";
        this.load = param;
        this.hideVisableF = true;

      },
      refresh(param){
        var param = param || "刷新中...";
        this.load = param;
        this.loadFlag=true;
        this.hideVisableF = false;
        this.refreshFlag=true;
        // console.log(this.loadFlag);
      },
      hide(param){
        var param=param || "";
        this.load = param;
        this.loadFlag=false;
        this.hideVisableF = true;
      }
    },
    activate(){
      //  this.init();
    }
  }
</script>
