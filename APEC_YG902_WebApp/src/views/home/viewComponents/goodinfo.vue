<template>
   <div class="app1">
       <div class="header-goods">
           <h1>选择商品信息</h1>
           <div class="return-goods" @click="back">
             <img src="../../../assets/img/ret.png">
           </div>
       </div>
       <div style="background: #fff;">
         <div class="history-com">
             <div class="z-history clearfix ">
               <p class="z-text">历史浏览记录</p>
               <div v-for = "item in historys"
                        class="c-history-bs"
                         >
                         <ul class="clearfix">
                             <li :data-id="item.skuId" @click="historyRecord">
                                 {{item.skuName}}
                             </li>

                         </ul>
               </div>
             </div>
         </div>
         <div class="z-list-d">
           <div class="ul-list"
                v-for = "item in goodinfos"
                :data-id = "item.attrId"
           >
             <h6>{{item.goodsAttrName}}<span v-if="item.mustRequired">(必填)</span></h6>
             <ul class="clearfix" @click="required">
               <li v-for = "(classification,index) in item.attributeValueVOS"
                   :data-id="classification.id"
                   :data-sort ="item.sort"
                   :data-path ="classification.sort"
                   :data-value ="classification.attrValue"
                   :data-required ="item.mustRequired"
               >
                 <img src="../../../assets/img/hack.png">{{classification.attrValue}}
               </li>
             </ul>
           </div>
         </div>

         <div class="z-btn">
           <ul class="clearfix">
             <li @click="reset">重置</li>
             <li @click="yes">确定</li>
           </ul>
         </div>
       </div>

   </div>

</template>
<style scoped>
@import "../../../assets/css/goodinfo.css";

</style>
<style>

</style>
<script>
  import API from '../../../api/api'
  import {Toast} from 'mint-ui'
  const api = new API();
  var enumer = [];
  var record = {};
  var fn = {
      message:function (data) {
        if(!data.succeed){
            Toast(data.errorMsg);
          return;
        }
          var dt = data.data.goodsAttrVOList;
        this.goodinfos = dt;
        dt.forEach(function (current) {
             var obj = {path:"",
               value:{
               attrId:"",
               attrName:"",
               attributeType:"",
               attributeShowLevel:"",
               sort:"0",
               attrValueId:"",
               attrValue:""
             },
             required:false
             };
          obj.required = current.mustRequired;
//             obj.value.attrValue = current.attributeValueVOS[0].attrValue;
             obj.value.attrValueId = current.attributeValueVOS[0].id;
             obj.value.attrId = current.attrId;
             obj.value.attrName = current.goodsAttrName;
             obj.value.attributeType = current.attributeType;
             obj.value.attributeShowLevel = current.attributeShowLevel;
             obj.value.sort = current.sort;
             record[current.attrId] = obj;
        });

        this.RECORD = record;
      },
      r:function (data) {
        if(!data.succeed){
          return;
        }

        this.$store.state.skuName = data.data[0].skuName;
        this.$store.state.skuId = data.data[0].skuId;
        this.$store.state.skuFlag = true;

        this.$router.go(-1);
      },
      hr:function (data) {
        this.$store.state.skuName = data.skuName;
        this.$store.state.skuId = data.skuId;
        this.$store.state.skuFlag = true;
        this.$router.go(-1);
      },
      historyShow:function (data) {
        if(!data.succeed){
          return;
        }
        var arr = [];
        var dt = data.data;
        dt.forEach(function (current) {

            var obj = {
                skuName:'',
                skuId:''
            }
            obj.skuName = current.skuName;
            obj.skuId = current.skuId;
            arr.push(obj);
        });
        if(dt.length > 0){
          this.historys = arr;
        }

      }
  }
   export default{
      data(){
        return{
          goodinfos:[],
          historys:null,
          RECORD:null,
        }
      },
       methods:{
         back(){
//             var path = this.$route.query.path;
//             this.$router.push({name:path,query:{back:"GQ"}});

               this.$router.go(-1);
           },
         historyRecord(event){
           var evt = event || window.event;
           var target = evt.toElement || evt.srcElement;
           var tag = target.tagName.toUpperCase(), parent,grand;
           if(tag == 'LI'){
             parent = target;
           }
           else{
             return;
           }

           var id = parent.dataset.id;
           var text = parent.innerHTML;
           var t = text.replace(/(^\s*)|(\s*$)/g, "");
           var obj = {
             skuName:t,
             skuId:id
           }

            fn.hr.bind(this)(obj);

         },
         required(event){
               var evt = event || window.event;
               var target = evt.toElement || evt.srcElement;
               var tag = target.tagName.toUpperCase(), parent,grand;

               if(tag == 'LI'){
                   parent = target;
               }
               else{
                   parent = target.parentElement;
               }

               if(parent.classList.contains("active")){
                   return;
               }else{

                   var p = parent.parentElement.children;
                    [].forEach.call(p,(function (current) {
                      current.classList.remove("active");
                    }));

                   parent.classList.add("active");

                   var pid = parent.dataset.id;
                   var pvalue = parent.dataset.value;
                   var psort= parent.dataset.sort;
                   var path = parent.dataset.path;
                   var grand = target.parentElement.parentElement;
                   var gid = grand.dataset.id;

                   record[gid].value.attrValueId = pid;
                   record[gid].value.attrValue = pvalue;
                   record[gid].value.sort = psort;
                   record[gid].path = path;
               }
         },
         reset(){
//           var eleA = document.querySelectorAll(".ul-list");
            var eleA = document.querySelector(".z-list-d");
            var children = eleA.children;
            var self = this;
            [].forEach.call(children, function (current) {
              var id = current.dataset.id;
              var path = self.RECORD[id].path;
              if(path == ""){
                return;
              }else{

                var ul = current.children[1],child;
//               console.dir(current, 11);
//               console.log(record[id]);
                child = ul.children[path];
                child.classList.remove("active");
//                var cChild =  ul.children[0];
//                cChild.classList.add("active");
//                var pid = cChild.dataset.id;
//                var pvalue = cChild.dataset.value;
//                var psort = cChild.dataset.path;

                self.RECORD[id].value.attrValueId = "";
                self.RECORD[id].value.attrValue = "";
                self.RECORD[id].path = "";
              }
            })
//           eleA.forEach(function (current) {
//              var id = current.dataset.id;
//              var path = record[id].path;
//              if(path == 0){
//                  return;
//              }else{
//
//                var ul = current.children[1],child;
//                child = ul.children[path];
//                child.classList.remove("active");
//                var cChild =  ul.children[0];
//                cChild.classList.add("active");
//                var pid = cChild.dataset.id;
//                var pvalue = cChild.dataset.value;
//                var psort = cChild.dataset.path;
//
//                record[id].value.attrValueId = pid;
//                record[id].value.attrValue = pvalue;
//                record[id].value.sort = psort;
//                record[id].path = psort;
//              }
//           })

         },
         yes(){
             var arr = [];
               for(var key in record){
                if(record[key].required){
                    if(record[key].value.attrValue == ""){
                      Toast('请您填写必选项...')
                        return;
                    }
                }
                if( record[key].value.attrValue != ""){
                  arr.push(record[key].value);
                }
               }
             let params = {
                 api:"/yg-product-service/product/addProductAttr.apec",
                 data:{
                     "productAttrs":arr
                 }
             };
//           Toast('数据正在传输中，请您耐心等待...')
            var that = this;
             this.post(params,fn.r.bind(that));

         },
         history(){
           let params = {
             api: "/_node_user/_product_his.apno",
             data: {}
           }
           this.post(params,fn.historyShow.bind(this));
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
         }
       },
     activated(){

       this.goodinfos = [];
       let params = {
         api:"/yg-goods-service/goods/getGoods.apec",
         data:{
//                  "id":"192338805133376"
         }
       }
       this.post(params,fn.message.bind(this));
       this.history();
       this.reset();

       var el = document.querySelector(".page");
       if(el){
         var flag =  el.classList.contains("z-scroll");
         if(flag){
           el.classList.remove("z-scroll");
           el.classList.add("z-scroll-y");
         }else{
           el.classList.add("z-scroll-y");
         }
       }
     },
     mounted(){
//         let params = {
//             api:"/yg-goods-service/goods/getGoods.apec",
//                data:{
////                  "id":"192338805133376"
//                }
//            }
//
//       this.post(params,fn.message.bind(this));
//       this.history();
     }
   }
</script>
