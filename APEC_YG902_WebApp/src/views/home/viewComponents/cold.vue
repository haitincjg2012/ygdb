<template>
  <div class="z-cold">
    <div class="z-header-com">
      <!--<h1>冷库</h1>-->
      <div class="return" @click="back">
        <img src="../../../assets/img/ret.png">
      </div>
      <div class="z-cold-search">
        <img src="../../../assets/img/search.png">
        <input type="text" placeholder="" @change="search" v-model="ky"/>
      </div>
    </div>

    <div class="z-c-select">
      <div class="z-c-a" @click="dropdown" data-path = "0">
        <span class="sp-text-com">区域</span>
        <span class="triangle"></span>
        <!--<span class="z-vertical-line"></span>-->
      </div>
      <div class="z-c-s" @click="dropdown" data-path = "1">
        <span class="sp-text-com">筛选</span>
        <span class="triangle"></span>
      </div>
    </div>
    <div class="z-c-frame">
      <ul class="z-c-list clearfix">
        <li :is="item.ss"
            v-for = "item in itemC"
            :item = "item"
        ></li>
      </ul>
      <div class="z-c-shadow" v-if="shadowF">
        <ul class="z-c-listS clearfix" v-if="firstF">
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
        <ul class="z-c-listA clearfix" v-if="secondF">
          <li :is = "item.ss"
              v-for="item in itemA"
              :item = "item"
              v-on:rss="searchArea">
          </li>
        </ul>
        <div class="shadow" @click="remove"></div>
      </div>
      <div class="c-z-cold-empty" v-if="coldemptyFlag">
        <img src="../../../assets/img/noData.png">
        <p>暂无数据</p>
      </div>
    </div>
  </div>
</template>
<style>
  @import "../../../assets/css/cold.css";
</style>
<script>
  import childT from "./coldlist.vue"
  import API from '../../../api/api'
  import childPZ from "./PZ.vue"
  import Region from './region.vue'
  import coldDefault from "../../../assets/img/coldDefault.png"
  import dataConfig from "../../../assets/data/search.json"
  import {Toast} from 'mint-ui'
  const api = new API();

  var fn = {
    aD:null,//地区的数据
    r:null,//临时数据
    filter:null,
    achildD:{
      code:-1,
      rg:null,
    },//具体地区的数据
    coldData:[],//缓存分页数据
    reset:function () {
      fn.achildD.code = -1;
      fn.achildD.rg = null;
    },
    secondC:function () {
      var index = arguments[0];
      var dt = dataConfig;

      for(var key in dt){
        switch (key){
          case "filter":
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
          obj.keyword = current.keyword;
          if(current.hasOwnProperty("code")){
            obj.code = current.code;
            obj.ss = childPZ;

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
      rows.forEach(function (current, index) {

        var obj = {
          showOrgTagsInfo:{}
        };
        obj.path =index;
        obj.id = current.orgId;
        obj.orgId = current.orgId ? current.orgId:"-999";
        obj.userId = current.userId ? current.userId:"-1000";
        obj.ss = childT;
        obj.orgName = current.orgName;
        obj.address = current.address;
        obj.viewNum = current.viewNum;
        obj.orgStockCap = current.orgStockCap;
        obj.orgFirstBannerUrl = current.orgFirstBannerUrl || coldDefault;
        if(current.showOrgTagsInfo){
             current.showOrgTagsInfo.forEach(function (current) {
                 if(current.className == "QYRZ"){
                   obj.showOrgTagsInfo.QYRZ = true;
                 }else{
                   obj.showOrgTagsInfo.QYJRL = true;
                 }
             })
        }

        arr.push(obj);
      });

      fn.coldData = fn.coldData.concat(arr);
      this.itemC = fn.coldData;
      if(this.itemC.length == 0){
          this.coldemptyFlag = true;
      }else{
        this.coldemptyFlag = false;
      }

    }
  };
  export default{
      data(){
          return{
            itemC:null,
            itemS:null,
            itemA:null,
            itemFilter:null,
            shadowF:false,
            firstF:false,
            firstTF:false,
            secondF:false,
//            firstSearch:"",
            searchType:"",
            pageNumber:1,
            ky:"",
            pageCount:1000,//
            coldemptyFlag:false,////默认情况是有数据的
            bheight:0,//记录屏幕的默认高度
          }
      },
    methods:{
      back(){
        this.shadowF = false;
        fn.pzD = null;
        fn.aD = null;
        this.itemA = null;
        fn.reset()
        this.reset();
        this.initPagination();
        var p = document.querySelector(".z-c-select"),
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
        fn.coldData = [];
        this.pageNumber = 1;
      },
      reset(){
          this.firstF = false;
        this.firstTF = false;
        this.secondF = false;
        this.ky = "";
        this.searchType = "";
        this.coldemptyFlag = false;
      },
      remove(){
        this.shadowF = false;
      },
      search(){
          this.searchType = "";

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
        this.searchType = "";
          this.pageNumber = 1;
        var p = document.querySelector(".z-c-select"),
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
          if(fn.filter){
            this.itemFilter = fn.filter;
          }else{
            fn.secondC.bind(this)(path);
          }
        }
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
        if(index > 0){
//              alert(value.substring(0, index));
          this.searchType = value.substring(0, index);
        }else{
          this.searchType = value;
        }

        this.initPagination();
        this.list(0);
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
          api: "/_node_user_org/_depot_list.apno",
          data: {
            keyWord: that.ky,
//            orderType: that.firstSearch,
            searchType: that.searchType,
            pageNumber: pg,
          }
        }
        this.post(params, fn.list.bind(this));
      },
      menuList(evt){
        var e = evt || window.event;
        var el = document.querySelector(".z-cold")
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
              Toast('数据加载完...')
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
      filteBtn(data){
         var str = data.keyword;
         var searchType = "";
         searchType = str;
//         if(str == "企业认证"){
//           searchType = "QYRZ";
//         }else if(str == "供应金融链合作库"){
//           searchType = "GYLJRHZK";
//         }
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
      searchOk(){
        fn.aD.forEach(function (current,index) {
          current.sh = false;
        });
        if(fn.achildD.rg){
          fn.achildD.rg.forEach(function (current) {
            current.sh = false;
          });
        }


        this.shadowF = false;
        this.firstTF = false;
        this.initPagination();
        this.list(0);
      }
    },
    activated(){
      this.bheight = document.querySelector(".page").clientHeight;
          this.initPagination();
           this.list(1);
    },
    created(){
      window.addEventListener('scroll', this.menuList, false);
    }
  }
</script>
