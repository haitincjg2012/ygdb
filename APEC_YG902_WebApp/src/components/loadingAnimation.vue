<template>
  <div>
    <div class="button-tip-A" :class="{flip:arrive, loading:loadFlag}" v-if="emptyFlag">
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
        emptyFlag:true,//默认是显示
      }
    },
    methods:{
      init(){
        this.arrive = false,//到底底部箭头切换
        this.loadFlag = false,//箭头切换以后，加载数据
        this.showVisableF = true,//默认隐藏的
        this.load = "";//数据加载完...
      },
      move(){
        this.arrive = false;//开始动画效果
        this.loadFlag = true;//箭头切换以后，加载数据
        this.showVisableF = false;
        this.load = "松手开始更新...";//松手开始更新...
      },
      start(flag){
        this.arrive = true;//开始动画效果
        this.loadFlag = true;//箭头切换以后，加载数据
        this.showVisableF = false;
        this.load = "数据正在加载中...";//数据在加载中

      },
      end(flag){
        this.showVisableF = true;
        this.load ="没有更多数据...";//没有更多数据
      }
    }
  }
</script>
