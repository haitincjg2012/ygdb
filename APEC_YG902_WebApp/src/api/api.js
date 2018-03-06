import axios from 'axios'
import config from './config'
import store from '../store/store'
import router from '../router/router'
import c_js from '../assets/js/common'
import { Toast,MessageBox } from 'mint-ui';

var t;


class API {
  get(param) {
    //param.api = 'http://shoptest.ap-ec.cn/'+param.api;  //放到测试环境  get方法所有URL没有前缀，在此做的临时方案
    //param.api = 'https://shop.ap-ec.cn/'+param.api;
    return axios.get(param.api, {}, config);
  }

  all(param) {//异步多线程请求
    return axios.all(param)
  }

  //使用post发送请求，已经在axios拦截器里把accessToken和authToken写进header里面
  post(param) {
    config.data = param.data;
    return axios.post(param.api, {}, config);
  }

  //使用post发送img请求，已经在axios拦截器里把accessToken和authToken写进header里面
  postImg(param) {
    let config = {
      headers: {
        'Content-Type': 'multipart/form-data',
        'cc': 'WEIXIN'
      }
    };
    return axios.post(param.api, param.data, config);

  }

  parse(param) {//从一个字符串中解析出json对象
    return JSON.parse(param);
  }

  stringify(param) {
    return JSON.stringify(param);
  }//用于从一个对象解析出字符串
}


// http request 拦截器
axios.interceptors.request.use(
  config => {

    //添加authToken
    var token_Store = store.state.token;
     config.headers['token'] = token_Store || c_js.getValue("token");
    // config.headers['token']='3e66d3b1e7fe5a11d4811f584b21f2ec';
    //config.headers['token'] = '43b514522dc71661066a57c53a60732269';
    return config;
  },
  err => {
    return Promise.reject(err);
  });

// http response 拦截器

axios.interceptors.response.use(
  response => {
    //特定错误码统一处理
    try {
      const status = response.data.succeed;
      const errorCode = response.data.errorCode;
      if (!status && errorCode === '600001' && router.currentRoute.name!=='home'&& router.currentRoute.name!=='search'&& router.currentRoute.name!=='buy') {
        router.replace({
          path: '/login',
          // query: {redirect: router.currentRoute.fullPath}
        });
      }
      // else if(!status && errorCode !== '600001')
      // {
      //   clearTimeout(t);
      //   t = setTimeout(()=>{
      //     MessageBox({
      //       title: '提示',
      //       message: "服务器出错了哦，请点击下面按钮重新加载应用",
      //       confirmButtonText:'重新加载'
      //     }).then(action =>{
      //       window.location.reload();
      //     });
      //   },500);
      // }
      else{

      }
    } catch (e) {

    }
    return response;
  },
  error => {
    if (error.response) {
      //console.log(error.response)
    } else {
      //console.log('false')
    }
    return Promise.reject(error);   // 返回接口返回的错误信息
  });
export default API;
