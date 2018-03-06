<template>
  <div class="c-addressC-c">
    <my-header class="c-address-header" @receive="reset"></my-header>
    <my-scroll class="c-address" :data="county?county:[]" ref="scrollBar">
      <div>
        <div v-for="item in county" @click="town(item)" class="c-address-content">{{item}}</div>
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
        county:null,
        province:"",
        city:"",
        flag:0,
      }
    },
    methods:{
      reset(){
        this.county = null;
      },
      town(item){
        if(localStorage.city == item){
          localStorage.county = "";
        }else{
          localStorage.county = item;
        }

        localStorage.removeItem("town");//清空镇

        if(this.flag){
          this.$router.push({name:"pcInfo"});
        }else{
          this.$router.push({name:"town",query:{
            province:this.province,
            city:this.city,
            county:item
          }});
        }

        this.reset();
      },
      getCounty(province, city){
        var self = this;
        var timeId = setInterval(function () {
          if(window.tree){
            clearInterval(timeId);
            self.county = [];
             if(self.flag){
               self.county = window.tree[province][city][self.flag];
             }else{
                 for(var key in window.tree[province][city]){
                   self.county.push(key);
                 }
             }

          }

        }, 100);
      }
    },
    activated(){
      this.$refs.scrollBar.init();
       this.province = this.$route.query.province;
       this.city = this.$route.query.city;
       this.flag = this.$route.query.county;
       console.log(this.province,this.city,this.flag);

        this.getCounty(this.province, this.city);

    },
    components:{
      "my-header":header,
      "my-scroll":scroll
    }
  }
</script>
