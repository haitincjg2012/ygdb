<template>
  <div class="c-preview">
    <div class="preview--wrapper">
      <div class="preview--img_wrapper">
        <img :src="imgSrc" alt="背景图片" class="preview--img">
      </div>
      <div class="preview--info_wrapper">
        <div class="preview--user_wrapper">
          <!--<img src="./img/timg.jpg" alt="用户头像" class="preview&#45;&#45;user_img">-->
          <!--<div class="preview&#45;&#45;user_portrait" v-html="imgHtml">-->
          <div class="preview--user_portrait">
            <img  class="c-preview-headPortrait" ref="headPortraitWrapper">
          </div>
          <div class="preview--user_profile">
            <!--<div class="preview&#45;&#45;profile"><span class="preview&#45;&#45;user-genre">姓名：&nbsp;</span>{{name}}</div>-->
            <div class="preview--profile">姓名：&nbsp;{{name}}</div>
            <div class="preview--profile">身份：&nbsp;{{identity}}</div>
            <div class="preview--profile">电话：&nbsp;{{phone}}</div>
            <div class="preview--profile" style="width: 100%;">地址：&nbsp;{{address}}</div>
          </div>
        </div>
        <div class="preview--footer_wrapper">
          <!--<img src="./img/qr.jpg" alt="用户头像" class="preview&#45;&#45;footer_qrcode">-->
          <div class="footer_qrcode">
             <img src="../../assets/img/znw_game.png" class="c-znw-qrcode">
          </div>
          <div class="preview-footer_desc">
            <!--<div><span class="diamond"></span>扫码参与游戏<span class="diamond"></span></div>-->
            <div class="c-preview-game">
               <img src="../../assets/img/game.png" class="c-preview-game-img">
            </div>
            <div class="preview-desc_slogan">急卖货 急找货 果来乐 都能做</div>
          </div>
        </div>
      </div>
    </div>
    <!--<div @click="btn">生成图片</div>-->
    <!--<img id="composeImg">-->
  </div>
</template>
<style>
@import "../../assets/css/operate/compose.css";
</style>
<script>
  import html2canvas from 'html2canvas';

  export default{
      data(){
          return{
//              name:"李智贤",
//             identity:"代办",
//             phone:"18946732657",
            imgHtml:null,
            imgSrc: '',
          }
      },
      props:['headPortrait', 'name','identity','phone','address'],
      watch:{
        'headPortrait':function (newValue, oldValue) {
          var that = this;
          this.convertImgToBase64(newValue,function () {
            that.$nextTick(function () {
              html2canvas(document.querySelector('.preview--wrapper')).then(canvas => {
              var data = canvas.toDataURL('image/png');
              that.$emit("componse", data);
//                var image = document.getElementById("composeImg");
//                image.src = canvas.toDataURL('image/png');
              });
            });
          });
        },


//  convertImgToBase64('http://bit.ly/18g0VNp', function(base64Img){
//    // Base64DataURL
//  });
      },
      methods:{
        convertImgToBase64(url, callback, outputFormat){
          var canvas = document.createElement('CANVAS'),
            ctx = canvas.getContext('2d'),
            elImg = this.$refs.headPortraitWrapper;
           var  img = new Image;
          img.crossOrigin = 'Anonymous';
          img.onload = function(){
            canvas.width = img.width;
            canvas.height = img.height;
            console.log(elImg.width, elImg.width);
            ctx.drawImage(img,0,0,);
            var dataURL = canvas.toDataURL(outputFormat || 'image/png');
            callback.call(this, dataURL);
            elImg.src = dataURL;
            canvas = null;
          };
          img.src = url;

        },
        btn(){
            var that = this;

        },
        getBgImg(){
        	let num = Math.floor(Math.random()*5)+1;
        	this.imgSrc = '../../static/img/o_canvas_'+ num + '.png';
        }
      },
      activated(){
      	this.getBgImg();
          var url = "https://ygdb-pro.oss-cn-hangzhou.aliyuncs.com/8835e38d8adb092e764a84a5ca50b5e8?x-oss-process=style/_head";

      }
  }
</script>
