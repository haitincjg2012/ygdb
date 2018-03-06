<template>
  <div class="c-addressC-c">
    <my-header class="c-address-header" @receive="reset"></my-header>
    <my-scroll class="c-address" :data="town?town:[]" ref="scrollBar">
      <div>
        <div v-for="item in county" @click="pc(item)" class="c-address-content">{{item}}</div>
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
        county:"",
        province:"",
        city:"",
        town:null,
      }
    },
    methods:{
      reset(){
        this.town = null;
      },
      pc(item){
          this.reset();
          localStorage.town = item;
        this.$router.push({name:"pcInfo"});
      },
      getCounty(province, city,county){
        var self = this;
        var timeId = setInterval(function () {
          if(window.tree){
            clearInterval(timeId);
            self.county = [];
            self.county = window.tree[province][city][county];
          }

        }, 100);
      }
    },
    activated(){
      this.$refs.scrollBar.init();
      this.province = this.$route.query.province;
      this.city = this.$route.query.city;
      this.county = this.$route.query.county;
      if(!this.town){
        this.getCounty(this.province, this.city, this.county);
      }

    },
    components:{
      "my-header":header,
      "my-scroll":scroll
    }
  }
</script>

