/**
 * Created by gsy on 2017/8/18.
 * 路由
 */
import Vue from 'vue'
import Router from 'vue-router'

import Login from '~/views/login/Login.vue'
import common from '~/views/common/common.vue'
import home from '~/views/home/home.vue'
//客户模块
import member from '~/views/member/member.vue'
import certificate from '~/views/member/certificate.vue'
import company from '~/views/member/company.vue'
//交易模块
import newsIssue from '~/views/transaction/newsIssue.vue'
import supplyDemand from '~/views/transaction/supplyDemand.vue'
import settlement from '~/views/transaction/settlement.vue'
import statistic from '~/views/transaction/settlement/statistic.vue'
import moments from '~/views/transaction/moments.vue'
import whistleBlowing from '~/views/transaction/whistleBlowing.vue'
import uploader from '~/views/transaction/uploader/uploader.vue'
//竞猜模块
import guessing from '~/views/guessing/guessing.vue'
//积分模块
import customer from '~/views/integral/customer.vue'
import level from '~/views/integral/level.vue'
import rules from '~/views/integral/rules.vue'
//运营模块
import rank from '~/views/operation/rank.vue'
import lottery from '~/views/operation/lottery.vue'
//广告
import advertising from '~/views/advertising/advertising.vue'
//商品
import attribute from '~/views/goods/attribute.vue'
import category from '~/views/goods/category.vue'

Vue.use(Router);

const router=new Router({
    mode:'hash',
    routes:[
        {
            path:'/',
            redirect:'login'
        },
        {
            name:'login',
            path:'/login',
            //component:require('../views/login/Login.vue') //使用require报错，具体原因暂未知
            component:Login
        },
        {
            name:'common',
            path:'/common',
            component:common,
            children:[
                {//主页
                    name:'home',
                    path:'/home',
                    component:home
                },
                /*客户模块*/
                {//会员
                    name:'member',
                    path:'/member',
                    component:member
                },
                {//会员实名认证审核
                    name:'certificate',
                    path:'/certificate',
                    component:certificate
                },
                {//企业管理
                    name:'company',
                    path:'/company',
                    component:company
                },
                /*交易模块*/
                {//行情发布
                    name:'newsIssue',
                    path:'/newsIssue',
                    component:newsIssue
                },
                {//供求管理
                    name:'supplyDemand',
                    path:'/supplyDemand',
                    component:supplyDemand
                },
                {//交收单管理
                    name:'settlement',
                    path:'/settlement',
                    component:settlement
                },
                {//交收单管理
                    name:'uploader',
                    path:'/uploader',
                    component:uploader
                },
                {//统计报表
                    name:'statistic',
                    path:'/statistic',
                    component:statistic
                },
                {//举报
                    name:'whistleBlowing',
                    path:'/whistleBlowing',
                    component:whistleBlowing
                },
                {//果圈动态
                    name:'moments',
                    path:'/moments',
                    component:moments
                },
                //竞猜
                {//竞猜列表
                    name:'guessing',
                    path:'/guessing',
                    component:guessing
                },
                //积分
                {//客户积分
                    name:'customerIntegral',
                    path:'/customerIntegral',
                    component:customer
                },
                {//等级设置
                    name:'level',
                    path:'/level',
                    component:level
                },
                {//积分规则
                    name:'rules',
                    path:'/rules',
                    component:rules
                },
                //运营
                {//排行榜
                    name:'rank',
                    path:'/rank',
                    component:rank
                },
                {//抽奖
                    name:'lottery',
                    path:'/lottery',
                    component:lottery
                },
                //广告
                {//抽奖
                    name:'advertising',
                    path:'/advertising',
                    component:advertising
                },
                //商品
                {//属性
                    name:'attribute',
                    path:'/attribute',
                    component: attribute
                },
                {//品类
                    name:'category',
                    path:'/category',
                    component:category
                },
            ]
        }


    ]
});
 export default router;