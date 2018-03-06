import Vue from 'vue'
import ElementUI from 'element-ui'
import router from './router/router.js'
// import 'element-ui/lib/theme-default/index.css'
import 'element-ui/lib/theme-chalk/index.css'
import store from '~/store/store' //引入vuex对象
import apiUrl from '~/api/url' //引入url
import axrequest from '~/api/request' //引入封装axios请求方式
import App from './App.vue'
import commonJs from '~/assets/js/common' //引入公共js方法
import myfilter from '~/filters/filter' //引入公共Filter方法
import staticUrl from '~/api/staticurl' //引入静态数据
import keys from '~/config/keyValue'//公用键值对
import filters from '~/config/filters'//公用键值对
import objCopy from '~/api/objectCopy'//公用键值对
import moment from 'moment'

Vue.use(ElementUI);
//注册全局api对象，便于每个组件调用而不用每个组件import
Vue.prototype.apiUrl=apiUrl;
Vue.prototype.ax=axrequest;
Vue.prototype.commonJs=commonJs;
Vue.prototype.staticUrl=staticUrl;
Vue.prototype.objCopy=objCopy;
Vue.prototype.moment = moment;
Vue.prototype.numberOnly = function(){
	var val = event.target.value.replace(/\D/g,"");
	var ev = event.target;
	setTimeout(function(){
		ev.value = val;
	})
};
//交易模块查看上传人
Vue.prototype.goto = function(row, routerName){
	router.push({ name: 'uploader', params: { id: row.userId, router: routerName}})
}

//检查从举报关联单号过来的页面
Vue.prototype.checkFromWhistleBlowing = function(vm, fucName, strName, str2){
	let id = sessionStorage.getItem('relatedIds');
	if(id){
		sessionStorage.removeItem('relatedIds');
		var obj = {};
		obj[strName] = id;
		if(str2){
			vm[fucName](obj, str2);
		}else{
			vm[fucName](obj);
		}
	}
};

//公用键值对
Vue.prototype.keys = keys;
//过滤函数
Vue.prototype.filters = filters;

new Vue({
      render: h => h(App),
      router,
      store,
      data:{
        eventHub:new Vue()
      }

}).$mount('#app');
