//九宫格的布局
//secondSq:四张图片的布局,threeSq:其他图片的布局
//一张图片与多张图片的大小是不一样的
<template>
  <div class="c-square"  :class="{secondSq:secondSF,threeSq:threeSF}">
      <div v-for="item in imgArr"  :class="{first:firstSF, three:threeSF}" @click="imgEvent(item)">
          <img :src="item.imgUrl + type" :class="{imgPicCom:threeSF }" >
          <div v-if="textFlag" :class="{wxShare:textFlag}">{{item.text}}</div>
      </div>
      <div v-if="blankDeal"  :class="{three:threeSF}"></div>
  </div>
</template>
<style>
@import "../assets/css/square.css";
</style>
<script>
  export default{
      data(){
          return {
          }
      },
    methods:{
       imgEvent(item){
           var flag = item.flag;
           if(flag== "wx1"){
               var obj = {flag:"wx1"}
               this.$emit('goWx',obj);
           }else if(flag == "wx2"){
             var obj = {flag:"wx2"}
             this.$emit('goWx', obj);
           }
       }
     },
      props:["imgArr", 'lengthN','textFlag','type'],
     activated(){

     },
    watch:{
         "imgArr":function () {
             var self = this;
             var imgArr = self.imgArr;

             if(imgArr){
                 //用于测试img的是否加载完成
                 var time = 0;

                 for(var i = 0, len = imgArr.length; i < len; i ++){
                   (function (index) {
//                     var img = document.createElement("img");
                     var img = new Image();
                     img.onload = function () {
                       time ++ ;
                        if(time == len){
//console.log("test");
                          self.$emit("receivedImg", true);
                        }
                     }
                     img.src = imgArr[index].imgUrl + self.type;
                   })(i);
                 }
             }
//
         }
    },
    computed:{
      firstSF(){
        var self = this;
        if(self.lengthN == 1){
          return true;
        }else{
          return false;
        }
      },
      secondSF(){
        var self = this;
        if(self.lengthN == 4){
          return true;
        }else{
          return false;
        }
      },
      threeSF(){
        var self = this;
        if(self.lengthN == 1){
          return false;
        }else{
          return true;
        }
      },
      blankDeal(){
        var self = this;
        if(self.lengthN == 5 || self.lengthN == 8 || self.lengthN == 2){
          return true;
        }else{
          return false;
        }
      }
    },
  }
</script>
