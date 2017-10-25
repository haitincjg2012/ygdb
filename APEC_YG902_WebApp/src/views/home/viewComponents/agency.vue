<template>
    <div class="z-ag">
      <div class="z-header-com">
        <!--<h1>代办</h1>-->
        <div class="return" @click="back">
          <img src="../../../assets/img/ret.png">
        </div>
        <div class="z-agency-search">
          <img src="../../../assets/img/search.png">
          <input type="text" placeholder="请输入查询内容" @change="search" v-model="ky"/>
        </div>
      </div>

      <div class="z-ag-select">
        <div class="z-ag-a" @click="dropdown" data-path = "0">
          <span class="sp-text-com">品种</span>
          <span class="triangle"></span>
          <!--<span class="z-vertical-line"></span>-->
        </div>
        <div class="z-ag-s" @click="dropdown" data-path = "1">
          <span class="sp-text-com">区域</span>
          <span class="triangle"></span>
        </div>
      </div>
      <div class="z-ag-frame">
          <ul class="z-ag-list clearfix">
            <li :is="item.ss"
                v-for = "item in itemC"
                :item = "item"
            ></li>
          </ul>
          <scrollS ref="childScroll"></scrollS>
          <div class="z-ag-shadow" v-if="shadowF">
            <ul class="z-ag-listS clearfix">
            <li :is = "item.ss"
                v-for="item in itemS"
                :item = "item"
                v-on:rs="pzSearch">
            </li>
          </ul>
            <ul class="z-ag-listA clearfix">
              <li :is = "item.ss"
                  v-for="item in itemA"
                  :item = "item"
                  v-on:rss="searchArea">
              </li>
            </ul>
           <div class="shadow" @click="remove"></div>
        </div>
          <div class="c-z-ag-empty" v-if="agemptyFlag">
              <img src="../../../assets/img/noData.png">
              <p>暂无数据</p>
          </div>
      </div>
    </div>
</template>
<style>
  @import "../../../assets/css/agency.css";
</style>
<script>
  import agksD from "../../../assets/img/DBKS.png"
  import childT from "./agencylist.vue"
  import childPZ from "./PZ.vue"
  import IMG from "../../../components/gqimg.vue"
  import Region from './region.vue'
  import API from '../../../api/api'
  import dataConfig from "../../../assets/data/search.json"
  import scrollS from "../../../components/scrollSecond.vue"
  import {Toast} from 'mint-ui'

  const api = new API();
  var fn = {
    pzD:null,//品种的数据
    aD:null,//地区的数据
    r:null,//临时数据
    achildD:{
        code:-1,
        rg:null,
    },
    agData:[],//具体地区的数据
    init:function (data) {
      var arr = [];
      for(var i = 0; i < 3; i++){
        var obj = {};
        obj.path = 1;
        obj.id = 1;
        obj.ss = childT;
        obj.imgLevel = IMG.methods.userLevel("钻石");
        arr.push(obj);

      }

      this.itemC = arr;
    },
    reset:function () {
      fn.achildD.code = -1;
      fn.achildD.rg = null;
    },
    secondC:function () {
      var index = arguments[0];
      var dt = dataConfig;
      for(var key in dt){
        switch (key){
          case "pz":
            fn.pzD = format(dt[key],100);
            break;
          case "area":
            fn.aD = format(dt[key]["parentA"], 10000);
            break;
        }
      }

      if(index == 0){
        this.itemS = fn.pzD;
      }else if(index == 1){
        this.itemS = fn.aD;
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
      }else if(data.data.pageCount == 1){
        this.$refs.childScroll.init();
      }else if(this.pageCount > this.pageNumber){
        this.$refs.childScroll.start();
      }
      rows.forEach(function (current, index) {
        var obj = {
          showOrgTagsInfo:{}
        };
        obj.path =index;
        obj.id = current.userId;//用户userId
        obj.orgId = current.orgId ? current.orgId:"-999";
        obj.userId = current.userId ? current.userId:"-1000";
        obj.ss = childT;
        obj.orgName = current.orgName;//用户名称
        obj.address = current.address;//用户地址
        obj.viewNum = current.viewNum;//浏览数量
        obj.orgFirstBannerUrl = (current.orgFirstBannerUrl || agksD)+"?x-oss-process=style/_list";//左侧图片
        obj.showOrgTagsInfo.tagName = current.showOrgTagsInfo.tagName;//企业认证
        obj.userLevelName = IMG.methods.userLevel(current.userLevelName);//用户等级
        obj.userRealAuthKey = current.userRealAuthKey == ""?false:true;//用户是否实名认证
        obj.voucherNum = current.voucherNum;//用户调用量
        obj.mainOperating = current.mainOperating;//用户主营品种
        arr.push(obj);
      });
       fn.agData = fn.agData.concat(arr);
      this.itemC = fn.agData

      if(this.itemC.length == 0){
          this.agemptyFlag = true;
      }else{
        this.agemptyFlag = false;
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
           recordIndex:0,//记录上一次点击的按钮
          searchType:"",
          pageNumber:0,
          pageCount:1000,
          ky:"",
          agemptyFlag:false,//默认情况是有数据的
          bheight:0,//记录屏幕高度
        }
      },
    methods:{
      back(){
        this.shadowF = false;
        fn.pzD = null;
        fn.aD = null;
        this.itemA = null;
        this.ky = "";
        this.searchType = "";
        fn.reset();
        this.reset();
        var p = document.querySelector(".z-ag-select"),
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
      reset(){
          this.emptyFlag = false;
      },
      search(){

        this.searchType = "";
        this.initPagination();
        this.list(1);
        this.shadowF = false;
      },
      initPagination(){
        fn.agData = [];
        this.pageNumber = 1;
      },
      remove(){
        this.shadowF = false;
      },
      pzSearch(param){
          var flag = param.flag;
          var type = param.type;
          var path = param.path;
          var code = param.code;
          var value = param.key;

          switch (type){
            case "100":
                //品种
              fn.reset();
                this.shadowF = false;
              fn.pzD.forEach(function (current,index) {
                if(path == index){
                    current.sh = true;
                }else{
                    current.sh = false;
                }
              });
              fn.aD.forEach(function (current,index) {
                  current.sh = false;
              });
              this.searchType = value;
              this.initPagination();
              this.list(1);
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
              fn.pzD.forEach(function (current,index) {
                current.sh = false;
              });
              var CODE = fn.achildD.code ;
              if(CODE == code){
                 this.itemA = fn.achildD.rg;
              }else{
                fn.region.bind(this)(code, value);
              }

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
          if(index > 0){
//              alert(value.substring(0, index));
            this.searchType = value.substring(0, index);
          }else{
            this.searchType = value;
          }

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
          var p = document.querySelector(".z-ag-select"),
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
            this.itemA = null;
             if(fn.pzD){
               this.itemS = fn.pzD;
             }else{
               fn.secondC.bind(this)(path);
             }
          }else if(path == 1){
            if(fn.aD){
              this.itemS = fn.aD;
            }else{
              fn.secondC.bind(this)(path);
            }
          }
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
          api: "/_node_user_org/_agency_list.apno",
          data: {
            keyWord: that.ky,
            searchType: that.searchType,
            pageNumber: pg,
          }
        }
        this.post(params, fn.list.bind(this));
      },
      menuList(e){

        var e = e || window.event;
        var el = document.querySelector(".z-ag")

        if(el){
          var target = e.target || e.srcElement;
          var offsetH = target.body.scrollTop;
          var sHeight = target.body.scrollHeight;
          var that = this;

          if(sHeight - offsetH - this.bheight == 0){
            if(this.pageCount > this.pageNumber){
              this.pageNumber ++;
              this.pageNum(this.pageNumber);
            }else{
              this.$refs.childScroll.end();
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
      }
    },
    activated(){
      this.bheight = document.querySelector(".page").clientHeight;
      this.initPagination();
      this.list(1);
    },
    created(){
//      window.addEventListener('scroll', this.menuList, false);
      window.addEventListener('scroll', this.menuList, false);
    },
    components:{
      scrollS
    }
  }
</script>
