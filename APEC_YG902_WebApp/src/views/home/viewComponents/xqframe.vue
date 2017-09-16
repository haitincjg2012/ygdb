<template>
  <div style="background-color: #fff">
    <div class="z-person">
      <div class="z-p-head-t">
        <!--<h1 class="z-p-h-text">{{person.title}}</h1>-->
        <div class="return-t" @click="back">
          <img src="../../../assets/img/ret.png">
        </div>
      </div>
      <div class="z-bannerImg">
        <img :src="person.bannerImgUrl">
        <img :src="person.portrait" class="z-head-portrait" v-if="agenyFlag || traderFlag">
      </div>
      <div class="z-p-info">
        <div class="c-t-info">
          <span class="z-p-name" v-if="agenyFlag || traderFlag">{{person.name}}</span>
          <span class="z-cold-name" v-if="coldFlag">{{person.name}}</span>
          <img :src="person.level" class="z-p-level" v-if="agenyFlag || traderFlag">
          <span class="z-p-represent" v-if="agenyFlag || traderFlag">{{person.representative}}</span>
          <span class="z-p-real" v-if="(agenyFlag && person.real)">实名认证</span>
          <span class="z-p-real" v-if="(traderFlag&& person.real)">实名认证</span>
          <span class="z-p-real c-p-c-real" v-if="coldFlag&& person.real">企业认证</span>
          <span class="c-p-tx" v-if="coldFlag&&person.coldS">供应链金融合作库</span>
        </div>
        <p class="z-p-x-agency" v-if="agenyFlag">{{person.ageny}}</p>
        <div class="z-p-path">
          <div class="z-p-part">
            <p class="z-p-number">{{person.notice}}</p>
            <p><span>关注</span></p>
          </div>
          <div class="z-p-part">
            <p class="z-p-number">{{person.bs}}</p>
            <p><span>浏览</span></p>
          </div>
          <div class="z-p-part">
            <p class="z-p-number">{{person.concat}}</p>
            <p><span>联系</span></p>
          </div>
          <!--<div class="z-p-follow">-->
            <!--<div class="z-p-f" :class="{active:person.sh}" @click="notice">-->
              <!--{{person.sh?"取消关注":"关注"}}-->
            <!--</div>-->
          <!--</div>-->
        </div>
      </div>

      <div class="z-space-h"></div>
      <div class="z-p-des">
        <div class="z-p-des-area z-p-des-w" v-if="coldFlag">
          <img class="z-p-des-icon" src="../../../assets/img/kr.png">
          <span class="z-x-sp-com">库 容 量&nbsp;</span>
          <input type="text" readonly v-model="person.userOrgClientVO.orgStockCap">
        </div>
        <div class="z-p-des-area z-p-des-w" >
          <img class="z-p-des-icon" src="../../../assets/img/qy.png">
          <span class="z-x-sp-com">所在区域</span>
          <input type="text" readonly v-model="person.userOrgClientVO.address">
        </div>
        <div class="z-p-des-pz z-p-des-w" v-if="agenyFlag || coldFlag || traderFlag">
          <img class="z-p-des-icon" src="../../../assets/img/zypz.png">
          <span class="z-x-sp-com">主营品种</span>
          <input type="text" readonly v-model="person.userOrgClientVO.mainOperating">
          <!--<span>{{person.pz}}</span>-->
        </div>
        <div class="z-p-des-pz z-p-des-w" v-if="agenyFlag">
          <img class="z-p-des-icon" src="../../../assets/img/kh.png">
          <span class="z-x-sp-com">客户市场</span>
          <input type="text" readonly v-model="person.userOrgClientVO.saleAddress">
          <!--<span>{{person.pz}}</span>-->
        </div>
        <!--<div class="z-p-des-market z-p-des-w" v-if="traderFlag">-->
          <!--<img class="z-p-des-icon" src="../../../assets/img/dhqy.png">-->
          <!--<span class="z-x-sp-com">调货区域</span>-->
          <!--<input type="text" readonly v-model="person.saleArea">-->
          <!--&lt;!&ndash;<span>{{person.market}}</span>&ndash;&gt;-->
        <!--</div>-->
        <div class="z-p-des-market z-p-des-w" v-if="traderFlag">
          <img class="z-p-des-icon" src="../../../assets/img/xsqy.png">
          <span class="z-x-sp-com">销售区域:</span>
          <input type="text" readonly v-model="person.userOrgClientVO.saleAddress">
          <!--<span>{{person.market}}</span>-->
        </div>
        <div class="z-p-des-text">
           <div class="z-p-des-w c-border-none">
             <img class="z-p-des-icon" src="../../../assets/img/sl.png">
             <span class="z-x-sp-com">实力描述:</span>
           </div>

          <p class="z-p-des-content">{{person.userOrgClientVO.remark}}</p>
        </div>
      </div>
      <!--<div class="z-space"></div>-->

    </div>
    <div class="z-space-h"></div>
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
    <div class="z-space-h"></div>
    <div class="z-num-main" v-if="status.ag">
      <h4 class="z-num-main-t">调果数据</h4>
      <div class="z-space"></div>
      <div id="main" :class="{Zmain:status.ag}" v-if="status.ag"></div>
    </div>
    <div class="z-space-h"></div>
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
          </ul>
      </div>
    </div>
    <div class="z-phone">
      <a @click.stop="phoneClick" :href="`tel:${mobile}`" class="c-phone" v-if="agenyFlag || traderFlag">打电话</a>
      <a @click.stop="phoneShow" v-if="coldFlag">打电话</a>
      <div class="z-cold-mobile" :class="{Cshow:coldShow}" v-if="coldFlag">
          <p class="c-text">请选择联系人</p>
        <div class="z-space-com c-space-t"></div>
          <a :href="`tel:${mobileO}`" class="c-com">
             <img src="../../../assets/img/p.png" class="c-img-com"/>
             <span class="c-sp-text">老板</span>
             <img src="../../../assets/img/mobile.png" class="c-m-icon-com c-icon-t">
          </a>
        <div class="z-space-com c-space-t"></div>
          <a v-for="item in itemPhone"
            :href="`tel:${item.mobile}`"  class="c-com">
          <img src="../../../assets/img/p.png" class="c-img-com"/>
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
      for(var key in data){
          xData.push(key);
          yData.push(data[key]);
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
          }else{
            this.person.portrait= dt.imgUrl;
          }
      }else{
        this.person.portrait= P;
      }

      this.person.name = dt.userOrgClientVO.orgName;
      this.person.userOrgClientVO.address = dt.userOrgClientVO.address;
      this.person.userOrgClientVO.mainOperating = dt.userOrgClientVO.mainOperating;//主营品种
      this.person.userOrgClientVO.saleAddress = dt.userOrgClientVO.saleAddress;//客户市场、销售区域
      this.person.userOrgClientVO.remark = dt.userOrgClientVO.remark;//实力描述
      this.person.userOrgClientVO.orgStockCap = dt.userOrgClientVO.orgStockCap;//库容量
      this.person.representative = dt.userTypeKey;
      this.person.bannerImgUrl = dt.userOrgClientVO.orgBannerUrl || DBanner;
      this.person.level = IMG.methods.userLevel(dt.userPoint.userLevel);
      this.person.userOrgClientVO.address = dt.userOrgClientVO.address;

      if(dt.userType == "LK"){
         var arrT = dt.userOrgClientVO.userTagsVOS;
        console.log(this.person.real,9999);

         arrT.forEach(function (current) {
             var className = current.className;
             if(className == "GYRZ"){
               this.person.coldF1 = true;
             }
           if(className == "GYLJRHZK"){
             this.person.coldS = true;
           }
         })


      }else{
        this.person.real = (dt.userStatus == "UNREALAUTH")? false:true;
      }
      this.person.mobile = dt.mobile;
    },
    initgq(data){
      if(!data.succeed){
        return;
      }
       var rows = data.data.rows;
      var arr = [];
      var that = this;
      rows.forEach(function (current, index) {
        var obj = {};
        var len = 0;
        obj.ss = MYGQList;
        obj.skuName = current.skuName;
        obj.showCredateTime = current.showCredateTime;
        var id = current.userId;
        obj.id = id;
        obj.levelImg =IMG.methods.userLevel(current.userLevelName);
        obj.img = current.firstImageUrl;
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
      that.itemGQ = arr;
      console.log(that.itemGQ);
    }
  }
  export default{
      data(){
        return{
          person:{
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
            representative:"",
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
//          mobileT:18986008673,
          coldShow:true,
          itemPhone:null,
          itemGQ:null,//供求信息
          pageNumber:1,//供求信息的页码
          del:{},//记录数据条数
          name:""
        }
      },
    methods:{
          phone(){},
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

        if(!el){
            setTimeout(function () {
              var em = document.querySelector(".z-cold-mobile");
              var offsetH = em.offsetHeight;
              em.style.top = -offsetH + "px";
            }, 1000)
        }else{

          setTimeout(function () {
            var offsetH = el.offsetHeight;
            el.style.top = -offsetH + "px";
          }, 500)
//
//          console.dir(window.getComputedStyle(el, null).height);
//          console.log(offsetH, 99999);

        }
      },
      coldPhone(data){
          var dt = data.data;
          var arr = [];
          var self = this;
          dt.forEach(function (current) {
             var obj = {};
             var type = current.userDetailType;
             if(type == "LK_LB"){
                self.mobileO = current.mobile;
             }else{
               obj.name = current.name;
               obj.mobile = current.mobile;
               arr.push(obj);
             }
          })

        this.itemPhone = arr;
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
            this.person.coldS = false;
            this.person.coldF1 = false;
          },
        init(name, id, orgId){
          var self = this;
          var id = arguments[1]?arguments[1]:self.id;
          var orgId = arguments[2]?arguments[2]:self.orgId;


          let params3 = {
            api:"/_node_user_org/_view_org_info.apno",
            data:{
              orgId:orgId
            }
          };
          this.post(params3,this.bs);

          if(name =="ag"){
            this.person.representative = "代办"
            this.agenyFlag = true;
            let params = {
              api:"/yg-user-service/user/findUserInfo.apec",
              data:{
                id:id
              }
            };
            this.post(params, fn.xqContent.bind(this));
            var params2 = {
              api:"/yg-voucher-service/voucher/getNumberRankViewVO.apec",
              data:{
                userId:id
              }
            }
            this.post(params2, this.DBRet);

          }else if(name =="trader"){
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
                userId:id
              }
            }
            this.post(pT, this.DBRet);
          }else if(name =="cold"){
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
        this.person.weight = dt.totalNumber + weight;
        this.person.rank = dt.rankNo;
        if(dt.attrNumberMap){
          fn.init(dt.attrNumberMap, weight);
        }else{
            fn.init(dt.attrNumberMap, weight);
        }
      },
      bs(data){
          var dt = data.data;
        this.person.notice = dt.attenNum;
        this.person.bs = dt.viewNum;
        this.person.concat = dt.phoneNum;
      },
      phoneClick(){
          let params = {
              api:"/_node_user_org/_view_org.apno",
              data:{
                  orgId:self.orgId,
                  type:"PHONENUM"
              }
          }

          this.post(params,this.phoneViewRet);
      },
      phoneViewRet(){
      },
      gqlist(){
        var pg = arguments[0] || this.pageNumber;
        var that = this;
        let params = {
          api:"/_node_user/_product_list.apno",
          data:{
            orgId:that.orgId,
            pageNumber: "1",
          }
        }
        this.post(params,fn.initgq.bind(this));
      },
      initD(id){
        this.init(this.name, id, id);
      }
    },
    activated(){
        var name = this.$route.query.flag;
        this.orgId = this.$route.query.orgId;
        this.userId = this.$route.query.userId;
        var id = this.$route.query.id;
        this.name = name;
        this.id = id;
        this.init(name);
      let params = {
        api:"/_node_user_org/_view_org.apno",
        data:{
          orgId:this.orgId,
          vieType:"VIEWNUM"
        }
      }

      this.post(params,this.phoneViewRet);

      this.gqlist(1);
    }
  }
</script>
