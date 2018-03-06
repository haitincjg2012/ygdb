<template>
  <div class="c-addressC-c">
    <my-header class="c-address-header" @receive="reset"></my-header>
    <my-scroll class="c-address" :data="city?city:[]" ref="scrollBar">
      <div>
        <div v-for="item in city" @click="county(item)" class="c-address-content">{{item}}</div>
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
  export default{
    data(){
      return{
        city:null,
        province:"",
      }
    },
    methods:{
        reset(){
            this.city = null;
        },
      county(item){
          var self = this;
          localStorage.city = item;

        localStorage.removeItem("county");//清空区县
        localStorage.removeItem("town");//清空镇

        if(window.tree[self.province].hasOwnProperty("市辖区")){
          this.$router.push({name:"county",query:{
            province:self.province,
            city:"市辖区",
            county:item,
          }});
        }else{
          this.$router.push({name:"county",query:{
            province:self.province,
            city:item,
          }});
        }
         this.reset();
      },
      getCity(province){
        var self = this;
        var timeId = setInterval(function () {
          if(window.tree){
            clearInterval(timeId);
            self.city = [];
            if(window.tree[province].hasOwnProperty("市辖区")){
              for(var key in window.tree[province]["市辖区"]){
                self.city.push(key);
              }
            }else{
              for(var key in window.tree[province]){
                self.city.push(key);
              }
            }

          }

        }, 100);
      }
    },
    activated(){
      this.$refs.scrollBar.init();
      this.province = this.$route.query.province;
      this.getCity(this.province);
    },
    components:{
      "my-header":header,
      "my-scroll":scroll
    }
  }
</script>
