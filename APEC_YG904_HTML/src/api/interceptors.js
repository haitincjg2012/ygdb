/**
 * Created by gsy on 2017/8/18.
 * 拦截器(暂不用)
 */
import axios from 'axios'
import config from './config'
import router from '~/router/router'
import { Message} from 'element-ui'
import commonjs from '~/assets/js/common'

class interceptor{
    //请求拦截器
axios.interceptors.request.use(function (config) {
    //在发送请求之前添加token头部
    var token = store.state.authToken || commonjs.getValue("authToken");
    if (token) {
        config.headers['token'] = token;
    }
    return config;
}, function (error) {
    //请求错误时
    return Promise.reject(error);
});

    //响应拦截器
 axios.interceptors.response.use(function(response){
    //特定错误码统一处理
    if (typeof response.data == 'string') {
        var res = JSON.parse(response.data)
    } else if (typeof response.data == 'object') {
        var res = response.data
    }
    if(res.errorCode=='600001'){//登录超时且不是登陆页就跳转到登陆页面
        var reg=new RegExp(/login/);
        var a=reg.exec(router.currentRoute.fullPath);
        if(a==null){
            router.replace({
                path:'/login',
                query:{redirect:router.currentRoute.fullPath}
            })
        }
    }
    return response;
},function(error){
    //请求错误时做的事
    return Promise.reject(error);
});

}
export default interceptor;
