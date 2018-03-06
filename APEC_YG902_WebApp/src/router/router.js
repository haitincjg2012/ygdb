import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '../store/store'
import c_js from '../assets/js/common'

Vue.use(VueRouter);


const router = new VueRouter({
  mode: 'hash',
  // mode: 'history',
  routes: [
    {
      name: 'home',
      path: '/home',
      component: require('../views/home/home'),
      meta: {requireNav: true}
    },
    {
      name: 'add',
      path: '/add',
      component: require('../views/home/home.vue'),
      meta: {requireNav: false}
    },
    {
      name: 'pc',
      path: '/pc',
      component: require('../views/pc/mainPCenter'),
      meta: {requireNav: true, requiresAuth: true}
    },
    {
      name: 'pictureOriginal',
      path: '/pictureOriginal',
      component: require('../views/businessV/pictureOriginal'),
    },
    {
      name: 'pcInfo',
      path: '/pcInfo',
      component: require('../views/pc/viewComponents/personInfo'),
      // meta: {requiresAuth: true}
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
      name: 'edit',
      path: '/edit',
      component: require('../views/pc/viewComponents/edit.vue'),
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
    {//新闻发布
      name: 'newsPublish',
      path: '/newsPublish',
      component: require('../views/home/viewComponents/newsPublish.vue'),
    },
    {//发表评论
      name: 'newsComm',
      path: '/newsComm',
      component: require('../views/home/viewComponents/newsComm.vue'),
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
      path: '/detail/',
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
      name: 'activePage_four',
      path: '/activePage_four',
      component: require('../views/home/viewComponents/activePage_four.vue'),
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
      component: require('../views/home/viewComponents/search.vue'),
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
      name: 'setting',
      path: '/setting',
      component: require('../views/pc/viewComponents/setting.vue'),
    },
    {
      name: 'fruitCircle',
      path: '/fruitCircle',
      component: require('../views/fruit/fruitCircle.vue'),
      meta: {requireNav: true}
    },
    {
      name: 'publishPost',
      path: '/publishPost',
      component: require('../views/fruit/publishPost.vue'),
    },
    {
      name: 'socialDetail',
      path: '/socialDetail',
      component: require('../views/fruit/socialDetail.vue'),
    },
    {
      name: 'newsfruitComment',
      path: '/newsfruitComment',
      component: require('../views/fruit/newsfruitComment.vue'),
    },
    {
      name: 'morePost',
      path: '/morePost',
      component: require('../views/fruit/morePosts.vue'),
    },
    {
      name: 'myTidings',
      path: '/myTidings',
      component: require('../views/fruit/myTidings.vue'),
    },
    //消息管理-主页面
    {
      name: 'messageInfo',
      path: '/messageInfo',
      component: require('../views/comm/messageInfo.vue'),
    },
    //消息管理-赞
    {
      name: 'praise',
      path: '/praise',
      component: require('../views/comm/viewComponents/praise.vue'),
    },
    //消息管理-评论
    {
      name: 'comments',
      path: '/comments',
      component: require('../views/comm/viewComponents/comments.vue'),
    },
    //消息管理-系统消息
    {
      name: 'sysmessage',
      path: '/sysmessage',
      component: require('../views/comm/viewComponents/sysmessage.vue'),
    },
    {
      name: 'findSHome',
      path: '/findSHome',
      component: require('../views/sourcing/findSHome.vue'),
      meta: {requireNav: true}
    },
    //果价猜猜猜
    {
      name: 'priveMovement',
      path: '/priveMovement',
      component: require('../views/home/viewComponents/priceMovement.vue'),
    },
    //省
    {
      name: 'province',
      path: '/province',
      component: require('../views/businessV/province.vue'),
    },
    // 市
    {
      name: 'city',
      path: '/city',
      component: require('../views/businessV/city.vue'),
    },
    // 区
    {
      name: 'county',
      path: '/county',
      component: require('../views/businessV/county.vue'),
    },
    //镇
    {
      name: 'town',
      path: '/town',
      component: require('../views/businessV/town.vue'),
    },
    //定位所在的城市
    {
      name: 'location',
      path: '/location',
      component: require('../views/sourcing/location.vue'),
    },
    //果满仓的商品详情
    {
      name: 'sourcingXq',
      path: '/sourcingXq',
      component: require('../views/sourcing/sourcingXq.vue'),
    },
    {
      name: 'warn',
      path: '/warn',
      component: require('../views/home/viewComponents/warn.vue'),
    },
    //二维码扫描注册果来乐
    {
      name: 'Twocode',
      path: '/Twocode',
      component: require('../views/register/TwoCode.vue'),
    },
    {
      name:'invite',
      path:'/invite',
      component:require('../views/register/invite.vue'),
    },
    {
      name:'qrcode',
      path:'/qrcode',
      component:require('../views/register/inviteRegist.vue'),
    },
    //活动运营一
    {
      name:'oHomeF',
      path:'/oHomeF',
      component:require('../views/operateF/home.vue'),
    },
    {
      name: 'test',
      path: '/test',
      component: require('../views/home/viewComponents/test.vue'),
    },
    // {
    //   name:'revise',
    //   path:'/revise',
    //   component:require('../views/operateF/revise.vue'),
    // },
    // {
    //   name:'compose',
    //   path:'/compose',
    //   component:require('../views/operateF/composePicture.vue'),
    // },
    {
      path: '/', redirect: '/home'
    },
  ],
});

router.beforeEach((to, from, next) => {

  let toPath = to.path;
  let fromPath = from.path;
  let name = to.name;


  if (to.matched.some(record => record.meta.requireNav)) {//控制导航栏显现
    store.commit("incrementNavIndex", {'navIndex': true});
  } else {
    store.commit("incrementNavIndex", {'navIndex': false});
  }

  var fakePath = window.location.search;//url?后面的字符
  var path = '',
      pos = fakePath.indexOf("FakePath");

  if(pos > -1){
    var strArr = fakePath.split("=");
    strArr.splice(0, 1);
    path =window.location.origin + "/#/" + strArr.join("=");
    localStorage.wx = true;
    window.location.replace(path);
    return;
  }


  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (store.state.token || c_js.getLocalValue('token')) {
      next();
    }else {
       let name1 = to.name;
       if(name1 != "search" || name1 != "buy"|| name1 != "sell" || name != "gq"){
         next({
           path: '/login',
           query: {redirect: to.fullPath}
         });
       }
    }
  }
  else{
    next();
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
