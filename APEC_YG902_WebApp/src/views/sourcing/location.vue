<template>
 <div class="c-city-location">
     <my-header class="c-city-header-feature">
       <h1 slot="pack" class="c-city-header">选择城市</h1>
     </my-header>
     <h6 class="c-subtitle-city">当前城市</h6>
     <div class="c-city-feature">{{city}}</div>
     <h6 class="c-subtitle-city">所有城市</h6>
     <my-scroll class="c-location-scroll" :data="cityArr" ref="cityWrapper">
       <div>
          <div v-for="item in cityArr" @click="setCity(item)" class="c-city-feature c-city-charactics">
             {{item}}
          </div>
       </div>
     </my-scroll>
 </div>
</template>
<style>
@import '../../assets/css/location.css';
</style>
<script>
  import header from '../../components/header/headerTwo.vue'
  import scroll from '../../components/scroll/scrollbg.vue'

  export default{
      data(){
          return {
              city:"",//所在城市
              cityArr:['烟台市', '威海市'],
          }
      },
     created(){
       this.$nextTick(function () {
         this.$refs.cityWrapper.init();
       })
     },
      methods:{
          setCity(city){
            this.city = city;
            this.$store.state.cityName = city;
            this.$router.go(-1);
          },
      },
      activated(){

          this.city = this.$route.query.city;
      },
    components:{
       'my-header':header,
       'my-scroll':scroll,
    }
  }
</script>
