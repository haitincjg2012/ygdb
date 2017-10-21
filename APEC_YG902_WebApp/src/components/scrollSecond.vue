<template>
  <div class="button-tip" :class="{flip:arrive, loading:loadFlag}" v-if="emptyFlag">
    <div class="c-z-pullup" :class="{showVisable:showVisableF}">
    </div>
    <div class="c-z-pullup-text">
      <span>{{load}}</span>
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
            load:"数据在加载中...",//数据在加载中
            emptyFlag:true,//默认是显示
          }
      },
      methods:{
          init(flag){
              if(flag){
                 this.emptyFlag = false;
              }else{
                this.emptyFlag = true;
                this.load ="数据在加载完...";//数据在加载中
              }
          },
        start(flag){
            this.arrive = true;//开始动画效果
            this.loadFlag = true;//箭头切换以后，加载数据
            this.showVisableF = true;//默认显示的
            this.load ="数据在加载中...";//数据在加载中

        },
        end(flag){
          this.showVisableF = true;//默认显示的
          this.load ="数据在加载完...";//数据在加载中
        }
      }
  }
</script>
