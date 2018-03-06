<template>
  <div>
    <div class="button-tip" :class="{flip:arrive, loading:loadFlag}" v-if="emptyFlag">
      <div class="c-z-pullup" :class="{showVisable:showVisableF}">
      </div>
      <div class="c-z-pullup-text">
        <span>{{load}}</span>
      </div>
    </div>
  </div>
</template>
<style>
  @import "../assets/css/scrollStyle.css";
</style>
<script>
  export default{
      data(){
          return{
            arrive:false,//到底底部箭头切换
            loadFlag:false,//箭头切换以后，加载数据
            showVisableF:true,//默认隐藏的
            load:"数据加载完...",//数据加载完...
            emptyFlag:true,//默认是显示
          }
      },
      methods:{
          init(flag){
              if(flag){
                 this.emptyFlag = false;
              }else{
                  this.load = "没有更多数据";
              }
          },
        start(flag){

            this.emptyFlag = true;
            this.arrive = true;//开始动画效果
            this.loadFlag = true;//箭头切换以后，加载数据
            this.showVisableF = false;
            this.load ="数据在加载中...";//数据在加载中

        },
        end(flag){
          this.emptyFlag = true;
          this.showVisableF = true;
          this.load ="没有更多数据...";//数据在加载中
        }
      }
  }
</script>
