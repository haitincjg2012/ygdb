<template>
  <div class="page page-current">
    <bar id="footerBar" v-if="navIndex">
      <bar-item @click.native="doAction(0, 0)" path="/home" label="首页" icon="home" name="home"></bar-item>
      <bar-item @click.native="doAction(0, 1)" path="/home" label="" icon="add" name="add"></bar-item>
      <bar-item @click.native="doAction(1, 2)" path="/pc" label="我的" icon="user" name="user"></bar-item>
    </bar>
    <transition>
      <keep-alive>
        <router-view style="min-height:100vh"></router-view>
      </keep-alive>
    </transition>
  </div>
</template>

<script>
  import bar from './components/Bar'
  import keyboard from './components/keyboard/keyboard'
  import bigImg from './components/common/bigImg'
  import barItem from './components/BarItem'
  import c_js from './assets/js/common'
  import $ from 'zepto'
  import axios from 'axios'
  import API from './api/api'
  import store from './store/store'
 
  const api = new API();

  export default {
    mounted () {
//        console.log(this,1);
    },
    data () {
      return {
          time:0
      }
    },
    activated(){
    },
    created() {
    },
    methods: {
      doAction(index ,idx) {
        this.go(index,idx);
      },
      go(index,idx){
//          var show = this.$store.state.show;
        this.$store.state.xqInfo = 0;
        switch (index) {
          case 0:
            var i = idx - 0;
            var obj = {};
            if(i == 0){
              obj.flag = idx;
            }else if( i == 1){
              if(this.time == 1){
                this.time = 0;
              }else{
                this.time = 1;
              }
              obj.flag = this.time;

            }

            this.$router.push({name: 'home',query:obj});

            break;
          case 1:
            var obj = {};
            obj.flag = idx;
            var Time = this.$store.state.Time;
            clearInterval(Time);
            this.$router.push({name: 'pc',query:obj});
            break;
          case 2:
            var Time = this.$store.state.Time;
            clearInterval(Time);
            this.$router.push({name: 'register'});
            break;
          case 3:
            var Time = this.$store.state.Time;
            clearInterval(Time)
            this.$router.push({name: 'userCenter'});
            break;
          default:
            var Time = this.$store.state.Time;
            clearInterval(Time)
            this.$router.push({name: 'home'});
            break;
        }
      },
      wx(){
        wx.config({
          debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
          appId: '', // 必填，公众号的唯一标识
          timestamp:'', // 必填，生成签名的时间戳
          nonceStr: '', // 必填，生成签名的随机串
          signature: '',// 必填，签名，见附录1
          jsApiList: ['onMenuShareTimeline'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        });
        wx.ready(function(){
          wx.checkJsApi({
            jsApiList: ['onMenuShareTimeline'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
            success: function(res) {
              // 以键值对的形式返回，可用的api值true，不可用为false
              // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}

              wx.onMenuShareTimeline({
                title: '', // 分享标题
                link: '', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                imgUrl: '', // 分享图标
                success: function () {
                  // 用户确认分享后执行的回调函数
                },
                cancel: function () {
                  // 用户取消分享后执行的回调函数
                }
              });
            }
          });
          });
      }

    },
    computed: {
      navIndex(){
          return this.$store.state.navIndex;
      }
    },
    components: {
      bar,
      barItem,
      keyboard,
      bigImg
    }

  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
/*  @import './assets/css/sm.css';*/
  @import './assets/css/reset.css'
  @import './assets/css/style.css'
  [v-cloak] {
    display: none;
  }
</style>
