<template>
  <div class="z-trader">
    <div class="z-header-com">
      <!--<h1>客商</h1>-->
      <div class="return" @click.stop="back">
        <img src="../../../assets/img/ret.png">
      </div>
      <div class="z-trader-search">
        <img src="../../../assets/img/search.png">
        <input type="text" placeholder="" @change="search" v-model="traderSearch"/>
      </div>
    </div>

    <div class="z-t-select">
         <div class="z-t-a" @click="dropdown" data-path = "0">
             <span class="sp-text-com">区域</span>
             <span class="triangle"></span>
             <!--<span class="z-vertical-line"></span>-->
         </div>
         <div class="z-t-s" @click="dropdown" data-path = "1">
           <span class="sp-text-com">筛选</span>
           <span class="triangle"></span>
         </div>
    </div>
    <div class="z-t-frame">
      <ul class="z-t-list clearfix">
        <li :is="item.ss"
            v-for = "item in itemC"
            :item = "item"
        ></li>
      </ul>
      <scrollS ref="childScroll"></scrollS>
      <div class="z-t-shadow" v-if="shadowF">
        <ul class="z-t-listS clearfix" v-if="firstF">
          <li :is = "item.ss"
              v-for="item in itemS"
              :item = "item"
              v-on:rs="pzSearch">
          </li>
        </ul>
        <ul class="c-filter" v-if="firstTF">
          <li v-for="item in itemFilter" class="c-filter-com" @click="filteBtn(item)">
            <span :class="{Cactive:item.sh}">{{item.keyword}}</span>
            <!--<img>-->
          </li>
          <li class="c-filter-ok">
            <div class="z-f-ok" @click="searchOk">确认</div>
          </li>
        </ul>
        <ul class="z-t-listA clearfix" v-if="secondF">
          <li :is = "item.ss"
              v-for="item in itemA"
              :item = "item"
              v-on:rss="searchArea">
          </li>
        </ul>
        <div class="shadow" @click="remove"></div>
      </div>
      <div class="c-z-tr-empty" v-if="tremptyFlag">
        <img src="../../../assets/img/noData.png">
        <p>暂无数据</p>
      </div>
    </div>
  </div>
</template>
<style scoped>
  @import "../../../assets/css/trader.css";
</style>
<script>
  import agksD from "../../../assets/img/DBKS.png"
  import IMG from "../../../components/gqimg.vue"
  import childT from "./traderlist"
  import API from '../../../api/api'
  import childPZ from "./PZ.vue"
  import Region from './region.vue'
  import dataConfig from "../../../assets/data/search.json"
  import {Toast} from 'mint-ui'
  import scrollS from "../../../components/scrollSecond.vue"

  const api = new API();
  var fn = {
    aD:null,//地区的数据
    r:null,//临时数据
    filter:null,
    achildD:{
      code:-1,
      rg:null,
    },
    traderData:[],//具体地区的数据
    reset:function () {
      fn.achildD.code = -1;
      fn.achildD.rg = null;
      fn.traderData = []
    },
    init:function (data) {
          var arr = [];
         for(var i = 0; i < 3; i++){
             var obj = {};
             obj.path = 1;
             obj.id = 1;
             obj.ss = childT;

             arr.push(obj);

         }

         this.itemC = arr;
      },
    secondC:function () {
      var index = arguments[0];
      var dt = dataConfig;

      for(var key in dt){
        switch (key){
          case "filter1":
            fn.filter = format(dt[key], key, 100);
            break;
          case "area":
            fn.aD = format(dt["area"]["parentA"], 10000);
            break;
        }
      }

      if(index == 0){
        this.itemS = fn.aD;
      }else if(index == 1){
        this.itemFilter = fn.filter;
      }

      function format(rows, flag) {
        var arr = [];
        rows.forEach(function (current, index) {
          var obj = {};
          obj.path = index;
          obj.sh = false;
          obj.type = flag;
          obj.ss = childPZ;
          obj.keyword = current.keyword;
          if(current.hasOwnProperty("code")){
            obj.code = current.code;
          }else{
            obj.code = "9999";
          }
          arr.push(obj);
        });

        return arr;
      }
    },
    region:function (code,city) {
      var dt = dataConfig["area"]["childA"][code];
      var arr = [];
      dt.forEach(function (current,index) {
        var obj = {};
        obj.keyword = current.keyword;
        obj.path = index;
        obj.city = city;
        obj.ss = Region;
        obj.sh = false;
        obj.code = code;
        arr.push(obj);
      });
      fn.r = arr;
      this.itemA = arr;
    },
    list(data){
      if(!data.succeed){
        return;
      }
      var arr = [];
      var rows = data.data.rows;
      this.pageCount = data.data.pageCount;
      if(data.data.pageCount == 0){
        this.$refs.childScroll.init(true);
      }else{
        this.$refs.childScroll.init(false);
      }
      rows.forEach(function (current, index) {
        var obj = {
          showOrgTagsInfo:{}
        };
        obj.path =index;
        obj.id = current.userId;
        obj.ss = childT;
        obj.orgId = current.orgId ? current.orgId:"-999";
        obj.userId = current.userId ? current.userId:"-1000";
        obj.orgName = current.orgName;//用户姓名
        obj.orgFirstBannerUrl = (current.orgFirstBannerUrl || agksD)+"?x-oss-process=style/_list";//左侧图片
        obj.userLevelName = IMG.methods.userLevel(current.userLevelName);//用户等级
        obj.userRealAuthKey = current.userRealAuthKey == ""?false:true;//用户是否实名认证
        obj.mainOperating = current.mainOperating;//用户主营品种
        obj.saleAddress = current.saleAddress;//用户的销售区域
        obj.address = current.address;
        arr.push(obj);
      });

      fn.traderData = fn.traderData.concat(arr);
      this.itemC = fn.traderData;

      if(this.itemC.length == 0){
          this.tremptyFlag = true;
      }else{
        this.tremptyFlag = false;
      }
    }
  };
export default{
    data(){
        return{
          itemC:null,
          itemS:null,
          itemA:null,
          shadowF:false,
          searchType:"",
          pageNumber:1,
          ky:"",
          itemFilter:null,
          firstF:false,
          firstTF:false,
          secondF:false,
          tremptyFlag:false,////默认情况是有数据的
          bheight:0,//记录屏幕的默认高度
          traderSearch:"",
        }
    },
    methods:{
       back(){
         this.shadowF = false;
         fn.pzD = null;
         fn.aD = null;
         this.itemA = null;
         fn.reset();
         this.reset();
         this.initPagination();
         var p = document.querySelector(".z-t-select"),
           child = p.children;
         [].forEach.call(child,function (current, index) {
           var childG = current.children,
             cGF = childG[0].classList,
             cGS = childG[1].classList;
           var flag = cGF.contains("activeC");
           if(cGF.contains("activeC")){
             cGF.remove("activeC");
           }
           if(cGS.contains("activeTri")){
             cGS.remove("activeTri");
           }
         });
           this.$router.go(-1);

       },
      initPagination(){
           fn.traderData = [];
           this.pageNumber = 1;
      },
      reset(){
        this.firstF = false;
        this.firstTF = false;
        this.secondF = false;
        this.ky = "";
        this.searchType = "";
        this.tremptyFlag = false;
      },
      remove(){
        this.shadowF = false;
      },
      search(){
        this.searchType = this.traderSearch;
        this.initPagination();
        this.list(1);
      },
      dropdown(e){
        var e = e || window.event;
        var target = e.srcElement || e.toElement;
        var tag = target.tagName.toUpperCase();
        var parent;
        this.shadowF = true;
        if(tag == "DIV"){
          parent = target;
        }else{
          parent = target.parentElement;
        }
        var path = parent.dataset.path;
        var p = document.querySelector(".z-t-select"),
          child = p.children;
        [].forEach.call(child,function (current, index) {
          var childG = current.children,
            cGF = childG[0].classList,
            cGS = childG[1].classList;
          var flag = cGF.contains("activeC");
          if(path == index){
            if(!flag){
              cGF.add("activeC");
              cGS.add("activeTri");
            }
          }else{
            if(cGF.contains("activeC")){
              cGF.remove("activeC");
            }
            if(cGS.contains("activeTri")){
              cGS.remove("activeTri");
            }
          }
        });
        if(path == 0){
          if(fn.aD){
            this.itemS = fn.aD;
          }else{
            fn.secondC.bind(this)(path);
          }
          this.reset();
          this.firstF = true
        }else if(path == 1){
          this.reset();
          this.firstTF = true
          if(fn.aD){
            this.itemS = fn.aD;
          }else{
            fn.secondC.bind(this)(path);
          }
        }
      },
      pzSearch(param){
          console.log(param, 88888);
        var flag = param.flag;
        var type = param.type;
        var path = param.path;
        var code = param.code;
        var value = param.key;

        switch (type){
          case "100":
            //品种
            fn.reset();
//            this.initPagination();
            this.shadowF = false;
//            fn.pzD.forEach(function (current,index) {
//              if(path == index){
//                current.sh = true;
//              }else{
//                current.sh = false;
//              }
//            });
            fn.aD.forEach(function (current,index) {
              current.sh = false;
            });
            break;
          case "10000":
            //区域
            fn.aD.forEach(function (current,index) {
              if(path == index){
                current.sh = true;
              }else{
                current.sh = false;
              }
            });
//            fn.pzD.forEach(function (current,index) {
//              current.sh = false;
//            });
            var CODE = fn.achildD.code ;
            if(CODE == code){
              this.itemA = fn.achildD.rg;
            }else{
              fn.region.bind(this)(code, value);
            }
            this.secondF = true;
            break;
        }
      },
      searchArea(param){

        var value = param.key;
        var path = param.path;
        var code = param.code;
        this.shadowF = false;
        fn.achildD.code = code;
        fn.achildD.rg = fn.r;
        fn.achildD.rg.forEach(function (current, index) {
          if(index == path){
            current.sh = true;
          }else{
            current.sh = false;
          }
        });

        var index = value.indexOf("全部");
        if(index > -1){
////              alert(value.substring(0, index));
          this.searchType = value.substring(0, index);
        }else{
          this.searchType = value;
        }

        this.initPagination();
        this.list(1);
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
      list(){
        var pg = arguments[0] || this.pageNumber;
        var that = this;
        let params = {
          api: "/_node_user_org/_merchant_list.apno",
          data: {
            keyWord: that.ky,
            orderType: that.firstSearch,
            searchType: that.searchType,
            pageNumber: pg,
          }
        }
        this.post(params, fn.list.bind(this));
      },
      searchOk(){
        fn.aD.forEach(function (current,index) {
          current.sh = false;
        });
        if(fn.achildD.rg){
          fn.achildD.rg.forEach(function (current) {
            current.sh = false;
          });
        }

        this.initPagination();
        fn.traderData = [];
        this.shadowF = false;
        this.firstTF = false;

        this.list(0);
      },
      filteBtn(data){
        var str = data.keyword;
        var searchType = "";
        if(str == "企业认证"){
          searchType = "QYRZ";
        }else if(str == "供应金融链合作库"){
          searchType = "GYLJRHZK";
        }
        var path = data.path;
        this.itemFilter.forEach(function (current, index) {
          if(path == index){
            current.sh = true;
          }else{
            current.sh = false;
          }

        });

        this.searchType = searchType;
      },
      menuList(e){

        var e = e || window.event;
        var el = document.querySelector(".z-trader")
        if(el){

          var target = e.target || e.srcElement;
          var offsetH = target.body.scrollTop;
          var sHeight = target.body.scrollHeight;
          var that = this;

          if(sHeight - offsetH - this.bheight == 0){

            if(this.pageCount > this.pageNumber){
              this.$refs.childScroll.start();
              this.pageNumber ++;
              this.pageNum(this.pageNumber);
            }else{
              this.$refs.childScroll.end();
//              Toast('数据加载完...')
            }

          }
        }
      },
      pageNum(aa){
        var argument = [].slice.call(arguments);
        var number = argument[0];
        var that = this;
        function l() {
          that.list(aa);
        }
        setTimeout(l, 1000)
      },
    },
    activated(){
      this.bheight = document.querySelector(".page").clientHeight;
        this.initPagination();
        this.list(1);
    },
   created(){
    window.addEventListener('scroll', this.menuList, false);
  },
  components:{
    scrollS
  }
}
</script>
