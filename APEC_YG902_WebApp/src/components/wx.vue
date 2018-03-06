<script>
  import API from "../api/api"
  const api = new API();
  let dataDefault = {
    title:"",
    link:"",
    desc:"快乐每一天",
    imgUrl:'http://photo.ap88.com/20171011183239.png'
  };
  function wx(title,urlT, callback){
    var self = this;
    var url = window.location.href;
    checkWxToken();//判断缓存是否存在
//    var url = window.location.href +"&WX = yg",
    var url =urlT || window.location.href,
        urlArr = url.split("#"),
        domain = urlArr[0],
        paths = urlArr[1];


    var path1 = paths.split("?")[0].substring(1) || "";
    var path2 = paths.split("?")[1] || "";

    var pathT = (path1?"?FakePath=" + path1 +(path2?"?"+path2:""):"");//?和&的区别

    var link = domain + pathT ;

//    var t = decodeURIComponent(path);
//    var pos = t.indexOf("WX");
//    console.log(t, t.length,t.slice(0, pos -1), 2222);

   
    dataDefault.link = link;
    dataDefault.title = title;
    window.share(dataDefault, callback);//朋友、朋友圈、qq

  };

  function wxline(title){
      //分享给朋友
    var self = this;
    var url =window.location.href;

    var flag = url.indexOf("newsDetail");
    let params = {
      api:"/yg-user-service/wxapi/getSignInfo.apec",
      data:{
        url:window.location.href
      }
    }
    post(params, function (data) {
      window.wxinit(data.data);
      if(flag > -1){
        dataDefault.link = window.location.href;
        dataDefault.title = title;
        // console.log(dataDefault);
        window.MenuShareTimeline(dataDefault);//朋友
      }else{
        window.MenuShareTimeline();//朋友
      }

    });
  };

  function wxMessage(title){
      //分享到朋友圈
    var self = this;
    var url =window.location.href;
    let params = {
      api:"/yg-user-service/wxapi/getSignInfo.apec",
      data:{
        url:window.location.href
      }
    }
    post(params, function (data) {
      window.wxinit(data.data);
      if(flag > -1){
        dataDefault.link = window.location.href;
        dataDefault.title = title;
        window.shareAppMessage(dataDefault);//朋友圈
      }else{
        window.shareAppMessage();//朋友圈
      }

    });
  };

  function checkWxToken(){
    //检查微信的token是否有效
    var tokenString = document.cookie,Token;

    if(tokenString.indexOf("wxappid") > -1){
      var data = {
        data:{}
      };

      var arr = tokenString.split(";");
      for(var key in arr){
        if(arr[key].indexOf("wxappid") > - 1){
          data.data.appid = arr[key].split("=")[1];
        }else if(arr[key].indexOf("wxtimestamp") > - 1){
          data.data.timestamp = arr[key].split("=")[1];
        }else if(arr[key].indexOf("wxnonceStr") > - 1){
          data.data.nonceStr = arr[key].split("=")[1];
        }else if(arr[key].indexOf("wxsignature") > - 1){
          data.data.signature = arr[key].split("=")[1];
        }
      }
      window.wxinit(data.data);
    } else{
        //获取token
      let params = {
        api: "/yg-user-service/wxapi/getSignInfo.apec",
        data: {
//            url:"http://yg.ap88.com"
          url: window.location.href
        }
      }
      post(params, function (data) {
        window.wxinit(data.data);
      });
    }

  }

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
   };
  export default{
    wx,wxline,wxMessage
   }
</script>
