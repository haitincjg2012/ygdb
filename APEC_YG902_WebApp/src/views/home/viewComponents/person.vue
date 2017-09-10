<template>
   <div class="z-person">
       <div class="z-p-head-t">
          <h1 class="z-p-h-text">{{person.title}}</h1>
           <div class="return" @click="back">
               <img src="../../../assets/img/ret.png">
           </div>
       </div>
       <div class="z-bannerImg">
           <!--<img :src="person.bannerImgUrl">-->
         <img src="../../../assets/img/xqimg1.png">
         <img src="../../../assets/img/tool-1.png" class="z-head-portrait">
       </div>
       <div class="z-p-info">
           <div>
               <span class="z-p-name">{{person.name}}</span>
               <span class="z-p-real">{{person.real}}</span>
               <img :src="person.level" class="z-p-level">
           </div>
           <p class="z-p-x-agency" v-if="agenyFlag">{{person.ageny}}</p>
           <div class="z-p-path">
              <div class="z-p-part">
                 <p class="z-p-number">{{person.notice}}</p>
                 <p>关注我</p>
              </div>
              <div class="z-p-part">
                <p class="z-p-number">{{person.bs}}</p>
                <p>浏览我</p>
              </div>
              <div class="z-p-part">
                <p class="z-p-number">{{person.concat}}</p>
                <p>联系我</p>
              </div>
           </div>
       </div>
       <div class="z-space"></div>
       <div class="z-p-des">
          <div class="z-p-des-area">
            <img class="z-p-des-icon" src="../../../assets/img/pos.png">
            <label>所在区域:</label>
            <span>{{person.address}}</span>
          </div>
          <div class="z-p-des-pz">
             <img class="z-p-des-icon" src="../../../assets/img/pos.png">
             <label>主营品种:</label>
             <span>{{person.pz}}</span>
          </div>
          <div class="z-p-des-market">
             <img class="z-p-des-icon" src="../../../assets/img/pos.png">
             <label>主营市场:</label>
             <span>{{person.market}}</span>
          </div>
          <div class="z-p-des-text">
            <img class="z-p-des-icon" src="../../../assets/img/pos.png">
            <label>实力描述:</label>
            <p class="z-p-des-content">{{person.des}}</p>
          </div>
       </div>
       <div class="z-space"></div>
       <div class="z-transaction">
           <h4>平台战绩:</h4>
           <div class="z-transaction-results">
             <div class="z-r-totalW">
               <p class="z-r-s-com">{{person.weight}}</p>
               <p>累计调货</p>
             </div>
             <div class="z-r-rank">
               <p class="z-r-s-com">{{person.rank}}</p>
               <p>平台排名</p>
             </div>
           </div>
      </div>
       <div class="z-space"></div>
       <div id="main" class="z-main">
       </div>
       <div class="z-p-gy">
          <h4>供求</h4>
       </div>
   </div>
</template>
<style scoped>
  @import "../../../assets/css/person.css";
</style>
<script>
  var ec = require("../../../assets/js/echarts.min");
  var fn = {
      init:function (data) {
        var el = document.getElementById("main");
        var myChart = ec.echarts.init(el);
        var nameData = ["70#一二","75#一二","80#一二","85#一二",];
        var option = {
          color: ['#3398DB'],
          title: {
            text: '调果数据',
          },
          tooltip: {
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
              type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
          },
          legend: {
            data:['数量'],
            selectedMode:false,
          },
          xAxis: {
            data: nameData,
            name:"种类",
//            max:5,
//            splitNumber:10,
            boundaryGap: ['120%', '120%'],
            axisLabel:{
                rotate:45
            },
            axisTick:{
                interval:1,
//                length:10
            }
          },
          yAxis: {
              name:"重量",
            splitLine:{
                  show:false
            }
          },
          series: [{
            name: '数量',
            type: 'bar',
            barWidth:"30%",
            data: [5, 20, 36, 10]
          }]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
      }
  }
  export default{
      data(){
          return{
              person:{
                title:"冷库详情",
                bannerImgUrl:"#",
                des:"11111111111111111111111111",
                level:"#",
                ageny:"烟台栖霞苹果合作社",
                notice:"19",
                bs:"222",
                concat:"55",
                name:"张三丰",
                real:'实名认证',
                market:'山东 湖北 广州',
                weight:"19万斤",
                rank:"19名"
              },
            agenyFlag:false
          }
      },
      methods:{
        back(){
            this.$router.go(-1);
        }
      },
    activated(){
          fn.init();
    }
  }
</script>
