<template>
    <div class="buyS">
      <!--<scroller ref="my_scroller" :on-infinite="infinite">-->
          <div class="com">
             <div class="z-s-header-T1">
               <div class="z-search">
                 <img src="../../../assets/img/search.png">
                 <!--<input placeholder="" v-model="content" @change="searchRet">-->
                 <input placeholder="" v-model="content" @change="searchRet">
               </div>
               <div class="return" @click="back">
                 <img src="../../../assets/img/ret.png">
               </div>
             </div>
         </div>
          <div class="z-classify-s">
             <ul class="clearfix z-k-list">
                 <li data-index = "0" @click="searchR" class="z-li-list">
                     <span class="sp-text-com Z-H">综合排序</span>
                     <span class="triangle"></span>
                 </li>
                 <li data-index = "1" @click="searchR" class="z-li-list">
                     <span class="sp-text-com g-sp-time">时间</span>
                    <div class="triangleUp"></div>
                    <div class="triangleDown"></div>
                 </li>
               <li data-index = "2" @click="searchR" class="z-li-list">
                 <span class="sp-text-com Z-GQ">供求</span>
                 <span class="triangle"></span>
               </li>
             </ul>
         </div>
          <div class="z-list-first">
                  <ul class="clearfix smallClass" v-if="SORT">
                      <li @click="dropdown" data-index = "0" class="z-buy-li">
                        <span class="sp-text-com">品种</span>
                        <span class="triangle"></span>
                      </li>
                    <li @click="dropdown" data-index = "1" class="z-buy-li">
                      <span class="sp-text-com">规格</span>
                      <span class="triangle"></span>
                    </li>
                    <li @click="dropdown" data-index = "2" class="z-buy-li">
                      <span class="sp-text-com">市-县</span>
                      <span class="triangle"></span>
                    </li>
                  </ul>
            <ul  v-if="GQFlag" class="clearfix z-gq-f t">
              <li
                v-for="item in itemGQ"
              >
                <div @click="sgq"  class='z-gq-style' :data-id = "item.keyWord" :data-path = "item.path" :class="{on:item.sh}">
                  <img src="../../../assets/img/hack.png" :data-id = "item.keyWord">
                  <span :data-id = "item.keyWord">{{item.keyWord}}</span>
                </div>
              </li>
            </ul>
         </div>
         <div style="position: relative">
             <div class="z-content">
              <ul class="z-ul-items">
                <li :is="item.ss"   v-for="item in itemG"
                    :item = "item">
                </li>
              </ul>
            </div>
             <div class="up t" v-if="showShadow">
                 <ul class="clearfix" v-if="first" >
                      <li :is = "item.ss" v-for="item in items"
                          :symbolId ="item.type"
                          :CharacteristicId="item.keyWord"
                          :path = "item.path"
                          :sh = "item.sh"
                          :value = "item.value"
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
  @import "../../../assets/css/search.css";
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
      ZH:null,//综合
      TIMED:null,//时间正绪
      TIMES:null,//时间倒叙
      GY:null,//供应
      QG:null,//求购
      GG:null,//规格
      BREED:null,//品种
      AREA:null,//市县
      enumerableType:{
          "供求":"GYTYPE",
          "求购":"QGTYPE"
      },
    clearN:function (type) {
          if(type == ""){
              return
          }
       fn[type] = null;
    },
    goodD:function () {
      var arr = [];
      var enumer = ["供应", "求购"];
      var path = arguments[0];
      for(var i = 0; i < 2; i ++){

          var obj = {};
        obj.path = i;
        obj.keyWord = enumer[i];
        obj.sh = 0;
        if(path == i){
            obj.sh = true;
        }else{
            obj.sh = false;
        }

        arr.push(obj)
      }

      fn.gD = arr;
      this.GQFlag = true;

      this.SORT = false;
      this.itemGQ = fn.gD;
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
    secondC:function () {

       var idx = arguments[0];
       var dt = dataConfig;

//        var dt = data.data;
//      console.log(dt, 111111);
//        dt.forEach(function (current,idx) {
//          for(var key in current){
//             switch (key){
//               case "1001":
//                 fn.bD = format(current[key], key, 0);
//                   break;
//               case "1002":
//                 fn.sD = format(current[key], key, 0);
//                   break;
//               case "1003":
//                 fn.aD = format(current[key], key, 1);
//                   break;
//             }
//          }
//        });
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
            console.log(fn.aD);
            break;
        }
      }

        if(idx == 0){
          this.items = fn.bD;
        }else if(idx == 1){
          this.items = fn.sD;
        }else if(idx == 2){
          this.itemUrban = fn.aD;
        }
         function format(rows,key,i) {
           var arr = [];
           var mm = key;
           rows.forEach(function (current, index) {
             var obj = {};
             obj.path = index;
             obj.sh = false;
             if(i == 1){
               for(var key in current){
                   obj[key] = current[key];
               }
               obj.ss = URBAN;
               obj.type = current.value;
             }else{
               obj.ss = rank;
               obj.type = mm;
               obj.keyWord = current.keyword;
             }
             arr.push(obj);
           });

           return arr;
         }
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
      this.pageCount = data.data.pageCount;
      this.num = rows.length;
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
        obj.id = current.id;
        obj.levelImg =QG.methods.userLevel(current.userLevelName);
        obj.img = current.firstImageUrl;
        obj.local = current.address;
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
        obj.productTypeName = QG.methods.img(current.productTypeName);
        var n = (250 - (len + 3 + 3 + 3) * 10)/20;

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
        obj.addreeWh = (186 - (obj.name.toString().length + obj.agency.length) * 12 + lF * 6 + lt*8)/20;;
        obj.wh = n;
        if(that.del.hasOwnProperty(id)){
            return;
        }
        that.del[id] = 0;
        arr.push(obj);
      });
      var fType = that.firstSearch;
      var type = this.TYPEPATH;
      var tArr = [];
      if(fType != ""){
        switch (type){
          case "ZH":
              if(fn.ZH){
                tArr = fn.ZH.concat(arr);
              }else{
                tArr = arr;
              }
            fn.ZH = tArr;
            break;
          case "TIMED":
            if(fn.TIMED){
              tArr = fn.TIMED.concat(arr);
            }else{
              tArr = arr;
            }
            fn.TIMED = tArr;
            break;
          case "TIMES":
            if(fn.TIMES){
              tArr = fn.TIMES.concat(arr);
            }else{
              tArr = arr;
            }
            fn.TIMES = tArr;
            break;
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
              break
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
              console.log(arr, 8989);
            if(fn.EMPTY){
              tArr = fn.EMPTY.concat(arr);
            }else{
              tArr = arr;
            }
            fn.EMPTY = tArr;

            break;
        }
      }
      this.itemG = tArr;

    },
    clearF(){
      var eleA = document.querySelector(".z-gq-f")
      var children = eleA.children;
      [].forEach.call(children, function (current) {
        current.children[0].classList.remove("on");
      });
    }
  }
  export default{
      data(){
        return{
            gqf:2,
          itemGQ:null,
            GQFlag:false,
            SORT:true,
            first:true,
            second:true,
          space:false,
          addressFlag:true,
          itemUrban:null,
          showShadow:false,
          hack:true,
          isAct:false,
          items:[],
          content:'',
          spot:"0",
          itemG:null,
          isSelect:0,
//          gy:true,
//          qg:true,
          firstSearch:'',
          smain:0,
          searchCondition:{  //分页属性
            pageNo:"1",
            pageSize:"10"
          },
          pageList:[],
          itemChild:null,
          allLoaded: false, //是否可以上拉属性，false可以上拉，true为禁止上拉，就是不让往上划加载数据了
          scrollMode:"auto", //移动端弹性滚动效果，touch为弹性滚动，auto是非弹性滚动
          ky:'',
          pageNum:0,
          num:0,
          DISTANCE:0,
          pageY:0,
          searchType:"",
          pageCount:0,
          TYPEPATH:"",
          pageNumber:0,
          bheight:0,
          isActivated:true,
          r:0,
          del:{}
        }
      },
    components: {
      'v-loadmore':Loadmore  // 为组件起别名，vue转换template标签时不会区分大小写，例如：loadMore这种标签转换完就会变成loadmore，容易出现一些匹配问题
      // 推荐应用组件时用a-b形式起名
    },
    methods: {
      initB(type){
        var that = this;
        let params = {
          api: "/_node_product/_all.apno",
          data: {
            keyWord: this.content,
            orderType: type,
            searchType: "",
            pageNumber: 0,
          }
        }
        this.post(params, fn.init.bind(that))
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
      back(){
        this.$store.state.xqF = false;
        this.itemG = null;
        this.$store.state.recordPZ = null;
        this.$store.state.recordArea = null;
        this.$store.state.urban = null;
        this.content = "";
        if (this.smain == 0) {
          this.$router.go(-1);
        } else {
          this.$router.push({name: "home"})
          this.smain = 0;
        }
        this.items = null;
        this.showShadow = false;
        var liA = document.querySelector(".z-k-list")
          if(liA){
            var lis = liA.children;
            [].forEach.call(lis, function (current, index) {
              var cl = current.children[0].classList,
                len = current.children.length,
                flag = cl.contains("activeS");
              var flag1 = false;
              if (flag) {
                cl.remove("activeS");
                flag1 = current.children[1].classList.contains("activeTri");
                if (flag1) {
                  current.children[1].classList.remove("activeTri");
                } else {
                  current.children[2].classList.remove("activeTri");
                }
              }
            });
          }



        var eleP = document.querySelector(".smallClass");
        if(eleP){
            var eleA = eleP.children;
          [].forEach.call(eleA, function (current, index) {

            var cl = current.children[0].classList;
            var flag = cl.contains("activeS");
            if (flag) {
              cl.remove("activeS");
              current.children[1].classList.remove("activeTri")
            }
          });
        }

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
//        this.showShadow = false;
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
        this.itemChild = null;
        var idx = parent.dataset.index;
        var elArr = document.querySelector(".smallClass").children;
        [].forEach.call(elArr, function (current, index) {

          var cl = current.children[0].classList;
          var flag = cl.contains("activeS");
          if (flag) {
            cl.remove("activeS");
            current.children[1].classList.remove("activeTri")
          }
          if (idx == index) {
            cl.add("activeS")
            current.children[1].classList.add("activeTri")
          }
        });
        var PZ = this.$store.state.recordPZ;

        if (idx == '0') {
          this.second = false;
          this.first = true;
          this.$store.state.recordArea = null;
          this.$store.state.urban = null;
          console.log(456789000);
          console.log(fn.bD);
            if(fn.bD){
                console.log(PZ);
                if(PZ && PZ.id == "1001"){
                  var path = PZ.path;
                    fn.bD.forEach(function (current,index) {
                      if(path == index){
                        current.sh = PZ.sh;
                      }else{
                        current.sh = false;
                      }
                    });
                }else{
                    console.log(123123);
                  fn.bD.forEach(function (current,index) {
                      current.sh = false;
                  });
                }

              this.items = fn.bD;
            }else{
              this.twoDirector(idx)
            }
        }
        else if (idx == '1') {
          this.del = {};
          this.second = false;
          this.first = true;
          this.$store.state.recordArea = null;
          this.$store.state.urban = null;
          if(fn.sD){
            if(PZ && PZ.id == "1002"){
              var path = PZ.path;
                     fn.sD.forEach(function (current,index) {
                        if(path == index){
                            current.sh = PZ.sh;
                        }else{
                            current.sh = false;
                        }
                     });
            }else{
              fn.sD.forEach(function (current,index) {
                  current.sh = false;
              });
            }
            this.items = fn.sD;
          }else{
            this.twoDirector(idx)
          }
        } else if (idx == '2') {
          var ZZ = this.$store.state.urban;
          this.$store.state.recordPZ = null;
          this.second = true;
          this.first = false;
          if(fn.aD){
            if(ZZ){
              var path = ZZ.path;
                fn.aD.forEach(function (current,index) {
                  if(path == index){
                    current.sh = true;
                  }else{
                    current.sh = false;
                  }
                });

            }
            this.itemUrban = fn.aD;
          }else{
            this.twoDirector(idx);
          }

        }

        this.showShadow = true;
//        this.showShadow = false;
      },
      remove(){
        this.showShadow = false;
        this.GQFlag = false;
        this.SORT = true;
      },
      twoDirector(index){
        fn.secondC.bind(this,index)();
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
      sgq(evt){
          this.del = {}
          var e = evt || window.event;
          var target = e.srcElement || e.toElement;
          var tag = target.tagName,p;
          if(tag.toUpperCase() == "DIV"){
              p = target;
          }else{
              p = target.parentElement;
          }
          var ch = p.dataset.id;
          var path = p.dataset.path;
          fn.clearF();
        this.gqf = path;
        if (ch == "供应") {
          this.firstSearch = "GYTYPE";
          this.TYPEPATH = 'GY';
        } else if (ch == "求购") {
          this.firstSearch = "QGTYPE";
          this.TYPEPATH = 'QG';
        }

        this.showShadow = false;
        this.GQFlag = false;
        this.SORT = true;
        this.searchType = "";
        this.list();
      },
      sret(param){
        var ch = param.text;
        var id = param.id;
        this.pageNumber = 1;
        fn.clearN(this.TYPEPATH);


        if(id == "1001"){
            this.del = {};
          this.searchType = ch;
          this.TYPEPATH ="GG"
        }else if(id == "1002"){
          this.del = {};
          this.searchType = ch;
          this.TYPEPATH ="BREED"
        }else if(id == "1003"){
          var kk = param.value;
          var path = param.path;
          var childO = fn.aD[path];
          var childA = childO[kk]
          var arrT = [];
          var p;
          var RA = this.$store.state.recordArea;

          if(RA){
            p = RA.path;
          }
          childA.forEach(function (current, index) {
            var obj = {};
            obj.path = index;
            obj.id = kk;
            obj.keyWord = current.keyWord;
            obj.city = ch;
            obj.ss = CITY;
            if(p){
              if(p == index){
                obj.sh = true;
              }else{
                obj.sh = false;
              }
            }else{
              obj.sh = false;
            }
            arrT.push(obj)
          });

          this.itemChild = arrT;
          return;
        }

        this.list();
        this.showShadow = false;
      },
      searchR(event){
        var evt = event || window.event;
        var target = evt.toElement || evt.srcElement;
        var tag = target.tagName.toUpperCase(), parent;
        var GQF = this.$store.state.GQF;
        if (tag != "LI") {
          parent = target.parentElement;
        } else {
          parent = target;
        }

        this.$store.state.recordArea = null;
        this.$store.state.urban = null;

        var idx = parent.dataset.index,
          orderType = '',
          searchType = '',
          pageNumber = 0;

        var elP = document.querySelector(".smallClass")
          if(elP){
            var eleArr = elP.children;
            [].forEach.call(eleArr, function (current, index) {

              var cl = current.children[0].classList;
              var flag = cl.contains("activeS");
              if (flag) {
                cl.remove("activeS")
                current.children[1].classList.remove("activeTri");
              }
            });
          }


        var lis = document.querySelector(".z-k-list").children;
        [].forEach.call(lis, function (current, index) {
          var cl = current.children[0].classList,
            len = current.children.length,
            flag = cl.contains("activeS");
          var flag1 = false;
          if (flag) {
            cl.remove("activeS");
            flag1 = current.children[1].classList.contains("activeTri");
            if (flag1) {
              current.children[1].classList.remove("activeTri");
            } else {
              current.children[2].classList.remove("activeTri");
            }
          }

          if (idx == index) {
            cl.add("activeS");
            if (idx == 1) {
              if (flag1) {
                current.children[2].classList.add("activeTri");
              } else {
                current.children[1].classList.add("activeTri");
              }
            } else {
              current.children[1].classList.add("activeTri");
            }

          }
        });

        var that = this;
        if(this.content == ""){
            that.ky = "";
        }

        if (idx == 0) {
          this.del = {};
          this.gqf = 1000;
          this.GQFlag = false;
          this.SORT = true;
          this.firstSearch = "ZHPX";
          this.searchType = "";
          this.pageNumber = 1;
          fn.clearN(this.TYPEPATH);
          this.TYPEPATH = "ZH";
          this.$store.state.GQF = null;
          this.$store.state.recordPZ = null;
        } else if (idx == 1) {
          this.$store.state.recordPZ = null;
          this.del = {};
          this.gqf = 1000;
          this.GQFlag = false;
          this.SORT = true;
            this.searchType = "";
            this.pageNumber = 1;
          this.$store.state.GQF = null;
          fn.clearN(this.TYPEPATH);
          if (this.spot == 0) {
            this.firstSearch = "DATEDES";
            this.TYPEPATH = "TIMED";
            this.spot = "1";
          } else {
            this.firstSearch = "DATEASC";
            this.TYPEPATH = "TIMES";
            this.spot = "0";
          }
        }
        else if (idx == 2) {
          this.showShadow = true;
          this.first = false;
          this.second = false;
          var pp = 1000;
          this.pageNumber = 1;
          if(this.gqf){
             pp = this.gqf;
          }
          fn.goodD.bind(this)(pp);
          return;
        }
        this.list();
        this.showShadow = false;
      },
      searchRet(){
        this.del = {};
        this.showShadow = false;
        this.GQFlag = false;
        this.SORT = true;
        this.second = false;
        this.first = false;
        this.itemChild = null;
        fn.clearN(this.TYPEPATH);
        this.TYPEPATH = "EMPTY";

        this.ky = this.content;
        this.pageNumber = 1;
        var that = this;
        this.$store.state.recordPZ = null;

        this.list();
      },
      child(param){
          this.del = {};
        this.showShadow = false;
        this.searchType = param;
        this.pageNumber = 1;
        this.r = 0;
        fn.clearN(this.TYPEPATH);
        this.TYPEPATH = "AREA";
        this.list();

        this.itemChild = null;
      },
      sretF(param){
        var cityName = param.text;
        var kk = param.value;
        var path = param.path;
        var childO = fn.aD[path];
        console.log(childO);
        var childA = childO[kk]
        var arrT = [];
        var p, id;
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
        this.DISTANCE += (this.pageY - pageY);
        if(pageY < this.pageY){
            var eleCh = document.querySelector(".z-ul-items").children;
            var clientH = eleCh[0].clientHeight;
            var height = clientH* this.num;
            if(height < this.DISTANCE){
              this.DISTANCE = 0;
              this.pageY = 0;
              if( this.pageCount <= this.pageNumber){
                Toast('数据加载完...123')
                return;
              }

              this.pageNumber ++;
              this.list();
            }

        }
      },
      list(){
        var that = this;
        var pg = arguments[0] || this.pageNumber;
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
      menuS(evt){
          var that = this;
         var e = evt || window.event;
         var el = document.querySelector(".buyS");
         if(el){
           var target = e.target || e.srcElement;
           var offsetH = target.body.scrollTop;
           var sHeight = target.body.scrollHeight;
           if(sHeight - offsetH - this.bheight == 0){
               if(this.pageCount != 0){
                 if(this.pageCount >= this.pageNumber){
//                   this.Time = setTimeout(that.list(), 1000);
                     this.pageNumber ++;
                     this.st(this.pageNumber);
                 }else{
                   Toast('数据加载完...')
                 }
               }
           }
         }


      },
      st(aa){
          var argument = [].slice(arguments);
          var number = argument[0];
        var that = this;
          function l() {
            that.list(aa);
          }
        setTimeout(l, 1000)
      },
      infinite (done) {
        if(this.pageCount >= this.pageNumber){
            return;
        }
        if (!this.isActivated) return done(true);
        setTimeout(()=>{
          this.list();
          done(true);
        }, 1500);
      },
    },
    mounted(){
//      this.$store.state.recordArea = null;
//      var container = document.querySelector(".z-ul-items");
//      container.addEventListener('touchstart', this.down, false);
//      container.addEventListener('touchend', this.up, false)
//      this.$refs.my_scroller.finishInfinite(true);
    },
      watch:{
        itemChild:function () {
             this.addressFlag = true;
        }
      },
      activated(){
        this.isActivated = true;
        this.bheight = document.querySelector(".page").clientHeight;

        var sH =  this.$store.state.xqInfoF;

        if(this.$store.state.xqF){
          window.scrollTo(0,sH);
        }else{
          this.del = {};
          var keyword = this.$route.query.KEY;
          this.ky = keyword
          this.firstSearch = "ZHPX";
          var el = document.querySelector(".Z-H");
          var nextSibine = el.nextElementSibling;
          el.classList.add("activeS");
          nextSibine.classList.add("activeTri");
          this.searchType = "";
          this.pageNumber = 1;

          this.TYPEPATH = "ZH";
          fn.clearN(this.TYPEPATH);
          this.list();
        }
    },
    deactivated () {
      this.isActivated = false
    },
      created(){
        window.addEventListener('scroll', this.menuS, false);
      }
  }
</script>
