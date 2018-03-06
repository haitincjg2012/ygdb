<template>
  <div class="c-publish">
    <my-Mask :pageflag="sh2" v-show="zIndex">
      <div class="c-h-publish">
        <div class="c-h-p-add">
          <div class="c-h-p-addCom" @click="wsell">
            <img :src="sellPic">
            <p class="p-text">发布供应</p>
          </div>
          <div class="c-h-p-addCom" @click="wbuy">
            <img :src="buyPic">
            <p class="p-text">发布求购</p>
          </div>
          <!--<div class="c-h-p-addCom" @click="quotesJump">-->
            <!--<img :src="quotesPic">-->
            <!--<p class="p-text">发布行情</p>-->
          <!--</div>-->
        </div>
        <div class="c-h-p-cancel" @click="cancel">
            <img :src="cancelP">
        </div>
      </div>
    </my-Mask>
  </div>
</template>
<style>
@import "../../../assets/css/publishPage.css";
</style>
<script>
  import MaskF from '../../../components/mask.vue'
  //发布供应 发布求购 发布行情的图片
  import SellPic from '../../../assets/img/supply.png'//供应图片
  import BuyPic from '../../../assets/img/buy.png'//求购
  import QuotesPic from '../../../assets/img/quotes.png'//行情
  import Cancel from '../../../assets/img/home_cancel.png'//取消
  import API from '../../../api/api'
  const api = new API();
  export default{
      data(){
          return {
              sh2:true,
            buyPic:BuyPic,//求购的图片
            sellPic:SellPic,//供应的图片
            quotesPic:QuotesPic,//行情的图片
            cancelP:Cancel,//取消的图片
            zIndex:false,
          }
      },
      methods:{
        wbuy(){
//        this.$router.push({name: "pbuy"});
          var that = this;
          let params = {
            api:"/_node_user/_check_pronum.apno",
            data:{}
          }
//          clearInterval(this.Time);
          this.post(params, that.authT);
        },
        wsell(){
          var that = this;
          let params = {
            api:"/_node_user/_check_pronum.apno",
            data:{}
          }
//          clearInterval(this.Time);
          this.post(params, that.auth);
        },
        cancel(){
//        this.$router.push({name: 'home'});
           this.initializeState();
        },
        initializeState(){
            //初始化状态
          this.zIndex = false;
          this.$emit("changePublish");
        },
        post(params, fn){
          var vm=this;
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
        auth(data) {
          this.initializeState();
          if(!data.succeed){
            this.$router.push({name:'login'});
            return;
          }else{
            var dt = data.data;
            if(dt.checkStatus){
              this.$router.push({name: "psell",query:{Info:"SELL"}});
              return;
            }

            if(!dt.realInfo){
              this.$router.push({name: "validate",query:{id:1}});
              this.$store.state.check = "1";
              return;
            }
            if(!dt.realAuth){
              this.$store.state.check = "2";
              this.$router.push({name: "validate",query:{id:2}});
              return;
            }
          }
        },
        authT(data) {
          this.initializeState();
          if(!data.succeed){
            this.$router.push({name:'login'});
            return;
          }else{
            var dt = data.data;
            if(dt.checkStatus){
              this.$router.push({name: "pbuy",query:{Info:"BUY"}});
              return;
            }

            if(!dt.realInfo){
              this.$router.push({name: "validate"});
              this.$store.state.check = "1";
              return;
            }
            if(!dt.realAuth){
              this.$store.state.check = "2";
              this.$router.push({name: "validate"});
              return;
            }
          }
        },
        quotesJump(){
          //行情跳转
          this.initializeState();
          this.$router.push({name:'newsPublish'});
        },
      },
       created(){

       },
    components:{
      'my-Mask':MaskF,
    },
    computed:{

    },
    watch:{
           'publish':function (cur, old) {
//               console.log(cur, old);
             this.zIndex = cur;
           }
    },
    props:['publish']
  }
</script>
