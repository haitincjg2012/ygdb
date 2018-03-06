/**
* Created on 2017/7/29.
* 协议窗口 弹窗
*/
<template>
  <div>
    <transition name="rodal">
      <div>
        <div class="rodal-fade-enter" v-show="show" id="buyWindow" style="animation-duration:300ms;">
          <div class="minerWindow" ref="proWindow" @click="hideWindow"></div>
          <div class="minerWinContent rodal-zoom-enter" style="animation-duration:300ms;">
            <div v-html="content" style="font-size: 14px;padding:5px;line-height: 25px">

            </div>
            <span class="fa fa-times close" @click="hideWindow"></span>
            <a @click.stop="hideWindow" class="applyBtn">确认协议</a>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>
<script>
  import API from '../../../api/api'
  const api = new API();
  export default{
    created(){
      const self = this;
      this.$root.eventHub.$on('windowEvent',function(){
        self.show=true;
      });
    },
    mounted(){

    },
    data(){
      return {
        show: false,//认购窗口显示/隐藏标志,
        title:'',
        content:''
      }
    },
    mounted(){
      this.getAur();
      var that = this;
      var el = that.$refs.proWindow;
      el.addEventListener("touchmove",function (e) {
        e.preventDefault();
      }, false)
    },
    computed: {

    },
    methods: {
      getAur(){
        const self = this;
        let params = {
          api: "/yg-cms-service/cms/articleList.apec",
          data: {
            "category":"DECLARATION",
            "channelCode":"ZCXY"
          }
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            let data = item.data[0];
            this.content = data.content;
            this.title = data.title;
          }).catch((error) => {
            console.log(error)
          })
        } catch (error) {
          console.log(error)
        }
      },
      hideWindow(){
        this.show = false;
      }
    },
    watch:{
      showTag:function (oldVal,newVal) {
        this.show = oldVal;
      }
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  _rem = 20rem;
  .minerWindow
    z-index: 99;
    position: fixed;
    left: 0;
    top: 0;
    bottom: 0;
    right: 0;
    background-color: #000000;
    width: 100%;
    height: 100%;
    opacity: 0.5;
  .minerWinContent
    text-align: center;
    z-index: 999;
    background-color: #fff;
    padding: 0.68266667rem 1.152rem 1.92rem;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    border-radius: 0.32rem;
    height: (500/_rem);
    width: (300/_rem);
    overflow-y: auto;
    .close
      font-size: 1.2rem !important;
      color: #28cba7;
      width: 0.768rem;
      height: 0.768rem;
      position: fixed;
      top: 0.68266667rem;
      right: 0.68266667rem;
    .minerTit
      span
        margin-left: 0.42666667rem;
    .minerTitMoney
      span
        position: absolute;
        right: 0;
        color: #000;
    .remark
      span
        margin-right: 0.29866667rem;
        color: #ffcb3c;
    .applyBtn
      position: absolute;
      left: 0;
      padding: 0.55466667rem 0;
      width: 100%;
      font-size: 0.704rem !important;
      background-color: #28cba7;
      color: #fff;
      display: inline-block;
      white-space: nowrap;
      letter-spacing: 0.08533333rem;

</style>

<style scoped>
  @-webkit-keyframes rodal-fade-leave {
    to {
      opacity: 0
    }
  }

  @keyframes rodal-fade-leave {
    to {
      opacity: 0
    }
  }

  .rodal-fade-leave {
    -webkit-animation: rodal-fade-leave both ease-out;
    animation: rodal-fade-leave both ease-out
  }

  @-webkit-keyframes rodal-fade-enter {
    0% {
      opacity: 0
    }
  }

  @keyframes rodal-fade-enter {
    0% {
      opacity: 0
    }
  }

  .rodal-fade-enter {
    -webkit-animation: rodal-fade-enter both ease-in;
    animation: rodal-fade-enter both ease-in
  }

  @-webkit-keyframes rodal-zoom-enter {
    0% {
      -webkit-transform: scale3d(.3, .3, .3);
      transform: scale3d(.3, .3, .3)
    }
  }

  @keyframes rodal-zoom-enter {
    0% {
      -webkit-transform: scale3d(.3, .3, .3);
      transform: scale3d(.3, .3, .3)
    }
  }

  .rodal-zoom-enter {
    -webkit-animation: rodal-zoom-enter both cubic-bezier(.4, 0, 0, 1.5);
    animation: rodal-zoom-enter both cubic-bezier(.4, 0, 0, 1.5)
  }

  @-webkit-keyframes rodal-zoom-leave {
    to {
      -webkit-transform: scale3d(.3, .3, .3);
      transform: scale3d(.3, .3, .3)
    }
  }

  @keyframes rodal-zoom-leave {
    to {
      -webkit-transform: scale3d(.3, .3, .3);
      transform: scale3d(.3, .3, .3)
    }
  }

  .rodal-zoom-leave {
    -webkit-animation: rodal-zoom-leave both;
    animation: rodal-zoom-leave both
  }

</style>
