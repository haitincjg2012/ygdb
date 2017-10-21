/**
 * Created by gsy on 2017/8/18.
 * 全局信息
 */
import Vue from 'vue'
import Vuex from 'vuex'
import commonjs from '~/assets/js/common'

Vue.use(Vuex);

const store=new Vuex.Store({
    state:{
        staticFlag:false, //true加载静态数据，false请求后台接口
        authToken:commonjs.getValue('authToken') || '',//登录token
        userName:commonjs.getValue('userName') || '', //用户名
        userNo:commonjs.getValue('userNo') || '' //用户信息
    },
    mutations:{
        changeUserName(state,UT){
            state.userName=UT.name
        },
        changeAuthToken(state,UT){
            state.authToken=UT.token
        },
        changeUserInfo(state,UT){
            state.userNo=UT.userNo
        }
    }
})
//输出store
export default store;
