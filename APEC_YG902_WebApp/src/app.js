import Vue from 'vue'
import VueResource from 'vue-resource'
import router from './router/router'
import app from './main'
import store from './store/store'
import vmodal from 'vue-js-modal'
import VueScroller from 'vue-scroller'
import vueg from 'vueg'
import 'vueg/css/transition-min.css'
import './assets/js/dateFormat.js'


//mint-ui
import Mint from 'mint-ui'
import 'mint-ui/lib/style.css'
Vue.use(Mint);

//滚东条
Vue.use(VueScroller);

//模态框
Vue.use(vmodal)

const options = {
  duration: '0.3',              //转场动画时长，默认为0.3，单位秒
  firstEntryDisable: false,     //值为true时禁用首次进入应用时的渐现动画，默认为false
  firstEntryDuration: '.6',     //首次进入应用时的渐现动画时长，默认为.6
  forwardAnim: 'fadeIn',   //前进动画，默认为fadeInRight
  backAnim: 'fadeIn',       //后退动画，默认为fedeInLeft
  sameDepthDisable: false,      //url深度相同时禁用动画，默认为false
  tabs: [{
    name: 'home'
  }, {
    name: 'pc'
  }, {
    name: 'viewUploadList'
  }, {
    name:'messageList'
  },{
    name:'realNameAur'
  },{
    name:'updateSecret'
  },{
    name: 'pcInfo'
  }, {
    name: 'upPhone'
  }, {
    name: 'addressInfo'
  }, {
    name: 'uploadList'
  }, {name: 'addressSel'}, {name: 'billList'}],                       //默认为[]，'name'对应路由的name,以实现类似app中点击tab页面水平转场效果，如tabs[1]到tabs[0]，会使用backAnim动画，tabs[1]到tabs[2]，会使用forwardAnim动画
  tabsDisable: false,           //值为true时，tabs间的转场没有动画，默认为false
  shadow: false,                  //值为false，转场时没有阴影的层次效果
  disable: false,               //禁用转场动画，默认为false，嵌套路由默认为true
}
Vue.use(vueg, router, options);

Vue.use(VueResource);


new Vue({
  store,
  router,
  render: h => h(app),
  data: {
    eventHub: new Vue()
  }
}).$mount('#app')
