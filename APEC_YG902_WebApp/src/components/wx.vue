<script>
  import API from "../api/api"
  const api = new API();
  let dataDefault = {
    title:"",
    link:"",
    desc:"快乐每一天",
    imgUrl:'http://photo.ap88.com/20171011183239.png'
  };
  function wx(title){
    var self = this;
    var url =window.location.href;
    var flag = url.indexOf("newsDetail");
    let params = {
      api:"/yg-user-service/wxapi/getSignInfo.apec",
      data:{
//            url:"http://yg.ap88.com"
        url:window.location.href
      }
    }
    post(params, function (data) {
      window.wxinit(data.data);
      if(flag > -1){
        dataDefault.link = window.location.href;
        dataDefault.title = title;
        console.log(dataDefault);
        window.share(dataDefault);//朋友、朋友圈、qq
      }else{
        window.share();//朋友、朋友圈、qq
      }

    });
  };
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
    wx
   }
</script>
