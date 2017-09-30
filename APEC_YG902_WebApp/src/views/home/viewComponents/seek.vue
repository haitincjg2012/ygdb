<template>
  <div class="z-frame">
    <div class="com">
      <div class="header">
        <h1>发布求购信息</h1>
        <div class="return" @click="back">
          <img src="../../../assets/img/ret.png">
        </div>
      </div>
    </div>
    <div class="com">
      <div class="good-com clearfix" @click="goodinfo">
        <i class="z-star-O"></i>
        <label>商品信息:</label>
        <input id="g-info" class="input-com" v-model="skuName" style="background: transparent;" disabled>
        <div class="z-pos-o">
          <img src="../../../assets/img/more-1.png"/>
        </div>
      </div>
    </div>
    <div class="com">
      <div class="good-com clearfix">
        <i class="z-star-O"></i>
        <label for="z-supply">求购量:</label>
        <input id="z-supply" class="input-com" @input="handle"  maxlength="5" v-model="number" placeholder="请填写求购的数值">
        <!--<div class="z-pos-o">-->
          <!--<img src="../../../assets/img/more-1.png"/>-->
        <!--</div>-->
        <span class="z-f1">
            <div class="z-f1" >
               <ul class="clearfix ul-weight" @click = "weightunit">
                   <li class="active-li li-left li-com">
                     <div>万斤</div>
                   </li>
                   <li class="li-right li-com">
                       <div>桶</div>
                   </li>
               </ul>
           </div>
        </span>
      </div>
    </div>
    <div class="com">
      <div class="good-com clearfix">
        <i class="z-star-O"></i>
        <label>求购价格:</label>
        <div class="z-f">
          <input class="input-price" v-model="priceO" @input="handlep" maxlength="4" data-id="0" placeholder="请填写单价">
          <span>-</span>
          <input class="input-price" v-model="priceT" @input="handlep" maxlength="4" data-id="1" placeholder="请填写单价">
          <span class="sp-O">元/斤</span>
        </div>

      </div>
    </div>
    <div class="com">
      <div class="good-com clearfix" @click="zone">
        <i class="z-star-O"></i>
        <label >货源区域:</label>
        <input id="g-area" class="input-com" v-model="address" disabled>
        <div class="z-pos-o">
          <img src="../../../assets/img/more-1.png"/>
        </div>
        <span class="z-f1 z-sp2">市/镇</span>
      </div>
    </div>
    <div class="com">
      <div class="good-com clearfix">
        <i class="z-star-O"></i>
        <label>上架有效时间:</label>
        <div class="z-valid">
          <ul @click="pickup">
            <li class="">7天</li>
            <li>15天</li>
            <li>30天</li>
          </ul>
        </div>
      </div>
    </div>
    <div class="com">
      <div class="good-com clearfix">
        <label style="margin: 0;">描述信息:</label>
        <!--<input id="z-des" class="input-com" placeholder="请输入描述"/>-->
        <textarea id="z-des" placeholder="请输入描述" rows="3" cols="20" ></textarea>
      </div>
    </div>
    <div class="pic-show"></div>
    <div class="submit" @click="submit">
      提交
    </div>
  </div>
</template>
<style>
  @import "../../../assets/css/seek.css";
</style>
<script>
  import API from '../../../api/api'
  import { Toast } from 'mint-ui';
  const api = new API();
  var fn = {
    sumbit:function (data) {
      if(!data.succeed){
        return;
      }
        var success = data.succeed;
        if(success){
            this.number = "";
            this.priceO = "";
            this.priceT = "";
          this.$store.state.skuName = "";
          this.$store.state.skuId = "";
          this.$store.state.addrData = null;
          this.address = '';
           document.getElementById("z-des").value = "";
           var father = document.querySelector(".z-valid").children[0];
           var children = father.children;
           [].forEach.call(children, function (current) {
                 current.classList.remove("active-li");
           });
           var eleArr = document.querySelector(".ul-weight").children;
          eleArr[0].classList.add("active");
          eleArr[1].classList.remove("active");
           this.$router.go(- 1);
        }
    }
  }
  export default{
      data(){
        return{
          skuName:'',
          skuId:'',
          number:'',
          priceO:'',
          priceT:'',
          time:'',
          address:'',
          unit:'万斤'
        }
      },
    methods:{
      back(){
        this.number = "";
        this.priceO = "";
        this.priceT = "";
        this.$store.state.skuName = "";
        this.$store.state.skuId = "";
        this.$store.state.addrSeekData = null;
        this.address = '';
        document.getElementById("z-des").value = "";
        var father = document.querySelector(".z-valid").children[0];
        var children = father.children;
        [].forEach.call(children, function (current) {
          current.classList.remove("active-li");
        });
        var eleArr = document.querySelector(".ul-weight").children;
        eleArr[0].classList.add("active");
        eleArr[1].classList.remove("active");
        this.$router.go(-1);
      },
      goodinfo(){
        this.$router.push({name: 'goodinfo',query:{path:"pbuy"}});
      },
      zone(){
        this.$store.state.address = 2;
//        this.$router.push({name:'address'});
        this.$router.push({name:'addrSeek'});
      },
      pickup(event){
        var evt = event || window.event;
        var target = evt.toElement || evt.srcElement;
        var tagname = target.tagName.toUpperCase();
        var eleA = document.querySelectorAll(".z-valid ul li"),
           parent;

        if(tagname == 'LI')
        {
            parent = target;
        }
        else{
          parent = target.parentElement;
        }

        var classl = parent.classList;
        this.time = parseInt(parent.innerHTML);
        if(classl.contains("active")){
          return;
        }
        else{
          for(var key = 0, len = eleA.length; key < len; key++){
            eleA[key].classList.remove("active");
          }
          classl.add("active");
        }

      },
      handle(event){
        var evt = window.event || event;
        var target = evt.toElement || evt.srcElement;
        var value = target.value;
        var pattern =/^([1-9]*(\.)?\d*)$/;
        var pattern1 = /^[0-9]$/;
        var pattern2 = /^0(\.)\d*$/;
        var len = value.length;
        var flag;

        if(len == 1){
          flag = pattern1.test(value)
        }else{
          if(value[0] == 0){
            flag = pattern2.test(value)
          }else{
            flag = pattern.test(value)
          }
        }

        if(!flag){
//          this.number= value.slice(0, -1);
          this.number = "";
        }

        var t = value.toString();
        var ta = t.split(".");
        if(ta[1]){
          t = ta[1].slice(0,1);
          this.number = ta[0]+"."+t;
        }

      },
      handlep(event){
        var evt = window.event || event;
        var target = evt.toElement || evt.srcElement;
        var idx = target.dataset.id;
        var value = target.value;
        var len = value.length;
        var pattern =/^([1-9]*(\.)?\d*)$/;
        var pattern1 = /^[0-9]$/;
        var pattern2 = /^0(\.)\d*$/;
        var flag;
        if(len == 1){
          flag = pattern1.test(value)
        }else{
          if(value[0] == 0){
            flag = pattern2.test(value)
          }else{
            flag = pattern.test(value)
          }
        }
        if(!flag){
            if(idx == 0){
//              this.priceO= value.slice(0, -1);
              this.priceO = "";
            }else{
//              this.priceT= value.slice(0, -1);
              this.priceT = "";
            }
        }
      },
      submit(){

        if(this.skuName == '' || this.skuId == "" || this.number == "" || this.priceO == "" || this.time == "" || this.address == "" || this.priceT == ""){
          Toast('请您填写所有必填参数...')
          return;
        }
        var cityId ='',areaId ='',townId = '';
        var addrData = this.$store.state.addrSeekData;
        if(addrData){
          cityId = addrData.cityId;
          areaId = addrData.countyId;
          townId = addrData.townId;
        }

        var remark = document.getElementById("z-des").value;
        var wUnit = this.unit;
        var obj = {
          productType:'BUY',
          skuName:this.skuName,
          skuId:this.skuId,
          weight:this.number,
          startAmount:this.priceO,
          endAmount:this.priceT,
          timeout:this.time,
          address:this.address,
          weightUnit:wUnit,
          priceUnit:"元/斤",
          remark:remark,
          cityId:cityId,
          areaId:areaId,
          townId:townId
        }

        let params = {
          api:"/yg-product-service/product/addNewProduct.apec",
          data:obj
        }
        var that = this;
        this.post(params, fn.sumbit.bind(that))
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
      weightunit(event){
        var evt = window.event || event;
        var target = evt.toElement || evt.srcElement;
        var tag = target.tagName.toUpperCase(),parent;
        if(tag == "LI"){
          parent = target;
        }else{
          parent = target.parentElement;
        }
        var eleArr = document.querySelector(".ul-weight").children;
        [].forEach.call(eleArr,function (current) {
          var cl = current.classList;
          if(cl.contains("active-li")){
            cl.remove("active-li")
          }
        })

        parent.classList.add("active-li");
        var text = parent.children[0].textContent;
        this.unit = text;
      }
    },
    mounted(){

    },
    activated(){

      this.skuName = this.$store.state.skuName;
      this.skuId = this.$store.state.skuId;
      var addreObj = this.$store.state.addrSeekData;
      if(addreObj){
        this.address = addreObj.detailAddress;
      }

    },

  }
</script>
