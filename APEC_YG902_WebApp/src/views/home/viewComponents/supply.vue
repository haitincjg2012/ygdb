<template>
  <div>
    <div class="com">
      <div class="z-s-header">
        <div class="z-search">
          <img src="../../../assets/img/search.png">
          <input placeholder="" v-model="content">
        </div>
        <div class="return" @click="back">
          <img src="../../../assets/img/ret.png">
        </div>
      </div>
    </div>
    <div class="z-classify-s">
      <ul class="clearfix">
        <li data-index = "0" @click="searchR" class="z-li-list">
          <span class="sp-text-com">综合排序</span>
          <span class="triangle"></span>
        </li>
        <li data-index = "1" @click="searchR" class="z-li-list">
          <span class="sp-text-com">时间</span>
          <img src="../../../assets/img/updown.png" class="updown" />
        </li>
        <li data-index = "2" @click="searchR" class="z-li-list">
          <span class="sp-text-com activeS">求购</span>
          <span class="triangle"></span>
        </li>
      </ul>
    </div>
    <div class="z-list">
      <ul class="clearfix smallClass">
        <li @click="dropdown" data-index = "0">
          <span class="sp-text-com">规格</span>
          <span class="triangle"></span>
        </li>
        <li @click="dropdown" data-index = "1">
          <span class="sp-text-com">品种</span>
          <span class="triangle"></span>
        </li>
        <li @click="dropdown" data-index = "2">
          <span class="sp-text-com">区域</span>
          <span class="triangle"></span>
        </li>
      </ul>
    </div>
    <div style="position: relative">
      <div class="z-content">
        <ul>
          <li v-for="item in itemG"
              :is = "item.ss"
              :item = "item"
          ></li>
        </ul>
      </div>
      <div class="up" v-if="showShadow">
        <ul class="clearfix">
          <li :is = "item.ss" v-for="item in items"
              :symbolId ="item.type"
              :CharacteristicId="item.keyword"
              v-on:rs="sret">
          </li>
        </ul>
        <div class="shadow" @click="remove">
        </div>
      </div>
    </div>
  </div>
</template>
<style scoped>
  @import "../../../assets/css/search.css";
</style>

<script>
  import rank from './rank.vue'
  import goodD from  './goodlist.vue'
  import API from '../../../api/api'
  const api = new API();

  var fn = {
    sellD:null,//供应初始化
    gD:null,//供应和求购两个
    bD:null,
    sD:null,
    aD:null,
    enumerableType:{
      "供求":"GYTYPE",
      "求购":"QGTYPE"
    },
    goodD:function () {
      if(!data.succeed){
        return;
      }
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
      dt.forEach(function (current, index) {
        var obj = {};
        obj.ss = rank;
        obj.type = current.code;
        obj.keyword = current.keyword;
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
      dt.forEach(function (current, index) {
        var obj = {};
        obj.ss = rank;
        obj.type = current.code;
        obj.keyword = current.keyword;
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
      dt.forEach(function (current, index) {
        var obj = {};
        obj.ss = rank;
        obj.type = current.code;
        obj.keyword = current.keyword;
        arr.push(obj);
      });
      fn.aD = arr;
      this.items = fn.aD;
    },
    result:function (data) {
      if(!data.succeed){
        return;
      }
      var rows = data.data.rows;
      fn.formatD(rows,this);
    },
    init:function (data) {
      if(!data.succeed){
        return;
      }
      var rows = data.data.rows;
      var arr = [];
      rows.forEach(function (current, index) {
        var obj = {};
        obj.ss = goodD;
        obj.skuName = current.skuName;
        obj.showCredateTime = current.showCredateTime;
        obj.id = current.id;
        obj.levelImg = current.userLevelName;
        obj.img = current.firstImageUrl;
        obj.local = current.address;
        obj.name = current.showUserName;
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
        obj.path = index;

        var goodTime = current.showSecondInfo;
        var text = "";
        goodTime.forEach(function (current) {
          var t = "|" + " "+current;
          text += t;
        });
        obj.fruitdate = text;

        var Identification = current.productTypeName;

        if(Identification == "求购"){
          obj.indentification = 0;
          obj.endAmount = current.endAmount;
          obj.startAmount = current.startAmount;
        }else{
          obj.indentification = 1;
          obj.amount = current.amount;
        }

        arr.push(obj);
      });
      fn.sellD = arr;
      this.itemG =  fn.sellD;
    },
    formatD:function (rows,that) {
      if(!data.succeed){
        return;
      }

      var arr = [];
      rows.forEach(function (current, index) {
        var Identification = current.productTypeName;
        if(Identification == "求购"){
          var obj = {};
          obj.indentification = 0;
          obj.endAmount = current.endAmount;
          obj.startAmount = current.startAmount;
        }else {
            return
        }

        obj.ss = goodD;
        obj.skuName = current.skuName;
        obj.showCredateTime = current.showCredateTime;
        obj.id = current.id;
        obj.levelImg = current.userLevelName;
        obj.img = current.firstImageUrl;
        obj.local = current.address;
        obj.name = current.showUserName;
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
        obj.path = index;

        var goodTime = current.showSecondInfo;
        var text = "";
        goodTime.forEach(function (current) {
          var t = "|" + " "+current;
          text += t;
        });
        obj.fruitdate = text;

        var Identification = current.productTypeName;



        arr.push(obj);
      });
      that.itemG = arr;
    }
  }
  export default{
    data(){
      return{
        showShadow:false,
        hack:true,
        isAct:false,
        items:[],
        content:'',
        spot:"0",
        itemG:null
      }
    },
    methods:{
      initB(){
        var that = this;
        let params = {
          api:"/_node_product/_all.apno",
          data:{
            keyWord:'',
            orderType:'',
            searchType:"QGTYPE",
            pageNumber:''
          }
        }
        this.post(params, fn.init.bind(that))
      },
      back(){
        this.$router.go(-1);
      },
      search(event){
        var evt = event || window.event;
        var target = evt.toElement || evt.srcElement;
        var tag = target.tagName.toUpperCase(),classlist;
        if(tag != "DIV"){
          classlist = target.parentElement.classList;
        }else{
          classlist = target.classList;
        }
        if(classlist.contains("on")){
          return;
        }else{
          var elArr = document.querySelectorAll(".pitch");
          for(var i = 0, len = elArr.length; i < len; i ++){
            var ele = elArr[i].classList;
            var flag = ele.contains("on");
            if(flag){
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
        var tag = target.tagName.toUpperCase(),parent;
        if(tag != "LI"){
          parent = target.parentElement;
        }else{
          parent = target;
        }

        var idx = parent.dataset.index;
        var elArr = document.querySelector(".smallClass").children;
        [].forEach.call(elArr, function (current, index) {
          var cl = current.children[0].classList;
          var flag = cl.contains("activeS");
          if(flag){
            cl.remove("activeS")
          }
          if(idx == index){
            cl.add("activeS")
          }
        });

        var eleA = document.querySelectorAll(".z-li-list");
        [].forEach.call(eleA, function (current) {
          var cl = current.children[0].classList;
          var flag = cl.contains("activeS");
          if(flag){
            cl.remove("activeS")
          }
        });

        if(idx == '0'){
          var D = fn.bD;
          if(D){
            this.items = D;
          }else{
            this.breed();
          }

        }
        else if(idx == '1'){

          var D = fn.sD;
          if(D){
            this.items = D;
          }else{
            this.specifications();
          }
        }else if(idx == '2'){
          var D = fn.aD;
          if(D){
            this.items = D;
          }else{
            this.area();
          }
        }

        this.showShadow = true;
      },
      remove(){
        this.showShadow = false;
      },
      breed(){
        var that = this;
        let params= {
          api:'/yg-systemConfig-service/wordBook/listNeedWordBook.apec',
          data:{
            code:"1001"
          }
        }
        this.post(params, fn.breedD.bind(that));
      },
      specifications(){
        var that = this;
        let params = {
          api:'/yg-systemConfig-service/wordBook/listNeedWordBook.apec',
          data:{
            code:"1002"
          }
        }

        this.post(params, fn.specificationsD.bind(that));
      },
      area(){
        var that = this;
        let params = {
          api:'/yg-systemConfig-service/wordBook/listNeedWordBook.apec',
          data:{
            code:"1003"
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
        this.showShadow = false;
//        var ch = param;
        if(param == "供应"){
          this.$router.push({name:'buy'})
          return;
        }else if(param == "求购"){
//          this.itemG =  fn.sellD;
          this.initB();
          return;
        }

        var that = this;
        let params = {
          api:"/_node_product/_all.apno",
          data:{
            keyWord:this.content,
            orderType:'',
            searchType:param,
            pageNumber:''
          }
        }

        this.post(params, fn.result.bind(that))
      },
      searchR(event){

        var evt = event || window.event;
        var target = evt.toElement || evt.srcElement;
        var tag = target.tagName.toUpperCase(),parent;
        if(tag != "LI"){
          parent = target.parentElement;
        }else{
          parent = target;
        }

        var idx = parent.dataset.index,
          orderType = '',
          searchType = '',
          pageNumber = '';

        var lis = document.querySelectorAll(".z-li-list");
        lis.forEach(function (current, index) {
          var cl = current.children[0].classList,
            flag = cl.contains("activeS");
          if(flag){
            cl.remove("activeS");
          }
          if(idx == index){
            cl.add("activeS");
          }
        })


        if(idx == 0){
          orderType = "ZHPX";
        }else if(idx == 1){
          if(this.spot == 0){
            searchType = 'DATEDES';
            this.spot = "1";
          }else{
            searchType = 'DATEASC';
            this.spot = "0";
          }

        }else if(idx == 2){
          this.showShadow = true;

          if(fn.gD){
            return;
          }else{
            fn.goodD.bind(this)();
            return;
          }

        }

        var that = this;
        let params = {
          api:"/_node_product/_all.apno",
          data:{
            keyWord:this.content,
            orderType:orderType,
            searchType:searchType,
            pageNumber:pageNumber
          }
        }

        this.post(params, fn.result.bind(that))
        this.showShadow = false;
      },
//      xq(event){
//        this.$router.push({name: "detail"});
//      },
    },
    mounted(){
      this.initB();
    }
  }
</script>
