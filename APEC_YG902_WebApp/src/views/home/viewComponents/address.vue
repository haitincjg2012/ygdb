<template>
  <div class="c-addressS">
     <!--<my-head >-->
     <div class="c-addr-header">
         <div class="c-addr-back" @click="back"></div>
         <h1 slot="pack" class="c-addr-h-text">选择地区</h1>
     </div>
     <!--</my-head>-->
     <div v-html="history" @click="historyAddr($event)"></div>
     <div class="c-address-blank"></div>
     <div class="c-address-content">
       <h6 class="c-address-title" v-html="h6HTML"></h6>
     </div>
    <my-scroll :data="tmpArr || []" :pullup="pullup" ref="addressWrapper" class="c-addr-c-main">
        <div >
          <div  v-for="(item,index) in tmpArr"
                class="c-addr-c-item"
                :class="{'c-addr-c-active':item.activeFlag}"
                @click="checked(item,index)"
          >
            {{item.keyWord}}
          </div>
        </div>
    </my-scroll>

  </div>
</template>
<style>
  @import "../../../assets/css/addressSecond.css";
</style>
<script>
  import header from '../../../components/header/headerTwo.vue'
//  import charactierAddr from "../../../assets/data/characteristicAddress.json"
  import p from "../../../components/post.vue"
  import scroll from '../../../components/scroll/scrollbg.vue'
  import {Toast} from 'mint-ui'
  export default{
      data(){
          return {
              tmpArr:null,
              provinceArr:null,
              cityArr:null,
              countyArr:null,
              townArr:null,
              h6HTML:"请选择",
              address:{
                  province:"",
                  city:"",
                  county:"",
                  town:"",
                  municipal:"",//市辖区
              },

            history:"",
            recordIndex:{
              province:-1,
              city:-1,
              county:-1,
              town:-1,
            },
            recordBack:[],

            //滑动
            pullup:true,
          }
      },
      methods:{
        back(){
            //模拟返回的按钮
          var that = this;
           if(that.recordBack.length > 0){
               var v = that.recordBack.pop();
               console.log(v);
               switch (v){
                 case "province":
                   that.h6HTML = "请选择";
                   that.tmpArr = that.provinceArr;
                   break;
                 case "city":
                   that.h6HTML =that.address.province;
                   that.tmpArr = that.cityArr;
                     break;
                 case "town":
                   that.h6HTML =that.address.province +" "+ that.address.city +" "+  that.address.county;
                   that.tmpArr = that.townArr;
                     break;
                 case "county":
                   that.h6HTML =that.address.province +" "+ that.address.city;
                   that.tmpArr = that.countyArr;
                     break;
               }
           }else{
             that.cityArr = null;
             that.countyArr = null;
             that.townArr = null;
             that.tmpArr = null;
             that.recordIndex.p = -1;
             that.recordIndex.c = -1;
             that.recordIndex.t = -1;
             that.recordIndex.county = -1;
             that.$router.go(-1);
           }
        },
        checked(item,index){
            var that = this;
            if(item.style == "province"){
              var mark = selectArea("province", index);
              if(mark){
                return;
              }
              that.address.province = item.keyWord;
              that.h6HTML =that.address.province;
              if(window.tree[that.address.province].hasOwnProperty("市辖区")){
                that.address.city = "";
                that.address.municipal = "市辖区";
                that.getCounty(that.address.province, "市辖区");
              }else{
                that.getCity();
              }
            }else if(item.style == "city"){
              var mark = selectArea("city", index);
              if(mark){
                  return;
              }
              that.address.city = item.keyWord;
              that.h6HTML =that.address.province +" "+ that.address.city;

              that.getCounty(that.address.province, that.address.city);
          }else if(item.style == "county"){
            var mark = selectArea("county", index);
            if(mark){
              return;
            }
            that.address.county = item.keyWord;
            if(that.address.city == item.keyWord){
              that.address.county = "";
              that.h6HTML =that.address.province +" "+ that.address.city +  that.address.county;
            }else{
              that.h6HTML =that.address.province +" "+ that.address.city +" "+  that.address.county;
            }
              that.getTown();
          }else if(item.style == "town"){
            var mark = selectArea("town", index);
            if(mark){
              return;
            }
            that.address.town = item.keyWord;
            that.h6HTML =that.address.province +" "+  that.address.city +" "+that.address.county + " "+  that.address.town;
             var type = this.$route.query.type;
              if(type == "gy"){
//      供应初始化
                this.$store.state.addrData = {detailAddress: that.h6HTML};
              }else if(type == "qg"){
//      求购初始化
                this.$store.state.addrSeekData = {detailAddress: that.h6HTML};
              }
             that.$router.go(-1);
              //保存选择的区域
              that.saveBtn();
          }
          that.$refs.addressWrapper.scrollTo(0, 0);
          that.$refs.addressWrapper.refresh();

          function selectArea(type, index) {
            if(that.recordBack.indexOf(type) > -1){
              return true;
            }else{
              that.recordBack.push(type);
            }

            var oldIndex = that.recordIndex[type];
            that.recordIndex[type] = index;
          }
        },
        getProvince(){
            var that = this,
                 provincArr = [];
          if(!window.tree){
            var timeId = setInterval(function () {
              if(window.tree){
                clearInterval(timeId);
                for(var key in window.tree){
                  var obj = {};
                  obj.keyWord= key;
                  obj.style= "province";
                  obj.activeFlag = false;
                  provincArr.push(obj)
                }
                that.provinceArr = provincArr;
                that.tmpArr = provincArr;
              }
            }, 100);
          }else{
              for(var key in window.tree){
                var obj = {};
                obj.keyWord= key;
                obj.style= "province";
                obj.activeFlag = false;
                provincArr.push(obj)
               }
              that.provinceArr = provincArr;
              that.tmpArr = provincArr;
          }
        },
        getCity(){
            var that = this,
                arr = [],
              province = that.address.province;
          for(var key in window.tree[province]){
            var obj = {};
            obj.keyWord= key;
            obj.style= "city";
            obj.activeFlag = false;
            arr.push(obj)
          }

          that.cityArr = arr;
          that.tmpArr = arr;
        },
        getTown(){
          var that = this,
            province = that.address.province,
            city = that.address.city,
            county = that.address.county,
            townArr = [];
            if(city == ""){
               city = that.address.municipal;
            }
            if(county == ""){
              county = city;
            }
           var townArray = window.tree[province][city][county];
           for(var key in townArray){
             var obj = {};
                 obj.keyWord= townArray[key];
                 obj.style= "town";
                 obj.activeFlag = false;
                 townArr.push(obj)
           }
          that.townArr = townArr;
          that.tmpArr = townArr;
        },
        getCounty(province, city){
            var that = this,
                countyArr = [];

             for(var key in window.tree[province][city]){
              var obj = {};
              obj.keyWord= key;
              obj.style= "county";
              obj.activeFlag = false;
              countyArr.push(obj)
            }
            that.countyArr = countyArr;
            that.tmpArr = countyArr;
        },
        getHistory(type){
            var that = this;
            var params = {
              api:"_node_user/_address_his.apno",
                data:{
                }
            }

            p.post(params, function (data) {
                if(data.succeed){
                  var html = "";
                  if(data.data.address.detailAddress){
                    html = '<div class="c-address-history"><h6 class="c-history-title">历史货源区域</h6><div class="c-history-text"><span data-mark="detail">' + data.data.address.detailAddress + "</span></div></div>";
                  }
                  that.history = html
                }

            },function (error) {
              Toast(error);
            })
        },
        historyAddr(event){
          var that = this,
            el = event.target || event.srcElement,
            mark = el.dataset.mark;
            if(mark){
                var type = this.$route.query.type;
              if(type == "gy"){
//      供应初始化
                this.$store.state.addrData = {detailAddress: el.innerHTML};
              }else if(type == "qg"){
//      求购初始化
                this.$store.state.addrSeekData = {detailAddress: el.innerHTML};
              }
              this.$router.go(-1);
            }
        },
        saveBtn(){
          var that = this;

          var param = that.address.province +" " + that.address.city +" " +that.address.county+" " +that.address.town;
          var obj = {
            detailAddress:param
          }

          let params = {
            api:"/_node_user/_save_address.apno",
            data:{
              "detailAddress":obj
            }
          }

          p.post(params, function (data) {
            if(!data.succeed){
                Toast(data.errorMsg);
            }
          },function (error) {
            Toast(error);
          })
        },
      },
      activated(){
          var that = this,type = that.$route.query.type;
          that.recordBack = [];
        if(!that.provinceArr){
          that.getProvince();
        }else{
          that.tmpArr = that.provinceArr;
        }

         that.h6HTML = "请选择";
         that.getHistory();
    },
    mounted(){
      var el = this.$refs.addressWrapper;
      el.init();
    },
      components:{
        "my-head":header,
        'my-scroll':scroll
      }
  }
</script>
