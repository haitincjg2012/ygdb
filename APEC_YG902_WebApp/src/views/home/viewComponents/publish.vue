<template>
   <div class="z-frame" style="background: #fff;">
        <div class="com">
          <div class="header">
            <h1>发布供应信息</h1>
            <div class="return" @click="back">
              <img src="../../../assets/img/ret.png">
            </div>
          </div>
        </div>
     <div class="com">
         <div class="good-com clearfix" @click="goodinfo">
             <i class="z-star-O"></i>
             <label >商品信息:</label>
             <input id="g-info" class="input-com" disabled v-model="skuName" style="background: transparent;">
             <div class="z-pos-o">
               <img src="../../../assets/img/more-1.png"/>
             </div>
         </div>
     </div>
     <div class="com">
       <div class="good-com clearfix">
         <i class="z-star-O"></i>
         <label for="z-supply">供应量:</label>
         <input id="z-supply" class="input-com" @input="handle"  maxlength="5" v-model="number" placeholder="请填写供应量的数值">
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
       </div>
     </div>
     <div class="com">
       <div class="good-com clearfix">
         <i class="z-star-O"></i>
         <label for="z-price">供应价格:</label>
         <div class="z-f">
             <input id="z-price" class="input-price" v-model="price" @input="handlep" maxlength="4" placeholder="请填写单价">
             <span class="sp-O">元/斤</span>
         </div>

       </div>
     </div>
     <div class="com">
       <div class="good-com clearfix" @click="zone">
         <i class="z-star-O"></i>
         <label>货源区域:</label>
         <input id="g-area" class="input-com" disabled v-model="address">
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
         <label style="margin: 0">描述信息:</label>
         <!--<input id="z-des" class="input-com" placeholder="请输入描述"/>-->
         <textarea id="z-des" placeholder="请输入描述" rows="3" cols="20" ></textarea>
       </div>
     </div>
        <div class="pic-show">
            <p class="z-up-text">上传图片(* 建议横屏拍照)</p>
            <ul class="clearfix z-list-pic" >
                <li :is="item.SS" v-for="item in items" :src="item.src" :index="item.index" v-on:selecttype="delItems"></li>
                <li>
                    <div class="z-add-one">
                        <img src="../../../assets/img/add-9.png"/>
                      <input type="file" accept="image/*" capture="camera" @change="handleimg"/>
                    </div>
                </li>
            </ul>
        </div>
        <div class="submit-s" @click="submit" >
             提交
        </div>
   </div>
</template>
<style>
  @import "../../../assets/css/publish.css";
</style>
<script>
  import API from '../../../api/api'
  import DEL from '../../../components/del.vue'
  import { Toast } from 'mint-ui';

  const api = new API();
  var observer = {
      list:{},
      addObserver:function (mes,callback) {
        observer.list[mes] = observer.list[mes] || [];
        observer.list[mes].push(callback);
      },
      publisher:function (mes) {
           if(!observer.list[mes]){
               return;
           }
          for(var i = 0; i < observer.list[mes].length; i ++){
            observer.list[mes][i]();
          }
        observer.list[mes] = [];
      }
  }
  var fn = {
      picture:function (data) {
//          observer.publisher("upload");
        if(!data.succeed){
          return;
        }
        var d = new Date()
        var timeTamp = d.getTime();
        var dt = data.data[0];
       var that = this;
       var file = that.img;
        this.uploadImg = 2;
        var fr = new FileReader();
        fr.onload = function (e) {

          var value = e.target.result;

          var obj = {
            SS:DEL,
            srcM:dt[1].imagePath,
            src:value,
            srcT:dt[2].imagePath,
            index:timeTamp
          };
          that.items.push(obj);
        }
          fr.readAsDataURL(file);
      },
      sumbit:function (data) {
        if(!data.succeed){
          return;
        }
        this.number = "";
        this.price = "";
        this.$store.state.skuName = "";
        this.$store.state.skuId = "";
        this.$store.state.addrData = null;
        this.address = '';
        document.getElementById("z-des").value = "";
        var father = document.querySelector(".z-valid").children[0];
        var children = father.children;
        [].forEach.call(children, function (current) {
          current.classList.remove("active");
        });
        var eleArr = document.querySelector(".ul-weight").children;
        eleArr[0].classList.add("active-li");
        eleArr[1].classList.remove("active-li");
        this.$router.go(- 1);
//      this.$router.push({name:"home"})
    }
  }
  var uplaod = {
  };
  export default{
      data(){
        return{
            "skuName":'',
            skuId:'',
             "number":'',
                price:'',
              address:'',
               time:'',
              record:0,
              recordImg:{},
               items:[],
                unit:'万斤',
               uploadImg:0 //0代表没有上传图片点击图片 1代表上传了图片 2代表上传完图片后点击提交
        }
      },
    components: {"aaa": DEL},
    methods:{
      back(){
        this.$store.state.skuName = "";
        this.$store.state.skuId = "";
        this.$store.state.addrData = null;
        this.price ="";
        this.address = '';
        this.time = '';
        document.getElementById("z-des").value = "";
        var father = document.querySelector(".z-valid").children[0];
        var children = father.children;
        [].forEach.call(children, function (current) {
          current.classList.remove("active");
        });
        var eleArr = document.querySelector(".ul-weight").children;
        eleArr[0].classList.add("active-li");
        eleArr[1].classList.remove("active-li");
        this.$router.go(-1);
      },
      zone(){
        this.$store.state.address = 1;
        this.$router.push({name:'address'});
      },
      goodinfo(){
          this.$router.push("/goodinfo");
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
          this.number= value.slice(0, -1);
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
          this.price= value.slice(0, -1);
        }
      },
      handleimg(evt){

          var that = this;
          var files = evt.target.files || evt.dataTransfer.files;
          var file = files[0];
          var type = file.type;
          if(!type){
            Toast({
              message:"对不起，您上传的图片格式不对，请您重新上传图片",
              duration:1000
            })
          }
          var size = file.size/(1000*1000);
          var tpattern = /image\/(png|jpeg)/g;
          if(!tpattern.test(type)){
              return;
          }

          if(size > 10){
            Toast({
              message:"对不起，您上传的图片过大，请您重新上传图片",
              duration:1000
            })
              return;
          }
          var name = file.name;
          var nameA = name.split(".");
          var n = encodeURI(nameA[0]);
          var obj =this.recordImg ;
          if(obj.hasOwnProperty(n)){
              return;
          }
        var fd = new FormData();
        fd.append("whetherCompression",1);
        fd.append("file",file);
        let params = {
          api:"/common/uploadImg.apec",
          data:fd
        }
//        this.uploadImg = 1;
        that.img = file;
        that.postImg(params,fn.picture.bind(that));
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
        this.time = parent.innerHTML;
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
      postImg(params, fn){
        try {
          api.postImg(params).then((res) => {
            var data = res.data;
            fn(data);
          }).catch((error) => {
            Toast("网络不好，请您重新上传图片");
            console.log(error)
          })
        } catch (error) {
          console.log(error)
        }
      },
      delItems(timeTamp){
          var items = this.items;
          for(var key in items){
              if(items[key].index == timeTamp){
                  items.splice(key,1);
                  return;
              }
          }

      },
      submit(){
          var upload = this.uploadImg;
          var that = this;
          this.process();
//          if(upload == 1){
//            observer.addObserver("upload", this.process.bind(that));
//          }else{
//              this.process();
//          }

      },
      process(){
        if(this.skuName == '' || this.skuId == "" || this.number == "" || this.price == "" || this.time == "" || this.address == ""){

          Toast({
            message:"请您填写所有必填参数...",
            duration:1000
          })
          return;
        }
        this.uploadImg = 0;
        var imgArr = [];
        this.items.forEach(function (current, index) {
          var o = {
            imageUrl:current.srcT,
            sort:index
          }
          imgArr.push(o);
        });
        var cityId ='',areaId ='',townId = '';
        var addrData = this.$store.state.addrData;
        if(addrData){
          cityId = addrData.cityId;
          areaId = addrData.countyId;
          townId = addrData.townId;
        }
        var remark = document.getElementById("z-des").value;
        var src;
        if(this.items[0]){
            src = this.items[0].srcM
        }else{
            src = "";
        }
        var wUnit = this.unit;
        var obj = {
          productType:'SALE',
          skuName:this.skuName,
          skuId:this.skuId,
          firstImageUrl:src,
          weight:this.number,
          amount:this.price,
          timeout:parseInt(this.time),
          address:this.address,
          weightUnit:wUnit,
//          priceUnit:"元/"+ this.unit,
          priceUnit:'元/斤',
          productImages:imgArr,
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
        this.$store.state.skuName = "";
        this.$store.state.skuId = "";
        this.$store.state.addrData = null;
        this.post(params, fn.sumbit.bind(that))
      },
      showHide(){
          this.wShow = true;
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
//        this.unit = text.length == 2 ? text.slice(1):text;
        this.unit = text;

      }
    },
    activated(){
      this.skuName = this.$store.state.skuName;
      this.skuId = this.$store.state.skuId;
      var addreObj = this.$store.state.addrData;

      if(addreObj){
//        this.address = addreObj.cityName + addreObj.countyName + addreObj.townName;
        this.address = addreObj.detailAddress;
      }
    },
    watched:{
        write:"wr"
    },
  }
</script>
