<template>
  <div class="z-gq" ref="ZGQ">
    <!--<scroller ref="my_scrollerT" :on-infinite="infinite">-->
    <div class="com">
      <div class="z-s-header">
        <div class="z-search">
          <img src="../../../assets/img/search.png">
          <!--<input placeholder="" v-model="content" @change="searchRet">-->
          <input placeholder="请填写搜索的内容" v-model="content" @change="searchRet">
        </div>
        <div class="return" @click="back">
          <img src="../../../assets/img/ret.png">
        </div>
      </div>
    </div>
    <div class="z-list-s">
      <ul class="clearfix smallClass">
        <li @click="dropdown" data-index = "0" class="z-li-com">
          <span class="sp-text-com">品种</span>
          <span class="triangle"></span>
        </li>
        <li @click="dropdown" data-index = "1" class="z-li-com">
          <span class="sp-text-com">规格</span>
          <span class="triangle"></span>
        </li>
        <li @click="dropdown" data-index = "2" class="z-li-com">
          <span class="sp-text-com">市-县</span>
          <span class="triangle"></span>
        </li>
      </ul>
    </div>
    <div style="position: relative">
      <div class="z-content">
        <ul class="z-ul-itemS">
          <li :is="item.ss"   v-for="item in itemG"
              :item = "item">
          </li>
        </ul>
        <div class="button-tip" :class="{flip:arrive, loading:loadFlag}" v-if="!space">
          <div class="c-z-pullup" :class="{showVisable:showVisableF}">
          </div>
          <div class="c-z-pullup-text">
            <span>{{load}}</span>
          </div>
        </div>
      </div>
      <div class="z-up" v-if="showShadow">
        <ul class="clearfix" v-if="first">
          <li :is = "item.ss" v-for="item in items"
              :symbolId ="item.type"
              :CharacteristicId="item.keyWord"
              :path = "item.path"
              :sh = "item.sh"
              :value = "item.value"
              :recordF = "item.recordF"
              v-on:rs="sret">
          </li>
        </ul>
        <ul class="clearfix" v-if="second">
          <li :is = "item.ss" v-for="item in itemUrban"
              :symbolId ="item.type"
              :CharacteristicId="item.keyWord"
              :path = "item.path"
              :sh = "item.sh"
              :value = "item.value"
              v-on:rsss="sretF">
          </li>
        </ul>
        <ul  class="clearfix">
          <li :is="item.ss" v-for="item in itemChild"
              :child = "item"
              v-on:rs="child"
              v-if="addressFlag"
          ></li>
        </ul>
        <div class="shadow" @click="remove">
        </div>
      </div>
    </div>
    <div v-if="space" class="z-tip-img">
      <img src="../../../assets/img/searchF.svg">
      <p>抱歉,没有找到符合条件的供求</p>
    </div>
    <!--</scroller>-->
  </div>
</template>
<style scoped>
  @import "../../../assets/css/gq.css";
  @import "../../../assets/css/scrollStyle.css";
</style>

<script>
  import QT from '../../../assets/img/copper@3x.png'//铜牌
  import BY from '../../../assets/img/silver@3x.png'//银牌
  import HJ from '../../../assets/img/gold@3x.png'//金牌
  import BJ from '../../../assets/img/Pt@3x.png'//铂金
  import ZS from '../../../assets/img/Diamonds.png'//砖石
  import DS from '../../../assets/img/Ancrown@3x.png'//大师
  import {Toast} from 'mint-ui'
  import rank from './rank.vue'
  import goodD from  './goodlist.vue'
  import API from '../../../api/api'
  import {Loadmore} from 'mint-ui';
  import QG from '../../../components/gqimg.vue'
  import CITY from  './city.vue'
  import URBAN from './urban.vue'
  import dataConfig from "../../../api/data"
  const api = new API();


  var fn = {
    buyD:null,//供应初始化
    EMPTY:null,//一级搜索
    gD:null,//供应和求购两个
    bD:null,
    sD:null,
    aD:null,
    GY:null,//供应
    QG:null,//求购
    GG:null,//规格
    BREED:null,//品种
    AREA:null,//市县
    enumerableType:{
      "供求":"GYTYPE",
      "求购":"QGTYPE"
    },
    initialization(){
      //fn对象的初始化
      fn.buyD = null;
      fn.EMPTY = null;
      fn.gD = null;
      fn.bD = null;
      fn.sD = null;
      fn.aD = null;
      fn.GY = null;
      fn.QG = null;
      fn.GG = null;
      fn.BREED = null;
      fn.AREA = null;
    },
    clearN:function (type) {
      if(type == ""){
        return
      }
      fn[type] = null;
    },
    goodD:function () {
      var arr = [];
      var enumer = ["供应", "求购"]
      for(var i = 0; i < 2; i ++){
        var obj = {};
        obj.ss = rank;
        obj.type = i;
        obj.keyword = enumer[i];
        arr.push(obj)
      }

      fn.gD = arr;
      this.items = fn.gD;

    },
    breedD:function (data) {
      if(!data.succeed){
        return;
      }
      var dt = data.data;
      var arr = [];
      var PZ = this.$store.state.recordPZ;
      dt.forEach(function (current, index) {
        if(index == 0){
          return ;
        }
        var obj = {};
        obj.ss = rank;
        obj.type = current.code;
        obj.keyword = current.keyword;
        obj.path = index - 1;
        if(PZ ){
          if(obj.type == PZ.id && obj.path == PZ.path){
            obj.sh = true;
          }else{
            obj.sh = false;
          }
        }else{
          obj.sh = false;
        }
        arr.push(obj);
      });
      fn.bD = arr;
      this.items = fn.bD;


    },
    specificationsD:function (data) {
      if(!data.succeed){
        return;
      }
      var dt = data.data;
      var arr = [];
      var PZ = this.$store.state.recordPZ;
      dt.forEach(function (current, index) {
        if(index == 0){
          return ;
        }
        var obj = {};
        obj.ss = rank;
        obj.type = current.code;
        obj.keyword = current.keyword;
        obj.path = index - 1;
        if(PZ ){
          if(obj.type == PZ.id && obj.path == PZ.path){
            obj.sh = true;
          }else{
            obj.sh = false;
          }
        }else{
          obj.sh = false;
        }
        arr.push(obj);
      });
      fn.sD = arr;
      this.items = fn.sD;
    },
    areaD:function (data) {
      if(!data.succeed){
        return;
      }
      var dt = data.data;
      var arr = [];
      var PZ = this.$store.state.recordPZ;
      dt.forEach(function (current, index) {
        if(index == 0){
          return ;
        }
        var obj = {};
        obj.ss = rank;
        obj.type = current.code;
        obj.keyword = current.keyword;
        obj.path = index - 1;
        if(PZ ){
           if(obj.type == PZ.id && obj.path == PZ.path){
               obj.sh = true;
           }else{
             obj.sh = false;
           }
        }else{
          obj.sh = false;
        }
        arr.push(obj);
      });
      fn.aD = arr;
      this.items = fn.aD;
    },
    init:function (data) {
      if(!data.succeed){
        return;
      }
      var that = this;
      var rows = data.data.rows;
      this.num = rows.length;
      this.pageCount = data.data.pageCount;
      if(this.pageCount == 0){
          this.space = true;
      }else{
          this.space = false;
      }
      var arr = [];
      rows.forEach(function (current, index) {
        var obj = {};
        var len = 0;
        obj.ss = goodD;
        obj.skuName = current.skuName;
        obj.showCredateTime = current.showCredateTime;
        var id = current.id;
        obj.id = id;
        obj.levelImg =QG.methods.userLevel(current.userLevelName);
        obj.img = current.firstImageUrl+"?x-oss-process=style/_list";//使用阿里云的上传方式
        obj.local = current.address;
        obj.name = current.showUserName;
        obj.priceUnit = current.priceUnit;
//        obj.agency = QG.methods.userTypeNameSwitch(current.userTypeName);
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
        obj.productTypeName = QG.methods.img(current.productTypeName);
        if(that.del.hasOwnProperty(id)){
            return;
        }
        that.del[id] = 0;
        arr.push(obj);
      });
      var type = this.TYPEPATH;
      var tArr = [];
      switch (type){
        case "GY":
            if(fn.GY){
                tArr = fn.GY.concat(arr);
            }else{
                tArr = arr;
            }
            fn.GY = tArr;
            break;
        case "QG":
          if(fn.QG){
            tArr = fn.QG.concat(arr);
          }else{
            tArr = arr;
          }
          fn.QG = tArr;
            break;
        case "GG":
          if(fn.GG){
            tArr = fn.GG.concat(arr);
          }else{
            tArr = arr;
          }
          fn.GG = tArr;
            break;
        case "BREED":
          if(fn.BREED){
            tArr = fn.BREED.concat(arr);
          }else{
            tArr = arr;
          }
          fn.BREED = tArr;
          break;
        case "AREA":
          if(fn.AREA){
            tArr = fn.AREA.concat(arr);
          }else{
            tArr = arr;
          }
          fn.AREA = tArr;
            break;
        case "EMPTY":
          if(fn.EMPTY){
            tArr = fn.EMPTY.concat(arr);
          }else{
            tArr = arr;
          }
          console.log(fn.EMPTY);
          fn.EMPTY = tArr;
      }
      this.itemG = tArr;

    },
    secondC:function () {
      var index = arguments[0];
      var dt = dataConfig;

      for(var key in dt){
        switch (key){
          case "1001":
            fn.bD = format(dt[key], key, 0);
            break;
          case "1002":
            fn.sD = format(dt[key], key, 0);
            break;
          case "1003":
            fn.aD = format(dt[key], key, 1);
            break;
        }
      }

      if(index == 0){
        this.items = fn.bD;
      }else if(index == 1){
        this.items = fn.sD;
      }else if(index == 2){
        this.itemUrban = fn.aD;
      }

      function format(rows,key,i) {
        var arr = [];
        var mm = key;
        rows.forEach(function (current, index) {
          var obj = {};

          obj.path = index;
          obj.sh = false;
          obj.recordF = false;
          if(i == 1){
              obj.type = current.value;
              obj.ss = URBAN;
            for(var k in current){
              obj[k] = current[k];
            }
          }else{
            obj.type = mm;
            obj.ss = rank;
            obj.keyWord = current.keyword;

          }
          arr.push(obj);
        });


        return arr;
      }
    }
  }
  export default{
    data(){
      return{
        first:false,
        second:false,
        itemUrban:null,
          space:false,
        addressFlag:true,
        showShadow:false,
        hack:true,
        isAct:false,
        items:[],
        content:'',
        spot:"0",
        itemG:null,
        isSelect:0,
        firstSearch:'',
        smain:0,
        itemChild:null,
        searchCondition:{  //分页属性
          pageNo:"1",
          pageSize:"10"
        },
        pageList:[],
        allLoaded: false, //是否可以上拉属性，false可以上拉，true为禁止上拉，就是不让往上划加载数据了
        scrollMode:"auto", //移动端弹性滚动效果，touch为弹性滚动，auto是非弹性滚动
        pageY:0,
        distance:0,
        num:0,
        pageCount:1000,
        TYPEPATH:"",
        ky:'',
        pageNum:0,
        pageNumber:1,
        searchType:'',
        bheight:0,
        isActivated:true,
        Time1:null,
        Time2:null,
        Time3:null,
        del:{},
        el:null,//onscroll滚动机制
        arrive:false,//到底底部箭头切换
        loadFlag:false,//箭头切换以后，加载数据
        showVisableF:false,//默认显示的
        load:"",//数据在加载中
        emptyFlag:false,//是否需要显示
      }
    },
    components: {
      'v-loadmore':Loadmore  // 为组件起别名，vue转换template标签时不会区分大小写，例如：loadMore这种标签转换完就会变成loadmore，容易出现一些匹配问题
      // 推荐应用组件时用a-b形式起名
    },
    methods: {
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
      gqresult(type){
        var that = this;
//        var text = type.text;

        let params = {
          api: "/_node_product/_all.apno",
          data: {
            keyWord: this.content,
            orderType: that.firstSearch,
            searchType: type,
            pageNumber: 0,
          }
        }

        this.post(params, fn.init.bind(that))
      },
      back(){
        this.content = "";
        this.$store.state.xqF = false;
        this.$store.state.recordPZ = null;
        this.$store.state.recordArea = null;
        this.$store.state.urban = null;
        this.showVisableF = false;
        this.reset();
        this.searchType = "";
        this.el = null;
        fn.initialization();
        if (this.smain == 0) {
          this.$router.go(-1);
        } else {
          this.$router.push({name: "home"})
          this.smain = 0;
        }
        this.items = null;
        this.itemChild = null;
        this.showShadow = false;

        var elArr = document.querySelector(".smallClass").children;
        [].forEach.call(elArr, function (current, index) {

          var cl = current.children[0].classList;
          var flag = cl.contains("activeC");
          if (flag) {
            cl.remove("activeC");
            current.children[1].classList.remove("activeTri")
          }
        });
      },
      reset(){
          this.pageNumber = 1;
          this.firstSearch = "";
        this.searchType = "";
      },
      search(event){
        var evt = event || window.event;
        var target = evt.toElement || evt.srcElement;
        var tag = target.tagName.toUpperCase(), classlist;
        if (tag != "DIV") {
          classlist = target.parentElement.classList;
        } else {
          classlist = target.classList;
        }
        if (classlist.contains("on")) {
          return;
        } else {
          var elArr = document.querySelectorAll(".pitch");
          for (var i = 0, len = elArr.length; i < len; i++) {
            var ele = elArr[i].classList;
            var flag = ele.contains("on");
            if (flag) {
              ele.remove("on");
            }
          }
          classlist.add("on");

        }
      },
      dropdown(event){

        var evt = event || window.event;
        var target = evt.toElement || evt.srcElement;
        var tag = target.tagName.toUpperCase(), parent;
        if (tag != "LI") {
          parent = target.parentElement;
        } else {
          parent = target;
        }
        var idx = parent.dataset.index;
        var elArr = document.querySelector(".smallClass").children;
        [].forEach.call(elArr, function (current, index) {
          var cl = current.children[0].classList;
          var flag = cl.contains("activeC");
          if (flag) {
            cl.remove("activeC");
            current.children[1].classList.remove("activeTri")
          }
          if (idx == index) {
            cl.add("activeC")
            current.children[1].classList.add("activeTri")
          }
        });

        var PZ = this.$store.state.recordPZ;
        if (idx == '0') {
          this.first = true;
          this.second = false;
          this.$store.state.recordArea = null;
          this.$store.state.urban = null;
              if(fn.bD){
                if(PZ){
                  var path = PZ.path;
                  if(PZ.id == "1001"){
                    fn.bD.forEach(function (current,index) {
                      if(path == index){
                        current.sh = PZ.sh;
                      }else{
                        current.sh = false;
                      }
                    });
                  }else{
                    fn.bD.forEach(function (current,index) {
                        current.sh = false;
                    });
                  }
                }else{
                  fn.bD.forEach(function (current,index) {
                    current.sh = false;
                  });
                }
                this.items = null;
                this.items = fn.bD;
              }else{
                this.twoDirector(idx)
              }
          this.itemChild = null;
        }
        else if (idx == '1') {
            this.first = true;
          this.second = false;
          this.$store.state.recordArea = null;
          this.$store.state.urban = null;
          if (fn.sD) {
            if (PZ && PZ.id == "1002") {
              var path = PZ.path;
                fn.sD.forEach(function (current, index) {
                  if (path == index) {
                    current.sh = PZ.sh;
                  } else {
                    current.sh = false;
                  }
                });
              }else {
              fn.sD.forEach(function (current, index) {
                current.sh = false;
              });
            }

              this.items = fn.sD;
            } else {
              this.twoDirector(idx)
            }
            this.itemChild = null;
          }
        else if (idx == '2') {
            var ZZ = this.$store.state.urban;
            this.second = true;
          this.first = false;
          this.$store.state.recordPZ = null;
            if (fn.aD) {
              if (ZZ) {
                var path = ZZ.path;
                  fn.aD.forEach(function (current, index) {
                    if (path == index) {
                      current.sh = true;
                    } else {
                      current.sh = false;
                    }
                  });
              }else{
                fn.aD.forEach(function (current, index) {
                  current.sh = false;
                });
              }
              this.itemUrban = fn.aD;
            } else {
              this.twoDirector(idx)
            }
//          }
        }
        this.showShadow = true;
      },
      clearChild(){
        var PZ = this.$store.state.recordPZ;
        var ZZ = this.$store.state.urban;
        if(PZ){
            var path = PZ.id;

            if(id == "1001"){
              fn.bD.forEach(function (current,index) {
                current.sh = false;
              });
            }else if(id == "1002"){
              fn.sD.forEach(function (current, index) {
                current.sh = false;
              });
            }
        }else if(ZZ){
          fn.aD.forEach(function (current, index) {
            current.sh = false;
          });
        }
      },
      remove(){
        this.showShadow = false;
      },
      breed(){
        var that = this;
        let params = {
          api: '/yg-systemConfig-service/wordBook/listNeedWordBook.apec',
          data: {
            code: "1001"
          }
        }
        this.post(params, fn.breedD.bind(that));
      },
      specifications(){
        var that = this;
        let params = {
          api: '/yg-systemConfig-service/wordBook/listNeedWordBook.apec',
          data: {
            code: "1002"
          }
        }

        this.post(params, fn.specificationsD.bind(that));
      },
      twoDirector(index){
        fn.secondC.bind(this,index)();
      },
      area(){
        var that = this;
        let params = {
          api: '/yg-systemConfig-service/wordBook/listNeedWordBook.apec',
          data: {
            code: "1003"
          }
        }

        this.post(params, fn.areaD.bind(that));
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
      sret(param){
        this.del = {};
        var ch = param.text;
        var id = param.id;
        this.searchType = ch;
        this.pageNumber = 1;
        fn.clearN(this.TYPEPATH);
        if(id == "1001"){
         this.TYPEPATH = "GG"
        }else if(id == "1002"){
          this.TYPEPATH = "BREED"
        }
         this.list();
        this.$store.state.recordArea = null;
        this.showShadow = false;
      },
      child(param){
          this.del = {};
        this.showShadow = false;
        this.searchType = param;
        this.pageNumber = 1;
        fn.clearN(this.TYPEPATH);
        this.TYPEPATH = "AREA";
        this.list();
        this.itemChild = null;
      },
      keySearch(data){
        var dt = data;
        data.succeed = true;
        fn.init.bind(this)(dt);
      },
      searchRet(){
          this.del = {};
          fn.clearN(this.TYPEPATH);
          this.TYPEPATH = "EMPTY";
        this.GQFlag = false;
        this.SORT = true;
        this.second = false;
        this.first = false;
        this.itemChild = null;
        this.showShadow = false;
        this.ky = this.content;
        this.pageNumber = 1;
        this.searchType = 0;

        this.$store.state.recordPZ = null;
        var elArr = document.querySelector(".smallClass").children;
        [].forEach.call(elArr, function (current, index) {

          var cl = current.children[0].classList;
          var flag = cl.contains("activeC");
          if (flag) {
            cl.remove("activeC");
            current.children[1].classList.remove("activeTri")
          }
        });

        this.list();

      },
      sretF(param){
          var cityName = param.text;
          var kk = param.value;
          var path = param.path;
          var childO = fn.aD[path];
          var childA = childO[kk]
          var arrT = [];
          var p, id, identity;
          var RA = this.$store.state.recordArea;

          if(RA){
            p = RA.path;
            id = RA.id;
          }
          childA.forEach(function (current, index) {
            var obj = {};
            obj.path = index + 1;
            obj.id = kk;
            obj.keyWord = current.keyWord;
            obj.city = cityName;
            obj.ss = CITY;
            if(id && p && id == kk && p == obj.path){
              obj.sh = true;
            }else{
              obj.sh = false;
            }

            arrT.push(obj)
          });

          var o = {};
          o.path = 0;
          o.city = cityName;
          o.ss = CITY;
          o.id = kk;
          o.keyWord = "全部";
          if(id && p && id == kk  && p == 0){
            o.sh = true;
          }else{
            o.sh = false;
          }
          arrT.unshift(o);

          this.itemChild = arrT;
      },
      down(evt){
        var e = evt || window.event;
        this.pageY = e.changedTouches ? e.changedTouches[0].pageY : e.pageY;
      },
      up(evt){
        var e = evt || window.event;
        var pageY = e.changedTouches ? e.changedTouches[0].pageY : e.pageY;
        this.distance += (this.pageY - pageY);
        if(this.pageY > pageY){
            var el = document.querySelector(".z-ul-itemS");
            var child = el.children[0];
            var childH = child.clientHeight;
            var height = childH * this.num;
            if(height < this.distance){

                this.distance = 0;
              this.pageY = 0;
              if( this.pageCount <= this.pageNumber){
//                Toast('数据加载完...')
                return;
              }
              this.pageNumber ++;
              this.list();
            }
        }
      },
      list(){
        var pg = arguments[0] || this.pageNumber;
        var that = this;
        let params = {
          api: "/_node_product/_all.apno",
          data: {
            keyWord: that.ky,
            orderType: that.firstSearch,
            searchType: that.searchType,
            pageNumber: pg,
          }
        }
        this.post(params, fn.init.bind(this));
      },
      menuG(evt){
        var e = evt || window.event;
        var el = document.querySelector(".z-gq");

        if(this.el){
          var target = e.target || e.srcElement;
          var offsetH = target.body.scrollTop;
          var sHeight = target.body.scrollHeight;
          var that = this;
          if(sHeight - offsetH - this.bheight == 0){
            if(this.pageCount > this.pageNumber){
                this.arrive = true;
                 this.loadFlag = true;
                 this.load = "数据正在加载中...";
                 this.pageNumber ++;
                 this.st(this.pageNumber);
            }else{
              this.arrive = false;
              this.loadFlag = false;
              this.showVisableF = true;
              this.load = "数据正在加载完...";
//              Toast('数据加载完...')
            }

          }
        }
      },
      st(aa){
        var argument = [].slice.call(arguments);
        var number = argument[0];
        var that = this;
        function l() {
          that.list(aa);
        }
        setTimeout(l, 1000)
      },
      infinite (done) {
        if (!this.isActivated) return done(true);
        setTimeout(()=>{
          this.list();
          done(true);
        }, 1500);
      },
    },
    mounted(){
      this.$store.state.recordPZ = null;
      this.$store.state.recordArea = null;
      this.$store.state.urban = null;

    },
    activated(){
      this.el = this.$refs.ZGQ;
      this.isActivated = true;
      this.del = {};
      this.bheight = document.querySelector(".page").clientHeight;
        var type = this.$route.query.gq;


      var sH =  this.$store.state.xqInfoF;
      if( this.$store.state.xqF){
        window.scrollTo(0,sH);
      }else{
        this.ky = "";
        if(type == "GYTYPE"){
          //   供应
          this.firstSearch = type;
          this.TYPEPATH = "GY";
        }else if(type == "QGTYPE"){
          //求购
          this.firstSearch = type;
          this.TYPEPATH = "QG";
        }
        this.itemG = null;
        fn.clearN(this.TYPEPATH);
        this.list(1);
//        if(this.pageNumber == 1){
//
//        }
      }

    },
    created(){
      window.addEventListener('scroll', this.menuG, false);
    }
  }
</script>
