import axios from 'axios'
import config from './config'
import router from '~/router/router'
import store from '~/store/store'
import { Message} from 'element-ui'
import commonjs from '~/assets/js/common'

const ax={
  //get请求
  get:function(params,sucCb){
    //添加token头部
    var token = store.state.authToken || commonjs.getValue("authToken");
    if (token) {
      config.headers['token'] = token;
    }
    //设置请求参数
    config.data=params.data;
    var url=params.url;
    //返回请求数据
    axios.get(url,{params:config.data},config).then((res)=>{
    //var data=res.data?JSON.parse(res.data):[];
    var data=typeof(res.data)!="object"?JSON.parse(res.data):res.data;
    this.successCb(data,sucCb);
  }).catch((err)=>{
      //请求失败的回调函数
      this.failCb(err);
  });
  },

  all:function(param) {//异步多线程请求
    return axios.all(param)
  },
  //post请求-demo
/*  getPlaceList(){
    var vm = this;
    let params = {
      url: cityUrl,
      data: {}
    }
    api.post(params,vm.sucCb);
  },
 sucCb(data){
   this.provinceData = data.data;
   },
 */
  post:function(params,sucCb,vm){
    //添加token头部
    var token = store.state.authToken || commonjs.getValue("authToken");
    if (token) {
      config.headers['token'] = token;
    }
    //设置请求参数
    config.data=params.data;
    var url=params.url;
    //返回请求数据
    axios.post(url,{},config).then((res)=>{
      if(res.data){
        var data=typeof(res.data)!="object"?JSON.parse(res.data):res.data;
      }
      else{
        var data=[];
      }
      //var data=res.data?JSON.parse(res.data):[];
      this.successCb(data,sucCb,vm);

  }).catch((err)=>{
      //请求失败的回调函数
      this.failCb(err,vm);
    });
  },

//请求成功的回调函数
  successCb:function(data,sucCb,vm){
    //将列表loading字段“loadCircl”置为false
    var reg=/^loadCir[a-zA-Z0-9]+/;
      if (vm) {
        for(var i in vm.$data){
          if(reg.test(i))
            vm.$data[i]=false;
        }
      }
    if(data.succeed){
      sucCb(data);
    }
    else{
      if(data.errorCode=='600001'){//登录超时且不是登陆页就跳转到登陆页面
        Message.error("您的登录信息已超时,请重新登录！");
        var reg=new RegExp(/login/);
        var a=reg.exec(router.currentRoute.fullPath);
        if(a==null){
          router.replace({
            path:'/login',
            query:{redirect:router.currentRoute.fullPath}
          })
        }

      }
      else{
        console.log("错误信息："+data.errorMsg);
        Message.error("错误信息："+data.errorMsg);
      }

    }
  },

  //请求失败的回调函数
  failCb:function(err,vm){
  //将列表loading字段“loadCircl”置为false
    var reg=/^loadCir[a-zA-Z0-9]+/;
      if (vm) {
        for(var i in vm.$data){
          if(reg.test(i))
            vm.$data[i]=false;
        }
      }
    console.log("请求失败："+err);
    Message.error("请求失败："+err);

  },
  //从一个字符串中解析出json对象
  parse:function(param) {
    return JSON.parse(param);
  },
  //用于从一个对象解析出字符串
  stringify:function(param) {
    return JSON.stringify(param);
  },
  
  //发送图片
  postImg: function(param) {
    let config = {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    };
    return axios.post(param.url, param.data, config);

  }
}

export default ax;
