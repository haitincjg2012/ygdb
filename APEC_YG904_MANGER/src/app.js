import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-default/index.css'
// import App from './App.vue'
import VueRouter from 'vue-router'
import F from './component/view/main/Frame.vue'
import Login from './component/view/login/Login.vue'
import R from './component/view/main/r.vue'
import G from './component/view/goodc/good.vue'
import Member from './component/view/customerc/member.vue'
import Addr from './component/view/customerc/addRouter.vue'
import BaseInfo from './component/view/customerc/addMember.vue'
import MemberAuthentication from './component/view/customerc/authentication.vue'
import Report from './component/view/customerc/report.vue'
import DeliveryAdministrationv from './component/view/tradec/DeliveryAdministration.vue'
import Supply from './component/view/tradec/supply.vue'
import Buy from './component/view/tradec/buy.vue'
import Analysis from './component/view/datac/analysis.vue'
Vue.use(ElementUI)
Vue.use(VueRouter)

const routes = [
  {path:'/',redirect:'/M'},
   {path:'/router',component:R,children:[
       {path:'/F',component:F,children:[
           {path:'/Good',name:'good',component:G},
           {path:'/M',name:"member",component:Member},
           {path:'/Addr',name:'add',component:Addr,children:[
               {path:'/BaseInfo',name:'base',component:BaseInfo}
           ]},
           {path:'/MemberAuthentication',name:'Authentication',component:MemberAuthentication},
           {path:'/Report',name:'report',component:Report},
           {path:'/DeliveryAdministrationv',name:'Delivery',component:DeliveryAdministrationv},
           {path:'/Supply',name:'supply',component:Supply},
           {path:'/Buy',name:'buy',component:Buy},

           {path:'/Analysis',name:'analysis',component:Analysis}
       ]},
       {path:'/Login',name:'login',component:Login},

   ]}
]

const router = new VueRouter({
     routes
});
new Vue({
      router,
  render: h => h(R)
}).$mount("#app");


