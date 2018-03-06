<template>
  <div v-show="showHideF">
    <my-mask :pageflag="sh2" class="c-wxMask">
       <div class="c-wxShare">
          <my-imgLayout :imgArr="arrImg" :textFlag ="textFlag" class="c-wxShare-img" @goWx="goShare"></my-imgLayout>
         <div class="c-wxShare-line"></div>
         <div class="c-wxShare-cancel" @click="cancel">
           取消
         </div>
       </div>
    </my-mask>
  </div>
</template>
<style>
  @import "../assets/css/share.css";
</style>
<script>
  import WX from './wx.vue' //微信分享内容
  import maskC from './mask.vue'//朦层
  import imgLayout from './pictureLayout.vue'
//  import wx from '../assets/img/wx.png'
//  import freind from '../assets/img/friend.png'
  export default{
      data(){
          return{
//            arrImg:[{imgUrl:wx, text:"朋友",flag:"wx1"},{imgUrl:freind, text:"朋友圈",flag:"wx2"}],
            arrImg:[],
            textFlag:true,
            sh2:true,
          }
      },
      methods:{
        cancel(){
            var obj = {
                flag:0
            }
            this.$emit("shareH", obj);
        },
        goShare(obj){
            var title = this.tile;
          if(obj.flag == "wx1"){
              //朋友
              WX.wxline(title)
          }else{
              //朋友圈
             WX.wxMessage(title)
          }
        }
      },
      props:['showHideF','title'],
      components:{
        'my-mask':maskC,
        'my-imgLayout':imgLayout
      }
  }
</script>
