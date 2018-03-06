<template>
  <div style="background-color: #fff">
    <!--<my-scroll class="scrollWrapperT" :data="itemGQ" :pullup="pullup" @scrollToEnd="loadMore" >-->
      <div class="scrollWrapperT">
        <div class="z-person">
          <div>
            <div class="z-p-head-t">
              <div class="return-t" @click="back">
                <img src="../../../assets/img/ret.png">
              </div>
            </div>
            <div class="z-bannerImg">
              <img :src="person.bannerImgUrl" class="z-bannerImg-bg">
              <div class="z-bannerImg-mask">
                <div class="z-bannerImg-picture-mask"></div>
                <div class="z-bannerImg-personPicture">
                  <img :src="person.portrait" class="z-bannerImg-img" :data-portraitSrc="person.portraitSrc" @click="picture($event)">
                  <p class="z-bannerImg-text">{{person.name}}</p>
                </div>
              </div>
            </div>
          </div>
          <div class="z-p-info">
            <div class="z-p-path">
              <div class="z-p-part">
                <p class="z-p-number">{{person.notice}}</p>
                <p><span class="z-p-style">关注</span></p>
              </div>
              <div class="z-p-part">
                <p class="z-p-number">{{person.bs}}</p>
                <p><span class="z-p-style">浏览</span></p>
              </div>
              <div class="z-p-part">
                <p class="z-p-number">{{person.concat}}</p>
                <p><span class="z-p-style">联系</span></p>
              </div>
            </div>
            <div class="z-p-path-line"></div>
            <div class="c-p-introduce">
              <div class="c-p-intro-com">
                <div class="c-p-intro-label">
                  <span class="c-p-label-color">身份&emsp;&emsp;</span>
                </div>
                 <div class="c-p-intro-text" v-html="person.identityHtml">
                 </div>
              </div>
              <div class="c-p-intro-com">
                <div class="c-p-intro-label">
                  <span class="c-p-label-color">签名&emsp;&emsp;</span>
                </div>
                <div class="c-p-intro-text">
                  <span class="z-p-sign-text">{{person.userOrgClientVO.remark == ""?'急卖货，急找货，果来乐都能做':person.userOrgClientVO.remark}}</span>
                </div>
              </div>
            </div>
            <p class="z-p-x-agency" v-if="agenyFlag">{{person.ageny}}</p>
          </div>

          <div class="z-space-h"></div>
          <div class="z-p-des">
            <div class="z-p-des-area z-p-des-w" v-if="coldFlag">
              <!--<img class="z-p-des-icon" src="../../../assets/img/kr.png">-->
              <div class="z-p-des-com">
                <span class="z-x-sp-com">库 容 量&emsp;</span>
              </div>
              <div class="z-p-des-comText">
                {{person.userOrgClientVO.orgStockCap}}
              </div>

              <!--<input type="text" disabled v-model="person.userOrgClientVO.orgStockCap">-->
            </div>
            <div class="z-p-des-area z-p-des-w" >
              <div class="z-p-des-com">
                <span class="z-x-sp-com">所在区域</span>
              </div>
              <div class="z-p-des-comText">
                  {{person.userOrgClientVO.address}}
              </div>
              <!--<input type="text" disabled v-model="person.userOrgClientVO.address">-->
            </div>
            <div class="z-p-des-pz z-p-des-w" v-if="agenyFlag || coldFlag || traderFlag">
              <div class="z-p-des-com">
                <span class="z-x-sp-com">主营品种</span>
              </div>
             <div class="z-p-des-comText">
               {{person.userOrgClientVO.mainOperating}}
             </div>
              <!--<input type="text" disabled v-model="person.userOrgClientVO.mainOperating">-->
              <!--<span>{{person.pz}}</span>-->
            </div>
            <div class="z-p-des-pz z-p-des-w" v-if="agenyFlag || coldFlag">
              <!--<img class="z-p-des-icon" src="../../../assets/img/kh.png">-->
              <div class="z-p-des-com">
                <span class="z-x-sp-com">客户市场</span>
              </div>
              <div class="z-p-des-comText">
                {{person.userOrgClientVO.saleAddress}}
              </div>
              <!--<input type="text" disabled v-model="person.userOrgClientVO.saleAddress">-->
            </div>
            <div class="z-p-des-market z-p-des-w" v-if="traderFlag">
              <!--<img class="z-p-des-icon" src="../../../assets/img/xsqy.png">-->
              <div class="z-p-des-com">
                <span class="z-x-sp-com">销售区域</span>
              </div>
              <div class="z-p-des-comText">
                {{person.userOrgClientVO.saleAddress}}
              </div>
            </div>
            <!--<div class="z-p-des-text">-->
              <!--<div class="z-p-des-w c-border-none">-->
                <!--&lt;!&ndash;<img class="z-p-des-icon" src="../../../assets/img/sl.png">&ndash;&gt;-->
                <!--<span class="z-x-sp-com">实力描述:</span>-->
              <!--</div>-->
              <!--<p class="z-p-des-content">{{person.userOrgClientVO.remark}}</p>-->
            <!--</div>-->
          </div>
        </div>
        <!--<div class="z-space-h" v-if="status.ag || status.trader"></div>-->
        <div class="z-transaction" v-html="battlefieldHtml"></div>
        <!--<div class="z-transaction" v-if="status.ag || status.trader">-->
          <!--<h4 class="z-h4-title">平台战绩</h4>-->
          <!--<div class="z-space"></div>-->
          <!--<div class="z-transaction-results">-->
            <!--<div class="z-r-totalW">-->
              <!--<p class="z-r-s-com">{{person.weight}}</p>-->
              <!--<p>累计调货</p>-->
            <!--</div>-->
            <!--<div class="z-vertical-line">-->
              <!--<div class="z-v-line">-->
              <!--</div>-->
            <!--</div>-->
            <!--<div class="z-r-rank">-->
              <!--<p class="z-r-s-com">{{person.rank}}</p>-->
              <!--<p>平台排名</p>-->
            <!--</div>-->
          <!--</div>-->
        <!--</div>-->
        <div class="z-space-h"></div>
        <div class="z-num-main" :class ="{show:!status.ag}">
          <h4 class="z-num-main-t">调果数据</h4>
          <div class="z-space"></div>
          <div id="main" class="Zmain"></div>
          <!--<div id="main" class="Zmain" :class="{Zshow:AgChart}"  ></div>-->
          <!--<div class="c-z-ondata" v-if="AgChart">-->
             <!--<img src="../../../assets/img/noData.png" class="c-z-nodata-img">-->
             <!--<p class="c-z-nodata-text">暂无数据</p>-->
          <!--</div>-->
        </div>
        <div class="z-space-h" v-if="status.ag"></div>
        <div class="z-p-gy">
          <h4 class="c-p-gy-x">供求</h4>
          <div class="c-gy-v"></div>
          <div>
            <ul class="c-gq-list">
              <li
                :is="item.ss"
                :item="item"
                v-for="item in itemGQ" v-on:xq="initD">
              </li>
              <li class="li-space">{{loadingText}}</li>
            </ul>
          </div>
        </div>
      </div>
    <!--</my-scroll>-->
    <div class="z-phone-one">
      <!--<div v-if="agenyFlag || traderFlag" class="xq-c-sign">-->
      <div class="xq-c-sign">
        <div class="z-p-follow" @click="notice">
          <div class="z-p-star" :class="{startActive:person.sh}">
          </div>
          <div class="z-p-f" >
          {{person.sh?"已关注":"关注"}}
          </div>
        </div>
        <div class="z-p-phone">
          <a @click.stop="phoneClick" :href="`tel:${mobile}`" class="c-phone" v-if="!coldFlag">打电话</a>
          <a @click.stop="phoneShow" v-if="coldFlag">打电话</a>
        </div>
      </div>

      <div class="z-cold-mobile" :class="{Cshow:coldShow}" v-if="coldFlag">
          <p class="c-text">请选择联系人</p>
        <div class="z-space-com c-space-t"></div>
          <a :href="`tel:${mobileO}`" @click.stop="phoneClick" class="c-com" v-if="LB">
             <img :src="LBIcon" class="c-img-com"/>
             <span class="c-sp-text">老板</span>
             <img  src="../../../assets/img/mobile.png" class="c-m-icon-com c-icon-t">
          </a>
        <div class="z-space-com c-space-t"></div>
          <a v-for="item in itemPhone"
            :href="`tel:${item.mobile}`"  class="c-com" @click.stop="phoneClick">
          <img :src="item.img" class="c-img-com"/>
          <span class="c-sp-text">{{item.name}}</span>
            <img src="../../../assets/img/mobile.png" class="c-m-icon-com">
          </a>
        <div class="z-space-com c-space-t"></div>
          <div @click="cancel" class="c-cancel">取消</div>
      </div>
    </div>
  </div>
</template>
<style scoped>
  @import "../../../assets/css/xqframe.css";
</style>
<script>
  import IMG from "../../../components/gqimg.vue"
  import P from "../../../assets/img/icon.png"
  import {Swipe, SwipeItem, Indicator,MessageBox, Toast} from 'mint-ui'
  import DBanner from "../../../assets/img/defaultBg.png"
  import defaultIcon from "../../../assets/img/defaultForm.png"
  import scroll from '../../../components/scroll/scroll'
  import WX from '../../../components/wx.vue'  //微信分享功能

  import T from "../../../assets/img/t.png"
  var ec = require("../../../assets/js/echarts.min");
  import API from '../../../api/api'
  import MYGQList from "./mygqlist.vue"

  const api = new API();
  var fn = {
      gq:[],
    init:function (data, weight){


      var el = document.getElementById("main");
      var myChart = ec.echarts.init(el);
      var xData = [];
      var yData = [];

      if(data){
          var dt = JSON.parse(data);
      }
      for(var key in dt){
          xData.push(key);
          yData.push(dt[key]);
      }
      var tw;
      if(tw){
          tw ="重量("+weight+")";
      }else{
        tw ="重量";
      }
      var option = {
        color: ['#27cba8'],
        title: {
          text: '',
        },
        grid: {
          left: '3%',
          right: '13%',
          bottom: '15%',
          containLabel: true
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
          data: xData,
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
          name:tw,
          splitLine:{
            show:false
          }
        },
        series: [{
          name: '数量' ,
          type: 'bar',
          barWidth:"30%",
//          data: [5, 20, 36, 20, 36, 10]
          data:yData
        }]
      };

      // 使用刚指定的配置项和数据显示图表。
      myChart.setOption(option);
    },
    xqContent(data){
      var dt = data.data;

      if(dt.imgUrl){
        if(dt.imgUrl == ""){
            this.person.portrait= P;
            this.person.portraitSrc = "";
          }else{
            this.person.portrait= dt.imgUrl + "?x-oss-process=style/_head";
          this.person.portraitSrc = dt.imgUrl;
          }
      }else{
        this.person.portrait= P;
        this.person.portraitSrc = "";
      }


//      this.person.identityHtml = "<span class='z-p-name'>" + this.person.representative +"</span>";
      this.person.name = dt.userOrgClientVO.orgName;
      var name = this.person.name == ""?"详情":this.person.name;
      WX.wx("果来乐-"+ name);
      this.person.userOrgClientVO.address = dt.userOrgClientVO.address == ""?"暂无数据":dt.userOrgClientVO.address;
      this.person.userOrgClientVO.mainOperating = dt.userOrgClientVO.mainOperating == ""?"暂无数据":dt.userOrgClientVO.mainOperating;;//主营品种
      this.person.userOrgClientVO.saleAddress = dt.userOrgClientVO.saleAddress== ""?"暂无数据":dt.userOrgClientVO.saleAddress;;//客户市场、销售区域
      this.person.userOrgClientVO.remark = dt.userOrgClientVO.remark;//实力描述
      this.person.userOrgClientVO.orgStockCap = dt.userOrgClientVO.orgStockCap == ""?"暂无数据":dt.userOrgClientVO.orgStockCap;;//库容量
      this.person.representative = dt.userTypeKey;
      this.person.bannerImgUrl = (dt.userOrgClientVO.orgBannerUrl || DBanner) +"?x-oss-process=style/_detail";
      this.person.level = IMG.methods.userLevel(dt.userPoint.userLevel);
//      this.person.userOrgClientVO.address = dt.userOrgClientVO.address;

      //身份的html
      var html = "<span class='z-p-name'>" + this.person.representative +"</span>";
      if(dt.userType == "LK"){
          //供应链金融,企业认证
         var arrT = dt.userOrgClientVO.userTagsVOS;
        var self = this;
         arrT.forEach(function (current) {
             var className = current.className;
             if(className == "QYRZ"){
                 html += " <span class='z-p-authentication'>"+ current.tagName + "</span>";
                 self.person.coldF1 = true;
             }
           if(className == "GYLJRHZK"){
             html += " <span class='z-p-coor'>"+ current.tagName + "</span>";
             self.person.coldS = true;
           }
         })


      }else{
//        this.person.real = (dt.userStatus == "UNREALAUTH")? false:true;
        html += (dt.userStatus == "UNREALAUTH")? "":"实名认证";
      }
      this.person.identityHtml = html;
      this.mobile = dt.mobile;
    },
    initgq(data){
      if(!data.succeed){
        return;
      }
       var rows = data.data.rows;
      var arr = [];
      var that = this;
      that.pageCount = data.data.pageCount;
      if(data.data.currentNo == data.data.pageCount){
         that.loadingText = "没有更多数据";
      }
      this.pageNumber = data.data.currentNo;

      rows.forEach(function (current, index) {
        var obj = {};
        var len = 0;
        obj.ss = MYGQList;
        obj.skuName = current.skuName;
        obj.showCredateTime = current.showCredateTime;
        var id = current.id;
        obj.id = id;
        obj.levelImg =IMG.methods.userLevel(current.userLevelName);
        obj.img = current.firstImageUrl + "?x-oss-process=style/_list";
        obj.local = current.address;
        obj.name = current.showUserName;
        obj.Flag = that.name;
        obj.userId = current.userId;//userId和orgId是相同的
        obj.orgId = current.userId;
        obj.priceUnit = current.priceUnit;
        obj.agency = current.userTypeName;
        obj.number = current.viewNum;
        obj.star = current.saveNum;
        obj.starFlag = current.saveFlag;
        var wU = current.weightUnit;
        var t = "";
        if(wU){
          t = wU;
        }
        obj.weight = current.weight +" " + t;
        len += obj.weight.length;
        obj.path = index;

        var goodTime = current.showSecondInfo;
        var text = goodTime.join(" ");

        obj.fruitdate = text;

        var Identification = current.productTypeName;
        obj.gq = current.productTypeName;
        //单一的排序
        //只有求购和供应
        if(Identification == "求购"){
          obj.bg = true;
          obj.indentification = 0;
          obj.endAmount = current.endAmount.toString();
          len += obj.endAmount.length;
          obj.startAmount = current.startAmount.toString();
          len += obj.startAmount.length;
        }else{
          obj.bg = false;
          obj.indentification = 1;
          obj.amount = current.amount.toString();
          len += obj.amount.length;
        }
        var n = (250 - (len + 3 + 3 + 3) * 10)/20;
        obj.wh = n;

        var pattern =/[0-9]*/g;
        var pt = /([a-z]*|[A-Z]*|(\s*))/g;
        var st = obj.name +"";
        var arrttt =st.match(pattern);
        var arrtttt = st.match(pt);
        var lF = 0,lt = 0;

        arrttt.forEach(function (current) {
          lF += current.length;
        })

        arrtttt.forEach(function (current) {
          lt += current.length;
        })
        obj.addreeWh = (185 - (obj.name.toString().length + obj.agency.length) * 12 + lF * 6 + lt*8)/20;;
        obj.productTypeName = IMG.methods.img(current.productTypeName);
        if(that.del.hasOwnProperty(id)){
          return;
        }
        that.del[id] = 0;
        arr.push(obj);
      });

      if(arr.length > 0){
        that.itemGQ = that.itemGQ? that.itemGQ.concat(arr):[].concat(arr);
      }

      //下拉分页的开关
      if(data.data.pageCount > 1){
        setTimeout(function () {
          that.gq.state = true;
        },that.gq.time)
      }else{
        this.gq.state = false;
      }
    },
    GZ(data){
      if(!data.succeed){
        return;
      }
        this.person.sh = data.data.attenFlag;
    }
  }
  export default{
      data(){
        return{
          person:{
            identityHtml:'',//身份
            bannerImgUrl:DBanner,
            level:"#",
            ageny:"",
            notice:"",
            bs:"",
            concat:"",
            name:"",
            real:'',
            weight:"",
            rank:"",
            representative:"",//代表冷库，客商,代办
            coldName:"",
            coldStroage:"",
            saleArea:"",
            address:"",
            pz:"",
            coldS:false,
            userOrgClientVO:{},
            sh:false,//是否关注的标志
            portrait:"",
            coldF1:false
          },
          status:{
              ag:true,
              trader:true
          },
          traderFlag:false,
          agenyFlag:false,
          coldFlag:false,
          mobile:"18986008673",
          mobileO:"",
          coldShow:true,
          itemPhone:null,
          itemGQ:null,//供求信息

          del:{},//记录数据条数
          name:"",
          browseId:"",//被浏览者Id
          browseOrgId:"",//被浏览者组织Id
          LBIcon:"",//老板图像
          LB:false,//默认是无老板

//          供求的容量
          pageNumber:1,//供求信息的页码
          pageCount:10000,//供求的总数
          loadingText:"",//没有更多数据
          mPgSize:10,//页容量
          gqData:[],//供求容器
          AgChart:false,//默认是有数据的


          //供求加载的数据
          gq:{
              time:200,//触发边界条件的时间间隔
              distance:10,//触发的距离
              state:false,//触发发的状态
          },
          bodyHeight:0,

          battlefieldHtml:"",//调果战绩
        }
      },
    methods:{
          //原图
      picture(event){
        var el = event.toElement || event.srcElement;
        var src = el.dataset.portraitsrc;
        if(src){
          this.$router.push({name:"pictureOriginal",query:{src:src}});
        }

      },
      phone(){},
      phoneReset(){
            this.itemPhone = null;
            this.mobileO = "";
            this.LBIcon = "";
            this.LB = false;
          },
      phoneShow(){
              var self = this;
         let params = {
             api:"/yg-user-service/user/findUserByOrgId.apec",
             data:{
               userOrgId:self.orgId
             }
         }

         this.post(params, this.coldPhone);
        this.coldShow = false;

        var el =document.querySelector(".z-cold-mobile");

      },
      coldPhone(data){
          this.phoneReset();
          var dt = data.data;
          var self = this;
            var arr = [];
            dt.forEach(function (current) {
              var obj = {};
              var type = current.userDetailType;
              if(type == "LK_LB"){
                self.LB = true;
                self.mobileO = current.mobile;
                self.LBIcon = (current.imgUrl || defaultIcon)+"?x-oss-process=style/_head";
              }else{
                obj.img = (current.imgUrl || defaultIcon) +"?x-oss-process=style/_head";
                obj.name = current.name;
                obj.mobile = current.mobile;
                arr.push(obj);
              }
            })

        if(arr.length > 0){
          this.itemPhone =  this.itemPhone? this.itemPhone.concat(arr):[].concat(arr);
        }else{
          this.itemPhone = null;
        }
      },
      cancel(){
          this.coldShow = true;
      },
      back(param){
              this.reset();
           this.$nextTick(()=>{
               var wxF = localStorage.wx;
               if(wxF){
                 this.$router.push({name:"home"});
                 localStorage.removeItem("wx");
               }else{
                 this.$router.go(-1);
               }
        });
          },
      reset(){
            this.status.ag = true;
            this.status.trader = true;
            this.traderFlag = false;
            this.agenyFlag = false;
            this.coldFlag = false;
            this.coldShow = true;
            this.person.coldS = false;
            this.person.coldF1 = false;
            this.person.name ="";
            this.loadFlag = true;
            this.AgChart = false;
            this.battlefieldHtml = "";

          },
      init(name, id, orgId){
          var self = this;
          var id = arguments[1]?arguments[1]:self.id;
          var browseId = arguments[1]?arguments[1]:self.id;//被浏览者Id
          var orgId = arguments[2]?arguments[2]:self.orgId;

          let params3 = {
            api:"/_node_user_org/_view_org_info.apno",
            data:{
              orgId:orgId
            }
          };
          this.post(params3,this.bs);

          if(name =="ag"){
              //代办
            this.person.representative = "代办"
            this.agenyFlag = true;
            let params = {
              api:"/yg-user-service/user/findUserInfo.apec",
              data:{
                id:browseId
              }
            };
            this.post(params, fn.xqContent.bind(this));
            var params2 = {
              api:"/yg-voucher-service/voucher/getNumberRankViewVO.apec",
              data:{
                userId:browseId
              }
            }
            this.post(params2, this.DBRet);

          }else if(name =="trader"){
              //客商
              this.person.representative = "客商"
            this.status.trader = true;
            this.status.ag = false;
            this.traderFlag = true;
            let params = {
              api:"/yg-user-service/user/findUserInfo.apec",
              data:{
                id:id
              }
            };
            this.post(params, fn.xqContent.bind(this));
            var pT = {
              api:"/yg-voucher-service/voucher/getNumberRankViewVO.apec",
              data:{
                userId:browseId
              }
            }
            this.post(pT, this.DBRet);
          }else if(name =="cold"){
              //冷库
            this.person.representative = "冷库"
            this.person.real = "企业认证"
            this.status.trader = false;
            this.status.ag = false;
            this.coldFlag= true;
            let params = {
              api:"/yg-user-service/user/findUserInfo.apec",
              data:{
                userOrgId:id
              }
            };
            this.post(params, fn.xqContent.bind(this));
          }

      },
      notice(){
            var fn;
        if(!this.person.sh){
          Toast('关注');
          this.person.notice =this.person.notice*1 + 1;
          this.person.sh = true;
          fn = true;
        }else{
          Toast('取消关注');
          this.person.notice =this.person.notice - 1;
          this.person.sh = false;
          fn = false;
        }

        var self = this;
        let params = {
          api:"/_node_user/_save_user_org.apno",
          data:{
            "orgId":self.orgId,
            saveFlag:fn
          }
        }

        this.post(params,this.phoneViewRet);
      },
      phoneViewRet(){
      },
      post(params, fn){
        try {
          api.post(params).then((res) => {
            var data = res.data;
            fn(data);
          }).catch((error) => {
            console.log(error)
          })
        } catch (error) {
          console.log(error)
        }
      },
      DBRet(data){
        var dt = data.data;
        var weight = dt.weight;
        var number = dt.totalNumber.toFixed(1);

        var numberW = dt.totalNumber.toFixed(1) + weight;
        var rank = dt.rankNo;
        var html = "";
        if(number > 0){
          html = "<div class='z-space-h'></div><h4 class='z-h4-title'>平台战绩</h4><div class='z-space'></div><div class='z-transaction-results'><div class='z-r-totalW'>"
            +"<p class='z-r-s-com'>" + numberW + "</p><p>累计调货</p></div><div class='z-vertical-line'><div class='z-v-line'></div></div>"
            +"<div class='z-r-rank'><p class='z-r-s-com'>"+ rank + "</p><p>平台排名</p></div></div>";

        }else{
          this.status.ag = false;
          return;
        }
        this.battlefieldHtml =  html
        if(dt.attrNumberMap){
            this.$nextTick(function () {
              fn.init(dt.attrNumberMap, weight);
            })

        }else{
            this.AgChart = true;
        }
      },
      bs(data){
          var dt = data.data;
        this.person.notice = dt.attenNum;
        this.person.bs = dt.viewNum;
        this.person.concat = dt.phoneNum;
      },
      phoneClick(){
          var self = this;
          let params = {
              api:"/_node_user_org/_view_org.apno",
              data:{
                  orgId:self.orgId,
                vieType:"PHONENUM"
              }
          }
          this.post(params,this.phoneViewRet);
      },
      gqlist(){
        var pg = arguments[0] || this.pageNumber;
        var that = this;
        let params = {
          api:"/_node_user/_product_list.apno",
          data:{
            orgId:that.orgId,
            pageNumber: pg,
          }
        }
        this.post(params,fn.initgq.bind(this));
      },
      initD(id){
        this.init(this.name, id, id);
      },
      GZ(){
     // 是否关注的方法
        var self = this;
        var params = {
            api:"/_node_user/_org_atten_flag.apno",
            data:{
              "orgId":self.orgId
            }
        }

        self.post(params,fn.GZ.bind(this));
      },
      //上拉加载更多
      loadMore(){
        if(this.pageCount > this.pageNumber){
            this.pageNumber ++;
            this.gqlist();
        }
      },
      xqList(evt){
        var e = evt || window.event;
        var el = document.querySelector(".scrollWrapperT");
        var self = this;
        if(el){
            if(self.gq.state){
              var target = e.target || e.srcElement;
              var offsetH = target.body.scrollTop;
              var sHeight = target.body.scrollHeight;
              if(sHeight - offsetH - self.bodyHeight - self.gq.distance > 0){
                self.gq.state = false;
                self.loadingText = "数据正在加载中...";
                this.loadMore();
              }
            }
        }
      },
    },

    activated(){
        this.loadingText = "";
        this.bodyHeight = document.querySelector(".page").clientHeight;
        var name = this.$route.query.flag;
        this.orgId = this.$route.query.orgId+"";
        this.userId = this.$route.query.userId;
        this.browseId = this.$route.query.userId;

        var id = this.$route.query.id;
        this.name = name;
        this.id = id;
        this.del = {};
        this.AgChart = false;
        var self = this;
        this.init(name);
        this.GZ();//关注的方法
        this.itemGQ = null;
        let params = {
          api:"/_node_user_org/_view_org.apno",
          data:{
            orgId:self.orgId,
            vieType:"VIEWNUM"
          }
        }

      this.post(params,this.phoneViewRet);

      this.gqlist(1);
    },
    mounted(){
        window.addEventListener("scroll",this.xqList, false);
    },
    components: {
      'my-scroll':scroll
    }
  }
</script>
