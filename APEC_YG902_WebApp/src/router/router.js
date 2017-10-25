import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '../store/store'
import c_js from '../assets/js/common'

Vue.use(VueRouter);


const router = new VueRouter({
  mode: 'hash',
  routes: [
    {
      name: 'home',
      path: '/home',
      component: require('../views/home/home'),
      meta: {requireNav: true}
    },
    {
      name: 'pc',
      path: '/pc',
      component: require('../views/pc/mainPCenter'),
      meta: {requireNav: true, requiresAuth: true}
    },
    {
      name: 'pcInfo',
      path: '/pcInfo',
      component: require('../views/pc/viewComponents/personInfo'),
      meta: {requiresAuth: true}
    },
    {
      name: 'messageList',
      path: '/messageList',
      component: require('../views/pc/viewComponents/messageList.vue'),
      meta: {requiresAuth: true}
    },
    {
      name: 'addressInfo',
      path: '/addressInfo',
      component: require('../views/pc/viewComponents/addressInfo.vue'),
      meta: {requiresAuth: true}
    },
    {
      name: 'uploadList',
      path: '/uploadList',
      component: require('../views/pc/viewComponents/uploadList.vue'),
      meta: {requiresAuth: true}
    },
    {
      name: 'viewUploadList',
      path: '/viewUploadList',
      component: require('../views/pc/viewComponents/viewUploadList.vue'),
      meta: {requiresAuth: true}
    },
     {//调果排行榜
       name: 'ranking',
       path: '/ranking',
       component: require('../views/pc/viewComponents/ranking.vue'),
       meta: {requiresAuth: true}
     },
    {//我的关注
      name: 'myAttention',
      path: '/myAttention',
      component: require('../views/pc/viewComponents/myAttention.vue'),
      meta: {requiresAuth: true}
    },
    {
      name: 'updateSecret',
      path: '/updateSecret',
      component: require('../views/pc/viewComponents/updateSecret.vue'),
      meta: {requiresAuth: true}
    },
    {
      name: 'mySpDe',
      path: '/mySpDe',
      component: require('../views/pc/viewComponents/mySpDe.vue'),
      meta: {requiresAuth: true}
    },
    {
      name: 'myCollect',
      path: '/myCollect',
      component: require('../views/pc/viewComponents/myCollect.vue'),
      meta: {requiresAuth: true}
    },
    {
      name: 'realNameAur',
      path: '/realNameAur',
      component: require('../views/pc/viewComponents/realNameAur.vue'),
      meta: {requiresAuth: true}
    },
    {
      name: 'pointDoc',
      path: '/pointDoc',
      component: require('../views/pc/viewComponents/pointDoc.vue'),
      meta: {requiresAuth: true}
    },
    {
      name: 'billList',
      path: '/billList',
      component: require('../views/pc/viewComponents/billList.vue'),
      meta: {requiresAuth: true}
    },
    {
      name: 'addressSel',
      path: '/addressSel',
      component: require('../views/pc/viewComponents/addressSel.vue'),
      meta: {requiresAuth: true}
    },
    {
      name: 'address',
      path: '/address',
      component: require('../views/home/viewComponents/address.vue'),
      meta: {requiresAuth: true}
    },
    {
      name: 'addrSeek',
      path: '/addrSeek',
      component: require('../views/home/viewComponents/addseek.vue'),
      meta: {requiresAuth: true}
    },
    {
      name: 'upPhone',
      path: '/updatePhone',
      component: require('../views/pc/viewComponents/updatePhone'),
      meta: {requiresAuth: true}
    },
    {
      name: 'myPoint',
      path: '/myPoint',
      component: require('../views/pc/viewComponents/myPoint'),
      meta: {requiresAuth: true}
    },
    {
      name: 'goodinfo',
      path: '/goodinfo',
      component: require('../views/home/viewComponents/goodinfo.vue'),
    },
    {
      name: 'login',
      path: '/login',
      component: require('../views/login/login'),
    },
    {
      name: 'register',
      path: '/register',
      component: require('../views/login/register.vue'),
    },
    {
      name: 'pwFindOut',
      path: '/pwFindOut',
      component: require('../views/login/pwFindOut.vue'),
    },
    {
      name: 'pwFindOutCom',
      path: '/pwFindOutCom',
      component: require('../views/login/pwFindOutCom.vue'),
    },
    {
      name: 'buy',
      path: '/buy',
      component: require('../views/home/viewComponents/buy.vue'),
    },
    {
      name: 'cold',
      path: '/cold',
      component: require('../views/home/viewComponents/cold.vue'),
    },
    {
      name: 'agency',
      path: '/agency',
      component: require('../views/home/viewComponents/agency.vue'),
    },
    {
      name: 'news',
      path: '/news',
      component: require('../views/home/viewComponents/news.vue'),
    },
    {
      name: 'newsDetail',
      path: '/newsDetail',
      component: require('../views/home/viewComponents/newsDetail.vue'),
    },
    {
      name: 'xqframe',
      path: '/xqframe',
      component: require('../views/home/viewComponents/xqframe.vue'),
      meta:{
        keepAlive:false
      }
    },
    {
      name: 'picShow',
      path: '/picShow',
      component: require('../views/home/viewComponents/picShow.vue'),
      meta:{
        keepAlive:false
      }
    },
    {
      name: 'trader',
      path: '/trader',
      component: require('../views/home/viewComponents/trader.vue'),
    },
    {
      name: 'personH',
      path: '/personH',
      component: require('../views/pc/viewComponents/personH.vue'),
    },
   {
      name: 'personXq',
      path: '/personXq',
      component: require('../views/pc/viewComponents/personXq.vue'),
    },
    {
      name: 'sell',
      path: '/sell',
      component: require('../views/home/viewComponents/supply.vue'),
    },
    {
      name: 'pbuy',
      path: '/pbuy',
      component: require('../views/home/viewComponents/seek.vue'),
      meta: {requiresAuth: true}
    },
    {
      name: 'supplyInfo',
      path: '/supplyInfo/:id',
      component: require('../views/home/viewComponents/supplyInfo.vue'),
    },
    {
      name: 'psell',
      path: '/psell',
      component: require('../views/home/viewComponents/publish.vue'),
      meta: {requiresAuth: true}
    },
    {
      name: 'goodinfo',
      path: '/goodinfo',
      component: require('../views/home/viewComponents/goodinfo.vue'),
    },
    {
      name:'goodDetail',
      path:'/goodDetail',
      component:require('../views/home/viewComponents/goodDetail.vue'),
    },
    {
      name: 'detail',
      path: '/detail/:id',
      component: require('../views/home/viewComponents/xq.vue'),
    },
    {
      name: 'activePage_one',
      path: '/activePage_one',
      component: require('../views/home/viewComponents/activePage_one.vue'),
    },
    {
      name: 'activePage_two',
      path: '/activePage_two',
      component: require('../views/home/viewComponents/activePage_two.vue'),
    },
    {
      name: 'activePage_three',
      path: '/activePage_three',
      component: require('../views/home/viewComponents/activePage_three.vue'),
    },
    {
      name: 'cold',
      path: '/cold',
      component: require('../views/home/viewComponents/cold.vue'),
    },
    {
      name: 'agency',
      path: '/agency',
      component: require('../views/home/viewComponents/agency.vue'),
    },
    {
      name: 'gq',
      path: '/gq',
      component: require('../views/home/viewComponents/gq.vue'),
      // meta: {requiresAuth: true}
    },
    {
      name: 'search',
      path: '/search',
      component: require('../views/home/viewComponents/fSearch.vue'),
    },
    {
      name: 'validate',
      path: '/validate',
      component: require('../views/home/viewComponents/validate.vue'),
    },
    {
      name: 'mainOperating',
      path: '/mainOperating',
      component: require('../views/pc/viewComponents/mainOperating.vue'),
    },
    {
      name: 'quotes',
      path: '/quotes',
      component: require('../views/pc/viewComponents/quotes.vue'),
    },
    {
      name: 'quotesMessage',
      path: '/quotesMessage',
      component: require('../views/pc/viewComponents/quotesMessage.vue'),
    },
    {
      path: '/', redirect: '/home'
    },
  ],
});

router.beforeEach((to, from, next) => {

  let toPath = to.path;
  let fromPath = from.path;
  let name = to.name;

  // let flag = to.query.flag;
  // let obj = {};
  // obj.flag = flag;
  // if (flag == 1) {
  //   obj.flag = flag;
  // }else{
  //
  // }
  // obj.name = name;
  // console.log(123);
  if (to.matched.some(record => record.meta.requireNav)) {//控制导航栏显现
    store.commit("incrementNavIndex", {'navIndex': true});
    // store.commit("incrementInit", obj);
  } else {
    store.commit("incrementNavIndex", {'navIndex': false});
  }


  next();

  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (store.state.token || c_js.getLocalValue('token')) {
      next();
    } else {
       let name1 = to.name;
       if(name1 != "search" || name1 != "buy"|| name1 != "sell" || name != "gq"){
         next({
           path: '/login',
           query: {redirect: to.fullPath}
         });
       }
    }
  }
  // else if (to.matched.some(record => record.meta.requiresUserInfoRes)) {
  //   if (window.localStorage.getItem('userInfoRes')=='true') {
  //     next();
  //   } else {
  //     next({
  //       path: '/userInfo',
  //       query: {redirect: to.fullPath}
  //     });
  //   }
  // } else {
  //   next();
  // }
});

router.afterEach(function (to) {
});

//输出router
export default  router;
