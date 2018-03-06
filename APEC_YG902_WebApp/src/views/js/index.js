/**
 * Created by Administrator on 2017/12/29.
 */
import API from "../../api/api"
const api = new API();

function sendWx(id) {
  if(!window.postId){
    return;
  }
  var params = {
    api:"yg-society-service/societyPost/addSocietyPostTransCount.apec",
    data:{
      id:id
    }
  }
  post(params, function () {
    console.log(111);
  });

  window.postId = "";
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
  install(Vue){

    (function (w) {
      w.data = {
        title:"果来乐",
        link:"http://yg.ap88.com",
        desc:"快乐每一天",
        // imgUrl:'http://photo.ap88.com/20171011183239.png'
        imgUrl:"https://ygdb-pro.oss-cn-hangzhou.aliyuncs.com/20171011183239.png"
      };

      function wxinit(data) {
        w.wx.config({
          debug:false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
          appId: data.appid, // 必填，公众号的唯一标识
          timestamp:data.timestamp, // 必填，生成签名的时间戳
          nonceStr: data.nonceStr, // 必填，生成签名的随机串
          signature: data.signature,// 必填，签名，见附录1
          jsApiList: ['onMenuShareTimeline','onMenuShareAppMessage', 'onMenuShareQQ'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        });

        w.wx.error(function(res){
           console.log(res);
          //alert(res);
          // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
        });
      }


      //分享到朋友、朋友圈、qq
      function share(dt, callback) {
        var postId = window.postId;
        w.wx.ready(function(){
          // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。;
          // wx.checkJsApi({
          //   jsApiList: ['onMenuShareTimeline'], // 分享到朋友圈
          //   success: function(res) {
          //     // 以键值对的形式返回，可用的api值true，不可用为false
          //     // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
          dt = dt || w.data;

          w.wx.onMenuShareTimeline({
            title:dt.title, // 分享标题
            link: dt.link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: w.data.imgUrl, // 分享图标
            success: function () {
              alert("分享成功1");
              sendWx(postId);
              if(callback){
                callback();
              }
              // 用户确认分享后执行的回调函数
            },
            cancel: function () {
              alert("分享失败");
              // 用户取消分享后执行的回调函数
            }
          });
          w.wx.onMenuShareAppMessage({
            title:dt.title, // 分享标题
            desc: dt.desc, // 分享描述
            link:dt.link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: w.data.imgUrl, // 分享图标
            type: '', // 分享类型,music、video或link，不填默认为link
            dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
            success: function () {
              // window.location.href = "http://yg.ap88.com";
              if(callback){
                callback()
              }
              sendWx(postId);
              // 用户确认分享后执行的回调函数
            },
            cancel: function () {
              alert("分享失败");
              // 用户取消分享后执行的回调函数
            }
          });
          w.wx.onMenuShareQQ({
            title: dt.title, // 分享标题
            desc: dt.desc, // 分享描述
            link: dt.link, // 分享链接
            imgUrl: w.data.imgUrl, // 分享图标
            success: function () {
              alert("分享成功3");
              sendWx(postId);
              if(callback){
                callback()
              }
              // 用户确认分享后执行的回调函数
            },
            cancel: function () {
              alert("分享失败");
              // 用户取消分享后执行的回调函数
            }
          });

          wx.error(function(res){
            console.log(res);
            // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
          });
        });

      }
      //分享到朋友
      function MenuShareTimeline(data) {

        w.wx.ready(function(){
          // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
          // wx.checkJsApi({
          //   jsApiList: ['onMenuShareAppMessage',], // 分享到朋友圈，分享给朋友
          //   success: function(res) {

          // 以键值对的形式返回，可用的api值true，不可用为false
          // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
//分享到朋友
          var data = data || w.data;
          wx.onMenuShareTimeline({
            title:data.title, // 分享标题
            link:data.link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: data.imgUrl, // 分享图标
            success: function () {
              alert("分享成功2");
              // 用户确认分享后执行的回调函数
            },
            cancel: function () {
              alert("分享失败2");
              // 用户取消分享后执行的回调函数
            }
          });
        });
        w.wx.error(function(res){
          // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
        });
      }
      //分享到朋友圈
      function shareAppMessage(data) {

        w.wx.ready(function(){
          // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
          // wx.checkJsApi({
          //   jsApiList: ['onMenuShareAppMessage',], // 分享到朋友圈，分享给朋友
          //   success: function(res) {

          // 以键值对的形式返回，可用的api值true，不可用为false
          // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
//分享到朋友
          var data = data || w.data;
          // if(res.checkResult.onMenuShareAppMessage){
          w.wx.onMenuShareAppMessage({
            title:data.title, // 分享标题
            desc: data.desc, // 分享描述
            link:data.link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: data.imgUrl, // 分享图标
            type: '', // 分享类型,music、video或link，不填默认为link
            dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
            success: function () {
              // window.location.href = "http://yg.ap88.com";
              alert("分享成功2");
              // 用户确认分享后执行的回调函数
            },
            cancel: function () {
              alert("分享失败2");
              // 用户取消分享后执行的回调函数
            }
          });
          // }
          //   }
          // });


        });
        w.wx.error(function(res){
          // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
        });
      }


      w.share = share;//分享到所有
      w.MenuShareTimeline = MenuShareTimeline;//分享朋友
      w.shareAppMessage = shareAppMessage;//分享朋友圈
      // w.menuShareQQ = menuShareQQ;
      w.wxinit = wxinit;
    })(window);
  }
}

