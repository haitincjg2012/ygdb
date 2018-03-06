<template>
    <div class="myContainer c-home-page">
      <div class="c-blankbg"></div>
      <!--<my-scroll class="c-home-Scroll" :data="scrollData" :pullup="pullup" @scrollToEnd="loadMore" ref="wrapper">-->
        <div class="z-content c-home-content">
          <div class="z-header-h">
            <div class="z-carousel">
              <mt-swipe :auto="4000" style="height: 7.35rem" class="cssHeight">
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
              <div class="z-search-position" @click="province">
                 {{positionS}}
              </div>
              <div class="z-search-down">
              </div>
              <div class="z-part-one" @click.stop="sNext">
                <div class="z-part-one-bg"></div>
                <div class="z-part-one-content">
                  <img src="../../assets/img/search.png">
                  <input type="text" v-model="preSearch"/>
                </div>
              </div>
              <div class="z-part-two" @click="messageT">
                <div v-if="circle" class="circle"></div>
              </div>
            </div>
          </div>
          <div class="c-arc">
            <img src="../../assets/img/arc.png">
          </div>
          <div class="tool">
            <ul class="clearfix">
              <li class="li-com" @click.stop="apple">
                <img src="../../assets/img/xq.png"/>
                <p>苹果行情</p>
              </li>
              <li @click.stop="sell" class="li-com">
                <img src="../../assets/img/qg.png"/>
                <p>求购信息</p>
              </li>
              <li @click.stop="buy" class="li-com">
                <img src="../../assets/img/gy.png"/>
                <p>供应信息</p>
              </li>
              <li class="li-com" @click.stop="fruit">
                <img src="../../assets/img/fruitS.png"/>
                <p>果圈</p>
              </li>
               <!--<li @click.stop="agency" class="li-com">-->
                <!--<img src="../../assets/img/db.png"/>-->
                <!--<p>找代办</p>-->
              <!--</li>-->
              <!--<li @click.stop="cold" class="li-com">-->
                <!--<img src="../../assets/img/coldS.png"/>-->
                <!--<p>找冷库</p>-->
              <!--</li>-->
              <!--<li class="li-com" @click.stop="trade">-->
                <!--<img src="../../assets/img/ks.png"/>-->
                <!--<p>找客商</p>-->
              <!--</li>-->
            </ul>
          </div>
          <div class="c-news">
            <div class="c-news-logo"></div>
            <div class="c-news-msg">
                 <my-horse class="c-news-rotate"></my-horse>
            </div>
          </div>
          <!--<div class="c-sign">-->
            <!--<div class="c-sign-title">-->
              <!--<img src="../../assets/img/date.png">-->
              <!--<span class="c-sign-sp">珍惜今天的拥有,明天才会富有!</span>-->
            <!--</div>-->
            <!--<div class="c-sign-v">-->
              <!--<div class="v-line"></div>-->
            <!--</div>-->
            <!--<div class="c-sign-img" @click="signIn">-->
              <!--<img src="../../assets/img/sign.png">-->
            <!--</div>-->
          <!--</div>-->
          <div class="c-sign" v-if="signFlag">
             <div class="c-sign-content">
                <div class="c-sign-c-img">
                  <img src="../../assets/img/sign.png">
                </div>
                <div class="c-sign-c-text">
                  <div class="c-sign-c-content">
                    <p class="c-sign-pcom c-sign-cc-first">每日签到</p>
                    <p class="c-sign-pcom c-sign-cc-second">天天签到，天天领积分</p>
                    <!--<p class="c-sign-pcom c-sign-cc-three">本周签到3天</p>-->
                  </div>
                </div>
                <div class="c-sign-c-btn">
                   <div class="c-sign-c-btn-text" @click="signIn">马上签到</div>
                </div>
             </div>
          </div>
          <div class="c-home-greyBlock"></div>
          <my-guess mark="1"></my-guess>
          <my-activeP class="c-home-active">
          </my-activeP>
          <div class="c-z-c-tunef-blank"></div>
          <!--<div class="z-home-gq">-->
            <!--<div class="z-h-com">-->
              <!--<img src="../../assets/img/gy.png" @click.stop="buy"/>-->
            <!--</div>-->
            <!--<div class="z-h-com c-right">-->
              <!--<img src="../../assets/img/qg.png" @click.stop="sell"/>-->
            <!--</div>-->
          <!--</div>-->
          <!--<div class="z-banner" @click="signIn">-->
          <!--<img src="../../assets/img/banner.png" v-if="showpic"/>-->
          <!--<img :src="bannerSrc">-->
          <!--</div>-->
          <!--<div class="c-h-news">-->
            <!--<h4 class="c-news-title">市场详情</h4>-->
            <!--<div class="c-news-more" @click="apple">-->
              <!--<span class="c-sp">更多</span>-->
              <!--<img src="../../assets/img/back.png" class="c-img">-->
            <!--</div>-->
          <!--</div>-->
          <!--<div>-->
          <!--<div v-if="newsShowFlag">-->
            <!--<my-scrolltip ref="newsTip"></my-scrolltip>-->
          <!--</div>-->
          <!--<my-newslist :listFilter="newsData" :loadMflag="loadMflag" class="c-newslist"></my-newslist>-->
          <!--<div class="greyBlock"></div>-->
          <!--调果龙虎榜-->
          <!--<div class="c-z-c-tunef-blank"></div>-->
          <!--<div class="c-z-c-tunef" @click="tuneFruit">-->
             <!--<img src="../../assets/img/tuneFruit.png">-->
          <!--</div>-->
          <div class="c-gq-info">
            <h4 class="c-gq-title">供求信息</h4>
          </div>
          <div>
            <ul class="z-h-ul">
              <li v-for="item in items"
                  :data-id="item.id"
                  :class="item.id"
                  @click="xq($event,item)"
              >
                <div class="bread-com"
                     :data-path="item.path"
                     :data-id="item.id">
                  <div class="primaryMain clearfix" :data-id="item.id">
                    <div class="pic-com" :data-id="item.id">
                      <img :src="item.img" :data-id="item.id"/>
                    </div>
                    <div class="z-info clearfix" :data-id="item.id">
                      <div class="c-introduce" :data-id="item.id">
                        <div class="z-gq" :data-id="item.id">
                          <div class="z-gq-t" :data-id="item.id" :class="{bg:item.bg}">{{item.gq}}</div>
                        </div>
                        <div class="c-sp-com c-skuName" :data-id="item.id">
                          <p class=" z-c-sku" :data-id="item.id">
                            {{item.skuName}}
                          </p>
                        </div>
                      </div>
                      <div class="c-pos" :data-id="item.id" v-html="item.fruitdate">
                        <!--<span class="g-sp-com g-first  g-agency" :data-id="item.id" :style="{maxWidth:item.wh + 'rem'}">{{item.fruitdate}}</span>-->
                        <!--<span class="g-sp-com g-first g-weight" :data-id="item.id">{{item.weight}}</span>-->
                      </div>
                      <div class="c-level" :data-id="item.id">
                          <div class="c-l-price" v-html="item.price"></div>
                          <div class="c-l-weight" v-html="item.weight"></div>
                        <!--<div :data-id="item.id" class="z-g-agency">-->
                          <!--<div class="z-g-d" :data-id="item.id">-->
                            <!--{{item.agency}}-->
                          <!--</div>-->
                        <!--</div>-->
                        <!--<div class="g-level" :data-id="item.id">-->
                          <!--<div class="z-img" :data-id="item.id">-->
                            <!--<img :src="item.levelImg" :data-id="item.id">-->
                          <!--</div>-->
                        <!--</div>-->

                        <!--<div :data-id="item.id" class="z-g-name">-->
                          <!--{{item.name}}-->
                        <!--</div>-->
                        <!--<div class="g-text" :data-id="item.id" :style="{width:item.addreeWh + 'rem'}">-->
                          <!--<p :data-id="item.id" style="margin-left: 0.1rem"> {{item.local}}</p>-->
                        <!--</div>-->
                      </div>
                      <div class="c-location clearfix" :data-id="item.id">
                        <div class="c-loc-addr">
                          {{item.local}}
                        </div>
                        <div class="c-loc-time">
                          {{item.showCredateTime}}
                        </div>
                        <div class="c-loc-view">
                          {{item.number}}人浏览
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </li>
            </ul>
            <my-loading ref="loading"></my-loading>
          </div>
        </div>
      <!--</my-scroll>-->
      <!--积分抽奖window-->
      <div class="myWrapper" v-show="lotteryShow" @click="closeLottery"></div>
      <div class="lotteryWin" v-show="lotteryShow">
        <h3 class="lotteryTit tipshow" v-if="tipFlag">签到成功，领<span>{{score}}</span>积分</h3>
        <h3 class="lotteryTit" v-if="!tipFlag">翻牌赚积分</h3>
        <ul class="lotterMain">
          <li class="card" :id="`cardItem${item.id}`" @click="cardFlipped($event,item)" v-for="item in cardData">
          <!--<li class="card" :id="'cardItem' + item.id" @click="cardFlipped(item.id)" v-for="item in cardData">-->
            <div class="front face"></div>
            <div class="back face">
              <span><i>{{item.score}}</i>积分</span>
            </div>
          </li>
        </ul>
        <i class="closeBtn" @click="closeLottery"></i>
      </div>
      <my-cue :cueFlag.sync="cueData" @switch="tip"></my-cue>
    </div>
</template>
<style>
  @import "../../assets/css/home.css";
  @import "../../assets/css/gsyhome.css";
  @import "../../assets/css/news.css";
</style>
<script>

  import QG from '../../components/gqimg.vue'
  import newslist from './viewComponents/newslist.vue'//新闻列表
  import scrollTip from '@/components/downLoadAnimal' //加载提示

  import guess from './viewComponents/guess.vue'//果价猜猜猜
  import horse from './viewComponents/newsRotate.vue'//新闻的轮播滚动
  import activePerson from '../businessV/active.vue'//分享达人

//  import scroll from '@/components/scroll/scroll'//分页
  import scroll from '@/components/scroll/scrollbg'//分页
  import c_js from '../../assets/js/common'
  import store from '../../store/store'
  import API from '../../api/api'
  import scoreData from '../../assets/data/score' //积分json
//  import scrollS from "../../components/loadingAnimation.vue"
  import scrollS from "../../components/loading.vue"
  import cue from '../businessV/cue.vue' //客户认证的提示

  import {Swipe, SwipeItem, MessageBox, Toast} from 'mint-ui'
  const api = new API();

  var fn = {
       data:null,
       cacheData:[],//预加载数据
       finishFlag:false,//数据加载完的标志位
       record:1,
       recordF:true,
    message: function (data) {
      if(!data.succeed){
        return;
      }

      this.circle = data.data.messageCount > 0 || data.data.praiseCount > 0  || data.data.replyCount > 0 ?true:false;
    },
    boost: function (data) {
      if(!data.succeed){
        return;
      }
      if (data.data && data.data.length) {
        this.imgCount = [];
        data.data.forEach((item, index)=>{
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
    list:function (data) {
      var length = arguments.length;
      if(length > 1){
          var data = arguments[1];
      }
      if(!data.succeed){
        Toast(data.errorCode);
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
//            obj.levelImg = that.userLevelKeySwitch(current.userLevelName);
            obj.gq = current.productTypeName;
            obj.productTypeName = QG.methods.img(current.productTypeName);
            obj.img = current.firstImageUrl+"?x-oss-process=style/_list";
            //地址的截取
            var pos = current.address.indexOf("镇");
            var posL = current.address.indexOf("区");

            if(pos > -1){
              obj.local = current.address.substring(0, pos + 1);
            }else if(posL > -1){
              obj.local = current.address.substring(0, posL + 1);
            }else
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

            //品种的种类
            var html = "";
            if(current.productTags){
              current.productTags.forEach(function (recommands) {
                html +="<span class='g-sp-com g-first g-second' data-id="+current.id+">"+recommands.tagName + "</span>";
              });
            }
            current.showSecondInfo.forEach(function (fruitC) {
              html +="<span class='g-sp-com g-first ' data-id="+current.id+">"+fruitC + "</span>";
            });

//            var goodTime = current.showSecondInfo;
//            var text = goodTime.join(" ");
//            obj.fruitdate = text;//
            obj.fruitdate = html;

            var Identification = current.productTypeName;
            //求购和供应的价格
            var hprice = "";
              if(Identification == "求购"){
                  hprice = "<span data-id=" + current.id +" class='g-price-com-f gy'>&yen;" + current.startAmount + "~" +current.endAmount + "</span><span class='g-unit'  data-id=" + current.id + ">" + current.priceUnit +"</span>";
                  obj.price = hprice;
//                 obj.endAmount = current.endAmount+"";
//                 len += obj.endAmount.length;
//                 obj.startAmount = current.startAmount+"";
//                len += obj.startAmount.length;
                  obj.bg = true;
//                 var n = (250 - (len + 6) * 10)/20;
              }else{
                hprice = "<span data-id=" + current.id +" class='g-price-com-f gy'>&yen;" + current.amount + "</span><span class='g-unit'  data-id=" + current.id + ">" + current.priceUnit +"</span>";
                obj.price = hprice;
//                obj.amount = current.amount+"";
//                len += obj.amount.length;
//                var n = (230 - (len + 1) * 10)/20;
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
            obj.addreeWh = (201 - ((obj.name+"").length + obj.agency.length) * 12 + lF * 6 + lt*8)/20;
            if(that.del.hasOwnProperty(id)){
                return;
            }

            that.del[id] = 0;
            arr.push(obj);

          });

      if(length == 2){
        this.items = arr;
        this.mergeScroll(arr);
        this.preLoad();//预加载
      }else{
        fn.cacheData = arr;
      }

      this.switch = true;

      if(data.data.pageCount == data.data.currentNo){
        //数据加载完的标志位和cacheData一起使用
        fn.finishFlag = true;
      }
      fn.record ++;

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
          if(data.data.checkStatus){//已签到
//              Toast("您已签到！");
              vm.signFlag=false;
          }
          else{//未签到
             //弹出抽奖窗口
            vm.signFlag=true;
//             vm.lotteryShow=true;

          }
        }
      else{
          Toast(data.errorMsg);
//          if(data.errorCode="600001"){
////            vm.$router.replace({
////              path: '/login'
////            });
//          }
//          else{
//
//          }
        }
    },
    hasSignCb:function(data){
        var vm=this;
        if(data.succeed){
          if(data.data.checkStatus){//已签到
              Toast("您已签到！");
              vm.signFlag=false;
          }
          else{//未签到
             //弹出抽奖窗口
            vm.signFlag=true;
            vm.lotteryShow=true;
//          vm.lotteryShow=true;
          }
        }
      else{
          Toast(data.errorMsg);
//          if(data.errorCode="600001"){
////            vm.$router.replace({
////              path: '/login'
////            });
//          }
//          else{
//
//          }
        }
    }

  };

  export default{
    data(){
      return {
        //新闻列表
//        newsData:[],by gsy
        scrollFlag:false,//每次路由进入该页面的时候可以，操作scroll事件，离开的时候不能操作该页面的scroll
        scrollPos:0,//悬浮的位置
        preloadFlag:false,//是否需要预加载,为扩展优化列表使用
        switch:false,//类节流函数的开关



        scrollData:null,
        newsData:null,
        newsShowFlag:false, //显示新闻加载tip
        pageNum:1,//新闻页码
        pageSize:3,//新闻页容量
        loadMflag:true,//控制是否显示“无更多”提示
        //积分抽奖
        lotteryShow:false,//正式为false
//         lotteryShow:true,//测试环境
        cardAFlipped: false,
        clickFlag:true,//限制只能点击一张牌
        cardData:[
          {id:1,
            score:0,},
          {id:2,
            score:0,},
          {id:3,
            score:0,},
          {id:4,
            score:0,},
          {id:5,
            score:0,},
          {id:6,
            score:0,}
        ],
        score:null,//获得积分
        tipFlag:false,//获得积分提示
        scoreObj:{},
        circle: true,
        showpic: false,
        bannerSrc: '',
        number: "",
        preSearch:'',
        imgCount:null,//图片数组为空
        items:null,
        showIs:0,
        pageY:0,
        distance:0,
        num:0,
        pageNumber:1,
        pageCount:1000,
        bheight:0,
        stopFlag:true,
        recordF:0,
        rFlag:true,
        Time:null,
        del:{},
        //上拉加载更多
        pullup:true,
        load:"数据正在加载中...",

        arrive:false,//到底底部箭头切换
        loadFlag:false,//箭头切换以后，加载数据
        showVisableF:false,//默认显示的
        mFlag:true,//默认是无遮罩的
        signFlag:true,//签到的标志位

        home:null,//home监控节点
        homeActiveF:true,//默认的情况是为空

        cueData:true,//提示语的出现情况

        positionS:this.$store.state.homeProvince,//默认
      }
    },
    mounted(){
      var self = this;
//      self.home = new self.Observer("home");//接口监控系统

      self.scoreObj=scoreData; //积分json
      this.bheight = document.querySelector(".page").clientHeight;
      this.sign();
      this.preS();
      this.boost();

      window.addEventListener('scroll', self.menu, false)
        //解决卡顿的问题
//        window.addEventListener("pageshow",function () {
//          self.$refs.wrapper.init();
//        }, false);
    },
    methods: {
        province(){
            //选择全国的城市
          var sign = "home";
          this.$router.push({name: 'province',query:{
              sign:sign
          }});
        },
        tip(flag){
            this.cueData = flag;
        },
        getTip(){
          var name = localStorage.name;
          if(name){
            localStorage.tipRecord = 1;
            var self = this;
            var params={
              api:"yg-user-service/user/checkFullUserInfo.apec",
              data:{}
            }

            this.post(params, function (data) {
              if(data.succeed){
                self.cueData = data.data ;
              }
            });
          }

        },
       //获取达人
      changeState(flag){
          if(flag){
              this.homeActiveF = true;
          }

      },
      //获取新闻列表
      newslist(){
        var vm=this;
        vm.newsShowFlag=true;
        vm.$nextTick(function(){
          vm.$refs.newsTip.refresh("数据加载中...");
        });
        /*
        Indicator.open({
          text:'加载中...',
          spinnerType:'fading-circle'
        });
        */
        let params={
          api:"/yg-society-service/societyPost/societyPostPage.apec",
          data:{
            //固参
            realm:"ARTICLE",
            auditState:'Y',
            //分页信息
            pageNumber:vm.pageNum,//页码
            pageSize:vm.pageSize//页容量
          }
        };
        vm.post(params,vm.newslistCb,true,'newsTip');
      },
      newslistCb(data){
        var vm=this;
        vm.newsShowFlag=false;
        // Indicator.close();
        if(data.succeed){
          vm.mergeScroll(data.data.rows);
          vm.newsData=data.data.rows;
        }
        else{
          vm.$refs[scrollTip].end(data.errorMsg);
          Toast(data.errorMsg);
        }
      },
      //算积分
      getScore(){
          var vm=this;
          var rand=Math.floor(Math.random()*100+1); //生成一个随机数，范围[1,100]
//          console.log("随机数："+rand);
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
        vm.$nextTick(function () {
          vm.cardData.forEach(item=>{
            var cardDom=document.getElementById("cardItem"+item.id);

//          console.log("类名："+cardDom.className);
//            cardDom.className="card";

          });
        })
      },
      //纸牌翻转点击
      cardFlipped (self,item) {
        var vm=this;

        if(vm.clickFlag){
          var cardDom=document.getElementById("cardItem"+item.id);
          //父节点
          var parent = cardDom.parentElement;

          //获取积分
//          vm.score=vm.getScore();
          vm.cardData.forEach(function (current) {
            current.score = vm.getScore();
          });
          console.log("获得积分:"+vm.score);
          //翻转样式
          cardDom.className+=" flipped";
          vm.clickFlag=false;
          var child = parent.children;
         setTimeout(function () {
           for(var len = child.length, i = 0; i < len; i ++){
             child[i].className +=" flip";//只是反转过来
           }
         }, 1000);
//          function flip(index) {
//            setTimeout(function () {
//              child[index].className +=" flip";//只是反转过来
//            }, index*250)
//          }

//         setTimeout(()=>{
//           vm.tipFlag=true;
//           vm.signFlag = false;
//           vm.lotteryShow=false;
//           for(var len = child.length, i = 0; i < len; i ++){
//             child[i].classList.remove("flip");//只是反转过来
//           }
//           cardDom.classList.remove("flipped");
//         },2000);
          //暂时备注
        vm.uploadScore(item.score);//积分上传


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
      //多个地方的签到
      hasSign(){
        var vm=this;
        let params={
          api:"_node_user/_check_sign.apno",
          data:{}
        };
        vm.post(params,fn.hasSignCb.bind(this));
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

        var name = localStorage.name,
            that = this;
        if(name){
          var vm=this;
          vm.hasSign();
          vm.initialCard();
        }else{
//          Toast('请您先登录，再签到');
          MessageBox.confirm("请您先登录，再签到").then(function () {
            that.$router.push({"name":"login"});
          },function () {

          });
        }
      },
      closeLottery(){
        var vm=this;
        vm.lotteryShow=false;
        vm.initialCard();
      },
      waitingFn(){
          Toast('功能正在开发中，敬请期待...')
      },
      buy(){
//        this.$store.state.main = 1;
        this.$store.state.xqInfoF = 0;
        this.$router.push({name: "gq",query:{gq:"GYTYPE"}});
        clearInterval(this.Time);
      },
      sell(){
        this.$store.state.main = 2;
        this.$store.state.xqInfoF = 0;
        this.$router.push({name: "gq",query:{gq:"QGTYPE"}});
        clearInterval(this.Time);
      },
      fruit(){
          //跳转到果圈
        this.$router.push({name: 'fruitCircle',query:{flag:2}});
      },
      list(param){
          //通过多态来判断首屏的加载
        const self = this;
        if (!self.stopFlag)
          return;
        var page = param || this.pageNumber;
        let params = {
          api: "/_node_product/_all.apno",
          data: {
            keyWord: "",
            orderType: "DATEDES",
            searchType: "",
            pageNumber: page
          }
        }
        if (param) {
          //如果此参数存在，那么说明是第一页的加载
          this.post(params, fn.list.bind(self, param));
        }else{
          this.post(params, fn.list.bind(self));
        }
       },
      xq(event,item){

          var evt = event || window.event;
          var target = evt.toElement || evt.srcElement;
          var cl = target.classList;
          var flag = cl.contains("g-star");
//        var id = target.dataset.id;
          var id = item.id;
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
            this.$router.push({path: 'detail',query:{id:id}});
          }
        this.scrollPos = document.body.scrollTop;
        this.$store.state.xqInfo = true;
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
        this.$router.push({name:'news'});
      },
      trade(){
        this.$router.push({name: 'trader',query:{name:"TR"}});
      },
      sNext(){
        var v = this.preSearch;
        this.$store.state.xqInfoF = 0;
        this.$router.push({name: 'search',query:{search:v}});
        clearInterval(this.Time);
      },
      update(){
          var hash = window.location.hash;
//          if(hash.indexOf("add") > -1){
//             this.exitPublish = false;
//          }else if(hash.indexOf("home") > -1){
//            this.exitPublish = true;
//          }
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
      post(params, fn, flag, scrollTip){
        var vm=this;
        try {
          api.post(params).then((res) => {
            var data = res.data;
            fn(data);
          }).catch((error) => {
            console.log(error)
            if(flag){
              vm.newsShowFlag=true;
              Toast(error);
              // vm.$refs.newsTip.end("没有更多数据了");
              vm.$refs[scrollTip].end("网络异常了...");
            }
          })
        } catch (error) {
          console.log(error)
          if(flag){
            vm.newsShowFlag=true;
            Toast(error);
            vm.$refs[scrollTip].end("网络异常了...");
          }

        }
      },
      messageT(event){
        var name = localStorage.name;
        if(!name){
          return;
        };
        this.$store.state.xqInfoF = 0;
          var evt = event || window.event;
          var target = evt.toElement || evt.srcElement;
          var flag = target.classList.contains("circle");
          var child = target.children;
          // this.$router.push({ name:'messageList'})  旧消息模块
          this.$router.push({ name:'messageInfo'})
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
             var height = 10000;
        }
      },
      preImage(url, result){
          var img = new Image();
          img.src = url;
          if(img.complete){
            result.src = img.src;
          }else{
            img.onload = function (res) {
              result.src = img.src;
            };
          }

      },
      preLoad(){
          //预加载
        var self = this;
        fn.recordF = false;
        if(this.pageCount > self.pageNumber){
          self.pageNumber ++;
          self.list();
        }


      },
      loadMore(){
        var len = fn.cacheData.length;
        var that = this;
        if(len != 0){
          that.items = that.items.concat(fn.cacheData);
          that.mergeScroll(fn.cacheData);
          fn.cacheData = [];
          that.$refs.loading.start();
        }

        if(that.pageCount > that.pageNumber){
          that.pageNumber ++;
          that.list();
        }else{
          that.$refs.loading.end();
        }
      },
      menu(evt){
        //进入该页面，以及接口都返回和页面加载完
        var self = this;
        var elT = document.querySelector(".c-home-page");//用判断是否进入页面
        if(elT){
          var e = evt || window.evt,
              scrollTop = document.body.scrollTop,
              scrollHeight = document.body.scrollHeight;
              if(scrollHeight - scrollTop - this.bheight < 70 && self.switch){
                throttle();
              }
        }
        //节流函数
         function throttle(){
           var len = fn.cacheData.length;
           //&& (len > 0)

              for(var i = 0; i < len; i ++){
                self.items.push(fn.cacheData[i]);
              }

              fn.cacheData = [];
              self.switch = false;
              if(self.pageCount > self.pageNumber){
                self.pageNumber ++;
                self.list();
              }

            if(fn.finishFlag){
              self.$refs.loading.end();
            }else{
              self.$refs.loading.start();
            }
         }

      },
      wx(){
        var self = this;
        let params = {
          api:"/yg-user-service/wxapi/getSignInfo.apec",
          data:{
            url:window.location.href
          }
        }

//        console.log(window.location.href,"home-wx");
        api.post(params).then((res) => {

          var data = res.data;
          var dt = data.data;

          for(var key in dt){
              var name = "wx" + key;
            self.saveWxToken(name, dt[key], 100)
          }
          window.share();//朋友、朋友圈、qq

        }).catch((error) => {
          console.log(error)
        })
      },
      saveWxToken(name,value,second){
        //设置cookie的有效期
        //设置wx的token
          var d = new Date();
          d.setTime(d.getTime() + (second * 60*1000));
          var expires = "expires=" + d.toUTCString();
          document.cookie = name + "=" + value + "; " + expires;
      },
      checkWxToken(name){
          //检查微信的token是否有效
          var tokenString = document.cookie,Token;
          if(tokenString.indexOf(name) > -1){
              var data = {
                  data:{}
              };

            var arr = tokenString.split(";");
//            console.log(arr,222);
            for(var key in arr){
                if(arr[key].indexOf("wxappid") > - 1){
                  data.data.appid = arr[key].split("=")[1];
                }else if(arr[key].indexOf("wxtimestamp") > - 1){
                  data.data.timestamp = arr[key].split("=")[1];
                }else if(arr[key].indexOf("wxnonceStr") > - 1){
                  data.data.nonceStr = arr[key].split("=")[1];
                }else if(arr[key].indexOf("wxsignature") > - 1){
                  data.data.signature = arr[key].split("=")[1];
                }
            }

            window.wxinit(data.data);
          }
        this.wx();
      },
      mergeScroll(data){
          var self = this;

          setTimeout(function () {
            if(!this.scrollData){
              this.scrollData = data;
            }else{
              this.scrollData = this.scrollData.concat(data);
            }
          }, 100)
      },
      //调果排行榜
      tuneFruit(){
        this.$router.push({name:'ranking'});
      },
    },
    activated(){
      //新闻列表
      this.positionS = this.$store.state.homeProvince
      var name = localStorage.name;
      if(!localStorage.tipRecord){
        this.getTip();//提示
      }
      window.postId = "";
      if(name){
          this.isSign();
      }

      var self = this;
      this.checkWxToken("wxappid");//检查微信的Token是否有效
      this.showVisableF = false;
      this.del = {};
      this.isActivated = true;
      this.messageN();

      var flag =  this.$store.state.xqInfo;//悬浮的标志位

      if(flag){
        window.scrollTo(0,self.scrollPos);//返回到悬浮的位置
        this.$store.state.xqInfo = false;
      }else{
        this.pageNumber = 1;
        this.list(1);//首屏加载的数目

        fn.recordF = true;
        fn.record = 1;

        this.items = null;
        this.scrollData = null;
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
      pageNumber:function (cur, old) {
      }
    },
    components:{
      'my-newslist':newslist,
      'my-scrolltip':scrollTip,
      'my-scroll':scroll,
      'my-loading':scrollS,
      'my-guess':guess,
      'my-horse':horse,
      'my-activeP':activePerson,
      'my-cue':cue,
    }
  }
</script>
