<template>
    <div class="c-z-roll">
       <div class="c-z-r-v-w sw">
          <div class="c-z-r-real vw">
             <div v-for="item in itemImg" class="c-z-c-img">
                <img :src="item.imageUrl +'?x-oss-process=style/_show'" class="c-z-r-img" ref="zj"><!--  -->
             </div>
          </div>
       </div>
       <div class="c-z-r-record">{{current}}/{{total}}</div>
    </div>
</template>
<style>
@import "../../../assets/css/rolling.css";
</style>
<script>
//import test1 from "../../../assets/img/icon/test1.jpg"
//import test2 from "../../../assets/img/icon/test2.jpg"
//import test3 from "../../../assets/img/icon/test3.jpg"
//import test4 from "../../../assets/img/icon/test4.jpg"
//import test5 from "../../../assets/img/icon/test5.jpg"
//import test6 from "../../../assets/img/icon/test6.jpg"
//var arr = [{imageUrl:test1},{imageUrl:test2},{imageUrl:test3},{imageUrl:test4},{imageUrl:test5},{imageUrl:test6}]
  export default{
      data(){
          return{
            itemImg:null,//图片数组
            fingerSPos:0,//手指初始的位置
            fingerNextP:0,//临时的位置记录
            isBackFlag:false,//是否是跳转功能
            width:0,//屏幕的宽度
            wrapPicW:0,//包裹图片的宽带
            marginLeft:0,//图片与图片的间距
            current:"",//当前页数
            total:"",//总共页数
            step:0,//左右距离
            moveFlag:true,//默认允许移动
            timeStart:0,//滑动开始的时间
            timeEnd:0,//滑动结束的时间
          }
      },
       methods:{
          timeInit(){
              //滑动结束后的初始化
             this.timeStart = 0;
             this.timeEnd = 0;
          },
          init(){
          },
         bs(property){
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
         translate(el, x,y) {
           var property = this.bs("Transform");
           if(property == false){
             alert("浏览器不存在此属性");
           }else{
             var adaptation = property+"Transform";
             el.style[adaptation] ='translate3d('+x +'px,'+ y +'px, 0)';
           }
         },
          reset(){
            this.fingerSPos = 0;
            this.isBackFlag = false;
            this.itemImg = null;
            this.moveFlag = true;
          },
          back(){
              this.reset();
              this.$router.go(-1);
          },
          start(e){
              var e = window.event || e;
             var touch = e.targetTouches[0];
             this.timeStart = e.timeStamp;
             this.fingerNextP = this.fingerSPos = touch.pageX;
            e.preventDefault();
          },
         move(e){
           var e = window.event || e;
           var touch = e.targetTouches[0];
           var target = e.target;
           var pos = touch.pageX;
           var delay = pos - this.fingerNextP;
           var el = document.querySelector(".c-z-r-real");
           if(this.current == 1){
             var xPos = 0;
               if(delay >= 0){
                 if(this.moveFlag){
                   var movePos = xPos + (pos - this.fingerSPos);
                   this.translate(el, movePos, 0);
                   if(pos - this.fingerSPos - this.marginLeft >= 0){
                     this.moveFlag = false;
                   }
                 }
               }else{
                 var movePos = xPos + (pos - this.fingerSPos);
                 this.translate(el, pos - this.fingerSPos, 0);
               }

           }else if(this.current == this.total){
              var xPosE = -(this.current-1) * this.wrapPicW;
               if(delay <= 0){
                 if(this.moveFlag){
                   var movePos = xPosE + (pos - this.fingerSPos);
                   this.translate(el, movePos, 0);
                   if(pos - this.fingerSPos + this.step <= 0){
                     this.moveFlag = false;
                   }
                 }
               }else{
                 var movePos = xPosE + (pos - this.fingerSPos);
                 this.translate(el, movePos, 0);
               }
           }
           else{
               var xPosM = -(this.current-1) * this.wrapPicW;
             var movePos = xPosM + (pos - this.fingerSPos);
             this.translate(el, movePos, 0);
           }

           this.fingerNextP = pos;
           this.isBackFlag = true;//判断是否跳转
           e.preventDefault();
         },
          end(e){
            var e = window.event || e;
            var touch = e.changedTouches[0];
            var pos = touch.pageX;
            var timeStart = this.timeStart;
            var timeEnd  = this.timeEnd = e.timeStamp;
            this.timeInit();
            var el = document.querySelector(".c-z-r-real")
            if(this.isBackFlag){
              this.moveFlag = true;
              this.isBackFlag = false;

              if((this.current == 1)&& (this.current == this.total)){
                this.translate(el, 0, 0);
                return ;
              }
              if(this.current == 1){

                if(((timeEnd - timeStart) < 500) && (pos - this.fingerSPos < 0)){
                  this.current ++;
                  var movePos = - (this.current - 1) * this.wrapPicW;
                  this.translate(el, movePos, 0);

                }else{
                  if(pos - this.fingerSPos > 0 || pos - this.fingerSPos > -this.width/2){
                    this.translate(el, 0, 0);
                  }else if(pos - this.fingerSPos < - this.width/2){
                    this.current ++;
                    var movePos = - (this.current - 1) * this.wrapPicW;
                    this.translate(el, movePos, 0);
                  }
                }

              }else if(this.current == this.total){
                if((timeEnd - timeStart) < 500 && pos - this.fingerSPos > 0){
                  this.current --;
                  var movePos = -(this.current - 1) * this.wrapPicW;
                  this.translate(el, movePos, 0);
                }else{
                  if(pos - this.fingerSPos < 0 ||  pos - this.fingerSPos <= this.width/2 ){
                    var movePos = -(this.current-1) * this.wrapPicW;
                    this.translate(el, movePos, 0);
                  }else if(pos - this.fingerSPos > this.width/2){
                    this.current --;
                    var movePos = -(this.current - 1) * this.wrapPicW;
                    this.translate(el, movePos, 0);
                  }
                }
              }else{

                  if((timeEnd - timeStart) < 500){
                      if(pos - this.fingerSPos > 0){
                        this.current --;
                        var movePos = -(this.current - 1) * this.wrapPicW;
                        this.translate(el, movePos, 0);
                      }else if(pos - this.fingerSPos < 0){
                        this.current ++;
                        var movePos = - (this.current - 1) * this.wrapPicW;
                        this.translate(el, movePos, 0);
                      }else{
                        var movePos = -(this.current - 1) * this.wrapPicW;
                        this.translate(el, movePos, 0)
                      }

                  }else{
                      //时间差大于50ms
                    if(Math.abs(pos - this.fingerSPos) < this.width/2){

                      var movePos = -(this.current - 1) * this.wrapPicW;
                      this.translate(el, movePos, 0)
                    }else if(pos - this.fingerSPos > this.width/2){

                      this.current --;
                      var movePos = -(this.current - 1) * this.wrapPicW;
                      this.translate(el, movePos, 0);
                      return;
                    }else if(pos - this.fingerSPos < - this.width/2){

                      this.current ++;
                      var movePos = - (this.current - 1) * this.wrapPicW;
                      this.translate(el, movePos, 0);
                      return;
                    }
                  }

//                  else if((this.timeEnd - this.timeStart) < 1000){
//
//                    return;
//                  }
              }

            }else{
              if(this.fingerSPos == pos){
                this.$router.go(-1);
              }
            }


            e.preventDefault();
          },
          setWidth(index){
              var imgArr = [];
              var xqImgArr = this.$store.state.xqImgArr;
              xqImgArr.forEach(function (current) {
              var strImg = current.imageUrl;
              var obj = {};
              var arr = strImg.split("/"),len = arr.length;


               if(arr[len - 1].indexOf("_") > 0){
                 arr.splice(1,1,"/");
                 arr.splice(-2,1);
                 var arrEle = arr.pop();
                 var pattern =/(_\d+)/g;
                 var t =  arrEle.replace(pattern,function (key, match) {
                   return "";
                 });
                 arr.push(t);
                 obj.imageUrl = arr.join("/");
               }else{
                 obj.imageUrl = strImg;
               }

                imgArr.push(obj);

            });
            this.itemImg =  imgArr;
              var len = this.itemImg.length;
              this.total = len;

            this.$nextTick(function () {
              var el = document.querySelector(".c-z-r-real");
              var elImg = this.$refs.zj[0];
              var imgw = elImg.offsetWidth,imgPleft = elImg.offsetLeft;

              this.marginLeft = imgPleft;
              var imgContentW = imgw+ imgPleft;
              this.wrapPicW = imgContentW ;
              el.style.width = imgContentW * len + imgPleft + "px";

              var movePos = - (index - 1) * this.wrapPicW;
              this.translate(el,movePos, 0);
              var step = el.offsetLeft;
              this.step = Math.abs(step);
              this.width = document.querySelector(".sw").offsetWidth;
            });


          }
       },
      mounted(){
        var self = this;
        var el = document.querySelector(".sw");

        el.addEventListener("touchstart",self.start,false);
        el.addEventListener("touchmove",self.move,false);
        el.addEventListener("touchend",self.end,false);
      },
      activated(){
        this.current = +this.$route.query.index + 1;
//        this.init();
        this.setWidth(this.current);

      }
  }
</script>
