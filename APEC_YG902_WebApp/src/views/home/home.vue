<template>
  <div class="myContainer">
    <div class="content">
      <!--<scroller ref="my_scroller" :on-infinite="infinite">-->
      <div class="z-header">
        <div class="z-carousel">
          <mt-swipe :auto="4000" style="height: 9.45rem" class="cssHeight">
            <template v-for="item in imgCount">
              <mt-swipe-item>
                <a :href="item.url">
                  <img :src="item.content" style="width: 100%;height: 100%;">
                </a>
              </mt-swipe-item>
            </template>
          </mt-swipe>
        </div>
        <div class="z-search clearfix">
          <div class="z-part-one" @click.stop="sNext">
            <img src="../../assets/img/search.png">
            <input type="text" v-model="preSearch"/>
          </div>
          <div class="z-part-two" @click="messageT">
            <div v-if="circle" class="circle"></div>
          </div>

        </div>
      </div>
      <div class="tool">
        <ul class="clearfix">
          <li class="li-com" @click.stop="apple">
            <img src="../../assets/img/supply.png" />
            <p>苹果详情</p>
          </li>
          <li @click.stop="agency" class="li-com">
            <img src="../../assets/img/Agency.png" />
            <p>找代办</p>
          </li>
          <li @click.stop="cold" class="li-com">
            <img src="../../assets/img/ColdStorage.png" />
            <p>找冷库</p>
          </li>
          <li class="li-com" @click.stop="trade">
            <img src="../../assets/img/buy.png" />
            <p>找客商</p>
          </li>
        </ul>
      </div>
      <div class="z-home-gq">
           <div class="z-h-com">
               <img src="../../assets/img/supply.png" @click.stop="buy"/>
                <p>供应信息</p>
           </div>
           <div class="z-h-com">
               <img src="../../assets/img/buy.png" @click.stop="sell"/>
               <p>求购信息</p>
           </div>
      </div>
      <div class="z-banner" @click="signIn">
        <img src="../../assets/img/banner.png" v-if="showpic"/>
        <img :src="bannerSrc">
      </div>
      <div class="z-recommend clearfix">
        <div class="z-title-pic">
          <img src="../../assets/img/recommend.jpg">
        </div>
      </div>
      <div v-if="sh">
        <ul class="z-h-ul">

          <li v-for="item in items"
              :data-id = "item.id"
              :class ="item.id"
              @click="xq"
          >
            <div class="bread-com"
                 :data-path = "item.path"
                 :data-id = "item.id">
              <div class="primaryMain clearfix" :data-id = "item.id">
                <div class="pic-com" :data-id = "item.id">
                  <img :src ="item.img" :data-id = "item.id"/>
                  <!--<div class="z-representative"  :data-id = "item.id">{{item.productTypeName}}</div>-->
                  <!--<img :src = "item.productTypeName" :data-id = "item.id" class="z-representative">-->
                </div>
                <div class="z-info clearfix" :data-id = "item.id">
                  <div class="c-introduce" :data-id = "item.id">
                    <!--<span class="c-sp-com">{{item.fruitname}}</span> <span class="c-sp-com">{{item.pg}}</span> <span class="c-sp-com">{{item.level}}</span>-->
                    <!--<span ></span>-->
                    <div class="z-gq" :data-id = "item.id">
                      <div class="z-gq-t" :data-id = "item.id" :class="{bg:item.bg}">{{item.gq}}</div>
                    </div>
                    <div class="c-sp-com c-skuName" :data-id = "item.id">
                      <p class=" z-c-sku" :data-id = "item.id">
                        {{item.skuName}}
                      </p>
                    </div>
                  </div>
                  <div class="c-pos" :data-id = "item.id">
                    <span class="g-sp-com g-first  g-agency" :data-id = "item.id" :style="{maxWidth:item.wh + 'rem'}">{{item.fruitdate}}</span>
                    <span class="g-sp-com g-first g-weight" :data-id = "item.id">{{item.weight}}</span>
                  </div>
                  <div class="c-level" :data-id = "item.id">
                    <!--<img :src="item.levelImg" class="g-level">-->
                    <!--<img src="../../assets/img/Ancrown@3x.png" class="g-level" :data-id = "item.id">-->
                    <!--<img :src="item.agency" class="z-g-person" :data-id="item.id">-->
                    <div :data-id="item.id" class="z-g-agency">
                      <div class="z-g-d" :data-id = "item.id">
                        {{item.agency}}
                      </div>

                    </div>
                    <div class="g-level" :data-id = "item.id">
                      <div class="z-img" :data-id = "item.id">
                        <img :src="item.levelImg" :data-id = "item.id">
                      </div>
                    </div>

                    <div :data-id="item.id" class="z-g-name">
                      {{item.name}}
                    </div>
                    <div class="g-text" :data-id = "item.id"  :style="{width:item.addreeWh + 'rem'}">
                      <p :data-id = "item.id" style="margin-left: 0.1rem"> {{item.local}}</p>
                    </div>
                  </div>
                  <div class="c-price clearfix" :data-id = "item.id">
                    <span :data-id = "item.id" class="g-price-com-f" :class="{qg:item.qg, gy:item.gy}" v-if="item.indentification == 0 ?true:false">{{item.startAmount}}~{{item.endAmount}}</span>
                    <span :data-id = "item.id" class="g-price-com-f" :class="{qg:item.qg, gy:item.gy}" v-if="item.indentification == 1 ? true:false">{{item.amount}}</span>
                    <span class="g-sp-com g-unit" :data-id = "item.id">{{item.priceUnit}}</span>
                    <span class="g-time g-right" :data-id = "item.id">{{item.showCredateTime}}</span>
                    <span class="g-eye-num g-right" :data-id = "item.id" >{{item.number}}人浏览</span>
                  </div>
                </div>
              </div>
            </div>

          </li>
        </ul>
        <div class="button-tip">
          <span>{{load}}</span>
        </div>
      </div>

      <div class="z-add" v-if="sh2">
        <ul class="clearfix">
          <li class="li-com" @click="wsell">
            <img src="../../assets/img/supply.png">
            <p class="p-text">发布供应</p>
          </li>
          <li class="li-com" @click="wbuy">
            <img src="../../assets/img/buy.png">
            <p class="p-text">发布求购</p>
          </li>
        </ul>
      </div>
      <!--</scroller>-->
    </div>
    <!--积分抽奖window-->
    <div class="myWrapper" v-if="lotteryShow" @click="closeLottery"></div>
    <div class="lotteryWin" v-if="lotteryShow">
      <h3 class="lotteryTit tipshow" v-if="tipFlag">签到成功，领<span>{{score}}</span>积分</h3>
      <h3 class="lotteryTit" v-if="!tipFlag">翻牌赚积分</h3>
      <ul class="lotterMain">
          <li class="card" :id="`cardItem${item.id}`" @click="cardFlipped(item.id)" v-for="item in cardData">
            <div class="front face"></div>
            <div class="back face">
              <span><i>{{score}}</i>积分</span>
            </div>
          </li>
        </ul>
      <i class="closeBtn" @click="closeLottery"></i>
    </div>
  </div>


</template>
<style>
  @import "../../assets/css/home.css";
  @import "../../assets/css/gsyhome.css";
</style>
<script>
  import QT from '../../assets/img/copper@3x.png'//铜牌
  import BY from '../../assets/img/silver@3x.png'//银牌
  import HJ from '../../assets/img/gold@3x.png'//金牌
  import BJ from '../../assets/img/Pt@3x.png'//铂金
  import ZS from '../../assets/img/Diamonds.png'//砖石
  import DS from '../../assets/img/Ancrown@3x.png'//大师

  import QG from './viewComponents/gqimg.vue'
  import c_js from '../../assets/js/common'
  import store from '../../store/store'
  import API from '../../api/api'
  import scoreData from '../../assets/data/score' //积分json
  import {Swipe, SwipeItem, Indicator,MessageBox, Toast} from 'mint-ui'
  const api = new API();
  var fn = {
       data:null,
       cacheData:[],
       record:1,
       recordF:true,
    message: function (data) {
      if(!data.succeed){
        return;
      }
      if (data.data <= 0) {
        this.circle = false;
      }else{
        this.circle = true;
      }
    },
    boost: function (data) {
      if(!data.succeed){
        return;
      }
      if (data.data && data.data.length) {
          console.log(data);
        data.data.forEach((item)=>{
            this.imgCount.push({
                content:item.content,
              url:item.url
            })
        })
      }
    },
    pic: function (data) {
           if(!data.succeed){
               return;
           }
        var len = data.data.length;
         var url = data.data[0].content;
         this.bannerSrc = url;
    },
    search:function (data) {
           if(data.succeed){
             this.preSearch = data.data.keyword;
           }

    },
    auth:function (data) {
      if(!data.succeed){
        this.$router.push({name:'login'});
        return;
      }else{
        var dt = data.data;
        if(dt.checkStatus){
            this.$router.push({name: "psell",query:{Info:"SELL"}});
            return;
        }

        if(!dt.realInfo){
          this.$router.push({name: "validate",query:{id:1}});
          this.$store.state.check = "1";
          return;
        }
        if(!dt.realAuth){
          this.$store.state.check = "2";
          this.$router.push({name: "validate",query:{id:2}});
          return;
        }
      }
    },
    authT:function (data) {
      if(!data.succeed){
        this.$router.push({name:'login'});
        return;
      }else{
        var dt = data.data;
        if(dt.checkStatus){
          this.$router.push({name: "pbuy",query:{Info:"BUY"}});
          return;
        }

        if(!dt.realInfo){
          this.$router.push({name: "validate"});
          this.$store.state.check = "1";
          return;
        }
        if(!dt.realAuth){
          this.$store.state.check = "2";
          this.$router.push({name: "validate"});
          return;
        }
      }
    },
    list:function (data) {
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
        this.items = arr;
      }else{
        fn.cacheData = arr;
      }

      fn.record ++;

      this.switch = true;
//      this.pageNumber ++;

    },
    h:function () {
    },
    uploadScoreCb:function(data){
      var vm=this;
      if(data.succeed){
        console.log("积分上传成功！");
        vm.uploadSign();//调用签到接口
      }
      else{
        console.log("错误消息："+data.errorMsg);
      }
    },
    uploadSignCb:function(data){
      if(data.succeed){
        console.log("签到成功！");
      }
      else{
        console.log("错误消息："+data.errorMsg);
      }
    },
    isSignCb:function(data){
        var vm=this;
        if(data.succeed){
          console.log("data.data.checkStatus:"+data.data.checkStatus);
          if(data.data.checkStatus){//已签到
              Toast("您已签到！");
          }
          else{//未签到
             //弹出抽奖窗口
             vm.lotteryShow=true;
          }
        }
      else{
          if(data.errorCode="600001"){
            vm.$router.replace({
              path: '/login'
            });
          }
          else{
            Toast(data.errorMsg);
          }
        }
    }

  };



  export default{
    data(){
      return {
        //积分抽奖
        lotteryShow:false,//正式为false
        cardAFlipped: false,
        clickFlag:true,//限制只能点击一张牌
        cardData:[
          {id:1},
          {id:2},
          {id:3},
          {id:4},
          {id:5},
          {id:6}
        ],
        score:null,//获得积分
        tipFlag:false,//获得积分提示
        scoreObj:{},

        sh: true,
        sh2: false,
        circle: true,
        showpic: false,
        bannerSrc: '',
        number: "",
        preSearch:'',
        imgCount:[],
        items:null,
        showIs:0,
        pageY:0,
        distance:0,
        num:0,
        pageNumber:1,
        pageCount:1000,
        bheight:0,
//        fontSize:0,
        stopFlag:true,
        recordF:0,
        rFlag:true,
        Time:null,
        del:{},
        load:"数据正在加载中...",
        switch:true

      }
    },
    created(){
      var vm=this;
      window.addEventListener('scroll', this.menu, false);
    },
    mounted(){
      var vm=this;
      vm.scoreObj=scoreData; //积分json
      this.bheight = document.querySelector(".page").clientHeight;
      this.sign();
      this.preS();
      this.boost();
      var container = document.querySelector(".z-h-ul");
//      this.$refs.my_scroller.finishInfinite(true);
//      ce.addEventListener('scroll', this.menu, false);
      container.addEventListener('touchstart', this.down, false);
////      container.addEventListener('touchmove', this.move, false);
//      container.addEventListener('touchend', this.up, false)

      var id = this.$route.query.flag - 0;
      if (!isNaN(id)) {
        if (id == 0) {
          this.sh = true;
          this.sh2 = false;
        }else if(id == 1){
          this.sh = false;
          this.sh2 = true;
        }
      }
    },
    methods: {
      //算积分
      getScore(){
          var vm=this;
          var rand=Math.floor(Math.random()*100+1); //生成一个随机数，范围[1,100]
          console.log("随机数："+rand);
          //遍历数组test
         /* for(var i in vm.scoreObj){
              console.log("积分数组：vm.scoreObj["+i+"][0]"+vm.scoreObj[i][0]);
            }*/
          if(rand<21){
            console.log("这是区间rand<21："+vm.scoreObj[1][0]);
            return vm.scoreObj[1][0]; //1
          }
          else if(rand<41){
            console.log("这是区间rand<41："+vm.scoreObj[2][0]);
            return vm.scoreObj[2][0]; //2
          }
          else if(rand<71){
            console.log("这是区间rand<71："+vm.scoreObj[5][0]);
            return vm.scoreObj[5][0]; //5
          }
          else if(rand<81){
            console.log("这是区间rand<81："+vm.scoreObj[8][0]);
            return vm.scoreObj[8][0]; //8
          }
          else if(rand<91){
            console.log("这是区间rand<91："+vm.scoreObj[10][0]);
            return vm.scoreObj[10][0]; //10
          }
          else{
            console.log("这是区间rand<101："+vm.scoreObj[12][0]);
            return vm.scoreObj[12][0]; //12
          }
        },

      //纸牌初始化
      initialCard(){
        var vm=this;
        vm.clickFlag=true;
        vm.tipFlag=false;
        vm.resetCard(); //纸牌重置样式
      },
      //将所有纸牌重置
      resetCard(){
        var vm=this;
        vm.cardData.forEach(item=>{
        var cardDom=document.getElementById("cardItem"+item.id);
//        console.log("类名："+cardDom.className);
          cardDom.className="card";

      });

      },
      //纸牌翻转点击
      cardFlipped (id) {
        var vm=this;
        if(vm.clickFlag){
          var cardDom=document.getElementById("cardItem"+id);
          //获取积分
          vm.score=vm.getScore();
          console.log("获得积分:"+vm.score);
          //翻转样式
          cardDom.className+=" flipped";
          vm.clickFlag=false;
         setTimeout(()=>{
           vm.tipFlag=true;
         },500);
          //暂时备注
        vm.uploadScore(vm.score);//积分上传


        }
      },
      //判断是否签到
      isSign(){
        var vm=this;
        let params={
          api:"_node_user/_check_sign.apno",
          data:{}
        };
        vm.post(params,fn.isSignCb.bind(this));
      },
      //积分上传
      uploadScore(score){
        var vm=this;
        let params={
          api:"yg-user-service/userpoint/reducePoint.apec",
          data:{
            pointRuleType:"SINGLE_ONE_SIGN_IN",
            pointsChanged:score,
            pointsChangedType:"PLUS",
            remark:"通过签到获得的积分"
          }
        };
        vm.post(params,fn.uploadScoreCb.bind(this));
      },
      uploadSign(){
        var vm=this;
        let params={
          api:"_node_user/_save_sign.apno",
          data:{}
        };
        vm.post(params,fn.uploadSignCb);
      },
      //签到
      signIn(){
//        Toast('功能正在开发中，敬请期待...')
        var vm=this;
        vm.isSign();//判断是否签到

      },
      closeLottery(){
        var vm=this;
        vm.lotteryShow=false;
        vm.initialCard();
      },
      waitingFn(){
          Toast('功能正在开发中，敬请期待...')
      },
      userLevelKeySwitch(key){
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
      buy(){
//        this.$store.state.main = 1;
        this.$store.state.xqInfo = 0;
        this.$store.state.xqInfoF = 0;
        this.$router.push({name: "gq",query:{gq:"GYTYPE"}});
        clearInterval(this.Time);
      },
      sell(){
        this.$store.state.main = 2;
        this.$store.state.xqInfo = 0;
        this.$store.state.xqInfoF = 0;
        this.$router.push({name: "gq",query:{gq:"QGTYPE"}});
        clearInterval(this.Time);
      },
      wbuy(){
//        this.$router.push({name: "pbuy"});
        var that = this;
        let params = {
          api:"/_node_user/_check_pronum.apno",
          data:{}
        }
        clearInterval(this.Time);
        this.post(params, fn.authT.bind(that));
      },
      wsell(){
          var that = this;
        let params = {
            api:"/_node_user/_check_pronum.apno",
            data:{}
        }
        clearInterval(this.Time);
        this.post(params, fn.auth.bind(that));
      },
      list(){
          const self =this;
          if(!self.stopFlag)
              return;
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

          var that = this;
          this.post(params, fn.list.bind(that));
       },
      xq(event){
        clearInterval(this.Time);
          var evt = event || window.event;
          var target = evt.toElement || evt.srcElement;
          var cl = target.classList;
          var flag = cl.contains("g-star");
        var id = target.dataset.id;
          if(flag){
              var path = target.dataset.path;
              var flag = target.dataset.flag;
              if(flag == 0){
                  fn.data[path].starFlag = true;
                  var star = fn.data[path].star + 1;
                  fn.data[path].star = star;
              }else{
                fn.data[path].starFlag = false;
                var star = fn.data[path].star - 1;
                fn.data[path].star = star;
              }

              this.house(id, !flag);

          }else{
              var bd = target.ownerDocument.body.scrollTop;
              this.$store.state.xqInfo = bd;
            this.$router.push({path: '/detail/' + id});
          }
      },
      house(id, flag){
        let params = {
          api:"/_node_user/_save_product.apno",
            data:{
              elasticId:id,
              saveFlag:flag
            }
        }

        this.post(params, fn.h);
      },
      cold(){
        this.$router.push({name: 'cold',query:{name:"COLD"}});
      },
      agency(){
        this.$router.push({name: 'agency',query:{name:"AG"}});
      },
      apple(){

      },
      trade(){
        this.$router.push({name: 'trader',query:{name:"TR"}});
      },
      sNext(){
        var v = this.preSearch;
        this.$store.state.xqInfo = 0;
        this.$store.state.xqInfoF = 0;
        this.$router.push({name: 'search',query:{search:v}});
        clearInterval(this.Time);
      },
      update(){
//        this.messageN();
        var id = this.$route.query.flag - 0;
        if (!isNaN(id)) {
          if (id == 0) {
            this.sh = true;
            this.sh2 = false;
          }else if(id == 1){
            this.sh = false;
            this.sh2 = true;
          }
        }
      },
      messageN(){
          let params = {
            api: "/_node_user/_message.apno",
            }
          this.post(params, fn.message.bind(this));
       },
      sign(){
        let params = {
          api: "/yg-cms-service/cms/articleList.apec",
          data: {
          "category":"HOMEPAGE",
          "channelCode":"HOME_BOOST"
           }
        }
        this.post(params, fn.pic.bind(this));
      },
      boost(){
        let params = {
          api: "/yg-cms-service/cms/articleList.apec",
          data: {
            "category":"HOMEPAGE",
            "channelCode":"HOME_BARNNER"
          }
        }
        this.post(params, fn.boost.bind(this));
      },
       preS(){
         let params = {
           api: "/yg-systemConfig-service/preSearch/getPreSearchInfo.apec",
           data:{
             "searchType":"YZSS"
           }
         }
         this.post(params, fn.search.bind(this));
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
      messageT(event){
        this.$store.state.xqInfo = 0;
        this.$store.state.xqInfoF = 0;
          var evt = event || window.event;
          var target = evt.toElement || evt.srcElement;
          var flag = target.classList.contains("circle");
          var child = target.children;
          this.$router.push({ name:'messageList'})
      },
      down(evt){
       var e = evt || window.event;
//       this.pageY = e.changedTouches ? e.changedTouches[0].pageY : e.pageY;
      },
      move(evt){
        var e = evt || window.event;
        var pageY = e.changedTouches ? e.changedTouches[0].pageY : e.pageY;
      },
      up(evt){
        var e = evt || window.event;
        var pageY = e.changedTouches ? e.changedTouches[0].pageY : e.pageY;
        this.distance +=( this.pageY- pageY);
        if(this.pageY - pageY == 0){
          var target = e.toElement || evt.srcElement;
          var id = target.dataset.id;
          this.$router.push({path: '/detail/' + id});
        }else{
            var ele = document.querySelector(".bread-com");
            var clientH = ele.clientHeight;
//            var height = clientH*this.num;
             var height = 10000;
//            if(height < this.distance){
//                this.distance = 0;
//                this.pageY = 0;
//                if( this.pageCount <= this.pageNumber){
//                  Toast('数据加载完...')
//                  return;
//                }
//              this.pageNumber ++;
//                 this.list();
//            }
        }
      },
      preLoad(){
          var self = this;
//          if(fn.recordF){
//            this.Time = setInterval(function () {
//              if(self.pageCount >= self.pageNumber){
//                self.list();
//              }else{
//                clearInterval(self.Time);
//              }
//              if(self.pageCount <= fn.record){
//                fn.recordF = false;
//                return;
//              }
//            }, 1500)
//            this.$store.state.Time = this.Time;
//
//          }
        fn.recordF = false;
        setTimeout(function () {
              if(self.pageCount >= self.pageNumber){
                self.pageNumber ++;
                self.list(self.pageNumber);
              }
            },1500);
      },
      menu(evt){
       var e = evt || window.evt;
       var el = document.querySelector(".content");
       if(el){
         var target = e.target || e.srcElement;
         var offsetH = target.body.scrollTop;
         var sHeight = target.body.scrollHeight;
         if(sHeight - offsetH - this.bheight < 70 && this.switch){
           var len = fn.cacheData.length;
           if(len != 0){
                 this.switch = false;
                  var that = this;
                  that.items = that.items.concat(fn.cacheData);
//                  Array.prototype.push.apply(that.items, fn.cacheData);
                  fn.cacheData = [];
             if(this.pageCount == this.pageNumber){
               this.switch = false;
               this.load = "数据加载完..."
               return
             }else{
               this.preLoad();
             }
           }
         }
       }

      },
      GetPointList(){//获取信息
        this.list();
      },
      infinite (done) {
        if (!this.isActivated) return done(true);
//        const self = this;
//        self.currentPageNo++;
//        self.busy = true;
        setTimeout(()=>{
          this.list();
          done(true);
        }, 1500);
      },
      refresh_infinite_dt(fn){
          this.list();
      },
    },
    activated(){

      this.del = {};
      this.isActivated = true;
      this.messageN();
      var sH =  this.$store.state.xqInfo;
      if(sH){
        window.scrollTo(0,sH);
      }else{
        this.list(1);
        fn.recordF = true;
        this.preLoad();
        this.pageNumber = 1;
        fn.record = 1;

        this.items = null;
        fn.cacheData = [];
      }

    },
    deactivated () {
      this.isActivated = false
    },
    computed: {
      test(){
        var r = this.$route;
      }
    },
    watch: {
      "$route": "update",
      pageNumber:function () {
//        this.stopFlag = false;
      }
    }
  }
</script>
