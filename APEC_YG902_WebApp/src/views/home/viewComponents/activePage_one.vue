<template>
  <div style="position: relative">
    <top-bar></top-bar>
    <div class="active-main-one">
      <scroller>
        <div class="page-cli-one">
          <img :src="img_one">
        </div>
        <div class="page-cli-two">
          <img :src="img_two">
        </div>
      </scroller>
    </div>
  </div>

</template>

<style lang="stylus" rel="stylesheet/stylus" scoped>
  _rem=20rem;
  .active-main-one
    position:fixed;
    top:0;
    left:0;
    height 100%;
    width 100%;
    .page-cli-one
    img
      display block
      height 100%;
      width 100%;
    .page-cli-two
    img
      display block
      height 100%;
      width 100%;
</style>

<script>
  import {MessageBox, Toast,Swipe, SwipeItem} from 'mint-ui';
  import API from '../../../api/api'
  import topBar from '../../../components/topBar/topBar'
  import img_one from  '../../../assets/img/activeF_one.jpg'
  import img_two from '../../../assets/img/activeF_two.jpg'

  const api = new API();
  export default{
    data(){
      return{
        img_one:img_one,
        img_two:img_two
      }
    },
    methods:{
    },
    activated(){
    },
    components: {
      topBar,
    }

  }
</script>
