<template>
  <div>
    <div class="button-tip-A" :class="{flip:arrive, loading:loadFlag}" v-show="emptyFlag">
      <div class="c-z-pullup-A" :class="{showVisableA:showVisableF}">
        <div class="c-pullupIcon">
          <span class="c-animation"></span>
        </div>
      </div>
      <div class="c-z-pullup-text">
        <span>{{load}}</span>
      </div>
    </div>
  </div>
</template>
<style>
  @import "../assets/css/loadingA.css";
</style>
<script>
  export default{
    data(){
      return{
        arrive:false,//到底底部箭头切换
        loadFlag:false,//箭头切换以后，加载数据
        showVisableF:true,//默认隐藏的
        load:"",//数据加载完...
        emptyFlag:false,//默认是隐藏
      }
    },
    methods:{
      init(){
        this.loadFlag = true,//箭头切换以后，加载数据
        this.showVisableF = false,//默认隐藏的
        this.emptyFlag = true;
        this.load = "正在刷新中...";//数据加载完...
      },
      end(flag){
        this.emptyFlag = false;
      }
    }
  }
</script>
