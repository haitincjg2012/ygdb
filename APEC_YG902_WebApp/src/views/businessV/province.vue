<template>
  <div class="c-addressC-p">
    <my-header class="c-address-header"></my-header>
    <my-scroll class="c-address" :data="province?province:[]" ref="scrollBar">
      <div>
        <div v-for="(item,index) in province" @click="city(item)" class="c-address-content">{{item}}</div>
      </div>
    </my-scroll>
  </div>
</template>
<style>
@import "../../assets/css/addressC.css";
</style>
<script>
  import scroll from "../../components/scroll/scrollbg.vue"
  import header from '../../components/header/headerTwo.vue'
  import API from '../../api/api'
  const api = new API();
  export default{
    data(){
      return{
        province:null,
        sign:undefined,
      }
    },
    methods:{
      city(item){
          if(this.sign){
            this.$store.state.homeProvince = item;
            this.$router.go(-1);
          }else{
            localStorage.province = item;//缓存省
            localStorage.removeItem("city");//清空市、区、县
            localStorage.removeItem("county")
            localStorage.removeItem("town")


            this.$router.push({name:"city",query:{province:item}});
          }

      },
      post(params, fn, flag, scrollTip){
        var vm=this;
        try {
          api.post(params).then((res) => {
            var data = res.data;
            fn(data);
          }).catch((error) => {
            console.log(error)
            if(flag){
              vm.newsShowFlag=true;
              Toast(error);
              // vm.$refs.newsTip.end("没有更多数据了");
              vm.$refs[scrollTip].end("网络异常了...");
            }
          })
        } catch (error) {
          console.log(error)
          if(flag){
            vm.newsShowFlag=true;
            Toast(error);
            vm.$refs[scrollTip].end("网络异常了...");
          }

        }
      },
      getProvince(){
          var self = this;
          var timeId = setInterval(function () {
              if(window.tree){
                clearInterval(timeId);
                self.province = self.province || [];
                for(var key in window.tree){
                  self.province.push(key);
                }
              }
          }, 100);

      },
    },

    activated(){
      if(!this.province){
        this.$refs.scrollBar.init();
        this.getProvince();
      }

      this.sign = this.$route.query.sign;
    },
    components:{
      "my-header":header,
      "my-scroll":scroll
    }
  }
</script>
