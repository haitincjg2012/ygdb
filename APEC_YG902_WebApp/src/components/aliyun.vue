<script>
  //by zy gsy yd
  //中农网
  import API from '../api/api'
  import DEL from './del.vue'
  import configFile from "../assets/data/AWconfig.json"

  const api = new API();
  var AccessKeyId = "",
    AccessKeySecret = "",
    SecurityToken = "",
    url = "";
  //post
  function post(params, fn){
    try {
      api.post(params).then((res) => {
        var data = res.data;
        fn(data);
      }).catch((error) => {
        console.log(error)
      })
    } catch (error) {
      console.log(error)
    }
  }
  function aliyun(){
    var cookieString = document.cookie;
    var arr = cookieString.split(";");
    var flag = true;

    if(cookieString.indexOf("AccessKeyId") > -1){
      flag = false;
    }

    if(flag){
      checkcookie();//异步执行
    }else{
      for(var key in arr){
      	console.log(arr[key])
        if(arr[key].indexOf("AccessKeyId") > -1){
          AccessKeyId = arr[key].split("AccessKeyId=")[1];
        }
        if(arr[key].indexOf("AccessKeySecret") > -1){
          AccessKeySecret = arr[key].split("AccessKeySecret=")[1];
        }
        if(arr[key].indexOf("SecurityToken") > -1){
          SecurityToken = arr[key].split("SecurityToken=")[1];//截取SecurityToken= 直接截取=token自带的==会被截取
        }
      }
    }

    /**
     * OSS Function
     */
    return function (userId,fn, file) {
      var cookieString = document.cookie;
      if(cookieString.indexOf("AccessKeyId") < 0){
        checkcookie();//异步执行
      }
      var strFormat = userId +""+ new Date().getTime();
      var storeAs = window.md5(strFormat);
      var aliyunConfig = configFile.aliyunConfig;
      var client = new OSS.Wrapper({
        accessKeyId: AccessKeyId,
        accessKeySecret: AccessKeySecret,
        stsToken: SecurityToken,
//        endpoint: 'https://oss-cn-qingdao.aliyuncs.com/',
        endpoint:aliyunConfig.endpoint,
        bucket: aliyunConfig.bucket
//        bucket: 'yg-pro'
      });

      client.multipartUpload(storeAs, file).then(function (result) {
//        url = "https://yg-pro.oss-cn-qingdao.aliyuncs.com/"+result.name;
        url = aliyunConfig.url+result.name;
        fn(url);
      }).catch(function (err) {
        alert("错误："+err);
      });
    }
  }

//  函数写法的样例 by yd
//  var doOssUpload = function() {
//
//  };

//  检测cookie的有效期
  function checkcookie() {
    let params = {
      api: "/_node_image/_oauth.apno",
      data: {}
    }

    post(params,function (response) {
      try {
        var  result = response.data;
      } catch (e) {
        return alert('parse sts response info error123: ' + e.message);
      }
      AccessKeyId = result.AccessKeyId;
      AccessKeySecret = result.AccessKeySecret;
      SecurityToken = result.SecurityToken;
      setCookie("AccessKeyId",AccessKeyId, 50);
      setCookie("AccessKeySecret",AccessKeySecret, 50);
      setCookie("SecurityToken",SecurityToken, 50);
    });
  }
//设置cookie的有效期
  function setCookie(name,value,second) {
    var d = new Date();
    d.setTime(d.getTime() + (second * 60*1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = name + "=" + value + "; " + expires;

  }
  export default{
    aliyun
  }

</script>
