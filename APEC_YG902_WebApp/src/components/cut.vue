<template>
   <div class="main-cut">
        <div class="cropper-canvas">
            <img :src="src"  class="imgT">
        </div>
        <div class="cropper-drag-box cropper-modal">
        </div>
        <div class="cropper-crop-box">
            <div class="cropper-view-box">
                 <img :src="src" class="imgT tt">
            </div>
          <span class="cropper-dashed dashed-h" v-if="H"></span>
          <span class="cropper-dashed dashed-v" v-if="V"></span>
          <div class="cropper-face" data-drag="all"></div>
          <div class="cropper-line line-e" v-if="lineE"></div>
          <div class="cropper-line line-n" v-if="lineN"></div>
          <div class="cropper-line line-w" v-if="lineW"></div>
          <div class="cropper-line line-s" v-if="lineS"></div>
          <div class="cropper-point point-e" data-drag="e" v-if="pointE"></div>
          <div class="cropper-point point-n" data-drag="n" v-if="pointN"></div>
          <div class="cropper-point point-w" data-drag="w" v-if="pointW"></div>
          <div class="cropper-point point-s" data-drag="s" v-if="pointS"></div>
          <div class="cropper-point point-ne" data-drag="ne" v-if="pointNE"></div>
          <div class="cropper-point point-nw" data-drag="nw" v-if="pointNW"></div>
          <div class="cropper-point point-sw" data-drag="sw" v-if="pointSW"></div>
          <div class="cropper-point point-se" data-drag="se" v-if="pointSE"></div>
        </div>
        <div @click="submit" class="z-cut-ok">
             确定
        </div>
       <img :src="mmmm" class="mimg">
   </div>
</template>
<style>
  @import "../assets/css/cut.css";
</style>
<script>
  import API from '../api/api'
  const api = new API();
  export default{
      data(){
          return {
              H:false,
              V:false,
            lineE:true,
            lineN:true,
            lineW:true,
            lineS:true,
            pointE:false,
            pointN:false,
            pointW:false,
            pointS:false,
            pointNE:true,
            pointNW:true,
            pointSW:true,
            pointSE:true,
            pageY:0,
            offsetHeight:0,
            el:null,
            upperlimit:0,
            lowerlimit:0,
            boxHeight:0,
            boxWidth:0,
            imgHeight:0,
            src:'',
            mmmm:''
          }
      },
    mounted(){
      const self = this;
      var el = document.querySelector(".cropper-crop-box");
      self.boxHeight = el.offsetHeight;
      self.boxWidth = el.offsetWidth;
      el.addEventListener("touchstart",self.down,false);
      el.addEventListener("touchmove", self.move,false)
      el.addEventListener("touchend", self.up,false)


    },
    methods:{
        down(e){
          var el = document.querySelector(".cropper-canvas");
          var rect = el.getBoundingClientRect();
          this.lowerlimit =rect.top;
          this.upperlimit = rect.bottom;
          var elImg = document.querySelector(".tt");
          this.imgHeight = elImg.offsetHeight;
            if(this.imgHeight < this.boxHeight){
                return;
            }
            if(!this.el){
              this.el = document.querySelector(".cropper-crop-box");
            }
            this.offsetHeight = this.el.offsetHeight;
            var e = window.event || e;
            this.pageY = e.changedTouches ? e.changedTouches[0].pageY : e.pageY;
        },
      move(e){

            var el = document.querySelector(".cropper-crop-box");
            if(el){
              var e = window.event || e;
              console.log(e);
              if(this.imgHeight < this.boxHeight){
                return;
              }
              var cutImg = document.querySelector(".tt");

              var pageY = e.changedTouches ? e.changedTouches[0].pageY : e.pageY;
              var delay = pageY - this.pageY;
              this.pageY = pageY;
              var top = this.el.offsetTop;
              top += delay;
              if(top - this.lowerlimit <= 0){
                top = this.lowerlimit;
              }else if(top - this.upperlimit + this.boxHeight >= 0){
                top = this.upperlimit - this.boxHeight;
              }
              this.el.style.top = top + "px";
              cutImg.style.marginTop = (-top) + "px";
              e.preventDefault()
            }
        },
        up(e){
          if(this.imgHeight < this.boxHeight){
            return;
          }
          var e = window.event || e;
        },
        submit(){
            let self = this;
          if(!this.el) {
            this.el = document.querySelector(".cropper-crop-box");
          }
            var  canvas = document.createElement("canvas");
          canvas.width  =  this.boxWidth;
          canvas.height  = this.boxHeight;
          var elImg = document.querySelector(".tt");
          var imgHeight = elImg.height;
          var im= document.createElement('img');
          im.src = elImg.src;
          var real_width  = im.width;
          var  real_height = im.height;
          var context = canvas.getContext("2d");
          var rate = real_height/imgHeight;
          console.log(rate,  self.el.offsetTop);
//          alert(canvas.height);
//          alert(real_width);
//          alert(real_height);
//          alert(imgHeight);
//          alert(self.el.offsetTop);
//          context.scale(0.5, 0.5);
          context.drawImage(elImg, 0, self.el.offsetTop * rate, real_width, canvas.height*rate, 0, 0, canvas.width, canvas.height);
          var type = this.$store.state.btype;
          var tempSrc = canvas.toDataURL(type);


          function dataURLtoBlob(dataurl) {
            var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
              bstr = atob(arr[1]), n = bstr.length, u8arr = new Array(n);
            while(n--){
              u8arr[n] = bstr.charCodeAt(n);
//              console.log(u8arr[n]);
            }
            return u8arr;
//        return new Blob([u8arr], {type:mime});
          }
         var at = dataURLtoBlob(tempSrc);
          var d = new Date().getTime();
          var generatedFile = new File(at, "Draft1.jpg", {type:"image/jpeg",lastModified: d});
          var fd = new FormData();
          fd.append("file", generatedFile);
//        this.mmmm = tempSrc;
        let params = {
            api:"/yg-user-service/user/uploadBanner/uploadFile.apec",
            data: fd

        }

        this.postImg(params);
        },
      postImg(params, fn){
            var self = this;
        try {
          api.postImg(params).then((res) => {
            var data = res.data;
            console.log(data);
            self.mmmm = data.bannerImgUrl;
//            fn(data);
          }).catch((error) => {
//            Toast("网络不好，请您重新上传图片");
            console.log(error)
          })
        } catch (error) {
          console.log(error)
        }
      },
    },
    activated(){
      this.src = this.$store.state.blob;
    },
    created(){
//      window.addEventListener('scroll', this.moveT, false);
    },

  }
</script>
