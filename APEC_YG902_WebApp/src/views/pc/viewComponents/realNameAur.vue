<template>
  <div class="realNameAur-info-page">
    <scroller>
      <top-bar title="实名验证"></top-bar>
      <div class="main-page">
        <div class="r-v-form-cli">
          <div class="sell-l">
            <span>姓名：</span>
            <input v-model="name" class="input-name" oninvalid="setCustomValidity(' ')" type="text"
                   placeholder="请输入您的真实姓名"
                   oninput="setCustomValidity('')"/>
          </div>
        </div>
        <div class="r-v-form-cli">
          <div class="sell-l">
            <span>身份证号：</span>
            <input v-model="num" class="input-aur" oninvalid="setCustomValidity(' ')" type="text"
                   placeholder="请输入真实身份证号"
                   oninput="setCustomValidity('')"/>
          </div>
        </div>
        <split></split>
        <div class="aur-face-img">
          <!-- 放大图片 -->
          <!--<big-img v-if="showImg" @clickit="viewImg" :imgSrc="imgBigSrc"></big-img>-->
          <!--<file-load imgSize="10" :onChange="fileloadZM"></file-load>-->
          <div class="img-upload">
            <div class="upload">
              <div class="cli-btn">
                <i style="font-size: 25px;color: #8c939d;" class="fa fa-plus" aria-hidden="true"></i>
              </div>
              <input class="img-input" accept="image/*" type="file" @change="fileloadZM($event)">
            </div>
          </div>
          <div class="img-show">
            <!--@click="clickImg($event)"-->
            <img class="" :src="imgSrc">
            <span v-show="!imgSrc" style="font-size: 14px;">请点击左侧上传您的身份证正面照</span>
          </div>
        </div>
        <div class="aur-face-img">
         <!--<file-load imgSize="10" :onChange="fileloadFM"></file-load>-->
          <div class="img-upload">
            <div class="upload">
              <div class="cli-btn">
                <i style="font-size: 25px;color: #8c939d;" class="fa fa-plus" aria-hidden="true"></i>
              </div>
              <input class="img-input" accept="image/*" type="file" id="upload" @change="fileloadFM($event)">
            </div>
          </div>
          <div class="img-show">
            <img :src="imgSrc_">
            <span v-show="!imgSrc_" style="font-size: 14px;">请点击左侧上传您的身份证反面照</span>
          </div>
        </div>
        <!--<div v-show="_btnshow" class="login-btn"   @click="postImg">-->
          <!--提交-->
          <!--&lt;!&ndash;<input class="btn-login-c login-confirm" type="submit" id="btn-login-code" value="提交"&ndash;&gt;-->
               <!--&lt;!&ndash;&gt;</input>&ndash;&gt;-->
        <!--</div>-->

      </div>
    </scroller>
    <div @click="upload" v-show="_btnshow" class="login-btn-t">
      提交
    </div>
  </div>
</template>
<style>
@import "../../../assets/css/realP.css";
</style>
<script>
  import split from '../../../components/split/split'
  import topBar from '../../../components/topBar/topBar'
  import API from '../../../api/api'
  import c_js from '../../../assets/js/common'
  import fileLoad from '../../../components/common/uploadFile'
  import BigImg from '../../../components/common/bigImg';
  import {MessageBox, Toast, Indicator} from 'mint-ui';

  const api = new API();
  var ob = {
      contain:[],
      listen:function (param) {
          ob.contain.push(param)
      },
      publish:function () {
        var num = ob.contain.length;
        if(num == 2){
            ob.contains = [];
//            this.$router.go(-1);
        }
      }
  }
  export default {
    data() {
      return {
        showImg:false,
        imgBigSrc: '',
        name: '',
        num: '',
        imgSrc: '',
        imgSrc_: '',
        formData: new FormData(),
//        formDataS: new FormData(),
        fileloadZM:function (e){//身份证正面压缩回调函数
//            this.imgSrc=dtBase64;
          var target = e.target || e.srcElement;
          var file = target.files[0];
         var fd = new FormData();
          fd.append('file', file);
//          if(!this.formData.has('file'))
//          {
//            this.formData.append('file', file);
//          }else{
//            this.formData.set('file', file);
//          }

          this.uploadImg(fd, 0);
        }.bind(this),
        fileloadFM:function (e){//身份证反面压缩回调函数
          var target = e.target || e.srcElement;
          var file = target.files[0];
          var fd = new FormData();
          fd.append('file', file);
//          this.imgSrc_=dtBase64;
//          if(!this.formData.has('file'))
//          {
//            this.formData.append('file', file);
//          }else{
//            this.formData.set('file', file);
//          }

          this.uploadImg(fd, 1);
        }.bind(this)
      }
    },
    activated(){
      this.name = '',
        this.num = '',
        this.imgSrc = '',
        this.imgSrc_ = ''
    },
    computed:{
      _btnshow(){
        return this.name && this.num && this.imgSrc && this.imgSrc_;
      }
    },
    methods: {
      clickImg(e) {
          console.log(e)
        this.showImg = true;
        // 获取当前图片地址
        this.imgBigSrc = e.currentTarget.src;
      },
      viewImg(){
        this.showImg = false;
      },
      postImg()
      {
        const self = this;
        Indicator.open();
        if(!self.formData.has('idNumber'))
        {
          self.formData.append('idNumber', self.num);
        }else{
          self.formData.set('idNumber', self.num);

        }
        if(!self.formData.has('realName'))
        {
          self.formData.append('realName', self.name);

        }else{
          self.formData.set('realName', self.name);

        }
        self.upload();
      },
      upload(){
          const self = this;
        let params = {
          api: "/yg-user-service/user/userRealName.apec",
          data: {
            idNumber:self.num,
            realName:self.name,
            imgOneURL:self.imgSrc,
            imgTwoURL:self.imgSrc_
          }
        }

        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
              Indicator.close();
              self.$router.go(-1);
            }else{
              Indicator.close();
              Toast(item.errorMsg);
            }
          }).catch((error) => {
            Indicator.close();
//            self.$router.go(-1);
          })
        } catch (error) {
          Indicator.close();
//          self.$router.go(-1);
        }
      },
      uploadImg(fd, index){
       console.log(fd);
          const self = this;
        let params = {
          api:"/common/uploadImg.apec",
          data: fd
        }

        try {
          api.postImg(params).then((res) => {
            var item = res.data;
            if (item.succeed) {

                var dt = item.data[0][0];
                if(index == 0){
                  self.imgSrc=dt.imagePath;
                  Toast("正面身份提交成功~");
                }else if(index == 1){
                    self.imgSrc_ = dt.imagePath;
                  Toast("反面身份提交成功~");
                }
                ob.listen(0);
              ob.publish.bind(this)();
            } else {
              if(index == 0){
                Toast("正面身份提交失败:"+item.errorMsg);
              }else if(index == 1){
                Toast("反面身份提交失败:"+item.errorMsg);
              }
            }
            Indicator.close();
          }).catch((error) => {
              alert(123);
              console.log(error);
            Indicator.close();
            self.$router.go(-1);
          })
        } catch (error) {
          Indicator.close();
          self.$router.go(-1);
        }
      },
      compress(img,Orientation) {
        let canvas = document.createElement("canvas");
        let ctx = canvas.getContext('2d');
        //瓦片canvas
        let tCanvas = document.createElement("canvas");
        let tctx = tCanvas.getContext("2d");
        let initSize = img.src.length;
        let width = img.width;
        let height = img.height;
        //如果图片大于四百万像素，计算压缩比并将大小压至400万以下
        let ratio;
        if ((ratio = width * height / 4000000) > 1) {
          console.log("大于400万像素")
          ratio = Math.sqrt(ratio);
          width /= ratio;
          height /= ratio;
        } else {
          ratio = 1;
        }
        canvas.width = width;
        canvas.height = height;
        //        铺底色
        ctx.fillStyle = "#fff";
        ctx.fillRect(0, 0, canvas.width, canvas.height);
        //如果图片像素大于100万则使用瓦片绘制
        let count;
        if ((count = width * height / 1000000) > 1) {
          console.log("超过100W像素");
          count = ~~(Math.sqrt(count) + 1); //计算要分成多少块瓦片
          //            计算每块瓦片的宽和高
          let nw = ~~(width / count);
          let nh = ~~(height / count);
          tCanvas.width = nw;
          tCanvas.height = nh;
          for (let i = 0; i < count; i++) {
            for (let j = 0; j < count; j++) {
              tctx.drawImage(img, i * nw * ratio, j * nh * ratio, nw * ratio, nh * ratio, 0, 0, nw, nh);
              ctx.drawImage(tCanvas, i * nw, j * nh, nw, nh);
            }
          }
        } else {
          ctx.drawImage(img, 0, 0, width, height);
        }
        //修复ios上传图片的时候 被旋转的问题
        if(Orientation != "" && Orientation != 1){
          switch(Orientation){
            case 6://需要顺时针（向左）90度旋转
              this.rotateImg(img,'left',canvas);
              break;
            case 8://需要逆时针（向右）90度旋转
              this.rotateImg(img,'right',canvas);
              break;
            case 3://需要180度旋转
              this.rotateImg(img,'right',canvas);//转两次
              this.rotateImg(img,'right',canvas);
              break;
          }
        }
        //进行最小压缩
        let ndata = canvas.toDataURL('image/jpeg', 0.1);
        console.log('压缩前：' + initSize);
        console.log('压缩后：' + ndata.length);
        console.log('压缩率：' + ~~(100 * (initSize - ndata.length) / initSize) + "%");
        tCanvas.width = tCanvas.height = canvas.width = canvas.height = 0;
        return ndata;
      }
    },

    created() {
    },

    components: {
      split,
      fileLoad,
      topBar,
      BigImg
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  _rem = 20rem;
  .realNameAur-info-page
    position: fixed;
    top: 0;
    left: 0;
    height 100%;
    width 100%;
    font-size (15 /_rem)
    .r-v-form-cli:first-child
      border-bottom 1px solid #D7D7D7
    .r-v-form-cli
      height (30 /_rem)
      line-height (30 /_rem)
      padding 10px
      .sell-l
        span
          font-size (14 /_rem)
        input
          height (20 /_rem)
          font-size (15 /_rem)
          width 50%
    .aur-face-img
      padding 10px
      display: -moz-box;
      display: -webkit-box;
      display: box;
      .upload
        width (120 /_rem)
        height (120 /_rem)
        position: relative;
        -moz-box-flex: 1;
        -webkit-box-flex: 1;
        box-flex: 1;
        .cli-btn
          text-align: center
          line-height (120 /_rem)
          background-color: #fbfdff;
          border: 1px dashed #c0ccda;
          border-radius: 6px;
          box-sizing: border-box;
          width: (120 /_rem)
          height: (120 /_rem)
          cursor: pointer;
          vertical-align: top;
      .img-show
        -moz-box-flex: 3;
        -webkit-box-flex: 3;
        box-flex: 3;
        text-align center
        position relative
        img
          height (120 /_rem)
          width (200 /_rem)
        span
          position absolute
          top 50%
          left 50%
          transform translate(-50%, -50%)
          width (150 /_rem)
      .img-input
        width (120 /_rem)
        height (120 /_rem)
        position: absolute;
        top: 0;
        left: 0;
        opacity: 0;
        display: block;

    .login-btn
      margin (35 /_rem) (15 /_rem) 0 (15 /_rem)
      .btn-login-c
        color #ffffff
        line-height (15 /_rem)
        height (40 /_rem)
        border-radius: 5px;
        display: inline-block;
        width: 100%;
      .login-confirm
        background-color: #28CBA7;
        color #FFFFFF;
        border: 1px solid #0bbe06;

</style>
