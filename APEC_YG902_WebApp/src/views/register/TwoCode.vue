<template>
  <div class="c-twocode">
    <my-header class="c-twocode-header">
       <h1 slot="pack" class="c-twocode-title">推荐有奖</h1>
    </my-header>
    <div class="c-twocode-content">
      <div class="c-twoContent" ref="twoWrapper">
        <img :src="src" class="c-code-png">
      </div>
    </div>
    <canvas id="canvas"></canvas>
  </div>
</template>
<style>
  @import "../../assets/css/Twocode.css";
</style>
<script>
  import header from "../../components/header/headerTwo.vue"
  import {Toast} from 'mint-ui'
  import wx from '../../components/wx.vue'
  import p from "../../components/post.vue"
  import QRCode from 'qrcode'

  export default{
      data(){
          return{
              width:"",
              height:"",
              src:null,
          }
      },
      mounted(){
          var that = this,
            el = that.$refs.twoWrapper,
            style = getComputedStyle(el, null);
            that.width = parseFloat(style.width);
            that.height = parseFloat(style.height);
      },
      methods:{
        getId(){
            var id = localStorage.useTId,
                 that = this;
            if(!id){
              var params = {
                api:"/_node_user/_info.apno"
              }
              p.post(params, function (item) {
                if (item.succeed) {
                  dt = JSON.parse(item.data);
                  id = dt.userId;
                  localStorage.useTId = id;
                }else{
                    Toast(data.errorMsg);
                }
              }, function (error) {
                Toast(error);
              })
            }

            return id;
        }
      },
      activated(){
          var that = this,
               id = that.getId();
          if(id){
            var canvas = document.getElementById('canvas');
            var url = location.origin + "/#/invite?id=" + id;
            console.log(url, 88888888);
//            var url = "https://yg.ap88.com/#/home";
            QRCode.toCanvas(canvas, url,{
              margin:1,
              width:that.width,
              height:that.height,
            } ,function (error) {
              if (error)
                console.error(error)
              else{
                convertCanvasToImage(canvas, that);
              }
              console.log('success!');
            })

            function convertCanvasToImage(canvas, that) {
//              var image = new Image();
              that.src = canvas.toDataURL("image/png");
//              return image;
            }
            wx.wx("邀请好友，注册有礼", url);
          }
      },
      components:{
        'my-header':header,
      }
  }
</script>
