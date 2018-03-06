<template>
  <div class="c-revise">
      <h1 class="c-revise-title">请上传您的照片</h1>
      <div class="c-revise-canvas" ref="frameWrapper">
         <div class="photo-clip-view" ref="photoWrapper">
            <div class="photo-clip-moveLayer" ref="frameWrapperF">
                 <div class="photo-clip-rotateLayer" ref="frameWrapperS">
                      <img :src="src" ref="imgWrapper"/>
                      <!--<img :src="../../assets/img/o_home_f_bg.png"/>-->
                 </div>
            </div>
         </div>
         <div class="c-revise-canvas-mask">
             <div class="photo-clip-mask-left"></div>
             <div class="photo-clip-mask-bottom"></div>
             <div class="photo-clip-mask-right"></div>
             <div class="photo-clip-mask-top"></div>
             <div class="photo-clip-mask-area"></div>
         </div>
      </div>
      <div class="c-revise-btn">
           <div class="c-revise-btn-l">
             <label class="c-revise-btn-l-bg" for="upId"></label>
             <input type="file" id="upId" class="c-revise-btn-file" @change="handlePicture">
           </div>
           <div class="c-revise-btn-r">
             <div class="c-revise-btn-r-bg" @click="compose"></div>
           </div>
      </div>
     <canvas id="myCanvas"></canvas>
     <div class="c-revise-compose">
         <compose></compose>
     </div>
  </div>
</template>
<style>
  @import "../../assets/css/operate/revise.css";
</style>
<script>
  import ALIYUN from "../../components/aliyun.vue" //使用阿里云上传图片
  import compose from "./compose.vue"
  import { Toast,Indicator} from 'mint-ui';
  export default{
      data(){
          return{
            AccessKeyId:null,
            AccessKeySecret:null,
            SecurityToken:null,

              src:null,
              offsetTop:null,//距离的高度和宽度
              offsetLeft:null,
              offsetWidth:null,//框的高度和宽度
              offsetHeight:null,
              frameWidth:null,
              frameHeight:null,
              x:0,
              y:0,
              fingurX:0,
              fingurY:0,
              nextFingurX:0,
              nextFingurY:0,

              leftLimitX:0,
              TopLimitY:0,

            tSrc:null,
            aliyunO:null,//阿里云
          }
      },
      methods:{
        handlePicture(evt){
          var that = this;
          var files = evt.target.files || evt.dataTransfer.files;
          var file = files[0];
          if(!file){
              return;
          }
          var type = file.type;
          if(!type){
            Toast({
              message:"对不起，您上传的图片格式不对，请您重新上传图片",
              duration:1000
            })
            return;
          }
          //读取图片
          var reader = new FileReader();
          reader.readAsDataURL(file);
          reader.onload = function (e) {
              that.src = this.result;
              that.setStyle();
          };

        },
        bs:function(property){
          var _elementstyle = document.createElement("div").style;
          var vendors = ["","webkit","ms","O","Moz"],
            transform,
            i = 0,
            l = vendors.length;

          for(; i < l; i ++){
            transform = vendors[i] + property;
            if(transform in _elementstyle){
              return vendors[i];
            }
          }

          return false;
        },
        translate:function (el, x,y) {
          var that = this;
          var property = that.bs("Transform");
          if(property == false){
            alert("浏览器不存在此属性");
          }else{
            var adaptation = property+"Transform";
            el.style[adaptation]='translate('+x +'px,'+ y +'px)';
          }
        },
          offset(){
            var that = this;
           if(!that.offsetHeight){
             var el = this.$refs.photoWrapper;
             var frameEl = this.$refs.frameWrapper;
                  that.offsetTop = el.offsetTop;
                  that.offsetLeft = el.offsetLeft;
                  that.offsetWidth = el.clientWidth;
                  that.offsetHeight = el.clientHeight;
                  that.frameHeight = frameEl.clientHeight;
                  that.frameWidth = frameEl.clientWidth;
           }
        },
          setStyle(){
            var that = this;
            this.$nextTick(function (e) {
               var el = that.$refs.imgWrapper;
               var fEl = that.$refs.frameWrapperF;
               var style = getComputedStyle(el, null),
                    width = parseFloat(style.width),
                    height = parseFloat(style.height);



               var elF = that.$refs.frameWrapperF;
                   elF.style.width = width + "px";
                   elF.style.height = height + "px";

              var elS = that.$refs.frameWrapperS;
                   elS.style.width = width + "px";
                   elS.style.height = height + "px";
                   if(that.frameWidth < width){
                       var x =  - that.offsetLeft - (width - that.frameWidth)/2;
                       that.x = x;
                       that.leftLimitX = width - that.offsetWidth;//右边界条件
                       that.TopLimitY = height - that.offsetWidth;//上边界条件
                       var y = 0;
                       if(that.frameHeight< height ){
                         y = - that.offsetTop;
                       }else if(that.offsetHeight < height && that.frameHeight> height){
                         y = - (height - that.offsetHeight)/2;
                       }
                       that.y = y;
                       that.translate(fEl,x ,y)
                   }else if(that.frameWidth > width && width > that.offsetWidth){
                       var x =  - (width - that.offsetWidth)/2;
                       that.x = x;
                       that.leftLimitX = width - that.offsetWidth;//右边界条件
                       that.TopLimitY = height - that.offsetHeight;//上边界条件
                       var y = 0;
                       if(that.frameHeight< height ){
                         y = - that.offsetTop;
                       }else if(that.offsetHeight < height && that.frameHeight> height){
                         y = - (height - that.offsetHeight)/2;
                       }
                     that.y = y;
                     that.translate(fEl,x ,y)
                   }
            });
          },
          start(e){
            var touch = e.targetTouches[0];
            this.fingurX = touch.pageX;
            this.fingurY = touch.pageY;

          },
          move(e){
              var that = this;
            var touch = e.targetTouches[0];
            var fEl = this.$refs.frameWrapperF;

            var posX = touch.pageX;
            var posY = touch.pageY;
            var delayX = posX - this.fingurX;
            var delayY = posY - this.fingurY;
            this.fingurX = posX;
            this.fingurY = posY;
            var x = this.x;
            var y = this.y;

            x = this.x + delayX;
            y = this.y + delayY;

            //一种情况：右移动，不向上、下移动
            //一种情况：左移动，不向上、下移动
            //一种情况：右移动,向下
            //一种情况：右移动,向上
            //一种情况：左移动,向上
            //一种情况：左移动,向上
            if(delayX >= 0){
                //右移动,向下，向上
              if(x > 0){
                  //右移动的x的边界
                this.x = 0;
                moveY(delayY, that.x, y, fEl);
              }else{
                this.x = x;
                moveY(delayY, that.x, y, fEl);
              }
            }else{
              //左移动,向下，向上
               if(Math.abs(x) >= Math.abs(that.leftLimitX)){
                this.x = - that.leftLimitX;
                 moveY(delayY, that.x, y, fEl);
              }else{
                this.x = x;
                 moveY(delayY, that.x, y, fEl);
//                this.translate(fEl,x ,y)
              }
            }
              function moveY(delayY, x, y, el) {
                if(delayY > 0){
                  //向下
                  if(y >= 0){
                    that.y = 0;
                    that.translate(el,x ,0)
                  }else{
                    that.y = y;
                    that.translate(el,x ,y)
                  }
                }else{
//                    向上
                  if(Math.abs(y) >= Math.abs(that.TopLimitY)){
                    that.y = - that.TopLimitY;
                    that.translate(fEl,x ,- that.TopLimitY)
                  }else{
                    that.y = y;
                    that.translate(fEl,x ,y)
                  }
                }
              }
          },
          end(e){
            var touch = e.targetTouches[0];
          },
          b64toBlob(b64Data, contentType='', sliceSize=512) {
            const byteCharacters = atob(b64Data);
            const byteArrays = [];

            for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
              const slice = byteCharacters.slice(offset, offset + sliceSize);

              const byteNumbers = new Array(slice.length);
              for (let i = 0; i < slice.length; i++) {
                byteNumbers[i] = slice.charCodeAt(i);
              }

              const byteArray = new Uint8Array(byteNumbers);

              byteArrays.push(byteArray);
            }

            const blob = new Blob(byteArrays, {type: contentType});
            return blob;
          },
        compose(){
          var c=document.getElementById("myCanvas");
          var that = this;

          c.width = that.offsetWidth;
          c.height = that.offsetHeight;
          var sx = Math.abs(that.x), sy =  Math.abs(that.y);
          var ctx=c.getContext("2d");
          var img=this.$refs.imgWrapper;
          if(img.complete){
            ctx.drawImage(img,sx,sy,that.offsetWidth,that.offsetHeight, 0, 0,that.offsetWidth,that.offsetHeight);
          }else{
            img.onload = function()
            {
              ctx.drawImage(img,sx,sy,that.offsetWidth,that.offsetHeight, 0, 0,that.offsetWidth,that.offsetHeight);
            }

          }

          var ImageURL = c.toDataURL();
          const block = ImageURL.split(";");
          const contentType = block[0].split(":")[1];// In this case "image/jpeg"
          const realData = block[1].split(",")[1];// In this case "R0lGODlhPQBEAPeoAJosM...."

          var Blob = that.b64toBlob(realData);

          const formData = new FormData();
          formData.append('blob', Blob);
          that.ajax(formData);

//          this.aliyunO(userId,function (data) {
//            console.log(data, 8888888);
//          }, data);

//          that.tSrc = data;
        },
        ali(){
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
          });
        },
        ajax(data,url){
          var xmlhttp=null,that = this;
            if (window.XMLHttpRequest)
            {// code for all new browsers
              xmlhttp=new XMLHttpRequest();
            }
            else if (window.ActiveXObject)
            {// code for IE5 and IE6
              xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
            }
            if (xmlhttp!=null)
            {
              xmlhttp.onreadystatechange= state_Change;
              xmlhttp.open("post","https://ygdb-pro.oss-cn-hangzhou.aliyuncs.com/",true);
              xmlhttp.send(data);
            }
            else
            {
              alert("Your browser does not support XMLHTTP.");
            }

            function state_Change(e)
            {
              if (xmlhttp.readyState==4)
              {// 4 = "loaded"
                if (xmlhttp.status==200)
                {
                  console.log(e, 99999999999999999999);
                  // ...our code here...
                }
                else
                {
                  alert("Problem retrieving XML data");
                }
              }
            }
        },
      },
      activated(){
          this.offset();
          this.aliyunO = ALIYUN.aliyun();
      },
      mounted(){
          var that = this,
              el = this.$refs.photoWrapper;
          el.addEventListener("touchstart",function (e) {
              that.start(e);
              e.preventDefault();
          }, false);
          el.addEventListener("touchmove",function (e) {
            that.move(e);
            e.preventDefault();
          }, false);
          el.addEventListener("touchend",function (e) {
            that.end(e);
            e.preventDefault();
          }, false);
      },
    components:{
      compose
    }
  }
</script>
