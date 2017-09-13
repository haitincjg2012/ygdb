<template>
  <div style="background-color: #fff">
    <div class="z-person">
      <div class="z-p-head-t">
        <!--<h1 class="z-p-h-text">{{person.title}}</h1>-->
        <div class="return" @click="back">
          <img src="../../../assets/img/ret.png">
        </div>
      </div>
      <div class="z-bannerImg">
        <img src="../../../assets/img/xqimg1.png">
        <img src="../../../assets/img/tool-1.png" class="z-head-portrait" v-if="agenyFlag || traderFlag">
      </div>
      <div class="z-p-info">
        <div class="c-t-info">
          <span class="z-p-name" v-if="agenyFlag || traderFlag">{{person.name}}</span>
          <span class="z-cold-name" v-if="coldFlag">{{person.coldName}}</span>
          <img :src="person.level" class="z-p-level" v-if="agenyFlag || traderFlag">
          <span class="z-p-represent" v-if="agenyFlag || traderFlag">{{person.representative}}</span>
          <span class="z-p-real" v-if="agenyFlag || traderFlag">{{person.real}}</span>
          <span class="z-p-real c-p-c-real" v-if="coldFlag">{{person.real}}</span>
          <span class="c-p-tx" v-if="coldFlag">{{person.coldS}}</span>
        </div>
        <p class="z-p-x-agency" v-if="agenyFlag">{{person.ageny}}</p>
        <div class="z-p-path">
          <div class="z-p-part">
            <p class="z-p-number">{{person.notice}}</p>
            <p>关注</p>
          </div>
          <div class="z-p-part">
            <p class="z-p-number">{{person.bs}}</p>
            <p>浏览</p>
          </div>
          <div class="z-p-part">
            <p class="z-p-number">{{person.concat}}</p>
            <p>联系</p>
          </div>
          <div class="z-p-follow">
            <div class="z-p-f" :class="{active:person.sh}" @click="notice">
              {{person.sh?"已关注":"关注"}}
            </div>
          </div>
        </div>
      </div>
      <div class="z-space"></div>
      <div class="z-p-des">
        <div class="z-p-des-area z-p-des-w" v-if="coldFlag">
          <img class="z-p-des-icon" src="../../../assets/img/kr.png">
          <span class="z-x-sp-com">库 容 量&nbsp;:</span>
          <input type="text" readonly v-model="person.coldStroage">
        </div>
        <div class="z-p-des-area z-p-des-w" >
          <img class="z-p-des-icon" src="../../../assets/img/qy.png">
          <span class="z-x-sp-com">所在区域:</span>
          <input type="text" readonly v-model="person.address">
        </div>
        <div class="z-p-des-pz z-p-des-w" v-if="agenyFlag || coldFlag || traderFlag">
          <img class="z-p-des-icon" src="../../../assets/img/zypz.png">
          <span class="z-x-sp-com">主营品种:</span>
          <input type="text" readonly v-model="person.pz">
          <!--<span>{{person.pz}}</span>-->
        </div>
        <div class="z-p-des-pz z-p-des-w" v-if="agenyFlag">
          <img class="z-p-des-icon" src="../../../assets/img/kh.png">
          <span class="z-x-sp-com">客户市场:</span>
          <input type="text" readonly v-model="person.market">
          <!--<span>{{person.pz}}</span>-->
        </div>
        <div class="z-p-des-market z-p-des-w" v-if="traderFlag">
          <img class="z-p-des-icon" src="../../../assets/img/dhqy.png">
          <span class="z-x-sp-com">调货区域</span>
          <input type="text" readonly v-model="person.address">
          <!--<span>{{person.market}}</span>-->
        </div>
        <div class="z-p-des-market z-p-des-w" v-if="traderFlag">
          <img class="z-p-des-icon" src="../../../assets/img/xsqy.png">
          <span class="z-x-sp-com">销售区域:</span>
          <input type="text" readonly v-model="person.saleArea">
          <!--<span>{{person.market}}</span>-->
        </div>
        <div class="z-p-des-text">
           <div class="z-p-des-w c-border-none">
             <img class="z-p-des-icon" src="../../../assets/img/sl.png">
             <span class="z-x-sp-com">实力描述:</span>
           </div>

          <p class="z-p-des-content">{{person.des}}</p>
        </div>
      </div>
      <!--<div class="z-space"></div>-->
    </div>
    <div class="z-transaction" v-if="status.ag || status.trader">
      <h4 class="z-h4-title">平台战绩</h4>
      <div class="z-space"></div>
      <div class="z-transaction-results">
        <div class="z-r-totalW">
          <p class="z-r-s-com">{{person.weight}}</p>
          <p>累计调货</p>
        </div>
        <div class="z-vertical-line">
          <div class="z-v-line">
          </div>
        </div>
        <div class="z-r-rank">
          <p class="z-r-s-com">{{person.rank}}</p>
          <p>平台排名</p>
        </div>
      </div>
    </div>
    <div class="z-num-main" v-if="status.ag">
      <h4 class="z-num-main-t">调果数据</h4>
      <div class="z-space"></div>
      <div id="main" :class="{Zmain:status.ag}" v-if="status.ag"></div>
    </div>
    <div class="z-phone">
      <a @click.stop="phoneShow" v-if="coldFlag">打电话</a>
      <a @click.stop="phoneClick" :href="`tel:${mobile}`" v-if="agenyFlag || traderFlag">打电话</a>
      <div class="z-cold-mobile" :class="{Cshow:coldShow}" v-if="coldFlag">
          <p class="c-text">请选择联系人</p>
        <div class="z-space-com"></div>
          <a :href="`tel:${mobileO}`" class="c-com">
             <img src="../../../assets/img/p.png" class="c-img-com"/>
             <span>老板</span>
             <img src="../../../assets/img/mobile.png" class="c-m-icon-com c-icon-t">
          </a>
        <div class="z-space-com"></div>
          <a :href="`tel:${mobileT}`"  class="c-com">
          <img src="../../../assets/img/p.png" class="c-img-com"/>
          <span>保管员</span>
            <img src="../../../assets/img/mobile.png" class="c-m-icon-com">
          </a>
        <div class="z-space-com"></div>
          <div @click="cancel" class="c-cancel">取消</div>
      </div>
    </div>
    <div class="z-p-gy">
      <h4>供求</h4>
    </div>
  </div>
</template>
<style scoped>
  @import "../../../assets/css/xqframe.css";
</style>
<script>
  import {Swipe, SwipeItem, Indicator,MessageBox, Toast} from 'mint-ui'
//  import market from "../../../assets/img/"
  import T from "../../../assets/img/t.png"
  var ec = require("../../../assets/js/echarts.min");
  var fn = {
    init:function (data) {
      var el = document.getElementById("main");
      var myChart = ec.echarts.init(el);
      var nameData = ["70#一二","75#一二","80#一二","85#一二","95#一二","89#一二"];
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

          axisTick:{
            interval:0,
//                length:10
          },
          axisLabel:{
            interval:0 ,
            rotate:45
//            formatter:function(val){
//              return val.split("").join("\n");
//            }
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
          data: [5, 20, 36, 20, 36, 10]
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
            level:T,
            ageny:"烟台栖霞苹果合作社",
            notice:"19",
            bs:"222",
            concat:"55",
            name:"张三丰",
            real:'实名认证',
            market:'山东 湖北 广州',
            weight:"19万斤",
            rank:"19名",
            representative:"代办",
            coldName:"烟台栖霞苹果合作社",
            coldStroage:"5000吨",
            saleArea:"广州 深圳",
            address:"烟台栖霞市",
            pz:"红富士 70# 80#",
            coldS:"供应链金融合作库",
            sh:false//是否关注的标志
          },
          status:{
              ag:true,
              trader:true
          },
          traderFlag:false,
          agenyFlag:false,
          coldFlag:false,
          mobile:18986008673,
          mobileO:18986008673,
          mobileT:18986008673,
          coldShow:true,

        }
      },
    methods:{
          phone(){},
      phoneShow(){
        this.coldShow = false;
      },
      cancel(){
          this.coldShow = true;
      },
      back(param){
              this.reset();
              this.$router.go(-1);
          },
          reset(){
            this.status.ag = true;
            this.status.trader = true;
            this.traderFlag = false;
            this.agenyFlag = false;
            this.coldFlag = false;
            this.coldShow = true;
          },
        init(name){
          if(name =="ag"){
            this.person.representative = "代办"
//            this.person.real = "个人认证"
              this.agenyFlag = true;
              fn.init();
          }else if(name =="trader"){
              this.person.representative = "客商"
            this.status.trader = true;
            this.status.ag = false;
            this.traderFlag = true;
          }else if(name =="cold"){
              console.log(name);
              this.person.real = "企业认证"
            this.status.trader = false;
            this.status.ag = false;
            this.coldFlag= true;
          }
      },
      notice(){
        if(!this.person.sh){
          Toast('已关注');
          this.person.sh = true;
        }else{
          Toast('关注');
          this.person.sh = false;
        }
      }
    },
    activated(){
        var name = this.$route.query.id;
        this.init(name);

    }
  }
</script>
