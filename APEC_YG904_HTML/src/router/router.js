/**
 * Created by gsy on 2017/8/18.
 * 路由
 */
import Vue from 'vue'
import Router from 'vue-router'

import Login from '~/views/login/Login.vue'
import common from '~/views/common/common.vue'
import home from '~/views/home/home.vue'
import member from '~/views/member/member.vue'
import certificate from '~/views/member/certificate.vue'
import newsIssue from '~/views/transaction/newsIssue.vue'

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
                /*交易模块*/
                {//行情发布
                    name:'newsIssue',
                    path:'/newsIssue',
                    component:newsIssue
                }
            ]
        }


    ]
});
 export default router;