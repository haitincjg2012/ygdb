<template>
  <!--<div>test</div>-->
  <li class="comT c-gq">
    <div class="bread-com-f-T"
         :data-path = "item.path"
         :data-id = "item.id"
         @click="xq($event,item)">
      <div class="primaryMain clearfix" :data-id = "item.id">
        <div class="pic-com" :data-id = "item.id">
            <img :src ="item.img" :data-id = "item.id"/>
            <!--<div class="z-representative"  :data-id = "item.id">{{item.productTypeName}}</div>-->
            <!--<img :src = "item.productTypeName" :data-id = "item.id" class="z-representative" >-->
        </div>
        <div class="z-info clearfix" :data-id = "item.id">
          <div class="c-introduce" :data-id = "item.id">
            <div class="z-gq" :data-id = "item.id">
              <div class="z-gq-t" :data-id = "item.id" :class="{bg:item.bg}">{{item.gq}}</div>
            </div>
            <div class="c-sp-com c-skuName" :data-id = "item.id">
              <p class=" z-c-sku" :data-id = "item.id">
                {{item.skuName}}
              </p>
            </div>
          </div>
          <div class="c-pos" :data-id = "item.id" v-html="item.fruitdate">
            <!--<span class="g-sp-com g-first  g-agency" :data-id = "item.id" ></span>-->
            <!--<span class="g-sp-com g-first g-weight" :data-id = "item.id">{{item.weight}}</span>-->
          </div>
          <div class="c-level" :data-id = "item.id">
            <div class="c-l-price" :data-id = "item.id" v-html="item.price"></div>
            <div class="c-l-weight" :data-id = "item.id" v-html="item.weight"></div>
            <!--<div :data-id="item.id" class="z-g-agency">-->
              <!--<div class="z-g-d" :data-id = "item.id">-->
                <!--{{item.agency}}-->
              <!--</div>-->
            <!--</div>-->
            <!--<div class="g-level" :data-id = "item.id">-->
              <!--<div class="z-img" :data-id = "item.id">-->
                <!--<img :src="item.levelImg" :data-id = "item.id">-->
              <!--</div>-->
            <!--</div>-->

            <!--<div :data-id="item.id" class="z-g-name">-->
              <!--{{item.name}}-->
            <!--</div>-->
            <!--<div class="g-text" :data-id = "item.id"  :style="{width:item.addreeWh + 'rem'}">-->
              <!--{{item.local}}-->
            <!--</div>-->
          </div>
          <div class="c-location clearfix" :data-id = "item.id">
            <!--<span :data-id = "item.id" class="g-price-com-f" :class="{qg:item.qg, gy:item.gy}" v-if="item.indentification == 0 ?true:false">&yen;{{item.startAmount}}~{{item.endAmount}}</span>-->
            <!--<span :data-id = "item.id" class="g-price-com-f" :class="{qg:item.qg, gy:item.gy}" v-if="item.indentification == 1 ? true:false">&yen;{{item.amount}}</span>-->
            <!--<span class="g-sp-com g-unit" >{{item.priceUnit}}</span>-->
            <!--<span class="g-time g-right" :data-id = "item.id">{{item.showCredateTime}}</span>-->
            <!--<span class="g-eye-num g-right" :data-id = "item.id" >{{item.number}}人浏览</span>-->
            <div class="c-loc-addr">
              {{item.local}}
            </div>
            <div class="c-loc-time">
              {{item.showCredateTime}}
            </div>
            <div class="c-loc-view">
              {{item.number}}人浏览
            </div>
            <!--<span class="sp-com-one">1000斤</span>-->
          </div>
        </div>
      </div>
    </div>
  </li>

</template>
<style scoped>
  @import "../../../assets/css/goodlist.css";
</style>
<script>

  import API from '../../../api/api'
  const api = new API();
  var fn = {
      h:function () {

      }
  }
  export default{
      props:{
          item:""
      },
      methods:{
        xq(event,item){

          var evt = event || window.event;
          var target = evt.toElement || evt.srcElement;
          var cl = target.classList;
          var flag = cl.contains("g-star");
          var id = item.id;
          if(flag){
            var path = target.dataset.path;
            var flag = target.dataset.flag;
            if(flag == 0){
              this.item.star ++;
              this.item.starFlag = true;
//              fn.data[path].starFlag = true;
//              var star = fn.data[path].star + 1;
//              fn.data[path].star = star;
            }else{
//              fn.data[path].starFlag = false;
//              var star = fn.data[path].star - 1;
//              fn.data[path].star = star;
                 this.item.star --;
                 this.item.starFlag = false;
            }
            this.house(id, flag);

          }else{
            var scrollTop = document.body.scrollTop;
//            this.$store.state.xqInfoF = bd;

            if(item.Flag == "xq"){
              this.$emit("receive", id);
              this.$store.state.xqF = false;
            }else{
              this.$store.state.xqF = true;
              this.$emit("receive", scrollTop);
            }

            this.$store.state.xqF = true;
            this.$router.push({path:"detail",query:{id:id}});
          }

//          console.log(item.Flag == "xq", 99999);

        },
        house(id, flag){

          let params = {
//            api:"_node_user/saveFlag_save_product.apno",
            api:"/_node_user/_save_product.apno",
            data:{
              elasticId:id,
              saveFlag:flag
            }
          }

          this.post(params, fn.h);
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
//    computed:{
//
//    },

  }
</script>
