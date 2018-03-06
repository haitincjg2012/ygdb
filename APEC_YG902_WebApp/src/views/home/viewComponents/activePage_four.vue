<template>
  <div>
    <top-bar title="猜果价,赢积分"></top-bar>
    <div class="active-main-one">
      <scroller>
        <div class="page-cli-one">
          <img :src="img_one">
        </div>
      </scroller>
    </div>
  </div>

</template>

<style lang="stylus" rel="stylesheet/stylus" scoped>
  _rem=20rem;
  .active-main-one
    position:fixed;
    top:42px;
    left:0;
    height 100%;
    width 100%;
    .page-cli-one
    img
      display block
      height 100%;
      width 100%;
</style>

<script>
  import {MessageBox, Toast,Swipe, SwipeItem} from 'mint-ui';
  import API from '../../../api/api'
  import topBar from '../../../components/topBar/topBar'
  import img_one from  '../../../assets/img/guess.png'

  const api = new API();
  export default{
    data(){
      return{
        img_one:img_one,
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
