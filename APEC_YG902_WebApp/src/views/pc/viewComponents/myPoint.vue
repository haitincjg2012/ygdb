<template>
<div class="c-point-bg">
  <my-scroll :pullup="pullup" class="pointWrapper" :data="pointViewList?pointViewList:[]" @scrollToEnd="loadMore" ref="wrapper">
    <div>
      <div class="c-point-header">
        <my-header class="c-point-header-active" icon="1">
          <h1 slot="pack" class="my-header-point-title">会员中心</h1>
        </my-header>
        <div class="c-point">
          <img class="c-point-banner-bg" :src="pointImgbg" >
          <div class="c-point-banner-content">
            <div v-html="htmllevelIcon">
            </div>
            <p class="c-mypoint-text">我的积分：{{point}}分</p>
            <div class="c-point-level">
              <div class="c-p-line" ref="lineWrapper">
                <div class="c-p-line-f" ref="linef"></div>
                <div class="c-p-line-other" ref="lineOther"></div>
              </div>
              <div class="c-p-line-box-dot" v-html="htmlDot">
              </div>
              <div class="c-p-line-text" v-html="htmlLevelText">
              </div>
            </div>
            <p class="c-mypoint-next-leveltext">还差{{surplus}}积分升级为{{nextLevel}}会员</p>
          </div>
        </div>
      </div>
      <div class="c-point-title">
        <h4 class="c-point-t-text">积分规则</h4>
        <div class="c-point-t-btn">
          <img class="c-point-t-icon" src="../../../assets/img/back.png">
        </div>
      </div>
      <div class="c-point-content">
        <h4 class="c-point-t-text">积分明细</h4>
        <div  v-for="e in pointViewList" class="c-p-content-list">
          <div class="c-p-c-list-content">
            <p class="c-p-c-l-content-text">{{e.remark}}</p>
            <p class="c-p-c-l-content-time">{{e.createDate}}</p>
          </div>
          <div class="c-p-c-l-score">
            <p class="c-p-c-l-score-num">{{e.pointsChanged}}</p>
          </div>
        </div>
      </div>
    </div>
  </my-scroll>
</div>
</template>
<style>
  @import "../../../assets/css/mypoint.css";
</style>
<script>
  import scroll from '../../../components/scroll/scrollbg.vue' //分页组件
  import header from "../../../components/header/headerTwo.vue"  //头部组件
  import pointBg from "../../../assets/img/pointBg.png"  //黄金会员的背景

  import bronze from "../../../assets/img/bronze.png"  //青铜会员的图片
  import silver from "../../../assets/img/silver.png"  //白银会员的图片
  import gold from "../../../assets/img/gold.png"  //黄金会员的图片
  import platinum from "../../../assets/img/platinum.png"  //铂金会员的图片
  import diamond from "../../../assets/img/diamond.png"  //钻石会员的图片
  import master from "../../../assets/img/master.png"  //大师会员的图片

  import c_js from '../../../assets/js/common'

  import API from '../../../api/api';

  var enumerate = {
      "QT":0,
      "BY":1,
      "HJ":2,
      "ZS":3,
      "BJ":4,
      "DS":5
  };

  var enumerateScore = {
    "QT":{minscore:0,
          standard:500},//0 500等级的满分
    "BY":{minscore:500,
          standard:1000},//1000 1500等级的满分
    "HJ":{
           score:1500,
           standard:1500,//1500  3000等级的满分
         },
    "ZS":{
      score:3000,
      standard:2000,//2000  5000等级的满分
    },
    "BJ":{
      score:3000,
      standard:2250,//2250  7250等级的满分
    },
//    "DS":"",
  };
  const api = new API();
  export default{
      data(){
          return {
              pointImgbg:pointBg,//积分背景图
              pointViewList:null,//积分的列表
              point:0,//我的积分
              surplus:0,//剩余多少分
              nextLevel:"青铜",//默认为青铜

              pageCount:10000,//供求的总数
              currentPageNo:1,//供求信息的页码
              pullup:true,

              htmlDot:"",//积分的点
              htmlLevelText:"",//积分的文字
              htmllevelIcon:"",//默认是青铜
              levelBg:{
                "QT":bronze,
                "BY":silver,
                "HJ":gold,
                "ZS":platinum,
                "BJ":diamond,
                "DS":master
              },
          }
      },
      methods:{
        GetPointList(){//获取信息
          const self = this;

          let params = {
            api: "/yg-user-service/userpoint/pageUserPointRecords.apec",
            data: {
              pageNumber: self.currentPageNo
            }
          }
          try {
            api.post(params).then((res) => {
              var item = res.data;
              if (item.succeed) {
                self.pageCount = item.data.pageCount;
                self.pointViewList = self.pointViewList || [];
                item.data.rows.forEach((item) => {
                  self.pointViewList.push({
                    'createDate': new Date().format(item.createDate, 'yyyy-MM-dd'),
                    'pointsChanged': item.pointsChanged > 0? "+" + item.pointsChanged :item.pointsChanged ,
//                    'remark': item.remark.length > 10?(item.remark.substring(0,10) + "..."):item.remark,
                    'remark': item.remark,
                    'class':item.pointsChanged-0>0?'blue':'red'
                  })
                });

                console.log(self.pointViewList, 8888888);
              } else {
              }

            }).catch((error) => {
              console.log(error)
            })
          } catch (error) {
            console.log(error)
          }
        },
        loadMore(){

          var self = this;
          if(this.pageCount > this.currentPageNo){
            this.currentPageNo ++;
            this.GetPointList();
          }
        },
        getPoint(){
          const self = this;
          let params = {
            api: "/yg-user-service/userpoint/queryMyPoint.apec",
            data: {

            }
          }
          var type;
          try {
            api.post(params).then((res) => {
              var item = res.data;
              var percent;
              if (item.succeed) {
                  self.point = item.data.availablePoints;
                  self.surplus = item.data.lastLevelPoints;
                  self.nextLevel = item.data.lastUserLevelKey;
                  type = item.data.userLevel;

                  percent = (self.point/enumerateScore[type].standard).toFixed(2);

              } else {
                type = "QT";
                percent = 0;
              }
              html(type, percent);
            }).catch((error) => {
              type = "QT";
              html(type, 0);
              console.log(error)
            })
          } catch (error) {
            type = "QT";
            html(type,0);
            console.log(error)
          }

          function html(type,percent) {
            var htmlDot = "<span class='c-p-line-dot c-p-line-dot-active'></span>"
              +"<span class='c-p-line-dot " + (enumerate[type] >= 1?"c-p-line-dot-active  '":"'") + "></span>"
              +"<span class='c-p-line-dot " + (enumerate[type] >= 2?"c-p-line-dot-active '":"'") + "></span>"
              +"<span class='c-p-line-dot " + (enumerate[type] >= 3?"c-p-line-dot-active  '":"'") + "></span>"
              +"<span class='c-p-line-dot " + (enumerate[type] >= 4?"c-p-line-dot-active  '":"'") + "></span>"
              +"<span class='c-p-line-dot " + (enumerate[type] >= 5?"c-p-line-dot-active  '":"'") + "></span>";
            self.htmlDot = htmlDot;

            var htmlLevelText = "<div class='c-p-line-text-com'><p class='c-p-line-name levelActive'>青铜</p> </div>"
              +"<div class='c-p-line-text-com'><p class='c-p-line-name " +(enumerate[type] >= 1?"levelActive '":"'") + ">白银</p></div>"
              +"<div class='c-p-line-text-com'><p class='c-p-line-name " +(enumerate[type] >= 2?"levelActive '":"'") + ">黄金</p></div>"
              +"<div class='c-p-line-text-com'><p class='c-p-line-name " +(enumerate[type] >= 3?"levelActive '":"'") + ">铂金</p></div>"
              +"<div class='c-p-line-text-com'><p class='c-p-line-name " +(enumerate[type] >= 4?"levelActive '":"'") + ">钻石</p></div>"
              +"<div class='c-p-line-text-com'><p class='c-p-line-name " +(enumerate[type] >= 5?"levelActive '":"'") + ">大师</p></div>";
            self.htmlLevelText = htmlLevelText;

            var htmllevelIcon =  '<img src="' + self.levelBg[type] + '" class="c-point-levelPortrait">';
            self.htmllevelIcon = htmllevelIcon;

            var el = self.$refs.lineWrapper;
            var elf = self.$refs.linef;
            var elOther = self.$refs.lineOther;

            var width = el.offsetWidth;
            var aSection = width / 5;
            var fwidth = aSection*enumerate[type] + aSection*percent;
            var otherwidth = width - fwidth;

            elf.style.width = fwidth + "px";
            elOther.style.width = otherwidth + "px";

          }

        }
      },
    activated(){
      this.currentPageNo=1;
      this.GetPointList();
      this.getPoint();
      this.pointViewList = null;
      this.$refs.wrapper.init();
//      this.point = this.$store.state.point || c_js.getLocalValue('point') || 0;
    },
      components:{
        "my-header":header,
        'my-scroll':scroll,//分页组件
      }
  }
</script>
