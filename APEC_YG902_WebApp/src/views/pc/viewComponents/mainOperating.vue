<template>
   <div class="c-operating-pz">
       <div class="c-m-head">
           <div class="c-m-head-title">品种选择</div>
           <div @click.stop="back" class="c-pz-ret">
             <img src="../../../assets/img/ret.png">
           </div>
       </div>
       <div>
          <ul class="c-p-table clearfix">
             <li  v-for="item in itemPZ"
                 class="c-z-table-cell"
                 ref="tableCell">
               <div class="c-z-table-cell-content"  @click="option($event,item)">{{item.attrValue}}</div>
             </li>
          </ul>
       </div>
       <div class="c-pz-ok" @click="ok">
           确定
       </div>
   </div>
</template>
<style>
@import "../../../assets/css/mainOperating.css";
</style>
<script>
  import API from "../../../api/api"
  var api = new API();
  var fn = {};
  export default{
      data(){
          return{
              itemPZ:null,
              pzObj:{},
          }
      },
      methods:{
          init(){
            let params = {
              api:"/yg-goods-service/goods/getGoods.apec",
              data:{
              }
            }

            this.post(params,this.tableCell);
       },
        tableCell(data){
              var dt = data.data.goodsAttrVOList;
              var obj;
              dt.forEach(function (current) {
                if(current.goodsAttrName == "商品"){
                  obj = current.attributeValueVOS;
                }
              });

             this.itemPZ = obj;

        },
        initTableCell(){
          //初始化所有品种的事件
          this.itemPZ = null;
          this.pzObj = {};
        },
       back(){
         this.initTableCell();
           this.$router.go(-1);
       },
        option(e,item){
           var el = e.toElement || e.srcElement;
           if(el.classList.contains("c-z-table-cell-active")){
               el.classList.remove("c-z-table-cell-active");
              delete  this.pzObj[item.id];
           }else{
               el.classList.add("c-z-table-cell-active");
             this.pzObj[item.id] = item.attrValue;
           }

        },
        ok(){
            var str = "";
            var obj = this.pzObj
            for(var key in obj){
                var t = obj[key] + " ";
               str += t;
            }

          localStorage.pz = str;

          this.initTableCell();
          this.$router.go(-1);

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
        this.init();
      }
  }
</script>
