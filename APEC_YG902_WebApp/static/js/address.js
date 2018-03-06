/**
 * Created by Administrator on 2018/1/6.
 *
 *
 *
 */

var utiles = {
  xml:function () {
    var xmlhttp;
    if(window.XMLHttpRequest){
      xmlhttp = new XMLHttpRequest();
    }else if(window.ActiveXObject){
      xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }else{
      return false;
    }

    return xmlhttp;
  },
  get:function (url,callback,style) {
    var xmlObj = utiles.xml();
    if(xmlObj){
      xmlObj.open("GET", url);
      xmlObj.onreadystatechange = function (){
        if(xmlObj.readyState == "4"){
          var jobj = xmlObj.responseText;
          if(style == "json"){
            jobj = JSON.parse(xmlObj.responseText);
          }
          callback(jobj);
        }
      }

      xmlObj.send(null);
    }else{
      alert("请您升级浏览器");
    }
  },
  post:function (url) {
   //有时间补充
  }
}

//数据的处理
function znw(data) {
  window.tree = data;
}
//省

utiles.get("./static/json/pcas-code1.json", znw, "json");



