<template>
  <div style="height: 100% ;overflow-y:auto;">
    <!--<top-bar title="供应详情"></top-bar>-->
    <div class="z-s-header-O">
      <div class="z-title">{{title}}</div>
      <div class="return" @click="back">
        <img src="../../../assets/img/ret.png">
      </div>
    </div>
    <scroller ref="my_scroller" :style="styleCal">
    <div class="z-calcuros">
      <mt-swipe :auto="4000" class="cssHeight">
        <template v-for="(item,index) in detailUrl">
          <mt-swipe-item>
            <img :src="item.imageUrl" style="width: 100%;height: 100%;" @click.stop="originalPic(index)">
          </mt-swipe-item>
        </template>
      </mt-swipe>
    </div>
    <div class="part-two">
      <div class="z-gq-name">
          <div class="z-gq-text" :class="{GQC:dataDetail.name == '供应'?false:true}">
            {{dataDetail.productTypeName}}
          </div>
      </div>
      <div class="sp-com-one">
         {{dataDetail.skuName}}
      </div>
      <div class="z-x-time">
          {{dataDetail.showCredateTime}}
      </div>
      <!--<img class="z-logo" :src="dataDetail.productTypeName">-->

    </div>
    <div class="goods-info">
      <span class="sp-one" v-if="dataDetail.name == '供应'">{{dataDetail.amount}}</span>
      <span class="sp-one" v-if="dataDetail.name == '求购'">{{dataDetail.startAmount}}-{{dataDetail.endAmount}}</span>
      <span class="sp-com">{{dataDetail.priceUnit}}</span>
      <span class="z-g-j">{{dataDetail.weight}}{{dataDetail.weightUnit}}</span>
      <!--<span class="sp-com"></span>-->
      <span class="sp-com">{{dataDetail.showSecondInfo}}</span>
    </div>
    <div class="com">
      <div class="z-area clearfix">
        <img class="pos-pic" src="../../../assets/img/pos.png">
        <span class="sp-two">{{dataDetail.address}}</span>
        <!--<span class="sp-three sp-three-com"></span>-->
        <span class="sp-three-com">{{dataDetail.phoneNum}} 人联系</span>
        <span class="sp-three-com">{{dataDetail.viewNum}} 次浏览</span>

      </div>
    </div>
    <div class="z-p-x">
      <div @click.stop="routerPerInfo" class="peoson-info clearfix">
        <div class="z-img-person">
          <img :src="dataDetail.imgRT">
        </div>
        <div class="z-p-info">
          <div class="z-p-text">{{dataDetail.showUserName}}</div>
          <div class="z-p-name">
            <img class="level-one" :src="dataDetail.userLevelName">
            <!--<img v-show="dataDetail.userRealAuthFlag" class="z-author" :src="dataDetail.userRealAuthFlag">-->
            <span v-if="dataDetail.userShow" class="z-author">{{dataDetail.userReal}}</span>
            <span class="z-p-n-t">{{dataDetail.userTypeName}}</span>
          </div>
        </div>
        <div class="z-d">
             <img src="../../../assets/img/back.png">
        </div>

      </div>
    </div>
    <div class="z-my-publishes">
        <!--<div class="z-my-goods" @click ="popup">-->
          <!--<p>我发布的商品</p>-->
        <!--</div>-->
      <div class="wrapper" ref="wrapper" v-if="pp">
        <div>
          <div class="top-tip">
            <span class="refresh-hook">下拉刷新</span>
          </div>
          <ul>
            <li :is="item.ss" v-for="item in itemG"
                :item = "item"
            >
            </li>
          </ul>
          <div class="bottom-tip">
            <span class="loading-hook">查看更多</span>
          </div>
        </div>
      </div>
    </div>
    <div class="z-cs">
      <!--<div class="xq" @click="goodxq">-->
      <div class="xq">
        <span class="sp-f">商品详情:</span>
        <!--<img class="z-d-o" src="../../../assets/img/more-1.png">-->
      </div>
      <table class="z-table">
        <tr v-for="item in items"
        >
          <td v-for="key in item">{{key}}</td>
          <!--<td class="z-name">{{item.key}}</td>-->
          <!--<td class="z-key">{{item.value}}</td>-->
        </tr>
      </table>
      <div class="z-f-s">
          <p>描述:</p>
          <p>{{dataDetail.remark}}</p>
      </div>
    </div>
    </scroller>
    <div class="footer">
       <ul>
           <li class="z-phone-T">
             <a @click.stop="phoneClick" :href="`tel:${dataDetail.mobile}`">打电话</a>
           </li>
           <li @click.stop="collect($event,dataDetail.id)" class="z-t z-collect" :class="{activeDel:dataDetail.saveFlag}">{{save_cl}}</li>
       </ul>
    </div>

  </div>
</template>
<style scoped>
  @import "../../../assets/css/xq.css";
</style>
<script>
  import {MessageBox, Toast,Swipe, SwipeItem} from 'mint-ui';
  import API from '../../../api/api'
  import BScroll from 'better-scroll';
  import topBar from '../../../components/topBar/topBar'
//  import suInfo from './supplyInfo.vue'
  import defaultIcon from "../../../assets/img/defaultForm.png"

  import default_1 from '../../../assets/img/xqimg1.png'//默认的详情轮播图
  import default_2 from '../../../assets/img/xqimg2.png'//默认的详情轮播图
  import default_3 from '../../../assets/img/xqimg3.png'//默认的详情轮播图

  import sell from '../../../assets/img/sell-1.png'//供应
  import buy from '../../../assets/img/logo-1.png'//求购

  import Aur from '../../../assets/img/AgencyTipsR.png'//是否认证

  import daiban from '../../../assets/img/AgencyTips.png'//代办
  import zzHu from '../../../assets/img/AgencyTipsG.png'//种植户
  import keshang from '../../../assets/img/AgencyTipsK.png'//客商
  import lengku from '../../../assets/img/AgencyTipsL.png'//冷库
  import hezuoshang from  '../../../assets/img/hezuoshang.png'//合作商


  import QT from '../../../assets/img/t.png'//铜牌
  import BY from '../../../assets/img/y.png'//银牌
  import HJ from '../../../assets/img/j.png'//金牌
  import BJ from '../../../assets/img/bj-1.png'//铂金
  import ZS from '../../../assets/img/zs.png'//砖石
  import DS from '../../../assets/img/Ancrown@3x.png'//大师
  import goodD from  './goodlist.vue'

  const api = new API();
  var fn = {
      cacheData:[],
      number:function (data) {
        if(!data.succeed){
          return;
        }
      },
      phone:function (data) {
          if(data.succeed){
            this.dataDetail.phoneNum++;
          }

      },
      plain:function (data) {
        if(!data.succeed){
          return;
        }
        var dt = data.data;
        var self = this;
        self.dataDetail.id = dt.id;
        self.dataDetail.orgId = dt.orgId;
        self.dataDetail.skuName = dt.skuName;
        self.dataDetail.address = dt.address;
        self.dataDetail.showCredateTime = dt.showCredateTime;
        self.dataDetail.amount = dt.amount;
        self.dataDetail.startAmount = dt.startAmount;
        self.dataDetail.endAmount = dt.endAmount;
        self.dataDetail.priceUnit = dt.priceUnit;
        self.dataDetail.mobile = dt.mobile;
        self.dataDetail.weight = dt.weight;
        self.dataDetail.weightUnit = dt.weightUnit;
        self.dataDetail.userLevelName = self.userLevelKeySwitch(dt.userLevelName);
//        self.dataDetail.userTypeName = self.userTypeNameSwitch(dt.userTypeName);
        self.dataDetail.userTypeName = dt.userTypeName;
        self.dataDetail.showUserName = dt.showUserName;
//        self.dataDetail.productTypeName = self.productTypeSwitch(dt.productTypeName);
        self.dataDetail.productTypeName = dt.productTypeName;
        self.dataDetail.userShow = dt.userRealAuthFlag;
        self.dataDetail.userReal = dt.userRealAuthFlag?"实名认证":'';
        self.dataDetail.showSecondInfo = dt.showSecondInfo.join('|');
        self.dataDetail.viewNum = dt.viewNum-0?dt.viewNum:0;
        self.dataDetail.phoneNum = dt.phoneNum-0?dt.phoneNum:0;
        self.dataDetail.saveFlag = dt.saveFlag;
        self.dataDetail.userId = dt.userId;
        self.dataDetail.imgRT = (dt.imgUrl || defaultIcon) + "?x-oss-process=style/_head";

        if(dt.productImages.length){
            var arr = [];
            var obj = {};
            var arrImg = dt.productImages;
            for(var key in arrImg){
                obj.imageUrl = arrImg[key].imageUrl+"?x-oss-process=style/_detail";
                arr.push(obj);
            }
          self.detailUrl = arr;
          this.$store.state.xqImgArr = dt.productImages;
        }

        var tables = dt.productAttrs;

        var arr = [];
//        tables.forEach(function (current) {
//          var obj ={};
//          obj.value = current.attrValue;
//          if(obj.key == ""){
//            return;
//          }
//          obj.key = current.attrName;
//          arr.push(obj);
//        });
        self.dataDetail.name = dt.productTypeName;
        self.dataDetail.remark = dt.remark;
        this.items = fn.parameter(tables);
        this.$store.state.productImages = dt.productImages;
        this.$store.state.productTypeName = dt.productTypeName;
        this.title = dt.productTypeName + "商品";


      },
      collectFn:function (data) {
        if(data.succeed){
//          Toast("收藏成功~");
//          this.dataDetail.saveFlag = true;
        }

      },
      parameter:function (data) {
        var row = data;
        var len = row.length;
        var n = len % 3;
        if(n != 0){
            var r = Math.ceil(len/3);
        }
        var num = 0;

         var arr = [];
        var childArrN = [];
        var childValue = [];
          data.forEach(function (current,index) {
          num ++;
          childArrN.push(current.attrName);
          childValue.push(current.attrValue);
          var idx = num % 3;

          if(idx == 0){
            arr.push(childArrN);
            arr.push(childValue)
            childArrN = [];
            childValue = [];
          }
          if(num == len){

            for(var i = 0; i < r*3 - num; i ++){
              childArrN.push("");
              childValue.push("");
            }
            arr.push(childArrN);
            arr.push(childValue)
            childArrN = [];
            childValue = [];
          }

        });

         return arr;
      },
      format(){
        if(!data.succeed){
          return;
        }
        this.pageCount = data.data.pageCount;
        var rows = data.data.rows;
        this.num = rows.length;
        var arr = [];
        var that = this;
        rows.forEach(function (current, index) {
          var obj = {};
          var len = 0;
          obj.ss = goodD;
          obj.skuName = current.skuName;
          obj.showCredateTime = current.showCredateTime;
          var id = current.id;
          obj.id = id;
          obj.levelImg = that.userLevelKeySwitch(current.userLevelName);
          obj.gq = current.productTypeName;
          obj.productTypeName = QG.methods.img(current.productTypeName);
          obj.img = current.firstImageUrl;
          obj.local = current.address;
          obj.name = current.showUserName;
          obj.priceUnit = current.priceUnit;
//            obj.agency = QG.methods.userTypeNameSwitch(current.userTypeName);
          obj.agency = current.userTypeName + " ";
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
          if(Identification == "求购"){
            obj.bg = true;
            obj.indentification = 0;
            obj.endAmount = current.endAmount.toString();
            len += obj.endAmount.length;
            obj.startAmount = current.startAmount.toString();
            len += obj.startAmount.length;

            var n = (250 - (len + 6) * 10)/20;
            obj.wh = n;
            obj.qg = true;
          }else{
            obj.bg = false;
            obj.indentification = 1;
            obj.amount = current.amount.toString();
            len += obj.amount.length;
            var n = (230 - (len + 1) * 10)/20;
            obj.wh = n;
            obj.gy = true;
          }

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
          obj.addreeWh = (201 - (obj.name.toString().length + obj.agency.length) * 12 + lF * 6 + lt*8)/20;
          if(that.del.hasOwnProperty(id)){
            return;
          }

          that.del[id] = 0;
          arr.push(obj);
        });


        if(this.pageNumber == 1){
          this.publishViewList = arr;
        }else{
          fn.cacheData = arr;
        }
      },
      dt(){}
  }
  export default{
      data(){
          return{
              browse:100,
              mobile:100,
              data:null,
              dataDetail:{
                id:'',
                skuName:'',
                address:'',
                showCredateTime:'',
                amount:'',//单价
                priceUnit:'',//单价 单位
                mobile:'',//手机号
                weight:'',//数量
                weightUnit:'',//数量单位
                userLevelName:'',//QT 等级
                userTypeName:'',//客商
                showUserName:'',
                productTypeName:'',
                userRealAuthFlag:'',
                showSecondInfo:'',//中期果
                viewNum:'',//浏览次数
                phoneNum:'',//联系次数
                saveFlag:'',//是否收藏
                userId:'',
                supplyInfo:{
                },
                imgRT:"",
              },
              detailUrl:[],
              dataId: '',
              title:'',
              items:null,
              pp:false,

            publishViewList:[],
            pageCount:1,
            pageNumber:1,
            styleCal:""
          }
      },
    computed:{
      save_cl(){
          return this.dataDetail.saveFlag?"已收藏":'收藏'
      }
    },
    methods:{
      originalPic(index){

          this.$router.push({name:"picShow", query:{index:index}});
      },
      _initScroll(){
        const self = this;
        self.$nextTick(function () {
          var winHeight = window.innerHeight;
          var mainHeight = winHeight-50;
          self.styleCal='top:42px;height:'+mainHeight+'px';
        })
      },
      routerPerInfo(){
        if(!this.dataDetail.userId)
            return;
        var id = this.dataDetail.userId;
        var type = this.dataDetail.userTypeName;
        var orgId = this.dataDetail.orgId;
//        this.$router.push({path: '/supplyInfo/' + this.dataDetail.userId});
        this.$router.push({name:"personXq",query:{userId:id,orgId:orgId}});
      },
      back(){
        this.$router.go(-1);
      },
      bs(){
          let params = {
              api:"/_node_product/_view_product.apno",
              data:{
                elasticId:this.dataId,
                vieType:"VIEWNUM"
              }
          };
          var that = this;
          this.post(params, fn.number);
      },
      phoneClick(){
        let params = {
          api:"/_node_product/_view_product.apno",
          data:{
            elasticId:this.dataId,
            vieType:"PHONENUM"
          }
        }
        var that = this;
        this.post(params, fn.phone.bind(that));
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
      content(){
          var that= this ;
          let params = {
              api:"/_node_product/_info.apno",
              data:{
                elasticId:this.dataId
              }
          };
          this.post(params, fn.plain.bind(that));
      },
      collect(e,id){//收藏
        var that= this ;
        if(that.dataDetail.saveFlag){
          that.dataDetail.saveFlag = false;
        }
        else{
          that.dataDetail.saveFlag = true;
        }
        var el = document.querySelector(".z-collect");
        var cl = el.classList;
        var flag = cl.contains("activeDel");
        if(flag){
            cl.remove("activeDel");
        }else{
            cl.add("activeDel");
        }
        let params = {
          api:"_node_user/_save_product.apno",
          data:{
            elasticId:this.dataId,
            saveFlag:that.dataDetail.saveFlag
          }
        };
        this.post(params, fn.collectFn.bind(that));
      },
      productTypeSwitch(key){//求购和供应转换
        switch (key) {
          case '供应':
            return buy;
            break;
          case '求购':
            return sell;
            break;
          default:
            return '';
            break;
        }
      },
      userLevelKeySwitch(key){//用户积分转换
        switch (key) {
          case 'QT':
            return QT;
            break;
          case 'BY':
            return BY;
            break;
          case 'HJ':
            return HJ;
            break;
          case 'BJ':
            return BJ;
            break;
          case 'ZS':
            return ZS;
            break;
          case 'DS':
            return DS;
            break;
          default:
            return '';
            break;
        }
      },
      userTypeNameSwitch(key){//用户身份转换
        switch (key) {
          case 'DB':
            return daiban;
            break;
          case 'ZZH':
            return zzHu;
            break;
          case 'LK':
            return lengku;
            break;
          case 'KS':
            return keshang;
            break;
          case 'HZS':
            return hezuoshang;
            break;
          default:
            return '';
            break;
        }
      },
      goodxq(){
//          var id = this.dataId;
//          this.$router.push({name:'goodDetail',query:{id:id}})
      },
      parameter(){
        var that= this ;
        var id = this.dataId;

        let params = {
          api:"/_node_product/_info.apno",
          data:{
            elasticId:id
          }
        };
        this.post(params, fn.parameter.bind(that));
      },
      popup(){
      },
      list(){
        var page =arguments[0] || this.pageNumber;
        let params = {
          api: "/_node_product/_all.apno",
          data: {
            keyWord: "",
            orderType: "DATEDES",
            searchType: "",
            pageNumber: page
          }
        }
//        this.post(params);
      },
    },
    activated(){
       this.dataId = this.$route.params.id;
       this.content();
       this.bs();
//       this.parameter();
       this._initScroll();

      var el = document.querySelector(".page");
      if(el){
        var flag =  el.classList.contains("z-scroll");
        if(flag){
          el.classList.remove("z-scroll");
          el.classList.add("z-scroll-y");
        }else{
          el.classList.add("z-scroll-y");
        }
      }
    },
    components: {
      topBar,
//      suInfo
    }

  }
</script>
