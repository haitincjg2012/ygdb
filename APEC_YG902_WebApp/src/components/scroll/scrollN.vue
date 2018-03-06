<template>
  <div>
    <div>
      <Animation ref="pullDown" v-show="pullDown"></Animation>
      <slot></slot>
      <Animation ref="pullUpC" v-show="pullup"></Animation>
    </div>
  </div>
</template>
<script>
import Animation from '../../components/loadingAnimation.vue'
  export default{
      data(){
        return{
          myScroll:null,
          pullDownEl:null,
          pullUpEl:null,
          el:null,
        }
      },
      components:{
        Animation
      },
      props:['pullDown', 'pullup'],
      methods:{
          init(){
            console.log(this.$el, 8888);
            this.el = this.$el;

            var self = this;
            var timeId= 0;
            timeId = setInterval(function () {
              if(document.readyState == "complete"){
                clearInterval(timeId)
                setTimeout(() => {
                  self.loaded()
                }, 100)
              }
            }, 500)
          },
        loaded:function () {

//          var pullDownEl = this.$refs.pullDown.$el;
//          var pullDownOffset = pullDownEl.offsetHeight;

          var pullUpC = this.$refs.pullUpC;
          var pullUpEl =  this.$refs.pullUpC.$el;
          var pullUpOffset = pullUpEl.offsetHeight;

          var el = this.el;

          this.myScroll = new window.iScroll(el,{
            scrollbarClass: 'myScrollbar', /* 重要样式 */
            useTransition: false, /* 此属性不知用意，本人从true改为false */
//            topOffset: pullDownOffset,
//                topOffset:0,
            onRefresh: function () {
                console.log("fresh");
                if(pullUpC){
                  pullUpC.init();
                }
            },
            onScrollMove: function () {
                if(this.y < (this.maxScrollY - 5)){
//                    if(pullUpC){
//                      pullUpC.move();
//                    }

                }
            },
            onScrollEnd: function () {
                if(pullUpC){
                  pullUpC.start();
                }
            },
            onCompensate:function () {
              //第一次快速滑动的时候
//
//              if(pullUpC){
//                pullUpC.move();
//              }
            }
          });
        }
      },
      activated(){
          console.log(this);
      }
  }
</script>
