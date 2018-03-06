<template>
  <div class="page page-current">
    <transition>
      <keep-alive>
        <router-view style="min-height:100vh"></router-view>
      </keep-alive>
    </transition>
    <bar id="footerBar" v-if="navIndex">
      <!--<bar-item @click.native.stop="doAction(1, 0)" path="/home" label="首页" icon="home" name="home"></bar-item>-->
      <!--<bar-item @click.native.stop="doAction(1, 1)" path="/findSHome" label="货源" icon="findSHome" name="findSHome"></bar-item>-->
      <!--<bar-item @click.native.stop="doAction(0, 1)" path="" label="" icon="add" name="add"></bar-item>-->
      <!--<bar-item @click.native.stop="doAction(1, 2)" path="/fruitCircle" label="果圈" icon="fruitCircle" name="fruitCircle"></bar-item>-->
      <!--<bar-item @click.native.stop="doAction(1, 3)"  label="我的" icon="user" name="pc"></bar-item>-->
      <!--<bar-item @click.native.stop="doAction(1, 0)" path="/home" label="首页" icon="home" name="home"></bar-item>-->
      <!--<bar-item @click.native.stop="doAction(1, 1)" path="/findSHome" label="货源" icon="findSHome" name="findSHome"></bar-item>-->
      <!--<bar-item @click.native.stop="doAction(0, 1)" path="" label="" icon="add" name="add"></bar-item>-->
      <!--<bar-item @click.native.stop="doAction(1, 2)" path="/fruitCircle" label="果圈" icon="fruitCircle" name="fruitCircle"></bar-item>-->
      <!--<bar-item @click.native.stop="doAction(1, 3)" path="" label="我的" icon="user" name="pc"></bar-item>-->
      <div class="c-footSty" @click="doAction('home')">
         <img :src="footControl.home.flag?footCheck.home.checked:footCheck.home.unchecked" class="c-f-img-f">
         <p class="c-f-text"  :class="{'c-footText-active':footControl.home.flag}">首页</p>
      </div>
      <div class="c-footSty" @click="doAction('findSHome')">
          <img :src="footControl.findSHome.flag?footCheck.findSHome.checked:footCheck.findSHome.unchecked" class="c-f-img-f">
        <p class="c-f-text"  :class="{'c-footText-active':footControl.findSHome.flag}">货源</p>
      </div>
      <div class="c-footSty" @click="doAction('add')">
        <img :src="footControl.add.flag?footCheck.add.checked:footCheck.add.unchecked" class="c-f-img-s">
      </div>
      <div class="c-footSty" @click="doAction('fruitCircle')">
          <img :src="footControl.fruitCircle.flag?footCheck.fruitCircle.checked:footCheck.fruitCircle.unchecked" class="c-f-img-f">
        <p class="c-f-text"  :class="{'c-footText-active':footControl.fruitCircle.flag}">果圈</p>
      </div>
      <div class="c-footSty" @click="doAction('pc')">
        <img :src="footControl.pc.flag?footCheck.pc.checked:footCheck.pc.unchecked" class="c-f-img-f">
        <p class="c-f-text" :class="{'c-footText-active':footControl.pc.flag}">我的</p>
      </div>
    </bar>
    <!--遮盖的浮框-->
    <my-publish :publish="exitPublish" v-on:changePublish="parentPage"></my-publish>

  </div>
</template>
<script>
  import bar from './components/Bar'
  import keyboard from './components/keyboard/keyboard'
  import bigImg from './components/common/bigImg'
  import barItem from './components/BarItem'
  import c_js from './assets/js/common'
  import publish from './views/home/viewComponents/publishPage.vue'//发布的遮盖框

  import homeIcon from './assets/img/home.png'
  import homeIconR from './assets/img/homeR.png'

  import goods from './assets/img/goodsD.png'//默认货源
  import goodsR from './assets/img/goods.png'//货源
  import add from './assets/img/add.png';//发布供求的图标
  import sub from './assets/img/sub.png';//
  import fruitCircleD from './assets/img/fruitCircleD.png'//默认果圈
  import fruitCircle from './assets/img/fruitCircle.png'//果圈
  import My from './assets/img/My.png';
  import MyR from './assets/img/MyR.png';

  import $ from 'zepto'
  import axios from 'axios'
  import API from './api/api'
  import store from './store/store'

  const api = new API();

  export default {
    mounted () {
    },
    data () {
      return {
          time:0,
          exitPublish:false,//发布供应 求购 行情
          test:false,
          footCheck:{
              home:{
                  //首页图标
                  checked:homeIconR,
                  unchecked:homeIcon,
              },
            findSHome:{
                  //货源图标
              checked:goodsR,
              unchecked:goods,
            },
            add:{
                  //供求买卖图标
              checked:sub,
              unchecked:add,
            },
            fruitCircle:{
                  //果圈图标
              checked:fruitCircle,
              unchecked:fruitCircleD,
            },
            pc:{
              //个人中心图标
              checked:MyR,
              unchecked:My,
            },
          },
          footControl:{
              'home':{
                flag:false,
               },
            'findSHome':{
              flag:false,
            },
           'add':{
             flag:false,
           },
            'fruitCircle':{
              flag:false,
            },
            'pc':{
              flag:false,
            }
          },
//          navIndex:true,
      }
    },
    created() {
       this.checkIcon();
    },
    methods: {
      doAction(type) {
          var that = this,
               hash = location.hash;
          switch(type){
            case 'home':
                if(that.footControl.home.flag){
                    return;
                }else{
                  that.initFControl(type);
                  that.$router.push({name: 'home'});
                }
                break;
            case 'findSHome':
              if(that.footControl.findSHome.flag){
                return;
              }else{
                that.initFControl(type);
                that.$router.push({name: 'findSHome'});
              }
                break;
            case 'add':
                that.exitPublish = true;
                break;
            case 'fruitCircle':
              if(that.footControl.fruitCircle.flag){
                return;
              }else{
                that.initFControl(type);
                that.$router.push({name: 'fruitCircle'});
              }
                break;
            case 'pc':
              if(that.footControl.pc.flag){
                return;
              }else{
//                that.getInfo();
                that.initFControl(type);
                that.$router.push({name: 'pc'});
              }
                break;
          }
//        this.go(index,idx);
      },
     initFControl(type) {
       var that = this;
        for(var key in that.footControl){
          if(key != type){
            that.footControl[key].flag = false;
          }else{
            that.footControl[key].flag = true;
          }
        }
      },
      checkIcon(type){
        var that = this,
          hash = type || location.hash;
        if(hash.indexOf("home") > -1){
          that.footControl.home.flag = true;
          return;
        }else if(hash.indexOf("findSHome") > -1){
          that.footControl.findSHome.flag = true;
          return;
        }else if(hash.indexOf("fruitCircle") > -1){
          that.footControl.fruitCircle.flag = true;
          return;
        }else if(hash.indexOf("pc") > -1){
          that.footControl.pc.flag = true;
          return;
        }
      },
//      getInfo(){//获取用户等级、积分等资料
//        const self = this;
//        let params = {
//          api: "/_node_user/_view.apno",
//          data: {}
//        }
//        try {
//          api.post(params).then((res) => {
//            var item = res.data;
////            if (item.succeed && item.data) {
////              var data = JSON.parse(item.data);
////              self.point = data.point;
////              self.userLevelKey = data.userLevelKey;
//////              self.userLevelName = self.userLevelKeySwitch(data.userLevelName);
////              self.userLevelName = IMG.methods.userLevel(data.userLevelName);
////              self.userRealAuthKey = data.userRealAuthKey;
////              self.userRealAuthName = data.userRealAuthName;
////
////              self.$store.commit("incrementPoint", {'point': data.point || 0});//积分
////              c_js.setLocalValue('point',data.point || 0);
////
////              self.$store.commit("incrementUserLevelName", {'userLevelName': data.userLevelName || ''});//等级积分 'QT'
////              c_js.setLocalValue('userLevelName',data.userLevelName || '');
////
////              self.$store.commit("incrementUserLevelKey", {'userLevelKey': data.userLevelKey || ''});//等级积分 '青铜'
////              c_js.setLocalValue('userLevelKey',data.userLevelKey || '');
////
////              self.$store.commit("incrementAurKey", {'userRealAuthKey': data.userRealAuthKey || ''});//验证情况
////              c_js.setLocalValue('userRealAuthKey',data.userRealAuthKey || '');
////
////            }else {
////              self.point = this.$store.state.point || c_js.getLocalValue('point');
////              self.userLevelKey = this.$store.state.userLevelKey || c_js.getLocalValue('userLevelKey');
////              self.userLevelName = IMG.methods.userLevel(this.$store.state.userLevelName || c_js.getLocalValue('userLevelName'));
////              self.userRealAuthKey = this.$store.state.userRealAuthKey || c_js.getLocalValue('userRealAuthKey');
////            }
//          }).catch((error) => {
//          })
//        } catch (error) {
//          console.log(error)
//        }
//      },
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
      },
      parentPage(){
        this.exitPublish = false;
      }

    },
//    activated(){
//        console.log(6666);
//        var self = this;
//        console.log(this, 7777);
//        var name = localStorage.name;
//       if(name){
//         self.test =  true;
//       }else
//         self.test = false;
//        console.log(name, 8888);
//    },
    computed: {
      navIndex(){
        return this.$store.state.navIndex;
      },
    },
    watch:{
        '$route':function (to, from) {
          var that = this,
               type = to.name;
          that.initFControl(type);
        }
    },
    components: {
      bar,
      barItem,
      keyboard,
      bigImg,
      'my-publish':publish,
    }

  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
/*  @import './assets/css/sm.css';*/
  @import './assets/css/main.css'
  @import './assets/css/reset.css'
  @import './assets/css/style.css'
  [v-cloak] {
    display: none;
  }
</style>
