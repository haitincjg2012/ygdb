<template>
    <div>
        <div class="z-s-header-O">
           <div class="z-title">{{title}}</div>
            <div class="return" @click="back">
                <img src="../../../assets/img/ret.png">
            </div>
        </div>
        <div >
             <p class="z-detail"><span>详情规格：</span></p>
             <table class="z-table">
                <tr v-for="item in items"
                   >
                    <td v-for="key in item">{{key}}</td>
                </tr>
             </table>
             <!--<ul class="ul-flex">-->
                  <!--<li v-for="item in items"-->
                  <!--&gt;-->
                      <!--<div>-->
                         <!--<p class="z-name">{{item.attrName}}</p>-->
                         <!--<p class="z-name-v">{{item.attrValue}}</p>-->
                      <!--</div>-->
                  <!--</li>-->
             <!--</ul>-->
        </div>
        <div class="z-artile">
          <p class="z-des"><span>描述:</span></p>
          <p>{{remark}}</p>
        </div>
        <div>
          <div class="z-imgs" v-for = "item in imgs">
            <img :src = "item.imageUrl">
          </div>
        </div>

    </div>
</template>
<style scoped>
@import "../../../assets/css/goodDetail.css";
</style>
<script>
  import API from '../../../api/api'
  const api = new API();
  var fn = {
    plain:function (data) {
        if(!data.succeed){
            return;
        }
      var dt = data.data;
          var title = "";
          var row = dt.productAttrs;
          var len = row.length;
//          var num = len / 3 == 0 ? (len/3) : Math.ceil(len/3);

          var num = 0;
          var dataArr = dt.productAttrs;
          var arr = [];
          var childArrN = [];
          var childValue = [];
          dataArr.forEach(function (current,index) {
            num ++;
            childArrN.push(current.attrName);
            childValue.push(current.attrValue);
             var idx = num % 3;
                 if(idx == 0){
                   arr.push(childArrN);
                   arr.push(childValue)
                   childArrN = [];
                   childValue = [];
                 }
                 if(num == len){
                   arr.push(childArrN);
                   arr.push(childValue)
                   childArrN = [];
                   childValue = [];
                 }

          });
      this.items = arr;
      this.remark = dt.remark;
      this.imgs = dt.productImages;
      this.title = dt.productTypeName + "商品";
      }
  }
   export default{
       data(){
           return {
               items:null,
             remark:"",
             imgs:null,
             title:''
           }
       },
     methods:{
       back(){
           this.$router.go(-1);
       },
       xq(){
         var that= this ;
         var id = this.$route.query.id;

         let params = {
           api:"/_node_product/_info.apno",
           data:{
             elasticId:id
           }
         };
         this.post(params, fn.plain.bind(that));
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
     },
     activated(){
          this.xq();
     }
   }
</script>
