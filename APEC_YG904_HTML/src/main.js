import Vue from 'vue'
import ElementUI from 'element-ui'
import router from './router/router.js'
import 'element-ui/lib/theme-default/index.css'
import store from '~/store/store' //引入vuex对象
import apiUrl from '~/api/url' //引入url
import axrequest from '~/api/request' //引入封装axios请求方式
import App from './App.vue'
import commonJs from '~/assets/js/common' //引入封装缓存方法

Vue.use(ElementUI);



//注册全局api对象，便于每个组件调用而不用每个组件import
Vue.prototype.apiUrl=apiUrl;
Vue.prototype.ax=axrequest;
Vue.prototype.storageJs=commonJs;

new Vue({
      render: h => h(App),
      router,
      store,
      data:{
        eventHub:new Vue()
      }

}).$mount('#app')




