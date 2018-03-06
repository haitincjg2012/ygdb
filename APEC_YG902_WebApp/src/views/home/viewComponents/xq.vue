<template>
  <div style="height: 100% ;overflow-y:auto;">
    <!--<top-bar title="供应详情"></top-bar>-->
    <div class="z-s-header-O">
      <div class="z-title">{{title}}</div>
      <div class="return" @click="back">
        <img src="../../../assets/img/ret.png">
      </div>
      <div class="c-xq-warn" @click="warn">
          举报
      </div>
    </div>
    <my-scroll class="c-xq-goods" :data="itemG?itemG:[]" :pullup="pullup" ref="scrollBar" @scrollToEnd="loadMore">
      <div>
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
      <div class="c-goods-pz" v-html="dataDetail.classify"></div>
      <div class="z-area clearfix">
        <!--<img class="pos-pic" src="../../../assets/img/pos.png">-->
        <span class="sp-two">{{dataDetail.address}}</span>
        <span class="sp-three-com">{{dataDetail.phoneNum}} 人联系</span>
        <span class="sp-three-com">{{dataDetail.viewNum}} 次浏览</span>

      </div>
    </div>
    <div class="c-goods-price">
      <span class="sp-one" v-if="dataDetail.name == '供应'">{{dataDetail.amount}}</span>
      <span class="sp-one" v-if="dataDetail.name == '求购'">{{dataDetail.startAmount}}-{{dataDetail.endAmount}}</span>
      <span class="sp-com">{{dataDetail.priceUnit}}</span>
      <span class="z-g-j">{{dataDetail.weight}}{{dataDetail.weightUnit}}</span>
    </div>
     <!--<div v-html="dataDetailSpecifications">-->
     <div class="c-specification" v-html="dataDetail.specification">
       <!--<div class="c-detai-blank"></div>-->
       <!--<h4 class="c-specification-title">详情规格</h4>-->
       <!--<p class="c-specification-text"></p>-->
     </div>
     <div class="c-describe" v-html="dataDetail.describe"></div>
     <div class="c-detai-blank"></div>
    <div class="z-p-x">
      <div @click.stop="routerPerInfo($event)" class="peoson-info clearfix">
        <div class="z-img-person">
          <img :src="dataDetail.imgRT" data-flag="protrait" :data-src ="dataDetail.imgUrl">
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
     <div class="c-detai-blank"></div>
    <div class="z-my-publishes">
      <div class="z-my-publish-title">
        <h6>他的发布</h6>
      </div>
        <!--//临时的切换-->
        <div>
          <ul>
            <li :is="item.ss" v-for="item in itemG"
                :item = "item"
                v-on:receive="switchT"
                >
            </li>
          </ul>
        </div>
    </div>
    <!--<div class="z-cs">-->
      <!--&lt;!&ndash;<div class="xq" @click="goodxq">&ndash;&gt;-->
      <!--<div class="xq">-->
        <!--<span class="sp-f">商品详情:</span>-->
        <!--&lt;!&ndash;<img class="z-d-o" src="../../../assets/img/more-1.png">&ndash;&gt;-->
      <!--</div>-->
      <!--<table class="z-table">-->
        <!--<tr v-for="item in items"-->
        <!--&gt;-->
          <!--<td v-for="key in item">{{key}}</td>-->
          <!--&lt;!&ndash;<td class="z-name">{{item.key}}</td>&ndash;&gt;-->
          <!--&lt;!&ndash;<td class="z-key">{{item.value}}</td>&ndash;&gt;-->
        <!--</tr>-->
      <!--</table>-->
      <!--&lt;!&ndash;<div class="z-f-s">&ndash;&gt;-->
          <!--&lt;!&ndash;<p>描述:</p>&ndash;&gt;-->
          <!--&lt;!&ndash;<p>{{dataDetail.remark}}</p>&ndash;&gt;-->
      <!--&lt;!&ndash;</div>&ndash;&gt;-->
    <!--</div>-->
      </div>
    </my-scroll>
    <!--<div class="footer">-->
    <my-phone class="footer" :mobile="dataDetail.mobile" :attentionFlag.sync="dataDetail.saveFlag" @phone="phoneClick" @attention="collect" @share="shareT" mark="1"></my-phone>
    <shareWx :wxflag="Wxflag" @getWx="getWx"></shareWx>
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


  import WX from '../../../components/wx.vue'  //微信分享功能
  import phoneC from '../../businessV/phone.vue' //打电话组件
  import defaultIcon from "../../../assets/img/defaultForm.png"

  import default_1 from '../../../assets/img/xqimg1.png'//默认的详情轮播图
  import scroll from '../../../components/scroll/scrollbg.vue'//分页
//  import scroll from '../../../components/scroll/scroll.vue'

  import default_2 from '../../../assets/img/xqimg2.png'//默认的详情轮播图
  import default_3 from '../../../assets/img/xqimg3.png'//默认的详情轮播图

  import sell from '../../../assets/img/sell-1.png'//供应
  import buy from '../../../assets/img/logo-1.png'//求购

  import Aur from '../../../assets/img/AgencyTipsR.png'//是否认证
  import shareWx from '../../../components/shareWx.vue' //微信引导图

  import IMG from '../../../components/gqimg.vue'

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
      plain:function (type,data) {
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

//        self.dataDetail.userLevelName = self.userLevelKeySwitch(dt.userLevelName);
        self.dataDetail.userLevelName = IMG.methods.userLevel(dt.userLevelName);

        self.dataDetail.userTypeName = dt.userTypeName;
        self.dataDetail.showUserName = dt.showUserName;
        var name = dt.showUserName == ""?"商品详情":dt.showUserName;
        WX.wx("果来乐" + name,undefined, self.getWx);//分享功能
//        self.dataDetail.productTypeName = self.productTypeSwitch(dt.productTypeName);
        self.dataDetail.productTypeName = dt.productTypeName;
        self.dataDetail.userShow = dt.userRealAuthFlag;
        self.dataDetail.userReal = dt.userRealAuthFlag?"实名认证":'';

        var html = "";//苹果品种的
        if(dt.productTags){
          dt.productTags.forEach(function (recommands) {
            html +="<span class='c-goods-pz-text g-second'>"+recommands.tagName + "</span>";
          });
        }
        dt.showSecondInfo.forEach(function (current) {
          html += "<span class='c-goods-pz-text'>" +current +"</span>"
        });
        self.dataDetail.classify = html;
        //商品的描述
        var htmlDes = "";
        if(dt.remark){
          htmlDes = "<div class='c-detai-blank'></div><h4 class='c-describe-title'>产品描述</h4><p class='c-describe-text'>" +dt.remark +"</p>";
        }

        self.dataDetail.describe = htmlDes;

        self.dataDetail.viewNum = dt.viewNum-0?dt.viewNum:0;
        self.dataDetail.phoneNum = dt.phoneNum-0?dt.phoneNum:0;
        self.dataDetail.saveFlag = dt.saveFlag;

        self.dataDetail.userId = dt.userId;
        self.dataDetail.imgRT = (dt.imgUrl || defaultIcon) + "?x-oss-process=style/_head";
        self.dataDetail.imgUrl = dt.imgUrl;

        if(dt.productImages.length){
            var arr = [];
            var arrImg = dt.productImages;
            for(var key in arrImg){
                var obj = {};
                obj.imageUrl = arrImg[key].imageUrl+"?x-oss-process=style/_detail";
                arr.push(obj);
            }
          self.detailUrl = arr;
          this.$store.state.xqImgArr = dt.productImages;
        }

        var tables = dt.productAttrs;

        var arr = [];

        self.dataDetail.name = dt.productTypeName;

        this.items = fn.parameter(tables);
        this.$store.state.productImages = dt.productImages;
        this.$store.state.productTypeName = dt.productTypeName;
        this.title = dt.productTypeName + "商品";

        this.orgId = dt.orgId;

        if(!self.xqtype){
            this.gqlist(type);
            return;
        }
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
      format(type,data){
        if(!data.succeed){
          return;
        }

        if(this.xqtype != type){
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
          obj.Flag = "xq";
          obj.skuName = current.skuName;
          obj.showCredateTime = current.showCredateTime;
          var id = current.id;
          if(that.treeRecord[id]){
              return;
          }
          that.treeRecord[id] = 1;
          obj.id = id;
//          obj.levelImg = that.userLevelKeySwitch(current.userLevelName);
          obj.gq = current.productTypeName;
//          obj.productTypeName = QG.methods.img(current.productTypeName);
          obj.img = current.firstImageUrl + "?x-oss-process=style/_list";
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

//          var goodTime = current.showSecondInfo;
//          var text = goodTime.join(" ");
//          obj.fruitdate = text;
          //品种的种类
          var html = "";
          if(current.productTags){
            current.productTags.forEach(function (recommands) {
              html +="<span class='g-sp-com g-first g-second'>"+recommands.tagName + "</span>";
            });
          }
          current.showSecondInfo.forEach(function (fruitC) {
            html +="<span class='g-sp-com g-first ' data-id="+current.id+">"+fruitC + "</span>";
          });
          obj.fruitdate = html;

          var Identification = current.productTypeName;
          var hprice = "";
          if(Identification == "求购"){
            hprice = "<span data-id=" + current.id +" class='g-price-com-f gy'>&yen;" + current.startAmount + "~" +current.endAmount + "</span><span class='g-unit'  data-id=" + current.id + ">" + current.priceUnit +"</span>";
            obj.price = hprice;
            obj.bg = true;
          }else{
            hprice = "<span data-id=" + current.id +" class='g-price-com-f gy'>&yen;" + current.amount + "</span><span class='g-unit'  data-id=" + current.id + ">" + current.priceUnit +"</span>";
            obj.price = hprice;
          }

          //重量
          obj.weight = "<span class='g-unit'>" + current.weight + "&nbsp;" + current.weightUnit + "</span>";

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
//          if(that.del.hasOwnProperty(id)){
//            return;
//          }

//          that.del[id] = 0;
          arr.push(obj);
        });

          if(rows.length > 0){
              if(data.data.currentNo == 1){
                this.itemG = [].concat(arr);
              }else{
                this.itemG = this.itemG.concat(arr);
              }
          }

//        if(this.pageNumber == 1){
//          this.publishViewList = arr;
//        }else{
//          fn.cacheData = arr;
//        }
      },
  }
  export default{
      created(){
        this.$store.state.xqF = false;
      },
      data(){
          return{
              browse:100,
              mobile:0,
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
              detailUrl:null,
              dataId: '',
              title:'',
              items:null,
              pp:false,

            publishViewList:[],
            pageCount:1,
            pageNumber:1,
            styleCal:"",
            arr:[],//滑动的数组
            pullup:true,
            orgId:"",//组织id
            itemG:null,//数组
            treeRecord:{},

            xqtype:"",//切换 不切换两种问题
            Wxflag:false,//控制引导图
          }
      },
    computed:{
      save_cl(){
          return this.dataDetail.saveFlag?"已收藏":'收藏'
      }
    },
    methods:{
      getWx(){
        this.Wxflag = false;
      },
      shareT(){
        this.Wxflag = true;
      },
      warn(){
        //举报的
        var name = localStorage.name;
        if(name){
          var self = this;
          this.$router.push({name:"warn",query:{
            articalId:self.$route.query.id,
            articalName:self.dataDetail.showUserName,
            realm:self.dataDetail.productTypeName,
          }})
        }else{
          Toast("请您先登录，再进行举报!");
        }
      },
      switchT(id){
//        this.itemG = null;
        this.$refs.scrollBar.scrollTo(0, 0);
        this.$refs.scrollBar.init();
        this.treeRecord = {};
        this.content(id);
        this.xqtype = "tab";
      },
      originalPic(index){
          this.$router.push({name:"picShow", query:{index:index}});
      },
      loadMore(){
          var that = this;

        if(that.pageCount > that.pageNumber){
          that.pageNumber ++;
          that.gqlist(that.xqtype);
        }
      },
      gqlist(type){
        var pg = this.pageNumber;
        var that = this;
        let params = {
          api:"/_node_user/_product_list.apno",
          data:{
            orgId:that.orgId,
            pageNumber: pg,
          }
        }
        this.post(params,fn.format.bind(this, type));
      },
      routerPerInfo(event){
          var el = event.toElement || event.srcElement;
          var flag = el.dataset.flag;
          if(flag == "protrait"){
            var src = el.dataset.src;
            if(src){
              this.$router.push({name:"pictureOriginal",query:{src:src}});
              return;
            }
          }
        if(!this.dataDetail.userId)
            return;
        var id = this.dataDetail.userId;
        var type = this.dataDetail.userTypeName;
        var orgId = this.dataDetail.orgId;

        this.$router.push({name:"personXq",query:{userId:id,orgId:orgId}});
      },
      back(){
        var wxF = localStorage.wx;
        if(wxF){
          this.$router.push({name:"home"});
          localStorage.removeItem("wx");
        }else{
          this.$router.go(-1);
        }

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
      content(id){
          var that= this ;
          let params = {
              api:"/_node_product/_info.apno",
              data:{
                elasticId:id,
              }
          };
          this.post(params, fn.plain.bind(that,that.xqtype));
      },
      collect(flag){//收藏
        var that= this ;
        let params = {
          api:"_node_user/_save_product.apno",
          data:{
            elasticId:that.dataId,
//            saveFlag:that.dataDetail.saveFlag
            saveFlag:flag
          }
        };
        this.dataDetail.saveFlag = flag;
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
    },
    activated(){

       var id = this.$route.query.id;
       this.attentionFlag = false;//默认的情况
       this.dataId = id;
      this.xqtype = "";
       this.content(id);


       this.bs();
       this.detailUrl = null;
       this.itemG = null;
       this.treeRecord = {};
       this.$refs.scrollBar.init();

    },
    components: {
      topBar,
      'my-scroll':scroll,
      'my-phone':phoneC,
      shareWx
//      suInfo
    }

  }
</script>
